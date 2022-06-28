package edu.esprit.lib.persistence.sqlbuilder.query.select;

import edu.esprit.lib.persistence.sqlbuilder.DbContext;
import edu.esprit.lib.persistence.sqlbuilder.query.TerminalClause;
import java.sql.SQLException;
import java.util.List;

public class Where extends Condition implements TerminalClause {

	Where(DbContext dbContext) {
		super(dbContext);
		add("1 = 1");
	}

	Where(DbContext dbContext, String condition) {
		super(dbContext);
		add(condition);
	}

	public GroupBy groupBy() {
		return new GroupBy(dbContext);
	}

	public GroupBy groupBy(String... columns) {
		return new GroupBy(dbContext, columns);
	}

	public OrderBy orderBy() {
		return new OrderBy(dbContext);
	}

	public OrderBy orderBy(String... columns) {
		return new OrderBy(dbContext, columns);
	}

	public OrderBy orderBy(OrderByType order, String... columns) {
		return new OrderBy(dbContext, order, columns);
	}

	public Where and(Object condition) {
		new AndCondition(dbContext).add(condition);
		return this;
	}

	public Where and(Object condition, Object parameter) {
		new AndCondition(dbContext).add(condition, parameter);
		return this;
	}

	public Where and(String condition, String parameter) {
		new AndCondition(dbContext).add(condition, parameter);
		return this;
	}

	public Where and(Object condition, Object... parameters) {
		new AndCondition(dbContext).add(condition, parameters);
		return this;
	}

	public Where andBetween(String columnName, Object start, Object end) {
		new AndCondition(dbContext).between(columnName, start, end);
		return this;
	}

	public Where or(Object condition) {
		new OrCondition(dbContext).add(condition);
		return this;
	}

	public Where or(Object condition, Object parameter) {
		new OrCondition(dbContext).add(condition, parameter);
		return this;
	}

	public Where or(String condition, String parameter) {
		new OrCondition(dbContext).add(condition, parameter);
		return this;
	}

	public Where or(Object condition, Object... parameters) {
		new OrCondition(dbContext).add(condition, parameters);
		return this;
	}

	public Where orBetween(String columnName, Object start, Object end) {
		new OrCondition(dbContext).between(columnName, start, end);
		return this;
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

	@Override
	protected String getPrefix() {
		return "WHERE";
	}

	@Override
	public String toString() {
		return dbContext.toString();
	}
}
