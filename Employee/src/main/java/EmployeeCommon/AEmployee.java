package EmployeeCommon;

import java.io.IOException;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

import ClientServerApi.ResultDescriptor;
import EmployeeDefs.AEmployeeException;
import EmployeeDefs.AEmployeeException.AmountBiggerThanAvailable;
import EmployeeDefs.AEmployeeException.AuthenticationError;
import EmployeeDefs.AEmployeeException.ConnectionFailure;
import EmployeeDefs.AEmployeeException.CriticalError;
import EmployeeDefs.AEmployeeException.InvalidCommandDescriptor;
import EmployeeDefs.AEmployeeException.InvalidParameter;
import EmployeeDefs.AEmployeeException.ProductAlreadyExistInCatalog;
import EmployeeDefs.AEmployeeException.ProductNotExistInCatalog;
import EmployeeDefs.AEmployeeException.ProductPackageDoesNotExist;
import EmployeeDefs.AEmployeeException.ProductStillForSale;
import EmployeeDefs.AEmployeeException.EmployeeAlreadyConnected;
import EmployeeDefs.AEmployeeException.EmployeeNotConnected;
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

	protected void establishCommunication(int port, String host, int timeout) throws ConnectionFailure {
		log.info("Establish communication with server: port: " + WorkerDefs.port + " host: " + WorkerDefs.host);
		try {
			clientRequestHandler.createSocket(port, host, timeout);
		} catch (UnknownHostException | RuntimeException e) {
			log.fatal("Creating communication with the server encounter severe fault: " + e.getMessage());

			throw new ConnectionFailure();
		}
	}

	protected void terminateCommunication() {
		if (clientRequestHandler != null)
			clientRequestHandler.finishRequest();
	}

	protected String sendRequestWithRespondToServer(String request) throws ConnectionFailure {
		log.info("Sending command to server");
		try {
			return this.clientRequestHandler.sendRequestWithRespond(request); 
		} catch (java.net.SocketTimeoutException e) {
			log.fatal("Sending logout command to server encounter sever fault : " + e.getMessage());
			terminateCommunication();
			throw new ConnectionFailure();
		} catch (IOException e) {
			log.fatal("Sending logout command to server encounter sever fault : " + e.getMessage());
			terminateCommunication();
			throw new ConnectionFailure();
		}
	}

	protected void resultDescriptorHandler(ResultDescriptor ¢) throws InvalidCommandDescriptor,
	InvalidParameter, CriticalError, EmployeeNotConnected, EmployeeAlreadyConnected,
	AuthenticationError, ProductNotExistInCatalog, ProductAlreadyExistInCatalog,
	ProductStillForSale, AmountBiggerThanAvailable, ProductPackageDoesNotExist {

		switch (¢) {

		case SM_OK:
			log.info("Command executed successfully");

			break;
			
		case SM_INVALID_CMD_DESCRIPTOR:
			log.fatal("Command execution failed, invalid command description");
			
			throw new AEmployeeException.InvalidCommandDescriptor();

		case SM_INVALID_PARAMETER:
			log.fatal("Command execution failed, invalid parameter");
			
			throw new AEmployeeException.InvalidParameter();
			
		case SM_ERR:
			log.fatal("Command execution failed, critical error");
			
			throw new AEmployeeException.CriticalError();
			
		case SM_SENDER_IS_NOT_CONNECTED:
			log.fatal("Command execution failed, worker not connected");
			
			throw new AEmployeeException.EmployeeNotConnected();
			
		case SM_SENDER_IS_ALREADY_CONNECTED:
			log.fatal("Command execution failed, worker already connected");
			
			throw new AEmployeeException.EmployeeAlreadyConnected();	
			
		case SM_USERNAME_DOES_NOT_EXIST_WRONG_PASSWORD:
			log.info("Command execution failed, autentication error");
			
			throw new AEmployeeException.AuthenticationError();
			
		case SM_CATALOG_PRODUCT_DOES_NOT_EXIST:
			log.info("Command execution failed, product no exist in catalog");
			
			throw new AEmployeeException.ProductNotExistInCatalog();

		case SM_CATALOG_PRODUCT_ALREADY_EXISTS:
			log.info("Command execution failed, product already exist in catalog");
			
			throw new AEmployeeException.ProductAlreadyExistInCatalog();
		
		case SM_CATALOG_PRODUCT_STILL_FOR_SALE:
			log.info("Command execution failed, product is still for sale");
			
			throw new AEmployeeException.ProductStillForSale();
			
		case SM_PRODUCT_PACKAGE_AMOUNT_BIGGER_THEN_AVAILABLE:
			log.info("Command execution failed, amount is bigger then available");
			
			throw new AEmployeeException.AmountBiggerThanAvailable();
			
		case SM_PRODUCT_PACKAGE_DOES_NOT_EXIST:
			log.fatal("Command execution failed, product package does not exist");
			
			throw new AEmployeeException.ProductPackageDoesNotExist();
			
		default:
			log.fatal("Command execution failed, failed to parse result description");
			
			throw new AEmployeeException.CriticalError();
		}
	}

}
