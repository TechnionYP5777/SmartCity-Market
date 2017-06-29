package CommandHandler;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipFile;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import BasicCommonClasses.CatalogProduct;
import BasicCommonClasses.CustomerProfile;
import BasicCommonClasses.Ingredient;
import BasicCommonClasses.Login;
import BasicCommonClasses.Manufacturer;
import BasicCommonClasses.PlaceInMarket;
import BasicCommonClasses.ProductPackage;
import BasicCommonClasses.Sale;
import BasicCommonClasses.SmartCode;
import ClientServerApi.ClientServerDefs;
import ClientServerApi.CommandDescriptor;
import ClientServerApi.CommandWrapper;
import ClientServerApi.ResultDescriptor;
import CommonDefs.GuiCommonDefs;
import PicturesHandler.PictureManager;
import SQLDatabase.SQLDatabaseConnection;
import SQLDatabase.SQLDatabaseException.AuthenticationError;
import SQLDatabase.SQLDatabaseException.CriticalError;
import SQLDatabase.SQLDatabaseException.GroceryListIsEmpty;
import SQLDatabase.SQLDatabaseException.IngredientNotExist;
import SQLDatabase.SQLDatabaseException.IngredientStillUsed;
import SQLDatabase.SQLDatabaseException.ManufacturerNotExist;
import SQLDatabase.SQLDatabaseException.ManufacturerStillUsed;
import SQLDatabase.SQLDatabaseException.NoGroceryListToRestore;
import SQLDatabase.SQLDatabaseException.NumberOfConnectionsExceeded;
import SQLDatabase.SQLDatabaseException.ProductAlreadyExistInCatalog;
import SQLDatabase.SQLDatabaseException.ProductNotExistInCatalog;
import SQLDatabase.SQLDatabaseException.ProductPackageAmountNotMatch;
import SQLDatabase.SQLDatabaseException.ProductPackageNotExist;
import SQLDatabase.SQLDatabaseException.ProductStillForSale;
import SQLDatabase.SQLDatabaseException.SaleAlreadyExist;
import SQLDatabase.SQLDatabaseException.SaleNotExist;
import SQLDatabase.SQLDatabaseException.SaleStillUsed;
import UtilsImplementations.Packing;
import UtilsImplementations.Serialization;
import SQLDatabase.SQLDatabaseException.ClientAlreadyConnected;
import SQLDatabase.SQLDatabaseException.ClientAlreadyExist;
import SQLDatabase.SQLDatabaseException.ClientNotConnected;
import SQLDatabase.SQLDatabaseException.ClientNotExist;

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
		try {
			inCommandWrapper = CommandWrapper.deserialize(command);
		} catch (Exception e) {
			log.fatal("Failed to parse data for command");
		}
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
			outCommandWrapper.setSenderID(c.loginWorker(login.getUserName(), login.getPassword()));

			try {
				outCommandWrapper.setData(c.getClientType(outCommandWrapper.getSenderID()));
			} catch (ClientNotConnected e) {
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
			int senderID = c.loginCustomer(login.getUserName(), login.getPassword());
			outCommandWrapper.setSenderID(senderID);

			try {
				outCommandWrapper.setData(c.getCustomerProfile(login.getUserName()));
			} catch (ClientNotExist e) {
				log.fatal("Client is not exist with username: " + outCommandWrapper.getSenderID());
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

	private void updateProductsPictures(SQLDatabaseConnection __) {
		LocalDate customerDate;
		
		log.info("Update products pictures command called with senderID " + inCommandWrapper.getSenderID());

		try {
			customerDate = Serialization.deserialize(inCommandWrapper.getData(), LocalDate.class);
		} catch (java.lang.RuntimeException e) {
			log.fatal("Failed to parse data for Update products pictures command");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);

			return;
		}
		
		try {
			if (PictureManager.checkIfMostUpdate(customerDate)) {
				log.info("No update needed products");
				
				outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_NO_UPDATE_NEEDED);

			} else {
				File productsPicturesFolder = new File(GuiCommonDefs.productsPicturesFolderPath);
				File[] pics = productsPicturesFolder.listFiles();
				ZipFile pictures = Packing.pack(pics, GuiCommonDefs.productsPicturesFolderZipFile);
				if (pictures == null) {
					log.fatal("Failed to pack pictures in Update products pictures command");
					outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);
				}
				outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_OK, Serialization
						.serialize(Packing.encode(new File(GuiCommonDefs.productsPicturesFolderZipFile))));
			}
		} catch (IOException e1) {
			log.fatal("Failed to fecth date for Update products pictures command");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);

			return;
		}

		log.info("Update products pictures finished");
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
					Serialization.serialize(c.getProductFromCatalog(inCommandWrapper.getSenderID(), smartCode.getBarcode())));

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
		Map<Sale, Boolean> specialSaleTaken;

		log.info("Checkout Grocery List from serderID " + inCommandWrapper.getSenderID() + " command called");

		try {
			specialSaleTaken = new Gson().fromJson(inCommandWrapper.getData(), new TypeToken<Map<Sale, Boolean>>() {}.getType());
			
			c.cartCheckout(inCommandWrapper.getSenderID());

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_OK);
		} catch (CriticalError e) {
			log.fatal("Checkout Grocery List command failed, critical error occured from SQL Database connection");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);
		} catch (ClientNotConnected e) {
			log.info("Checkout Grocery List command failed, client is not connected");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_SENDER_IS_NOT_CONNECTED);
		} catch (GroceryListIsEmpty e) {
			log.info("Checkout Grocery List command failed, grocery list is empty");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_GROCERY_LIST_IS_EMPTY);
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

		try {
			c.addWorker(inCommandWrapper.getSenderID(), login, login.getForgetPassword());
			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_OK);
		} catch (CriticalError e) {
			log.fatal("Register new worker command failed, critical error occured from SQL Database connection");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);
		} catch (ClientAlreadyExist e) {
			log.info("Register new worker command failed, worker already exists");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_USERNAME_ALREADY_EXISTS);
		} catch (ClientNotConnected e) {
			log.info("Register new worker command failed, manager is not connected");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_SENDER_IS_NOT_CONNECTED);
		}

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

		try {
			c.removeWorker(inCommandWrapper.getSenderID(), username);
			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_OK);
		} catch (CriticalError e) {
			log.fatal("Remove worker command failed, critical error occured from SQL Database connection");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);
		} catch (ClientNotExist e) {
			log.info("Remove worker command failed, worker is not exist");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_USERNAME_DOES_NOT_EXIST);
		} catch (ClientNotConnected e) {
			log.info("Remove worker command failed, manager is not connected");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_SENDER_IS_NOT_CONNECTED);
		}

		log.info("Remove worker " + username + " from system finished");
	}
	
	private void getAllWorkers(SQLDatabaseConnection c) {		
		log.info("Get all workers from serderID " + inCommandWrapper.getSenderID() + " command called");
		
		try {
			String workersList = c.getWorkersList(inCommandWrapper.getSenderID());
			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_OK, workersList);
		} catch (ClientNotConnected e) {
			log.info("Get all workers command failed, client is not connected");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_SENDER_IS_NOT_CONNECTED);
		} catch (CriticalError e) {
			log.fatal("Get all workers command failed, critical error occured from SQL Database connection");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);
		}
		
				
		log.info("Get all workers from system finished");
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

		try {
			c.registerCustomer(profile.getUserName(), profile.getPassword());
			c.setCustomerProfile(profile.getUserName(), profile);
			c.setSecurityQACustomer(profile.getUserName(), profile.getForgetPassword());
			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_OK);
		} catch (CriticalError e) {
			log.fatal("Register new customer command failed, critical error occured from SQL Database connection");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);
		} catch (ClientAlreadyExist e) {
			log.info("Register new customer command failed, client already exists");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_USERNAME_ALREADY_EXISTS);
		} catch (ClientNotExist e) {
			log.fatal("Register new customer command failed, the sql report that the client not exists but i added it just now");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);
		} catch (IngredientNotExist e) {
			log.info("Register new customer command failed, client try to use not existed ingredient");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_INVALID_PARAMETER);
		}


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
		
		if (ClientServerDefs.anonymousCustomerUsername.equals(username)){
			log.info("Remove customer command failed, can't remove guest");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_INVALID_PARAMETER);
			return;
		}

		log.info("Trying to remove customer " + username + " from system");

		try {
			c.removeCustomer(username);
			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_OK);
		} catch (CriticalError e) {
			log.fatal("Remove customer command failed, critical error occured from SQL Database connection");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);
		} catch (ClientNotExist e) {
			log.info("Remove customer command failed, customer is not exist");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_USERNAME_DOES_NOT_EXIST);
		}

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

		try {
			c.setCustomerProfile(profile.getUserName(), profile);
			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_OK);
		} catch (CriticalError e) {
			log.fatal("Update customer command failed, critical error occured from SQL Database connection");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);
		} catch (ClientNotExist e) {
			log.info("Update customer command failed, client is not exist");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_USERNAME_DOES_NOT_EXIST);
		} catch (IngredientNotExist e) {
			log.info("Update customer command failed, client try to use not existed ingredient");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_INVALID_PARAMETER);
		}

		log.info("Update customer " + profile + " to system finished");
	}
	
	private void addNewIngredient(SQLDatabaseConnection c) {
		Ingredient ingredient = null;
		String ingredientResult = null;

		log.info("Add new ingredient from serderID " + inCommandWrapper.getSenderID() + " command called");

		try {
			ingredient = Serialization.deserialize(inCommandWrapper.getData(), Ingredient.class);
		} catch (java.lang.RuntimeException e) {
			log.fatal("Failed to parse data for Add New Ingredient command");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);

			return;
		}

		log.info("Trying to add new ingredient " + ingredient + " to system");

		try {
			ingredientResult = c.addIngredient(inCommandWrapper.getSenderID(), ingredient.getName());
			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_OK, ingredientResult);
		} catch (CriticalError e) {
			log.fatal("Add new ingredient command failed, critical error occured from SQL Database connection");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);
		} catch (ClientNotConnected e) {
			log.info("Add new ingredient customer command failed, client is not connected");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_SENDER_IS_NOT_CONNECTED);
		}

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

		try {
			c.removeIngredient(inCommandWrapper.getSenderID(), ingredient);
			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_OK);
		} catch (CriticalError e) {
			log.fatal("Remove ingredient command failed, critical error occured from SQL Database connection");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);
		} catch (ClientNotConnected e) {
			log.info("Remove ingredient customer command failed, client is not connected");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_SENDER_IS_NOT_CONNECTED);
		} catch (IngredientNotExist e) {
			log.info("Remove ingredient customer command failed, ingredient does not exist");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.PARAM_ID_IS_NOT_EXIST);
		} catch (IngredientStillUsed e) {
			log.info("Remove ingredient customer command failed, ingredient still in use");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_INGREDIENT_STILL_IN_USE);
		}

		log.info("Remove ingredient " + ingredient + " from system finished");
	}
	
	private void forceRemoveIngredient(SQLDatabaseConnection c) {
		Ingredient ingredient = null;

		log.info("Force remove ingredient from serderID " + inCommandWrapper.getSenderID() + " command called");

		try {
			ingredient = Serialization.deserialize(inCommandWrapper.getData(), Ingredient.class);
		} catch (java.lang.RuntimeException e) {
			log.fatal("Failed to parse data for Force Remove Ingredient command");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);

			return;
		}

		log.info("Trying to force remove ingredient " + ingredient + " from system");

		try {
			c.removeIngredient(inCommandWrapper.getSenderID(), ingredient);
			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_OK);
		} catch (CriticalError e) {
			log.fatal("Force Remove ingredient command failed, critical error occured from SQL Database connection");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);
		} catch (ClientNotConnected e) {
			log.info("Force Remove ingredient customer command failed, client is not connected");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_SENDER_IS_NOT_CONNECTED);
		} catch (IngredientNotExist e) {
			log.info("Force Remove ingredient customer command failed, ingredient does not exist");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.PARAM_ID_IS_NOT_EXIST);
		} catch (IngredientStillUsed e) {
			log.info("Force Remove ingredient customer command failed, ingredient still in use");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_INGREDIENT_STILL_IN_USE);
		}

		log.info("Force Remove ingredient " + ingredient + " from system finished");
	}
	
	private void editIngredient(SQLDatabaseConnection c) {
		Ingredient ingredient = null;

		log.info("Edit ingredient from serderID " + inCommandWrapper.getSenderID() + " command called");

		try {
			ingredient = Serialization.deserialize(inCommandWrapper.getData(), Ingredient.class);
		} catch (java.lang.RuntimeException e) {
			log.fatal("Failed to parse data for Edit Ingredient command");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);

			return;
		}

		log.info("Trying to edit ingredient " + ingredient);

		try {
			c.editIngredient(inCommandWrapper.getSenderID(), ingredient);
			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_OK);
		} catch (CriticalError e) {
			log.fatal("Edit ingredient command failed, critical error occured from SQL Database connection");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);
		} catch (ClientNotConnected e) {
			log.info("Edit ingredient customer command failed, client is not connected");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_SENDER_IS_NOT_CONNECTED);
		} catch (IngredientNotExist e) {
			log.info("Edit ingredient customer command failed, ingredient does not exist");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.PARAM_ID_IS_NOT_EXIST);
		}

		log.info("Edit ingredient " + ingredient + " from system finished");
	}

	private void addNewManufacturer(SQLDatabaseConnection c) {
		Manufacturer manufacturer = null;

		log.info("Add new manufacturer from serderID " + inCommandWrapper.getSenderID() + " command called");

		try {
			manufacturer = Serialization.deserialize(inCommandWrapper.getData(), Manufacturer.class);
		} catch (java.lang.RuntimeException e) {
			log.fatal("Failed to parse data for Add New Manufacturer command");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);

			return;
		}

		log.info("Trying to add new manufacturer " + manufacturer + " to system");

		try {
			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_OK,
					c.addManufacturer(inCommandWrapper.getSenderID(), manufacturer.getName()));
		} catch (CriticalError e) {
			log.fatal("Add new manufacturer command failed, critical error occured from SQL Database connection");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);
		} catch (ClientNotConnected e) {
			log.info("Add new manufacturer command failed, client is not connected");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_SENDER_IS_NOT_CONNECTED);
		}

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

		try {
			c.removeManufacturer(inCommandWrapper.getSenderID(), manufacturer);
			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_OK);
		} catch (CriticalError e) {
			log.fatal("Remove manufacturer command failed, critical error occured from SQL Database connection");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);
		} catch (ClientNotConnected e) {
			log.info("Remove manufacturer command failed, client is not connected");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_SENDER_IS_NOT_CONNECTED);
		} catch (ManufacturerNotExist e) {
			log.info("Remove manufacturer customer command failed, manufacturer does not exist");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.PARAM_ID_IS_NOT_EXIST);
		} catch (ManufacturerStillUsed e) {
			log.info("Remove manufacturer customer command failed, manufacturer still in use");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_MANUFACTURER_STILL_IN_USE);
		}

		log.info("Remove manufacturer " + manufacturer + " from system finished");
	}
	
	private void editManufacturer(SQLDatabaseConnection c) {
		Manufacturer manufacturer = null;

		log.info("Edit manufacturer from serderID " + inCommandWrapper.getSenderID() + " command called");

		try {
			manufacturer = Serialization.deserialize(inCommandWrapper.getData(), Manufacturer.class);
		} catch (java.lang.RuntimeException e) {
			log.fatal("Failed to parse data for Edit Manufacturer command");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);

			return;
		}

		log.info("Trying to edit manufacturer " + manufacturer);

		try {
			c.editManufacturer(inCommandWrapper.getSenderID(), manufacturer);
			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_OK);
		} catch (CriticalError e) {
			log.fatal("Edit manufacturer command failed, critical error occured from SQL Database connection");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);
		} catch (ClientNotConnected e) {
			log.info("Edit manufacturer command failed, client is not connected");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_SENDER_IS_NOT_CONNECTED);
		} catch (ManufacturerNotExist e) {
			log.info("Edit manufacturer customer command failed, manufacturer does not exist");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.PARAM_ID_IS_NOT_EXIST);
		}

		log.info("Edit manufacturer " + manufacturer + " to system finished");
	}
		
	private void getAllManufacturers(SQLDatabaseConnection c) {
		log.info("Get all manufacturers from serderID " + inCommandWrapper.getSenderID() + " command called");
		
		try {
			String manufacturersList = c.getManufacturersList(inCommandWrapper.getSenderID());
			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_OK, manufacturersList);
		} catch (ClientNotConnected e) {
			log.info("Get all manufacturers command failed, client is not connected");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_SENDER_IS_NOT_CONNECTED);
		} catch (CriticalError e) {
			log.fatal("Get all manufacturers command failed, critical error occured from SQL Database connection");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);
		}
		
				
		log.info("Get all manufacturers from system finished");
	}
	
	private void getAllIngredients(SQLDatabaseConnection c) {
		log.info("Get all ingredeints from serderID " + inCommandWrapper.getSenderID() + " command called");

		String ingredientsList;
		try {
			ingredientsList = c.getIngredientsList();
			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_OK, ingredientsList);
		} catch (CriticalError e) {
			log.fatal("Get all manufacturers command failed, critical error occured from SQL Database connection");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);
		}
		
				
		log.info("Get all ingredeints from system finished");
	}
	
	private void isFreeCustomerUsername(SQLDatabaseConnection c) {
		String username;
		
		log.info("Is free customer username command called with from serderID " + inCommandWrapper.getSenderID() + " command called");
		
		try {
			username = Serialization.deserialize(inCommandWrapper.getData(), String.class);
		} catch (java.lang.RuntimeException e) {
			log.fatal("Failed to parse data for Is free customer username command");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);

			return;
		}
		
		try {
			outCommandWrapper = new CommandWrapper(c.isCustomerUsernameAvailable(username) ? ResultDescriptor.SM_OK
					: ResultDescriptor.SM_USERNAME_ALREADY_EXISTS);
		} catch (CriticalError e) {
			log.fatal("Is free customer username command failed, critical error occured from SQL Database connection");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);
		}

		log.info("Is free customer username command system finished, username " + username
				+ (outCommandWrapper.getResultDescriptor() == ResultDescriptor.SM_OK ? " is" : " isn't") + " free");
	}
	
	private void forgetPasswordGetQuestion(SQLDatabaseConnection c) {
		String username;
		
		log.info("Get question for forget password command called with from serderID " + inCommandWrapper.getSenderID() + " command called");
		
		try {
			username = Serialization.deserialize(inCommandWrapper.getData(), String.class);
		} catch (java.lang.RuntimeException e) {
			log.fatal("Failed to parse data for Get question for forget password command");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);

			return;
		}
		
		try {
			
			if (inCommandWrapper.getSenderID() == 0) {
				/* Command sent from employee */
				String SecurityQuestion = c.getSecurityQuestionWorker(username);
				outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_OK, SecurityQuestion);			
			} else {
				/* Command sent from customer */
				String SecurityQuestion = c.getSecurityQuestionCustomer(username);
				outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_OK, SecurityQuestion);	
			}
			
			log.info("Get question for forget password command for user: " + username + "succeded. the question is: " + outCommandWrapper.getData());
		} catch (CriticalError e) {
			log.fatal("Get question for forget password command failed, critical error occured from SQL Database connection");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);

		} catch (ClientNotExist e) {
			log.info("Get question for forget password command failed, client is not exist");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_USERNAME_DOES_NOT_EXIST);
		}
		
				
		log.info("Get question for forget password command system finished");
	}
	
	private void forgetPasswordSendAnswerWithNewPassword(SQLDatabaseConnection c) {
		Login login;
		
		log.info("Get question for forget password send answer command called with from serderID " + inCommandWrapper.getSenderID() + " command called");
		
		try {
			login = Serialization.deserialize(inCommandWrapper.getData(), Login.class);
		} catch (java.lang.RuntimeException e) {
			log.fatal("Failed to parse data for Get question for forget password send answer command");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);

			return;
		}
		
		boolean goodAnswer;
		try {
			goodAnswer = inCommandWrapper.getSenderID() == 0 ?
					/* Command sent from employee */
					c.verifySecurityAnswerWorker(login.getUserName(), login.getForgetPassword().getAnswer()) 
					/* Command sent from customer */
					: c.verifySecurityAnswerCustomer(login.getUserName(), login.getForgetPassword().getAnswer()); 
			
			if (!goodAnswer) {
				log.info("the anwser is incorrect.");
				outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_FOROGT_PASSWORD_WRONG_ANSWER,
						Serialization.serialize(false));
			} else {
				log.info("the anwser is correct");
				
				if (inCommandWrapper.getSenderID() == 0) 
					/* Command sent from employee */
					c.setPasswordWorker(login.getUserName(), login.getPassword());
				else 
					/* Command sent from customer */
					c.setPasswordCustomer(login.getUserName(), login.getPassword());
				
				
				outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_OK, Serialization.serialize(true));
				log.info("the anwser is correct. password changed succesfully.");
			}
			
		} catch (CriticalError e) {
			log.fatal("Get question for forget password send answer command failed, critical error occured from SQL Database connection");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);
		} catch (ClientNotExist e) {
			log.info("Get question for forget password send answer command failed, client is not exist");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_USERNAME_DOES_NOT_EXIST);
		}

				
		log.info("Get question for forget password send answer command system finished");
	}
	
	private void createNewSale(SQLDatabaseConnection c) {
		Sale sale = null;

		log.info("Create new sale from serderID " + inCommandWrapper.getSenderID() + " command called");

		try {
			sale = Serialization.deserialize(inCommandWrapper.getData(), Sale.class);
		} catch (java.lang.RuntimeException e) {
			log.fatal("Failed to parse data for Create new sale command");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);

			return;
		}

		log.info("Trying to create new sale " + sale + " to system");

		String SaleId;
		try {
			SaleId = Serialization.serialize(c.addSale(inCommandWrapper.getSenderID(), sale, true));
			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_OK, SaleId);	
		} catch (ClientNotConnected e) {
			log.info("Create new sale command failed, client is not exist");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_SENDER_IS_NOT_CONNECTED);
		} catch (CriticalError e) {
			log.fatal("Create new sale command failed, critical error occured from SQL Database connection");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);
		} catch (SaleAlreadyExist e) {
			log.info("Create new sale command failed, sale for this product already exists");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.PARAM_ID_ALREADY_EXISTS);
		}
		

		log.info("Create new sale " + sale + " to system finished");
	}
	
	private void removeSale(SQLDatabaseConnection c) {
		Integer saleID = null;

		log.info("Remove sale from serderID " + inCommandWrapper.getSenderID() + " command called");

		try {
			saleID = Serialization.deserialize(inCommandWrapper.getData(), Integer.class);
		} catch (java.lang.RuntimeException e) {
			log.fatal("Failed to parse data for Remove Sale command");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);

			return;
		}

		log.info("Trying to remove sale with id " + saleID + " from system");

		
		try {
			c.removeSale(inCommandWrapper.getSenderID(), saleID);
			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_OK);	
		} catch (CriticalError e) {
			log.fatal("Remove sale command failed, critical error occured from SQL Database connection");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);
		} catch (ClientNotConnected e) {
			log.info("Remove sale command failed, client is not exist");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_SENDER_IS_NOT_CONNECTED);
		} catch (SaleNotExist e) {
			log.info("Remove sale command failed, sale is not exist");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.PARAM_ID_IS_NOT_EXIST);
		} catch (SaleStillUsed e) {
			log.info("Remove sale command failed, sale still used by clients");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.PARAM_ID_IS_NOT_EXIST);
		}
		

		log.info("Remove Sale " + saleID + " from system finished");
	}
	
	private void getAllSales(SQLDatabaseConnection c) {
		log.info("Get all sales from serderID " + inCommandWrapper.getSenderID() + " command called");
		
		String salesList;
		try {
			salesList = Serialization.serialize(c.getAllSales());
			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_OK, salesList);
		} catch (CriticalError e) {
			log.fatal("Get all sales command failed, critical error occured from SQL Database connection");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);
		}
		
				
		log.info("Get all sales from system finished");
	}
	
	private void getSaleForProduct(SQLDatabaseConnection c) {
		Long barcode;
		
		log.info("Get sale for product command called with from serderID " + inCommandWrapper.getSenderID() + " command called");
		
		try {
			barcode = Serialization.deserialize(inCommandWrapper.getData(), Long.class);
		} catch (java.lang.RuntimeException e) {
			log.fatal("Failed to parse data for Is free customer username command");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);

			return;
		}
		
		String salesList;
		try {
			salesList = Serialization.serialize(c.getSaleForProduct(barcode));
			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_OK, salesList);
		} catch (CriticalError e) {
			log.fatal("Get sale for product command failed, critical error occured from SQL Database connection");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);
		} catch (ClientNotConnected e) {
			log.info("Get sale for product command failed, client is not exist");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_SENDER_IS_NOT_CONNECTED);
		}

		log.info("Get sale for product command system finished for barcode " + barcode);
	}
	
	private void getSpecialSaleForProduct(SQLDatabaseConnection c) {
		Long barcode;
		
		log.info("Get special sale for product command called with from serderID " + inCommandWrapper.getSenderID() + " command called");
		
		try {
			barcode = Serialization.deserialize(inCommandWrapper.getData(), Long.class);
		} catch (java.lang.RuntimeException e) {
			log.fatal("Failed to parse data for Is free customer username command");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);

			return;
		}
		
		//TODO Noam - call SQL here


		log.info("Get special sale for product command system finished for barcode " + barcode);
	}
	
	private void offerSpecialSaleForProduct(SQLDatabaseConnection c) {
		Sale sale;
		
		log.info("Offer special sale for product command called with from serderID " + inCommandWrapper.getSenderID() + " command called");
		
		try {
			sale = Serialization.deserialize(inCommandWrapper.getData(), Sale.class);
		} catch (java.lang.RuntimeException e) {
			log.fatal("Failed to parse data for Is free customer username command");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);

			return;
		}
		
		//TODO Noam - call SQL here

		log.info("Offer special sale for product command system finished for sale " + sale);
	}
	
	private void getAllExpiredProductPackages(SQLDatabaseConnection c) {
		log.info("Get all expired product packages from serderID " + inCommandWrapper.getSenderID() + " command called");
		
		String expiredPackagesList;
		try {
			expiredPackagesList = Serialization.serialize(c.getExpiredProductPackages());
			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_OK, expiredPackagesList);
		} catch (CriticalError e) {
			log.fatal("Get all expired product packages command failed, critical error occured from SQL Database connection");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);
		}
		
				
		log.info("Get all expired product packages from system finished");
	}
	
	public CommandWrapper execute(SQLDatabaseConnection c) {
		if (c == null) {
			log.fatal("Failed to get SQL Database Connection");

			return new CommandWrapper(ResultDescriptor.SM_ERR);
		}

		if ((inCommandWrapper.getCommandDescriptor() != CommandDescriptor.LOGIN_CUSTOMER || inCommandWrapper.getCommandDescriptor() != CommandDescriptor.LOGIN_EMPLOYEE)
				&& (inCommandWrapper.getSenderID() < 0)) {
			log.info("Command failed, senderID can't be negative");

			return new CommandWrapper(ResultDescriptor.SM_INVALID_PARAMETER);
		}

		switch (inCommandWrapper.getCommandDescriptor()) {
		case LOGIN_EMPLOYEE:
			loginEmployeeCommand(c);

			break;
			
		case LOGIN_CUSTOMER:
			loginClientCommand(c);

			break;

		case LOGOUT:
			logoutCommand(c);

			break;

		case UPDATE_PRODUCTS_PICTURES:
			updateProductsPictures(c);
			
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
			
		case GET_ALL_WORKERS:
			getAllWorkers(c);

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
			
		case FORCE_REMOVE_INGREDIENT:
			forceRemoveIngredient(c);

			break;
			
		case EDIT_INGREDIENT:
			editIngredient(c);

			break;
			
		case ADD_MANUFACTURER:
			addNewManufacturer(c);

			break;
			
		case EDIT_MANUFACTURER:
			editManufacturer(c);

			break;
			
		case REMOVE_MANUFACTURER:
			removeManufacturer(c);

			break;
			
		case GET_ALL_MANUFACTURERS:
			getAllManufacturers(c);
			
			break;
			
		case GET_ALL_INGREDIENTS:
			getAllIngredients(c);
			
			break;
			
		case IS_FREE_CUSTOMER_NAME:
			isFreeCustomerUsername(c);
			
			break;
			
		case FORGOT_PASSWORD_GET_QUESTION:
			forgetPasswordGetQuestion(c);
			
			break;
			
		case FORGOT_PASSWORD_SEND_ANSWER_WITH_NEW_PASSWORD:
			forgetPasswordSendAnswerWithNewPassword(c);
			
			break;
			
		case CREATE_NEW_SALE:
			createNewSale(c);

			break;
			
		case REMOVE_SALE:
			removeSale(c);

			break;
			
		case GET_ALL_SALES:
			getAllSales(c);

			break;
			
		case GET_SALE_FOR_PRODUCT:
			getSaleForProduct(c);
			
			break;	
			
		case GET_ALL_EXPIRED_PRODUCT_PACKAGES:
			getAllExpiredProductPackages(c);
			
			break;
			
		case GET_SPECIAL_SALE_FOR_PRODUCT:
			getSpecialSaleForProduct(c);
			
			break;

		case OFFER_SPECIAL_SALE_FOR_PRODUCT:
			offerSpecialSaleForProduct(c);
			
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
