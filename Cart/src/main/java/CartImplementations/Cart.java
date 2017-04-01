 package CartImplementations;

import java.net.SocketTimeoutException;
import java.util.HashMap;

import com.google.inject.Inject;

import BasicCommonClasses.CartProduct;
import BasicCommonClasses.CatalogProduct;
import BasicCommonClasses.GroceryList;
import BasicCommonClasses.Login;
import BasicCommonClasses.ProductPackage;
import BasicCommonClasses.SmartCode;
import CartContracts.ICart;
import CartContracts.ACartExceptions.ProductNotInCart;
import CartContracts.ACartExceptions.ProductPackageDoesNotExist;
import ClientServerApi.CommandDescriptor;
import ClientServerApi.CommandWrapper;
import CommonDefs.GroceryListExceptions.AmountIsBiggerThanAvailable;
import CommonDefs.GroceryListExceptions.ProductNotInList;
import UtilsContracts.IClientRequestHandler;
import CartContracts.ACartExceptions.AmountBiggerThanAvailable;
import CartContracts.ACartExceptions.AuthenticationError;
import CartContracts.ACartExceptions.CartNotConnected;
import CartContracts.ACartExceptions.CriticalError;
import CartContracts.ACartExceptions.GroceryListIsEmpty;
import CartContracts.ACartExceptions.InvalidCommandDescriptor;
import CartContracts.ACartExceptions.InvalidParameter;
import CartContracts.ACartExceptions.ProductCatalogDoesNotExist;
import UtilsImplementations.Serialization;
/**
 * Cart class represents shopping cart in the SmartMarket.
 * 
 * @author Lior Ben Ami, Aviad Cohen
 * @since 2017-01-02
 */
public class Cart extends ACart implements ICart {
	@Inject
	public Cart(IClientRequestHandler clientRequestHandler) {
		this.clientRequestHandler = clientRequestHandler;
	}
	
	GroceryList groceryList = new GroceryList();
	/**
	 * inner cache, Key: barcode, Value: CartProduct
	 */
	HashMap<Long, CartProduct> cartProductCache = new HashMap<Long, CartProduct>();
	
	Double totalSum = Double.valueOf(0);
	
	Integer totalProductsAmount = 0;

	private int loadCartProductsAmount() {
		int count = 0;
		for(HashMap.Entry<Long, CartProduct> entry : cartProductCache.entrySet())
			count += entry.getValue().getTotalAmount();
		return count;
	}

	private void loadCartProductCacheAndUpdateCartData() throws CriticalError, CartNotConnected, 
		ProductCatalogDoesNotExist {
		log.info("restorying grocery list from server.");
		
		for (HashMap.Entry<SmartCode, ProductPackage> ¢ : groceryList.getList().entrySet())
			addProductToCacheAndUpdateCartData(¢.getValue(), viewCatalogProduct(¢.getKey()));
		loadCartProductsAmount();
	}
	
	private void addProductToCacheAndUpdateCartData(ProductPackage p, CatalogProduct catalogProduct) {
		CartProduct cartProduct = cartProductCache.get(p.getSmartCode().getBarcode());
		
		/* No packages from this CartProduct, adding new one */
		if (cartProduct == null) 
			cartProduct = new CartProduct(catalogProduct, new HashMap<SmartCode, ProductPackage>(), 0);
		
		cartProduct.addProductPackage(p);
		
		cartProductCache.put(catalogProduct.getBarcode(), cartProduct);
		totalProductsAmount += p.getAmount();
		totalSum += p.getAmount() * catalogProduct.getPrice();
	}
	
	@Override
	public int getId() {
		return id;
	}

	@Override
	public HashMap<Long, CartProduct> getCartProductCache() {
		return cartProductCache;
	}

