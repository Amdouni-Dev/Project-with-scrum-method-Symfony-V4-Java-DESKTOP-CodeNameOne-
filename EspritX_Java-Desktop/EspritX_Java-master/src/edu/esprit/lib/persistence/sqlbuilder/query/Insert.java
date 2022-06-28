package edu.esprit.lib.persistence.sqlbuilder.query;

import edu.esprit.lib.persistence.sqlbuilder.DbContext;
import edu.esprit.lib.persistence.sqlbuilder.InvalidQueryException;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import java.util.*;

public class Insert {
	private String table;
	private final List<String> columns;
	private final List<Object[]> values;
	private final DbContext dbContext;
	private boolean terminated = false;

	public Insert(DbContext dbContext) {
		this.dbContext = dbContext;
		this.dbContext.append("INSERT INTO ");
		this.columns = new LinkedList<>();
		this.values = new LinkedList<>();
	}

	public Insert(DbContext dbContext, String table) {
		this(dbContext);
		this.table = String.format("`%s`", table);
	}

	public Insert table(String table) {
		this.table = table;
		return this;
	}

	public Insert columns(String... columns) {
		Collections.addAll(this.columns, Arrays.stream(columns)
				.map(s -> String.format("`%s`", s))
				.toArray(String[]::new));
		return this;
	}

	public Insert values(Object... values) {
		this.values.add(values);
		return this;
	}

	@Override
	public String toString() {
		terminate();
		return dbContext.toString();
	}

	private synchronized void terminate() {
		if (columns.isEmpty()) throw new InvalidQueryException("No columns informed. Use the method 'columns' to inform the columns.");

		if (values.isEmpty()) throw new InvalidQueryException("No values informed. Use the method 'values' to inform the values.");

		for (Object[] valueSet : values) {
			if (valueSet.length != columns.size()) {
				throw new InvalidQueryException(
					"Value size different from column size."
				);
			}
		}

		if (!terminated) {
			dbContext
				.appendLine(table)
				.append(" ( ")
				.append(StringUtils.join(columns, ", "))
				.appendLine(" )")
				.append("VALUES ")
				.append(StringUtils.join(getValues(), ", "));
		}
		terminated = true;
	}

	private String[] getValues() {
		String[] result = new String[values.size()];

		for (int i = 0; i < result.length; i++) {
			Object[] objs = values.get(i);
			result[i] = toValue(objs);
		}

		return result;
	}

	private String toValue(Object[] objs) {
		String[] result = new String[objs.length];

		for (int i = 0; i < result.length; i++) {
			if(objs[i] == null) {
				result[i] = "NULL";
			}
			else if (objs[i] instanceof String || objs[i] instanceof Date) {
				result[i] = "'" + StringEscapeUtils.escapeSql(objs[i].toString()) + "'";
			} else {
				result[i] = objs[i].toString();
			}
		}

		return "(" + StringUtils.join(result, ", ") + ")";
	}
}
