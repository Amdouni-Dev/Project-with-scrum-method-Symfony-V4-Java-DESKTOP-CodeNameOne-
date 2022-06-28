package edu.esprit.lib.persistence.sqlbuilder.mapper;

import edu.esprit.lib.persistence.sqlbuilder.query.select.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RowToScalarMapper<T> extends RowMapper<T> {
    protected Class<T> targetClass;

    public RowToScalarMapper(Class<T> targetClass) {
        this.targetClass = targetClass;
    }

    @Override
    public T convert(ResultSet resultSet, int rowNum) throws SQLException {
        return (T) resultSet.getObject(1, this.targetClass);
    }
}
