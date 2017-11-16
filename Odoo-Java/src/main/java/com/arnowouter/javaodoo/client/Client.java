/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arnowouter.javaodoo.client;

import com.arnowouter.javaodoo.defaults.ConnectorDefaults;
import com.arnowouter.javaodoo.util.DatabaseParams;
import com.arnowouter.javaodoo.util.VersionInfo;
import de.timroes.axmlrpc.XMLRPCClient;
import de.timroes.axmlrpc.XMLRPCException;
import de.timroes.axmlrpc.XMLRPCServerException;
import java.net.MalformedURLException;
import java.net.URL;
import static java.util.Collections.emptyList;
import java.util.Map;

/**
 *
 * @author Arno
 */
public class Client {
    
    XMLRPCClient commonClient;
    XMLRPCClient objectClient;
    
    public Client(URL url) throws MalformedURLException {
        this(url, false);
    }
    
    public Client(URL url, boolean ignoreInvalidSSL) throws MalformedURLException{
        URL objectClientURL = new URL(url.toString()+ ConnectorDefaults.OBJECT_ENDPOINT);
        URL commonClientURL = new URL(url.toString()+ ConnectorDefaults.COMMON_ENDPOINT);
        commonClient = createClient(commonClientURL, ignoreInvalidSSL);
        objectClient = createClient(objectClientURL, ignoreInvalidSSL);
    }
    
    public Client(String protocol, String hostname, boolean HTTPS) throws MalformedURLException {
        this(protocol, hostname, HTTPS, false);
    }
    
    public Client(String protocol, String hostName, boolean HTTPS, boolean ignoreInvalidSSL) throws MalformedURLException {
        int port = changePortIfHTTPS(HTTPS);
        URL commonClientURL = new URL(protocol,hostName,port, ConnectorDefaults.COMMON_ENDPOINT);
        URL objectClientURL = new URL(protocol,hostName,port, ConnectorDefaults.OBJECT_ENDPOINT);
        objectClient = createClient(objectClientURL, ignoreInvalidSSL);
        commonClient = createClient(commonClientURL, ignoreInvalidSSL);
    }
    
    public Client(String protocol, String hostName, int connectionPort) throws MalformedURLException {
        this(protocol, hostName, connectionPort, false);
    }

    public Client(String protocol, String hostName, int connectionPort, boolean ignoreInvalidSSL) throws MalformedURLException {
        commonClient = createClient(new URL(protocol, hostName, connectionPort, ConnectorDefaults.COMMON_ENDPOINT), ignoreInvalidSSL);
        objectClient = createClient(new URL(protocol, hostName, connectionPort, ConnectorDefaults.OBJECT_ENDPOINT), ignoreInvalidSSL);
    }
    
    private XMLRPCClient createClient(URL url, boolean ignoreInvalidSSL) throws MalformedURLException {
        XMLRPCClient client;
        if(ignoreInvalidSSL) {
            client = ClientFactory.createUnsecureClient(url);
        } else {
            client = ClientFactory.createClient(url);
        }
        return client;
    }
    
    public int authenticate(DatabaseParams dbParams) throws XMLRPCException, ClassCastException {
        int userID = -1;
        Object userIDobj = commonClient.call(ConnectorDefaults.ACTION_AUTHENTICATE,
                dbParams.getDatabaseName(),
                dbParams.getDatabaseLogin(),
                dbParams.getDatabasePassword(),
                emptyList()
        );
        if(userIDobj instanceof Boolean) {
            throw new XMLRPCException("Could not authenticate to server. Check credentials!");
        } 
        userID = (int) userIDobj;
        return userID;
    }
    
    public Object updateGeoLocation(Object[] params) throws XMLRPCException {
        return objectClient.call("exec_workflow", params);
    }
    
    public VersionInfo getVersion() throws XMLRPCException {
        return new VersionInfo(commonClient.call(ConnectorDefaults.ACTION_VERSION_INFO));
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
        return (int) objectClient.call(ConnectorDefaults.EXECUTE, params);
    }
    
    public Map<String,String> callToStartTestDatabase(URL url) throws XMLRPCException {
        XMLRPCClient client = new XMLRPCClient(url);
        Map<String, String> info = (Map<String,String>) client.call("start");
        return info;
    }
    
    private Object[] executeCall(Object[] params) throws XMLRPCException {
        return (Object[]) objectClient.call(ConnectorDefaults.EXECUTE, params);
    }
    
    private int changePortIfHTTPS(boolean HTTPS) {
        int port = ConnectorDefaults.DEFAULT_HTTP_PORT;
        if(HTTPS){
            port = ConnectorDefaults.DEFAULT_HTTPS_PORT;
        }
        return port;
    }
}
