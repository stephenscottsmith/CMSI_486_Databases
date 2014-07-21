/*  
This is a flat file database management system from the command line
for my database system class (CMSI 486). I wrote it using Java 7. 
**/

import java.util.Scanner;
import java.util.Arrays;
import java.io.File;
import java.io.IOException;

public class Flava {
	private static String currentDatabase = "test";

	public static void main(String[] args) {
		initiateStartup();

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
		        	System.out.println("Exiting Flava");
		        	System.exit(0);
		        } else {
		        	System.out.println("Your command was not a valid Flava command!"/* + commandList*/);
		        }
		    } else if (commands.length >= 2) {
		        // Handle the parameters parameters
		    	// String flatCommand = commands[0].toLowerCase();

		    	// switch (flatCommand) {
		    	// 	case "create" :
		    	// 		// Can CREATE the following:
		    	// 		// Datebase
		    	// 		// Table

		    	// 		create(Arrays.copyOfRange(commands, 1, commands.length));
		    			
		    	// 		break;
		    	// 	case "delete" :
		  
		    	// 		break;
		    	// 	case "update" :
		  
		    	// 		break;
		    	// 	case "select" :
		  
		    	// 		break;
		    	// 	case "insert" :
		  
		    	// 		break;
		    	// 	default:
		    	// 		invalidCommand();
		    	// }
		    } 
		}
	}

	public static void initiateStartup () {
		// 1. Find data folder
		// 	a. If data folder exists:
		// 		-Populate global list of databases w/ string names
		// 		-Confirm that test database exists
		// 	b. If it doesn't exist, create it and create test database within it
		// c. Set global database to test
		String [] databases;
		if (directoryExists("data")) {

		} else {
			createDirectory("data");
			createDirectory("./data/test.fdf");
		}

	}

	public static boolean directoryExists (String path) {
		File directory = new File(path);
		return directory.exists();
	}

	public static void createDatabase (String databaseName) {
		createDirectory("./data/" + databaseName + ".fdb");
	}

	public static void createTable (String databaseName, String tableName) {
		createDirectory(("./data/" + databaseName + ".fdb/" +  tableName), false);
	}

	public static void createDirectory (String directoryPath, Boolean isDatabase) {
		// Cite: http://stackoverflow.com/questions/3634853/how-to-create-a-directory-in-java
		File newDir = new File(databaseName);

		if (!newDir.exists()) {
		    System.out.println("Attempting to create database: " + databaseName + "...");
		    boolean createDBResult = false;

		    try {
		        newDir.mkdir();
		        createDBResult = true;
		    } catch (SecurityException se) {
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

	public static void prompt () {
		System.out.print(">> ");
	}

	public static void invalidCommand () {
		System.out.println("THE COMMAND YOU ENTERED CANNOT BE PARSED!");
	}

	// public static void create(String [] commands) {
	// 	String objectToCreate = commands[0].toLowerCase();

	// 	switch (objectToCreate) {
	// 		case "database" :
	// 			System.out.println("DATABASE");
	// 			createDatabase(Arrays.copyOfRange(commands, 1, commands.length));
	// 			break;
	// 		case "table" :
	// 			System.out.println("TABLE");
	// 			break;
	// 		case "index" :
	// 			System.out.println("INDEX");
	// 			break;
	// 		default :
	// 			System.out.println("THE OBJECT YOU ARE TRYING TO CREATE IS NOT A VALID OBJECT!");
	// 			break;
	// 	}

	// }



}




