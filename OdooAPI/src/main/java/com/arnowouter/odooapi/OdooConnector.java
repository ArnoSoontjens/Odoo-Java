/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arnowouter.odooapi;

import de.timroes.axmlrpc.XMLRPCClient;
import de.timroes.axmlrpc.XMLRPCException;
import java.net.MalformedURLException;
import java.net.URL;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import java.util.HashMap;

/**
 *
 * @author Arno
 */


public class OdooConnector {
    
    private String odooDataBaseName;
    private String odooDataBaseLogin;
    private String odooDatabasePassword;
    
    private XMLRPCClient commonClient;
    private XMLRPCClient objectClient;
    
    public OdooConnector() {
        
    }
    
     /**
     * The authenticate function checks which userid is coupled to the account used
     * for logging in and returns that userid. This should be used for further calls to odoo.
     * 
     * @return user id
     * @throws AuthenticationFailedException 
     */
    private int authenticate() throws XMLRPCException{
        return (int) commonClient.call(
                OdooDefaults.ACTION_AUTHENTICATE,
                odooDataBaseName,
                odooDataBaseLogin,
                odooDatabasePassword,
                emptyList()
        ); 
    }
    
    private int write(String model, HashMap<String,String> dataToWrite) {
        try {
            int userID = authenticate();

            Object[] params = {
                odooDataBaseName,
                userID,
                odooDatabasePassword,
                model,
                OdooDefaults.ACTION_WRITE,
                asList(dataToWrite)
            };
            
            return (int) objectClient.call(OdooDefaults.EXECUTE, params);
        } catch (XMLRPCException ex) {
            throw new ErpException(OdooDefaults.ERROR_XML_RPC, OdooDefaults.ERP_SYSTEM_NAME, ex.getMessage());
        }
    }
    
    /**
     * READ:
     * takes ids (from a previous 'search') and returns the requested fields. 
     */
     private Object[] read(String model, Object[] requestedIds, Object[] requestedFields) throws XMLRPCException  {
       
        int userId = authenticate();

        Object[] params = new Object[] {
                odooDataBaseName,
                userId,
                odooDatabasePassword,
                model,
                OdooDefaults.ACTION_READ,
                asList(asList(requestedIds)),
                new HashMap() {{
                    put("fields", asList(requestedFields));
                }}
        };

        return(Object[]) objectClient.call(OdooDefaults.EXECUTE, params);
    }
     
    /**
     * SEARCH and READ: 
     * Performs a search and read on the same model 
     * (so we don't need to call read and search separately)
     */
    private Object[] searchAndRead(String model, Object[] requestedFields) throws XMLRPCException  {
        return searchAndRead(model, new Object[0],requestedFields);
    }

    private Object[] searchAndRead(String model, Object[] query, Object[] requestedFields) throws XMLRPCException {   
        int userId = authenticate();

        Object[] params = new Object[]{
                odooDataBaseName,
                userId,
                odooDatabasePassword,
                model,
                OdooDefaults.ACTION_SEARCH_READ,
                asList(asList(query)),
                new HashMap() {{
                    put("fields", asList(requestedFields));
                }}
        };

        return (Object[]) objectClient.call(OdooDefaults.EXECUTE, params);
        
    }
               
    private XMLRPCClient createHTTPSClient(String protocol, String hostName, String endPoint) throws MalformedURLException {
        return createClient(protocol, hostName, OdooDefaults.DEFAULT_ODOO_PORT, endPoint, true);
    }
    
    private XMLRPCClient createHTTPSClient(String protocol, String hostName, int connectionPort, String endPoint) throws MalformedURLException {
        return createClient(protocol, hostName, connectionPort, endPoint, true);
    }
    
    private XMLRPCClient createHTTPClient(String protocol, String hostName, String endPoint) throws MalformedURLException {
        return createClient(protocol, hostName, OdooDefaults.DEFAULT_ODOO_PORT, endPoint, false);
    }
    
    private XMLRPCClient createHTTPCClient(String protocol, String hostName, int connectionPort, String endPoint) throws MalformedURLException {
        return createClient(protocol, hostName, connectionPort, endPoint, false);
    }
    
    private XMLRPCClient createClient(String protocol, String hostName, int connectionPort, String endPoint, boolean ignoreInvalidSSL) throws MalformedURLException {             
        URL url = new URL(protocol, hostName, connectionPort, endPoint);
        XMLRPCClient odooClient; 
        if(ignoreInvalidSSL){
            odooClient = new XMLRPCClient(url,XMLRPCClient.FLAGS_SSL_IGNORE_INVALID_CERT | XMLRPCClient.FLAGS_SSL_IGNORE_INVALID_HOST); 
        } else {
            odooClient = new XMLRPCClient(url);
        }
         
        return odooClient;
    }
}
