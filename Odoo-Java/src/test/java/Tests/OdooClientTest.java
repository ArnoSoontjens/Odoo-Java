/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tests;

import com.arnowouter.javaodoo.client.OdooClient;
import com.arnowouter.javaodoo.client.OdooClientFactory;
import com.arnowouter.javaodoo.defaults.OdooConnectorDefaults;
import com.arnowouter.javaodoo.supportClasses.OdooDatabaseParams;
import com.arnowouter.javaodoo.supportClasses.OdooVersionInfo;
import de.timroes.axmlrpc.XMLRPCClient;
import de.timroes.axmlrpc.XMLRPCException;
import java.net.MalformedURLException;
import java.net.URL;
import static java.util.Arrays.asList;

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
public class OdooClientTest {
    static String hostName;
    static String protocolHTTP;
    static String protocolHTTPS;
    static int port;
    static XMLRPCClient xmlrpcClient;
    static OdooClient odooClient;
    static String odooHostName,databaseName, databaseLogin, databasePassword;
    static OdooDatabaseParams dbParams;
    static URL clientURL;
    static int userID;
    
    public OdooClientTest() {
    }
    
    @BeforeClass
    public static void setUpClass() throws MalformedURLException, XMLRPCException {
        hostName = "demo.odoo.com";
        protocolHTTP = "http";
        protocolHTTPS = "https";
        port = 8080;
        URL url = new URL(protocolHTTP, hostName,80,"/start");
        xmlrpcClient = OdooClientFactory.createClient(url);
        
        Map<String, String> info = setUpNewTestDatabase(); 
        odooHostName = info.get("host");
        databaseLogin = info.get("user");
        databaseName = info.get("database");
        databasePassword = info.get("password");

        System.out.println("URL: " + odooHostName);
        System.out.println("Database: " + databaseName);
        System.out.println("User: " + databaseLogin);
        System.out.println("Password: " + databasePassword);

        clientURL = new URL(odooHostName);
        odooClient = new OdooClient(clientURL);
        odooClient = new OdooClient(clientURL);
        dbParams = new OdooDatabaseParams(databaseName, databaseLogin, databasePassword);
        userID = odooClient.authenticate(dbParams);
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
    public void shouldCreateClientFromURL() throws MalformedURLException {
        URL url = new URL(protocolHTTPS,hostName,port,"");
        OdooClient client = new OdooClient(url);
        assertNotNull(client);
    }
    
    @Test
    public void shouldCreateClientThreeParams() throws MalformedURLException {
        OdooClient client = new OdooClient(protocolHTTPS,hostName, false);
        assertNotNull(client);
        
        client = new OdooClient(protocolHTTPS,hostName, true);
        assertNotNull(client);
        
        client = new OdooClient(protocolHTTPS,hostName, port);
        assertNotNull(client);
    }
    
    @Test
    public void shouldCreateclientFourParams() throws MalformedURLException {
        OdooClient client = new OdooClient(protocolHTTPS,hostName,port,true);
        assertNotNull(client);
        
        client = new OdooClient(protocolHTTPS,hostName,port,false);
        assertNotNull(client);
    }

    @Test
    public void shouldGetVersionInformation() throws XMLRPCException {
        OdooVersionInfo versionInfo = odooClient.getVersion();
        assertNotNull(versionInfo);
        System.out.println(versionInfo);
    }
    
    @Test 
    public void shouldAuthenticate() throws MalformedURLException, XMLRPCException{
        int userID = odooClient.authenticate(dbParams);
        System.out.println("User ID: " + userID);
        assertNotEquals(-1, userID);
        assertTrue(userID>0);
    }
    
    @Test
    public void shouldSearchAndReturnEmptyArray() throws XMLRPCException {
        Object[] query = {};
        Object[] params = {
                dbParams.getDatabaseName(),
                userID,
                dbParams.getDatabasePassword(),
                "not.existing.model",
                OdooConnectorDefaults.ACTION_SEARCH,
                asList(asList(query))
        };
        int[] ids = odooClient.search(params);
        assertTrue(ids.length == 0);
    }
    @Test
    public void shouldSearchWithoutQuery() throws XMLRPCException {
        Object[] query = {};
        
        Object[] params = {
                dbParams.getDatabaseName(),
                userID,
                dbParams.getDatabasePassword(),
                "sale.order",
                OdooConnectorDefaults.ACTION_SEARCH,
                asList(asList(query))
        };
        int[] ids = odooClient.search(params);
        for(int i=0;i<ids.length;i++){
            System.out.println(ids[i]);
        }
    }
    
    @Test
    public void shouldReadWithoutQuery() throws XMLRPCException{
        Object[] ids = {1,2,4,5,484,4484,1454,484,4541,5564};
        Object[] query = {};
        Object[] params = {
                dbParams.getDatabaseName(),
                userID,
                dbParams.getDatabasePassword(),
                "sale.order",
                OdooConnectorDefaults.ACTION_READ,
                asList(asList(ids))
        };
        Object[] result = odooClient.read(params);
        for(Object res : result){
            System.out.println("Result: " + res.toString());
        }
        assertNotNull(result);
        assertTrue(result.length>0);
    }
    
    private static Map<String,String> setUpNewTestDatabase() throws XMLRPCException {
        Map<String,String> info = (Map<String,String>) xmlrpcClient.call("start");
        return info;
    }
}