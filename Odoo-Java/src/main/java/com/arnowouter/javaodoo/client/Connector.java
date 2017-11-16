/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arnowouter.javaodoo.client;

import com.arnowouter.javaodoo.defaults.ConnectorDefaults;
import com.arnowouter.javaodoo.exceptions.ConnectorException;
import com.arnowouter.javaodoo.exceptions.ExceptionMessages;
import com.arnowouter.javaodoo.util.DatabaseParams;
import com.arnowouter.javaodoo.util.VersionInfo;
import de.timroes.axmlrpc.XMLRPCException;
import java.net.MalformedURLException;
import java.net.URL;
import static java.util.Arrays.asList;
import java.util.HashMap;
import java.util.Map;
import com.arnowouter.javaodoo.util.Query;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.arnowouter.javaodoo.IConnector;
/**
 *
 * @author  Arno Soontjens
 * @see     https://github.com/ArnoSoontjens
 */


public class Connector implements IConnector {
    
    private Client odooClient;
    
    private String protocol;
    private String hostName;
    private int connectionPort;
    private DatabaseParams dbParams;
    
    private int odooUserId;

    public Connector() {
    }
    
    public Connector(String hostName) throws MalformedURLException{
        this(hostName, false);
    }
    
    public Connector(String hostName, boolean ignoreInvalidSSL) throws MalformedURLException {
        URL newURL = new URL(hostName);
        odooClient = new Client(newURL,ignoreInvalidSSL);
    }
    
    public Connector(String protocol, String hostName, int connectionPort) throws ConnectorException {
        this(protocol,hostName,connectionPort,false);
    }
    
    public Connector(String protocol, String hostName, int connectionPort, boolean ignoreInvalidSSL) 
            throws ConnectorException 
    {
        this.protocol = protocol;
        this.hostName = hostName;
        this.connectionPort = connectionPort;
        try {
            URL newURL = new URL(protocol,hostName,String.valueOf(connectionPort));
            odooClient = new Client(newURL,ignoreInvalidSSL);
        } catch (MalformedURLException ex) {
            throw new ConnectorException(ex.getMessage(), ex);
        }
    }
    
    public Connector(String hostName,DatabaseParams dbParams) throws MalformedURLException {
        this(hostName,dbParams,false);
    }
    
    public Connector(String hostName,DatabaseParams dbParams, boolean ignoreInvalidSSL) throws MalformedURLException {
        URL newURL = new URL(hostName);
        this.dbParams = dbParams;
        odooClient = new Client(newURL, ignoreInvalidSSL);
    }
    
    public Connector(String protocol, String hostName, int connectionPort, DatabaseParams dbParams) throws ConnectorException {
        this(protocol,hostName,connectionPort,false);
    }
    
    public Connector(String protocol, String hostName, int connectionPort, DatabaseParams dbParams, boolean ignoreInvalidSSL) 
            throws ConnectorException 
    {
        this.protocol = protocol;
        this.hostName = hostName;
        this.connectionPort = connectionPort;
        this.dbParams = dbParams;
        this.odooUserId = -1;
        createClient(ignoreInvalidSSL);
    }
  
    public Connector(String protocol, String hostName, int connectionPort, String databaseName, String databaseLogin, String databasePassword, boolean ignoreInvalidSSL) 
            throws ConnectorException 
    {
        this.protocol = protocol;
        this.hostName = hostName;
        this.connectionPort = connectionPort;
        this.dbParams = new DatabaseParams(databaseName, databaseLogin, databasePassword);
        this.odooUserId = -1;
        createClient(ignoreInvalidSSL);
    }
    
    @Override
    public Map<String,String> setupTestDataBase(URL url) {
        Map<String, String> info = null;
        try {
            Client client = new Client(url);
            info = client.callToStartTestDatabase(url);
        } catch (MalformedURLException | XMLRPCException ex) {
            System.out.println(ex.getMessage());
        }
        return info;
    }
            
    @Override
    public int authenticate() throws ConnectorException {
        try {
            odooUserId = odooClient.authenticate(dbParams);
            return odooUserId;
        } catch (XMLRPCException ex) {
            throw new ConnectorException(ex.getMessage(), ex);
        }
    }
    
    @Override
    public VersionInfo getVersion() throws ConnectorException {
        try {
            return odooClient.getVersion();
        } catch (XMLRPCException ex) {
            throw new ConnectorException(ex.getMessage(), ex);
        }
    }
    
