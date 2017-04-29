package SQLDatabase;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;

import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;

import BasicCommonClasses.CatalogProduct;
import BasicCommonClasses.CustomerProfile;
import BasicCommonClasses.GroceryList;
import BasicCommonClasses.Ingredient;
import BasicCommonClasses.Location;
import BasicCommonClasses.Manufacturer;
import BasicCommonClasses.PlaceInMarket;
import BasicCommonClasses.ProductPackage;
import BasicCommonClasses.SmartCode;
import SQLDatabase.SQLDatabaseEntities.CustomersIngredientsTable;
import SQLDatabase.SQLDatabaseEntities.CustomersTable;
import SQLDatabase.SQLDatabaseEntities.GroceriesListsTable;
import SQLDatabase.SQLDatabaseEntities.IngredientsTable;
import SQLDatabase.SQLDatabaseEntities.LocationsTable;
import SQLDatabase.SQLDatabaseEntities.ManufacturerTable;
import SQLDatabase.SQLDatabaseEntities.ProductsCatalogIngredientsTable;
import SQLDatabase.SQLDatabaseEntities.ProductsCatalogLocationsTable;
import SQLDatabase.SQLDatabaseEntities.ProductsCatalogTable;
import SQLDatabase.SQLDatabaseEntities.WorkersTable;
import SQLDatabase.SQLDatabaseException.CriticalError;
import SQLDatabase.SQLDatabaseStrings.LOCATIONS_TABLE;
import UtilsImplementations.Serialization;

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
			ingredients = createIngredientsListByBarcode(productBarcode, productIngredients);

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
			return Serialization.serialize(new CatalogProduct(productBarcode, productName, ingredients,
					new Manufacturer(productManufacturerID, productManufacturerName), productDescription, productPrice,
					productPicture, locations));

		} catch (SQLException e) {
			throw new SQLDatabaseException.CriticalError();
		}

	}
	
	/**
	 * create ingredients list for specified barcode.
	 *
	 * @param barcode (assuming the resultset is ordered by barcode column)
	 * @param productIngredients
	 *            the resultset of ingredients (assuming the result set pointing
	 *            to the first ingredient with specified barcode OR pointing the
	 *            AfterLast row OR the resultset is empty.
	 * @return
	 * @throws CriticalError
	 */
	private static HashSet<Ingredient> createIngredientsListByBarcode(long productBarcode, ResultSet productIngredients)
			throws CriticalError {
//		HashSet<Ingredient> $ = new HashSet<Ingredient>();
//
//		try {
//			if (productIngredients.getRow() != 0)
//				// adding all ingredients
//				while (!productIngredients.isAfterLast() && productBarcode == productIngredients
//						.getLong(ProductsCatalogIngredientsTable.barcodeCol.getColumnNameSQL())) {
//
//					// extracting the ingredients
//					Ingredient ingredient = newIngredientFromResultset(productIngredients);
//					if (ingredient != null)
//						$.add(ingredient);
//
//					productIngredients.next();
//				}
//		} catch (SQLException e) {
//			throw new SQLDatabaseException.CriticalError();
//		}
//
//		return $;
		return createIngredientsList(productBarcode, ProductsCatalogIngredientsTable.barcodeCol, productIngredients);
	}
	
	
	/**
	 * convert customerProfile from ResultSet to Json representation of product
	 * 
	 * @param customer
	 *            - ResultSet of the customer profile. the
	 *            ResultSet need to point to the username to convert). this
	 *            object will point the next product after returning.
	 * @param customerIngredients
	 *            - ResultSet of the customer\s ingredients (assuming the
	 *            ResultSet ordered by username column) the ResultSet should
	 *            pointing the product to convert, if it has ingredients. if so,
	 *            this object will point the next customer (or after last line) after returning.
	 * @return
	 * @throws CriticalError
	 */
	static String CostumerProfileToJson(ResultSet customer, ResultSet customerIngredients)
			throws CriticalError {

		HashSet<Ingredient> ingredients;

		try {
			
			//get customer username
			String customerUsername = getStringFromResultset(customer, CustomersTable.customerusernameCol);

			// get all customer ingredients
			ingredients = createIngredientsListForCustomer(customerUsername, customerIngredients);

			// get customer other details
			String customeraddress = getStringFromResultset(customer, CustomersTable.customerAddressCol);
			String customerCity = getStringFromResultset(customer, CustomersTable.customerCityCol);
			String customerEmail = getStringFromResultset(customer, CustomersTable.customerEmailCol);
			String customerFirstname = getStringFromResultset(customer, CustomersTable.customerFirstnameCol);
			String customerLastname = getStringFromResultset(customer, CustomersTable.customerLastnameCol);
			String customerPhonenumber = getStringFromResultset(customer, CustomersTable.customerPhonenumberCol);
			LocalDate customerBirthdate = customer.getDate(CustomersTable.customerBirthdateCol.getColumnNameSQL()).toLocalDate();

			customer.next();
			return Serialization.serialize(new CustomerProfile(customerUsername, null, customerFirstname, customerLastname,
					customerPhonenumber, customerEmail, customerCity, customeraddress, customerBirthdate, ingredients, null));

		} catch (SQLException e) {
			throw new SQLDatabaseException.CriticalError();
		}

	}
	
	/**
	 * create ingredients list for specified customer.
	 *
	 * @param username the username of the costumer to get its ingredients (assuming the resultset is ordered by username column)
	 * @param customerIngredients
	 *            the resultset of ingredients (assuming the result set pointing
	 *            to the first ingredient with specified barcode OR pointing the
	 *            AfterLast row OR the resultset is empty.
	 * @return
	 * @throws CriticalError
	 */
	private static HashSet<Ingredient> createIngredientsListForCustomer(String username, ResultSet customerIngredients)
			throws CriticalError {

		return createIngredientsList(username, CustomersIngredientsTable.customerUsernameCol, customerIngredients);
	}
	
	
	/**
	 * generic method that creating ingredients list for given resultset (either for customer or product)
	 * 
	 * @param whileThisID assuming resultset is ordered by columnToCheckIn, the extraction of ingredients will be only while the row contain that value.
	 * 			if this parameter is null then this constrain is ignored (and the parameter columnToCheckIn is irrelevant too)
	 * @param columnToCheckIn assuming resultset is ordered by columnToCheckIn, this column define which column to compare to whileThisID
	 * @param ingredientsSet
	 * 			  the resultset of ingredients (assuming the result set pointing
	 *            to the first ingredient with specified barcode OR pointing the
	 *            AfterLast row OR the resultset is empty.
	 * @return
	 * @throws CriticalError
	 */
	private static<T> HashSet<Ingredient> createIngredientsList(T whileThisID, DbColumn columnToCheckIn, ResultSet ingredientsSet)
			throws CriticalError {
		HashSet<Ingredient> $ = new HashSet<Ingredient>();

		try {
			if (ingredientsSet.getRow() != 0)
				// adding all ingredients
				while (!ingredientsSet.isAfterLast() && 
						( (whileThisID == null) || whileThisID.equals(ingredientsSet.getObject(columnToCheckIn.getColumnNameSQL())) ) ) {

					// extracting the ingredients
					Ingredient ingredient = newIngredientFromResultset(ingredientsSet);
					if (ingredient != null)
						$.add(ingredient);

					ingredientsSet.next();
				}
		} catch (SQLException e) {
			throw new SQLDatabaseException.CriticalError();
		}

		return $;
	}
	
	/**
	 * Create Ingredient from current row of an resultset
	 * @param ingredients the result to extract the data from. the resultset must contain the column: ingredientIDCol, ingredientNameCol
	 * @return new ingredient. on error - return null
	 * @throws SQLException 
	 * @throws CriticalError 
	 */
	private static Ingredient newIngredientFromResultset(ResultSet ingredients) throws SQLException, CriticalError{
		Ingredient result = null;
		
		// extracting the ingredient
		int ingredientId = ingredients.getInt(IngredientsTable.ingredientIDCol.getColumnNameSQL());
		if (!ingredients.wasNull()) {
			String ingdientName = getStringFromResultset(ingredients,
					IngredientsTable.ingredientNameCol);

			// adding the ingredient to set
			result = new Ingredient(ingredientId, ingdientName);
		}
		
		return result;
	}
	
	/**
	 * convert ingredients list from ResultSet to Json representation of list
	 * of ingredients list
	 * 
	 * @param ingredientsSet
	 * 			  - ResultSet of the ingredientsList. The ResultSet should
	 *            point the first row. At returning, this object will point the
	 *            next row after the last row.
	 * @return Json representation of the ingredients list
	 * @throws CriticalError
	 */
	public static String allIngredientsListToJson(ResultSet ingredientsSet)
			throws CriticalError {
		return Serialization.serialize(createIngredientsList(null, null, ingredientsSet));
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

		return Serialization.serialize(resultSetToGroceryList(groceryList));

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
	 * @return Json representation of the manufacturers list
	 * @throws CriticalError
	 */
	static String manufaturersListToJson(ResultSet manufaturersList) throws CriticalError {
		HashSet<Manufacturer> $ = new HashSet<>();

		try {
			if (manufaturersList.getRow() != 0)
				while (!manufaturersList.isAfterLast()) {
					int manufaturerID = manufaturersList.getInt(ManufacturerTable.manufacturerIDCol.getColumnNameSQL());
					String manufaturerName = getStringFromResultset(manufaturersList,
							ManufacturerTable.manufacturerNameCol);
					$.add(new Manufacturer(manufaturerID, manufaturerName));
					manufaturersList.next();
				}
		} catch (SQLException e) {
			throw new SQLDatabaseException.CriticalError();
		}

		return Serialization.serialize($);

	}
	
	/**
	 * convert workers list from ResultSet to list of workers usernames
	 * 
	 * @param workersList
	 *            - ResultSet of the workersList. The ResultSet should
	 *            point the first row. At returning, this object will point the
	 *            next row after the last row.
	 * @return workers usernames list
	 * @throws CriticalError
	 */
	static HashSet<String> createWorkersList(ResultSet workersList) throws CriticalError {
		HashSet<String> $ = new HashSet<>();

		try {
			if (workersList.getRow() != 0)
				while (!workersList.isAfterLast()) {
					
					String workerUsername = getStringFromResultset(workersList,
							WorkersTable.workerusernameCol);
					$.add(workerUsername);
					workersList.next();
				}
		} catch (SQLException e) {
			throw new SQLDatabaseException.CriticalError();
		}

		return $;

	}
}