	@Override
	public void login(String username, String password) throws CriticalError, AuthenticationError {
		CommandWrapper $ = null;
		String serverResponse = null;
		
		log.info("Creating login command wrapper for cart");
		
		establishCommunication(CartDefs.port, CartDefs.host, CartDefs.timeout);
		
		try {
			serverResponse = sendRequestWithRespondToServer((new CommandWrapper(CartDefs.loginCommandSenderId,
					CommandDescriptor.LOGIN, Serialization.serialize(new Login(username, password)))).serialize());
		} catch (SocketTimeoutException e) {
			log.fatal("Critical bug: failed to get respond from server");
			
			throw new CriticalError();
		}
		
		terminateCommunication();	
		
		$ = CommandWrapper.deserialize(serverResponse);
		
		try {
			resultDescriptorHandler($.getResultDescriptor());
		} catch (InvalidCommandDescriptor | InvalidParameter | CartNotConnected | ProductCatalogDoesNotExist |
				AmountBiggerThanAvailable | 	ProductPackageDoesNotExist | GroceryListIsEmpty ¢) {
			log.fatal("Critical bug: this command result isn't supposed to return here");
			
			¢.printStackTrace();
			
			throw new CriticalError();
		}
		
		id = $.getSenderID();
		this.username = username;
		this.password = password;
		
		log.info("Cart Login to server as succeed. Client id is: " + id);
	}
	
	@Override
	public void logout() throws CartNotConnected, CriticalError {
		String serverResponse;		
		
		log.info("Creating cart logout command wrapper with id: " + id);
		
		establishCommunication(CartDefs.port, CartDefs.host, CartDefs.timeout);

		try {
			serverResponse = sendRequestWithRespondToServer(
					(new CommandWrapper(id, CommandDescriptor.LOGOUT))
							.serialize());
		} catch (SocketTimeoutException e) {
			log.fatal("Critical bug: failed to get respond from server");
			
			throw new CriticalError();
		}
		
		terminateCommunication();
		
		try {
			resultDescriptorHandler(CommandWrapper.deserialize(serverResponse).getResultDescriptor());
		} catch (InvalidCommandDescriptor | InvalidParameter | ProductCatalogDoesNotExist |
				 AmountBiggerThanAvailable | 	ProductPackageDoesNotExist | GroceryListIsEmpty | AuthenticationError ¢) {
			log.fatal("Critical bug: this command result isn't supposed to return here");
			
			¢.printStackTrace();
			
			throw new CriticalError();
		}
		
		log.info("logout from server succeed.");
	}
	
	@Override
	public void resume(int _id) throws CriticalError, CartNotConnected, ProductCatalogDoesNotExist {
		CommandWrapper $ = null;
		String serverResponse;
		
		log.info("Creating cart Load grocery list command wrapper with id: " + id);
		
		establishCommunication(CartDefs.port, CartDefs.host, CartDefs.timeout);
		
		try {
			serverResponse = sendRequestWithRespondToServer(
					(new CommandWrapper(id, CommandDescriptor.LOAD_GROCERY_LIST))
							.serialize());
		} catch (SocketTimeoutException e) {
			log.fatal("Critical bug: failed to get respond from server");
			
			throw new CriticalError();
		}
		
		terminateCommunication();
		
		try {
			$ = CommandWrapper.deserialize(serverResponse);
			
			resultDescriptorHandler($.getResultDescriptor());
			
			id = _id;
			groceryList = Serialization.deserialize($.getData(), GroceryList.class);
		} catch (InvalidCommandDescriptor | InvalidParameter |
				 AmountBiggerThanAvailable | 	ProductPackageDoesNotExist | GroceryListIsEmpty | AuthenticationError ¢) {
			log.fatal("Critical bug: this command result isn't supposed to return here");
			
			¢.printStackTrace();
			
			throw new CriticalError();
		}
		
		/* Restoring Grocery list */
		loadCartProductCacheAndUpdateCartData();
		
		log.info("load grocery list from server succeed.");
	}
	
