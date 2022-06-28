package edu.esprit.lib.persistence.sqlbuilder;

import edu.esprit.lib.persistence.sqlbuilder.query.Delete;
import edu.esprit.lib.persistence.sqlbuilder.query.Insert;
import edu.esprit.lib.persistence.sqlbuilder.query.select.Select;
import edu.esprit.lib.persistence.sqlbuilder.query.Update;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class QueryBuilder {
	private final DbContext dbContext;

	public QueryBuilder(DatabaseType databaseType, DataSource dataSource)
		throws SQLException {
		this(databaseType, dataSource.getConnection());
	}

	public QueryBuilder(DatabaseType databaseType, Connection connection) {
		this.dbContext = new DbContext(databaseType, connection);
	}

	public Select select() {
		return new Select(dbContext);
	}

	public Update update() {
		return new Update(dbContext);
	}

	public Update update(String table) {
		return new Update(dbContext, table);
	}

	public Delete delete() {
		return new Delete(dbContext);
	}

	public Delete delete(String table) {
		return new Delete(dbContext, table);
	}

	public Insert insert() {
		return new Insert(dbContext);
	}

	public Insert insert(String table) {
		return new Insert(dbContext, table);
	}

	@Override
	public String toString() {
		return dbContext.toString();
	}
}
