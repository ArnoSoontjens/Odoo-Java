/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tests;

import com.arnowouter.javaodoo.client.OdooClient;
import com.arnowouter.javaodoo.client.OdooClientFactory;
import com.arnowouter.javaodoo.defaults.OdooConnectorDefaults;
import java.net.MalformedURLException;
import java.net.URL;
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
public class OdooClientFactoryTest {
    
    static String hostName;
    static String protocolHTTP;
    static String protocolHTTPS;
    
    public OdooClientFactoryTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        hostName = "demo3.odoo.com";
        protocolHTTP = "http";
        protocolHTTPS = "https";
        
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
    public void shouldCreateOdooClientWithDefaultPort() {
        OdooClient client = null;
        try {
            client = OdooClientFactory.createClient(new URL(protocolHTTPS,hostName,OdooConnectorDefaults.ODOO_DEFAULT_PORT ,OdooConnectorDefaults.COMMON_ENDPOINT));
        } catch (MalformedURLException ex) {
            fail(ex.getMessage());
        }
        assertNotNull(client);
        assertEquals(OdooClient.class, client.getClass());
    }
    
    @Test
    public void shouldCreateUnsecureOdooClient() {
        OdooClient client = null;
        try {
            client = OdooClientFactory.createUnsecureClient(new URL(protocolHTTPS,hostName,OdooConnectorDefaults.ODOO_DEFAULT_PORT ,OdooConnectorDefaults.COMMON_ENDPOINT));
        } catch (MalformedURLException ex) {
            fail(ex.getMessage());
        }
        assertNotNull(client);
        assertEquals(OdooClient.class, client.getClass());
    }
}
