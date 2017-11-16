/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arnowouter.javaodoo.exceptions;

/**
 *
 * @author Arno Soontjens
 */
public class ExceptionMessages {
    public static final String EX_MSG_NOT_AUTHENTENTICATED = "Not authenticated. Please call the 'authenticate' function first.";
    public static final String EX_MSG_NEGATIVE_CONNECTION_PORT = "The connection port cannot be negative";
    public static final String EX_MSG_NO_QUERY_OPERATOR_SET = "Query operator was not set. Use the '.equals()', 'biggerThan()' or 'smallerThan()' functions";
    public static final String EX_MSG_NO_QUERY_FIELD_SET = "Field to search was not set. Use '.searchField(fieldName)' function to set a field to search.";
    public static final String EX_MSG_NO_QUERY_VALUE_SET = "Value was null!";
}
