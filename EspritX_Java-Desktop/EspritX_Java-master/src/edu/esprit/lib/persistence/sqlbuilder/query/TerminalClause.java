package edu.esprit.lib.persistence.sqlbuilder.query;

import edu.esprit.lib.persistence.sqlbuilder.query.select.RowMapper;
import java.sql.SQLException;
import java.util.List;

public interface TerminalClause {
	<E> List<E> list(RowMapper<E> rowMapper) throws SQLException;

	<E> E single(RowMapper<E> rowMapper) throws SQLException;
}
