/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arnowouter.javaodoo.client;

import com.arnowouter.javaodoo.defaults.OdooConnectorDefaults;
import de.timroes.axmlrpc.XMLRPCException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author Arno
 */
public class OdooObjectClient {
    
    OdooClient client;
    
    public OdooObjectClient(String protocol, String hostName, boolean HTTPS) throws MalformedURLException {
        int port = changePortIfHTTPS(HTTPS);
        URL clientURL = new URL(protocol,hostName,OdooConnectorDefaults.DEFAULT_HTTPS_PORT, OdooConnectorDefaults.COMMON_ENDPOINT);
        client = OdooClientFactory.createClient(clientURL);
    }
    
    public OdooObjectClient(URL url) throws MalformedURLException{
        URL clientURL = new URL(url.toString()+ OdooConnectorDefaults.OBJECT_ENDPOINT);
        client = OdooClientFactory.createClient(clientURL);
    }
    
    public OdooObjectClient(String protocol, String hostName, int connectionPort) throws MalformedURLException{
        client = OdooClientFactory.createClient(new URL(protocol,hostName,connectionPort,OdooConnectorDefaults.OBJECT_ENDPOINT));
    }

    public OdooObjectClient(String protocol, String hostName, int connectionPort, boolean ignoreInvalidSSL) throws MalformedURLException {
        if(ignoreInvalidSSL) {
            client = OdooClientFactory.createUnsecureClient(new URL(protocol, hostName, connectionPort, OdooConnectorDefaults.OBJECT_ENDPOINT));
        } else {
            client = OdooClientFactory.createClient(new URL(protocol, hostName, connectionPort, OdooConnectorDefaults.OBJECT_ENDPOINT));
        }
    }
    
    public Object[] search(Object[] params) throws XMLRPCException {
        return executeCall(params);
    }
    
    public Object[] read(Object[] params) throws XMLRPCException {
        return executeCall(params);
    }
    
    public Object[] searchAndRead(Object[] params) throws XMLRPCException {
        return executeCall(params);
    }
    
    public int write(Object[] params) throws XMLRPCException{
        return (int) client.call(OdooConnectorDefaults.EXECUTE, params);
    }
    
    private Object[] executeCall(Object[] params) throws XMLRPCException {
        return (Object[]) client.call(OdooConnectorDefaults.EXECUTE, params);
    }
    
    private int changePortIfHTTPS(boolean HTTPS) {
        int port = OdooConnectorDefaults.DEFAULT_HTTP_PORT;
        if(HTTPS){
            port = OdooConnectorDefaults.DEFAULT_HTTPS_PORT;
        }
        return port;
    }
}
