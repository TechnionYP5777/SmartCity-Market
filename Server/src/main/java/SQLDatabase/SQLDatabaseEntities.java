package SQLDatabase;

import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbSchema;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbSpec;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbTable;

import SQLDatabase.SQLDatabaseStrings.ACTIVE_CUSTOMERS_LIST_TABLE;
import SQLDatabase.SQLDatabaseStrings.CUSTOMERS_INGREDIENTS_TABLE;
import SQLDatabase.SQLDatabaseStrings.CUSTOMERS_TABLE;
import SQLDatabase.SQLDatabaseStrings.FREE_IDS_TABLE;
import SQLDatabase.SQLDatabaseStrings.GROCERIES_LISTS_HISTORY_TABLE;
import SQLDatabase.SQLDatabaseStrings.GROCERIES_LISTS_TABLE;
import SQLDatabase.SQLDatabaseStrings.INGREDIENTS_TABLE;
import SQLDatabase.SQLDatabaseStrings.LOCATIONS_TABLE;
import SQLDatabase.SQLDatabaseStrings.MANUFACTURER_TABLE;
import SQLDatabase.SQLDatabaseStrings.PRODUCTS_CATALOG_INGREDIENTS_TABLE;
import SQLDatabase.SQLDatabaseStrings.PRODUCTS_CATALOG_LOCATIONS_TABLE;
import SQLDatabase.SQLDatabaseStrings.PRODUCTS_CATALOG_TABLE;
import SQLDatabase.SQLDatabaseStrings.PRODUCTS_PACKAGES_TABLE;
import SQLDatabase.SQLDatabaseStrings.WORKERS_TABLE;

/**
 * SQLDatabaseEntities - contains objects to use with SQLBuilder in
 * {@link SQLDatabaseConnection}. The class devided into sections (via
 * sub-classes) for better organizing
 * 
 * @author Noam Yefet
 * @since 2016-12-14
 */
class SQLDatabaseEntities {

	/*
	 * constants
	 */
	private static final String TYPE_TEXT = "text";
	private static final String TYPE_INTEGER = "integer";
	private static final String TYPE_LONG = "bigint";
	private static final String TYPE_REAL = "real";
	private static final String TYPE_DATE = "datetime";
	private static final String TYPE_BOOLEAN = "integer";
	private static final String TYPE_ID = TYPE_INTEGER;

	static DbSpec spec = new DbSpec();
	static DbSchema databaseSchema = spec.addDefaultSchema();

	/**
	 * this class contains all the objects used buy SQLBuilder for Ingredients
	 * Table
	 * 
	 * @author noam
	 *
	 */
	static class IngredientsTable {
		static DbTable table;

		static DbColumn ingredientIDCol;
		static DbColumn ingredientNameCol;
		
		static {
			/*
			 * initialize the ingredients table
			 */
			table = databaseSchema.addTable(INGREDIENTS_TABLE.INGREDIENTS_TABLE);
			ingredientIDCol = table.addColumn(INGREDIENTS_TABLE.ATTR_INGREDIENT_ID,
					TYPE_ID, null);
			ingredientNameCol = table.addColumn(INGREDIENTS_TABLE.ATTR_INGREDIENT_NAME,
					TYPE_TEXT, null);
			// set primary key
			table.primaryKey(table.getName(), ingredientIDCol.getName());
		}
	}

	/**
	 * this class contains all the objects used buy SQLBuilder for Manufacturer
	 * Table
	 * 
	 * @author noam
	 *
	 */
	static class ManufacturerTable {
		static DbTable table;

		static DbColumn manufacturerIDCol;
		static DbColumn manufacturerNameCol;
		