	@Override
	public void addProductToCart(SmartCode c, int amount) throws CriticalError, CartNotConnected,
			AmountBiggerThanAvailable, ProductPackageDoesNotExist, ProductCatalogDoesNotExist {
		String serverResponse;
		
		log.info("Creating viewProductFromCatalog (in order to addPtoductToCart) command wrapper to cart with id: " + id);

		CatalogProduct catalogProduct = viewCatalogProduct(c);
		
		establishCommunication(CartDefs.port, CartDefs.host, CartDefs.timeout);
		
		try {
			serverResponse = sendRequestWithRespondToServer(
					(new CommandWrapper(id, CommandDescriptor.ADD_PRODUCT_TO_GROCERY_LIST,
							Serialization.serialize(new ProductPackage(c, amount, null))).serialize()));
		} catch (SocketTimeoutException e) {
			log.fatal("Critical bug: failed to get respond from server");
			
			throw new CriticalError();
		}
		
		terminateCommunication();
				
		try {
			resultDescriptorHandler(CommandWrapper.deserialize(serverResponse).getResultDescriptor());
		} catch (InvalidCommandDescriptor | InvalidParameter |
				 ProductCatalogDoesNotExist |
				GroceryListIsEmpty | AuthenticationError ¢) {
			log.fatal("Critical bug: this command result isn't supposed to return here");
			
			¢.printStackTrace();
			
			throw new CriticalError();
		}
				
		ProductPackage productPackage1 = new ProductPackage(c, amount, null);
		ProductPackage productPackage2 = new ProductPackage(c, amount, null);

		groceryList.addProduct(productPackage1);
		
		addProductToCacheAndUpdateCartData(productPackage2, catalogProduct);
		
		log.info("addProductToGroceryList command succeed.");
	}
	
	@Override
	public void returnProductToShelf(SmartCode c, int amount) throws ProductNotInCart, 
			AmountBiggerThanAvailable, ProductPackageDoesNotExist, CriticalError, CartNotConnected {
		String serverResponse;
		
		log.info("Creating REMOVE_PRODUCT_FROM_GROCERY_LIST command wrapper to cart with id: " + id);
		
		establishCommunication(CartDefs.port, CartDefs.host, CartDefs.timeout);
		
		try {
			serverResponse = sendRequestWithRespondToServer(
					(new CommandWrapper(id, CommandDescriptor.REMOVE_PRODUCT_FROM_GROCERY_LIST,
							Serialization.serialize(new ProductPackage(c, amount, null))).serialize()));
		} catch (SocketTimeoutException e) {
			log.fatal("Critical bug: failed to get respond from server");
			
			throw new CriticalError();
		}
		
		terminateCommunication();
		
		try {
			resultDescriptorHandler(CommandWrapper.deserialize(serverResponse).getResultDescriptor());
		} catch (InvalidCommandDescriptor | InvalidParameter | ProductCatalogDoesNotExist |
				GroceryListIsEmpty | AuthenticationError ¢) {
			log.fatal("Critical bug: this command result isn't supposed to return here");
			
			¢.printStackTrace();
			
			throw new CriticalError();
		}

		/* update cart data: groceryList, cartProductCache, totalSum */
		if (groceryList == null) 
			throw new CriticalError();
		
		try {
			groceryList.removeProduct(new ProductPackage(c, amount, null));
		} catch (ProductNotInList | AmountIsBiggerThanAvailable ¢) {
			throw new CriticalError();
		}
		
		long barcode = c.getBarcode();
		
		CartProduct cartProduct =  cartProductCache.get(barcode);
		ProductPackage productPackage = new ProductPackage(c, amount, null);
		
		cartProduct.removeProductPackage(productPackage);
		
		if (cartProduct.getTotalAmount() <= 0)
			cartProductCache.remove(barcode);
		else
			cartProductCache.put(barcode, cartProduct);
		
		totalProductsAmount -= amount;
		totalSum -= amount * cartProduct.getCatalogProduct().getPrice();
		
		log.info("REMOVE_PRODUCT_FROM_GROCERY_LIST command succeed.");
	}
	
	@Override
	public Double getTotalSum() {
		return totalSum;
	}
	
