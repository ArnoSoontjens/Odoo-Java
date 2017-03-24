/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tests;

import com.arnowouter.javaodoo.OdooConnector;
import com.arnowouter.javaodoo.client.OdooConnectorImpl;
import com.arnowouter.javaodoo.exceptions.OdooConnectorException;
import com.arnowouter.javaodoo.supportClasses.OdooDatabaseParams;
import com.arnowouter.javaodoo.supportClasses.OdooVersionInfo;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Arno
 */
public class OdooConnectorTest {
    static OdooConnector testDBConnector;
    static OdooConnector odooConnector;
    static OdooDatabaseParams dbParams;
    static int userID;
    static String odooHostName, databaseLogin, databaseName, databasePassword;
    
    @BeforeClass
    public static void setUpClass() throws MalformedURLException, OdooConnectorException {
        testDBConnector = new OdooConnectorImpl();
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

        dbParams = new OdooDatabaseParams(databaseName, databaseLogin, databasePassword);
        odooConnector = new OdooConnectorImpl(odooHostName);
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
    public void shouldGetVersionInformation() throws OdooConnectorException {
        OdooVersionInfo versionInfo = odooConnector.getVersion();
        assertNotNull(versionInfo);
        System.out.println(versionInfo);
    }
    
    @Test 
    public void shouldAuthenticate() throws OdooConnectorException{
        int userID = odooConnector.authenticate();
        System.out.println("User ID: " + userID);
        assertNotEquals(-1, userID);
        assertTrue(userID>0);
    }
    
    @Test
    public void shouldSearchAndReturnEmptyArray()throws OdooConnectorException {
        Object[] query = {};
        int[] ids = odooConnector.search("not.existing.model",query);
        assertTrue(ids.length == 0);
    }
    @Test
    public void shouldSearchWithoutQuery() throws OdooConnectorException {
        Object[] query = {};
        int[] ids = odooConnector.search("sale.order",query);
        for(int i=0;i<ids.length;i++){
            System.out.println(ids[i]);
        }
    }
    
    @Test
    public void shouldReadWithoutQuery() throws OdooConnectorException {
        int[] ids = {1,2,4,5,484,4484,1454,484,4541,5564};
        Object[] query = {};

        Object[] result = odooConnector.read("sale.order",ids);
        for(Object res : result){
            System.out.println("Result: " + res.toString());
        }
        assertNotNull(result);
        assertTrue(result.length>0);
    }
}
