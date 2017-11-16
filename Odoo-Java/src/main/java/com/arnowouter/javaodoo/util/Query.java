/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arnowouter.javaodoo.util;

import static java.util.Arrays.asList;

/**
 *
 * @author Arno
 */
public class Query {
    
    public static final String EQUALS = "=";
    public static final String BIGGER_THAN = ">";
    public static final String SMALLER_THAN = "<";
    
    private Object[] query;
    private String operator;
    private String field;
    private Object searched;
    
    public Query(String fieldToBeSearched, String operator, Object searchedForObject) {
        this.operator = operator;
        this.field = fieldToBeSearched;
        this.searched = searchedForObject;
        Object[] q = {
            asList(this.field,this.operator, this.searched)
        };
        query = q;
    }
    
    public Query(Object[] query) {
        this.query = query;
    }
    
    public Object[] getQueryObject() {
        return query;
    }   

    @Override
    public String toString() {
        return "OdooQuery{" + "query=" + query + ", operator=" + operator + ", field=" + field + ", searched=" + searched + '}';
    }
    
}
