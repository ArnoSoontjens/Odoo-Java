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


/**
 *
 * @author  Arno Soontjens
 * @see     https://github.com/ArnoSoontjens
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
    public Object[] getVersion() throws OdooConnectorException {
        try {
            return (Object[]) commonClient.getVersion();
        } catch (XMLRPCException ex) {
            throw new OdooConnectorException(ex.getMessage(), ex);
        }
    }
    
    @Override
    public Object[] getAllFieldsForModel(String model) throws OdooConnectorException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public int createRecord(String model, HashMap<String, String> dataToWrite) throws OdooConnectorException {
        try {
            Object[] params = {
                dbParams.getDatabaseName(),
                odooUserId,
                dbParams.getDatabasePassword(),
                model,
                OdooConnectorDefaults.ACTION_CREATE_RECORD,
                asList(dataToWrite)
            };
            
            return (int) objectClient.write(params);
        } catch (XMLRPCException ex) {
            throw new OdooConnectorException(ex.getMessage(), ex);
        }
    }

    @Override
    public Object[] read(String model, Object[] requestedIds, Object[] requestedFields) throws OdooConnectorException {
        if(!isAuthenticated()) throw new OdooConnectorException(OdooExceptionMessages.EX_MESSAGE_NOT_AUTHENTENTICATED);
        
        try {
            Object[] params = new Object[] {
                    dbParams.getDatabaseName(),
                    odooUserId,
                    dbParams.getDatabasePassword(),
                    model,
                    OdooConnectorDefaults.ACTION_READ,
                    asList(asList(requestedIds)),
                    new HashMap() {{
                        put(OdooConnectorDefaults.ODOO_FIELDS, asList(requestedFields));
                    }}
            };

            return (Object[]) objectClient.read(params);
        } catch (XMLRPCException ex) {
            throw new OdooConnectorException(ex.getMessage(), ex);
        }
    }
    
    @Override
    public int count(String model, Object[] query) throws OdooConnectorException {
        //TODO: implement this
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object[] search(String model, Object[] query) throws OdooConnectorException {
        try {
            Object[] params = new Object[] {
                dbParams.getDatabaseName(),
                odooUserId,
                dbParams.getDatabasePassword(),
                model,
                OdooConnectorDefaults.ACTION_SEARCH,
                asList(asList(query))
                //TODO: implement offset and limit (pagination)
            };
            return (Object[]) objectClient.search(params);
        } catch (XMLRPCException ex) {
            throw new OdooConnectorException(ex.getMessage(), ex);
        }
    }

    @Override
    public Object[] searchAndRead(String model, Object[] requestedFields) throws OdooConnectorException {
        return searchAndRead(model, new Object[0],requestedFields);
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
                    put(OdooConnectorDefaults.ODOO_FIELDS, asList(requestedFields));
                }}
            };
            return (Object[]) objectClient.searchAndRead(params);
        } catch (XMLRPCException ex) {
            throw new OdooConnectorException(ex.getMessage(), ex);
        }
    }

    @Override
    public int updateRecord(String model, HashMap<String, String> dataToUpdate) throws OdooConnectorException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteRecords(String model, Object[] idsToBeDeleted) throws OdooConnectorException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private boolean isAuthenticated() {
        return odooUserId != -1;
    }


    
}
