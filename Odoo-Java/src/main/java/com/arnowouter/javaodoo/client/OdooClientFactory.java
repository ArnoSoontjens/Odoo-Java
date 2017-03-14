/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arnowouter.javaodoo.client;

import com.arnowouter.javaodoo.exceptions.OdooExceptionMessages;
import de.timroes.axmlrpc.XMLRPCClient;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author Arno
 */
public class OdooClientFactory {
    public static OdooClient createUnsecureClient(String protocol, String hostName, int connectionPort, String endPoint) throws MalformedURLException {
        return createClient(protocol, hostName, connectionPort, endPoint, true);
    }
    
    public static OdooClient createClient(String protocol, String hostName, int connectionPort, String endPoint) throws MalformedURLException {
        return createClient(protocol, hostName, connectionPort, endPoint, false);
    }
    
    private static OdooClient createClient(String protocol, String hostName, int connectionPort, String endPoint, boolean ignoreInvalidSSL) throws MalformedURLException {  
        if(connectionPort <=0) throw new MalformedURLException(OdooExceptionMessages.EX_MSG_NEGATIVE_CONNECTION_PORT);
        
        URL url = new URL(protocol, hostName, connectionPort, endPoint);
        OdooClient odooClient; 
        if(ignoreInvalidSSL){
            odooClient = new OdooClient(url,XMLRPCClient.FLAGS_SSL_IGNORE_INVALID_CERT | XMLRPCClient.FLAGS_SSL_IGNORE_INVALID_HOST); 
        } else {
            odooClient = new OdooClient(url);
        }
         
        return odooClient;
    }
}
 