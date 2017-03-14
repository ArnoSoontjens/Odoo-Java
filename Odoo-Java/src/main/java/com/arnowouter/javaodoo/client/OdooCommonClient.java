/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arnowouter.javaodoo.client;

import com.arnowouter.javaodoo.defaults.OdooConnectorDefaults;
import com.arnowouter.javaodoo.supportClasses.OdooDatabaseParams;
import de.timroes.axmlrpc.XMLRPCException;
import java.net.MalformedURLException;
import java.net.URL;
import static java.util.Collections.emptyList;

/**
 *
 * @author Arno
 */
public class OdooCommonClient {
    OdooClient client;
    
    public OdooCommonClient(String protocol, String hostName, boolean HTTPS) throws MalformedURLException {        
        int port = changePortIfHTTPS(HTTPS);
        URL clientURL = new URL(protocol,hostName,port, OdooConnectorDefaults.COMMON_ENDPOINT);
        client = OdooClientFactory.createClient(clientURL);
    }
    
    public OdooCommonClient(URL url) throws MalformedURLException {
        URL ClientURL = new URL(url.toString() + OdooConnectorDefaults.COMMON_ENDPOINT);
        client = OdooClientFactory.createClient(ClientURL);
    }
    
    public OdooCommonClient(String protocol, String hostName, int connectionPort) throws MalformedURLException{
        URL clientURL = new URL(protocol,hostName, connectionPort, OdooConnectorDefaults.COMMON_ENDPOINT);
        client = OdooClientFactory.createClient(clientURL);
    }
    
    public OdooCommonClient(String protocol, String hostName, int connectionPort, boolean ignoreInvalidSSL) throws MalformedURLException {
        if(ignoreInvalidSSL) {
            client = OdooClientFactory.createUnsecureClient(new URL(protocol, hostName, connectionPort, OdooConnectorDefaults.COMMON_ENDPOINT));
        } else {
            client = OdooClientFactory.createClient(new URL(protocol, hostName, connectionPort, OdooConnectorDefaults.COMMON_ENDPOINT));
        }
    }
    
    public int authenticate(OdooDatabaseParams dbParams) throws XMLRPCException {
        return (int) client.call(OdooConnectorDefaults.ACTION_AUTHENTICATE,
                dbParams.getDatabaseName(),
                dbParams.getDatabaseLogin(),
                dbParams.getDatabasePassword(),
                emptyList()
        );
    }
    
    public Object getVersion() throws XMLRPCException {
        return (Object) client.call(OdooConnectorDefaults.ACTION_VERSION_INFO);
    }

    @Override
    public String toString() {
        return "OdooCommonClient{" + "client=" + client + '}';
    }
    
    private int changePortIfHTTPS(boolean HTTPS) {
        int port = OdooConnectorDefaults.DEFAULT_HTTP_PORT;
        if(HTTPS){
            port = OdooConnectorDefaults.DEFAULT_HTTPS_PORT;
        }
        return port;
    }
    
}
