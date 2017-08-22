/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arnowouter.javaodoo.supportClasses;

import static java.util.Arrays.asList;

/**
 *
 * @author Arno
 */
public class OdooQuery {
    
    public static final String EQUALS = "=";
    public static final String BIGGER_THAN = ">";
    public static final String SMALLER_THAN = "<";
    
    private Object[] query;

    public OdooQuery(String fieldToBeSearched, String operator, String searchedForObject) {
        Object[] q = {
            asList(fieldToBeSearched, operator, searchedForObject)
        };
        query = q;
    }
    
    public OdooQuery(Object[] query) {
        this.query = query;
    }
    
    public Object[] getQueryObject() {
        return query;
    }   
}
