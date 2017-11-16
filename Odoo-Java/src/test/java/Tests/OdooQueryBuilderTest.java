/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tests;

import com.arnowouter.javaodoo.exceptions.QueryException;
import com.arnowouter.javaodoo.util.Query;
import com.arnowouter.javaodoo.util.QueryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Arno
 */
public class OdooQueryBuilderTest {
    
    static QueryBuilder queryBuilder;
    
    public OdooQueryBuilderTest() {
    }
    
    @Before
    public void setUp() {
        queryBuilder = new QueryBuilder();  
    }
    
    @After
    public void tearDown() {
    }

    @Test (expected = QueryException.class)
    public void shouldThrowBecauseOperatorNotSet() throws QueryException {
        Query query = queryBuilder.build();
    }
    
    @Test (expected = QueryException.class)
    public void shouldThrowBecauseFieldNotSet() throws QueryException {
        Query query = queryBuilder.forValueSmallerThan("10").build();
    }
    
    @Test (expected = QueryException.class)
    public void shouldThrowBecauseNullValue() throws QueryException {
        Query query = queryBuilder.forValueEqualTo(null).build();
    }
    
    @Test
    public void shouldReturnValidQuery() throws QueryException {
        Query query = queryBuilder.searchField("a_field").forValueBiggerThan("10").build();
        assertNotNull(query);
        assertEquals(Query.class, query.getClass());
        assertEquals(Object[].class, query.getQueryObject().getClass()); 
        System.out.println("Valid query using string value: " +query.toString());
    }
    
    @Test
    public void shouldReturnValidQueryUsingInt() throws QueryException {
        Query query = queryBuilder.searchField("a_field").forValueEqualTo(10).build();
        assertNotNull(query);
        assertEquals(Query.class, query.getClass());
        assertEquals(Object[].class, query.getQueryObject().getClass()); 
        System.out.println("Valid query using int value: " +query.toString());
    }
    
    @Test
    public void shouldReturnValidQueryUsingString() throws QueryException {
        Query query = queryBuilder.searchField("a_field").forValueEqualTo("10").build();
        assertNotNull(query);
        assertEquals(Query.class, query.getClass());
        assertEquals(Object[].class, query.getQueryObject().getClass());  
        System.out.println("Valid (equal To) query using string value: " +query.toString());
    }
    
    @Test
    public void shouldBuildEmptyQuery() {
        Query query = queryBuilder.buildEmptyQuery();
        assertNotNull(query);
        assertTrue(query.getQueryObject().length == 0);
        System.out.println("Valid empty query: " +query.toString());
    }
}
