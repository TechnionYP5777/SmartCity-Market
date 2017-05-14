package EmployeeCommon;

import java.io.IOException;
import java.net.UnknownHostException;
import java.time.LocalDate;

import org.apache.log4j.Logger;

import ClientServerApi.CommandDescriptor;
import ClientServerApi.CommandWrapper;
import ClientServerApi.ResultDescriptor;
import EmployeeDefs.AEmployeeException;
import EmployeeDefs.AEmployeeException.AmountBiggerThanAvailable;
import EmployeeDefs.AEmployeeException.AuthenticationError;
import EmployeeDefs.AEmployeeException.ConnectionFailure;
import EmployeeDefs.AEmployeeException.CriticalError;
import EmployeeDefs.AEmployeeException.InvalidCommandDescriptor;
import EmployeeDefs.AEmployeeException.InvalidParameter;
import EmployeeDefs.AEmployeeException.ManfacturerStillInUse;
import EmployeeDefs.AEmployeeException.ParamIDAlreadyExists;
import EmployeeDefs.AEmployeeException.ParamIDDoesNotExist;
import EmployeeDefs.AEmployeeException.ProductAlreadyExistInCatalog;
import EmployeeDefs.AEmployeeException.ProductNotExistInCatalog;
import EmployeeDefs.AEmployeeException.ProductPackageDoesNotExist;
import EmployeeDefs.AEmployeeException.ProductStillForSale;
import EmployeeDefs.AEmployeeException.WorkerAlreadyExists;
import EmployeeDefs.AEmployeeException.WorkerDoesNotExist;
import PicturesHandler.PictureManager;
import EmployeeDefs.AEmployeeException.EmployeeAlreadyConnected;
import EmployeeDefs.AEmployeeException.EmployeeNotConnected;
import EmployeeDefs.AEmployeeException.IngredientStillInUse;
import EmployeeDefs.WorkerDefs;
import UtilsContracts.IClientRequestHandler;
import UtilsImplementations.Serialization;

/**
 * AEmployee - This abstract holds common functionality for the Employee such as
 * worker, manager.
 * 
 * @author Shimon Azulay
 * @author Aviad Cohen
 * 
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

		establishCommunication(WorkerDefs.port, WorkerDefs.host, WorkerDefs.timeout);
		log.info("Sending command to server");
		try {
			String $ = this.clientRequestHandler.sendRequestWithRespond(request);
			terminateCommunication();
			return $;
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

	private CommandWrapper getCommandWrapper(String serverResponse) {
		try {
			return CommandWrapper.deserialize(serverResponse);
		} catch (Exception e) {
			log.fatal("Critical bug: failed to desirealize server respond: " + serverResponse);
			
			throw new RuntimeException();
		}
	}
	
	protected void resultDescriptorHandler(ResultDescriptor ¢)
			throws InvalidCommandDescriptor, InvalidParameter, CriticalError, EmployeeNotConnected,
			EmployeeAlreadyConnected, AuthenticationError, ProductNotExistInCatalog, ProductAlreadyExistInCatalog,
			ProductStillForSale, AmountBiggerThanAvailable, ProductPackageDoesNotExist, WorkerAlreadyExists, ParamIDAlreadyExists,
			ParamIDDoesNotExist, WorkerDoesNotExist, IngredientStillInUse, ManfacturerStillInUse {

		switch (¢) {

		case SM_OK:
			log.info("Command executed successfully");

			break;
		
		case SM_NO_UPDATE_NEEDED:
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

		case SM_USERNAME_ALREADY_EXISTS:
			log.info("Command execution failed, worker username already exists");
			
			throw new AEmployeeException.WorkerAlreadyExists();
			
		case SM_USERNAME_DOES_NOT_EXIST:
			log.info("Command execution failed, worker username does not exist");
			
			throw new AEmployeeException.WorkerDoesNotExist();
			
		case PARAM_ID_ALREADY_EXISTS:
			log.info("Command execution failed, param ID already exists");

			throw new AEmployeeException.ParamIDAlreadyExists();
			
		case PARAM_ID_IS_NOT_EXIST:
			log.info("Command execution failed, param ID does not exist");

			throw new AEmployeeException.ParamIDDoesNotExist();
			
		case SM_PRODUCT_PACKAGE_DOES_NOT_EXIST:
			log.fatal("Command execution failed, product package does not exist");

			throw new AEmployeeException.ProductPackageDoesNotExist();
			
		case SM_INGREDIENT_STILL_IN_USE:
			log.fatal("Command execution failed, ingredient still in use");

			throw new AEmployeeException.IngredientStillInUse();
			
		case SM_MANUFACTURER_STILL_IN_USE:
			log.fatal("Command execution failed, ingredient still in use");

			throw new AEmployeeException.ManfacturerStillInUse();

		default:
			log.fatal("Command execution failed, failed to parse result description");

			throw new AEmployeeException.CriticalError();
		}
	}
	
	public int getClientId() {
		return clientId;
	}

	public void setClientId(int clientId) {
		this.clientId = clientId;
	}

	/***
	 * 
	 * @author idan atias
	 *
	 * @since May 11, 2017
	 * 
	 * This class used for verifying our product pictures are up to date. if not, we fetch them from server.
	 */
	public class UpdateProductPictures extends Thread {
				
		@Override
		public void run() {		
			CommandWrapper cmdwrppr = null;
			String serverResponse = null;

			log.info("Creating UpdateProductPictures wrapper for customer");

			try {
				establishCommunication(WorkerDefs.port, WorkerDefs.host, WorkerDefs.timeout);
			} catch (ConnectionFailure e) {
				log.fatal("Critical bug: failed to get respond from server");
				log.fatal(e + "");
				throw new RuntimeException();
			}

			try {
				LocalDate currentPicturesDate = PictureManager.getCurrentDate();
				serverResponse = sendRequestWithRespondToServer(
						(new CommandWrapper(getClientId(), CommandDescriptor.UPDATE_PRODUCTS_PICTURES,
								Serialization.serialize(currentPicturesDate))).serialize());
			} catch (/*SocketTimeoutException |*/ IOException | ConnectionFailure e) {
				log.fatal("Critical bug: failed to get respond from server");
				log.fatal(e + "");
				throw new RuntimeException();
			}

			terminateCommunication();

			cmdwrppr = getCommandWrapper(serverResponse);
			ResultDescriptor resDesc = cmdwrppr.getResultDescriptor();

			try {
				resultDescriptorHandler(resDesc);
			} catch (InvalidCommandDescriptor | InvalidParameter | CriticalError | EmployeeNotConnected
					| EmployeeAlreadyConnected | AuthenticationError | ProductNotExistInCatalog
					| ProductAlreadyExistInCatalog | ProductStillForSale | AmountBiggerThanAvailable
					| ProductPackageDoesNotExist | WorkerAlreadyExists | ParamIDAlreadyExists | ParamIDDoesNotExist
					| WorkerDoesNotExist | IngredientStillInUse | ManfacturerStillInUse e1) {

				log.fatal("Critical bug: this command result isn't supposed to return here");
				throw new RuntimeException();
			}
			
			if (resDesc == ResultDescriptor.SM_NO_UPDATE_NEEDED){
				log.info("No need to update the products pictures");
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

}
