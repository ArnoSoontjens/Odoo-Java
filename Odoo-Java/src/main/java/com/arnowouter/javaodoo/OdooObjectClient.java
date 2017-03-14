/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arnowouter.javaodoo;

import com.arnowouter.javaodoo.defaults.OdooConnectorDefaults;
import de.timroes.axmlrpc.XMLRPCException;
import java.net.MalformedURLException;

/**
 *
 * @author Arno
 */
public class OdooObjectClient {
    
    OdooClient client;
    
    public OdooObjectClient(String protocol, String hostName) throws MalformedURLException {
        client = OdooClientFactory.createClient(protocol, hostName, OdooConnectorDefaults.ODOO_DEFAULT_PORT, OdooConnectorDefaults.OBJECT_ENDPOINT);
    }
    
    public OdooObjectClient(String protocol, String hostName, int connectionPort) throws MalformedURLException{
        client = OdooClientFactory.createClient(protocol,hostName,connectionPort,OdooConnectorDefaults.OBJECT_ENDPOINT);
    }
    
    public OdooObjectClient(String protocol, String hostName, int connectionPort, boolean ignoreInvalidSSL) throws MalformedURLException {
        if(ignoreInvalidSSL) {
            client = OdooClientFactory.createUnsecureClient(protocol, hostName, connectionPort, OdooConnectorDefaults.OBJECT_ENDPOINT);
        } else {
            client = OdooClientFactory.createClient(protocol, hostName, connectionPort, OdooConnectorDefaults.OBJECT_ENDPOINT);
        }
    }
    
    public void search() {
        
    }
    
    public Object[] read(Object[] params) throws XMLRPCException {
        return (Object[]) client.call(OdooConnectorDefaults.EXECUTE, params);
    }
    
    public Object[] searchAndRead(Object[] params) throws XMLRPCException {
        return (Object[]) client.call(OdooConnectorDefaults.EXECUTE, params);
    }
    
    public void write(){
        
    }
}
