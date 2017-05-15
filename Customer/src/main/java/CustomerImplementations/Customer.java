package CustomerImplementations;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;

import BasicCommonClasses.CartProduct;
import BasicCommonClasses.CatalogProduct;
import BasicCommonClasses.CustomerProfile;
import BasicCommonClasses.GroceryList;
import BasicCommonClasses.Ingredient;
import BasicCommonClasses.Login;
import BasicCommonClasses.ProductPackage;
import BasicCommonClasses.SmartCode;
import ClientServerApi.CommandDescriptor;
import ClientServerApi.CommandWrapper;
import ClientServerApi.ResultDescriptor;
import CommonDefs.GroceryListExceptions.AmountIsBiggerThanAvailable;
import CommonDefs.GroceryListExceptions.ProductNotInList;
import CustomerContracts.ICustomer;
import PicturesHandler.PictureManager;
import CustomerContracts.ACustomerExceptions.AmountBiggerThanAvailable;
import CustomerContracts.ACustomerExceptions.AuthenticationError;
import CustomerContracts.ACustomerExceptions.CustomerNotConnected;
import CustomerContracts.ACustomerExceptions.CriticalError;
import CustomerContracts.ACustomerExceptions.GroceryListIsEmpty;
import CustomerContracts.ACustomerExceptions.InvalidCommandDescriptor;
import CustomerContracts.ACustomerExceptions.InvalidParameter;
import CustomerContracts.ACustomerExceptions.ProductCatalogDoesNotExist;
import CustomerContracts.ACustomerExceptions.ProductNotInCart;
import CustomerContracts.ACustomerExceptions.ProductPackageDoesNotExist;
import CustomerContracts.ACustomerExceptions.UsernameAlreadyExists;
import UtilsContracts.IClientRequestHandler;
import UtilsImplementations.Serialization;

/**
 * Customer class represents shopping customer in the SmartMarket.
 * 
 * @author Lior Ben Ami, Aviad Cohen
 * @since 2017-01-02
 */
public class Customer extends ACustomer implements ICustomer {
	@Inject
	public Customer(IClientRequestHandler clientRequestHandler) {
		this.clientRequestHandler = clientRequestHandler;
		this.customerProfile = null; // as long as customer is not logged in
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
		for (HashMap.Entry<Long, CartProduct> entry : cartProductCache.entrySet())
			count += entry.getValue().getTotalAmount();
		return count;
	}

