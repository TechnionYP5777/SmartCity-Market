package CommandHandler;

import org.apache.log4j.Logger;

import com.google.gson.Gson;

import BasicCommonClasses.CatalogProduct;
import BasicCommonClasses.Login;
import BasicCommonClasses.ProductPackage;
import BasicCommonClasses.SmartCode;
import ClientServerApi.CommandDescriptor;
import ClientServerApi.CommandWrapper;
import ClientServerApi.ResultDescriptor;
import SQLDatabase.SQLDatabaseConnection;
import SQLDatabase.SQLDatabaseException.AuthenticationError;
import SQLDatabase.SQLDatabaseException.CriticalError;
import SQLDatabase.SQLDatabaseException.NumberOfConnectionsExceeded;
import SQLDatabase.SQLDatabaseException.ProductNotExistInCatalog;
import SQLDatabase.SQLDatabaseException.WorkerAlreadyConnected;
import SQLDatabase.SQLDatabaseException.WorkerNotConnected;

/**
 * CommandExecuter - This structure will execute the given command the clients.
 * 
 * @author Aviad Cohen
 * @since 2016-12-26
 */

public class CommandExecuter {
	
	static Logger log = Logger.getLogger(CommandExecuter.class.getName());
	
	private CommandWrapper inCommandWrapper;
	private CommandWrapper outCommandWrapper;
	
	public CommandExecuter(String command) {
		inCommandWrapper = CommandWrapper.deserialize(command);
	}
	
	private void loginCommand(SQLDatabaseConnection c) {		
		Login login = null;
		
		log.info("Login command called");
		
		try {
			login = new Gson().fromJson(inCommandWrapper.getData(), Login.class);	
		} catch (java.lang.RuntimeException e) {
			log.fatal("Failed to parse data for login command");
			
			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);
			
			return;
		}
		
		if (!login.isValid()) {
			log.info("Login command failed, username and password can't be empty");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_INVALID_PARAMETER);
			