		static {
			/*
			 * initialize the Manufacturer Table
			 */
			ManufacturerTable.table = databaseSchema.addTable(MANUFACTURER_TABLE.MANUFACTURER_TABLE);
			ManufacturerTable.manufacturerIDCol = ManufacturerTable.table.addColumn(MANUFACTURER_TABLE.ATTR_MANUFACTURER_ID,
					TYPE_ID, null);
			ManufacturerTable.manufacturerNameCol = ManufacturerTable.table
					.addColumn(MANUFACTURER_TABLE.ATTR_MANUFACTURER_NAME, TYPE_TEXT, null);
			// set primary key
			ManufacturerTable.table.primaryKey(ManufacturerTable.table.getName(),
					ManufacturerTable.manufacturerIDCol.getName());
		}
	}

	/**
	 * this class contains all the objects used buy SQLBuilder for Locations
	 * Table
	 * 
	 * @author noam
	 *
	 */
	static class LocationsTable {
		static DbTable table;

		static DbColumn locationIDCol;
		static DbColumn placeInStoreCol;
		static DbColumn locationDescriptionCol;
		static DbColumn pointXCol;
		static DbColumn pointYCol;
		
		static {
			/*
			 * initialize the Locations Table
			 */
			LocationsTable.table = databaseSchema.addTable(LOCATIONS_TABLE.LOCATIONS_TABLE);
			LocationsTable.locationIDCol = LocationsTable.table.addColumn(LOCATIONS_TABLE.ATTR_LOCATION_ID, TYPE_ID, null);
			LocationsTable.locationDescriptionCol = LocationsTable.table
					.addColumn(LOCATIONS_TABLE.ATTR_LOCATION_DESCRIPTION, TYPE_TEXT, null);
			LocationsTable.placeInStoreCol = LocationsTable.table.addColumn(LOCATIONS_TABLE.ATTR_PLACE_IN_STORE, TYPE_TEXT,
					null);
			LocationsTable.pointXCol = LocationsTable.table.addColumn(LOCATIONS_TABLE.ATTR_POINT_X, TYPE_INTEGER, null);
			LocationsTable.pointYCol = LocationsTable.table.addColumn(LOCATIONS_TABLE.ATTR_POINT_Y, TYPE_INTEGER, null);
			// set primary key
			LocationsTable.table.primaryKey(LocationsTable.table.getName(), LocationsTable.locationIDCol.getName());
		}
	}

	/**
	 * this class contains all the objects used buy SQLBuilder for
	 * ProductsCatalog Table
	 * 
	 * @author noam
	 *
	 */
	static class ProductsCatalogTable {
		static DbTable table;

		static DbColumn barcodeCol;
		static DbColumn productNameCol;
		static DbColumn manufacturerIDCol;
		static DbColumn productDescriptionCol;
		static DbColumn productPriceCol;
		static DbColumn productWeightCol;
		static DbColumn productPictureCol;
		
		static {
			/*
			 * initialize the ProductsCatalog Table
			 */
			ProductsCatalogTable.table = databaseSchema.addTable(PRODUCTS_CATALOG_TABLE.PRODUCTS_CATALOG_TABLE);
			ProductsCatalogTable.barcodeCol = ProductsCatalogTable.table.addColumn(PRODUCTS_CATALOG_TABLE.ATTR_BARCODE,
					TYPE_LONG, null);
			ProductsCatalogTable.manufacturerIDCol = ProductsCatalogTable.table
					.addColumn(PRODUCTS_CATALOG_TABLE.ATTR_MANUFACTURER_ID, TYPE_ID, null);
			ProductsCatalogTable.productDescriptionCol = ProductsCatalogTable.table
					.addColumn(PRODUCTS_CATALOG_TABLE.ATTR_PRODUCT_DESCRIPTION, TYPE_TEXT, null);
			ProductsCatalogTable.productNameCol = ProductsCatalogTable.table
					.addColumn(PRODUCTS_CATALOG_TABLE.ATTR_PRODUCT_NAME, TYPE_TEXT, null);
			ProductsCatalogTable.productPictureCol = ProductsCatalogTable.table
					.addColumn(PRODUCTS_CATALOG_TABLE.ATTR_PRODUCT_PICTURE, TYPE_TEXT, null);
			ProductsCatalogTable.productPriceCol = ProductsCatalogTable.table
					.addColumn(PRODUCTS_CATALOG_TABLE.ATTR_PRODUCT_PRICE, TYPE_REAL, null);
			ProductsCatalogTable.productWeightCol = ProductsCatalogTable.table
					.addColumn(PRODUCTS_CATALOG_TABLE.ATTR_PRODUCT_WEIGHT, TYPE_REAL, null);
			// set primary key
			ProductsCatalogTable.table.primaryKey(ProductsCatalogTable.table.getName(),
					ProductsCatalogTable.barcodeCol.getName());
		}
	}

