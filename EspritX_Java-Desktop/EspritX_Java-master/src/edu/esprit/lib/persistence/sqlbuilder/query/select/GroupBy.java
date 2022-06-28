package edu.esprit.lib.persistence.sqlbuilder.query.select;

import edu.esprit.lib.persistence.sqlbuilder.DbContext;
import edu.esprit.lib.persistence.sqlbuilder.query.TerminalClause;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.lang.StringUtils;

public class GroupBy implements TerminalClause {
	private final DbContext dbContext;

	private final List<String> columns;

	private boolean terminated = false;

	GroupBy(DbContext dbContext) {
		this.dbContext = dbContext;
		dbContext.appendLine("GROUP BY");
		columns = new LinkedList<>();
	}

	GroupBy(DbContext dbContext, String... columns) {
		this(dbContext);
		this.columns.addAll(Arrays.asList(columns));
	}

	public GroupBy column(String column) {
		columns.add(column);
		return this;
	}

	public GroupBy columns(String... columns) {
		this.columns.addAll(Arrays.asList(columns));
		return this;
	}

	public Having having() {
		terminate();
		return new Having(dbContext);
	}

	public Having having(String condition) {
		terminate();
		return new Having(dbContext, condition);
	}

	public OrderBy orderBy() {
		terminate();
		return new OrderBy(dbContext);
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
			dbContext.appendLine(StringUtils.join(columns, ", "));
			terminated = true;
		}
	}
}
