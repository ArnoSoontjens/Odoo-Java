
package com.arnowouter.javaodoo.util;

import com.arnowouter.javaodoo.exceptions.ExceptionMessages;
import com.arnowouter.javaodoo.exceptions.QueryException;
/**
 *
 * @author Arno
 */
public class QueryBuilder {
    
    private String field;
    private Object value;
    private String operator;
    
    public QueryBuilder() {
    }
   
    public QueryBuilder searchField(String fieldName) {
        this.field = fieldName;
        return this;
    }
    
    public QueryBuilder forValueBiggerThan(Object value) {
        this.value = value;
        this.operator = Query.BIGGER_THAN;
        return this;
    }
    
    public QueryBuilder forValueSmallerThan(Object value) {
        this.value = value;
        this.operator = Query.SMALLER_THAN;
        return this;
    }

    public QueryBuilder forValueEqualTo(Object value) {
        this.value = value;
        this.operator = Query.EQUALS;
        return this;
    }
    
    public Query buildEmptyQuery() {
        Object[] q ={}; 
        return new Query(q);
    }
    
    public Query build() throws QueryException {
        performNullChecks();
        return new Query(field, operator, value);
    }
    
    private void performNullChecks() throws QueryException {
        if(operator == null) throw new QueryException(ExceptionMessages.EX_MSG_NO_QUERY_OPERATOR_SET);
        if(field == null) throw new QueryException(ExceptionMessages.EX_MSG_NO_QUERY_FIELD_SET);
        if(value == null) throw new QueryException(ExceptionMessages.EX_MSG_NO_QUERY_VALUE_SET);
    }
    
}