			return;
		}
		
		//TODO Noam - add exception to WorkerAlreadyConnected when implemented in SQL + test for it in CommandExectuerLoginTest
		try {
			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_OK);
			outCommandWrapper.setSenderID((c.workerLogin(login.getUserName(), login.getPassword())));
			//TODO Noam please add here:  outCommandWrapper.setData(CLIENT_TYPE.serialize());
			//TODO Noam add information to the print (which type logged in)
			log.info("Login command succeded with sender ID " + outCommandWrapper.getSenderID());
		} catch (AuthenticationError e) {
			log.info("Login command failed, username dosen't exist or wrong password received");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_USERNAME_DOES_NOT_EXIST_WRONG_PASSWORD);			
		} catch (CriticalError e) {
			log.fatal("Login command failed, critical error occured from SQL Database connection");
			
			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);
		} catch (WorkerAlreadyConnected e) {
			log.info("Login command failed, user already connected");
			
			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_SENDER_IS_ALREADY_CONNECTED);
		} catch (NumberOfConnectionsExceeded e) {
			log.fatal("Login command failed, too much connections (try to increase TRYS_NUMBER)");
			
			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);
		}
		
		log.info( "Login with User " + login.getUserName() + " finished");
	}
	
	private void logoutCommand(SQLDatabaseConnection c) {
		String username;
		
		log.info("Logout command called");
		
		try {
			username = new Gson().fromJson(inCommandWrapper.getData(), String.class);
		} catch (java.lang.RuntimeException e) {
			log.fatal("Failed to parse data for logout command");
			
			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);
			
			return;
		}
		
		if ("".equals(username)) {
			log.info("Logout command failed, username can't be empty");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_INVALID_PARAMETER);
			
			return;
		}
		
		//TODO Noam - add exception to SM_SENDER_ID_DOES_NOT_EXIST,  when implemented in SQL + test for it in CommandExectuerLogoutTest
		try {
			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_OK);
			c.workerLogout(inCommandWrapper.getSenderID(), username);
			
			log.info("Logout command succeded with sender ID " + inCommandWrapper.getSenderID());
		} catch (WorkerNotConnected e) {
			log.info("Logout command failed, username dosen't login to the system");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_SENDER_IS_NOT_CONNECTED);			
		} catch (CriticalError e) {
			log.fatal("Logout command failed, critical error occured from SQL Database connection");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);
		}
		
		log.info("Logout with User " + username + " finished");
	}
	
	private void viewProductFromCatalogCommand(SQLDatabaseConnection c) {
		SmartCode smartCode = null;
		
		log.info("View Product From Catalog command called");
		
		try {
			smartCode = new Gson().fromJson(inCommandWrapper.getData(), SmartCode.class);			
		} catch (java.lang.RuntimeException e) {
			log.fatal("Failed to parse data for View Product From Catalog command");
			
			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);
			
			return;
		}
		
		if (!smartCode.isValid()) {
			log.info("View Product From Catalog command failed, barcode can't be negative");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_INVALID_PARAMETER);
			
			return;
		}
		
		//TODO Noam - add exception to SM_SENDER_ID_DOES_NOT_EXIST,  when implemented in SQL + test for it in CommandExectuerLogoutTest
		try {
			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_OK, c.getProductFromCatalog(inCommandWrapper.getSenderID(), smartCode.getBarcode()));
			
			log.info("Get product from catalog command secceeded with barcode " + smartCode.getBarcode());
		} catch (ProductNotExistInCatalog e) {
			log.info("Get product from catalog command failed, product dosen't exist in the system");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_CATALOG_PRODUCT_DOES_NOT_EXIST);
		} catch (WorkerNotConnected e) {
			log.info("Get product from catalog command failed, username dosen't login to the system");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_SENDER_IS_NOT_CONNECTED);
		} catch (CriticalError e) {
			log.fatal("Get product from catalog command failed, critical error occured from SQL Database connection");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);
		}
		
		log.info("View Product From Catalog with product barcode " + smartCode.getBarcode() + " finished");
	}
	
	private void addProductPackageToWarehouseCommand(SQLDatabaseConnection __) {
		ProductPackage productPackage = null;
		
		log.info("Add Product Package To Warehouse command called");
		
		try {
			productPackage = new Gson().fromJson(inCommandWrapper.getData(), ProductPackage.class);
		} catch (java.lang.RuntimeException e) {
			log.fatal("Failed to parse data for Add Product Package To Warehouse command");
			
			e.printStackTrace();
			
			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);
			
			return;
		}
		
		if (productPackage.isValid())
			//TODO Noam - call SQL command here
			
			log.info("Add Product Package To Warehouse with product package barcode "
					+ productPackage.getSmartCode().getBarcode() + " and amount " + productPackage.getAmount()
					+ " finished");
		else {
			log.info("Add Product Package To Warehouse command failed, product package is invalid");
			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_INVALID_PARAMETER);
		}
	}
	
	private void addProductToCatalogCommand(SQLDatabaseConnection __) {
		CatalogProduct catalogProduct = null;
		
		log.info("Add Product To Catalog command called");
		
		try {
			catalogProduct = new Gson().fromJson(inCommandWrapper.getData(), CatalogProduct.class);
		} catch (java.lang.RuntimeException e) {
			log.fatal("Failed to parse data for Add Product To Catalog command");
			
			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);
			
			return;
		}
		
		if (catalogProduct.isValid())
			//TODO Noam - call SQL command here
			
			log.info("Add Product To Catalog with product " +
					 catalogProduct + " finished");
		else {
			log.info("Add Product To Catalog command failed, product is invalid");
			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_INVALID_PARAMETER);
		}
	}
	
	private void removeProductFromCatalogCommand(SQLDatabaseConnection __) {	
		SmartCode smartCode = null;
		
		log.info("Remove Product From Catalog command called");
		
		try {
			smartCode = new Gson().fromJson(inCommandWrapper.getData(), SmartCode.class);
		} catch (java.lang.RuntimeException e) {
			log.fatal("Failed to parse data for Remove Product From Catalog command");
			
			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);
			
			return;
		}
		
		if (smartCode.isValid())
			//TODO Noam - call SQL command here
			
			log.info("Remove Product From Catalog with product barcode " +
					 smartCode.getBarcode() + " finished");
		else {
			log.info("Remove Product From Catalog command failed, barcode can't be negative");
			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_INVALID_PARAMETER);
		}
	}
	
	private void editProductFromCatalogCommand(SQLDatabaseConnection __) {
		SmartCode smartCode = null;
		
		log.info("Edit Product From Catalog command called");
		
		try {
			smartCode = new Gson().fromJson(inCommandWrapper.getData(), SmartCode.class);
		} catch (java.lang.RuntimeException e) {
			log.fatal("Failed to parse data for Edit Product From Catalog command");
			
			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);
			
			return;
		}
		
		if (smartCode.isValid())
			//TODO Noam - call SQL command here
			
			log.info("Edit Product From Catalog with product barcode " +
					 smartCode.getBarcode() + " finished");
		else {
			log.info("Edit Product From Catalog command failed, barcode can't be negative");
			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_INVALID_PARAMETER);
		}
	}
	
	private void placeProductPackageOnShelvesCommand(SQLDatabaseConnection __) {
		ProductPackage productPackage = null;
		
		log.info("Place Product Package On Shelves command called");
		
		try {
			productPackage = new Gson().fromJson(inCommandWrapper.getData(), ProductPackage.class);
		} catch (java.lang.RuntimeException e) {
			log.fatal("Failed to parse data for Place Product Package On Shelves command");
			
			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);
			
			return;
		}
		
		if (productPackage.isValid())
			//TODO Noam - call SQL command here
			
			log.info("Place Product Package On Shelves with product package barcode "
					+ productPackage.getSmartCode().getBarcode() + " and amount " + productPackage.getAmount()
					+ " finished");
		else {
			log.info("Place Product Package On Shelves command failed, product package is invalid");
			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_INVALID_PARAMETER);
		}
	}
	
	private void removeProductPackageFromStoreCommand(SQLDatabaseConnection __) {
		ProductPackage productPackage = null;
		
		log.info("Remove Product Package From Store command called");
		
		try {
			productPackage = new Gson().fromJson(inCommandWrapper.getData(), ProductPackage.class);
		} catch (java.lang.RuntimeException e) {
			log.fatal("Failed to parse data for Remove Product Package From Store command");
			
			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);
			
			return;
		}
		
		if (productPackage.isValid())
			//TODO Noam - call SQL command here
			
			log.info("Remove Product Package From Store with product package barcode "
					+ productPackage.getSmartCode().getBarcode() + " and amount " + productPackage.getAmount()
					+ " finished");
		else {
			log.info("Remove Product Package From Store command failed, product package is invalid");
			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_INVALID_PARAMETER);
		}
	}
	
	private void getProductPackageAmount(SQLDatabaseConnection __) {
		ProductPackage productPackage = null;
		
		log.info("Get Product Package Amount command called");
		
		try {
			productPackage = new Gson().fromJson(inCommandWrapper.getData(), ProductPackage.class);
		} catch (java.lang.RuntimeException e) {
			log.fatal("Failed to parse data for Get Product Package Amount command");
			
			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);
			
			return;
		}
		
		if (productPackage.isValid())
			//TODO Noam - call SQL command here
			
			//TODO Noam - add amount to the log print
			log.info("Get Product Package Amount returned with amount " + " finished");
		else {
			log.info("Remove Product Package From Store command failed, product package is invalid");
			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_INVALID_PARAMETER);
		}
	}
	
	public CommandWrapper execute(SQLDatabaseConnection c) {
		if (c == null) {
			log.fatal("Failed to get SQL Database Connection");
			
			return new CommandWrapper(ResultDescriptor.SM_ERR);
		}
		
		if ((inCommandWrapper.getCommandDescriptor() != CommandDescriptor.LOGIN) && (inCommandWrapper.getSenderID() < 0)) {
			log.info("Command failed, senderID can't be negative");

			return new CommandWrapper(ResultDescriptor.SM_INVALID_SENDER_ID);
		}
		
		switch(inCommandWrapper.getCommandDescriptor()) {
		case LOGIN:
			loginCommand(c);

			break;
			
		case LOGOUT:
			logoutCommand(c);
			
			break;
			
		case VIEW_PRODUCT_FROM_CATALOG:
			viewProductFromCatalogCommand(c);
			
			break;
			
		case ADD_PRODUCT_PACKAGE_TO_WAREHOUSE:
			addProductPackageToWarehouseCommand(c);
			
			break;
			
		case ADD_PRODUCT_TO_CATALOG:
			addProductToCatalogCommand(c);
			
			break;
			
		case REMOVE_PRODUCT_FROM_CATALOG:
			removeProductFromCatalogCommand(c);
			
			break;
			
		case EDIT_PRODUCT_FROM_CATALOG:
			editProductFromCatalogCommand(c);
			
			break;
			
		case PLACE_PRODUCT_PACKAGE_ON_SHELVES:
			placeProductPackageOnShelvesCommand(c);
			
			break;
			
		case REMOVE_PRODUCT_PACKAGE_FROM_STORE:
			removeProductPackageFromStoreCommand(c);
			
			break;
			
		case GET_PRODUCT_PACKAGE_AMOUNT:
			getProductPackageAmount(c);
			
			break;
			
		default:
			try {
				/* Command not supported, returning invalid command */
				outCommandWrapper = (CommandWrapper)inCommandWrapper.clone();
				outCommandWrapper.setResultDescriptor(ResultDescriptor.SM_INVALID_CMD_DESCRIPTOR);
			} catch (CloneNotSupportedException e) {
				outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);
				
				log.fatal("Failed to clone command");
			}
			break;
		}
		
		return outCommandWrapper;
	}
}
