package edu.esprit.lib.persistence.sqlbuilder;

import edu.esprit.lib.persistence.sqlbuilder.query.select.RowMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DbContext {
	private static final String SPACER = " "; // System.getProperty("line.separator");

	private final Connection connection;

	private final StringBuilder sql;

	private final List<Object> parameters;

	private final DatabaseType databaseType;

	private final transient Logger log;

	{
		this.log = Logger.getLogger(getClass().getName());
	}

	public DbContext(DbContext clone) {
		this.connection = clone.connection;
		this.parameters = new LinkedList<Object>(clone.parameters);
		this.sql = new StringBuilder();
		this.databaseType = clone.databaseType;
	}

	public DbContext(DatabaseType databaseType, Connection connection) {
		this.connection = connection;
		this.databaseType = databaseType;
		sql = new StringBuilder();
		parameters = new LinkedList<>();
	}

	@Override
	public String toString() {
		return sql.toString();
	}

	public <E> List<E> list(RowMapper<E> rowMapper) throws SQLException {
		List<E> result = new LinkedList<>();

		try (ResultSet resultSet = execute(toString())) {
			int rowNum = 0;
			while (true) {
				if (!resultSet.next()) break;
				E obj = rowMapper.convert(resultSet, rowNum++);
				result.add(obj);
			}
		}

		return result;
	}

	public <E> E single(RowMapper<E> rowMapper) throws SQLException {
		E result = null;

		try (ResultSet resultSet = execute(toString())) {
			if (resultSet.next()) {
				result = rowMapper.convert(resultSet, 1);

				if (resultSet.next()) {
					throw new SQLException("The query returned more than one result");
				}
			}
		}

		return result;
	}

	public DbContext append(String expression) {
		sql.append(expression);
		return this;
	}

	public DbContext appendLine(String expression) {
		sql.append(expression);
		sql.append(SPACER);
		return this;
	}

	public DbContext newLine() {
		sql.append(SPACER);
		return this;
	}

	public void addParameters(Object... parameters) {
		this.parameters.addAll(Arrays.asList(parameters));
	}

	public DatabaseType getDatabase() {
		return databaseType;
	}

	private ResultSet execute(String sql) throws SQLException {
		log.log(Level.INFO, "\n" + sql);
		try {
			return prepareStatement(sql).executeQuery();
		} catch (SQLException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	private PreparedStatement prepareStatement(String sql) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(sql);

		int i = 1;
		for (Object parameter : parameters) {
			statement.setObject(i++, parameter);
		}
		return statement;
	}
}
