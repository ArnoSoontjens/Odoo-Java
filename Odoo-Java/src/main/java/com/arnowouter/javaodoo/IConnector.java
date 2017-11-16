/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arnowouter.javaodoo;

import com.arnowouter.javaodoo.exceptions.ConnectorException;
import com.arnowouter.javaodoo.util.DatabaseParams;
import com.arnowouter.javaodoo.util.Query;
import com.arnowouter.javaodoo.util.VersionInfo;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Arno
 */
public interface IConnector {
    public Map<String,String> setupTestDataBase(URL url);
    public int authenticate() throws ConnectorException;
    public Object geoLocalize(int id) throws ConnectorException;
    public VersionInfo getVersion() throws ConnectorException;
    public Object[] getAllFieldsForModel(String model) throws ConnectorException;
    public Object[] read(String model, int[] requestedIds) throws ConnectorException;
    public Object[] read(String model, int[] requestedIds, Object[] requestedFields) throws ConnectorException;
    public int[] search(String model, Query query) throws ConnectorException;
    public int[] search(String model, Object[] query) throws ConnectorException;
    public int count(String model, Query query) throws ConnectorException;
    public int count(String model, Object[] query) throws ConnectorException;
    public Object[] searchAndRead(String model, Object[] requestedFields) throws ConnectorException;
    public Object[] searchAndRead(String model, Query query, Object[] requestedFields) throws ConnectorException;
    public Object[] searchAndRead(String model, Object[] query, Object[] requestedFields) throws ConnectorException;
    public int createRecord(String model, HashMap<String, String> dataToCreate) throws ConnectorException;
    public int updateRecord(String model, HashMap<String, String> dataToUpdate) throws ConnectorException;
    public void deleteRecords(String model, Object[] idsToBeDeleted) throws ConnectorException;
    public void setDbParams(DatabaseParams dbParams);
}
