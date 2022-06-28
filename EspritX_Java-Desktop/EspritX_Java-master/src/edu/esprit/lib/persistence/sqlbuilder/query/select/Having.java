package edu.esprit.lib.persistence.sqlbuilder.query.select;

import edu.esprit.lib.persistence.sqlbuilder.DbContext;
import edu.esprit.lib.persistence.sqlbuilder.query.TerminalClause;
import org.apache.commons.lang.StringUtils;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Having implements TerminalClause {
	private final DbContext dbContext;

	private boolean terminated = false;

	private final List<String> conditions;

	Having(DbContext dbContext) {
		this.dbContext = dbContext;
		this.dbContext.appendLine("HAVING");
		conditions = new LinkedList<>();
	}

	Having(DbContext dbContext, String... conditions) {
		this(dbContext);
		this.conditions.addAll(Arrays.asList(conditions));
	}

	public Having condition(String condition) {
		conditions.add(condition);
		return this;
	}

	public Having conditions(String... conditions) {
		this.conditions.addAll(Arrays.asList(conditions));
		return this;
	}

	public OrderBy orderBy(String... columns) {
		terminate();
		return new OrderBy(dbContext, columns);
	}

	public OrderBy orderBy(OrderByType order, String... columns) {
		terminate();
		return new OrderBy(dbContext, order, columns);
	}

	public OrderBy orderBy(String column, OrderByType order) {
		terminate();
		return new OrderBy(dbContext, order, column);
	}

	@Override
	public <E> List<E> list(RowMapper<E> rowMapper) throws SQLException {
		terminate();
		return dbContext.list(rowMapper);
	}

	@Override
	public <E> E single(RowMapper<E> rowMapper) throws SQLException {
		terminate();
		return dbContext.single(rowMapper);
	}

	@Override
	public String toString() {
		terminate();
		return dbContext.toString();
	}

	private void terminate() {
		if (!terminated) {
			dbContext.appendLine(StringUtils.join(conditions, ", "));
			terminated = true;
		}
	}
}
