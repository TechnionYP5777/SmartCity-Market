 package CartImplemantations;

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
 * @author Lior Ben Ami
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

	private void loadCartProductCacheAndUpdateTotalSum() throws CriticalError, CartNotConnected, ProductCatalogDoesNotExist {
		for (HashMap.Entry<SmartCode, ProductPackage> entry : groceryList.getList().entrySet()) {
			ProductPackage productPackage = entry.getValue();
			CatalogProduct catalogProduct = viewCatalogProduct(entry.getKey());
			addProductToCache(productPackage,catalogProduct);
			
		}
	}
	
	private void addProductToCache(ProductPackage productPackage, CatalogProduct catalogProduct) {
		CartProduct cartProduct = cartProductCache.get(productPackage.getSmartCode().getBarcode());
		if (cartProduct == null) 
			cartProduct = new CartProduct(catalogProduct, new HashMap<SmartCode,ProductPackage>(), 0);
		cartProduct.addProductPackage(productPackage);
		cartProductCache.put(catalogProduct.getBarcode(), cartProduct);
		totalSum += productPackage.getAmount() * catalogProduct.getPrice();
	}
	
	public int getId() {
		return id;
	}

	public HashMap<Long, CartProduct> getCartProductCache() {
		return cartProductCache;
	}

	public void login(String username, String password) throws CriticalError, AuthenticationError {
		CommandWrapper $ = null;
		log.info("Creating login command wrapper for cart");
		establishCommunication(CartDefs.port, CartDefs.host, CartDefs.timeout);
		String serverResponse = null;
		try {
			serverResponse = sendRequestWithRespondToServer((new CommandWrapper(CartDefs.loginCommandSenderId,
					CommandDescriptor.LOGIN, Serialization.serialize(new Login(username, password)))).serialize());
		} catch (SocketTimeoutException e) {
			log.fatal("Critical bug: failed to get respond from server");
			throw new CriticalError();
		}
		terminateCommunication();	
		try {
			$ = CommandWrapper.deserialize(serverResponse);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		try {
			resultDescriptorHandler($.getResultDescriptor());
		} catch (InvalidCommandDescriptor | InvalidParameter | CartNotConnected | ProductCatalogDoesNotExist |
				ProductNotInCart | AmountBiggerThanAvailable | 	ProductPackageDoesNotExist | GroceryListIsEmpty ¢) {
			log.fatal("Critical bug: this command result isn't supposed to return here");
			¢.printStackTrace();
		}
		id = $.getSenderID();
		this.username = username;
		this.password = password;
		log.info("Cart Login to server as succeed. Client id is: " + id);
	}
	
	public void logout() throws CartNotConnected, CriticalError {
		establishCommunication(CartDefs.port, CartDefs.host, CartDefs.timeout);
		log.info("Creating cart logout command wrapper with id: " + id);
		String serverResponse;
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
				ProductNotInCart | AmountBiggerThanAvailable | 	ProductPackageDoesNotExist | GroceryListIsEmpty | AuthenticationError ¢) {
			log.fatal("Critical bug: this command result isn't supposed to return here");
			¢.printStackTrace();
		}
		log.info("logout from server succeed.");
	}
	
	public void resume(int _id) throws CriticalError, CartNotConnected, ProductCatalogDoesNotExist {
		CommandWrapper $ = null;
		establishCommunication(CartDefs.port, CartDefs.host, CartDefs.timeout);
		log.info("Creating cart Load grocery list command wrapper with id: " + id);
		String serverResponse;
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
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		try {
			resultDescriptorHandler(CommandWrapper.deserialize(serverResponse).getResultDescriptor());
		} catch (InvalidCommandDescriptor | InvalidParameter |
				ProductNotInCart | AmountBiggerThanAvailable | 	ProductPackageDoesNotExist | GroceryListIsEmpty | AuthenticationError ¢) {
			log.fatal("Critical bug: this command result isn't supposed to return here");
			¢.printStackTrace();
		}
		id = _id;
		groceryList = Serialization.deserialize($.getData(), GroceryList.class);
		loadCartProductCacheAndUpdateTotalSum();
		log.info("load grocery list from server succeed.");
	}
	
	public void addPtoductToCart(SmartCode c, int amount) throws CriticalError, CartNotConnected, AmountBiggerThanAvailable, ProductPackageDoesNotExist, ProductCatalogDoesNotExist {
		CatalogProduct catalogProduct = viewCatalogProduct(c);
		establishCommunication(CartDefs.port, CartDefs.host, CartDefs.timeout);
		log.info("Creating viewProductFromCatalog (in order to addPtoductToCart) command wrapper to cart with id: " + id);
		String serverResponse;
		CommandWrapper commandWrapper = null;
		try {
			serverResponse = sendRequestWithRespondToServer(
					(new CommandWrapper(id, CommandDescriptor.ADD_PRODUCT_TO_GROCERY_LIST,
							Serialization.serialize(new ProductPackage(c, amount, null))).serialize()));
		} catch (SocketTimeoutException e) {
			log.fatal("Critical bug: failed to get respond from server");
			throw new CriticalError();
		}
		terminateCommunication();
		commandWrapper = CommandWrapper.deserialize(serverResponse);
		try {
			resultDescriptorHandler(commandWrapper.getResultDescriptor());
		} catch (InvalidCommandDescriptor | InvalidParameter |
				ProductNotInCart | ProductCatalogDoesNotExist |
				GroceryListIsEmpty | AuthenticationError ¢) {
			log.fatal("Critical bug: this command result isn't supposed to return here");
			¢.printStackTrace();
		}
		log.info("addProductToGroceryList command succeed.");
		ProductPackage productPackage = new ProductPackage(c, amount, null);
//		if (groceryList == null) 
//			groceryList = new GroceryList();
		groceryList.addProduct(productPackage);
		addProductToCache(productPackage, catalogProduct);
	}
	
	public void returnProductToShelf(SmartCode smartCode, int amount) throws ProductNotInCart, AmountBiggerThanAvailable, ProductPackageDoesNotExist, CriticalError, CartNotConnected {
		establishCommunication(CartDefs.port, CartDefs.host, CartDefs.timeout);
		log.info("Creating REMOVE_PRODUCT_FROM_GROCERY_LIST command wrapper to cart with id: " + id);
		String serverResponse;
		CommandWrapper commandWrapper = null;
		try {
			serverResponse = sendRequestWithRespondToServer(
					(new CommandWrapper(id, CommandDescriptor.REMOVE_PRODUCT_FROM_GROCERY_LIST,
							Serialization.serialize(new ProductPackage(smartCode, amount, null))).serialize()));
		} catch (SocketTimeoutException e) {
			log.fatal("Critical bug: failed to get respond from server");
			throw new CriticalError();
		}
		terminateCommunication();
		commandWrapper = CommandWrapper.deserialize(serverResponse);
		try {
			resultDescriptorHandler(commandWrapper.getResultDescriptor());
		} catch (InvalidCommandDescriptor | InvalidParameter | ProductCatalogDoesNotExist |
				 AmountBiggerThanAvailable | ProductPackageDoesNotExist | 
				GroceryListIsEmpty | AuthenticationError ¢) {
			log.fatal("Critical bug: this command result isn't supposed to return here");
			¢.printStackTrace();
		}
		log.info("REMOVE_PRODUCT_FROM_GROCERY_LIST command succeed.");
		//update cart data: groceryList, cartProductCache, totalSum
		if (groceryList == null) 
			throw new ProductNotInCart();
		try {
			groceryList.removeProduct(new ProductPackage(smartCode, amount, null));
		} catch (ProductNotInList ¢) {
			throw new ProductNotInCart();
		} catch (AmountIsBiggerThanAvailable ¢) {
			throw new AmountBiggerThanAvailable();
		}
		long barcode = smartCode.getBarcode();
		CartProduct cartProduct =  cartProductCache.get(barcode);
		ProductPackage productPackage = new ProductPackage(smartCode, amount, null);
		cartProduct.removeProductPackage(productPackage);
		if (cartProduct.getTotalAmount()>0) {
			cartProductCache.put(barcode, cartProduct);
		}
		totalSum -= amount * cartProduct.getCatalogProduct().getPrice();
	}
	
	public Double getTotalSum() {
		return totalSum;
	}
	
	public Integer getCartProductsNum() {
		return cartProductCache.size();
	}
	
	public Double checkOutGroceryList() throws CriticalError, CartNotConnected {
		establishCommunication(CartDefs.port, CartDefs.host, CartDefs.timeout);
		log.info("Creating CHECKOUT_GROCERY_LIST command wrapper to cart with id: " + id);
		String serverResponse;
		CommandWrapper commandWrapper = null;
		try {
			serverResponse = sendRequestWithRespondToServer(
					(new CommandWrapper(id, CommandDescriptor.CHECKOUT_GROCERY_LIST)).serialize());
		} catch (SocketTimeoutException e) {
			log.fatal("Critical bug: failed to get respond from server");
			throw new CriticalError();
		}
		terminateCommunication();
		commandWrapper = CommandWrapper.deserialize(serverResponse);
		try {
			resultDescriptorHandler(commandWrapper.getResultDescriptor());
		} catch (InvalidCommandDescriptor | InvalidParameter |
				 AmountBiggerThanAvailable | ProductPackageDoesNotExist | ProductNotInCart | 
				GroceryListIsEmpty | AuthenticationError | ProductCatalogDoesNotExist ¢) {
			log.fatal("Critical bug: this command result isn't supposed to return here");
			¢.printStackTrace();
		}
		log.info("CHECKOUT_GROCERY_LIST command succeed.");
		//update cart data: groceryList, cartProductCache, totalSum
		groceryList = new GroceryList();
		cartProductCache = new HashMap<Long, CartProduct>();
		Double $ = totalSum;
		totalSum = Double.valueOf(0);
		return $;
	}

	@Override
	public CartProduct getCartProduct(SmartCode smartCode) {
		return cartProductCache.get(smartCode.getBarcode());
	}
	
	public CatalogProduct viewCatalogProduct(SmartCode c) throws CriticalError, CartNotConnected, ProductCatalogDoesNotExist {
		establishCommunication(CartDefs.port, CartDefs.host, CartDefs.timeout);
		//first: getting the product from the server
		log.info("Creating viewProductFromCatalog (in order to addPtoductToCart) command wrapper to cart with id: " + id);
		String serverResponse;
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
				ProductNotInCart | AmountBiggerThanAvailable | 	ProductPackageDoesNotExist | 
				GroceryListIsEmpty | AuthenticationError ¢) {
			log.fatal("Critical bug: this command result isn't supposed to return here");
			¢.printStackTrace();
			throw new CriticalError();
		}
		log.info("viewProductFromCatalog command succeed.");
		return Serialization.deserialize($.getData(), CatalogProduct.class);
	}
	
	public void removeAllItemsOfCartProduct(SmartCode smartCode) throws ProductNotInCart, CriticalError {
		CartProduct cartProduct = getCartProduct(smartCode);
		if (cartProduct == null) {
			throw new ProductNotInCart();
		}
		for( HashMap.Entry<SmartCode, ProductPackage> entry : cartProduct.getPackages().entrySet()) {
			try {
				returnProductToShelf(entry.getKey(), entry.getValue().getAmount());
			} catch (AmountBiggerThanAvailable | ProductPackageDoesNotExist | CriticalError | CartNotConnected e) {
				throw new CriticalError();
			}
		}
	}
	
}
