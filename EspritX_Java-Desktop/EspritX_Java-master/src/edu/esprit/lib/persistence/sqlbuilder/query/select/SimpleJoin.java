package edu.esprit.lib.persistence.sqlbuilder.query.select;

import edu.esprit.lib.persistence.sqlbuilder.DbContext;

class SimpleJoin extends Join {

	SimpleJoin(DbContext dbContext) {
		super(dbContext);
	}

	SimpleJoin(DbContext dbContext, String condition) {
		super(dbContext, condition);
	}

	@Override
	protected String expression() {
		return "JOIN";
	}
}
