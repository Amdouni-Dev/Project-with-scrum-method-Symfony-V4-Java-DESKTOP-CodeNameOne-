package edu.esprit.lib.persistence.sqlbuilder.query;

import edu.esprit.lib.persistence.sqlbuilder.DbContext;
import edu.esprit.lib.persistence.sqlbuilder.InvalidQueryException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

public class Delete {
	private String table;
	private final DbContext dbContext;
	private final Collection<String> conditions;
	private boolean terminated = false;

	public Delete(DbContext dbContext) {
		this.dbContext = dbContext;
		this.dbContext.append("DELETE FROM ");
		conditions = new LinkedList<>();
	}

	public Delete(DbContext dbContext, String table) {
		this(dbContext);
		this.table = table;
	}

	public Delete where(String condition) {
		conditions.add(condition);
		return this;
	}

	public Delete and(String condition) {
		conditions.add(condition);
		return this;
	}

	private void terminate() {
		if (table == null || table.trim().isBlank()) throw new InvalidQueryException(
			"No table specified!"
		);

		if (!terminated) {
			dbContext.append(String.format("`%s`", table));

			if (!conditions.isEmpty()) {
				dbContext.newLine().append("WHERE ");

				Iterator<String> conditionIter = conditions.iterator();

				while (conditionIter.hasNext()) {
					String condition = conditionIter.next();
					dbContext.append(condition);

					if (conditionIter.hasNext()) {
						dbContext.newLine().append("AND ");
					}
				}
			}

			terminated = true;
		}
	}

	@Override
	public String toString() {
		terminate();
		return dbContext.toString();
	}
}
