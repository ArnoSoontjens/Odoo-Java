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


public class OdooConnectorImpl implements OdooConnector {
    
    OdooCommonClient commonClient;
    OdooObjectClient objectClient;
    
    String protocol;
    String hostName;
    int connectionPort;
    OdooDatabaseParams dbParams;
    
    public OdooConnectorImpl(String protocol, String hostName, int connectionPort, OdooDatabaseParams dbParams) 
            throws OdooConnectorException 
    {
        this.protocol = protocol;
        this.hostName = hostName;
        this.connectionPort = connectionPort;
        this.dbParams = dbParams;
        
        createCommonAndObjectClient();
    }
  
    public OdooConnectorImpl(String protocol, String hostName, int connectionPort, String databaseName, String databaseLogin, String databasePassword) 
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

    @Override
    public int write() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void read() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void search() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void searchAndRead() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
