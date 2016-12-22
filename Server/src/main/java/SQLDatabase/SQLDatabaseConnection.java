package SQLDatabase;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import com.google.gson.Gson;
import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.CreateTableQuery;
import com.healthmarketscience.sqlbuilder.SelectQuery;
import com.healthmarketscience.sqlbuilder.ValidationException;

import BasicCommonClasses.CatalogProduct;
import BasicCommonClasses.Ingredient;
import BasicCommonClasses.Location;
import BasicCommonClasses.Manufacturer;
import BasicCommonClasses.PlaceInMarket;

import SQLDatabase.SQLDatabaseEntities;
import SQLDatabase.SQLDatabaseEntities.CartsListTable;
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
import SQLDatabase.SQLDatabaseException.ProductNotExistInCatalog;
import SQLDatabase.SQLDatabaseException.WorkerNotConnected;

/**
 * SqlDBConnection - Handles the server request to the SQL database.
 * 
 * @author Noam Yefet
 * @since 2016-12-14

 */
public class SQLDatabaseConnection {

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

	@SuppressWarnings("unused")
	private static final Long SESSION_IDS_BEGIN = 10000L;

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
	 * return the number of rows in specific ResultSet
	 * 
	 * @param s
	 * @return the number of rows
	 */
	@SuppressWarnings("unused")
	private int getResultSetRowCount(ResultSet s) {
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
	 * return if there any result in ResultsSet
	 * 
	 * @param ¢
	 * @return true - if there are rows in the ResultsSet, false otherwise.
	 * @throws CriticalError
	 */
	private boolean isResultSetRowsExist(ResultSet ¢) throws CriticalError {
		boolean $;
		try {
			$ = ¢.first();
			¢.beforeFirst();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new CriticalError();
		}
		return $;
	}

	public int WorkerLogin(String username, String password) throws AuthenticationError, CriticalError {
		String query = new SelectQuery().addAllTableColumns(WorkersTable.table)
				.addCondition(BinaryCondition.equalTo(WorkersTable.workerUsernameCol, PARAM_MARK))
				.addCondition(BinaryCondition.equalTo(WorkersTable.workerPasswordCol, PARAM_MARK)).validate() + "";

		PreparedStatement statement = getParameterizedQuery(query, username, password);

		ResultSet result;
		try {
			result = statement.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new CriticalError();
		}

		// if no result - throw exception
		if (!isResultSetRowsExist(result))
			throw new SQLDatabaseException.AuthenticationError();

		return 1001;
	}

	public void WorkerLogout(Integer sessionID, String username) throws WorkerNotConnected, CriticalError {
		checkSessionEstablished(sessionID);

	}

	public String getProductFromCatalog(Integer sessionID, long barcode)
			throws ProductNotExistInCatalog, WorkerNotConnected, CriticalError {

		checkSessionEstablished(sessionID);

		String query = new SelectQuery().addAllTableColumns(ProductsCatalogTable.table)
				.addCondition(BinaryCondition.equalTo(ProductsCatalogTable.barcodeCol, PARAM_MARK)).validate() + "";

		PreparedStatement statement = getParameterizedQuery(query, Long.valueOf(barcode));

		ResultSet result;
		try {
			result = statement.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new CriticalError();
		}

		// if no result - throw exception
		if (!isResultSetRowsExist(result))
			throw new SQLDatabaseException.ProductNotExistInCatalog();

		HashSet<Ingredient> ingredients = new HashSet<Ingredient>();
		ingredients.add(new Ingredient(123, "milk"));
		HashSet<Location> locations = new HashSet<Location>();
		locations.add(new Location(1, 1, PlaceInMarket.STORE));

		return new Gson().toJson((new CatalogProduct(1234567890L, "Milk 3%", ingredients,
				new Manufacturer(334, "Tnuva"), "", 10.0, locations)));
	}

	private void checkSessionEstablished(Integer sessionID) throws WorkerNotConnected, CriticalError {
		try {
			if (sessionID != null && sessionID != 1001 && !isResultSetRowsExist(
					getParameterizedQuery((new SelectQuery().addAllTableColumns(WorkersTable.table)
							.addCondition(BinaryCondition.equalTo(WorkersTable.sessionIDCol, PARAM_MARK)).validate()
							+ ""), sessionID).executeQuery())
					&& !isResultSetRowsExist(
							getParameterizedQuery((new SelectQuery().addAllTableColumns(WorkersTable.table)
									.addCondition(BinaryCondition.equalTo(WorkersTable.sessionIDCol, PARAM_MARK))
									.validate() + ""), sessionID).executeQuery()))
				throw new SQLDatabaseException.WorkerNotConnected();
		} catch (ValidationException | SQLException e) {
			e.printStackTrace();
			throw new CriticalError();
		}
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

		} catch (SQLException e) {

			e.printStackTrace();

		}

	}

}
