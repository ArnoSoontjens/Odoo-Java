/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arnowouter.javaodoo.client;

import de.timroes.axmlrpc.XMLRPCClient;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author Arno
 */
public class OdooClientFactory {
    
    public static XMLRPCClient createUnsecureClient(URL url) throws MalformedURLException {
        return createClient(url, true);
    }
    
    public static XMLRPCClient createClient(URL url) throws MalformedURLException {
        return createClient(url, false);
    }
    
    private static XMLRPCClient createClient(URL url, boolean ignoreInvalidSSL) throws MalformedURLException {  
        XMLRPCClient odooClient; 
        if(ignoreInvalidSSL){
            odooClient = new XMLRPCClient(url,XMLRPCClient.FLAGS_SSL_IGNORE_INVALID_CERT | XMLRPCClient.FLAGS_SSL_IGNORE_INVALID_HOST); 
        } else {
            odooClient = new XMLRPCClient(url);
        }
         
        return odooClient;
    }
}
 