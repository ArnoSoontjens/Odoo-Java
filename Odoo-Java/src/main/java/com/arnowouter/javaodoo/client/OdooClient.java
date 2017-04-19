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
import java.util.Map;

/**
 *
 * @author Arno
 */
public class OdooClient {
    
    XMLRPCClient commonClient;
    XMLRPCClient objectClient;
    
    public OdooClient(URL url) throws MalformedURLException {
        this(url, false);
    }
    
    public OdooClient(URL url, boolean ignoreInvalidSSL) throws MalformedURLException{
        URL objectClientURL = new URL(url.toString()+ OdooConnectorDefaults.OBJECT_ENDPOINT);
        URL commonClientURL = new URL(url.toString()+ OdooConnectorDefaults.COMMON_ENDPOINT);
        commonClient = createClient(commonClientURL, ignoreInvalidSSL);
        objectClient = createClient(objectClientURL, ignoreInvalidSSL);
    }
    
    public OdooClient(String protocol, String hostname, boolean HTTPS) throws MalformedURLException {
        this(protocol, hostname, HTTPS, false);
    }
    
    public OdooClient(String protocol, String hostName, boolean HTTPS, boolean ignoreInvalidSSL) throws MalformedURLException {
        int port = changePortIfHTTPS(HTTPS);
        URL commonClientURL = new URL(protocol,hostName,port, OdooConnectorDefaults.COMMON_ENDPOINT);
        URL objectClientURL = new URL(protocol,hostName,port, OdooConnectorDefaults.OBJECT_ENDPOINT);
        objectClient = createClient(objectClientURL, ignoreInvalidSSL);
        commonClient = createClient(commonClientURL, ignoreInvalidSSL);
    }
    
    public OdooClient(String protocol, String hostName, int connectionPort) throws MalformedURLException {
        this(protocol, hostName, connectionPort, false);
    }

    public OdooClient(String protocol, String hostName, int connectionPort, boolean ignoreInvalidSSL) throws MalformedURLException {
        commonClient = createClient(new URL(protocol, hostName, connectionPort, OdooConnectorDefaults.COMMON_ENDPOINT), ignoreInvalidSSL);
        objectClient = createClient(new URL(protocol, hostName, connectionPort, OdooConnectorDefaults.OBJECT_ENDPOINT), ignoreInvalidSSL);
    }
    
    private XMLRPCClient createClient(URL url, boolean ignoreInvalidSSL) throws MalformedURLException {
        XMLRPCClient client;
        if(ignoreInvalidSSL) {
            client = OdooClientFactory.createUnsecureClient(url);
        } else {
            client = OdooClientFactory.createClient(url);
        }
        return client;
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
    
    public Object updateGeoLocation(Object[] params) throws XMLRPCException {
        return objectClient.call("exec_workflow", params);
    }
    
    public OdooVersionInfo getVersion() throws XMLRPCException {
        return new OdooVersionInfo(commonClient.call(OdooConnectorDefaults.ACTION_VERSION_INFO));
    }
  
    public int[] search(Object[] params) throws XMLRPCException {
        Object[] result = executeCall(params);
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
    
    public Map<String,String> callToStartTestDatabase(URL url) throws XMLRPCException {
        XMLRPCClient client = new XMLRPCClient(url);
        Map<String, String> info = (Map<String,String>) client.call("start");
        return info;
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
