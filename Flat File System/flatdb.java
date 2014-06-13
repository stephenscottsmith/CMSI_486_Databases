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

public class flatdb {
	public static void main(String[] args) {
		// String newRecord = Arrays.toString(args);
		// System.out.println(newRecord);
		String commandList = "exit\t\t: Exits the FlatDB system.\n" +
							 "add\t\t: Adds new record.\n" + 
							 "delete\t\t : Deletes a record.\n";

		
		
		// http://stackoverflow.com/questions/12396765/continuous-input-commands
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
		        	System.out.println("Exiting FlatDB");
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

		// http://stackoverflow.com/questions/3634853/how-to-create-a-directory-in-java
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
		System.out.println(newCollectionPath + "\texists: " + newCollection.exists());
		if (!newCollection.exists()) {
			try {
				
				newCollection.createNewFile();
			} catch (IOException io) {
			
			}
		} else {
			System.out.println("The " + collectionName + " collection already exists in this database! \n" + 
							   "Try using another name for the collection you wish to create.");
		}
	}



	// TODO
	// add new element
	// // http://stackoverflow.com/questions/1625234/how-to-append-text-to-an-existing-file-in-java
	// try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("flat.txt", true)))) {
	//     out.println(newRecord);
	// } catch (IOException e) {
	//     //exception handling left as an exercise for the reader
	// }

	// find element

	// update element

	// delete element

	// delete all elements
}
