import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;


// TODO: comment and complete your HashTableADT implementation
//
// TODO: implement all required methods
// DO ADD REQUIRED PUBLIC METHODS TO IMPLEMENT interfaces
//
// DO NOT ADD ADDITIONAL PUBLIC MEMBERS TO YOUR CLASS 
// (no public or package methods that are not in implemented interfaces)
//
// TODO: describe the collision resolution scheme you have chosen
// identify your scheme as open addressing or bucket
//
// if open addressing: describe probe sequence 
// if buckets: describe data structure for each bucket
//
// TODO: explain your hashing algorithm here 
// NOTE: you are not required to design your own algorithm for hashing,
//       since you do not know the type for K,
//       you must use the hashCode provided by the <K key> object

/** HashTable implementation that uses:
 * @param <K> unique comparable identifier for each <K,V> pair, may not be null
 * @param <V> associated value with a key, value may be null
 */
public class BookHashTable implements HashTableADT<String, Book> {
	
	/**
	 * HashNode used to traverse the hash table
	 * @author Nate
	 *
	 * @param <String>
	 * @param <Book>
	 */
	private class BookHashNode<String,Book>{
		String key;
		Book value;
		
		BookHashNode<String,Book> next; //reference to next node in hash table
		
		public BookHashNode(String key, Book value) {
			this.key = key;
			this.value = value;
		}
	}

    /** The initial capacity that is used if none is specifed user */
    private static final int DEFAULT_CAPACITY = 101;
    
    /** The load factor that is used if none is specified by user */
    private static final double DEFAULT_LOAD_FACTOR_THRESHOLD = 0.75;

    private int capacity; //current capacity of the HashTable
    
    private double loadFactorThreshold; //user specified load factor
    
    private static int numKeys; //number of keys, value pairs in the data structure
    
    private ArrayList <BookHashNode<String,Book>> bucket; //array of buckets

    /**
     * REQUIRED default no-arg constructor
     * Uses default capacity and sets load factor threshold 
     * for the newly created hash table.
     */
    public BookHashTable() {
        this(DEFAULT_CAPACITY,DEFAULT_LOAD_FACTOR_THRESHOLD);
    }
    
    /**
     * Creates an empty hash table with the specified capacity 
     * and load factor.
     * @param initialCapacity number of elements table should hold at start.
     * @param loadFactorThreshold the ratio of items/capacity that causes table to resize and rehash
     */
    public BookHashTable(int initialCapacity, double loadFactorThreshold) {
        this.capacity = initialCapacity; //sets number of elements in table at start
        this.loadFactorThreshold = loadFactorThreshold; //sets ratio of items/capacity
        numKeys = 0; //sets number of keys in empty table to zero
        
        bucket = new ArrayList<BookHashNode<String, Book>>();
        
        for (int i = 0; i<capacity;i++) {
        	bucket.add(null);
        }
    }

    // Add the key,value pair to the data structure and increase the number of keys.
    // If key is null, throw IllegalNullKeyException;
    // If key is already in data structure, throw DuplicateKeyException();
	@Override
	public void insert(String key, Book value) throws IllegalNullKeyException, DuplicateKeyException {
		if(key==null) throw new IllegalNullKeyException(); //null key
		
		//get index of bucket key would be placed in
		int index = getHashIndex(key);
		BookHashNode<String,Book> root = bucket.get(index);
		
		while(root!=null) { //find if duplicate key
			if(root.key.equals(key)) {
				throw new DuplicateKeyException();
			}
			root = root.next; //traverse to next node in bucket
		}
		
		//if goes above loadfactor then double size of hashtable
		if(((1.0*numKeys)/capacity)>loadFactorThreshold) {
			
			ArrayList<BookHashNode<String,Book>> temp = bucket;
			capacity = 2*capacity+1;
			bucket = new ArrayList<BookHashNode<String, Book>>();
			numKeys = 0;
			
			//add empty buckets
			for(int i = 0;i<capacity;i++) {
				bucket.add(null);
			}
			
			//add in old keys to new bucket array
			for(BookHashNode<String,Book> head:temp) {
				while(head!=null) {
					insert(head.key,head.value);
					head = head.next;
				}
			}
		}
		//add key to bucket
		numKeys++;
		root = bucket.get(index);
		BookHashNode<String,Book> node = new BookHashNode<String, Book>(key,value);
		node.next = root; //set this new key to head of list in bucket
		bucket.set(index, node);
		

		
	}

