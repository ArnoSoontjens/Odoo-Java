/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arnowouter.javaodoo;

import com.arnowouter.javaodoo.OdooClient;
import com.arnowouter.javaodoo.OdooClientFactory;
import com.arnowouter.javaodoo.defaults.OdooDefaults;
import java.net.MalformedURLException;

/**
 *
 * @author Arno
 */
public class OdooObjectClient {
    
    OdooClient client;
    
    public OdooObjectClient(String protocol, String hostName) throws MalformedURLException {
        client = OdooClientFactory.createClient(protocol, hostName, OdooDefaults.DEFAULT_ODOO_PORT, OdooDefaults.OBJECT_ENDPOINT);
    }
    
    public OdooObjectClient(String protocol, String hostName, int connectionPort) throws MalformedURLException{
        client = OdooClientFactory.createClient(protocol,hostName,connectionPort,OdooDefaults.OBJECT_ENDPOINT);
    }
    
    public OdooObjectClient(String protocol, String hostName, int connectionPort, boolean ignoreInvalidSSL) throws MalformedURLException {
        if(ignoreInvalidSSL) {
            client = OdooClientFactory.createUnsecureClient(protocol, hostName, connectionPort, OdooDefaults.OBJECT_ENDPOINT);
        } else {
            client = OdooClientFactory.createClient(protocol, hostName, connectionPort, OdooDefaults.OBJECT_ENDPOINT);
        }
    }
    
    private void search() {
        
    }
    
    private void read() {
        
    }
    
    private void searchAndRead() {
        
    }
    
    private void write(){
        
    }
}
