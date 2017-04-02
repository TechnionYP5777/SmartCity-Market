package SQLDatabase;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;

import com.google.gson.Gson;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;

import BasicCommonClasses.CatalogProduct;
import BasicCommonClasses.GroceryList;
import BasicCommonClasses.Ingredient;
import BasicCommonClasses.Location;
import BasicCommonClasses.Manufacturer;
import BasicCommonClasses.PlaceInMarket;
import BasicCommonClasses.ProductPackage;
import BasicCommonClasses.SmartCode;
import SQLDatabase.SQLDatabaseEntities.GroceriesListsTable;
import SQLDatabase.SQLDatabaseEntities.IngredientsTable;
import SQLDatabase.SQLDatabaseEntities.LocationsTable;
import SQLDatabase.SQLDatabaseEntities.ManufacturerTable;
import SQLDatabase.SQLDatabaseEntities.ProductsCatalogIngredientsTable;
import SQLDatabase.SQLDatabaseEntities.ProductsCatalogLocationsTable;
import SQLDatabase.SQLDatabaseEntities.ProductsCatalogTable;
import SQLDatabase.SQLDatabaseException.CriticalError;
import SQLDatabase.SQLDatabaseStrings.LOCATIONS_TABLE;

class SQLJsonGenerator {

	/**
	 * Get string from Resultset. This method make sure we always get string
	 * (and not null)
	 * 
	 * @param s
	 *            Resultset to get from
	 * @param c
	 *            specified column to fetch
	 * @return
	 * @throws CriticalError
	 */
	private static String getStringFromResultset(ResultSet s, DbColumn c) throws CriticalError {

		String result;
		try {
			result = s.getString(c.getColumnNameSQL());
			return s.wasNull() ? "" : result;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLDatabaseException.CriticalError();
		}

	}

	/**
	 * convert product from ResultSet to Json representation of product
	 * 
	 * @param product
	 *            - ResultSet of the product\s joined with manufactures table
	 *            (assuming the ResultSet ordered by product barcode) the
	 *            ResultSet need to point to the product to convert). this
	 *            object will point the next product after returning.
	 * @param productIngredients
	 *            - ResultSet of the product\s ingredients (assuming the
	 *            ResultSet ordered by product barcode) the ResultSet should
	 *            pointing the product to convert, if it has ingredients. if so,
	 *            this object will point the next product after returning.
	 * @param productLocations
	 *            - ResultSet of the product\s locations (assuming the ResultSet
	 *            ordered by product barcode) the ResultSet should pointing the
	 *            product to convert, if it has ingredients. if so, this object
	 *            will point the next product after returning.
	 * @return
	 * @throws CriticalError
	 */
	static String ProductToJson(ResultSet product, ResultSet productIngredients, ResultSet productLocations)
			throws CriticalError {

		HashSet<Location> locations;
		HashSet<Ingredient> ingredients;

		try {
			long productBarcode = product.getLong(ProductsCatalogTable.barcodeCol.getColumnNameSQL());

			// adding all ingredients
			ingredients = createIngredientsList(productBarcode, productIngredients);

			// adding all locations
			locations = createLocationsList(productBarcode, productLocations);

			// get product other details
			String productManufacturerName = getStringFromResultset(product, ManufacturerTable.manufacturerNameCol);
			int productManufacturerID = product.getInt(ManufacturerTable.manufacturerIDCol.getColumnNameSQL());
			String productDescription = getStringFromResultset(product, ProductsCatalogTable.productDescriptionCol);
			String productName = getStringFromResultset(product, ProductsCatalogTable.productNameCol);
			String productPicture = getStringFromResultset(product, ProductsCatalogTable.productPictureCol);
			double productPrice = product.getDouble(ProductsCatalogTable.productPriceCol.getColumnNameSQL());

			product.next();
			return new Gson().toJson(new CatalogProduct(productBarcode, productName, ingredients,
					new Manufacturer(productManufacturerID, productManufacturerName), productDescription, productPrice,
					productPicture, locations));

		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLDatabaseException.CriticalError();
		}

	}

	/**
	 * create ingredients list for specified barcode.
	 *
	 * @param barcode
	 * @param productIngredients
	 *            the resultset of ingredients (assuming the result set pointing
	 *            to the first ingredient with specified barcode OR pointing the
	 *            AfterLast row OR the resultset is empty.
	 * @return
	 * @throws CriticalError
	 */
	private static HashSet<Ingredient> createIngredientsList(long productBarcode, ResultSet productIngredients)
			throws CriticalError {
		HashSet<Ingredient> $ = new HashSet<Ingredient>();

		try {
			if (productIngredients.getRow() != 0)
				// adding all ingredients
				while (!productIngredients.isAfterLast() && productBarcode == productIngredients
						.getLong(ProductsCatalogIngredientsTable.barcodeCol.getColumnNameSQL())) {

					// extracting the ingredients
					int ingredientId = productIngredients.getInt(IngredientsTable.ingredientIDCol.getColumnNameSQL());
					if (!productIngredients.wasNull()) {
						String ingdientName = getStringFromResultset(productIngredients,
								IngredientsTable.ingredientNameCol);

						// adding the ingredient to set
						$.add(new Ingredient(ingredientId, ingdientName));
					}

					productIngredients.next();
				}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLDatabaseException.CriticalError();
		}

		return $;
	}