	private void loadCartProductCacheAndUpdateCartData()
			throws CriticalError, CustomerNotConnected, ProductCatalogDoesNotExist {
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

	public CommandWrapper getCommandWrapper(String serverResponse) throws CriticalError {
		try {
			return CommandWrapper.deserialize(serverResponse);
		} catch (Exception e) {
			log.fatal("Critical bug: failed to desirealize server respond: " + serverResponse);
			
			throw new CriticalError();
		}
	}
	
	/***
	 * 
	 * @author idan atias
	 *
	 * @since April 30, 2017
	 * 
	 * This class used for verifying our product pictures are up to date. if not, we fetch them from server.
	 */
	public class UpdateProductPictures extends Thread {
				
		@Override
		public void run() {		
			CommandWrapper cmdwrppr = null;
			String serverResponse = null;

			log.info("Creating UpdateProductPictures wrapper for customer");

			establishCommunication(CustomerDefs.port, CustomerDefs.host, CustomerDefs.timeout);

			try {
				LocalDate currentPicturesDate = PictureManager.getCurrentDate();
				serverResponse = sendRequestWithRespondToServer(
						(new CommandWrapper(id, CommandDescriptor.UPDATE_PRODUCTS_PICTURES,
								Serialization.serialize(currentPicturesDate))).serialize());
			} catch (/*SocketTimeoutException |*/ IOException e) {
				log.fatal("Critical bug: failed to get respond from server");
				log.fatal(e + "");
				throw new RuntimeException();
			}

			terminateCommunication();

			try {
				cmdwrppr = getCommandWrapper(serverResponse);
			} catch (CriticalError e1) {
				throw new RuntimeException();
			}
			ResultDescriptor resDesc = cmdwrppr.getResultDescriptor();

			try {
				resultDescriptorHandler(resDesc);
			} catch (InvalidCommandDescriptor | InvalidParameter | CustomerNotConnected | ProductCatalogDoesNotExist
					| AmountBiggerThanAvailable | ProductPackageDoesNotExist | GroceryListIsEmpty
					| UsernameAlreadyExists | CriticalError | AuthenticationError ¢) {
				log.fatal("Critical bug: this command result isn't supposed to return here");

				throw new RuntimeException();
			}
			
			if (resDesc == ResultDescriptor.SM_NO_UPDATE_NEEDED){
				log.info("No need to update the products pictures currently on this cart");
				return;
			}

			String productsPicturesEncodedZipFile = Serialization.deserialize(cmdwrppr.getData(), String.class);
			
			try {
				PictureManager.doPicturesExchange(productsPicturesEncodedZipFile);
			} catch (Exception e){
				log.error("Error while trying to unpack the zip file returned from server");
				throw new RuntimeException();
			}
			log.info("Successfully updated product pictures");
		}
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
	public void login(String username, String password, boolean updateProductPictures) throws CriticalError, AuthenticationError {
		CommandWrapper cmdwrppr = null;
		String serverResponse = null;

		log.info("Creating login command wrapper for customer");

		establishCommunication(CustomerDefs.port, CustomerDefs.host, CustomerDefs.timeout);

		try {
			serverResponse = sendRequestWithRespondToServer(
					(new CommandWrapper(CustomerDefs.loginCommandSenderId, CommandDescriptor.LOGIN_CUSTOMER,
							Serialization.serialize(new Login(username, password)))).serialize());
		} catch (SocketTimeoutException e) {
			log.fatal("Critical bug: failed to get respond from server");

			throw new CriticalError();
		}

		terminateCommunication();

		cmdwrppr = getCommandWrapper(serverResponse);

		try {
			resultDescriptorHandler(cmdwrppr.getResultDescriptor());
		} catch (InvalidCommandDescriptor | InvalidParameter | CustomerNotConnected | ProductCatalogDoesNotExist
				| AmountBiggerThanAvailable | ProductPackageDoesNotExist | GroceryListIsEmpty
				| UsernameAlreadyExists ¢) {
			log.fatal("Critical bug: this command result isn't supposed to return here");

			throw new CriticalError();
		}

		id = cmdwrppr.getSenderID();
		if (this instanceof RegisteredCustomer)
			customerProfile = Serialization.deserialize(cmdwrppr.getData(), CustomerProfile.class);
		
		//check for new product pictures asynchronous
		if (updateProductPictures)
			new UpdateProductPictures().start();

		log.info("Customer Login to server as succeed. Client id is: " + id);
	}

	@Override
	public void logout() throws CustomerNotConnected, CriticalError {
		String serverResponse;

		log.info("Creating customer logout command wrapper with id: " + id);

		establishCommunication(CustomerDefs.port, CustomerDefs.host, CustomerDefs.timeout);

		try {
			serverResponse = sendRequestWithRespondToServer(
					(new CommandWrapper(id, CommandDescriptor.LOGOUT)).serialize());
		} catch (SocketTimeoutException e) {
			log.fatal("Critical bug: failed to get respond from server");

			throw new CriticalError();
		}

		terminateCommunication();

		try {
			resultDescriptorHandler(getCommandWrapper(serverResponse).getResultDescriptor());
		} catch (InvalidCommandDescriptor | InvalidParameter | ProductCatalogDoesNotExist | AmountBiggerThanAvailable
				| ProductPackageDoesNotExist | GroceryListIsEmpty | AuthenticationError | UsernameAlreadyExists ¢) {
			log.fatal("Critical bug: this command result isn't supposed to return here");

			throw new CriticalError();
		}

		customerProfile = null;
		log.info("logout from server succeed.");
	}

	@Override
	public void resume(int _id) throws CriticalError, CustomerNotConnected {
		CommandWrapper $ = null;
		String serverResponse;

		log.info("Creating customer Load grocery list command wrapper with id: " + id);

		establishCommunication(CustomerDefs.port, CustomerDefs.host, CustomerDefs.timeout);

		try {
			serverResponse = sendRequestWithRespondToServer(
					(new CommandWrapper(id, CommandDescriptor.LOAD_GROCERY_LIST)).serialize());
		} catch (SocketTimeoutException e) {
			log.fatal("Critical bug: failed to get respond from server");

			throw new CriticalError();
		}

		terminateCommunication();

		try {
			$ = getCommandWrapper(serverResponse);

			resultDescriptorHandler($.getResultDescriptor());

			id = _id;
			groceryList = Serialization.deserialize($.getData(), GroceryList.class);
		} catch (InvalidCommandDescriptor | InvalidParameter | ProductCatalogDoesNotExist | AmountBiggerThanAvailable
				| ProductPackageDoesNotExist | GroceryListIsEmpty | AuthenticationError | UsernameAlreadyExists ¢) {
			log.fatal("Critical bug: this command result isn't supposed to return here");

			throw new CriticalError();
		}

		/* Restoring Grocery list */
		try {
			loadCartProductCacheAndUpdateCartData();
		} catch (ProductCatalogDoesNotExist | CriticalError | CustomerNotConnected e) {
			log.fatal("Critical bug: Failed to fetch grocery list items from server");
		}

		log.info("load grocery list from server succeed.");
	}

	@Override
	public void addProductToCart(SmartCode c, int amount) throws CriticalError, CustomerNotConnected,
			AmountBiggerThanAvailable, ProductPackageDoesNotExist, InvalidParameter {
		String serverResponse;

		log.info("Creating viewProductFromCatalog (in order to addPtoductToCart) command wrapper to customer with id: "
				+ id);

		CatalogProduct catalogProduct;
		try {
			catalogProduct = viewCatalogProduct(c);
		} catch (ProductCatalogDoesNotExist e1) {
			log.fatal("Critical bug: failed to get catalog product from server");

			throw new CriticalError();
		}

		establishCommunication(CustomerDefs.port, CustomerDefs.host, CustomerDefs.timeout);

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
			resultDescriptorHandler(getCommandWrapper(serverResponse).getResultDescriptor());
		} catch (InvalidCommandDescriptor | ProductCatalogDoesNotExist | GroceryListIsEmpty | AuthenticationError
				| UsernameAlreadyExists ¢) {
			log.fatal("Critical bug: this command result isn't supposed to return here");

			throw new CriticalError();
		}

		ProductPackage productPackage1 = new ProductPackage(c, amount, null);
		ProductPackage productPackage2 = new ProductPackage(c, amount, null);

		groceryList.addProduct(productPackage1);

		addProductToCacheAndUpdateCartData(productPackage2, catalogProduct);

		log.info("addProductToGroceryList command succeed.");
	}

	@Override
	public void returnProductToShelf(SmartCode c, int amount) throws ProductNotInCart, AmountBiggerThanAvailable,
			ProductPackageDoesNotExist, CriticalError, CustomerNotConnected {
		String serverResponse;

		log.info("Creating REMOVE_PRODUCT_FROM_GROCERY_LIST command wrapper to customer with id: " + id);

		establishCommunication(CustomerDefs.port, CustomerDefs.host, CustomerDefs.timeout);

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
			resultDescriptorHandler(getCommandWrapper(serverResponse).getResultDescriptor());
		} catch (InvalidCommandDescriptor | InvalidParameter | ProductCatalogDoesNotExist | GroceryListIsEmpty
				| AuthenticationError | UsernameAlreadyExists ¢) {
			log.fatal("Critical bug: this command result isn't supposed to return here");

			throw new CriticalError();
		}

		/* update customer data: groceryList, cartProductCache, totalSum */
		if (groceryList == null)
			throw new CriticalError();

		try {
			groceryList.removeProduct(new ProductPackage(c, amount, null));
		} catch (ProductNotInList | AmountIsBiggerThanAvailable ¢) {
			throw new CriticalError();
		}

		long barcode = c.getBarcode();

		CartProduct cartProduct = cartProductCache.get(barcode);
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
	public Double checkOutGroceryList() throws CriticalError, CustomerNotConnected, GroceryListIsEmpty {
		String serverResponse;
		Double $ = totalSum;

		log.info("Creating CHECKOUT_GROCERY_LIST command wrapper to customer with id: " + id);

		establishCommunication(CustomerDefs.port, CustomerDefs.host, CustomerDefs.timeout);

		try {
			serverResponse = sendRequestWithRespondToServer(
					(new CommandWrapper(id, CommandDescriptor.CHECKOUT_GROCERY_LIST)).serialize());
		} catch (SocketTimeoutException e) {
			log.fatal("Critical bug: failed to get respond from server");

			throw new CriticalError();
		}

		terminateCommunication();

		try {
			resultDescriptorHandler(getCommandWrapper(serverResponse).getResultDescriptor());
		} catch (InvalidCommandDescriptor | InvalidParameter | AmountBiggerThanAvailable | ProductPackageDoesNotExist
				| AuthenticationError | ProductCatalogDoesNotExist | UsernameAlreadyExists ¢) {
			log.fatal("Critical bug: this command result isn't supposed to return here");

			throw new CriticalError();
		}

		/* update customer data: groceryList, cartProductCache, totalSum */
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
	public CatalogProduct viewCatalogProduct(SmartCode c)
			throws CriticalError, CustomerNotConnected, ProductCatalogDoesNotExist {
		String serverResponse;

		log.info("Creating viewProductFromCatalog (in order to addPtoductToCart) command wrapper to customer with id: "
				+ id);

		establishCommunication(CustomerDefs.port, CustomerDefs.host, CustomerDefs.timeout);

		/* first: getting the product from the server */
		try {
			serverResponse = sendRequestWithRespondToServer(
					(new CommandWrapper(id, CommandDescriptor.VIEW_PRODUCT_FROM_CATALOG, Serialization.serialize(c))
							.serialize()));
		} catch (SocketTimeoutException e) {
			log.fatal("Critical bug: failed to get respond from server");

			throw new CriticalError();
		}

		terminateCommunication();

		CommandWrapper $ = getCommandWrapper(serverResponse);

		try {
			resultDescriptorHandler($.getResultDescriptor());
		} catch (InvalidCommandDescriptor | InvalidParameter | CriticalError | AmountBiggerThanAvailable
				| ProductPackageDoesNotExist | GroceryListIsEmpty | AuthenticationError | UsernameAlreadyExists ¢) {
			log.fatal("Critical bug: this command result isn't supposed to return here");

			throw new CriticalError();
		}

		log.info("viewProductFromCatalog command succeed.");

		return Serialization.deserialize($.getData(), CatalogProduct.class);
	}

	@Override
	public void removeAllItemsOfCartProduct(SmartCode c) throws ProductNotInCart, CriticalError {
		CartProduct cartProduct = getCartProduct(c);

		if (cartProduct == null)
			throw new ProductNotInCart();

		for (HashMap.Entry<SmartCode, ProductPackage> entry : cartProduct.getPackages().entrySet())
			try {
				returnProductToShelf(entry.getKey(), entry.getValue().getAmount());
			} catch (AmountBiggerThanAvailable | ProductPackageDoesNotExist | CriticalError | CustomerNotConnected e) {
				throw new CriticalError();
			}
	}

	@Override
	public void registerNewCustomer(CustomerProfile p) throws CriticalError, InvalidParameter, UsernameAlreadyExists {
		String serverResponse;

		log.info("Creating registerNewCustomer command wrapper to customer with username: " + p.getUserName());

		establishCommunication(CustomerDefs.port, CustomerDefs.host, CustomerDefs.timeout);

		/* first: getting the product from the server */
		try {
			serverResponse = sendRequestWithRespondToServer(
					(new CommandWrapper(id, CommandDescriptor.REGISTER_NEW_CUSTOMER, Serialization.serialize(p))
							.serialize()));
		} catch (SocketTimeoutException e) {
			log.fatal("Critical bug: failed to get respond from server");

			throw new CriticalError();
		}

		terminateCommunication();

		CommandWrapper $ = getCommandWrapper(serverResponse);

		try {
			resultDescriptorHandler($.getResultDescriptor());
		} catch (InvalidCommandDescriptor | CriticalError | AmountBiggerThanAvailable | ProductPackageDoesNotExist
				| GroceryListIsEmpty | AuthenticationError | CustomerNotConnected | ProductCatalogDoesNotExist ¢) {
			log.fatal("Critical bug: this command result isn't supposed to return here");

			throw new CriticalError();
		}

		log.info("registerNewCustomer command succeed.");
	}

	@Override
	public List<Ingredient> getAllIngredients() throws CriticalError {
		String serverResponse;

		log.info("Creating getAllIngredients command wrapper");

		establishCommunication(CustomerDefs.port, CustomerDefs.host, CustomerDefs.timeout);

		/* first: getting the product from the server */
		try {
			serverResponse = sendRequestWithRespondToServer(
					(new CommandWrapper(id, CommandDescriptor.GET_ALL_INGREDIENTS).serialize()));
		} catch (SocketTimeoutException e) {
			log.fatal("Critical bug: failed to get respond from server");

			throw new CriticalError();
		}

		terminateCommunication();

		CommandWrapper commandWrapper = getCommandWrapper(serverResponse);

		try {
			resultDescriptorHandler(commandWrapper.getResultDescriptor());
		} catch (InvalidCommandDescriptor | CriticalError | AmountBiggerThanAvailable | ProductPackageDoesNotExist
				| GroceryListIsEmpty | AuthenticationError | CustomerNotConnected | ProductCatalogDoesNotExist
				| InvalidParameter | UsernameAlreadyExists ¢) {
			log.fatal("Critical bug: this command result isn't supposed to return here");

			throw new CriticalError();
		}

		log.info("getAllIngredients command succeed.");

		return new Gson().fromJson(commandWrapper.getData(), new TypeToken<List<Ingredient>>() {}.getType());
	}

	@Override
	public Boolean isFreeUsername(String username) throws CriticalError {
		Boolean isFree = true;
		String serverResponse;

		log.info("Creating isFreeUsername command wrapper");

		establishCommunication(CustomerDefs.port, CustomerDefs.host, CustomerDefs.timeout);

		/* first: getting the product from the server */
		try {
			serverResponse = sendRequestWithRespondToServer(
					(new CommandWrapper(id, CommandDescriptor.IS_FREE_CUSTOMER_NAME, Serialization.serialize(username))
							.serialize()));
		} catch (SocketTimeoutException e) {
			log.fatal("Critical bug: failed to get respond from server");

			throw new CriticalError();
		}

		terminateCommunication();

		CommandWrapper commandWrapper = getCommandWrapper(serverResponse);

		try {
			resultDescriptorHandler(commandWrapper.getResultDescriptor());
		} catch (UsernameAlreadyExists e) {
			isFree = false;
		} catch (InvalidCommandDescriptor | CriticalError | AmountBiggerThanAvailable | ProductPackageDoesNotExist
				| GroceryListIsEmpty | AuthenticationError | CustomerNotConnected | ProductCatalogDoesNotExist
				| InvalidParameter ¢) {
			log.fatal("Critical bug: this command result isn't supposed to return here");

			throw new CriticalError();
		}

		log.info("isFreeUsername command succeed.");

		return isFree;
	}
}
