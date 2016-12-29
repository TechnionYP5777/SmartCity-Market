package SQLDatabase;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.CreateTableQuery;
import com.healthmarketscience.sqlbuilder.SelectQuery;
import com.healthmarketscience.sqlbuilder.UnaryCondition;
import com.healthmarketscience.sqlbuilder.UpdateQuery;
import com.healthmarketscience.sqlbuilder.ValidationException;

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
import SQLDatabase.SQLDatabaseException.NumberOfConnectionsExceeded;
import SQLDatabase.SQLDatabaseException.ProductNotExistInCatalog;
import SQLDatabase.SQLDatabaseException.WorkerAlreadyConnected;
import SQLDatabase.SQLDatabaseException.WorkerNotConnected;
import static SQLDatabase.SQLQueryGenerator.generateSelectQuery1Table;
import static SQLDatabase.SQLQueryGenerator.generateSelectLeftJoinWithQuery2Tables;
import static SQLDatabase.SQLQueryGenerator.generateUpdateQuery;

/**
 * SqlDBConnection - Handles the server request to the SQL database.
 * 
 * @author Noam Yefet
 * @since 2016-12-14
 * 
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
	 * return the number of rows in specific ResultSet
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
	 * return if there any result in ResultsSet
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
	 * Validate if the worker login the system
	 * 
	 * @param sessionID
	 *            - sessionID of the worker
	 * @throws WorkerNotConnected
	 * @throws CriticalError
	 */
	private void validateSessionEstablished(Integer sessionID) throws WorkerNotConnected, CriticalError {
		try {
			if (!isSessionEstablished(sessionID))
				throw new SQLDatabaseException.WorkerNotConnected();
		} catch (ValidationException e) {
			e.printStackTrace();
			throw new CriticalError();
		}
	}

	/**
	 * Check if the worker logged in the system
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

		ResultSet result;
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

	public int WorkerLogin(String username, String password)
			throws AuthenticationError, WorkerAlreadyConnected, CriticalError, NumberOfConnectionsExceeded {
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
			throw new SQLDatabaseException.WorkerAlreadyConnected();

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

	public void WorkerLogout(Integer sessionID, String username) throws WorkerNotConnected, CriticalError {

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

	public String getProductFromCatalog(Integer sessionID, long barcode)
			throws ProductNotExistInCatalog, WorkerNotConnected, CriticalError {

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

	public void close() throws CriticalError {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLDatabaseException.CriticalError();
		}
	}

}
