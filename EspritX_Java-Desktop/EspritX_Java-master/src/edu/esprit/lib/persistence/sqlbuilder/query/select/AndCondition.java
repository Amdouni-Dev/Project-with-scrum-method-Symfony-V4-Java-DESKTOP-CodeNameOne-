package edu.esprit.lib.persistence.sqlbuilder.query.select;

import edu.esprit.lib.persistence.sqlbuilder.DbContext;

class AndCondition extends Condition {

	AndCondition(DbContext dbContext) {
		super(dbContext);
	}

	@Override
	protected String getPrefix() {
		return "AND";
	}
}
