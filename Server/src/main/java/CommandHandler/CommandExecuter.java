package CommandHandler;

import org.apache.log4j.Logger;

import com.google.gson.Gson;

import BasicCommonClasses.CatalogProduct;
import BasicCommonClasses.Login;
import BasicCommonClasses.PlaceInMarket;
import BasicCommonClasses.ProductPackage;
import BasicCommonClasses.SmartCode;
import ClientServerApi.CommandDescriptor;
import ClientServerApi.CommandWrapper;
import ClientServerApi.ResultDescriptor;
import SQLDatabase.SQLDatabaseConnection;
import SQLDatabase.SQLDatabaseException.AuthenticationError;
import SQLDatabase.SQLDatabaseException.CriticalError;
import SQLDatabase.SQLDatabaseException.IngredientNotExist;
import SQLDatabase.SQLDatabaseException.ManufacturerNotExist;
import SQLDatabase.SQLDatabaseException.NumberOfConnectionsExceeded;
import SQLDatabase.SQLDatabaseException.ProductAlreadyExistInCatalog;
import SQLDatabase.SQLDatabaseException.ProductNotExistInCatalog;
import SQLDatabase.SQLDatabaseException.ProductPackageAmountNotMatch;
import SQLDatabase.SQLDatabaseException.ProductPackageNotExist;
import SQLDatabase.SQLDatabaseException.ProductStillForSale;
import SQLDatabase.SQLDatabaseException.ClientAlreadyConnected;
import SQLDatabase.SQLDatabaseException.ClientNotConnected;

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

		try {
			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_OK);
			outCommandWrapper.setSenderID((c.workerLogin(login.getUserName(), login.getPassword())));

			try {
				outCommandWrapper.setData(c.getClientType(outCommandWrapper.getSenderID()));
			} catch (ClientNotConnected e) {
				e.printStackTrace();
				
				log.fatal("Client is not connected for sender ID " + outCommandWrapper.getSenderID());
			}
			
			log.info("Login command succeded with sender ID " +
					outCommandWrapper.getSenderID() + " with client type " + outCommandWrapper.getData());
		} catch (AuthenticationError e) {
			log.info("Login command failed, username dosen't exist or wrong password received");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_USERNAME_DOES_NOT_EXIST_WRONG_PASSWORD);
		} catch (CriticalError e) {
			log.fatal("Login command failed, critical error occured from SQL Database connection");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);
		} catch (ClientAlreadyConnected e) {
			log.info("Login command failed, user already connected");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_SENDER_IS_ALREADY_CONNECTED);
		} catch (NumberOfConnectionsExceeded e) {
			log.fatal("Login command failed, too much connections (try to increase TRYS_NUMBER)");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);
		}

		log.info("Login with User " + login.getUserName() + " finished");
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

		try {
			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_OK);
			c.workerLogout(inCommandWrapper.getSenderID(), username);

			log.info("Logout command succeded with sender ID " + inCommandWrapper.getSenderID());
		} catch (ClientNotConnected e) {
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

		try {
			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_OK,
					c.getProductFromCatalog(inCommandWrapper.getSenderID(), smartCode.getBarcode()));

			log.info("Get product from catalog command secceeded with barcode " + smartCode.getBarcode());
		} catch (ProductNotExistInCatalog e) {
			log.info("Get product from catalog command failed, product dosen't exist in the system");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_CATALOG_PRODUCT_DOES_NOT_EXIST);
		} catch (ClientNotConnected e) {
			log.info("Get product from catalog command failed, username dosen't login to the system");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_SENDER_IS_NOT_CONNECTED);
		} catch (CriticalError e) {
			log.fatal("Get product from catalog command failed, critical error occured from SQL Database connection");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);
		}

		log.info("View Product From Catalog with product barcode " + smartCode.getBarcode() + " finished");
	}

	private void addProductPackageToWarehouseCommand(SQLDatabaseConnection c) {
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

		if (!productPackage.isValid()) {
			log.info("Add Product Package To Warehouse command failed, product package is invalid");
			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_INVALID_PARAMETER);
		} else {
			try {
				c.addProductPackageToWarehouse(inCommandWrapper.getSenderID(), productPackage);
				outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_OK);
			} catch (CriticalError e) {
				log.fatal(
						"Add Product Package To Warehouse command failed, critical error occured from SQL Database connection");

				outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);
			} catch (ClientNotConnected e) {
				log.info("Add Product Package To Warehouse command failed, username dosen't login to the system");

				outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_SENDER_IS_NOT_CONNECTED);
			} catch (ProductNotExistInCatalog e) {
				log.info("Add Product Package To Warehouse command failed, product dosen't exist in the system");

				outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_CATALOG_PRODUCT_DOES_NOT_EXIST);
			}

			log.info("Add Product Package To Warehouse with product package barcode "
					+ productPackage.getSmartCode().getBarcode() + " and amount " + productPackage.getAmount()
					+ " finished");
		}
	}

	private void addProductToCatalogCommand(SQLDatabaseConnection c) {
		CatalogProduct catalogProduct = null;

		log.info("Add Product To Catalog command called");

		try {
			catalogProduct = new Gson().fromJson(inCommandWrapper.getData(), CatalogProduct.class);
		} catch (java.lang.RuntimeException e) {
			log.fatal("Failed to parse data for Add Product To Catalog command");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);

			return;
		}

		if (!catalogProduct.isValid()) {

			log.info("Add Product To Catalog command failed, product is invalid");
			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_INVALID_PARAMETER);

		} else {
			try {
				c.addProductToCatalog(inCommandWrapper.getSenderID(), catalogProduct);
				outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_OK);
			} catch (CriticalError e) {
				log.fatal("Add Product To Catalog command failed, critical error occured from SQL Database connection");

				outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);
			} catch (ClientNotConnected e) {
				log.info("Add Product To Catalog command failed, username dosen't login to the system");

				outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_SENDER_IS_NOT_CONNECTED);
			} catch (ProductAlreadyExistInCatalog e) {
				log.info("Add Product To Catalog command failed, product already exist in the system");

				outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_CATALOG_PRODUCT_ALREADY_EXISTS);
			} catch (IngredientNotExist e) {
				log.info(
						"Add Product To Catalog command failed, ingredient in the product dosen't exist in the system");

				outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_INVALID_PARAMETER);
			} catch (ManufacturerNotExist e) {
				log.info(
						"Add Product To Catalog command failed, manufacturer in the product dosen't exist in the system");

				outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_INVALID_PARAMETER);
			}

			log.info("Add Product To Catalog with product " + catalogProduct + " finished");
		}
	}

	private void removeProductFromCatalogCommand(SQLDatabaseConnection c) {
		SmartCode smartCode = null;

		log.info("Remove Product From Catalog command called");

		try {
			smartCode = new Gson().fromJson(inCommandWrapper.getData(), SmartCode.class);
		} catch (java.lang.RuntimeException e) {
			log.fatal("Failed to parse data for Remove Product From Catalog command");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);

			return;
		}

		if (!smartCode.isValid()) {
			log.info("Remove Product From Catalog command failed, barcode can't be negative");
			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_INVALID_PARAMETER);

		} else {
			try {
				c.removeProductFromCatalog(inCommandWrapper.getSenderID(), smartCode);
				outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_OK);

			} catch (CriticalError e) {
				log.fatal(
						"Remove Product From Catalog command failed, critical error occured from SQL Database connection");

				outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);
			} catch (ClientNotConnected e) {
				log.info("Remove Product From Catalog command failed, username dosen't login to the system");

				outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_SENDER_IS_NOT_CONNECTED);
			} catch (ProductNotExistInCatalog e) {
				log.info("Remove Product From Catalog command failed, product dosen't exist in the system");

				outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_CATALOG_PRODUCT_DOES_NOT_EXIST);
			} catch (ProductStillForSale e) {
				log.info("Remove Product From Catalog command failed, product still for sale");

				outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_CATALOG_PRODUCT_STILL_FOR_SALE);
			}

			log.info("Remove Product From Catalog with product barcode " + smartCode.getBarcode() + " finished");
		}
	}

	private void editProductFromCatalogCommand(SQLDatabaseConnection c) {
		SmartCode smartCode = null;

		log.info("Edit Product From Catalog command called");

		try {
			smartCode = new Gson().fromJson(inCommandWrapper.getData(), SmartCode.class);
		} catch (java.lang.RuntimeException e) {
			log.fatal("Failed to parse data for Edit Product From Catalog command");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);

			return;
		}

		if (!smartCode.isValid()) {
			log.info("Edit Product From Catalog command failed, barcode can't be negative");
			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_INVALID_PARAMETER);

		} else {
			// TODO Aviad - how can i update product with smartcode??
			// c.editProductInCatalog(inCommandWrapper.getSenderID(),
			// smartCode);
			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_OK);

			log.info("Edit Product From Catalog with product barcode " + smartCode.getBarcode() + " finished");
		}
	}

	private void placeProductPackageOnShelvesCommand(SQLDatabaseConnection c) {
		ProductPackage productPackage = null;

		log.info("Place Product Package On Shelves command called");

		try {
			productPackage = new Gson().fromJson(inCommandWrapper.getData(), ProductPackage.class);
		} catch (java.lang.RuntimeException e) {
			log.fatal("Failed to parse data for Place Product Package On Shelves command");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);

			return;
		}

		if (!productPackage.isValid()) {
			log.info("Place Product Package On Shelves command failed, product package is invalid");
			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_INVALID_PARAMETER);

		} else {

			try {
				c.placeProductPackageOnShelves(inCommandWrapper.getSenderID(), productPackage);
				outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_OK);
			} catch (CriticalError e) {
				log.fatal(
						"Place Product Package On Shelves command failed, critical error occured from SQL Database connection");

				outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);
			} catch (ClientNotConnected e) {
				log.info("Place Product Package On Shelves command failed, username dosen't login to the system");

				outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_SENDER_IS_NOT_CONNECTED);
			} catch (ProductNotExistInCatalog e) {
				log.info("Place Product Package On Shelves command failed, product dosen't exist in the system");

				outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_CATALOG_PRODUCT_DOES_NOT_EXIST);
			} catch (ProductPackageAmountNotMatch e) {
				log.info("Place Product Package On Shelves command failed, try to place more than available");

				outCommandWrapper = new CommandWrapper(
						ResultDescriptor.SM_PRODUCT_PACKAGE_AMOUNT_BIGGER_THEN_AVAILABLE);
			} catch (ProductPackageNotExist e) {
				log.info("Place Product Package On Shelves command failed, package dosen't exist in the system");

				outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_PRODUCT_PACKAGE_DOES_NOT_EXIST);
			}

			log.info("Place Product Package On Shelves with product package barcode "
					+ productPackage.getSmartCode().getBarcode() + " and amount " + productPackage.getAmount()
					+ " finished");
		}
	}

	private void removeProductPackageFromStoreCommand(SQLDatabaseConnection c) {
		ProductPackage productPackage = null;

		log.info("Remove Product Package From Store command called");

		try {
			productPackage = new Gson().fromJson(inCommandWrapper.getData(), ProductPackage.class);
		} catch (java.lang.RuntimeException e) {
			log.fatal("Failed to parse data for Remove Product Package From Store command");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);

			return;
		}

		if (!productPackage.isValid()) {
			log.info("Remove Product Package From Store command failed, product package is invalid");
			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_INVALID_PARAMETER);

		} else {

			try {
				if (productPackage.getLocation().equals(PlaceInMarket.STORE))
					c.removeProductPackageFromShelves(inCommandWrapper.getSenderID(), productPackage);
				else
					c.removeProductPackageFromWarehouse(inCommandWrapper.getSenderID(), productPackage);

				outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_OK);
			} catch (CriticalError e) {
				log.fatal(
						"Remove Product Package From Store command failed, critical error occured from SQL Database connection");

				outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);
			} catch (ClientNotConnected e) {
				log.info("Remove Product Package From Store command failed, username dosen't login to the system");

				outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_SENDER_IS_NOT_CONNECTED);
			} catch (ProductNotExistInCatalog e) {
				log.info("Remove Product Package From Store command failed, product dosen't exist in the system");

				outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_CATALOG_PRODUCT_DOES_NOT_EXIST);
			} catch (ProductPackageAmountNotMatch e) {
				log.info("Remove Product Package From Store command failed, try to place more than available");

				outCommandWrapper = new CommandWrapper(
						ResultDescriptor.SM_PRODUCT_PACKAGE_AMOUNT_BIGGER_THEN_AVAILABLE);
			} catch (ProductPackageNotExist e) {
				log.info("Remove Product Package From Store command failed, package dosen't exist in the system");

				outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_PRODUCT_PACKAGE_DOES_NOT_EXIST);
			}

			log.info("Remove Product Package From Store with product package barcode "
					+ productPackage.getSmartCode().getBarcode() + " and amount " + productPackage.getAmount()
					+ " finished");

		}
	}

	private void getProductPackageAmount(SQLDatabaseConnection c) {
		ProductPackage productPackage = null;

		log.info("Get Product Package Amount command called");

		try {
			productPackage = new Gson().fromJson(inCommandWrapper.getData(), ProductPackage.class);
		} catch (java.lang.RuntimeException e) {
			log.fatal("Failed to parse data for Get Product Package Amount command");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);

			return;
		}

		if (!productPackage.isValid()) {
			log.info("Get Product Package Amount command failed, product package is invalid");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_INVALID_PARAMETER);
		} else {
			String amount = "";

			try {
				amount = productPackage.getLocation().equals(PlaceInMarket.STORE)
						? c.getProductPackageAmonutOnShelves(inCommandWrapper.getSenderID(), productPackage)
						: c.getProductPackageAmonutInWarehouse(inCommandWrapper.getSenderID(), productPackage);

				outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_OK, amount);
			} catch (CriticalError e) {
				log.fatal(
						"Get Product Package Amount command failed, critical error occured from SQL Database connection");

				outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);
			} catch (ClientNotConnected e) {
				log.info("Get Product Package Amount command failed, username dosen't login to the system");

				outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_SENDER_IS_NOT_CONNECTED);
			} catch (ProductNotExistInCatalog e) {
				log.info("Get Product Package Amount command failed, product dosen't exist in the system");

				outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_CATALOG_PRODUCT_DOES_NOT_EXIST);
			}

			log.info("Get Product Package Amount returned with amount " + amount + " finished");
		}
	}

	public CommandWrapper execute(SQLDatabaseConnection c) {
		if (c == null) {
			log.fatal("Failed to get SQL Database Connection");

			return new CommandWrapper(ResultDescriptor.SM_ERR);
		}

		if ((inCommandWrapper.getCommandDescriptor() != CommandDescriptor.LOGIN)
				&& (inCommandWrapper.getSenderID() < 0)) {
			log.info("Command failed, senderID can't be negative");

			return new CommandWrapper(ResultDescriptor.SM_INVALID_PARAMETER);
		}

		switch (inCommandWrapper.getCommandDescriptor()) {
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
				outCommandWrapper = (CommandWrapper) inCommandWrapper.clone();
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
