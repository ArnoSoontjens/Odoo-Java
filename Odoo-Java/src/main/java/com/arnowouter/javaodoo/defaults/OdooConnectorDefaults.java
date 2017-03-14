/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arnowouter.javaodoo.defaults;

/**
 *
 * @author Arno
 */
public class OdooConnectorDefaults {

    // ERRORS
    public static final String ERROR_XML_RPC = "XML-RPC error";    
    public static final String ERROR_MALFORMED_URL = "Malformed URL Exception";
    public static final String ERROR_UNEXPECTED = "Unexpected Error";
    
    // ENDPOINTS
    public static final String COMMON_ENDPOINT = "/xmlrpc/2/common";
    public static final String OBJECT_ENDPOINT = "/xmlrpc/2/object";
    
    // RPC FUNCTIONS
    public static final String EXECUTE = "execute_kw";
    
    // ACTIONS
    public final static String ACTION_AUTHENTICATE = "authenticate";
    public final static String ACTION_VERSION_INFO = "version";
    public final static String ACTION_SEARCH = "search";
    public final static String ACTION_READ = "read";
    public final static String ACTION_SEARCH_READ = "search_read";
    public final static String ACTION_WRITE = "create";
    
    public final static String PROTOCOL_HTTP = "http";
    public final static String PROTOCOL_HTTPS = "https";
    
    // OTHER
    public final static int DEFAULT_ODOO_PORT = 8080;
    public final static String SEARCH_FOR_FIELDS = "fields";
}
