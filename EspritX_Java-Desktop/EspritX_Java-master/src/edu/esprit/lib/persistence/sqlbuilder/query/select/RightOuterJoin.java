package edu.esprit.lib.persistence.sqlbuilder.query.select;

import edu.esprit.lib.persistence.sqlbuilder.DbContext;

class RightOuterJoin extends Join {

	RightOuterJoin(DbContext dbContext, String condition) {
		super(dbContext, condition);
	}

	RightOuterJoin(DbContext dbContext) {
		super(dbContext);
	}

	@Override
	protected String expression() {
		return "RIGHT OUTER JOIN";
	}
}
