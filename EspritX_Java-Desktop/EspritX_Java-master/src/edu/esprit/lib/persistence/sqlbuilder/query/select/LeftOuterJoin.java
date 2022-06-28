package edu.esprit.lib.persistence.sqlbuilder.query.select;

import edu.esprit.lib.persistence.sqlbuilder.DbContext;

class LeftOuterJoin extends Join {

	LeftOuterJoin(DbContext dbContext) {
		super(dbContext);
	}

	LeftOuterJoin(DbContext dbContext, String condition) {
		super(dbContext, condition);
	}

	@Override
	protected String expression() {
		return "LEFT OUTER JOIN";
	}
}