	/**
	 * this class contains all the objects used buy SQLBuilder for
	 * ProductsCatalogIngredients Table
	 * 
	 * @author noam
	 *
	 */
	static class ProductsCatalogIngredientsTable {
		static DbTable table;

		static DbColumn barcodeCol;
		static DbColumn ingredientIDCol;
		
		static {
			/*
			 * initialize the ProductsCatalogIngredients Table
			 */
			ProductsCatalogIngredientsTable.table = databaseSchema
					.addTable(PRODUCTS_CATALOG_INGREDIENTS_TABLE.PRODUCTS_CATALOG_INGREDIENTS_TABLE);
			ProductsCatalogIngredientsTable.barcodeCol = ProductsCatalogIngredientsTable.table
					.addColumn(PRODUCTS_CATALOG_INGREDIENTS_TABLE.ATTR_BARCODE, TYPE_LONG, null);
			ProductsCatalogIngredientsTable.ingredientIDCol = ProductsCatalogIngredientsTable.table
					.addColumn(PRODUCTS_CATALOG_INGREDIENTS_TABLE.ATTR_INGREDIENT_ID, TYPE_ID, null);
		}
	}

	/**
	 * this class contains all the objects used buy SQLBuilder for
	 * ProductsCatalogLocations Table
	 * 
	 * @author noam
	 *
	 */
	static class ProductsCatalogLocationsTable {
		static DbTable table;

		static DbColumn barcodeCol;
		static DbColumn locationIDCol;
		
		static {
			/*
			 * initialize the ProductsCatalogLocations Table
			 */
			ProductsCatalogLocationsTable.table = databaseSchema
					.addTable(PRODUCTS_CATALOG_LOCATIONS_TABLE.PRODUCTS_CATALOG_LOCATIONS_TABLE);
			ProductsCatalogLocationsTable.barcodeCol = ProductsCatalogLocationsTable.table
					.addColumn(PRODUCTS_CATALOG_LOCATIONS_TABLE.ATTR_BARCODE, TYPE_LONG, null);
			ProductsCatalogLocationsTable.locationIDCol = ProductsCatalogLocationsTable.table
					.addColumn(PRODUCTS_CATALOG_LOCATIONS_TABLE.ATTR_LOCATION_ID, TYPE_ID, null);
		}
	}

	/**
	 * this class contains all the objects used buy SQLBuilder for
	 * ProductsPackages Table
	 * 
	 * @author noam
	 *
	 */
	static class ProductsPackagesTable {
		static DbTable table;

		static DbColumn barcodeCol;
		static DbColumn placeInStoreCol;
		static DbColumn expirationDateCol;
		static DbColumn amountCol;
		