    // If key is found, 
    //    remove the key,value pair from the data structure
    //    decrease number of keys.
    //    return true
    // If key is null, throw IllegalNullKeyException
    // If key is not found, return false
	@Override
	public boolean remove(String key) throws IllegalNullKeyException {
		if(key==null) throw new IllegalNullKeyException(); //null key
		//get index of bucket key would be placed in
		int index = getHashIndex(key);
		BookHashNode<String,Book> node = bucket.get(index);
		BookHashNode<String,Book> prevNode = null;
		
		while(node!=null) { //finds key if there
			if(node.key.equals(key)) {
				if(prevNode!=null) //remove node in between
					prevNode.next = node.next;
				else
					bucket.set(index,node.next);//set head of bucket to next node
				numKeys--;
				return true;
			}
			prevNode = node;
			node = node.next; //traverse to next node in bucket
		}
		
		return false;
	}

    // Returns the value associated with the specified key
    // Does not remove key or decrease number of keys
    //
    // If key is null, throw IllegalNullKeyException 
    // If key is not found, throw KeyNotFoundException().
	@Override
	public Book get(String key) throws IllegalNullKeyException, KeyNotFoundException {
		if(key==null) throw new IllegalNullKeyException(); //null key
		
		int index = getHashIndex(key);
		BookHashNode<String,Book> root = bucket.get(index);
		
		while(root!=null) { //search bucket for key
			if(root.key.equals(key)) 
				return root.value;
			
			root = root.next; //traverse to next node in bucket
		}
		//key not found at index
		throw new KeyNotFoundException();
	}

	// Returns the number of key,value pairs in the data structure
	@Override
	public int numKeys() {
		return numKeys; 
	}

    // Notice:
    // THIS INTERFACE EXTENDS AND INHERITS ALL METHODS FROM DataStructureADT
    // and adds the following operations:

    // Returns the load factor for this hash table
    // that determines when to increase the capacity 
    // of this hash table
	@Override
	public double getLoadFactorThreshold() {
		return loadFactorThreshold;
	}
	
    // Capacity is the size of the hash table array
    // This method returns the current capacity.
    //
    // The initial capacity must be a positive integer, 1 or greater
    // and is specified in the constructor.
    // 
    // REQUIRED: When the load factor is reached, 
    // the capacity must increase to: 2 * capacity + 1
    //
    // Once increased, the capacity never decreases
	@Override
	public int getCapacity() {
		return capacity;
	}

	// Returns the collision resolution scheme used for this hash table.
    // Implement this ADT with one of the following collision resolution strategies
    // and implement this method to return an integer to indicate which strategy.
    //
     // 1 OPEN ADDRESSING: linear probe
     // 2 OPEN ADDRESSING: quadratic probe
     // 3 OPEN ADDRESSING: double hashing
     // 4 CHAINED BUCKET: array list of array lists
     // 5 CHAINED BUCKET: array list of linked lists
     // 6 CHAINED BUCKET: array list of binary search trees
     // 7 CHAINED BUCKET: linked list of array lists
     // 8 CHAINED BUCKET: linked list of linked lists
     // 9 CHAINED BUCKET: linked list of of binary search trees
	@Override
	public int getCollisionResolutionScheme() {
		return 5; //CHAINED BUCKET: array list of linked lists implemented
	}
	
	/**
	 * Method that calculates an index in the hash table for the key
	 * 
	 * @param key 
	 * @return - the calculated hash index
	 */
	private int getHashIndex(String key) {
		int z = 0;
		int hashCode = 0;
		
		//calculate hashcode by weighting positions of characters in string
		for(int i =0;i<key.length();i++) {
			switch(z) {
			case 0: { 
				hashCode += (int)(key.charAt(i))*8;
				z++;
			}
			case 1: {
				hashCode += (int)(key.charAt(i))*4;
				z++;
			}
			case 2: {
				hashCode += (int)(key.charAt(i))*2;
				z = 0;
			}
			}
		}
		hashCode = Math.abs(hashCode);
		int hashIndex = hashCode % capacity;
		return hashIndex;
		
	}


}