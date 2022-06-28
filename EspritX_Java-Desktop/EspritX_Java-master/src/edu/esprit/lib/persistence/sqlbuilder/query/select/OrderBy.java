package edu.esprit.lib.persistence.sqlbuilder.query.select;

import edu.esprit.lib.persistence.sqlbuilder.DbContext;
import edu.esprit.lib.persistence.sqlbuilder.query.TerminalClause;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang.StringUtils;

public class OrderBy implements TerminalClause {
	private DbContext dbContext;

	private OrderByType order;

	private boolean terminated = false;

	private final List<String> columns = new ArrayList<>();

	OrderBy(DbContext dbContext) {
		this.dbContext = dbContext;
		this.order = OrderByType.ASC;
		dbContext.appendLine("ORDER BY");
	}

	OrderBy(DbContext dbContext, String... columns) {
		this(dbContext);
		this.columns.addAll(Arrays.asList(columns));
	}

	OrderBy(DbContext dbContext, OrderByType order, String... columns) {
		this(dbContext, columns);
		this.order = order;
	}

	public OrderBy column(String column) {
		return column(column, OrderByType.ASC);
	}

	public OrderBy columns(String... columns) {
		this.columns.addAll(Arrays.asList(columns));
		this.order = OrderByType.ASC;
		return this;
	}

	public OrderBy columns(OrderByType order, String... columns) {
		columns(columns);
		this.order = order;
		return this;
	}

	public OrderBy column(String column, OrderByType order) {
		if (order == null) {
			return column(column);
		}

		columns.add(column);
		this.order = order;
		return this;
	}

	public Limit limit(int start, int size) {
		terminate();
		return new Limit(dbContext, start, size);
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
			dbContext.appendLine(" ");
			dbContext.appendLine(StringUtils.join(columns, ", "));
			dbContext.appendLine(" " + order.name());
		}
	}
}
