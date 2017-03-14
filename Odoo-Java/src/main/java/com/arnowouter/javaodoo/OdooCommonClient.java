/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arnowouter.javaodoo;

import com.arnowouter.javaodoo.defaults.OdooDefaults;
import com.arnowouter.javaodoo.supportClasses.OdooDatabaseParams;
import de.timroes.axmlrpc.XMLRPCException;
import java.net.MalformedURLException;
import static java.util.Collections.emptyList;

/**
 *
 * @author Arno
 */
public class OdooCommonClient {
    OdooClient client;
    
    public OdooCommonClient(String protocol, String hostName) throws MalformedURLException {
        client = OdooClientFactory.createClient(protocol, hostName, OdooDefaults.DEFAULT_ODOO_PORT, OdooDefaults.COMMON_ENDPOINT);
    }
    
    public OdooCommonClient(String protocol, String hostName, int connectionPort) throws MalformedURLException{
        client = OdooClientFactory.createClient(protocol,hostName,connectionPort,OdooDefaults.COMMON_ENDPOINT);
    }
    
    public OdooCommonClient(String protocol, String hostName, int connectionPort, boolean ignoreInvalidSSL) throws MalformedURLException {
        if(ignoreInvalidSSL) {
            client = OdooClientFactory.createUnsecureClient(protocol, hostName, connectionPort, OdooDefaults.COMMON_ENDPOINT);
        } else {
            client = OdooClientFactory.createClient(protocol, hostName, connectionPort, OdooDefaults.COMMON_ENDPOINT);
        }
    }
    
    private int authenticate(OdooDatabaseParams dbParams) throws XMLRPCException {
        return (int) client.call(
                OdooDefaults.ACTION_AUTHENTICATE,
                dbParams.getDatabaseName(),
                dbParams.getDatabaseLogin(),
                dbParams.getDatabasePassword(),
                emptyList()
        );
    }
    
    private int getVersion() {
        return 0;
    }
}
