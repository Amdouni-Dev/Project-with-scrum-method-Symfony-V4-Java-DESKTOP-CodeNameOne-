package edu.esprit.lib.persistence.sqlbuilder.query.select;

import edu.esprit.lib.persistence.sqlbuilder.DbContext;
import edu.esprit.lib.persistence.sqlbuilder.DatabaseType;
import edu.esprit.lib.persistence.sqlbuilder.query.TerminalClause;
import java.sql.SQLException;
import java.util.List;

public class Limit implements TerminalClause {
	private final DbContext dbContext;

	public Limit(DbContext dbContext, int start, int size) {
		this.dbContext = limit(dbContext, start, size);
	}

	@Override
	public <E> List<E> list(RowMapper<E> rowMapper) throws SQLException {
		return dbContext.list(rowMapper);
	}

	@Override
	public <E> E single(RowMapper<E> rowMapper) throws SQLException {
		return dbContext.single(rowMapper);
	}

	private DbContext limit(DbContext dbContext, int start, int size) {
		return new LimiterFactory()
			.create(dbContext.getDatabase())
			.limit(dbContext, start, size);
	}

	@Override
	public String toString() {
		return dbContext.toString();
	}
}

interface Limiter {
	DbContext limit(DbContext dbContext, int start, int size);
}

class MySQLDbLimiter implements Limiter {
	@Override
	public DbContext limit(DbContext dbContext, int start, int size) {
		dbContext.appendLine("LIMIT ?");
		dbContext.addParameters(size);
		dbContext.appendLine("OFFSET ?");
		dbContext.addParameters(start);
		return dbContext;
	}
}

class DefaultLimiter implements Limiter {
	@Override
	public DbContext limit(DbContext dbContext, int start, int size) {
		return dbContext;
	}
}

class LimiterFactory {

	Limiter create(DatabaseType databaseType) {
		switch (databaseType) {
			case MYSQL:
				return new MySQLDbLimiter();
			default:
				return new DefaultLimiter();
		}
	}
}
