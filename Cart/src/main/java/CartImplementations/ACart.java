package CartImplementations;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

import CartContracts.ACartExceptions.CartNotConnected;
import CartContracts.ACartExceptions.CriticalError;
import CartContracts.ACartExceptions.InvalidCommandDescriptor;
import CartContracts.ACartExceptions.InvalidParameter;
import CartContracts.ACartExceptions.AuthenticationError;
import CartContracts.ACartExceptions.ProductPackageDoesNotExist;
import CartContracts.ACartExceptions.GroceryListIsEmpty;
import CartContracts.ACartExceptions.AmountBiggerThanAvailable;
import CartContracts.ACartExceptions.ProductCatalogDoesNotExist;
import ClientServerApi.ResultDescriptor;
import UtilsContracts.IClientRequestHandler;

/**
 * ACart - This abstract holds server communication functionality for the Cart.
 *
 * @author Lior Ben Ami
 * @since 2017-01-04
 */
public abstract class ACart {

	protected static Logger log = Logger.getLogger(ACart.class.getName());
	protected int id = CartDefs.loginCommandSenderId;
	protected String username;
	protected String password;

	protected IClientRequestHandler clientRequestHandler;

	protected void establishCommunication(int port, String host, int timeout) {
		log.info("Establish communication with server: port: " + CartDefs.port + " host: " + CartDefs.host);
		try {
			clientRequestHandler.createSocket(port, host, timeout);
		} catch (UnknownHostException | RuntimeException e) {
			log.fatal("Creating communication with the server encounter severe fault: " + e.getMessage());
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

	protected void terminateCommunication() {
		if (clientRequestHandler != null)
			clientRequestHandler.finishRequest();
	}

	protected String sendRequestWithRespondToServer(String request) throws SocketTimeoutException {
		log.info("Sending command to server");
		try {
			return this.clientRequestHandler.sendRequestWithRespond(request); 
		} catch (java.net.SocketTimeoutException e) {
			e.printStackTrace();
			log.fatal("Sending logout command to server encounter sever fault : " + e.getMessage());
			terminateCommunication();
			throw new java.net.SocketTimeoutException(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			log.fatal("Sending logout command to server encounter sever fault : " + e.getMessage());
			terminateCommunication();
			throw new RuntimeException(e.getMessage());
		}
	}

	protected void resultDescriptorHandler(ResultDescriptor ¢) throws InvalidCommandDescriptor,
		InvalidParameter, CriticalError, CartNotConnected, AmountBiggerThanAvailable,
		ProductPackageDoesNotExist, GroceryListIsEmpty, AuthenticationError, ProductCatalogDoesNotExist {

		switch (¢) {

		case SM_OK:
			log.info("Command executed successfully");

			break;
			
		case SM_INVALID_CMD_DESCRIPTOR:
			log.fatal("Command execution failed, invalid command description");
			
			throw new InvalidCommandDescriptor();

		case SM_INVALID_PARAMETER:
			log.fatal("Command execution failed, invalid parameter");
			
			throw new InvalidParameter();
			
		case SM_ERR:
			log.fatal("Command execution failed, critical error");
			
			throw new CriticalError();
		
		case SM_USERNAME_DOES_NOT_EXIST_WRONG_PASSWORD:
			log.fatal("Command execution failed, autentication error");
			
			throw new AuthenticationError();

		case SM_SENDER_IS_NOT_CONNECTED:
			log.fatal("Command execution failed, cart not connected");
			
			throw new CartNotConnected();

		case SM_PRODUCT_PACKAGE_AMOUNT_BIGGER_THEN_AVAILABLE:
			log.info("Command execution failed, amount is bigger then available");
			
			throw new AmountBiggerThanAvailable();
			
		case SM_PRODUCT_PACKAGE_DOES_NOT_EXIST:
			log.fatal("Command execution failed, product package does not exist");
			
			throw new ProductPackageDoesNotExist();
		
		case SM_GROCERY_LIST_IS_EMPTY:
			log.fatal("Command execution failed, grocery list is empty");
			
			throw new GroceryListIsEmpty();
		
		case SM_CATALOG_PRODUCT_DOES_NOT_EXIST:
			log.fatal("Command execution failed, product does not exist in catalog");
			
			throw new ProductCatalogDoesNotExist();
		default:
			log.fatal("Command execution failed, failed to parse result description");
			
			throw new CriticalError();
		}
	}

}
