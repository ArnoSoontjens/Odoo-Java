/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arnowouter.javaodoo.client;

import com.arnowouter.javaodoo.defaults.OdooConnectorDefaults;
import com.arnowouter.javaodoo.exceptions.OdooConnectorException;
import com.arnowouter.javaodoo.exceptions.OdooExceptionMessages;
import com.arnowouter.javaodoo.supportClasses.OdooDatabaseParams;
import com.arnowouter.javaodoo.supportClasses.OdooVersionInfo;
import de.timroes.axmlrpc.XMLRPCException;
import java.net.MalformedURLException;
import java.net.URL;
import static java.util.Arrays.asList;
import java.util.HashMap;
import java.util.Map;
import com.arnowouter.javaodoo.IOdooConnector;
import com.arnowouter.javaodoo.supportClasses.OdooQuery;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author  Arno Soontjens
 * @see     https://github.com/ArnoSoontjens
 */


public class OdooConnector implements IOdooConnector {
    
    private OdooClient odooClient;
    
    private String protocol;
    private String hostName;
    private int connectionPort;
    private OdooDatabaseParams dbParams;
    
    private int odooUserId;

    public OdooConnector() {
    }
    
    public OdooConnector(String hostName) throws MalformedURLException{
        this(hostName, false);
    }
    
    public OdooConnector(String hostName, boolean ignoreInvalidSSL) throws MalformedURLException {
        URL newURL = new URL(hostName);
        odooClient = new OdooClient(newURL,ignoreInvalidSSL);
    }
    
    public OdooConnector(String protocol, String hostName, int connectionPort) throws OdooConnectorException {
        this(protocol,hostName,connectionPort,false);
    }
    
    public OdooConnector(String protocol, String hostName, int connectionPort, boolean ignoreInvalidSSL) 
            throws OdooConnectorException 
    {
        this.protocol = protocol;
        this.hostName = hostName;
        this.connectionPort = connectionPort;
        try {
            URL newURL = new URL(protocol,hostName,String.valueOf(connectionPort));
            odooClient = new OdooClient(newURL,ignoreInvalidSSL);
        } catch (MalformedURLException ex) {
            throw new OdooConnectorException(ex.getMessage(), ex);
        }
    }
    
    public OdooConnector(String hostName,OdooDatabaseParams dbParams) throws MalformedURLException {
        this(hostName,dbParams,false);
    }
    
    public OdooConnector(String hostName,OdooDatabaseParams dbParams, boolean ignoreInvalidSSL) throws MalformedURLException {
        URL newURL = new URL(hostName);
        this.dbParams = dbParams;
        odooClient = new OdooClient(newURL, ignoreInvalidSSL);
    }
    
    public OdooConnector(String protocol, String hostName, int connectionPort, OdooDatabaseParams dbParams) throws OdooConnectorException {
        this(protocol,hostName,connectionPort,false);
    }
    
    public OdooConnector(String protocol, String hostName, int connectionPort, OdooDatabaseParams dbParams, boolean ignoreInvalidSSL) 
            throws OdooConnectorException 
    {
        this.protocol = protocol;
        this.hostName = hostName;
        this.connectionPort = connectionPort;
        this.dbParams = dbParams;
        this.odooUserId = -1;
        createClient(ignoreInvalidSSL);
    }
  
    public OdooConnector(String protocol, String hostName, int connectionPort, String databaseName, String databaseLogin, String databasePassword, boolean ignoreInvalidSSL) 
            throws OdooConnectorException 
    {
        this.protocol = protocol;
        this.hostName = hostName;
        this.connectionPort = connectionPort;
        this.dbParams = new OdooDatabaseParams(databaseName, databaseLogin, databasePassword);
        this.odooUserId = -1;
        createClient(ignoreInvalidSSL);
    }
    
    @Override
    public Map<String,String> setupTestDataBase(URL url) {
        Map<String, String> info = null;
        try {
            OdooClient client = new OdooClient(url);
            info = client.callToStartTestDatabase(url);
        } catch (MalformedURLException | XMLRPCException ex) {
            System.out.println(ex.getMessage());
        }
        return info;
    }
            
    @Override
    public int authenticate() throws OdooConnectorException {
        try {
            System.out.println("####"  + odooClient.authenticate(dbParams));
            odooUserId = odooClient.authenticate(dbParams);
            return odooUserId;
        } catch (XMLRPCException ex) {
            throw new OdooConnectorException(ex.getMessage(), ex);
        }
    }
    
    @Override
    public OdooVersionInfo getVersion() throws OdooConnectorException {
        try {
            return odooClient.getVersion();
        } catch (XMLRPCException ex) {
            throw new OdooConnectorException(ex.getMessage(), ex);
        }
    }
    
