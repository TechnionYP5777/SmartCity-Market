 package CartImplemantations;

import java.net.SocketTimeoutException;

import BasicCommonClasses.CatalogProduct;
import BasicCommonClasses.GroceryList;
import BasicCommonClasses.ProductPackage;
import BasicCommonClasses.SmartCode;
import CartContracts.ICart;
import CartContracts.ACartExceptions.ProductNotInCart;
import CartContracts.ACartExceptions.ProductPackageDoesNotExist;
import ClientServerApi.CommandDescriptor;
import ClientServerApi.CommandWrapper;
import CommonDefs.GroceryListExceptions.AmountIsBiggerThanAvailable;
import CommonDefs.GroceryListExceptions.ProductNotInList;
import CartContracts.ACartExceptions.AmountBiggerThanAvailable;
import CartContracts.ACartExceptions.CartNotConnected;
import CartContracts.ACartExceptions.CriticalError;
import CartContracts.ACartExceptions.GroceryListIsEmpty;
import CartContracts.ACartExceptions.InvalidCommandDescriptor;
import CartContracts.ACartExceptions.InvalidParameter;
import UtilsImplementations.Serialization;
/**
 * Cart class represents shopping cart in the SmartMarket.
 * 
 * @author Lior Ben Ami
 * @since 2017-01-02
 */
public class Cart extends ACart implements ICart {

	GroceryList groceryList = new GroceryList();
	
	public int getId() {
		return id;
	}

	public GroceryList getGroceryList() {
		return groceryList;
	}

	public void login() throws CriticalError {
		CommandWrapper $ = null;
		establishCommunication(CartDefs.port, CartDefs.host, CartDefs.timeout);
		log.info("Creating login command wrapper for cart");
		String serverResponse = null;
		try {
			serverResponse = sendRequestWithRespondToServer((new CommandWrapper(CartDefs.loginCommandSenderId,
					CommandDescriptor.CART_LOGIN)).serialize());
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
		} catch (InvalidCommandDescriptor | InvalidParameter| CriticalError | CartNotConnected |
				ProductNotInCart | AmountBiggerThanAvailable | 	ProductPackageDoesNotExist | GroceryListIsEmpty ¢) {
			log.fatal("Critical bug: this command result isn't supposed to return here");
			¢.printStackTrace();
		}
		id = $.getSenderID();
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
		} catch (InvalidCommandDescriptor | InvalidParameter| CriticalError |
				ProductNotInCart | AmountBiggerThanAvailable | 	ProductPackageDoesNotExist | GroceryListIsEmpty ¢) {
			log.fatal("Critical bug: this command result isn't supposed to return here");
			¢.printStackTrace();
		}
		log.info("logout from server succeed.");
	}
	
	public void resume(int _id) throws CriticalError, CartNotConnected {
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
		} catch (InvalidCommandDescriptor | InvalidParameter| CriticalError |
				ProductNotInCart | AmountBiggerThanAvailable | 	ProductPackageDoesNotExist | GroceryListIsEmpty ¢) {
			log.fatal("Critical bug: this command result isn't supposed to return here");
			¢.printStackTrace();
		}
		id = _id;
		groceryList = Serialization.deserialize($.getData(), GroceryList.class);
		log.info("load grocery list from server succeed.");
	}
	
	public void addPtoductToCart(SmartCode c, int amount) throws CriticalError, CartNotConnected {
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
		
		CommandWrapper commandWrapper = CommandWrapper.deserialize(serverResponse);
		try {
			resultDescriptorHandler(commandWrapper.getResultDescriptor());
		} catch (InvalidCommandDescriptor | InvalidParameter| CriticalError |
				ProductNotInCart | AmountBiggerThanAvailable | 	ProductPackageDoesNotExist | 
				GroceryListIsEmpty ¢) {
			log.fatal("Critical bug: this command result isn't supposed to return here");
			¢.printStackTrace();
		}
		log.info("viewProductFromCatalog command succeed.");
		CatalogProduct catalogProduct = Serialization.deserialize(commandWrapper.getData(), CatalogProduct.class);
		//add product to the cart
		try {
			groceryList.addProduct(c, catalogProduct, amount);
		} catch (CommonDefs.GroceryListExceptions.InvalidParameter ¢) {
			log.error("Critical bug: the given product isn't fit its smartcode");
			¢.printStackTrace();
		}
		establishCommunication(CartDefs.port, CartDefs.host, CartDefs.timeout);
		//add the product to the cart - server
		log.info("Creating viewProductFromCatalog (in order to addPtoductToCart) command wrapper to cart with id: " + id);
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
		} catch (InvalidCommandDescriptor | InvalidParameter| CriticalError |
				ProductNotInCart | AmountBiggerThanAvailable | 	ProductPackageDoesNotExist | 
				GroceryListIsEmpty ¢) {
			log.fatal("Critical bug: this command result isn't supposed to return here");
			try {
				groceryList.addProduct(c, catalogProduct, amount);
			} catch (CommonDefs.GroceryListExceptions.InvalidParameter e) {
				e.printStackTrace();
			}
			¢.printStackTrace();
		}
		log.info("addProductToGroceryList command succeed.");
	}
	
	public void returnProductToShelf(SmartCode c, int amount) throws ProductNotInCart {
		if (groceryList == null) 
			throw new ProductNotInCart();
		try {
			groceryList.removeProduct(c, amount);
		} catch (ProductNotInList ¢) {
			throw new ProductNotInCart();
		} catch (AmountIsBiggerThanAvailable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public double getTotalSum() {
		return groceryList == null ? 0 : groceryList.getTotalSum();
	}
	
	public double checkOutGroceryList() {
		//TODO: update server;
		double $ = this.getTotalSum();
		groceryList = null;
		return $;
	}

}