		static {
			/*
			 * initialize the ProductsPackages Table
			 */
			ProductsPackagesTable.table = databaseSchema.addTable(PRODUCTS_PACKAGES_TABLE.PRODUCTS_PACKAGES_TABLE);
			ProductsPackagesTable.barcodeCol = ProductsPackagesTable.table.addColumn(PRODUCTS_PACKAGES_TABLE.ATTR_BARCODE,
					TYPE_LONG, null);
			ProductsPackagesTable.amountCol = ProductsPackagesTable.table.addColumn(PRODUCTS_PACKAGES_TABLE.ATTR_AMOUNT,
					TYPE_INTEGER, null);
			ProductsPackagesTable.expirationDateCol = ProductsPackagesTable.table
					.addColumn(PRODUCTS_PACKAGES_TABLE.ATTR_EXPIRATION_DATE, TYPE_DATE, null);
			ProductsPackagesTable.placeInStoreCol = ProductsPackagesTable.table
					.addColumn(PRODUCTS_PACKAGES_TABLE.ATTR_PLACE_IN_STORE, TYPE_TEXT, null);
			// set primary key
			ProductsPackagesTable.table.primaryKey(ProductsPackagesTable.table.getName(),
					ProductsPackagesTable.barcodeCol.getName(), ProductsPackagesTable.expirationDateCol.getName(),
					ProductsPackagesTable.placeInStoreCol.getName());
		}
	}

	/**
	 * this class contains all the objects used buy SQLBuilder for
	 * GroceriesLists Table
	 * 
	 * @author noam
	 *
	 */
	static class GroceriesListsTable {
		static DbTable table;

		static DbColumn listIDCol;
		static DbColumn barcodeCol;
		static DbColumn expirationDateCol;
		static DbColumn amountCol;
		
		static {
			/*
			 * initialize the GroceriesLists Table
			 */
			GroceriesListsTable.table = databaseSchema.addTable(GROCERIES_LISTS_TABLE.GROCERIES_LISTS_TABLE);
			GroceriesListsTable.barcodeCol = GroceriesListsTable.table.addColumn(GROCERIES_LISTS_TABLE.ATTR_BARCODE,
					TYPE_LONG, null);
			GroceriesListsTable.amountCol = GroceriesListsTable.table.addColumn(GROCERIES_LISTS_TABLE.ATTR_AMOUNT,
					TYPE_INTEGER, null);
			GroceriesListsTable.expirationDateCol = GroceriesListsTable.table
					.addColumn(GROCERIES_LISTS_TABLE.ATTR_EXPIRATION_DATE, TYPE_DATE, null);
			GroceriesListsTable.listIDCol = GroceriesListsTable.table.addColumn(GROCERIES_LISTS_TABLE.ATTR_LIST_ID, TYPE_ID,
					null);
			// set primary key
			GroceriesListsTable.table.primaryKey(GroceriesListsTable.table.getName(),
					GroceriesListsTable.barcodeCol.getName(), GroceriesListsTable.expirationDateCol.getName(),
					GroceriesListsTable.listIDCol.getName());
		}
	}

	/**
	 * this class contains all the objects used buy SQLBuilder for
	 * GroceriesListsHistory Table
	 * 
	 * @author noam
	 *
	 */
	static class GroceriesListsHistoryTable {
		static DbTable table;

		static DbColumn listIDCol;
		static DbColumn barcodeCol;
		static DbColumn expirationDateCol;
		static DbColumn amountCol;
		
		static {
			/*
			 * initialize the GroceriesListsHistory Table
			 */
			GroceriesListsHistoryTable.table = databaseSchema
					.addTable(GROCERIES_LISTS_HISTORY_TABLE.GROCERIES_LISTS_HISTORY_TABLE);
			GroceriesListsHistoryTable.barcodeCol = GroceriesListsHistoryTable.table
					.addColumn(GROCERIES_LISTS_HISTORY_TABLE.ATTR_BARCODE, TYPE_LONG, null);
			GroceriesListsHistoryTable.amountCol = GroceriesListsHistoryTable.table
					.addColumn(GROCERIES_LISTS_HISTORY_TABLE.ATTR_AMOUNT, TYPE_INTEGER, null);
			GroceriesListsHistoryTable.expirationDateCol = GroceriesListsHistoryTable.table
					.addColumn(GROCERIES_LISTS_HISTORY_TABLE.ATTR_EXPIRATION_DATE, TYPE_DATE, null);
			GroceriesListsHistoryTable.listIDCol = GroceriesListsHistoryTable.table
					.addColumn(GROCERIES_LISTS_HISTORY_TABLE.ATTR_LIST_ID, TYPE_ID, null);
			// set primary key
			GroceriesListsHistoryTable.table.primaryKey(GroceriesListsHistoryTable.table.getName(),
					GroceriesListsHistoryTable.barcodeCol.getName(), GroceriesListsHistoryTable.expirationDateCol.getName(),
					GroceriesListsHistoryTable.listIDCol.getName());
		}
	}

