package SQLDatabase;

import java.sql.Statement;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Random;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.CreateTableQuery;
import com.healthmarketscience.sqlbuilder.CustomCondition;
import com.healthmarketscience.sqlbuilder.DeleteQuery;
import com.healthmarketscience.sqlbuilder.FunctionCall;
import com.healthmarketscience.sqlbuilder.InsertQuery;
import com.healthmarketscience.sqlbuilder.JdbcEscape;
import com.healthmarketscience.sqlbuilder.SelectQuery;
import com.healthmarketscience.sqlbuilder.SqlObject;
import com.healthmarketscience.sqlbuilder.UnaryCondition;
import com.healthmarketscience.sqlbuilder.UpdateQuery;
import com.healthmarketscience.sqlbuilder.ValidationException;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbTable;

import BasicCommonClasses.CatalogProduct;
import BasicCommonClasses.Ingredient;
import BasicCommonClasses.Location;
import BasicCommonClasses.Manufacturer;
import BasicCommonClasses.PlaceInMarket;
import BasicCommonClasses.ProductPackage;
import BasicCommonClasses.SmartCode;
import CommonDefs.CLIENT_TYPE;
import SQLDatabase.SQLDatabaseEntities;
import SQLDatabase.SQLDatabaseEntities.CartsListTable;
import SQLDatabase.SQLDatabaseEntities.FreeIDsTable;
import SQLDatabase.SQLDatabaseEntities.GroceriesListsHistoryTable;
import SQLDatabase.SQLDatabaseEntities.GroceriesListsTable;
import SQLDatabase.SQLDatabaseEntities.IngredientsTable;
import SQLDatabase.SQLDatabaseEntities.LocationsTable;
import SQLDatabase.SQLDatabaseEntities.ManufacturerTable;
import SQLDatabase.SQLDatabaseEntities.ProductsCatalogIngredientsTable;
import SQLDatabase.SQLDatabaseEntities.ProductsCatalogLocationsTable;
import SQLDatabase.SQLDatabaseEntities.ProductsCatalogTable;
import SQLDatabase.SQLDatabaseEntities.ProductsPackagesTable;
import SQLDatabase.SQLDatabaseEntities.WorkersTable;
import SQLDatabase.SQLDatabaseException.AuthenticationError;
import SQLDatabase.SQLDatabaseException.CriticalError;
import SQLDatabase.SQLDatabaseException.NumberOfConnectionsExceeded;
import SQLDatabase.SQLDatabaseException.ProductAlreadyExistInCatalog;
import SQLDatabase.SQLDatabaseException.ProductNotExistInCatalog;
import SQLDatabase.SQLDatabaseException.ManufacturerNotExist;
import SQLDatabase.SQLDatabaseException.ManufacturerStillUsed;
import SQLDatabase.SQLDatabaseException.IngredientNotExist;
import SQLDatabase.SQLDatabaseException.ProductPackageAmountNotMatch;
import SQLDatabase.SQLDatabaseException.ProductPackageNotExist;
import SQLDatabase.SQLDatabaseException.ProductStillForSale;
import SQLDatabase.SQLDatabaseException.ClientAlreadyConnected;
import SQLDatabase.SQLDatabaseException.ClientNotConnected;
import SQLDatabase.SQLDatabaseStrings.LOCATIONS_TABLE;
import SQLDatabase.SQLDatabaseStrings.PRODUCTS_PACKAGES_TABLE;
import SQLDatabase.SQLDatabaseStrings.WORKERS_TABLE;

import static SQLDatabase.SQLQueryGenerator.generateSelectQuery1Table;
import static SQLDatabase.SQLQueryGenerator.generateSelectLeftJoinWithQuery2Tables;
import static SQLDatabase.SQLQueryGenerator.generateUpdateQuery;
import static SQLDatabase.SQLQueryGenerator.generateDeleteQuery;

/**
 * SqlDBConnection - Handles the server request to the SQL database.
 * 
 * @author Noam Yefet
 * @since 2016-12-14
 * 
 */
public class SQLDatabaseConnection implements ISQLDatabaseConnection {

	static Logger log = Logger.getLogger(SQLDatabaseConnection.class.getName());
	
	private enum LOCATIONS_TYPES {
		WAREHOUSE, STORE, CART
	}

	/*
	 * Database parameters
	 */
	private static final String DATABASE_NAME = "SQLdatabase";
	private static final String DATABASE_PATH = "./src/main/resources/SQLDatabase/" + DATABASE_NAME;
	private static final String DATABASE_PARAMS = ";sql.syntax_mys=true";
	private static final String DATABASE_PATH_PARAMS = DATABASE_PATH + DATABASE_PARAMS;

	/*
	 * Queries parameters
	 */
	private static final String PARAM_MARK = "?";
	private static final String QUATED_PARAM_MARK = "'" + PARAM_MARK + "'";
	private static final String SQL_PARAM = "?";

	/**
	 * IMPORTANT: this parameter (SESSION_IDS_BEGIN) determines the number of
	 * cart in the system. the number divides the range of ints between the cart
	 * and the workers: number 0 - (SESSION_IDS_BEGIN-1) => FOR THE CARTS number
	 * SESSION_IDS_BEGIN - (MAX_INT) => FOR THE WORKER
	 * 
	 * I did this because its make authentication more simple (the session id
	 * determine the client type
	 * 
	 * BUT I will change it
	 */
	private static final Integer SESSION_IDS_BEGIN = 10000;
	private static final Integer TRYS_NUMBER = 1000;

	private Connection connection;
	private static boolean isEntitiesInitialized;

	public SQLDatabaseConnection() {

		// initialize entities object in first-run
		if (!isEntitiesInitialized) {
			SQLDatabaseEntities.initialize();
			isEntitiesInitialized = true;
		}

		// check if database exist on disk
		if (isDatabaseExists())
			// connect to database
			try {
			connection = DriverManager.getConnection("jdbc:hsqldb:file:" + DATABASE_PATH_PARAMS + ";ifexists=true", "SA", "");
			} catch (SQLException e) {
			e.printStackTrace();
			}
		else {
			// connect and create database
			try {
				connection = DriverManager.getConnection("jdbc:hsqldb:file:" + DATABASE_PATH_PARAMS, "SA", "");
			} catch (SQLException e) {
				e.printStackTrace();
			}

			// creates tables
			createTables();
		}

	}

