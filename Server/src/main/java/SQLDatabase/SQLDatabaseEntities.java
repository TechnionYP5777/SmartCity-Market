package SQLDatabase;

import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbSchema;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbSpec;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbTable;

import SQLDatabase.SQLDatabaseStrings.CARTS_LIST_TABLE;
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
 */
class SQLDatabaseEntities {

	/*
	 * constants
	 */
	private static final String TYPE_TEXT = "text";
	private static final String TYPE_INTEGER = "integer";
	private static final String TYPE_LONG = "integer";
	private static final String TYPE_REAL = "real";
	private static final String TYPE_DATE = "datetime";
	private static final String TYPE_BOOLEAN = "integer";
	private static final String TYPE_ID = TYPE_INTEGER;

	static DbSpec spec;
	static DbSchema databaseSchema;

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
		static DbColumn productPictureCol;
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
		static DbColumn locationIDCol;
		static DbColumn expirationDateCol;
		static DbColumn amountCol;
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
	}

	/**
	 * this class contains all the objects used buy SQLBuilder for CartsList
	 * Table
	 * 
	 * @author noam
	 *
	 */
	static class CartsListTable {
		static DbTable table;

		static DbColumn cartIDCol;
		static DbColumn listIDCol;
	}

	/**
	 * this class contains all the objects used buy SQLBuilder for Workers Table
	 * 
	 * @author noam
	 *
	 */
	static class WorkersTable {
		static DbTable table;

		static DbColumn workerIDCol;
		static DbColumn workerUsernameCol;
		static DbColumn workerPasswordCol;
		static DbColumn workerPrivilegesCol;
		static DbColumn isLoggedInCol;
		static DbColumn sessionIDCol;
	}

	/**
	 * the method is called in the first run of the program. the method assigns
	 * the class objects in this class
	 */
	public static void initialize() {
		// initializing the database fields
		spec = new DbSpec();
		databaseSchema = spec.addDefaultSchema();

		/*
		 * initialize the ingredients table
		 */
		IngredientsTable.table = databaseSchema.addTable(INGREDIENTS_TABLE.INGREDIENTS_TABLE);
		IngredientsTable.ingredientIDCol = IngredientsTable.table.addColumn(INGREDIENTS_TABLE.ATTR_INGREDIENT_ID,
				TYPE_ID, null);
		IngredientsTable.ingredientNameCol = IngredientsTable.table.addColumn(INGREDIENTS_TABLE.ATTR_INGREDIENT_NAME,
				TYPE_TEXT, null);
		// set primary key
		IngredientsTable.table.primaryKey(IngredientsTable.table.getName(), IngredientsTable.ingredientIDCol.getName());

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
		// set primary key
		ProductsCatalogTable.table.primaryKey(ProductsCatalogTable.table.getName(),
				ProductsCatalogTable.barcodeCol.getName());

		/*
		 * initialize the ProductsCatalogIngredients Table
		 */
		ProductsCatalogIngredientsTable.table = databaseSchema
				.addTable(PRODUCTS_CATALOG_INGREDIENTS_TABLE.PRODUCTS_CATALOG_INGREDIENTS_TABLE);
		ProductsCatalogIngredientsTable.barcodeCol = ProductsCatalogIngredientsTable.table
				.addColumn(PRODUCTS_CATALOG_INGREDIENTS_TABLE.ATTR_BARCODE, TYPE_LONG, null);
		ProductsCatalogIngredientsTable.ingredientIDCol = ProductsCatalogIngredientsTable.table
				.addColumn(PRODUCTS_CATALOG_INGREDIENTS_TABLE.ATTR_INGREDIENT_ID, TYPE_ID, null);

		/*
		 * initialize the ProductsCatalogLocations Table
		 */
		ProductsCatalogLocationsTable.table = databaseSchema
				.addTable(PRODUCTS_CATALOG_LOCATIONS_TABLE.PRODUCTS_CATALOG_LOCATIONS_TABLE);
		ProductsCatalogLocationsTable.barcodeCol = ProductsCatalogLocationsTable.table
				.addColumn(PRODUCTS_CATALOG_LOCATIONS_TABLE.ATTR_BARCODE, TYPE_LONG, null);
		ProductsCatalogLocationsTable.locationIDCol = ProductsCatalogLocationsTable.table
				.addColumn(PRODUCTS_CATALOG_LOCATIONS_TABLE.ATTR_LOCATION_ID, TYPE_ID, null);

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
		ProductsPackagesTable.locationIDCol = ProductsPackagesTable.table
				.addColumn(PRODUCTS_PACKAGES_TABLE.ATTR_LOCATION_ID, TYPE_ID, null);
		// set primary key
		ProductsPackagesTable.table.primaryKey(ProductsPackagesTable.table.getName(),
				ProductsPackagesTable.barcodeCol.getName(), ProductsPackagesTable.expirationDateCol.getName(),
				ProductsPackagesTable.locationIDCol.getName());

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

		/*
		 * initialize the CartsList Table
		 */
		CartsListTable.table = databaseSchema.addTable(CARTS_LIST_TABLE.CARTS_LIST_TABLE);
		CartsListTable.cartIDCol = CartsListTable.table.addColumn(CARTS_LIST_TABLE.ATTR_CART_ID, TYPE_ID, null);
		CartsListTable.listIDCol = CartsListTable.table.addColumn(CARTS_LIST_TABLE.ATTR_LIST_ID, TYPE_ID, null);

		/*
		 * initialize the Workers Table
		 */
		WorkersTable.table = databaseSchema.addTable(WORKERS_TABLE.WORKERS_TABLE);
		WorkersTable.workerIDCol = WorkersTable.table.addColumn(WORKERS_TABLE.ATTR_WORKER_ID, TYPE_ID, null);
		WorkersTable.isLoggedInCol = WorkersTable.table.addColumn(WORKERS_TABLE.ATTR_IS_LOGGED_IN, TYPE_BOOLEAN, null);
		WorkersTable.sessionIDCol = WorkersTable.table.addColumn(WORKERS_TABLE.ATTR_SESSION_ID, TYPE_LONG, null);
		WorkersTable.workerPasswordCol = WorkersTable.table.addColumn(WORKERS_TABLE.ATTR_WORKER_PASSWORD, TYPE_TEXT,
				null);
		WorkersTable.workerUsernameCol = WorkersTable.table.addColumn(WORKERS_TABLE.ATTR_WORKER_USERNAME, TYPE_TEXT,
				null);
		WorkersTable.workerPrivilegesCol = WorkersTable.table.addColumn(WORKERS_TABLE.ATTR_WORKER_PRIVILEGES,
				TYPE_INTEGER, null);

	}

}
