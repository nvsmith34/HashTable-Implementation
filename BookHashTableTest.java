/**
 * Filename:   TestHashTableDeb.java
 * Project:    p3
 * Authors:    Debra Deppeler (deppeler@cs.wisc.edu)
 * 
 * Semester:   Fall 2018
 * Course:     CS400
 * 
 * Due Date:   before 10pm on 10/29
 * Version:    1.0
 * 
 * Credits:    None so far
 * 
 * Bugs:       TODO: add any known bugs, or unsolved problems here
 */

import org.junit.After;
import java.io.FileNotFoundException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/** 
 * Test HashTable class implementation to ensure that required 
 * functionality works for all cases.
 */
public class BookHashTableTest {

    // Default name of books data file
    public static final String BOOKS = "books_clean.csv";

    // Empty hash tables that can be used by tests
    static BookHashTable bookObject;
    static ArrayList<Book> bookTable;

    static final int INIT_CAPACITY = 2;
    static final double LOAD_FACTOR_THRESHOLD = 0.49;
       
    static Random RNG = new Random(0);  // seeded to make results repeatable (deterministic)

    /** Create a large array of keys and matching values for use in any test */
    @BeforeAll
    public static void beforeClass() throws Exception{
        bookTable = BookParser.parse(BOOKS);
        //System.out.print("x");
    }
    
    /** Initialize empty hash table to be used in each test */
    @BeforeEach
    public void setUp() throws Exception {
        // TODO: change HashTable for final solution
         bookObject = new BookHashTable(INIT_CAPACITY,LOAD_FACTOR_THRESHOLD);
    }

    /** Not much to do, just make sure that variables are reset     */
    @AfterEach
    public void tearDown() throws Exception {
        bookObject = null;
    }

    private void insertMany(ArrayList<Book> bookTable) 
        throws IllegalNullKeyException, DuplicateKeyException {
        for (int i=0; i < bookTable.size(); i++ ) {
            bookObject.insert(bookTable.get(i).getKey(), bookTable.get(i));
        }
    }

    /** IMPLEMENTED AS EXAMPLE FOR YOU
     * Tests that a HashTable is empty upon initialization
     */
    @Test
    public void test000_collision_scheme() {
        if (bookObject == null)
        	fail("Gg");
    	int scheme = bookObject.getCollisionResolutionScheme();
        if (scheme < 1 || scheme > 9) 
            fail("collision resolution must be indicated with 1-9");
    }


    /** IMPLEMENTED AS EXAMPLE FOR YOU
     * Tests that a HashTable is empty upon initialization
     */
    @Test
    public void test000_IsEmpty() {
        //"size with 0 entries:"
        assertEquals(0, bookObject.numKeys());
    }

    /** IMPLEMENTED AS EXAMPLE FOR YOU
     * Tests that a HashTable is not empty after adding one (key,book) pair
     * @throws DuplicateKeyException 
     * @throws IllegalNullKeyException 
     */
    @Test
    public void test001_IsNotEmpty() throws IllegalNullKeyException, DuplicateKeyException {
    	bookObject.insert(bookTable.get(0).getKey(),bookTable.get(0));
        String expected = ""+1;
        //"size with one entry:"
        assertEquals(expected, ""+bookObject.numKeys());
    }
    
    /** IMPLEMENTED AS EXAMPLE FOR YOU 
    * Test if the hash table  will be resized after adding two (key,book) pairs
    * given the load factor is 0.49 and initial capacity to be 2.
    */
    
    @Test 
    public void test002_Resize() throws IllegalNullKeyException, DuplicateKeyException {

    	bookObject.insert(bookTable.get(0).getKey(),bookTable.get(0));
    	int cap1 = bookObject.getCapacity(); 
    	bookObject.insert(bookTable.get(1).getKey(),bookTable.get(1));
    	int cap2 = bookObject.getCapacity();
    	
        //"size with one entry:"
        assertTrue(cap2 > cap1 & cap1 ==2);
    }
    
    /**
     * Test if hash table resizes more than once
     */
    @Test
    public void test003_2_Resize()throws IllegalNullKeyException, DuplicateKeyException{
    	bookObject.insert(bookTable.get(0).getKey(),bookTable.get(0));
    	int cap1 = bookObject.getCapacity(); 
    	bookObject.insert(bookTable.get(1).getKey(),bookTable.get(1));
    	int cap2 = bookObject.getCapacity();
    	bookObject.insert(bookTable.get(2).getKey(),bookTable.get(2));
    	int cap3 = bookObject.getCapacity(); 
    	bookObject.insert(bookTable.get(3).getKey(),bookTable.get(3));
    	int cap4 = bookObject.getCapacity();
    	
    	//size with 4 entries
    	assertTrue(cap4>cap3 && cap3 == cap2 && cap2>cap1);
    }
    
