package CustomerImplementations;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

import BasicCommonClasses.CustomerProfile;
import ClientServerApi.ResultDescriptor;
import CustomerContracts.ACustomerExceptions.*;
import SMExceptions.CommonExceptions.CriticalError;
import UtilsContracts.IClientRequestHandler;

/**
 * ACustomer - This abstract holds server communication functionality for the Customer.
 *
 * @author Lior Ben Ami
 * @since 2017-01-04
 */
public abstract class ACustomer {

	protected static Logger log = Logger.getLogger(ACustomer.class.getName());
	protected int id = CustomerDefs.loginCommandSenderId;
	protected CustomerProfile customerProfile;

	protected IClientRequestHandler clientRequestHandler;

	protected void establishCommunication(int port, String host, int timeout) {
		log.info("Establish communication with server: port: " + CustomerDefs.port + " host: " + CustomerDefs.host);
		try {
			clientRequestHandler.createSocket(port, host, timeout);
		} catch (UnknownHostException | RuntimeException e) {
			log.fatal("Creating communication with the server encounter severe fault: " + e.getMessage());

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
			log.fatal("Sending logout command to server encounter sever fault : " + e.getMessage());
			terminateCommunication();
			throw new java.net.SocketTimeoutException(e.getMessage());
		} catch (IOException e) {
			log.fatal("Sending logout command to server encounter sever fault : " + e.getMessage());
			terminateCommunication();
			throw new RuntimeException(e.getMessage());
		}
	}

	protected void resultDescriptorHandler(ResultDescriptor ¢) throws InvalidCommandDescriptor,
		InvalidParameter, CriticalError, CustomerNotConnected, AmountBiggerThanAvailable,
		ProductPackageDoesNotExist, GroceryListIsEmpty, AuthenticationError, ProductCatalogDoesNotExist,
		UsernameAlreadyExists, ForgotPasswordWrongAnswer {

		switch (¢) {

		case SM_OK:
			log.info("Command executed successfully");

			break;
		
		case SM_NO_UPDATE_NEEDED:
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
			log.fatal("Command execution failed, customer not connected");
			
			throw new CustomerNotConnected();

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
			
		case SM_USERNAME_ALREADY_EXISTS:
			log.fatal("Command execution failed, username alread exists in the system");
			
			throw new UsernameAlreadyExists();
			
		case SM_FOROGT_PASSWORD_WRONG_ANSWER:
			log.fatal("Command execution failed, got wrong password for as answer");
			
			throw new ForgotPasswordWrongAnswer();
			
		//TODO - ADD MORE CASES RELEVANT FOR REGISTERED-CUSTOMERS
		default:
			log.fatal("Command execution failed, failed to parse result description");
			
			throw new CriticalError();
		}
	}
	
	@Override
	public int hashCode() {
		return 31 + ((customerProfile == null) ? 0 : customerProfile.hashCode());
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		ACustomer other = (ACustomer) o;
		if (customerProfile == null) {
			if (other.customerProfile != null)
				return false;
		} else if (!customerProfile.equals(other.customerProfile))
			return false;
		return true;
	}

}
