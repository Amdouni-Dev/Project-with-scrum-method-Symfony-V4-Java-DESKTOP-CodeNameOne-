package edu.esprit.lib.persistence.sqlbuilder.query.select;

import edu.esprit.lib.persistence.sqlbuilder.DbContext;
import edu.esprit.lib.persistence.sqlbuilder.query.TerminalClause;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class From implements TerminalClause {
    private DbContext dbContext;

    private boolean terminated = false;

    private final List<String> tables = new ArrayList<>();

    From(DbContext dbContext) {
        this.dbContext = dbContext;
        this.dbContext.appendLine("FROM");
    }

    public From table(String table) {
        // if table has space, split it
        if (table.contains(" ")) {
            String[] tableParts = table.split(" ");
            this.tables.add(String.format("`%s`", tableParts[0]) + " " + tableParts[1]);
            return this;
        }
        tables.add(String.format("`%s`", table));
        return this;
    }

    public From tables(String... tables) {
        for (String table : tables) {
            table(table);
        }
        return this;
    }

    public From select(String selectQuery, String alias) {
        this.tables.add("(" + selectQuery + ") " + alias);
        return this;
    }

    public Where where() {
        terminate();
        return new Where(dbContext);
    }

    public Where where(String condition) {
        terminate();
        return new Where(dbContext, condition);
    }

    public GroupBy groupBy() {
        terminate();
        return new GroupBy(dbContext);
    }

    public GroupBy groupBy(String... columns) {
        terminate();
        return new GroupBy(dbContext, columns);
    }

    public Join leftOuterJoin(String condition) {
        terminate();
        return new LeftOuterJoin(dbContext, condition);
    }

    public Join rightOuterJoin(String condition) {
        terminate();
        return new RightOuterJoin(dbContext, condition);
    }

    public Join innerJoin(String condition) {
        terminate();
        return new InnerJoin(dbContext, condition);
    }

    public Join join(String condition) {
        terminate();
        return new SimpleJoin(dbContext, condition);
    }

    public OrderBy orderBy() {
        terminate();
        return new OrderBy(dbContext);
    }

    public Limit limit(int start, int size) {
        terminate();
        return new Limit(dbContext, start, size);
    }

    public <E> List<E> list(RowMapper<E> rowMapper) throws SQLException {
        terminate();
        return dbContext.list(rowMapper);
    }

    public <E> E single(RowMapper<E> rowMapper) throws SQLException {
        terminate();
        return dbContext.single(rowMapper);
    }

    @Override
    public String toString() {
        terminate();
        return dbContext.toString();
    }

    private void terminate() {
        if (!terminated) {
            final String newLine = System.getProperty("line.separator");
            this.dbContext.appendLine(String.join("," + newLine, tables));
            this.dbContext.appendLine(newLine);
            terminated = true;
        }
    }
}
