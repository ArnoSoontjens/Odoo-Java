/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tests;

import com.arnowouter.javaodoo.client.Connector;
import com.arnowouter.javaodoo.exceptions.ConnectorException;
import com.arnowouter.javaodoo.util.DatabaseParams;
import com.arnowouter.javaodoo.util.VersionInfo;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import com.arnowouter.javaodoo.exceptions.QueryException;
import com.arnowouter.javaodoo.util.Query;
import com.arnowouter.javaodoo.util.QueryBuilder;
import static java.util.Arrays.asList;
import com.arnowouter.javaodoo.IConnector;

/**
 *
 * @author Arno
 */
public class OdooConnectorTest {
    static IConnector testDBConnector;
    static IConnector odooConnector;
    static DatabaseParams dbParams;
    static int userID;
    static String odooHostName, databaseLogin, databaseName, databasePassword;
    
    @BeforeClass
    public static void setUpClass() throws MalformedURLException, ConnectorException {
        testDBConnector = new Connector();
        String hostName = "demo.odoo.com";
        String protocolHTTP = "http";

        URL url = new URL(protocolHTTP, hostName,80,"/start");
        Map<String, String> info = setUpNewTestDatabase(url); 
        
        odooHostName = info.get("host");
        databaseLogin = info.get("user");
        databaseName = info.get("database");
        databasePassword = info.get("password");
        
        System.out.println("URL: " + odooHostName);
        System.out.println("Database: " + databaseName);
        System.out.println("User: " + databaseLogin);
        System.out.println("Password: " + databasePassword);

        dbParams = new DatabaseParams(databaseName, databaseLogin, databasePassword);
        odooConnector = new Connector(odooHostName, false);
        odooConnector.setDbParams(dbParams);

        System.out.println(odooConnector.getVersion());
        userID = odooConnector.authenticate();
    }
    
    private static Map<String,String> setUpNewTestDatabase(URL url) {
        Map<String,String> info = testDBConnector.setupTestDataBase(url);
        return info;
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void shouldGetVersionInformation() throws ConnectorException {
        VersionInfo versionInfo = odooConnector.getVersion();
        assertNotNull(versionInfo);
        System.out.println(versionInfo);
    }
    
    @Test 
    public void shouldAuthenticate() throws ConnectorException{
        int userID = odooConnector.authenticate();
        System.out.println("User ID: " + userID);
        assertNotEquals(-1, userID);
        assertTrue(userID>0);
    }
    
    @Test @Ignore
    public void shouldSearchAndReturnEmptyArray()throws ConnectorException {
        Object[] query = {};
        int[] ids = odooConnector.search("not.existing.model",query);
        assertTrue(ids.length == 0);
    }
    
    @Test
    public void shouldSearchWithSelfDefinedEmptyQuery() throws ConnectorException {
        Object[] query = {};
        int[] ids = odooConnector.search("sale.order",query);
        for(int i=0;i<ids.length;i++){
            System.out.println(ids[i]);
        }
        assertNotNull(ids);
        assertTrue(ids.length >= 1);
    }
    
    @Test
    public void shouldSearchWithBuiltEmptyQuery() throws ConnectorException {
        QueryBuilder builder = new QueryBuilder();
        Query query = builder.buildEmptyQuery();
        
        int[] ids = odooConnector.search("sale.order",query);
        for(int i=0;i<ids.length;i++){
            System.out.println("With query using builder: " + ids[i]);
        }
        assertNotNull(ids);
        assertTrue(ids.length >= 1);
    }
    
    @Test
    public void shouldRead() throws ConnectorException {
        int[] ids = {1,2,4,5,484,4484,1454,484,4541,5564};

        Object[] result = odooConnector.read("sale.order",ids);
        for(Object res : result){
            System.out.println("Result: " + res.toString());
        }
        assertNotNull(result);
        assertTrue(result.length>0);
    }
    
    @Test
    public void shouldSearchWithSelfDefinedQuery() throws ConnectorException {
        Object[] query = {
            asList("id", ">", "10")
        };
        
        int[] result = odooConnector.search("sale.order",query);
        for(int res : result){
            System.out.println("Saleorder (self defined query): " + res);
        }
        assertNotNull(result);
        assertTrue(result.length > 1);
    }
    
    @Test
    public void shouldSearchWithSelfDefinedEqualQuery() throws ConnectorException {
        Object[] query = {
            asList("id", "=", "10")
        };
        
        int[] result = odooConnector.search("sale.order",query);
        for(int res : result){
            System.out.println("Saleorder (self defined query): " + res);
        }
        assertNotNull(result);
        assertTrue(result.length == 1);
    }
    
    @Test
    public void shouldReadWithBuiltQuery() throws QueryException, ConnectorException {
        QueryBuilder builder = new QueryBuilder();
        Query query = builder.searchField("id").forValueBiggerThan("10").build();
        
        int[] result = odooConnector.search("sale.order",query);
        for(int res : result){
            System.out.println("Saleorder With query using builder: " + res);
        }
        assertNotNull(result);
        assertTrue(result.length > 1);
    }
    
    @Test
    public void shouldFindSaleOrderWithId10UsingInt() throws QueryException, ConnectorException {
        QueryBuilder builder = new QueryBuilder();
        Query query = builder.searchField("id").forValueEqualTo(10).build();
        
        int[] result = odooConnector.search("sale.order",query);
        for(int res : result){
            System.out.println("Saleorder With query using builder: " + res);
        }
        assertNotNull(result);
        assertTrue(result.length == 1);
    }
    
    @Test
    public void shouldFindSaleOrderWithId10UsingString() throws QueryException, ConnectorException {
        QueryBuilder builder = new QueryBuilder();
        Query query = builder.searchField("id").forValueEqualTo("10").build();
        
        int[] result = odooConnector.search("sale.order",query);
        for(int res : result){
            System.out.println("Saleorder (query with builder): " + res);
        }
        assertNotNull(result);
        assertTrue(result.length == 1);
    }
}
