package edu.esprit.lib.persistence.sqlbuilder.query.select;

import edu.esprit.lib.persistence.sqlbuilder.DbContext;

import java.util.LinkedList;
import java.util.List;

public class Select {
    private DbContext dbContext;

    private final List<String> columns;

    public Select(DbContext dbContext) {
        this.dbContext = dbContext;
        this.dbContext.appendLine("SELECT");
        columns = new LinkedList<>();
    }

    public From from() {
        this.dbContext.appendLine(String.join(",", columns));
        return new From(dbContext);
    }

    public Select all() {
        append("*");
        return this;
    }

    public Select column(String column) {
        if (column.contains(".")) {
            append(column);
            return this;
        }
        append(String.format("`%s`", column.trim()));
        return this;
    }

    public Select columns(String... columns) {
        for (String column : columns) {
            column(column);
        }
        return this;
    }

    public Select count(String column) {
        append("COUNT(" + column + ")");
        return this;
    }

    private void append(String expression) {
        columns.add(expression);
    }

    @Override
    public String toString() {
        return dbContext.toString();
    }
}
