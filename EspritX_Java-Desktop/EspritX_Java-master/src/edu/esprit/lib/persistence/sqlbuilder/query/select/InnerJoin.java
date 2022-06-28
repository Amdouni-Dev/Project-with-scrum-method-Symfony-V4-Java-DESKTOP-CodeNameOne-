package edu.esprit.lib.persistence.sqlbuilder.query.select;

import edu.esprit.lib.persistence.sqlbuilder.DbContext;

class InnerJoin extends Join {

	InnerJoin(DbContext dbContext, String condition) {
		super(dbContext, condition);
	}

	InnerJoin(DbContext dbContext) {
		super(dbContext);
	}

	@Override
	protected String expression() {
		return "INNER JOIN";
	}
}