	/**
	 * this class contains all the objects used buy SQLBuilder for CartsList Table
	 * Table
	 * 
	 * @author noam
	 *
	 */
	static class CartsListTable {
		static DbTable table;

		static DbColumn CartIDCol;
		static DbColumn listIDCol;
		
		static {
			/*
			 * initialize the ActiveCustomersList Table
			 */
			CartsListTable.table = databaseSchema.addTable(ACTIVE_CUSTOMERS_LIST_TABLE.ACTIVE_CUSTOMERS_LIST_TABLE);
			CartsListTable.CartIDCol = CartsListTable.table.addColumn(ACTIVE_CUSTOMERS_LIST_TABLE.ATTR_CUSTOMER_ID, TYPE_ID, null);
			CartsListTable.listIDCol = CartsListTable.table.addColumn(ACTIVE_CUSTOMERS_LIST_TABLE.ATTR_LIST_ID, TYPE_ID, null);
		}
	}

	
	/**
	 * 
	 * this abstract class contains all the objects used by SQLBuilder for any registered client Table
	 * 
	 * @author  noam
	 * 
	 */
	abstract static class ClientsTable {
		/*
		 * duplicate fields for using in inheritance
		 */
		DbTable table;
		
		DbColumn IDCol;
		DbColumn usernameCol;
		DbColumn passwordCol;
		DbColumn isLoggedInCol;
		DbColumn sessionIDCol;
		DbColumn securityQuestionCol;
		DbColumn securityAnswerCol;
		
	}
	
	/**
	 * this class contains all the objects used buy SQLBuilder for Workers Table
	 * 
	 * @author noam
	 *
	 */
	static class WorkersTable extends ClientsTable{

		static DbTable workertable;
		
		static DbColumn workerIDCol;
		static DbColumn workerusernameCol;
		static DbColumn workerpasswordCol;
		static DbColumn workerisLoggedInCol;
		static DbColumn workersessionIDCol;
		static DbColumn workersecurityQuestionCol;
		static DbColumn workersecurityAnswerCol;
		static DbColumn workerPrivilegesCol;
		
		static {
			/*
			 * initialize the Workers Table
			 */
			WorkersTable.workertable = databaseSchema.addTable(WORKERS_TABLE.WORKERS_TABLE);
			WorkersTable.workerIDCol = WorkersTable.workertable.addColumn(WORKERS_TABLE.ATTR_WORKER_ID, TYPE_ID, null);
			WorkersTable.workerisLoggedInCol = WorkersTable.workertable.addColumn(WORKERS_TABLE.ATTR_IS_LOGGED_IN, TYPE_BOOLEAN, null);
			WorkersTable.workersessionIDCol = WorkersTable.workertable.addColumn(WORKERS_TABLE.ATTR_SESSION_ID, TYPE_LONG, null);
			WorkersTable.workerpasswordCol = WorkersTable.workertable.addColumn(WORKERS_TABLE.ATTR_WORKER_PASSWORD, TYPE_TEXT,
					null);
			WorkersTable.workerusernameCol = WorkersTable.workertable.addColumn(WORKERS_TABLE.ATTR_WORKER_USERNAME, TYPE_TEXT,
					null);
			WorkersTable.workerPrivilegesCol = WorkersTable.workertable.addColumn(WORKERS_TABLE.ATTR_WORKER_PRIVILEGES,
					TYPE_INTEGER, null);
			WorkersTable.workersecurityAnswerCol = WorkersTable.workertable.addColumn(WORKERS_TABLE.ATTR_SECURITY_ANSWER, TYPE_TEXT,
					null);
			WorkersTable.workersecurityQuestionCol = WorkersTable.workertable.addColumn(WORKERS_TABLE.ATTR_SECURITY_QUESTION, TYPE_TEXT,
					null);
		}
		
