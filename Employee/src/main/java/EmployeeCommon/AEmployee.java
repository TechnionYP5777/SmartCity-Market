package EmployeeCommon;

import java.io.IOException;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

import ClientServerApi.ResultDescriptor;
import EmployeeDefs.AEmployeeExceptions;
import EmployeeDefs.AEmployeeExceptions.AmountBiggerThanAvailable;
import EmployeeDefs.AEmployeeExceptions.AuthenticationError;
import EmployeeDefs.AEmployeeExceptions.CriticalError;
import EmployeeDefs.AEmployeeExceptions.InvalidCommandDescriptor;
import EmployeeDefs.AEmployeeExceptions.InvalidParameter;
import EmployeeDefs.AEmployeeExceptions.ProductAlreadyExistInCatalog;
import EmployeeDefs.AEmployeeExceptions.ProductNotExistInCatalog;
import EmployeeDefs.AEmployeeExceptions.ProductPackageDoesNotExist;
import EmployeeDefs.AEmployeeExceptions.ProductStillForSale;
import EmployeeDefs.AEmployeeExceptions.UnknownSenderID;
import EmployeeDefs.AEmployeeExceptions.EmployeeAlreadyConnected;
import EmployeeDefs.AEmployeeExceptions.EmployeeNotConnected;
import EmployeeDefs.WorkerDefs;
import UtilsContracts.IClientRequestHandler;

/**
 * AEmployee - This abstract holds common functionality for the Employee such as
 * worker, manager.
 * 
 * @author Shimon Azulay
 * @author Aviad Cohen

 * @since 2016-12-17
 */

public abstract class AEmployee {

	protected static Logger log = Logger.getLogger(AEmployee.class.getName());
	
	protected IClientRequestHandler clientRequestHandler;
	protected int clientId = WorkerDefs.loginCommandSenderId;
	protected String username;
	protected String password;

	protected void establishCommunication(int port, String host, int timeout) {
		log.info("Establish communication with server: port: " + WorkerDefs.port + " host: " + WorkerDefs.host);
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

	protected String sendRequestWithRespondToServer(String request) {
		log.info("Sending command to server");
		try {
			return this.clientRequestHandler.sendRequestWithRespond(request);
		} catch (IOException e) {
			e.printStackTrace();
			log.fatal("Sending logout command to server encounter sever fault : " + e.getMessage());
			terminateCommunication();
			throw new RuntimeException(e.getMessage());
		}
	}

	protected void resultDescriptorHandler(ResultDescriptor ¢) throws InvalidCommandDescriptor,
	InvalidParameter, UnknownSenderID, CriticalError, EmployeeNotConnected, EmployeeAlreadyConnected,
	AuthenticationError, ProductNotExistInCatalog, ProductAlreadyExistInCatalog,
	ProductStillForSale, AmountBiggerThanAvailable, ProductPackageDoesNotExist {

		switch (¢) {

		case SM_OK:
			log.info("Command executed successfully");

			break;
			
		case SM_INVALID_CMD_DESCRIPTOR:
			log.info("Command execution failed, invalid command description");
			
			throw new AEmployeeExceptions.InvalidCommandDescriptor();

		case SM_INVALID_PARAMETER:
			log.info("Command execution failed, invalid parameter");
			
			throw new AEmployeeExceptions.InvalidParameter();
			
		case SM_ERR:
			log.info("Command execution failed, critical error");
			
			throw new AEmployeeExceptions.CriticalError();
			
		case SM_INVALID_SENDER_ID:
			log.info("Command execution failed, invalid sender id");
			
			throw new AEmployeeExceptions.UnknownSenderID();
			
		case SM_SENDER_IS_NOT_CONNECTED:
			log.info("Command execution failed, worker not connected");
			
			throw new AEmployeeExceptions.EmployeeNotConnected();
			
		case SM_SENDER_IS_ALREADY_CONNECTED:
			log.info("Command execution failed, worker already connected");
			
			throw new AEmployeeExceptions.EmployeeAlreadyConnected();	
			
		case SM_USERNAME_DOES_NOT_EXIST_WRONG_PASSWORD:
			log.info("Command execution failed, autentication error");
			
			throw new AEmployeeExceptions.AuthenticationError();
			
		case SM_CATALOG_PRODUCT_DOES_NOT_EXIST:
			log.info("Command execution failed, product no exist in catalog");
			
			throw new AEmployeeExceptions.ProductNotExistInCatalog();

		case SM_CATALOG_PRODUCT_ALREADY_EXISTS:
			log.info("Command execution failed, product already exist in catalog");
			
			throw new AEmployeeExceptions.ProductAlreadyExistInCatalog();
		
		case SM_CATALOG_PRODUCT_STILL_FOR_SALE:
			log.info("Command execution failed, product is still for sale");
			
			throw new AEmployeeExceptions.ProductStillForSale();
			
		case SM_PRODUCT_PACKAGE_AMOUNT_BIGGER_THEN_AVAILABLE:
			log.info("Command execution failed, amount is bigger then available");
			
			throw new AEmployeeExceptions.AmountBiggerThanAvailable();
			
		case SM_PRODUCT_PACKAGE_DOES_NOT_EXIST:
			log.info("Command execution failed, product package does not exist");
			
			throw new AEmployeeExceptions.ProductPackageDoesNotExist();
			
		default:
			log.info("Command execution failed, failed to parse result description");
			
			throw new AEmployeeExceptions.CriticalError();
		}
	}

}
