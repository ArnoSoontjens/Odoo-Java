/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arnowouter.javaodoo;

import com.arnowouter.javaodoo.exceptions.OdooConnectorException;
import com.arnowouter.javaodoo.supportClasses.OdooDatabaseParams;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Arno
 */


public class OdooConnector {
    
    OdooCommonClient commonClient;
    OdooObjectClient objectClient;
    
    String protocol;
    String hostName;
    int connectionPort;
    OdooDatabaseParams dbParams;
    
    public OdooConnector(String protocol, String hostName, int connectionPort, OdooDatabaseParams dbParams) 
            throws OdooConnectorException 
    {
        this.protocol = protocol;
        this.hostName = hostName;
        this.connectionPort = connectionPort;
        this.dbParams = dbParams;
        
        createCommonAndObjectClient();
    }
  
    public OdooConnector(String protocol, String hostName, int connectionPort, String databaseName, String databaseLogin, String databasePassword) 
            throws OdooConnectorException 
    {
        this.protocol = protocol;
        this.hostName = hostName;
        this.connectionPort = connectionPort;
        dbParams = new OdooDatabaseParams(databaseName, databaseLogin, databasePassword);
        
        createCommonAndObjectClient();
    }
    
    private void createCommonAndObjectClient() throws OdooConnectorException {
        try {
            commonClient = new OdooCommonClient(protocol, hostName, connectionPort);
            objectClient = new OdooObjectClient(protocol, hostName, connectionPort);
        } catch (MalformedURLException ex) {
            throw new OdooConnectorException(ex.getMessage(), ex);
        }
    }
}
