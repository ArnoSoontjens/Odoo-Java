/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arnowouter.javaodoo;

import com.arnowouter.javaodoo.defaults.OdooConnectorDefaults;
import com.arnowouter.javaodoo.exceptions.OdooConnectorException;
import com.arnowouter.javaodoo.exceptions.OdooExceptionMessages;
import com.arnowouter.javaodoo.supportClasses.OdooDatabaseParams;
import de.timroes.axmlrpc.XMLRPCException;
import java.net.MalformedURLException;
import static java.util.Arrays.asList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Arno
 */


public class OdooConnectorImpl implements OdooConnector {
    
    private OdooCommonClient commonClient;
    private OdooObjectClient objectClient;
    
    private final String protocol;
    private final String hostName;
    private final int connectionPort;
    private final OdooDatabaseParams dbParams;
    
    private int odooUserId;
    
    public OdooConnectorImpl(String protocol, String hostName, int connectionPort, OdooDatabaseParams dbParams) 
            throws OdooConnectorException 
    {
        this.protocol = protocol;
        this.hostName = hostName;
        this.connectionPort = connectionPort;
        this.dbParams = dbParams;
        this.odooUserId = -1;
        createCommonAndObjectClient();
    }
  
    public OdooConnectorImpl(String protocol, String hostName, int connectionPort, String databaseName, String databaseLogin, String databasePassword) 
            throws OdooConnectorException 
    {
        this.protocol = protocol;
        this.hostName = hostName;
        this.connectionPort = connectionPort;
        dbParams = new OdooDatabaseParams(databaseName, databaseLogin, databasePassword);
        this.odooUserId = -1;
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
    public int authenticate() throws OdooConnectorException {
        try {
            odooUserId = commonClient.authenticate(dbParams);
            return odooUserId;
        } catch (XMLRPCException ex) {
            throw new OdooConnectorException(ex.getMessage(), ex);
        }
    }

    @Override
    public int write(String model, HashMap<String, String> dataToWrite) throws OdooConnectorException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.     
    }

    @Override
    public Object[] read(String model) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object[] search(String model) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object[] searchAndRead(String model, Object[] query, Object[] requestedFields) throws OdooConnectorException {
        if(!isAuthenticated()) throw new OdooConnectorException(OdooExceptionMessages.EX_MESSAGE_NOT_AUTHENTENTICATED);
        try {  
            Object[] params = new Object[]{
                dbParams.getDatabaseName(),
                odooUserId,
                dbParams.getDatabasePassword(),
                model,
                OdooConnectorDefaults.ACTION_SEARCH_READ,
                asList(asList(query)),
                new HashMap() {{
                    put(OdooConnectorDefaults.SEARCH_FOR_FIELDS, asList(requestedFields));
                }}
            };
            return (Object[]) objectClient.searchAndRead(OdooConnectorDefaults.EXECUTE, params);
        } catch (XMLRPCException ex) {
            throw new OdooConnectorException(ex.getMessage(), ex);
        }
    }

    private boolean isAuthenticated() {
        return odooUserId != -1;
    }
    
}
