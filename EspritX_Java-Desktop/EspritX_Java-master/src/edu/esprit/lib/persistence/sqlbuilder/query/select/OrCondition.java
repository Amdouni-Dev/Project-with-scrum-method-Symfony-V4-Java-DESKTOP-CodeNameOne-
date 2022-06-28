package edu.esprit.lib.persistence.sqlbuilder.query.select;

import edu.esprit.lib.persistence.sqlbuilder.DbContext;

class OrCondition extends Condition {

	OrCondition(DbContext dbContext) {
		super(dbContext);
	}

	@Override
	protected String getPrefix() {
		return "OR";
	}
}
