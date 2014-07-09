/*  
This is a flat file database management system from the command line
for my database system class (CMSI 486). I wrote it using Java 7. 
**/

import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Arrays;
import java.io.IOException;
import java.lang.SecurityException;
import java.io.File;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.util.Scanner;

public class Flatty {
	public static void main(String[] args) {
		String commandList = "exit\t\t: Exits the FlatDB system.\n" +
							 "add\t\t: Adds new record.\n" + 
							 "delete\t\t : Deletes a record.\n";

		
		
		// Cite: http://stackoverflow.com/questions/12396765/continuous-input-commands
		Scanner sc = new Scanner(System.in);

		for (prompt(); sc.hasNextLine(); prompt()) {

		    String line = sc.nextLine().replaceAll("\n", "");

		    // split line into arguments
		    String[] commands = line.split(" ");    

		    // process arguments
		    if (commands.length == 0) {
		    	System.out.println("Continuing");
		    	continue;
		    } else if (commands.length == 1) {
		        if (commands[0].equalsIgnoreCase("exit")) {
		        	System.out.println("Exiting Flatty");
		        	System.exit(0);
		        } else {
		        	System.out.println("Your command was not a valid FlatDB command! \n" + commandList);
		        }
		    } else if (commands.length >= 2) {
		        // Handle the parameters parameters
		    	String flatCommand = commands[0];

		    	switch (flatCommand) {
		    		case "createDatabase" : 
		    			String databaseName = commands[1];
		    			createDatabase(databaseName);
		    			break;
		    		case "createCollection" :
		    			String database = commands[1],
		    				   collectionName = commands[2];
		    			createCollection(database, collectionName);
		    			break;
		    		case "addRecord" :
		    			String db = commands[1],
		    				   collection = commands[2],
		    				   record = Arrays.toString(Arrays.copyOfRange(commands, 3, commands.length));
		    			System.out.println(record);
		    			addRecord(db, collection, record);
		    			break;
		    		default:
		    			System.out.println("NO VALID COMMANDS ENTERED");
		    	}
		    } 
		}
	}

	public static void prompt () {
		System.out.print(">> ");
	}

	public static void createDatabase(String databaseName) {

		// Cite: http://stackoverflow.com/questions/3634853/how-to-create-a-directory-in-java
		File newDir = new File(databaseName);

		if (!newDir.exists()) {
		    System.out.println("Attempting to create database: " + databaseName + "...");
		    boolean createDBResult = false;

		    try {
		        newDir.mkdir();
		        createDBResult = true;
		    } catch (SecurityException se){
		        //handle it
		        throw new SecurityException("You may not have permissions to create a new database! Consult your IT.");
		    }        
		    if (createDBResult) {    
		       System.out.println("Database " + databaseName + " has been successfully created.");  
		    } 

		} else {
		    System.out.println("The " + databaseName + " database already exists! Try using another name.");
		}
	}

	public static void createCollection (String databaseName, String collectionName) {
		String newCollectionPath = "./" + databaseName + "/" + collectionName;
		File newCollection = new File(newCollectionPath);
		
		if (!newCollection.exists()) {
			try {
				
				newCollection.createNewFile();
			} catch (IOException io) {
			
			}

			System.out.println("The " + collectionName + " has successfully been created on the " + databaseName + ".\n" +
							   "Path: " + newCollectionPath);

		} else {
			System.out.println("The " + collectionName + " collection already exists in this database! \n" + 
							   "Try using another name for the collection you wish to create.");
		}
	}

	public static void addRecord (String databaseName, String collectionName, String record) {
		// Cite: http://stackoverflow.com/questions/1625234/how-to-append-text-to-an-existing-file-in-java
		try {
			File collection = new File("./" + databaseName + "/" + collectionName);
			FileWriter writer = new FileWriter(collection, true);
			writer.write((record + "\n"));
			writer.close();
		} catch (IOException ioe) {
			// throw new IOException("Record add was unsuccessful!");
		}
	}

	public static void find () {
		// Mike: 
		// 1. Is my structure of my flat system ok?
		// 2. Is there a certain way you want me to iterate over my collections?
		//    -Open file and iterate over the text? perhaps with some regex
		//    -Perform a grep in the file sytem (grep -Rnsi)
	}

	// TODO
	// 1. Clean up code - validate databases and collections before they are called in main method

	// 2. find element

	// 3. update element

	// 4. delete element

	// 5. delete all elements
}
