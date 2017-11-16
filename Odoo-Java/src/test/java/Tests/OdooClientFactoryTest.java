/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tests;

import com.arnowouter.javaodoo.client.ClientFactory;
import com.arnowouter.javaodoo.defaults.ConnectorDefaults;
import de.timroes.axmlrpc.XMLRPCClient;
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
        XMLRPCClient client = null;
        try {
            client = ClientFactory.createClient(new URL(protocolHTTPS,hostName,ConnectorDefaults.ODOO_DEFAULT_PORT ,ConnectorDefaults.COMMON_ENDPOINT));
        } catch (MalformedURLException ex) {
            fail(ex.getMessage());
        }
        assertNotNull(client);
        assertEquals(XMLRPCClient.class, client.getClass());
    }
    
    @Test
    public void shouldCreateUnsecureOdooClient() {
        XMLRPCClient client = null;
        try {
            client = ClientFactory.createUnsecureClient(new URL(protocolHTTPS,hostName,ConnectorDefaults.ODOO_DEFAULT_PORT ,ConnectorDefaults.COMMON_ENDPOINT));
        } catch (MalformedURLException ex) {
            fail(ex.getMessage());
        }
        assertNotNull(client);
        assertEquals(XMLRPCClient.class, client.getClass());
    }
}
