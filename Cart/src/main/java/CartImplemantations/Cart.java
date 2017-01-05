 package CartImplemantations;

import java.net.SocketTimeoutException;

import BasicCommonClasses.GroceryList;
import BasicCommonClasses.SmartCode;
import CartContracts.ICart;
import CartContracts.ACartExceptions.ProductNotInCart;
import CartContracts.ACartExceptions.ProductPackageDoesNotExist;
import ClientServerApi.CommandDescriptor;
import ClientServerApi.CommandWrapper;
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
	
	public void addPtoductToCart(SmartCode c) {
		
	}
	
	public void returnProductToShelf(SmartCode ¢) throws ProductNotInCart {
		if (groceryList == null) 
			throw new ProductNotInCart();
		groceryList.removeOneProduct(¢);
		
		//TODO: update server;
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
