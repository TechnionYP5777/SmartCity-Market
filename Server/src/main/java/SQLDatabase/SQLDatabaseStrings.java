package SQLDatabase;

/**
 * SQLDatabaseStrings - contains strings to use in {@link SQLDatabaseConnection}
 * 
 * @author Noam Yefet
 * @since 2016-12-14
 */
public class SQLDatabaseStrings {

	/*
	 * INGREDIENTS TABLE
	 */
	public static class INGREDIENTS_TABLE {
		// table name
		public static final String INGREDIENTS_TABLE = "IngredientsTable";

		// table attributes
		public static final String ATTR_INGREDIENT_ID = "IngredientID";
		public static final String ATTR_INGREDIENT_NAME = "IngredientName";

	}

	/*
	 * MANUFACTURER TABLE
	 */
	public static class MANUFACTURER_TABLE {
		// table name
		public static final String MANUFACTURER_TABLE = "ManufacturerTable";

		// table attributes
		public static final String ATTR_MANUFACTURER_ID = "ManufacturerID";
		public static final String ATTR_MANUFACTURER_NAME = "ManufacturerName";

	}

	/*
	 * LOCATIONS TABLE
	 */
	public static class LOCATIONS_TABLE {
		// table name
		public static final String LOCATIONS_TABLE = "LocationsTable";

		// table attributes
		public static final String ATTR_LOCATION_ID = "LocationID";
		public static final String ATTR_PLACE_IN_STORE = "PlaceInStore";
		public static final String ATTR_LOCATION_DESCRIPTION = "LocationDescription";
		public static final String ATTR_POINT_X = "PointX";
		public static final String ATTR_POINT_Y = "PointY";

		// table values
		public static final String VALUE_PLACE_WAREHOUSE = "warehouse";
		public static final String VALUE_PLACE_STORE = "store";
	}

	/*
	 * PRODUCTS CATALOG TABLE
	 */
	public static class PRODUCTS_CATALOG_TABLE {
		// table name
		public static final String PRODUCTS_CATALOG_TABLE = "ProductsCatalogTable";

		// table attributes
		public static final String ATTR_BARCODE = "Barcode";
		public static final String ATTR_PRODUCT_NAME = "ProductName";
		public static final String ATTR_MANUFACTURER_ID = MANUFACTURER_TABLE.ATTR_MANUFACTURER_ID;
		public static final String ATTR_PRODUCT_DESCRIPTION = "ProductDescription";
		public static final String ATTR_PRODUCT_PRICE = "ProductPrice";
		public static final String ATTR_PRODUCT_PICTURE = "ProductPicture";
	}

	/*
	 * PRODUCTS CATALOG INGREDIENTS TABLE
	 */
	public static class PRODUCTS_CATALOG_INGREDIENTS_TABLE {
		// table name
		public static final String PRODUCTS_CATALOG_INGREDIENTS_TABLE = "ProductsCatalogIngredientsTable";

		// table attributes
		public static final String ATTR_BARCODE = PRODUCTS_CATALOG_TABLE.ATTR_BARCODE;
		public static final String ATTR_INGREDIENT_ID = INGREDIENTS_TABLE.ATTR_INGREDIENT_ID;
	}

	/*
	 * PRODUCTS CATALOG LOCATIONS TABLE
	 */
	public static class PRODUCTS_CATALOG_LOCATIONS_TABLE {
		// table name
		public static final String PRODUCTS_CATALOG_LOCATIONS_TABLE = "ProductsCatalogLocationsTable";

		// table attributes
		public static final String ATTR_BARCODE = PRODUCTS_CATALOG_TABLE.ATTR_BARCODE;
		public static final String ATTR_LOCATION_ID = LOCATIONS_TABLE.ATTR_LOCATION_ID;
	}

	/*
	 * PRODUCTS PACKAGES TABLE
	 */
	public static class PRODUCTS_PACKAGES_TABLE {
		// table name
		public static final String PRODUCTS_PACKAGES_TABLE = "ProductsPackagesTable";

		// table attributes
		public static final String ATTR_BARCODE = PRODUCTS_CATALOG_TABLE.ATTR_BARCODE;
		public static final String ATTR_PLACE_IN_STORE = LOCATIONS_TABLE.ATTR_PLACE_IN_STORE;
		public static final String ATTR_EXPIRATION_DATE = "ExpirationDate";
		public static final String ATTR_AMOUNT = "Amount";
		
		// table values
		public static final String VALUE_PLACE_WAREHOUSE = LOCATIONS_TABLE.VALUE_PLACE_WAREHOUSE;
		public static final String VALUE_PLACE_STORE = LOCATIONS_TABLE.VALUE_PLACE_STORE;
	}

	/*
	 * GROCERIES LISTS TABLE
	 */
	public static class GROCERIES_LISTS_TABLE {
		// table name
		public static final String GROCERIES_LISTS_TABLE = "GroceriesListsTable";

		// table attributes
		public static final String ATTR_LIST_ID = "ListID";
		public static final String ATTR_BARCODE = PRODUCTS_CATALOG_TABLE.ATTR_BARCODE;
		public static final String ATTR_EXPIRATION_DATE = "ExpirationDate";
		public static final String ATTR_AMOUNT = "Amount";
	}

	/*
	 * GROCERIES LISTS HISTORY TABLE
	 */
	public static class GROCERIES_LISTS_HISTORY_TABLE {
		// table name
		public static final String GROCERIES_LISTS_HISTORY_TABLE = "GroceriesListsHistoryTable";

		// table attributes
		public static final String ATTR_LIST_ID = GROCERIES_LISTS_TABLE.ATTR_LIST_ID;
		public static final String ATTR_BARCODE = PRODUCTS_CATALOG_TABLE.ATTR_BARCODE;
		public static final String ATTR_EXPIRATION_DATE = GROCERIES_LISTS_TABLE.ATTR_EXPIRATION_DATE;
		public static final String ATTR_AMOUNT = GROCERIES_LISTS_TABLE.ATTR_AMOUNT;
	}

	/*
	 * CARTS LIST TABLE
	 */
	public static class CARTS_LIST_TABLE {
		// table name
		public static final String CARTS_LIST_TABLE = "CartsListTable";

		// table attributes
		public static final String ATTR_CART_ID = "CartID";
		public static final String ATTR_LIST_ID = GROCERIES_LISTS_TABLE.ATTR_LIST_ID;

	}

	/*
	 * WORKERS TABLE
	 */
	public static class WORKERS_TABLE {
		// table name
		public static final String WORKERS_TABLE = "WorkersTable";

		// table attributes
		public static final String ATTR_WORKER_ID = "WorkerID";
		public static final String ATTR_WORKER_USERNAME = "WorkerUsername";
		public static final String ATTR_WORKER_PASSWORD = "WorkerPassword";
		public static final String ATTR_WORKER_PRIVILEGES = "WorkerPrivileges";
		public static final String ATTR_IS_LOGGED_IN = "IsLoggedIn";
		public static final String ATTR_SESSION_ID = "SessionID";

		// table values
		public static final Integer VALUE_PRIVILEGE_MANAGER = 1;
		public static final Integer VALUE_PRIVILEGE_WORKER = 4;
	}
	
	/*
	 * FREE IDs TABLE
	 */
	public static class FREE_IDS_TABLE {
		// table name
		public static final String FREE_IDS_TABLE = "FreeIDsTable";

		// table attributes
		public static final String ATTR_ID = "ID";
		public static final String ATTR_FROM_TABLE_NAME = "FromTableName";
	}

}