	/**
	 * Creates SQL tables
	 */
	private void createTables() {

		try {
			Statement statement = connection.createStatement();
			String createTableString = new CreateTableQuery(IngredientsTable.table, true).validate() + "";

			statement.executeUpdate(createTableString);
			createTableString = new CreateTableQuery(LocationsTable.table, true).validate() + "";
			statement.executeUpdate(createTableString);
			createTableString = new CreateTableQuery(ManufacturerTable.table, true).validate() + "";
			statement.executeUpdate(createTableString);
			createTableString = new CreateTableQuery(ProductsCatalogTable.table, true).validate() + "";
			statement.executeUpdate(createTableString);
			createTableString = new CreateTableQuery(ProductsCatalogIngredientsTable.table, true).validate() + "";
			statement.executeUpdate(createTableString);
			createTableString = new CreateTableQuery(ProductsCatalogLocationsTable.table, true).validate() + "";
			statement.executeUpdate(createTableString);
			createTableString = new CreateTableQuery(ProductsPackagesTable.table, true).validate() + "";
			statement.executeUpdate(createTableString);
			createTableString = new CreateTableQuery(GroceriesListsTable.table, true).validate() + "";
			statement.executeUpdate(createTableString);
			createTableString = new CreateTableQuery(GroceriesListsHistoryTable.table, true).validate() + "";
			statement.executeUpdate(createTableString);
			createTableString = new CreateTableQuery(CartsListTable.table, true).validate() + "";
			statement.executeUpdate(createTableString);
			createTableString = new CreateTableQuery(WorkersTable.table, true).validate() + "";
			statement.executeUpdate(createTableString);
			createTableString = new CreateTableQuery(FreeIDsTable.table, true).validate() + "";
			statement.executeUpdate(createTableString);

		} catch (SQLException e) {

			e.printStackTrace();

		}

	}

	/**
	 * The method checks if SQL database is exist on local drive.
	 * 
	 * @return true if exist, false otherwise.
	 */
	private boolean isDatabaseExists() {

		// try to connect to database with "only-if-exist" parameter
		try {
			DriverManager.getConnection("jdbc:hsqldb:file:" + DATABASE_PATH_PARAMS + ";ifexists=true", "SA", "");
		} catch (SQLException e) {
			// if exception thrown - database not exists
			return false;
		}

		// else - database exists
		return true;
	}

	/**
	 * return the number of rows in specific ResultSet the cursor of the given
	 * reultset will point the beforeFirst row after that method
	 * 
	 * @param s
	 * @return the number of rows
	 */

	private static int getResultSetRowCount(ResultSet s) {
		if (s == null)
			return 0;
		try {
			s.last();
			return s.getRow();
		} catch (SQLException exp) {
			exp.printStackTrace();
		} finally {
			try {
				s.beforeFirst();
			} catch (SQLException exp) {
				exp.printStackTrace();
			}
		}
		return 0;
	}

	/**
	 * return if there any result in ResultsSet the cursor of the given reultset
	 * will point the beforeFirst row after that method
	 * 
	 * @param ¢
	 * @return false - if there are rows in the ResultsSet, true otherwise.
	 * @throws CriticalError
	 */
	private static boolean isResultSetEmpty(ResultSet ¢) throws CriticalError {
		boolean $;
		try {
			$ = ¢.first();
			¢.beforeFirst();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new CriticalError();
		}
		return !$;
	}

	private int generateSessionID(boolean forWorker) throws CriticalError, NumberOfConnectionsExceeded {

		int minVal, maxVal;
		int $;

		if (!forWorker) {
			minVal = 1;
			maxVal = SESSION_IDS_BEGIN - 1;
		} else {
			minVal = SESSION_IDS_BEGIN;
			maxVal = Integer.MAX_VALUE;
		}

		// trying to find available random session id.
		for (int i = 0; i < TRYS_NUMBER; ++i) {
			// generate number between mivVal to maxVal (include)
			$ = new Random().nextInt(maxVal - minVal) + minVal;

			String query = (forWorker
					? new SelectQuery().addAllTableColumns(WorkersTable.table)
							.addCondition(BinaryCondition.equalTo(WorkersTable.sessionIDCol, PARAM_MARK))
					: new SelectQuery().addAllTableColumns(CartsListTable.table)
							.addCondition(BinaryCondition.equalTo(CartsListTable.cartIDCol, PARAM_MARK))
							.addCondition(UnaryCondition.isNotNull(CartsListTable.listIDCol))).validate()
					+ "";

			PreparedStatement statement = getParameterizedQuery(query, $);
			ResultSet result;

			try {
				result = statement.executeQuery();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new CriticalError();
			}

			if (isResultSetEmpty(result))
				return $;
		}

		throw new NumberOfConnectionsExceeded();

	}

