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
    
    public static OdooClient createUnsecureClient(URL url) throws MalformedURLException {
        return createClient(url, true);
    }
    
    public static OdooClient createClient(URL url) throws MalformedURLException {
        return createClient(url, false);
    }
    
    private static OdooClient createClient(URL url, boolean ignoreInvalidSSL) throws MalformedURLException {  
        OdooClient odooClient; 
        if(ignoreInvalidSSL){
            odooClient = new OdooClient(url,XMLRPCClient.FLAGS_SSL_IGNORE_INVALID_CERT | XMLRPCClient.FLAGS_SSL_IGNORE_INVALID_HOST); 
        } else {
            odooClient = new OdooClient(url);
        }
         
        return odooClient;
    }
}
 