		public WorkersTable(){
			super.table = WorkersTable.workertable;
			
			super.IDCol = WorkersTable.workerIDCol ;
			super.usernameCol = WorkersTable.workerusernameCol;
			super.passwordCol = WorkersTable.workerpasswordCol;
			super.isLoggedInCol = WorkersTable.workerisLoggedInCol;
			super.sessionIDCol = WorkersTable.workersessionIDCol;
			super.securityQuestionCol = WorkersTable.workersecurityQuestionCol;
			super.securityAnswerCol = WorkersTable.workersecurityAnswerCol;
		}

	}
	
	/**
	 * this class contains all the objects used buy SQLBuilder for Customers Table
	 * 
	 * @author noam
	 *
	 */
	static class CustomersTable extends ClientsTable{
		static DbTable customertable;
		
		static DbColumn customerIDCol;
		static DbColumn customerusernameCol;
		static DbColumn customerpasswordCol;
		static DbColumn customerisLoggedInCol;
		static DbColumn customersessionIDCol;
		static DbColumn customersecurityQuestionCol;
		static DbColumn customersecurityAnswerCol;
		
		static DbColumn customerFirstnameCol;
		static DbColumn customerLastnameCol;
		static DbColumn customerPhonenumberCol;
		static DbColumn customerEmailCol;
		static DbColumn customerCityCol;
		static DbColumn customerAddressCol;
		static DbColumn customerBirthdateCol;
		
		static {
			/*
			 * initialize the Costumers Table
			 */
			CustomersTable.customertable = databaseSchema.addTable(CUSTOMERS_TABLE.CUSTOMERS_TABLE);
			CustomersTable.customerIDCol = CustomersTable.customertable.addColumn(CUSTOMERS_TABLE.ATTR_CUSTOMER_ID, TYPE_ID, null);
			CustomersTable.customerisLoggedInCol = CustomersTable.customertable.addColumn(CUSTOMERS_TABLE.ATTR_IS_LOGGED_IN, TYPE_BOOLEAN, null);
			CustomersTable.customersessionIDCol = CustomersTable.customertable.addColumn(CUSTOMERS_TABLE.ATTR_SESSION_ID, TYPE_LONG, null);
			CustomersTable.customerpasswordCol = CustomersTable.customertable.addColumn(CUSTOMERS_TABLE.ATTR_CUSTOMER_PASSWORD, TYPE_TEXT,
					null);
			CustomersTable.customerusernameCol = CustomersTable.customertable.addColumn(CUSTOMERS_TABLE.ATTR_CUSTOMER_USERNAME, TYPE_TEXT,
					null);
			CustomersTable.customersecurityAnswerCol = CustomersTable.customertable.addColumn(CUSTOMERS_TABLE.ATTR_SECURITY_ANSWER, TYPE_TEXT,
					null);
			CustomersTable.customersecurityQuestionCol = CustomersTable.customertable.addColumn(CUSTOMERS_TABLE.ATTR_SECURITY_QUESTION, TYPE_TEXT,
					null);
			
			CustomersTable.customerAddressCol = CustomersTable.customertable.addColumn(CUSTOMERS_TABLE.ATTR_CUSTOMER_ADDRESS, TYPE_TEXT,
					null);
			CustomersTable.customerBirthdateCol = CustomersTable.customertable.addColumn(CUSTOMERS_TABLE.ATTR_CUSTOMER_BIRTHDATE, TYPE_DATE,
					null);
			CustomersTable.customerCityCol = CustomersTable.customertable.addColumn(CUSTOMERS_TABLE.ATTR_CUSTOMER_CITY, TYPE_TEXT,
					null);
			CustomersTable.customerEmailCol = CustomersTable.customertable.addColumn(CUSTOMERS_TABLE.ATTR_CUSTOMER_EMAIL, TYPE_TEXT,
					null);
			CustomersTable.customerFirstnameCol = CustomersTable.customertable.addColumn(CUSTOMERS_TABLE.ATTR_CUSTOMER_FIRSTNAME, TYPE_TEXT,
					null);
			CustomersTable.customerLastnameCol = CustomersTable.customertable.addColumn(CUSTOMERS_TABLE.ATTR_CUSTOMER_LASTNAME, TYPE_TEXT,
					null);
			CustomersTable.customerPhonenumberCol = CustomersTable.customertable.addColumn(CUSTOMERS_TABLE.ATTR_CUSTOMER_PHONENUMBER, TYPE_TEXT,
					null);

			
			CustomersTable.customertable.primaryKey(CustomersTable.customertable.getName(),
					CustomersTable.customerIDCol.getName(), CustomersTable.customerusernameCol.getName());
		}
		