    @Override
    public Object[] getAllFieldsForModel(String model) throws OdooConnectorException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    @Override
    public Object geoLocalize(int id) throws OdooConnectorException {
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
                OdooConnectorDefaults.ACTION_UPDATE_LOCATION,
                asList(id)
            };

            return (Object) odooClient.updateGeoLocation(params);
        } catch (XMLRPCException ex) {
            System.out.println(ex.getMessage());
            throw new OdooConnectorException(ex.getMessage(), ex);
        }
    }
    
    
    @Override
    public int createRecord(String model, HashMap<String, String> dataToWrite) throws OdooConnectorException {
        if(!isAuthenticated()) throw new OdooConnectorException(OdooExceptionMessages.EX_MSG_NOT_AUTHENTENTICATED);
        try {
            Object[] params = {
                dbParams.getDatabaseName(),
                odooUserId,
                dbParams.getDatabasePassword(),
                model,
                OdooConnectorDefaults.ACTION_CREATE_RECORD,
                asList(dataToWrite)
            };
            
            return (int) odooClient.write(params);
        } catch (XMLRPCException ex) {
            throw new OdooConnectorException(ex.getMessage(), ex);
        }
    }
    
    @Override
    public Object[] read(String model, int[] requestedIds) throws OdooConnectorException {
        return read(model, requestedIds, new Object[0]);
    }
    
    @Override
    public Object[] read(String model, int[] requestedIds, Object[] requestedFields) throws OdooConnectorException {
        if(!isAuthenticated()) throw new OdooConnectorException(OdooExceptionMessages.EX_MSG_NOT_AUTHENTENTICATED);
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
                OdooConnectorDefaults.ACTION_READ,
                asList(asList(idsToRead)),
                new HashMap() {{
                    put(OdooConnectorDefaults.ODOO_FIELDS, asList(requestedFields));
                }}
            };

            return (Object[]) odooClient.read(params);
        } catch (XMLRPCException ex) {
            throw new OdooConnectorException(ex.getMessage(), ex);
        }
    }
    
    @Override
    public int count(String model, OdooQuery query) throws OdooConnectorException {
        return count(model, query.getQueryObject());
    }
    
    @Override
    public int count(String model, Object[] query) throws OdooConnectorException {
        //TODO: implement this
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public int[] search(String model, OdooQuery query) throws OdooConnectorException {
        return search(model, query.getQueryObject());
    }

    @Override
    public int[] search(String model, Object[] query) throws OdooConnectorException {
        try {
            Object[] params = {
                dbParams.getDatabaseName(),
                odooUserId,
                dbParams.getDatabasePassword(),
                model,
                OdooConnectorDefaults.ACTION_SEARCH,
                asList(asList(query))
                    //TODO: implement offset and limit (pagination)
            };
            return odooClient.search(params);
        } catch (XMLRPCException ex) {
            throw new OdooConnectorException(ex.getMessage(), ex);
        }
    }

    @Override
    public Object[] searchAndRead(String model, Object[] requestedFields) throws OdooConnectorException {
        return searchAndRead(model, new Object[0],requestedFields);
    }
    
    @Override
    public Object[] searchAndRead(String model, OdooQuery query, Object[] requestedFields) throws OdooConnectorException {
        return searchAndRead(model, query.getQueryObject(), requestedFields);
    }
    
    @Override
    public Object[] searchAndRead(String model, Object[] query, Object[] requestedFields) throws OdooConnectorException {
        if(!isAuthenticated()) throw new OdooConnectorException(OdooExceptionMessages.EX_MSG_NOT_AUTHENTENTICATED);
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
            return (Object[]) odooClient.searchAndRead(params);
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
    
    private void createClient(boolean ignoreInvalidSSL) throws OdooConnectorException {
        try {
            odooClient = new OdooClient(protocol, hostName, connectionPort, ignoreInvalidSSL);
        } catch (MalformedURLException ex) {
            throw new OdooConnectorException(ex.getMessage(), ex);
        }
    }

    public void setProtocol(String protocol) {this.protocol = protocol;}
    public void setHostName(String hostName) {this.hostName = hostName;}
    public void setConnectionPort(int connectionPort) {this.connectionPort = connectionPort;}
    @Override
    public void setDbParams(OdooDatabaseParams dbParams) {this.dbParams = dbParams;}
    public void setOdooUserId(int odooUserId) {this.odooUserId = odooUserId;}

    public String getProtocol() {return protocol;}
    public String getHostName() {return hostName;}
    public int getConnectionPort() {return connectionPort;}
    public OdooDatabaseParams getDbParams() {return dbParams;}
    public int getOdooUserId() {return odooUserId;}
}