	@Override
	public Integer getCartProductsNum() {
		return totalProductsAmount;
	}
	
	@Override
	public Double checkOutGroceryList() throws CriticalError, CartNotConnected, GroceryListIsEmpty {
		String serverResponse;
		Double $ = totalSum;
		
		log.info("Creating CHECKOUT_GROCERY_LIST command wrapper to cart with id: " + id);
		
		establishCommunication(CartDefs.port, CartDefs.host, CartDefs.timeout);

		try {
			serverResponse = sendRequestWithRespondToServer(
					(new CommandWrapper(id, CommandDescriptor.CHECKOUT_GROCERY_LIST)).serialize());
		} catch (SocketTimeoutException e) {
			log.fatal("Critical bug: failed to get respond from server");
			
			throw new CriticalError();
		}
		
		terminateCommunication();
		
		try {
			resultDescriptorHandler(CommandWrapper.deserialize(serverResponse).getResultDescriptor());
		} catch (InvalidCommandDescriptor | InvalidParameter |
				 AmountBiggerThanAvailable | ProductPackageDoesNotExist | 
				 AuthenticationError | ProductCatalogDoesNotExist ¢) {
			log.fatal("Critical bug: this command result isn't supposed to return here");
			
			¢.printStackTrace();
			
			throw new CriticalError();
		}

		/* update cart data: groceryList, cartProductCache, totalSum */
		groceryList = new GroceryList();
		cartProductCache = new HashMap<Long, CartProduct>();
		totalSum = Double.valueOf(0);
		totalProductsAmount = 0;
		log.info("CHECKOUT_GROCERY_LIST command succeed.");
		
		return $;
	}

	@Override
	public CartProduct getCartProduct(SmartCode ¢) {
		return cartProductCache.get(¢.getBarcode());
	}
	
	@Override
	public CatalogProduct viewCatalogProduct(SmartCode c) throws CriticalError, CartNotConnected,
			ProductCatalogDoesNotExist {
		String serverResponse;
		
		log.info("Creating viewProductFromCatalog (in order to addPtoductToCart) command wrapper to cart with id: " + id);

		establishCommunication(CartDefs.port, CartDefs.host, CartDefs.timeout);
		
		/* first: getting the product from the server */
		try {
			serverResponse = sendRequestWithRespondToServer(
					(new CommandWrapper(id, CommandDescriptor.VIEW_PRODUCT_FROM_CATALOG,
						Serialization.serialize(c)).serialize()));
		} catch (SocketTimeoutException e) {
			log.fatal("Critical bug: failed to get respond from server");
			
			throw new CriticalError();
		}
		
		terminateCommunication();
		
		CommandWrapper $ = CommandWrapper.deserialize(serverResponse);
		
		try {
			resultDescriptorHandler($.getResultDescriptor());
		} catch (InvalidCommandDescriptor | InvalidParameter| CriticalError |
				 AmountBiggerThanAvailable | ProductPackageDoesNotExist | 
				GroceryListIsEmpty | AuthenticationError ¢) {
			log.fatal("Critical bug: this command result isn't supposed to return here");
			
			¢.printStackTrace();
			
			throw new CriticalError();
		}
		
		log.info("viewProductFromCatalog command succeed.");
		
		return Serialization.deserialize($.getData(), CatalogProduct.class);
	}
	
	@Override
	public void removeAllItemsOfCartProduct(SmartCode c) throws ProductNotInCart,
			CriticalError {
		CartProduct cartProduct = getCartProduct(c);
		
		if (cartProduct == null)
			throw new ProductNotInCart();
		
		for( HashMap.Entry<SmartCode, ProductPackage> entry : cartProduct.getPackages().entrySet())
			try {
				SmartCode smartCode = entry.getKey();
				int amount = entry.getValue().getAmount();
				returnProductToShelf(smartCode, amount);
			} catch (AmountBiggerThanAvailable | ProductPackageDoesNotExist | CriticalError | CartNotConnected e) {
				throw new CriticalError();
			}
	}
}