	/**
	 * Allocate new ID for new row.
	 * 
	 * @param t
	 *            The table you want to insert new row in it
	 * @param c
	 *            The ID column of that table
	 * @return
	 * @throws CriticalError
	 */
	private int allocateIDToTable(DbTable t, DbColumn c) throws CriticalError {

		// search for free id
		String selectId = generateSelectQuery1Table(FreeIDsTable.table,
				BinaryCondition.equalTo(FreeIDsTable.fromTableNameCol, PARAM_MARK));

		PreparedStatement statement = null;
		ResultSet result = null;
		ResultSet maxIDResult = null;
		try {
			statement = getParameterizedReadQuery(selectId, t.getName());

			result = statement.executeQuery();
			int retID;

			if (!isResultSetEmpty(result)) {
				// return id from free_ids_table
				result.first();
				retID = result.getInt(FreeIDsTable.IDCol.getColumnNameSQL());

				// delete that id
				getParameterizedQuery(generateDeleteQuery(FreeIDsTable.table,
						BinaryCondition.equalTo(FreeIDsTable.fromTableNameCol, PARAM_MARK),
						BinaryCondition.equalTo(FreeIDsTable.IDCol, PARAM_MARK)), t.getName(), retID).executeUpdate();
			} else {
				// find max id and return the next number
				String maxIDQuery = new SelectQuery().addCustomColumns(FunctionCall.max().addColumnParams(c)).validate()
						+ "";

				maxIDResult = getParameterizedReadQuery(maxIDQuery, t.getName()).executeQuery();

				// if the table is empty - return 1
				if (isResultSetEmpty(result))
					retID = 1;
				else {
					maxIDResult.first();
					retID = maxIDResult.getInt(1) + 1;
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new CriticalError();
		} finally {
			closeResources(statement, result, maxIDResult);
		}

		return 0;

	}

	/**
	 * Free ID when removing row.
	 * 
	 * @param t
	 *            The table you want to remove row from it.
	 * @param col
	 *            The ID column of that table
	 * @return
	 * @throws CriticalError
	 */
	private void freeIDOfTable(DbTable t, Integer idToFree) throws CriticalError {
		String insertQuery = new InsertQuery(FreeIDsTable.table).addColumn(FreeIDsTable.fromTableNameCol, PARAM_MARK)
				.addColumn(FreeIDsTable.IDCol, PARAM_MARK).validate() + "";

		PreparedStatement statement = getParameterizedQuery(insertQuery, t.getName(), idToFree);

		try {
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new CriticalError();
		} finally {
			closeResources(statement);
		}

	}

	/**
	 * Validate if the worker login the system
	 * 
	 * @param sessionID
	 *            - sessionID of the worker
	 * @throws ClientNotConnected
	 * @throws CriticalError
	 */
	private void validateSessionEstablished(Integer sessionID) throws ClientNotConnected, CriticalError {
		try {
			if (!isSessionEstablished(sessionID))
				throw new SQLDatabaseException.ClientNotConnected();
		} catch (ValidationException e) {
			e.printStackTrace();
			throw new CriticalError();
		}
	}

	/**
	 * Check if the worker or cart logged in the system
	 * 
	 * @param sessionID
	 *            sessionID of the worker
	 * @return true - if connected
	 * @throws CriticalError
	 */
	private boolean isSessionEstablished(Integer sessionID) throws CriticalError {

		try {
			return (sessionID == null)
					|| !isResultSetEmpty(getParameterizedQuery(generateSelectQuery1Table(WorkersTable.table,
							BinaryCondition.equalTo(WorkersTable.sessionIDCol, PARAM_MARK)), sessionID).executeQuery());
		} catch (SQLException e) {
			e.printStackTrace();
			throw new CriticalError();
		}
	}

	/**
	 * Check if the worker logged in the system
	 * 
	 * @param username
	 *            username of the worker
	 * @return true - if connected
	 * @throws CriticalError
	 */
	private boolean isSessionEstablished(String username) throws CriticalError {

		if (username == null)
			return true;

		ResultSet result = null;
		try {
			result = getParameterizedQuery(generateSelectQuery1Table(WorkersTable.table,
					BinaryCondition.equalTo(WorkersTable.workerUsernameCol, PARAM_MARK)), username).executeQuery();

			if (isResultSetEmpty(result))
				return false;

			result.first();
			return result.getObject(WorkersTable.sessionIDCol.getName()) != null;

		} catch (SQLException e) {
			e.printStackTrace();
			throw new CriticalError();
		} finally {
			closeResources(result);
		}

	}

	private String dateToString(LocalDate ¢) {
		return ¢.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	}
	/**
	 * get parameterized query for execution.
	 * 
	 * @param query
	 *            - the query with parameter marks
	 * @param parameters
	 *            - the parameters to insert into the marks
	 * @return PreparedStatement of the parameterized query.
	 * @throws CriticalError
	 */
	private PreparedStatement getParameterizedQuery(String query, Object... parameters) throws CriticalError {

		query = query.replace(QUATED_PARAM_MARK, SQL_PARAM);

		PreparedStatement $;
		try {
			$ = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new CriticalError();
		}

		for (int ¢ = 0; ¢ < parameters.length; ++¢)
			try {
				$.setObject(¢ + 1, parameters[¢]);
			} catch (SQLException e) {
				e.printStackTrace();
				throw new CriticalError();
			}

		return $;

	}

	/**
	 * create parameterized read-only query for execution. usually used by
	 * SELECT queries.
	 * 
	 * @param query
	 *            - the query with parameter marks
	 * @param parameters
	 *            - the parameters to insert into the marks
	 * @return PreparedStatement of the parameterized query.
	 * @throws CriticalError
	 */
	private PreparedStatement getParameterizedReadQuery(String query, Object... parameters) throws CriticalError {

		query = query.replace(QUATED_PARAM_MARK, SQL_PARAM);

		PreparedStatement $;
		try {
			$ = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new CriticalError();
		}

		for (int ¢ = 0; ¢ < parameters.length; ++¢)
			try {
				$.setObject(¢ + 1, parameters[¢]);
			} catch (SQLException e) {
				e.printStackTrace();
				throw new CriticalError();
			}

		return $;

	}

	private void setNewAmountForStore(ProductPackage p, String placeCol, int oldAmount, int newAmount)
			throws ProductPackageAmountNotMatch, CriticalError {

		// case: not enough amount
		if (newAmount < 0)
			throw new ProductPackageAmountNotMatch();
		PreparedStatement statement = null;
		try {
			// case: add new row
			if (oldAmount == 0) {
				String insertQuery = new InsertQuery(ProductsPackagesTable.table)
						.addColumn(ProductsPackagesTable.barcodeCol, PARAM_MARK)
						.addColumn(ProductsPackagesTable.expirationDateCol, dateToString(p.getSmartCode().getExpirationDate()))
						.addColumn(ProductsPackagesTable.placeInStoreCol, PARAM_MARK)
						.addColumn(ProductsPackagesTable.amountCol, PARAM_MARK).validate() + "";

				insertQuery.hashCode();

				
				log.info("set amount to package: " + p.getSmartCode().getBarcode() + ", " + dateToString(p.getSmartCode().getExpirationDate()) + ", " + placeCol);


				
				statement = getParameterizedQuery(insertQuery, p.getSmartCode().getBarcode(),
						placeCol, newAmount);

			} else if (newAmount == 0) { // case: remove row
				String deleteQuery = generateDeleteQuery(ProductsPackagesTable.table,
						BinaryCondition.equalTo(ProductsPackagesTable.barcodeCol, PARAM_MARK),
						BinaryCondition.equalTo(ProductsPackagesTable.placeInStoreCol, PARAM_MARK),
						BinaryCondition.equalTo(ProductsPackagesTable.expirationDateCol,
								JdbcEscape.date(Date.from(p.getSmartCode().getExpirationDate()
										.atStartOfDay(ZoneId.systemDefault()).toInstant()))));

				deleteQuery.hashCode();

				statement = getParameterizedQuery(deleteQuery, p.getSmartCode().getBarcode(), placeCol);

			} else { // case: update amount to new value
				UpdateQuery updateQuery = generateUpdateQuery(ProductsPackagesTable.table,
						BinaryCondition.equalTo(ProductsPackagesTable.barcodeCol, PARAM_MARK),
						BinaryCondition.equalTo(ProductsPackagesTable.placeInStoreCol, PARAM_MARK),
						BinaryCondition.equalTo(ProductsPackagesTable.expirationDateCol,
								JdbcEscape.date(Date.from(p.getSmartCode().getExpirationDate()
										.atStartOfDay(ZoneId.systemDefault()).toInstant()))));

				updateQuery.addSetClause(ProductsPackagesTable.amountCol, newAmount).validate();

				statement = getParameterizedQuery(updateQuery + "", p.getSmartCode().getBarcode(), placeCol);
			}

			log.info("setNewAmountForStore : run query: " + statement);
			statement.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new CriticalError();
		} finally {
			closeResources(statement);
		}
	}

	private void setNewAmountForCart(ProductPackage p, Integer listID, int oldAmount, int newAmount)
			throws ProductPackageAmountNotMatch, CriticalError {

		// case: not enough amount
		if (newAmount < 0)
			throw new ProductPackageAmountNotMatch();

		PreparedStatement statement = null;
		try {
			// case: add new row
			if (oldAmount == 0) {
				String insertQuery = new InsertQuery(GroceriesListsTable.table)
						.addColumn(GroceriesListsTable.barcodeCol, PARAM_MARK)
						.addColumn(GroceriesListsTable.expirationDateCol, PARAM_MARK)
						.addColumn(GroceriesListsTable.listIDCol, PARAM_MARK)
						.addColumn(GroceriesListsTable.amountCol, PARAM_MARK).validate() + "";

				insertQuery.hashCode();

				statement = getParameterizedQuery(insertQuery, p.getSmartCode().getBarcode(),
						p.getSmartCode().getExpirationDate(), listID, newAmount);

			} else if (newAmount == 0) { // case: remove row
				String deleteQuery = generateDeleteQuery(GroceriesListsTable.table,
						BinaryCondition.equalTo(GroceriesListsTable.barcodeCol, PARAM_MARK),
						BinaryCondition.equalTo(GroceriesListsTable.listIDCol, PARAM_MARK),
						BinaryCondition.equalTo(GroceriesListsTable.expirationDateCol,
								JdbcEscape.date(Date.from(p.getSmartCode().getExpirationDate()
										.atStartOfDay(ZoneId.systemDefault()).toInstant()))));

				deleteQuery.hashCode();

				statement = getParameterizedQuery(deleteQuery, p.getSmartCode().getBarcode(), listID);

			} else { // case: update amount to new value
				UpdateQuery updateQuery = generateUpdateQuery(GroceriesListsTable.table,
						BinaryCondition.equalTo(GroceriesListsTable.barcodeCol, PARAM_MARK),
						BinaryCondition.equalTo(GroceriesListsTable.listIDCol, PARAM_MARK),
						BinaryCondition.equalTo(GroceriesListsTable.expirationDateCol,
								JdbcEscape.date(Date.from(p.getSmartCode().getExpirationDate()
										.atStartOfDay(ZoneId.systemDefault()).toInstant()))));

				updateQuery.addSetClause(ProductsPackagesTable.amountCol, newAmount).validate();

				statement = getParameterizedQuery(updateQuery + "", p.getSmartCode().getBarcode(), listID);
			}

			statement.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new CriticalError();
		} finally {
			closeResources(statement);
		}
	}

	private void addCatalogProduct(CatalogProduct p) throws CriticalError, SQLException {

		// add all ingredients of product
		for (Ingredient ¢ : p.getIngredients()) {
			String insertToProductQuery = new InsertQuery(ProductsCatalogIngredientsTable.table)
					.addColumn(ProductsCatalogIngredientsTable.barcodeCol, PARAM_MARK)
					.addColumn(ProductsCatalogIngredientsTable.ingredientIDCol, PARAM_MARK).validate() + "";

			insertToProductQuery.hashCode();

			PreparedStatement statement = getParameterizedQuery(insertToProductQuery, p.getBarcode(), ¢.getId());

			statement.executeUpdate();

			closeResources(statement);

		}

		// add all locations of product
		for (Location ¢ : p.getLocations()) {
			int newID = allocateIDToTable(LocationsTable.table, LocationsTable.locationIDCol);
			String insertLocationQuery = new InsertQuery(LocationsTable.table)
					.addColumn(LocationsTable.locationIDCol, PARAM_MARK)
					.addColumn(LocationsTable.placeInStoreCol, PARAM_MARK)
					.addColumn(LocationsTable.pointXCol, PARAM_MARK).addColumn(LocationsTable.pointYCol, PARAM_MARK)
					.validate() + "";

			insertLocationQuery.hashCode();

			PreparedStatement insertLocationStatement = getParameterizedQuery(
					insertLocationQuery, newID, ¢.getPlaceInMarket().equals(PlaceInMarket.STORE)
							? LOCATIONS_TABLE.VALUE_PLACE_STORE : LOCATIONS_TABLE.VALUE_PLACE_WAREHOUSE,
					¢.getX(), ¢.getY());

			String insertToProductQuery = new InsertQuery(ProductsCatalogLocationsTable.table)
					.addColumn(ProductsCatalogLocationsTable.barcodeCol, PARAM_MARK)
					.addColumn(ProductsCatalogLocationsTable.locationIDCol, PARAM_MARK).validate() + "";

			PreparedStatement statement = getParameterizedQuery(insertToProductQuery, p.getBarcode(), newID);

			insertLocationStatement.executeUpdate();
			statement.executeUpdate();

			closeResources(insertLocationStatement);
			closeResources(statement);
		}

		// add the product itself
		String insertQuery = new InsertQuery(ProductsCatalogTable.table)
				.addColumn(ProductsCatalogTable.barcodeCol, PARAM_MARK)
				.addColumn(ProductsCatalogTable.manufacturerIDCol, PARAM_MARK)
				.addColumn(ProductsCatalogTable.productDescriptionCol, PARAM_MARK)
				.addColumn(ProductsCatalogTable.productNameCol, PARAM_MARK)
				.addColumn(ProductsCatalogTable.productPictureCol, PARAM_MARK)
				.addColumn(ProductsCatalogTable.productPriceCol, PARAM_MARK).validate() + "";

		PreparedStatement statement = getParameterizedQuery(insertQuery, p.getBarcode(), p.getManufacturer().getId(),
				p.getDescription(), p.getName(), p.getImageUrl(), p.getPrice());

		statement.executeUpdate();

		closeResources(statement);

	}

	private void removeCatalogProduct(SmartCode p) throws CriticalError, SQLException {

		// remove all ingredients of product
		PreparedStatement statement = getParameterizedQuery(
				generateDeleteQuery(ProductsCatalogIngredientsTable.table,
						BinaryCondition.equalTo(ProductsCatalogIngredientsTable.barcodeCol, PARAM_MARK)),
				p.getBarcode());
		statement.executeUpdate();
		closeResources(statement);

		// remove all locations of product
		String selectAllLocationsQuery = new SelectQuery().addColumns(ProductsCatalogLocationsTable.locationIDCol)
				.addCondition(BinaryCondition.equalTo(ProductsCatalogLocationsTable.barcodeCol, PARAM_MARK)).validate()
				+ "";
		String deleteLocationsQuery = new DeleteQuery(LocationsTable.table)
				.addCondition(new CustomCondition(
						LocationsTable.locationIDCol.getColumnNameSQL() + " IN (" + selectAllLocationsQuery + " ) "))
				.validate() + "";

		PreparedStatement LocationsStatement = getParameterizedQuery(deleteLocationsQuery, p.getBarcode());
		LocationsStatement.executeUpdate();
		closeResources(LocationsStatement);

		// remove barcode form ProductsLocations Table
		PreparedStatement productLocationsStatement = getParameterizedQuery(
				generateDeleteQuery(ProductsCatalogLocationsTable.table,
						BinaryCondition.equalTo(ProductsCatalogLocationsTable.barcodeCol, PARAM_MARK)),
				p.getBarcode());
		productLocationsStatement.executeUpdate();
		closeResources(productLocationsStatement);

		// remove product itself
		PreparedStatement productStatement = getParameterizedQuery(generateDeleteQuery(ProductsCatalogTable.table,
				BinaryCondition.equalTo(ProductsCatalogTable.barcodeCol, PARAM_MARK)), p.getBarcode());
		productStatement.executeUpdate();
		closeResources(productStatement);

	}

	private int getAmountForStore(ProductPackage p, String placeCol) throws CriticalError {
		String selectQuery = generateSelectQuery1Table(ProductsPackagesTable.table,
				BinaryCondition.equalTo(ProductsPackagesTable.barcodeCol, PARAM_MARK),
				BinaryCondition.equalTo(ProductsPackagesTable.placeInStoreCol, PARAM_MARK),
				BinaryCondition.equalTo(ProductsPackagesTable.expirationDateCol, JdbcEscape.date(Date
						.from(p.getSmartCode().getExpirationDate().atStartOfDay(ZoneId.systemDefault()).toInstant()))));

		PreparedStatement statement = getParameterizedReadQuery(selectQuery, p.getSmartCode().getBarcode(), placeCol);

		log.info("getAmountForStore: execute query: " + statement);
		
		ResultSet result = null;
		try {
			result = statement.executeQuery();

			if (isResultSetEmpty(result))
				return 0;

			result.first();

			return result.getInt(ProductsPackagesTable.amountCol.getColumnNameSQL());

		} catch (SQLException e) {
			e.printStackTrace();
			throw new CriticalError();
		} finally {
			closeResources(statement, result);
		}

	}

	private int getAmountForCart(ProductPackage p, Integer listID) throws CriticalError, ProductPackageNotExist {

		String selectQuery = generateSelectQuery1Table(GroceriesListsTable.table,
				BinaryCondition.equalTo(GroceriesListsTable.barcodeCol, PARAM_MARK),
				BinaryCondition.equalTo(GroceriesListsTable.listIDCol, PARAM_MARK),
				BinaryCondition.equalTo(GroceriesListsTable.expirationDateCol, JdbcEscape.date(Date
						.from(p.getSmartCode().getExpirationDate().atStartOfDay(ZoneId.systemDefault()).toInstant()))));

		log.info("execute query: " + selectQuery);
		
		PreparedStatement statement = getParameterizedReadQuery(selectQuery, p.getSmartCode().getBarcode(), listID);

		ResultSet result = null;
		try {
			result = statement.executeQuery();

			if (isResultSetEmpty(result))
				return 0;

			result.first();

			return result.getInt(GroceriesListsTable.amountCol.getColumnNameSQL());

		} catch (SQLException e) {
			e.printStackTrace();
			throw new CriticalError();
		} finally {
			closeResources(statement, result);
		}

	}

	/**
	 * 
	 * assuming cart already connected
	 * 
	 * @param cartId
	 * @return
	 * @throws CriticalError
	 */
	private int getCartListId(int cartId) throws CriticalError {

		String selectQuery = generateSelectQuery1Table(CartsListTable.table,
				BinaryCondition.equalTo(CartsListTable.cartIDCol, PARAM_MARK));

		PreparedStatement statement = getParameterizedReadQuery(selectQuery, cartId);

		ResultSet result = null;

		try {
			result = statement.executeQuery();

			result.first();
			return result.getInt(CartsListTable.listIDCol.getColumnNameSQL());

		} catch (SQLException e) {
			e.printStackTrace();
			throw new CriticalError();
		} finally {
			closeResources(statement, result);
		}

	}

	private void moveProductPackage(int sessionId, LOCATIONS_TYPES from, LOCATIONS_TYPES to,
			ProductPackage packageToMove, int amount)
			throws CriticalError, ProductPackageAmountNotMatch, ProductPackageNotExist {
		if (from != null)
			switch (from) {
			case CART: {
				int listID = getCartListId(sessionId);
				int currentAmount = getAmountForCart(packageToMove, listID);
				if (currentAmount == 0)
					throw new ProductPackageNotExist();
				setNewAmountForCart(packageToMove, listID, currentAmount, currentAmount - amount);
				break;
			}
			case WAREHOUSE: {
				int currentAmount = getAmountForStore(packageToMove, PRODUCTS_PACKAGES_TABLE.VALUE_PLACE_WAREHOUSE);
				if (currentAmount == 0)
					throw new ProductPackageNotExist();
				setNewAmountForStore(packageToMove, PRODUCTS_PACKAGES_TABLE.VALUE_PLACE_WAREHOUSE, currentAmount,
						currentAmount - amount);
				break;
			}
			case STORE: {
				int currentAmount = getAmountForStore(packageToMove, PRODUCTS_PACKAGES_TABLE.VALUE_PLACE_STORE);
				if (currentAmount == 0)
					throw new ProductPackageNotExist();
				setNewAmountForStore(packageToMove, PRODUCTS_PACKAGES_TABLE.VALUE_PLACE_STORE, currentAmount,
						currentAmount - amount);
				break;
			}
			}

		if (to != null)
			switch (to) {
			case CART: {
				int listID = getCartListId(sessionId);
				int currentAmount = getAmountForCart(packageToMove, listID);
				setNewAmountForCart(packageToMove, listID, currentAmount, currentAmount + amount);
				break;
			}
			case WAREHOUSE: {
				int currentAmount = getAmountForStore(packageToMove, PRODUCTS_PACKAGES_TABLE.VALUE_PLACE_WAREHOUSE);
				setNewAmountForStore(packageToMove, PRODUCTS_PACKAGES_TABLE.VALUE_PLACE_WAREHOUSE, currentAmount,
						currentAmount + amount);
				break;
			}
			case STORE: {
				int currentAmount = getAmountForStore(packageToMove, PRODUCTS_PACKAGES_TABLE.VALUE_PLACE_STORE);
				setNewAmountForStore(packageToMove, PRODUCTS_PACKAGES_TABLE.VALUE_PLACE_STORE, currentAmount,
						currentAmount + amount);
				break;
			}
			}
	}

	private boolean isSuchRowExist(DbTable t, DbColumn c, Object value) throws CriticalError {
		String prodctsTableQuery = generateSelectQuery1Table(t, BinaryCondition.equalTo(c, PARAM_MARK));

		PreparedStatement productStatement = getParameterizedReadQuery(prodctsTableQuery, value);

		ResultSet productResult = null;
		try {
			productResult = productStatement.executeQuery();
			return !isResultSetEmpty(productResult);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResources(productStatement, productResult);
		}

		// if somehow we got here - bad and throw exception
		throw new CriticalError();
	}

	private boolean isProductExistInCatalog(Long barcode) throws CriticalError {
		return isSuchRowExist(ProductsCatalogTable.table, ProductsCatalogTable.barcodeCol, barcode);
	}

	private boolean isManufacturerExist(Integer manufacturerID) throws CriticalError {
		return isSuchRowExist(ManufacturerTable.table, ManufacturerTable.manufacturerIDCol, manufacturerID);
	}

	private boolean isIngredientExist(Integer ingredientID) throws CriticalError {
		return isSuchRowExist(IngredientsTable.table, IngredientsTable.ingredientIDCol, ingredientID);
	}

	/**
	 * close opened resources.
	 * 
	 * @param resources
	 *            - list of resources to close.
	 */
	void closeResources(AutoCloseable... resources) {
		for (AutoCloseable resource : resources)
			if (resource != null)
				try {
					resource.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
	}

	/*
	 * #####################################################################
	 * 
	 * Public Methods
	 * 
	 * ####################################################################
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see SQLDatabase.ISQLDatabaseConnection#WorkerLogin(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public int workerLogin(String username, String password)
			throws AuthenticationError, ClientAlreadyConnected, CriticalError, NumberOfConnectionsExceeded {
		String query = generateSelectQuery1Table(WorkersTable.table,
				BinaryCondition.equalTo(WorkersTable.workerUsernameCol, PARAM_MARK),
				BinaryCondition.equalTo(WorkersTable.workerPasswordCol, PARAM_MARK));

		PreparedStatement statement = getParameterizedQuery(query, username, password);

		ResultSet result;
		try {
			result = statement.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new CriticalError();
		}

		// check if no results or more than one - throw exception user not exist
		if (getResultSetRowCount(result) != 1)
			throw new SQLDatabaseException.AuthenticationError();

		// check if worker already connected
		if (isSessionEstablished(username))
			throw new SQLDatabaseException.ClientAlreadyConnected();

		/*
		 * EVERYTHING OK - initiate new session to worker
		 */
		int $ = generateSessionID(true);

		UpdateQuery updateQuery = generateUpdateQuery(WorkersTable.table,
				BinaryCondition.equalTo(WorkersTable.workerUsernameCol, PARAM_MARK),
				BinaryCondition.equalTo(WorkersTable.workerPasswordCol, PARAM_MARK));

		updateQuery.addSetClause(WorkersTable.sessionIDCol, $).validate();

		statement = getParameterizedQuery(updateQuery + "", username, password);

		try {
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new CriticalError();
		}

		return $;
	}
	
	@Override
	public String getClientType(Integer sessionID) throws ClientNotConnected, CriticalError {
		validateSessionEstablished(sessionID);
		
		String query = generateSelectQuery1Table(WorkersTable.table,
				BinaryCondition.equalTo(WorkersTable.sessionIDCol, PARAM_MARK));

		PreparedStatement statement = getParameterizedReadQuery(query, sessionID);
		
		log.debug("execute query: " + statement);
		
		ResultSet result = null;
		try {
			result = statement.executeQuery();
			result.first();
			
			int clientType = result.getInt(WorkersTable.workerPrivilegesCol.getColumnNameSQL());
			log.debug("worker type code from SQL: " + clientType);
			
			return new Gson().toJson(clientType != WORKERS_TABLE.VALUE_PRIVILEGE_MANAGER ? CLIENT_TYPE.WORKER : CLIENT_TYPE.MANAGER);
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new CriticalError();
		} finally {
			closeResources(statement, result);
		}
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see SQLDatabase.ISQLDatabaseConnection#WorkerLogout(java.lang.Integer,
	 * java.lang.String)
	 */
	@Override
	public void workerLogout(Integer sessionID, String username) throws ClientNotConnected, CriticalError {

		validateSessionEstablished(sessionID);

		String query = generateSelectQuery1Table(WorkersTable.table,
				BinaryCondition.equalTo(WorkersTable.workerUsernameCol, PARAM_MARK),
				BinaryCondition.equalTo(WorkersTable.sessionIDCol, PARAM_MARK));

		PreparedStatement statement = getParameterizedQuery(query, username, sessionID);

		ResultSet result;
		try {
			result = statement.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new CriticalError();
		}

		// check if no results or more than one - throw exception
		if (getResultSetRowCount(result) != 1)
			throw new SQLDatabaseException.CriticalError();

		/*
		 * EVERYTHING OK - disconnect worker
		 */
		UpdateQuery updateQuery = generateUpdateQuery(WorkersTable.table,
				BinaryCondition.equalTo(WorkersTable.workerUsernameCol, PARAM_MARK));

		updateQuery.addSetClause(WorkersTable.sessionIDCol, null).validate();

		statement = getParameterizedQuery(updateQuery + "", username);

		try {
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new CriticalError();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see SQLDatabase.ISQLDatabaseConnection#getProductFromCatalog(java.lang.
	 * Integer, long)
	 */
	@Override
	public String getProductFromCatalog(Integer sessionID, long barcode)
			throws ProductNotExistInCatalog, ClientNotConnected, CriticalError {

		validateSessionEstablished(sessionID);

		String prodctsIngredientsQuery = generateSelectLeftJoinWithQuery2Tables(ProductsCatalogIngredientsTable.table,
				IngredientsTable.table, IngredientsTable.ingredientIDCol, ProductsCatalogIngredientsTable.barcodeCol,
				BinaryCondition.equalTo(ProductsCatalogIngredientsTable.barcodeCol, PARAM_MARK));

		String prodctsLocationsQuery = generateSelectLeftJoinWithQuery2Tables(ProductsCatalogLocationsTable.table,
				LocationsTable.table, LocationsTable.locationIDCol, ProductsCatalogLocationsTable.barcodeCol,
				BinaryCondition.equalTo(ProductsCatalogLocationsTable.barcodeCol, PARAM_MARK));

		String prodctsTableQuery = generateSelectLeftJoinWithQuery2Tables(ProductsCatalogTable.table,
				ManufacturerTable.table, ManufacturerTable.manufacturerIDCol, ProductsCatalogTable.barcodeCol,
				BinaryCondition.equalTo(ProductsCatalogTable.barcodeCol, PARAM_MARK));

		PreparedStatement productStatement = getParameterizedReadQuery(prodctsTableQuery, Long.valueOf(barcode));
		PreparedStatement productIngredientsStatement = getParameterizedReadQuery(prodctsIngredientsQuery,
				Long.valueOf(barcode));
		PreparedStatement productLocationsStatement = getParameterizedReadQuery(prodctsLocationsQuery,
				Long.valueOf(barcode));

		ResultSet productResult = null;
		ResultSet ingredientResult = null;
		ResultSet locationsResult = null;

		try {
			// START transaction
			connection.setAutoCommit(false);
			productResult = productStatement.executeQuery();
			ingredientResult = productIngredientsStatement.executeQuery();
			locationsResult = productLocationsStatement.executeQuery();

			// END transaction
			connection.commit();
			connection.setAutoCommit(true);
			// if no result - throw exception
			if (isResultSetEmpty(productResult))
				throw new SQLDatabaseException.ProductNotExistInCatalog();

			productResult.next();
			ingredientResult.next();
			locationsResult.next();
			return SQLJsonGenerator.ProductToJson(productResult, ingredientResult, locationsResult);

		} catch (SQLException e) {
			e.printStackTrace();
			throw new CriticalError();
		} finally {
			closeResources(productResult, ingredientResult, locationsResult);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * SQLDatabase.ISQLDatabaseConnection#AddProductPackageToWarehouse(java.lang
	 * .Integer, BasicCommonClasses.ProductPackage)
	 */
	@Override
	public void addProductPackageToWarehouse(Integer sessionID, ProductPackage p)
			throws CriticalError, ClientNotConnected, ProductNotExistInCatalog {
		validateSessionEstablished(sessionID);

		try {
			// START transaction
			connection.setAutoCommit(false);
			if (!isProductExistInCatalog(p.getSmartCode().getBarcode()))
				throw new ProductNotExistInCatalog();

			moveProductPackage(sessionID, null, LOCATIONS_TYPES.WAREHOUSE, p, p.getAmount());

			// END transaction
			connection.commit();
			connection.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new CriticalError();
		} catch (ProductPackageAmountNotMatch | ProductPackageNotExist e) {
			e.printStackTrace();
			throw new CriticalError();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * SQLDatabase.ISQLDatabaseConnection#RemoveProductPackageToWarehouse(java.
	 * lang.Integer, BasicCommonClasses.ProductPackage)
	 */
	@Override
	public void removeProductPackageFromWarehouse(Integer sessionID, ProductPackage p) throws CriticalError,
			ClientNotConnected, ProductNotExistInCatalog, ProductPackageAmountNotMatch, ProductPackageNotExist {

		validateSessionEstablished(sessionID);

		try {
			// START transaction
			connection.setAutoCommit(false);
			if (!isProductExistInCatalog(p.getSmartCode().getBarcode()))
				throw new ProductNotExistInCatalog();

			moveProductPackage(sessionID, LOCATIONS_TYPES.WAREHOUSE, null, p, p.getAmount());
			// END transaction
			connection.commit();
			connection.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new CriticalError();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * SQLDatabase.ISQLDatabaseConnection#AddProductToCatalog(java.lang.Integer,
	 * BasicCommonClasses.CatalogProduct)
	 */
	@Override
	public void addProductToCatalog(Integer sessionID, CatalogProduct productToAdd) throws CriticalError,
			ClientNotConnected, ProductAlreadyExistInCatalog, IngredientNotExist, ManufacturerNotExist {

		validateSessionEstablished(sessionID);

		try {
			// START transaction
			connection.setAutoCommit(false);
			// TODO add savepoint

			if (isProductExistInCatalog(productToAdd.getBarcode()))
				throw new ProductAlreadyExistInCatalog();

			// check if manufacturer exist
			if (isManufacturerExist((int) productToAdd.getManufacturer().getId()))
				throw new ManufacturerNotExist();

			// check if all ingredients exists
			for (Ingredient ¢ : productToAdd.getIngredients())
				if (!isIngredientExist((int) ¢.getId()))
					throw new IngredientNotExist();

			addCatalogProduct(productToAdd);

			// END transaction
			connection.commit();
			connection.setAutoCommit(true);
		} catch (SQLException e) {
			// TODO rollback
			e.printStackTrace();
			throw new CriticalError();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * SQLDatabase.ISQLDatabaseConnection#RemoveProductFromCatalog(java.lang.
	 * Integer, BasicCommonClasses.CatalogProduct)
	 */
	@Override
	public void removeProductFromCatalog(Integer sessionID, SmartCode productToRemove)
			throws CriticalError, ClientNotConnected, ProductNotExistInCatalog, ProductStillForSale {

		validateSessionEstablished(sessionID);

		try {
			// START transaction
			connection.setAutoCommit(false);
			// TODO add savepoint

			if (!isProductExistInCatalog(productToRemove.getBarcode()))
				throw new ProductNotExistInCatalog();

			// TODO check if the product is in the system

			removeCatalogProduct(productToRemove);

			// END transaction
			connection.commit();
			connection.setAutoCommit(true);
		} catch (SQLException e) {
			// TODO rollback
			e.printStackTrace();
			throw new CriticalError();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * SQLDatabase.ISQLDatabaseConnection#HardRemoveProductFromCatalog(java.lang
	 * .Integer, BasicCommonClasses.CatalogProduct)
	 */
	@Override
	public void hardRemoveProductFromCatalog(Integer sessionID, CatalogProduct productToRemove)
			throws CriticalError, ClientNotConnected, ProductNotExistInCatalog {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see SQLDatabase.ISQLDatabaseConnection#UpdateProductInCatalog(java.lang.
	 * Integer, java.lang.Long, BasicCommonClasses.CatalogProduct)
	 */
	@Override
	public void editProductInCatalog(Integer sessionID, CatalogProduct productToUpdate) throws CriticalError,
			ClientNotConnected, ProductNotExistInCatalog, IngredientNotExist, ManufacturerNotExist {
		validateSessionEstablished(sessionID);

		try {
			// START transaction
			connection.setAutoCommit(false);
			// TODO add savepoint

			if (!isProductExistInCatalog(productToUpdate.getBarcode()))
				throw new ProductNotExistInCatalog();

			// check if manufacturer exist
			if (isManufacturerExist((int) productToUpdate.getManufacturer().getId()))
				throw new ManufacturerNotExist();

			// check if all ingredients exists
			for (Ingredient ¢ : productToUpdate.getIngredients())
				if (!isIngredientExist((int) ¢.getId()))
					throw new IngredientNotExist();

			// do update = remove product and adds it again
			removeCatalogProduct(new SmartCode(productToUpdate.getBarcode(), null));
			addCatalogProduct(productToUpdate);

			// END transaction
			connection.commit();
			connection.setAutoCommit(true);
		} catch (SQLException e) {
			// TODO rollback
			e.printStackTrace();
			throw new CriticalError();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * SQLDatabase.ISQLDatabaseConnection#AddProductToGroceryList(java.lang.
	 * Integer, BasicCommonClasses.ProductPackage)
	 */
	@Override
	public void addProductToGroceryList(Integer cartID, ProductPackage productToBuy) throws CriticalError,
			ClientNotConnected, ProductNotExistInCatalog, ProductPackageAmountNotMatch, ProductPackageNotExist {

		try {
			// START transaction
			connection.setAutoCommit(false);
			if (!isProductExistInCatalog(productToBuy.getSmartCode().getBarcode()))
				throw new ProductNotExistInCatalog();

			moveProductPackage(cartID, LOCATIONS_TYPES.STORE, LOCATIONS_TYPES.CART, productToBuy,
					productToBuy.getAmount());
			// END transaction
			connection.commit();
			connection.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new CriticalError();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * SQLDatabase.ISQLDatabaseConnection#RemoveProductFromGroceryList(java.lang
	 * .Integer, BasicCommonClasses.ProductPackage)
	 */
	@Override
	public void removeProductFromGroceryList(Integer cartID, ProductPackage productToBuy) throws CriticalError,
			ClientNotConnected, ProductNotExistInCatalog, ProductPackageAmountNotMatch, ProductPackageNotExist {

		try {
			// START transaction
			connection.setAutoCommit(false);
			if (!isProductExistInCatalog(productToBuy.getSmartCode().getBarcode()))
				throw new ProductNotExistInCatalog();

			moveProductPackage(cartID, LOCATIONS_TYPES.CART, LOCATIONS_TYPES.STORE, productToBuy,
					productToBuy.getAmount());
			// END transaction
			connection.commit();
			connection.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new CriticalError();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * SQLDatabase.ISQLDatabaseConnection#PlaceProductPackageOnShelves(java.lang
	 * .Integer, BasicCommonClasses.ProductPackage)
	 */
	@Override
	public void placeProductPackageOnShelves(Integer sessionID, ProductPackage p) throws CriticalError,
			ClientNotConnected, ProductNotExistInCatalog, ProductPackageAmountNotMatch, ProductPackageNotExist {

		validateSessionEstablished(sessionID);

		try {
			// START transaction
			connection.setAutoCommit(false);
			if (!isProductExistInCatalog(p.getSmartCode().getBarcode()))
				throw new ProductNotExistInCatalog();

			moveProductPackage(sessionID, LOCATIONS_TYPES.WAREHOUSE, LOCATIONS_TYPES.STORE, p, p.getAmount());
			// END transaction
			connection.commit();
			connection.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new CriticalError();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * SQLDatabase.ISQLDatabaseConnection#RemoveProductPackageFromShelves(java.
	 * lang.Integer, BasicCommonClasses.ProductPackage)
	 */
	@Override
	public void removeProductPackageFromShelves(Integer sessionID, ProductPackage p) throws CriticalError,
			ClientNotConnected, ProductNotExistInCatalog, ProductPackageAmountNotMatch, ProductPackageNotExist {

		validateSessionEstablished(sessionID);
		try {
			// START transaction
			connection.setAutoCommit(false);
			if (!isProductExistInCatalog(p.getSmartCode().getBarcode()))
				throw new ProductNotExistInCatalog();

			moveProductPackage(sessionID, LOCATIONS_TYPES.STORE, null, p, p.getAmount());
			// END transaction
			connection.commit();
			connection.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new CriticalError();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * SQLDatabase.ISQLDatabaseConnection#GetProductPackageAmonutOnShelves(java.
	 * lang.Integer, BasicCommonClasses.ProductPackage)
	 */
	@Override
	public String getProductPackageAmonutOnShelves(Integer sessionID, ProductPackage p)
			throws CriticalError, ClientNotConnected, ProductNotExistInCatalog {

		validateSessionEstablished(sessionID);

		return new Gson().toJson(getAmountForStore(p, PRODUCTS_PACKAGES_TABLE.VALUE_PLACE_STORE));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * SQLDatabase.ISQLDatabaseConnection#GetProductPackageAmonutInWarehouse(
	 * java.lang.Integer, BasicCommonClasses.ProductPackage)
	 */
	@Override
	public String getProductPackageAmonutInWarehouse(Integer sessionID, ProductPackage p)
			throws CriticalError, ClientNotConnected, ProductNotExistInCatalog {

		validateSessionEstablished(sessionID);

		return new Gson().toJson(getAmountForStore(p, PRODUCTS_PACKAGES_TABLE.VALUE_PLACE_WAREHOUSE));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see SQLDatabase.ISQLDatabaseConnection#cartCheckout(java.lang.Integer)
	 */
	@Override
	public void cartCheckout(Integer cartID) throws CriticalError, ClientNotConnected {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see SQLDatabase.ISQLDatabaseConnection#close()
	 */
	@Override
	public void close() throws CriticalError {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLDatabaseException.CriticalError();
		}
	}

	@Override
	public String addManufacturer(Integer sessionID, String manufacturerName) throws CriticalError, ClientNotConnected {

		validateSessionEstablished(sessionID);

		int $;
		try {
			// START transaction
			connection.setAutoCommit(false);
			$ = allocateIDToTable(ManufacturerTable.table, ManufacturerTable.manufacturerIDCol);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new CriticalError();
		}

		String insertQuery = new InsertQuery(ManufacturerTable.table)
				.addColumn(ManufacturerTable.manufacturerIDCol, PARAM_MARK)
				.addColumn(ManufacturerTable.manufacturerNameCol, PARAM_MARK).validate() + "";

		insertQuery.hashCode();

		try {
			getParameterizedQuery(insertQuery, $, manufacturerName).executeUpdate();

			// END transaction
			connection.commit();
			connection.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
			// if the creation failed - free the id
			freeIDOfTable(ManufacturerTable.table, $);
		}

		return new Gson().toJson($);
	}

	@Override
	public void removeManufacturer(Integer sessionID, Manufacturer m)
			throws CriticalError, ClientNotConnected, ManufacturerNotExist, ManufacturerStillUsed {
		validateSessionEstablished(sessionID);

		try {
			// START transaction
			connection.setAutoCommit(false);
			if (!isManufacturerExist((int) m.getId()))
				throw new ManufacturerNotExist();

			// if the manufacturer still used in catalog - throw exception
			if (isSuchRowExist(ProductsCatalogTable.table, ProductsCatalogTable.manufacturerIDCol, (Long) m.getId()))
				throw new ManufacturerStillUsed();

			// delete manufacturer
			getParameterizedQuery(generateDeleteQuery(ManufacturerTable.table,
					BinaryCondition.equalTo(ManufacturerTable.manufacturerIDCol, PARAM_MARK)), m.getId())
							.executeUpdate();

			freeIDOfTable(ManufacturerTable.table, (int) m.getId());

			// END transaction
			connection.commit();
			connection.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void editManufacturer(Integer sessionID, Manufacturer newManufacturer)
			throws CriticalError, ClientNotConnected, ManufacturerNotExist {
		validateSessionEstablished(sessionID);

		try {
			// START transaction
			connection.setAutoCommit(false);
			if (!isManufacturerExist((int) newManufacturer.getId()))
				throw new ManufacturerNotExist();

			// update manufacturer
			UpdateQuery updateQuery = generateUpdateQuery(ManufacturerTable.table,
					BinaryCondition.equalTo(ManufacturerTable.manufacturerIDCol, PARAM_MARK));

			updateQuery.addSetClause(ManufacturerTable.manufacturerNameCol, PARAM_MARK).validate();

			getParameterizedQuery(updateQuery + "", newManufacturer.getId(), newManufacturer.getName()).executeUpdate();

			// END transaction
			connection.commit();
			connection.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void logoutAllUsers() throws CriticalError {
		PreparedStatement statement = getParameterizedQuery(generateDeleteQuery(CartsListTable.table));
		try {
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new CriticalError();
		} finally {
			closeResources(statement);
		}

		statement = getParameterizedQuery(generateDeleteQuery(GroceriesListsTable.table));
		try {
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new CriticalError();
		} finally {
			closeResources(statement);
		}

		statement = getParameterizedQuery(new UpdateQuery(WorkersTable.table)
				.addSetClause(WorkersTable.sessionIDCol, SqlObject.NULL_VALUE).validate() + "");
		try {
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new CriticalError();
		} finally {
			closeResources(statement);
		}

	}



}
