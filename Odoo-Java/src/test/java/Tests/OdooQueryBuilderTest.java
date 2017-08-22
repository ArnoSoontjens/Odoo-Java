/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tests;

import com.arnowouter.javaodoo.exceptions.OdooQueryException;
import com.arnowouter.javaodoo.supportClasses.OdooQuery;
import com.arnowouter.javaodoo.supportClasses.OdooQueryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Arno
 */
public class OdooQueryBuilderTest {
    
    static OdooQueryBuilder queryBuilder;
    
    public OdooQueryBuilderTest() {
    }
    
    @Before
    public void setUp() {
        queryBuilder = new OdooQueryBuilder();  
    }
    
    @After
    public void tearDown() {
    }

    @Test (expected = OdooQueryException.class)
    public void shouldThrowBecauseOperatorNotSet() throws OdooQueryException {
        OdooQuery query = queryBuilder.build();
    }
    
    @Test (expected = OdooQueryException.class)
    public void shouldThrowBecauseFieldNotSet() throws OdooQueryException {
        OdooQuery query = queryBuilder.forValueSmallerThan("10").build();
    }
    
    @Test (expected = OdooQueryException.class)
    public void shouldThrowBecauseNullValue() throws OdooQueryException {
        OdooQuery query = queryBuilder.forValueEqualTo(null).build();
    }
    
    @Test
    public void shouldReturnValidQuery() throws OdooQueryException {
        OdooQuery query = queryBuilder.searchField("a_field").forValueBiggerThan("10").build();
        assertNotNull(query);
        assertEquals(OdooQuery.class, query.getClass());
        assertEquals(Object[].class, query.getQueryObject().getClass());  
    }
    
    @Test
    public void shouldReturnValidQueryUsingInt() throws OdooQueryException {
        OdooQuery query = queryBuilder.searchField("a_field").forValueEqualTo(10).build();
        assertNotNull(query);
        assertEquals(OdooQuery.class, query.getClass());
        assertEquals(Object[].class, query.getQueryObject().getClass());  
    }
    
    @Test
    public void shouldReturnValidQueryUsingString() throws OdooQueryException {
        OdooQuery query = queryBuilder.searchField("a_field").forValueEqualTo("10").build();
        assertNotNull(query);
        assertEquals(OdooQuery.class, query.getClass());
        assertEquals(Object[].class, query.getQueryObject().getClass());  
    }
    
    @Test
    public void shouldBuildEmptyQuery() {
        OdooQuery query = queryBuilder.buildEmptyQuery();
        assertNotNull(query);
        assertTrue(query.getQueryObject().length == 0);
    }
}
