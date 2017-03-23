/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arnowouter.javaodoo.client;

import com.arnowouter.javaodoo.defaults.OdooConnectorDefaults;
import com.arnowouter.javaodoo.supportClasses.OdooDatabaseParams;
import com.arnowouter.javaodoo.supportClasses.OdooVersionInfo;
import de.timroes.axmlrpc.XMLRPCClient;
import de.timroes.axmlrpc.XMLRPCException;
import java.net.MalformedURLException;
import java.net.URL;
import static java.util.Collections.emptyList;

/**
 *
 * @author Arno
 */
public class OdooClient {
    
    XMLRPCClient commonClient;
    XMLRPCClient objectClient;
    
    public OdooClient(URL url) throws MalformedURLException{
        URL objectClientURL = new URL(url.toString()+ OdooConnectorDefaults.OBJECT_ENDPOINT);
        URL commonClientURL = new URL(url.toString()+ OdooConnectorDefaults.COMMON_ENDPOINT);
        commonClient = OdooClientFactory.createClient(commonClientURL);
        objectClient = OdooClientFactory.createClient(objectClientURL);
    }
    
    public OdooClient(String protocol, String hostName, boolean HTTPS) throws MalformedURLException {
        int port = changePortIfHTTPS(HTTPS);
        URL commonClientURL = new URL(protocol,hostName,OdooConnectorDefaults.DEFAULT_HTTPS_PORT, OdooConnectorDefaults.COMMON_ENDPOINT);
        URL objectClientURL = new URL(protocol,hostName,OdooConnectorDefaults.DEFAULT_HTTPS_PORT, OdooConnectorDefaults.OBJECT_ENDPOINT);
        commonClient = OdooClientFactory.createClient(commonClientURL);
        objectClient = OdooClientFactory.createClient(objectClientURL);
    }
    
    public OdooClient(String protocol, String hostName, int connectionPort) throws MalformedURLException{
        URL commonClientURL = new URL(protocol,hostName,connectionPort,OdooConnectorDefaults.COMMON_ENDPOINT);
        URL objectClientURL = new URL(protocol,hostName,connectionPort,OdooConnectorDefaults.OBJECT_ENDPOINT);
        commonClient = OdooClientFactory.createClient(commonClientURL);
        objectClient = OdooClientFactory.createClient(objectClientURL);
    }

    public OdooClient(String protocol, String hostName, int connectionPort, boolean ignoreInvalidSSL) throws MalformedURLException {
        if(ignoreInvalidSSL) {
            commonClient = OdooClientFactory.createUnsecureClient(new URL(protocol, hostName, connectionPort, OdooConnectorDefaults.COMMON_ENDPOINT));
            objectClient = OdooClientFactory.createUnsecureClient(new URL(protocol, hostName, connectionPort, OdooConnectorDefaults.OBJECT_ENDPOINT));
        } else {
            commonClient = OdooClientFactory.createClient(new URL(protocol, hostName, connectionPort, OdooConnectorDefaults.COMMON_ENDPOINT));
            objectClient = OdooClientFactory.createClient(new URL(protocol, hostName, connectionPort, OdooConnectorDefaults.OBJECT_ENDPOINT));
        }
    }
    
    public int authenticate(OdooDatabaseParams dbParams) throws XMLRPCException, ClassCastException {
        int userID = (int) commonClient.call(
                OdooConnectorDefaults.ACTION_AUTHENTICATE,
                dbParams.getDatabaseName(),
                dbParams.getDatabaseLogin(),
                dbParams.getDatabasePassword(),
                emptyList()
        );
        return userID;
    }
    
    public OdooVersionInfo getVersion() throws XMLRPCException {
        return new OdooVersionInfo(commonClient.call(OdooConnectorDefaults.ACTION_VERSION_INFO));
    }
  
    public int[] search(Object[] params) {
        Object[] result;
        try {
            result = executeCall(params);
        } catch (XMLRPCException ex) {
            return new int[0];
        }
        int[] ids = new int[result.length];
        int i=0;
        for(Object id : result){
            ids[i] = (int) id;
            i++;
        }
        return ids;
    }
    
    public Object[] read(Object[] params) throws XMLRPCException {
        return executeCall(params);
    }
    
    public Object[] searchAndRead(Object[] params) throws XMLRPCException {
        return executeCall(params);
    }
    
    public int write(Object[] params) throws XMLRPCException{
        return (int) objectClient.call(OdooConnectorDefaults.EXECUTE, params);
    }
    
    private Object[] executeCall(Object[] params) throws XMLRPCException {
        return (Object[]) objectClient.call(OdooConnectorDefaults.EXECUTE, params);
    }
    
    private int changePortIfHTTPS(boolean HTTPS) {
        int port = OdooConnectorDefaults.DEFAULT_HTTP_PORT;
        if(HTTPS){
            port = OdooConnectorDefaults.DEFAULT_HTTPS_PORT;
        }
        return port;
    }
}
