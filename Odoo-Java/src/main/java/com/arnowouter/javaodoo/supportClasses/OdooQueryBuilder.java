
package com.arnowouter.javaodoo.supportClasses;

import com.arnowouter.javaodoo.exceptions.OdooExceptionMessages;
import com.arnowouter.javaodoo.exceptions.OdooQueryException;
/**
 *
 * @author Arno
 */
public class OdooQueryBuilder {
    
    private String field;
    private String value;
    private String operator;
    
    public OdooQueryBuilder() {
    }
   
    public OdooQueryBuilder searchField(String fieldName) {
        this.field = fieldName;
        return this;
    }
    
    public OdooQueryBuilder forValueBiggerThan(String value) {
        this.value = value;
        this.operator = OdooQuery.BIGGER_THAN;
        return this;
    }
    
    public OdooQueryBuilder forValueSmallerThan(String value) {
        this.value = value;
        this.operator = OdooQuery.SMALLER_THAN;
        return this;
    }
    
    public OdooQueryBuilder forValueEqualTo(int value) {
        String valueString = value+"";
        return forValueEqualTo(valueString);
    }
    
    public OdooQueryBuilder forValueEqualTo(String value) {
        this.value = value;
        this.operator = OdooQuery.EQUALS;
        return this;
    }
    
    public OdooQuery buildEmptyQuery() {
        Object[] q ={}; 
        return new OdooQuery(q);
    }
    
    public OdooQuery build() throws OdooQueryException {
        performNullChecks();
        return new OdooQuery(field, operator, value);
    }
    
    private void performNullChecks() throws OdooQueryException {
        if(operator == null) throw new OdooQueryException(OdooExceptionMessages.EX_MSG_NO_QUERY_OPERATOR_SET);
        if(field == null) throw new OdooQueryException(OdooExceptionMessages.EX_MSG_NO_QUERY_FIELD_SET);
        if(value == null) throw new OdooQueryException(OdooExceptionMessages.EX_MSG_NO_QUERY_VALUE_SET);
    }
    
}