	/**
	 * create locations list for specified barcode.
	 *
	 * @param barcode
	 * @param productLocations
	 *            the resultset of locations (assuming the result set pointing
	 *            to the first location with specified barcode OR pointing the
	 *            AfterLast row OR the resultset is empty.
	 * @return
	 * @throws CriticalError
	 */
	private static HashSet<Location> createLocationsList(long productBarcode, ResultSet productLocations)
			throws CriticalError {

		HashSet<Location> $ = new HashSet<Location>();

		try {
			if (productLocations.getRow() != 0)
				// adding all locations
				while (!productLocations.isAfterLast() && productBarcode == productLocations
						.getLong(ProductsCatalogLocationsTable.barcodeCol.getColumnNameSQL())) {
					// extracting the location
					@SuppressWarnings("unused")
					int locationId = productLocations.getInt(LocationsTable.locationIDCol.getColumnNameSQL());

					if (!productLocations.wasNull()) {
						@SuppressWarnings("unused")
						String locationDescription = getStringFromResultset(productLocations,
								LocationsTable.locationDescriptionCol);
						String locationPlace = getStringFromResultset(productLocations, LocationsTable.placeInStoreCol);
						int locationPointX = productLocations.getInt(LocationsTable.pointXCol.getColumnNameSQL());
						int locationPointY = productLocations.getInt(LocationsTable.pointYCol.getColumnNameSQL());

						// adding the location to set
						$.add(new Location(locationPointX, locationPointY,
								locationPlace == LOCATIONS_TABLE.VALUE_PLACE_STORE ? PlaceInMarket.STORE
										: PlaceInMarket.WAREHOUSE));
					}

					productLocations.next();
				}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLDatabaseException.CriticalError();
		}

		return $;
	}

	/**
	 * convert grocery list from ResultSet to Json representation of grocery
	 * list
	 * 
	 * @param groceryList
	 *            - ResultSet of the groceryList. the ResultSet need to point to
	 *            the groceryList to convert (and assuming all the rows of given
	 *            groceryList are grouped together). at returning, this object
	 *            will point the next row after the last row of the pointed
	 *            groceryList returning.
	 * @return Json representation of the grocery list
	 * @throws CriticalError
	 */
	static String GroceryListToJson(ResultSet groceryList) throws CriticalError {

		return new Gson().toJson(resultSetToGroceryList(groceryList));

	}

	/**
	 * convert grocery list from ResultSet to GroceryList object list
	 * 
	 * @param groceryList
	 *            - ResultSet of the groceryList. the ResultSet need to point to
	 *            the groceryList to convert (and assuming all the rows of given
	 *            groceryList are grouped together). at returning, this object
	 *            will point the next row after the last row of the pointed
	 *            groceryList returning.
	 * @return grocery list with the packages from the resultset
	 * @throws CriticalError
	 */
	static GroceryList resultSetToGroceryList(ResultSet groceryList) throws CriticalError {
		GroceryList $ = new GroceryList();

		try {
			if (groceryList.getRow() != 0)
				for (int groceryListID = groceryList
						.getInt(GroceriesListsTable.listIDCol.getColumnNameSQL()); !groceryList.isAfterLast()
								&& groceryListID == groceryList
										.getInt(GroceriesListsTable.listIDCol.getColumnNameSQL());) {
					long barcode = groceryList.getLong(GroceriesListsTable.barcodeCol.getColumnNameSQL());
					int amount = groceryList.getInt(GroceriesListsTable.amountCol.getColumnNameSQL());
					LocalDate expirationDate = groceryList
							.getDate(GroceriesListsTable.expirationDateCol.getColumnNameSQL()).toLocalDate();
					$.addProduct(new ProductPackage(new SmartCode(barcode, expirationDate), amount, null));
					groceryList.next();
				}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLDatabaseException.CriticalError();
		}

		return $;

	}

	/**
	 * convert manufacturers list from ResultSet to Json representation of list
	 * of manufacturers list
	 * 
	 * @param manufaturersList
	 *            - ResultSet of the manufaturersList. The ResultSet should
	 *            point the first row. At returning, this object will point the
	 *            next row after the last row.
	 * @return Json representation of the grocery list
	 * @throws CriticalError
	 */
	static String manufaturersListToJson(ResultSet manufaturersList) throws CriticalError {
		HashMap<Integer, Manufacturer> $ = new HashMap<>();

		try {
			if (manufaturersList.getRow() != 0)
				while (!manufaturersList.isAfterLast()) {
					int manufaturerID = manufaturersList.getInt(ManufacturerTable.manufacturerIDCol.getColumnNameSQL());
					String manufaturerName = getStringFromResultset(manufaturersList,
							ManufacturerTable.manufacturerNameCol);
					$.put(manufaturerID, new Manufacturer(manufaturerID, manufaturerName));
					manufaturersList.next();
				}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLDatabaseException.CriticalError();
		}

		return new Gson().toJson($);

	}
}
