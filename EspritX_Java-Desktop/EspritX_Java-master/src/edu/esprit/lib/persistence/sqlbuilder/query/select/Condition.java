package edu.esprit.lib.persistence.sqlbuilder.query.select;

import edu.esprit.lib.persistence.sqlbuilder.DbContext;
import edu.esprit.lib.persistence.sqlbuilder.InvalidQueryException;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

abstract class Condition {
	protected final DbContext dbContext;

	Condition(DbContext dbContext) {
		this.dbContext = dbContext;
	}

	void add(Object condition) {
		dbContext.appendLine(getPrefix() + " " + condition);
	}

	void add(Object condition, Object parameter) {
		if (parameter != null) {
			add(condition, new Object[] { parameter });
		} else {
			if (isEqualCondition(condition.toString())) {
				add(extractColumnName(condition.toString()) + " IS NULL");
			} else {
				throw new InvalidQueryException("Could not solve '" + condition + "' condition with a null parameter");
			}
		}
	}

	void add(Object condition, Object... parameters) {
		if (ArrayUtils.isNotEmpty(parameters)) {
			dbContext.addParameters(parameters);
			add(condition);
		}
	}

	void between(String columnName, Object start, Object end) {
		if (start == null) {
			if (end != null) {
				add(columnName + " <= ?", end);
			}
		} else {
			if (end == null) {
				add(columnName + " >= ?", start);
			} else {
				add(columnName + " BETWEEN ? AND ?", start, end);
			}
		}
	}

	private Boolean isEqualCondition(String condition) {
		return StringUtils.contains(condition, " =");
	}

	private String extractColumnName(String condition) {
		String[] conditions = new String[] {
			" =",
			" >",
			" >=",
			" <",
			" <=",
			" <>",
			" IN",
			" IS",
			" BETWEEN"
		};
		String result = condition.toString();

		for (String c : conditions) {
			result = StringUtils.substringBefore(result, c);
		}

		return result;
	}

	protected abstract String getPrefix();
}