    @Override
    public Object[] getAllFieldsForModel(String model) throws ConnectorException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    @Override
    public Object geoLocalize(int id) throws ConnectorException {
       /* Object[] idsToUpdate = new Object[ids.length];
        for(int i=0;i<ids.length;i++) {
            idsToUpdate[i] = ids[i];
        }*/
        
        try {
            Object[] params = {
                dbParams.getDatabaseName(),
                odooUserId,
                dbParams.getDatabasePassword(),
                "res.partner",
                ConnectorDefaults.ACTION_UPDATE_LOCATION,
                asList(id)
            };

            return (Object) odooClient.updateGeoLocation(params);
        } catch (XMLRPCException ex) {
            System.out.println(ex.getMessage());
            throw new ConnectorException(ex.getMessage(), ex);
        }
    }
    
    
    @Override
    public int createRecord(String model, HashMap<String, String> dataToWrite) throws ConnectorException {
        if(!isAuthenticated()) throw new ConnectorException(ExceptionMessages.EX_MSG_NOT_AUTHENTENTICATED);
        try {
            Object[] params = {
                dbParams.getDatabaseName(),
                odooUserId,
                dbParams.getDatabasePassword(),
                model,
                ConnectorDefaults.ACTION_CREATE_RECORD,
                asList(dataToWrite)
            };
            
            return (int) odooClient.write(params);
        } catch (XMLRPCException ex) {
            throw new ConnectorException(ex.getMessage(), ex);
        }
    }
    
    @Override
    public Object[] read(String model, int[] requestedIds) throws ConnectorException {
        return read(model, requestedIds, new Object[0]);
    }
    
    @Override
    public Object[] read(String model, int[] requestedIds, Object[] requestedFields) throws ConnectorException {
        if(!isAuthenticated()) throw new ConnectorException(ExceptionMessages.EX_MSG_NOT_AUTHENTENTICATED);
        Object[] idsToRead = new Object[requestedIds.length];
        for(int i=0;i<requestedIds.length;i++) {
            idsToRead[i] = requestedIds[i];
        }
        
        try {
            Object[] params = {
                dbParams.getDatabaseName(),
                odooUserId,
                dbParams.getDatabasePassword(),
                model,
                ConnectorDefaults.ACTION_READ,
                asList(asList(idsToRead)),
                new HashMap() {{
                    put(ConnectorDefaults.ODOO_FIELDS, asList(requestedFields));
                }}
            };

            return (Object[]) odooClient.read(params);
        } catch (XMLRPCException ex) {
            throw new ConnectorException(ex.getMessage(), ex);
        }
    }
    
    @Override
    public int count(String model, Query query) throws ConnectorException {
        return count(model, query.getQueryObject());
    }
    
    @Override
    public int count(String model, Object[] query) throws ConnectorException {
        //TODO: implement this
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public int[] search(String model, Query query) throws ConnectorException {
        return search(model, query.getQueryObject());
    }

    @Override
    public int[] search(String model, Object[] query) throws ConnectorException {
        try {
            Object[] params = {
                dbParams.getDatabaseName(),
                odooUserId,
                dbParams.getDatabasePassword(),
                model,
                ConnectorDefaults.ACTION_SEARCH,
                asList(asList(query))
                    //TODO: implement offset and limit (pagination)
            };
            return odooClient.search(params);
        } catch (XMLRPCException ex) {
            throw new ConnectorException(ex.getMessage(), ex);
        }
    }

    @Override
    public Object[] searchAndRead(String model, Object[] requestedFields) throws ConnectorException {
        return searchAndRead(model, new Object[0],requestedFields);
    }
    
    @Override
    public Object[] searchAndRead(String model, Query query, Object[] requestedFields) throws ConnectorException {
        return searchAndRead(model, query.getQueryObject(), requestedFields);
    }
    
    @Override
    public Object[] searchAndRead(String model, Object[] query, Object[] requestedFields) throws ConnectorException {
        if(!isAuthenticated()) throw new ConnectorException(ExceptionMessages.EX_MSG_NOT_AUTHENTENTICATED);
        try {  
            Object[] params = new Object[]{
                dbParams.getDatabaseName(),
                odooUserId,
                dbParams.getDatabasePassword(),
                model,
                ConnectorDefaults.ACTION_SEARCH_READ,
                asList(asList(query)),
                new HashMap() {{
                    put(ConnectorDefaults.ODOO_FIELDS, asList(requestedFields));
                }}
            };
            return (Object[]) odooClient.searchAndRead(params);
        } catch (XMLRPCException ex) {
            throw new ConnectorException(ex.getMessage(), ex);
        }
    }

    @Override
    public int updateRecord(String model, HashMap<String, String> dataToUpdate) throws ConnectorException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteRecords(String model, Object[] idsToBeDeleted) throws ConnectorException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private boolean isAuthenticated() {
        return odooUserId != -1;
    }
    
    private void createClient(boolean ignoreInvalidSSL) throws ConnectorException {
        try {
            odooClient = new Client(protocol, hostName, connectionPort, ignoreInvalidSSL);
        } catch (MalformedURLException ex) {
            throw new ConnectorException(ex.getMessage(), ex);
        }
    }

    public void setProtocol(String protocol) {this.protocol = protocol;}
    public void setHostName(String hostName) {this.hostName = hostName;}
    public void setConnectionPort(int connectionPort) {this.connectionPort = connectionPort;}
    @Override
    public void setDbParams(DatabaseParams dbParams) {this.dbParams = dbParams;}
    public void setOdooUserId(int odooUserId) {this.odooUserId = odooUserId;}

    public String getProtocol() {return protocol;}
    public String getHostName() {return hostName;}
    public int getConnectionPort() {return connectionPort;}
    public DatabaseParams getDbParams() {return dbParams;}
    public int getOdooUserId() {return odooUserId;}
}
