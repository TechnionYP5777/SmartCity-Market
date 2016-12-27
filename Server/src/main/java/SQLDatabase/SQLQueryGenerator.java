package SQLDatabase;

import com.healthmarketscience.sqlbuilder.Condition;
import com.healthmarketscience.sqlbuilder.SelectQuery;
import com.healthmarketscience.sqlbuilder.UpdateQuery;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbTable;

import SQLDatabase.SQLDatabaseEntities.WorkersTable;

/**
 * @author Noam Yefet
 */
class SQLQueryGenerator {

	/**
	 * Generate string of select query.
	 * 
	 * @param tabel
	 *            The table to select
	 * @param cs
	 *            Set of conditions
	 * @return string of select query.
	 */
	public static String generateSelectQuery1Table(DbTable tabel, Condition... cs) {
		SelectQuery resultQuery = new SelectQuery().addAllTableColumns(WorkersTable.table);

		for (int ¢ = 0; ¢ < cs.length; ++¢)
			resultQuery.addCondition(cs[¢]);

		return resultQuery.validate() + "";

	}

	/**
	 * Generate object of update query.
	 * 
	 * @param tabel
	 *            The table to select
	 * @param cs
	 *            Set of conditions
	 * @return string of select query.
	 */
	public static UpdateQuery generateUpdateQuery(DbTable tabel, Condition... cs) {
		UpdateQuery $ = new UpdateQuery(WorkersTable.table);

		for (int ¢ = 0; ¢ < cs.length; ++¢)
			$.addCondition(cs[¢]);

		return $;

	}

}
