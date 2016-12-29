package SQLDatabase;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

import com.google.gson.Gson;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;

import BasicCommonClasses.CatalogProduct;
import BasicCommonClasses.Ingredient;
import BasicCommonClasses.Location;
import BasicCommonClasses.Manufacturer;
import BasicCommonClasses.PlaceInMarket;
import SQLDatabase.SQLDatabaseEntities.IngredientsTable;
import SQLDatabase.SQLDatabaseEntities.LocationsTable;
import SQLDatabase.SQLDatabaseEntities.ManufacturerTable;
import SQLDatabase.SQLDatabaseEntities.ProductsCatalogIngredientsTable;
import SQLDatabase.SQLDatabaseEntities.ProductsCatalogLocationsTable;
import SQLDatabase.SQLDatabaseEntities.ProductsCatalogTable;
import SQLDatabase.SQLDatabaseException.CriticalError;
import SQLDatabase.SQLDatabaseStrings.LOCATIONS_TABLE;

class SQLJsonGenerator {

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
				String ingdientName = getStringFromResultset(productIngredients, IngredientsTable.ingredientNameCol);

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
				String locationDescription = getStringFromResultset(productLocations, LocationsTable.locationDescriptionCol);
				String locationPlace = getStringFromResultset(productLocations, LocationsTable.placeInStoreCol);
				int locationPointX = productLocations.getInt(LocationsTable.pointXCol.getColumnNameSQL());
				int locationPointY = productLocations.getInt(LocationsTable.pointYCol.getColumnNameSQL());

				// adding the location to set
				$.add(new Location(locationPointX, locationPointY, locationPlace == LOCATIONS_TABLE.VALUE_PLACE_STORE ? PlaceInMarket.STORE : PlaceInMarket.WAREHOUSE));
				}

				productLocations.next();
				}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLDatabaseException.CriticalError();
		}

		return $;
	}
}
