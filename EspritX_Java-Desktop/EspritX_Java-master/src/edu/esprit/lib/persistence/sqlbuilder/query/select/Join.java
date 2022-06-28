package edu.esprit.lib.persistence.sqlbuilder.query.select;

import edu.esprit.lib.persistence.sqlbuilder.DbContext;
import edu.esprit.lib.persistence.sqlbuilder.query.TerminalClause;
import java.sql.SQLException;
import java.util.List;

public abstract class Join implements TerminalClause {
	private DbContext dbContext;

	Join(DbContext dbContext) {
		this.dbContext = dbContext;
		dbContext.appendLine(expression());
	}

	Join(DbContext dbContext, String condition) {
		this(dbContext);
		dbContext.appendLine(condition);
	}

	public OrderBy orderBy() {
		return new OrderBy(dbContext);
	}

	public Where where() {
		return new Where(dbContext);
	}

	public Where where(String condition) {
		return new Where(dbContext, condition);
	}

	public Limit limit(int start, int size) {
		return new Limit(dbContext, start, size);
	}

	@Override
	public <E> List<E> list(RowMapper<E> rowMapper) throws SQLException {
		return dbContext.list(rowMapper);
	}

	@Override
	public <E> E single(RowMapper<E> rowMapper) throws SQLException {
		return dbContext.single(rowMapper);
	}

	protected abstract String expression();

	@Override
	public String toString() {
		return dbContext.toString();
	}
}
