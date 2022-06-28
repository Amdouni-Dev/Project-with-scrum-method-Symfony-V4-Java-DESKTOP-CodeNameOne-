package edu.esprit.lib.persistence.sqlbuilder.query;

import edu.esprit.lib.persistence.sqlbuilder.DbContext;
import edu.esprit.lib.persistence.sqlbuilder.InvalidQueryException;
import org.apache.commons.lang.StringEscapeUtils;

import java.util.*;
import java.util.Map.Entry;
import java.util.logging.Logger;

public class Update {
    private final DbContext dbContext;

    private boolean terminated = false;

    private String table;

    private final Map<String, Object> assignments;

    private final Collection<String> conditions;

    public Update(DbContext dbContext) {
        this.dbContext = dbContext;
        this.dbContext.append("UPDATE ");
        this.assignments = new LinkedHashMap<>();
        this.conditions = new LinkedList<>();
    }

    public Update(DbContext dbContext, String table) {
        this(dbContext);
        this.table = table;
    }

    public Update table(String table) {
        this.table = table;
        return this;
    }

    public Update set(String column, Object value) {
        assignments.put(column, value);
        return this;
    }

    public Update where(String condition) {
        conditions.add(condition);
        return this;
    }

    public Update and(String condition) {
        conditions.add(condition);
        return this;
    }
    private final transient Logger log;

    {
        this.log = Logger.getLogger(getClass().getName());
    }

    private void terminate() {
        if (assignments.isEmpty()) throw new InvalidQueryException(
                "Not contains SET statements!"
        );

        if (!terminated) {
            dbContext.append(String.format("`%s`", table)).appendLine(" SET");
            Iterator<Entry<String, Object>> iter = assignments.entrySet().iterator();

            while (iter.hasNext()) {
                Entry<String, Object> assignment = iter.next();
                if(assignment == null) {
                    log.warning("Assignment is null");
                    continue;
                }
                else if (assignment.getValue() == null) {
                    dbContext
                            .append(String.format("`%s` = NULL ", assignment.getKey()));
                } else if (assignment.getValue() instanceof String || assignment.getValue() instanceof Date) {
                    dbContext
                            .append(String.format("`%s`", assignment.getKey()))
                            .append(" = ")
                            .append("'")
                            .append(StringEscapeUtils.escapeSql(assignment.getValue().toString()))
                            .append("'");
                } else {
                    dbContext
                            .append(String.format("`%s`", assignment.getKey()))
                            .append(" = ")
                            .append(assignment.getValue().toString());
                }


                if (iter.hasNext()) {
                    dbContext.append(",").newLine();
                }
            }

            if (!conditions.isEmpty()) {
                dbContext.newLine().append("WHERE ");

                Iterator<String> conditionIter = conditions.iterator();

                while (conditionIter.hasNext()) {
                    String condition = conditionIter.next();
                    dbContext.append(condition);

                    if (conditionIter.hasNext()) {
                        dbContext.newLine().append("AND ");
                    }
                }
            }
            terminated = true;
        }

    }

    @Override
    public String toString() {
        terminate();
        return dbContext.toString();
    }
}
