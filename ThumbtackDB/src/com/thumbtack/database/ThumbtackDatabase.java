package com.thumbtack.database;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

/**
 * @author Ankith 
 * An in-memory database similar to Redis
 */
public class ThumbtackDatabase {
	private LinkedList<Transaction> blocks;

	// Initialize db to hold transaction history
	public ThumbtackDatabase(){
		blocks = new LinkedList<Transaction>();
		blocks.add(new Transaction());
	} 
	
	/**
	 * Set the variable name to the value.
	 * @param name
	 * @param value
	 */
	public void set(String name, Integer value){
		blocks.getLast().set(name, value);
	}

	
	/**
	 * Get the value of the variable name
	 * @param name
	 * @return 
	 * 		NULL if that variable is not set.
	 */
	public Integer get(String name){
		return blocks.getLast().get(name);
	}

	/**
	 * 
	 * @param value
	 * @return 
	 * 			Number of variables that are currently set to value
	 * 			0 if no variables are set
	 */
	public Integer numEqualTo(Integer value){
		return blocks.getLast().numEqualTo(value);
	}

	/**
	 * Open a new transaction block
	 * Note: Transaction blocks can be nested
	 */
	public void begin(){
		Transaction block = new Transaction();
		block.setPrev(blocks.getLast());
		blocks.add(block);
	}

	/**
	 * Close all open transaction blocks, permanently applying the changes made in them
	 * @return
	 * 			false - if no transaction exist
	 */
	@SuppressWarnings("unchecked")
	public boolean commit() {
		if (blocks.size() <= 1) return false;

		HashMap<String, Integer> storeMap = new HashMap<String, Integer>();
		HashMap<Integer, Integer> counterMap = new HashMap<Integer, Integer>();

		ListIterator<Transaction> iterator = blocks.listIterator();
		while (iterator.hasNext()) {
			Transaction block = iterator.next();
			storeMap.putAll((Map<? extends String, ? extends Integer>) block.getNameValue());
		}

		for (Entry<String, Integer> entry : storeMap.entrySet()) {
			Integer value = entry.getValue();
			if(counterMap.get(value) == null){
				counterMap.put(value, new Integer(1));
			}
			else{
				counterMap.put(value, new Integer(counterMap.get(value) + 1));
			}
			storeMap.put(entry.getKey(),entry.getValue());
		}		

		blocks = new LinkedList<Transaction>();
		blocks.add(new Transaction(storeMap, counterMap));

		return true;
	}

	/**
	 * Undo the recent transaction
	 * @return
	 */
	public boolean rollBack(){
		if (blocks.size() <= 1) return false;
		blocks.removeLast();
		return true;
	}

	public static void main(String[] args) {
		ThumbtackDatabase db = new ThumbtackDatabase();
		Scanner sc = new Scanner(System.in);
		
		sc.useDelimiter("\\s+"); 
		String command; 
		while (sc.hasNextLine()) {
			command = sc.nextLine();
			String[] tokens = command.split("\\s+");
			String cmd = tokens[0];
			String name;
			Integer value;
			try {
				switch (cmd) {
				case "GET":
					name = tokens[1];
					System.out.println(db.get(name) != null ? db.get(name):"NULL");
					break;
				case "SET":
					name = tokens[1];
					value = Integer.parseInt(tokens[2]);
					db.set(name, value);
					break;
				case "UNSET":
					name = tokens[1];
					db.set(name, null);
					break;
				case "NUMEQUALTO":
					value = Integer.parseInt(tokens[1]);
					System.out.println(db.numEqualTo(value));
					break;
				case "BEGIN":
					db.begin();
					break;
				case "ROLLBACK":
					if (!db.rollBack()) System.out.println("NO TRANSACTION");
					break;
				case "COMMIT":
					if (!db.commit()) System.out.println("NO TRANSACTION");
					break;					
				case "END":
					return;
				case "":
					break;
				default:
					System.out.println("Invalid Command: " + cmd );
				}
			} catch (NumberFormatException e) {			
				System.out.println("Number Format Exception : " + command );
			} catch (ArrayIndexOutOfBoundsException e) {
				System.out.println("Missing Operand: " + command );
			}
		}
		sc.close();
	}
}