		public CustomersTable() {
			super.table = CustomersTable.customertable;
			
			super.IDCol = CustomersTable.customerIDCol ;
			super.usernameCol = CustomersTable.customerusernameCol;
			super.passwordCol = CustomersTable.customerpasswordCol;
			super.isLoggedInCol = CustomersTable.customerisLoggedInCol;
			super.sessionIDCol = CustomersTable.customersessionIDCol;
			super.securityQuestionCol = CustomersTable.customersecurityQuestionCol;
			super.securityAnswerCol = CustomersTable.customersecurityAnswerCol;
		}
	}
	
	/**
	 * this class contains all the objects used buy SQLBuilder for
	 * CustomersIngredients Table
	 * 
	 * @author noam
	 *
	 */
	static class CustomersIngredientsTable {
		static DbTable table;

		static DbColumn customerUsernameCol;
		static DbColumn ingredientIDCol;
		
		static {
			/*
			 * initialize the CustomersIngredients Table
			 */
			CustomersIngredientsTable.table = databaseSchema.addTable(CUSTOMERS_INGREDIENTS_TABLE.CUSTOMERS_INGREDIENTS_TABLE);
			CustomersIngredientsTable.customerUsernameCol = CustomersIngredientsTable.table
					.addColumn(CUSTOMERS_INGREDIENTS_TABLE.ATTR_CUSTOMER_USERNAME, TYPE_TEXT, null);
			CustomersIngredientsTable.ingredientIDCol = CustomersIngredientsTable.table
					.addColumn(CUSTOMERS_INGREDIENTS_TABLE.ATTR_INGREDIENT_ID, TYPE_ID, null);
		}
	}
	
	/**
	 * This class contains all the free id's in all the table
	 * (when removing row - the id will go here) 
	 * 
	 * @author noam
	 *
	 */
	static class FreeIDsTable {
		static DbTable table;

		static DbColumn IDCol;
		static DbColumn fromTableNameCol;
		
		static {
			/*
			 * initialize the Free IDs Table
			 */
			FreeIDsTable.table = databaseSchema.addTable(FREE_IDS_TABLE.FREE_IDS_TABLE);
			FreeIDsTable.IDCol = FreeIDsTable.table.addColumn(FREE_IDS_TABLE.ATTR_ID, TYPE_ID, null);
			FreeIDsTable.fromTableNameCol = FreeIDsTable.table.addColumn(FREE_IDS_TABLE.ATTR_FROM_TABLE_NAME, TYPE_TEXT,
					null);
		}
	}

	/**
	 * the method is called in the first run of the program. the method assigns
	 * the class objects in this class
	 */
	@Deprecated
	public static void initialize() {


	}

}
