package CommandHandler;

import org.apache.log4j.Logger;

import BasicCommonClasses.CatalogProduct;
import BasicCommonClasses.CustomerProfile;
import BasicCommonClasses.Ingredient;
import BasicCommonClasses.Login;
import BasicCommonClasses.Manufacturer;
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
import SQLDatabase.SQLDatabaseException.NoGroceryListToRestore;
import SQLDatabase.SQLDatabaseException.NumberOfConnectionsExceeded;
import SQLDatabase.SQLDatabaseException.ProductAlreadyExistInCatalog;
import SQLDatabase.SQLDatabaseException.ProductNotExistInCatalog;
import SQLDatabase.SQLDatabaseException.ProductPackageAmountNotMatch;
import SQLDatabase.SQLDatabaseException.ProductPackageNotExist;
import SQLDatabase.SQLDatabaseException.ProductStillForSale;
import UtilsImplementations.Serialization;
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

	//TODO Aviad - remove login command and splitting login command finished
	private void loginCommand(SQLDatabaseConnection c) {
		Login login = null;

		log.info("Login command called");

		try {
			login = Serialization.deserialize(inCommandWrapper.getData(), Login.class);
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
			outCommandWrapper.setSenderID((c.login(login.getUserName(), login.getPassword())));

			try {
				outCommandWrapper.setData(c.getClientType(outCommandWrapper.getSenderID()));
			} catch (ClientNotConnected e) {
				e.printStackTrace();

				log.fatal("Client is not connected for sender ID " + outCommandWrapper.getSenderID());
			}

			log.info("Login command succeded with sender ID " + outCommandWrapper.getSenderID() + " with client type "
					+ outCommandWrapper.getData());
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

	private void loginEmployeeCommand(SQLDatabaseConnection c) {
		Login login = null;

		log.info("Login employee command called");

		try {
			login = Serialization.deserialize(inCommandWrapper.getData(), Login.class);
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
			//TODO Noam - change the calling of this command, all code should stay the same
			outCommandWrapper.setSenderID((c.login(login.getUserName(), login.getPassword())));

			try {
				outCommandWrapper.setData(c.getClientType(outCommandWrapper.getSenderID()));
			} catch (ClientNotConnected e) {
				e.printStackTrace();

				log.fatal("Client is not connected for sender ID " + outCommandWrapper.getSenderID());
			}

			log.info("Login command succeded with sender ID " + outCommandWrapper.getSenderID() + " with client type "
					+ outCommandWrapper.getData());
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

		log.info("Login employee with User " + login.getUserName() + " finished");
	}
	
	private void loginClientCommand(SQLDatabaseConnection c) {
		Login login = null;

		log.info("Login client command called");

		try {
			login = Serialization.deserialize(inCommandWrapper.getData(), Login.class);
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
			//TODO Noam - change the calling of this command, need to add CustomerProfile for login command here
			outCommandWrapper.setSenderID((c.login(login.getUserName(), login.getPassword())));

			try {
				outCommandWrapper.setData(c.getClientType(outCommandWrapper.getSenderID()));
			} catch (ClientNotConnected e) {
				e.printStackTrace();

				log.fatal("Client is not connected for sender ID " + outCommandWrapper.getSenderID());
			}

			log.info("Login command succeded with sender ID " + outCommandWrapper.getSenderID() + " with client type "
					+ outCommandWrapper.getData());
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

		log.info("Login client with User " + login.getUserName() + " finished");
	}
	
	private void logoutCommand(SQLDatabaseConnection c) {
		String username;

		log.info("Logout command called");

		try {
			username = Serialization.deserialize(inCommandWrapper.getData(), String.class);
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

			c.logout(inCommandWrapper.getSenderID(), username);

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

	private void isLoggedInCommand(SQLDatabaseConnection c) {
		log.info("Is logged in command called with senderID " + inCommandWrapper.getSenderID());

		try {
			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_OK,
					Serialization.serialize(c.isClientLoggedIn(inCommandWrapper.getSenderID())));
		} catch (CriticalError e) {
			log.fatal("Is logged in command failed, critical error occured from SQL Database connection");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);
		}

		log.info("Is logged in command finished");
	}

	private void viewProductFromCatalogCommand(SQLDatabaseConnection c) {
		SmartCode smartCode = null;

		log.info("View Product From Catalog command called");

		try {
			smartCode = Serialization.deserialize(inCommandWrapper.getData(), SmartCode.class);
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
			productPackage = Serialization.deserialize(inCommandWrapper.getData(), ProductPackage.class);
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
			catalogProduct = Serialization.deserialize(inCommandWrapper.getData(), CatalogProduct.class);
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
			smartCode = Serialization.deserialize(inCommandWrapper.getData(), SmartCode.class);
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
		CatalogProduct catalogProduct = null;

		log.info("Edit Product From Catalog command called");

		try {
			catalogProduct = Serialization.deserialize(inCommandWrapper.getData(), CatalogProduct.class);
		} catch (java.lang.RuntimeException e) {
			log.fatal("Failed to parse data for Edit Product From Catalog command");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);

			return;
		}

		if (!catalogProduct.isValid()) {
			log.info("Edit Product From Catalog command failed, barcode can't be negative");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_INVALID_PARAMETER);
		} else {
			try {
				outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_OK);

				c.editProductInCatalog(inCommandWrapper.getSenderID(), catalogProduct);
			} catch (CriticalError e) {
				log.fatal(
						"Edit Product From Catalog command failed, critical error occured from SQL Database connection");

				outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);
			} catch (ClientNotConnected e) {
				log.info("Edit Product From Catalog command failed, username dosen't login to the system");

				outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_SENDER_IS_NOT_CONNECTED);
			} catch (ProductNotExistInCatalog e) {
				log.info("Edit Product From Catalog command failed, product dosen't exist in the system");

				outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_CATALOG_PRODUCT_DOES_NOT_EXIST);
			} catch (IngredientNotExist e) {
				log.info(
						"Edit Product From Catalog command failed, ingredient in the product dosen't exist in the system");

				outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_INVALID_PARAMETER);
			} catch (ManufacturerNotExist e) {
				log.info(
						"Edit Product From Catalog command failed, manufacturer in the product dosen't exist in the system");

				outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_INVALID_PARAMETER);
			}

			log.info("Edit Product From Catalog with product " + catalogProduct + " finished");
		}
	}

	private void placeProductPackageOnShelvesCommand(SQLDatabaseConnection c) {
		ProductPackage productPackage = null;

		log.info("Place Product Package On Shelves command called");

		try {
			productPackage = Serialization.deserialize(inCommandWrapper.getData(), ProductPackage.class);
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
			productPackage = Serialization.deserialize(inCommandWrapper.getData(), ProductPackage.class);
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
				if (productPackage.getLocation().getPlaceInMarket().equals(PlaceInMarket.STORE))
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
			productPackage = Serialization.deserialize(inCommandWrapper.getData(), ProductPackage.class);
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
				amount = productPackage.getLocation().getPlaceInMarket().equals(PlaceInMarket.STORE)
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

	private void loadGroceryList(SQLDatabaseConnection c) {
		log.info("Load Grocery List from serderID " + inCommandWrapper.getSenderID() + " command called");

		try {
			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_OK,
					c.cartRestoreGroceryList(inCommandWrapper.getSenderID()));
		} catch (CriticalError e) {
			log.fatal("Load Grocery List command failed, critical error occured from SQL Database connection");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);
		} catch (NoGroceryListToRestore e) {
			log.info("Load Grocery List command failed, no grocery list for senderID " + inCommandWrapper.getSenderID()
					+ " to restore");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_SENDER_IS_NOT_CONNECTED);
		}

		log.info("Load Grocery List from serderID " + inCommandWrapper.getSenderID() + " command finished");
	}

	private void addProductToGroceryList(SQLDatabaseConnection c) {
		ProductPackage productPackage = null;

		log.info("Add Product To Grocery List from serderID " + inCommandWrapper.getSenderID() + " command called");

		try {
			productPackage = Serialization.deserialize(inCommandWrapper.getData(), ProductPackage.class);
		} catch (java.lang.RuntimeException e) {
			log.fatal("Failed to parse data for Add Product To Grocery List command");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);

			return;
		}

		log.info("Trying to add product package " + productPackage + " to grocery list");

		if (!productPackage.isValid()) {
			log.info("Add Product To Grocery List command failed, product package is invalid");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_INVALID_PARAMETER);
		} else
			try {
				c.addProductToGroceryList(inCommandWrapper.getSenderID(), productPackage);

				outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_OK);
			} catch (CriticalError e) {
				log.fatal(
						"Add Product To Grocery List command failed, critical error occured from SQL Database connection");

				outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);
			} catch (ClientNotConnected e) {
				log.info("Add Product To Grocery List command failed, client is not connected");

				outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_SENDER_IS_NOT_CONNECTED);
			} catch (ProductNotExistInCatalog e) {
				log.info("Add Product To Grocery List command failed, product is not exist in catalog");

				outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_CATALOG_PRODUCT_DOES_NOT_EXIST);
			} catch (ProductPackageAmountNotMatch e) {
				log.info("Add Product To Grocery List command failed, product package amount does not match");

				outCommandWrapper = new CommandWrapper(
						ResultDescriptor.SM_PRODUCT_PACKAGE_AMOUNT_BIGGER_THEN_AVAILABLE);
			} catch (ProductPackageNotExist e) {
				log.info("Add Product To Grocery List command failed, product package does not exist");

				outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_PRODUCT_PACKAGE_DOES_NOT_EXIST);
			}

		log.info("Add Product To Grocery List with product package " + productPackage + " finished");
	}

	private void removeProductFromGroceryList(SQLDatabaseConnection c) {
		ProductPackage productPackage = null;

		log.info(
				"Remove Product From Grocery List from serderID " + inCommandWrapper.getSenderID() + " command called");

		try {
			productPackage = Serialization.deserialize(inCommandWrapper.getData(), ProductPackage.class);
		} catch (java.lang.RuntimeException e) {
			log.fatal("Failed to parse data for Remove Product From Grocery List command");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);

			return;
		}

		log.info("Trying to remove product package " + productPackage + " from grocery list");

		if (!productPackage.isValid()) {
			log.info("Remove Product From Grocery List command failed, product package is invalid");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_INVALID_PARAMETER);
		} else
			try {
				c.removeProductFromGroceryList(inCommandWrapper.getSenderID(), productPackage);

				outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_OK);
			} catch (CriticalError e) {
				log.fatal(
						"Remove Product From Grocery List command failed, critical error occured from SQL Database connection");

				outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);
			} catch (ClientNotConnected e) {
				log.info("Remove Product From Grocery List command failed, client is not connected");

				outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_SENDER_IS_NOT_CONNECTED);
			} catch (ProductNotExistInCatalog e) {
				log.info("Remove Product From Grocery List command failed, product is not exist in catalog");

				outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_CATALOG_PRODUCT_DOES_NOT_EXIST);
			} catch (ProductPackageAmountNotMatch e) {
				log.info("Remove Product From Grocery List command failed, product package amount does not match");

				outCommandWrapper = new CommandWrapper(
						ResultDescriptor.SM_PRODUCT_PACKAGE_AMOUNT_BIGGER_THEN_AVAILABLE);
			} catch (ProductPackageNotExist e) {
				log.info("Remove Product From Grocery List command failed, product package does not exist");

				outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_PRODUCT_PACKAGE_DOES_NOT_EXIST);
			}

		log.info("Remove Product From Grocery List with product package " + productPackage + " finished");
	}

	private void checkoutGroceryList(SQLDatabaseConnection c) {
		log.info("Checkout Grocery List from serderID " + inCommandWrapper.getSenderID() + " command called");

		try {
			c.cartCheckout(inCommandWrapper.getSenderID());

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_OK);
		} catch (CriticalError e) {
			log.fatal("Checkout Grocery List command failed, critical error occured from SQL Database connection");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);
		} catch (ClientNotConnected e) {
			log.info("Checkout Grocery List command failed, client is not connected");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_SENDER_IS_NOT_CONNECTED);
		}

		log.info("Checkout Grocery List from serderID " + inCommandWrapper.getSenderID() + " finished");
	}

	private void registerNewWorker(SQLDatabaseConnection c) {
		Login login = null;

		log.info("Register new worker from serderID " + inCommandWrapper.getSenderID() + " command called");

		try {
			login = Serialization.deserialize(inCommandWrapper.getData(), Login.class);
		} catch (java.lang.RuntimeException e) {
			log.fatal("Failed to parse data for Register New Worker command");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);

			return;
		}

		log.info("Trying to register new worker " + login.getUserName() + " to system");

		//TODO Noam - Call sql function here, don't forget to validate that sender ID is manager

		log.info("Register new worker " + login.getUserName() + " to system finished");
	}
	
	private void removeWorker(SQLDatabaseConnection c) {
		String username = null;

		log.info("Remove worker from serderID " + inCommandWrapper.getSenderID() + " command called");

		try {
			username = Serialization.deserialize(inCommandWrapper.getData(), String.class);
		} catch (java.lang.RuntimeException e) {
			log.fatal("Failed to parse data for Remove Worker command");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);

			return;
		}

		log.info("Trying to remove worker " + username + " from system");

		//TODO Noam - Call sql function here, don't forget to validate that sender ID is manager

		log.info("Remove worker " + username + " from system finished");
	}
	
	private void registerNewCustomer(SQLDatabaseConnection c) {
		CustomerProfile profile = null;

		log.info("Register new customer from serderID " + inCommandWrapper.getSenderID() + " command called");

		try {
			profile = Serialization.deserialize(inCommandWrapper.getData(), CustomerProfile.class);
		} catch (java.lang.RuntimeException e) {
			log.fatal("Failed to parse data for Register New Customer command");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);

			return;
		}

		log.info("Trying to register new customer " + profile + " to system");

		//TODO Noam - Call sql function here

		log.info("Register new customer " + profile + " to system finished");
	}
	
	private void removeCustomer(SQLDatabaseConnection c) {
		String username = null;

		log.info("Remove customer from serderID " + inCommandWrapper.getSenderID() + " command called");

		try {
			username = Serialization.deserialize(inCommandWrapper.getData(), String.class);
		} catch (java.lang.RuntimeException e) {
			log.fatal("Failed to parse data for Remove Customer command");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);

			return;
		}

		log.info("Trying to remove customer " + username + " from system");

		//TODO Noam - Call sql function here, don't forget to validate that sender ID is manager or the client is trying to delete itself

		log.info("Remove customer " + username + " from system finished");
	}
	
	private void updateCustomerProfile(SQLDatabaseConnection c) {
		CustomerProfile profile = null;

		log.info("Update customer from serderID " + inCommandWrapper.getSenderID() + " command called");

		try {
			profile = Serialization.deserialize(inCommandWrapper.getData(), CustomerProfile.class);
		} catch (java.lang.RuntimeException e) {
			log.fatal("Failed to parse data for Update Customer Data command");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);

			return;
		}

		log.info("Trying to update customer " + profile + " to system");

		//TODO Noam - Call sql function here

		log.info("Update customer " + profile + " to system finished");
	}
	
	private void addNewIngredient(SQLDatabaseConnection c) {
		Ingredient ingredient = null;

		log.info("Register new ingredient from serderID " + inCommandWrapper.getSenderID() + " command called");

		try {
			ingredient = Serialization.deserialize(inCommandWrapper.getData(), Ingredient.class);
		} catch (java.lang.RuntimeException e) {
			log.fatal("Failed to parse data for Add New Ingredient command");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);

			return;
		}

		log.info("Trying to add new ingredient " + ingredient + " to system");

		//TODO Noam - Call sql function here

		log.info("Add new ingredient " + ingredient + " to system finished");
	}
	
	private void removeIngredient(SQLDatabaseConnection c) {
		Ingredient ingredient = null;

		log.info("Remove ingredient from serderID " + inCommandWrapper.getSenderID() + " command called");

		try {
			ingredient = Serialization.deserialize(inCommandWrapper.getData(), Ingredient.class);
		} catch (java.lang.RuntimeException e) {
			log.fatal("Failed to parse data for Remove Ingredient command");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);

			return;
		}

		log.info("Trying to remove ingredient " + ingredient + " from system");

		//TODO Noam - Call sql function here, don't forget to validate that sender ID is manager or the client is trying to delete itself

		log.info("Remove ingredient " + ingredient + " from system finished");
	}

	private void addNewManufacturer(SQLDatabaseConnection c) {
		Manufacturer manufacturer = null;

		log.info("Register new manufacturer from serderID " + inCommandWrapper.getSenderID() + " command called");

		try {
			manufacturer = Serialization.deserialize(inCommandWrapper.getData(), Manufacturer.class);
		} catch (java.lang.RuntimeException e) {
			log.fatal("Failed to parse data for Add New Manufacturer command");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);

			return;
		}

		log.info("Trying to add new manufacturer " + manufacturer + " to system");

		//TODO Noam - Call sql function here

		log.info("Add new manufacturer " + manufacturer + " to system finished");
	}
	
	private void removeManufacturer(SQLDatabaseConnection c) {
		Manufacturer manufacturer = null;

		log.info("Remove manufacturer from serderID " + inCommandWrapper.getSenderID() + " command called");

		try {
			manufacturer = Serialization.deserialize(inCommandWrapper.getData(), Manufacturer.class);
		} catch (java.lang.RuntimeException e) {
			log.fatal("Failed to parse data for Remove Manufacturer command");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);

			return;
		}

		log.info("Trying to remove manufacturer " + manufacturer + " from system");

		//TODO Noam - Call sql function here

		log.info("Remove manufacturer " + manufacturer + " from system finished");
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
			
		case LOGIN_EMPLOYEE:
			loginEmployeeCommand(c);

			break;
			
		case LOGIN_CLIENT:
			loginClientCommand(c);

			break;

		case LOGOUT:
			logoutCommand(c);

			break;

		case IS_LOGGED_IN:
			isLoggedInCommand(c);

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

		case LOAD_GROCERY_LIST:
			loadGroceryList(c);

			break;

		case ADD_PRODUCT_TO_GROCERY_LIST:
			addProductToGroceryList(c);

			break;

		case REMOVE_PRODUCT_FROM_GROCERY_LIST:
			removeProductFromGroceryList(c);

			break;

		case CHECKOUT_GROCERY_LIST:
			checkoutGroceryList(c);

			break;
			
		case REGISTER_NEW_WORKER:
			registerNewWorker(c);

			break;

		case REMOVE_WORKER:
			removeWorker(c);

			break;	

		case REGISTER_NEW_CUSTOMER:
			registerNewCustomer(c);

			break;

		case REMOVE_CUSTOMER:
			removeCustomer(c);

			break;
			
		case UPDATE_CUSTOMER_PROFILE:
			updateCustomerProfile(c);

			break;
			
		case ADD_INGREDIENT:
			addNewIngredient(c);

			break;
			
		case REMOVE_INGREDIENT:
			removeIngredient(c);

			break;
			
		case ADD_MANUFACTURER:
			addNewManufacturer(c);

			break;
			
		case REMOVE_MANUFACTURER:
			removeManufacturer(c);

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