    /**
     * Test that throws exception when inserting duplicate key
     * 
     */
    @Test
    public void test004_insert_same_key()throws IllegalNullKeyException, DuplicateKeyException{
    	try {
    	bookObject.insert(bookTable.get(1).getKey(),bookTable.get(1));
    	bookObject.insert(bookTable.get(1).getKey(),bookTable.get(1));
    	fail("shouldn't be able to insert duplicate keys");
    	}catch(DuplicateKeyException i) {
    		
    	}

    }
    
    /**
     * Test if hash table resizes to correct capacity after
     * many inserts
     */
    @Test 
    public void test005_insert_many_keys()throws IllegalNullKeyException, DuplicateKeyException{
    	try {
    		insertMany(bookTable);
    	}catch(Exception e) {
    		fail("unexpected exception occcurred");
    	}
    	
    	int cap = bookObject.getCapacity();
    	
    	//size with 89 entries
    	assertTrue(cap == 191);
    }
    
    /**
     * Tests basic remove function
     */
    @Test
    public void test006_insert_delete()throws IllegalNullKeyException, DuplicateKeyException{
    	try {
    		bookObject = new BookHashTable(10,.7);
    		bookObject.insert(bookTable.get(0).getKey(), bookTable.get(0));
    		boolean rem = bookObject.remove(bookTable.get(0).getKey());
    		if(rem==false)
    			fail("should have removed the key from the table");
    		
    	}catch(Exception e) {
    		fail("unexpected excpeption " + e);
    	}
    	
    }
    
    /**
     * Tests removing then reinserting that same key
     */
    @Test
    public void test007_insert_delete_insert()throws IllegalNullKeyException, DuplicateKeyException{
    	try {
    		try {
        		bookObject = new BookHashTable(10,.7);
	    		bookObject.insert(bookTable.get(0).getKey(), bookTable.get(0));
	    		bookObject.remove(bookTable.get(0).getKey());
	    		bookObject.insert(bookTable.get(0).getKey(), bookTable.get(0));
    		}catch(DuplicateKeyException i) {
    			fail("Should have been able to reinsert key");
    		}
    	}catch(Exception e) {
    		fail("unexpected exception " + e);
    	}
    }
    
    /**
     * Tests inserting whole book table after removing one, then removing key not at top of bucket
     */
    @Test
    public void test008_insert_many_and_remove()throws IllegalNullKeyException, DuplicateKeyException{
    	try {
    		bookObject = new BookHashTable(11,.7);
    		bookObject.insert(bookTable.get(5).getKey(),bookTable.get(5));
    		bookObject.remove(bookTable.get(5).getKey());
    		insertMany(bookTable);
    		boolean rem = bookObject.remove(bookTable.get(83).getKey());
    		if(rem==false)
    			fail("should have removed key");
    	}catch(Exception e) {
    		fail("unexpected exception " +e);
    	}
    }
    
    /**
     * Tests that removing key not there doesn't work
     */
    @Test
    public void test009_remove_key_not_there()throws IllegalNullKeyException, DuplicateKeyException{
    	bookObject.insert(bookTable.get(5).getKey(),bookTable.get(5));
		bookObject.remove(bookTable.get(5).getKey());
		boolean rem =bookObject.remove(bookTable.get(5).getKey());
		if(rem!=false)
			fail("should not have been able to remove this book");
    }
    
    /**
     * Tests that get method returns correct value
     */
    @Test
    public void test010_get_books()throws IllegalNullKeyException,DuplicateKeyException,KeyNotFoundException{
    	bookObject = new BookHashTable(11,.7);
    	insertMany(bookTable);
    	Book b = bookObject.get(bookTable.get(83).getKey());
    	if(!b.getKey().equals(bookTable.get(83).getKey()))
    		fail("did not return correct key");
    }
    
    @Test
    public void test011_get_book_not_there()throws IllegalNullKeyException,DuplicateKeyException,KeyNotFoundException{
	    try {
    		bookObject = new BookHashTable(11,.7);
	    	insertMany(bookTable);
	    	bookObject.remove(bookTable.get(83).getKey());
	    	bookObject.get(bookTable.get(83).getKey());
	    	fail("should not have been able to get book");
	    }catch(KeyNotFoundException i) {
	    	
	    }
	    
    }
    
    
}
