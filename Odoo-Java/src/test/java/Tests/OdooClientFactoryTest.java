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
public class OdooClientFactoryTest {
    
    public OdooClientFactoryTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
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
            client = OdooClientFactory.createClient("http","www.isoltechnics.com",OdooConnectorDefaults.ODOO_DEFAULT_PORT ,OdooConnectorDefaults.COMMON_ENDPOINT);
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
            client = OdooClientFactory.createUnsecureClient("http","www.isoltechnics.com",OdooConnectorDefaults.ODOO_DEFAULT_PORT ,OdooConnectorDefaults.COMMON_ENDPOINT);
        } catch (MalformedURLException ex) {
            fail(ex.getMessage());
        }
        assertNotNull(client);
        assertEquals(OdooClient.class, client.getClass());
    }
    
    @Test(expected = MalformedURLException.class)
    public void shouldThrowBecauseBadURL() throws MalformedURLException {
        OdooClientFactory.createClient("NotRight","www.isoltechnics.com",OdooConnectorDefaults.ODOO_DEFAULT_PORT ,OdooConnectorDefaults.COMMON_ENDPOINT);
    }
    
    @Test(expected = MalformedURLException.class)
    public void shouldThrowBecauseNegativePortNumber() throws MalformedURLException {
        OdooClientFactory.createClient("http","www.isoltechnics.com",-1,OdooConnectorDefaults.COMMON_ENDPOINT);
    }
}
