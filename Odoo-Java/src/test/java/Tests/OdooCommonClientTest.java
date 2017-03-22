/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tests;

import com.arnowouter.javaodoo.client.OdooClient;
import com.arnowouter.javaodoo.client.OdooClientFactory;
import com.arnowouter.javaodoo.client.OdooCommonClient;
import com.arnowouter.javaodoo.defaults.OdooConnectorDefaults;
import de.timroes.axmlrpc.XMLRPCException;
import java.net.MalformedURLException;
import java.net.URL;
import static java.util.Collections.emptyList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class OdooCommonClientTest {
    static String hostName;
    static String protocolHTTP;
    static String protocolHTTPS;
    static OdooClient odooClient;
    
    static String odooHostName,databaseName, databaseLogin, databasePassword;
    
    public OdooCommonClientTest() {
    }
    
    @BeforeClass
    public static void setUpClass() throws MalformedURLException {
        hostName = "demo.odoo.com";
        protocolHTTP = "http";
        protocolHTTPS = "https";
        
        URL url = new URL(protocolHTTP, hostName,80,"/start");
        odooClient = OdooClientFactory.createClient(url);
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
    public void shouldCreateCommonClientWithDefaultPort() throws MalformedURLException {
        OdooCommonClient commonClient = new OdooCommonClient(protocolHTTPS,hostName, false);
        assertNotNull(commonClient);
    }
    
    @Test
    public void shouldCreateCommonClient() throws MalformedURLException {
        OdooCommonClient commonclient = new OdooCommonClient(protocolHTTPS, hostName, 80);
        assertNotNull(commonclient);
    }
    
    @Test
    public void shouldCreateUnsecureCommonClient() throws MalformedURLException {
        OdooCommonClient commonClient = new OdooCommonClient(protocolHTTPS, hostName, 80, true);
        assertNotNull(commonClient);
    }
    
    @Test(expected = MalformedURLException.class)
    public void shouldThrowBecauseBadProtocol() throws MalformedURLException {
        OdooCommonClient commonClient = new OdooCommonClient("Wrong", hostName,false);
    }
    
    @Test
    public void shouldGetVersionInformation() {
        try {
            Map<String, String> info = setUpNewTestDatabase(); 
            odooHostName = info.get("host");
            databaseLogin = info.get("user");
            databaseName = info.get("database");
            databasePassword = info.get("password");
            
            System.out.println("URL: " + odooHostName);
            System.out.println("Database: " + databaseName);
            System.out.println("User: " + databaseLogin);
            System.out.println("Password: " + databasePassword);
        
            URL clientURL = new URL(odooHostName);
            OdooCommonClient commonClient = new OdooCommonClient(clientURL);
            
            System.out.println(commonClient.getVersion());
        } catch (XMLRPCException | MalformedURLException ex) {
            fail(ex.getMessage());
        } 
    }
    
    private Map<String,String> setUpNewTestDatabase() throws XMLRPCException {
        Map<String,String> info = (Map<String,String>) odooClient.call("start");
        return info;
    }
}
