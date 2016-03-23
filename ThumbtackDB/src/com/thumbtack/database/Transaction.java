package com.thumbtack.database;

import java.util.HashMap;

/**
 * @author Ankith
 * A transaction block conceptually includes all past uncommitted transactions accessible through traversing to the previous transaction block.
 */
public class Transaction {	
	private Transaction prev;	
 
	private HashMap<String, Integer> storeMap = new HashMap<String, Integer>();
	private HashMap<Integer, Integer> counterMap = new HashMap<Integer, Integer>(); 
 
	public Transaction(){}
 
	public void setPrev(Transaction block) {
		prev = block;
	}
 
	public Transaction(HashMap<String, Integer>nameValue, HashMap<Integer, Integer>valueCounter){
		storeMap = nameValue;
		counterMap = valueCounter;
	}
 
	public HashMap<String, Integer> getNameValue(){
		return storeMap;
	}
 
	
	/**
	 * Set the variable name to the value and maintain the counter.
	 * 
	 * @param name
	 * @param currentValue
	 */
	public void set(String name, Integer currentValue){
 
		// decrease counter of old 'name' value
		Integer prevValue = get(name);
		if (prevValue != null){
			Integer prevValueCounter = numEqualTo(prevValue);
			counterMap.put(prevValue, --prevValueCounter);
		}
 
		// increase counter of new 'name' value
		Integer currentValueCounter = numEqualTo(currentValue);
		if (currentValue != null) {
			if (currentValueCounter != null) {
				counterMap.put(currentValue, ++currentValueCounter);
			} else {
				counterMap.put(currentValue, new Integer(1));
			}
		}
 
		storeMap.put(name, currentValue);
	}
 
	/**
	 * 
	 * @param name
	 * @return 
	 * 			value for that particular name
	 */
	public Integer get(String name) {
		Transaction block = this;
		Integer value = block.storeMap.get(name);
		while(!block.storeMap.containsKey(name) && block.prev != null){
			block = block.prev;
			value = block.storeMap.get(name);
		}
		return value;
	}
 
	/**
	 * 
	 * @param value
	 * @return 
	 * 			Number of variables that are currently set to value
	 * 			0 if no variables are set
	 */
	public Integer numEqualTo(Integer value){
		if (value == null) return 0;
 
		Transaction block = this;
		Integer counter = block.counterMap.get(value);
		while(counter == null && block.prev != null){
			block = block.prev;
			counter = block.counterMap.get(value);
		}
 
		if (counter == null)
			return 0;
		else{
			return counter;	
		}
	}
}
