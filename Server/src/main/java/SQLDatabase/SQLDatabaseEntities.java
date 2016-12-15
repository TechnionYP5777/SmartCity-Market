package SQLDatabase;

import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbSchema;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbSpec;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbTable;

/**
 * SQLDatabaseEntities - contains objects to use with sqlbuilder in
 * {@link SQLDatabaseConnection}
 * 
 * @author Noam Yefet
 */
class SQLDatabaseEntities {

	static DbSpec spec;
	static DbSchema databaseSchema;

	static class IngredientsTable {
		static DbTable ingredientsTable;

		static DbColumn ingredientIDCol;
		static DbColumn ingredientNameCol;
	}

	static class ManufacturerTable {
		static DbTable manufacturerTable;

		static DbColumn manufacturerIDCol;
		static DbColumn manufacturerNameCol;
	}

	static class LocationsTable {
		static DbTable locationsTable;

		static DbColumn locationIDCol;
		static DbColumn placeInStoreCol;
		static DbColumn locationDescriptionCol;
		static DbColumn pointXCol;
		static DbColumn pointYCol;
	}

	static class ProductsCatalogTable {
		static DbTable productsCatalogTable;

		static DbColumn barcodeCol;
		static DbColumn productNameCol;
		static DbColumn manufacturerIDCol;
		static DbColumn productDescriptionCol;
		static DbColumn productPriceCol;
		static DbColumn productPictureCol;
	}

	static class ProductsCatalogIngredientsTable {
		static DbTable productsCatalogIngredientsTable;

		static DbColumn barcodeCol;
		static DbColumn ingredientIDCol;
	}

	static class ProductsCatalogLocationsTable {
		static DbTable productsCatalogLocationsTable;

		static DbColumn barcodeCol;
		static DbColumn locationIDCol;
	}

	static class ProductsPackagesTable {
		static DbTable productsPackagesTable;

		static DbColumn barcodeCol;
		static DbColumn locationIDCol;
		static DbColumn expirationDateCol;
		static DbColumn amountCol;
	}

	static class GroceriesListsTable {
		static DbTable groceriesListsTable;

		static DbColumn listIDCol;
		static DbColumn barcodeCol;
		static DbColumn expirationDateCol;
		static DbColumn amountCol;
	}

	static class GroceriesListsHistoryTable {
		static DbTable groceriesListsHistoryTable;

		static DbColumn listIDCol;
		static DbColumn barcodeCol;
		static DbColumn expirationDateCol;
		static DbColumn amountCol;
	}

	static class CartsListTable {
		static DbTable cartsListTable;

		static DbColumn cartIDCol;
		static DbColumn listIDCol;
	}

	static class WorkersTable {
		static DbTable workersTable;

		static DbColumn workerIDCol;
		static DbColumn workerUsernameCol;
		static DbColumn workerPasswordCol;
		static DbColumn workerPrivilegesCol;
		static DbColumn isLoggedInCol;
		static DbColumn sessionIDCol;
	}

}
