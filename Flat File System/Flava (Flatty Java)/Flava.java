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


		// Prepare the list of databases to use
		// String [] databases = getListOfDatabases();
		System.out.println(currentDatabase);
		currentDatabase = "bash";
		System.out.println(currentDatabase);

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
		    	String flatCommand = commands[0].toLowerCase();

		    	switch (flatCommand) {
		    		case "create" :
		    			// Can CREATE the following:
		    			// Datebase
		    			// Table

		    			create(Arrays.copyOfRange(commands, 1, commands.length));
		    			
		    			break;
		    		case "delete" :
		  
		    			break;
		    		case "update" :
		  
		    			break;
		    		case "select" :
		  
		    			break;
		    		case "insert" :
		  
		    			break;
		    		default:
		    			invalidCommand();
		    	}
		    } 
		}
	}

	public static void prompt () {
		System.out.print(">> ");
	}

	public static void invalidCommand () {
		System.out.println("THE COMMAND YOU ENTERED CANNOT BE PARSED!");
	}

	public static void create(String [] commands) {
		String objectToCreate = commands[0].toLowerCase();

		switch (objectToCreate) {
			case "database" :
				System.out.println("DATABASE");
				createDatabase(Arrays.copyOfRange(commands, 1, commands.length));
				break;
			case "table" :
				System.out.println("TABLE");
				break;
			case "index" :
				System.out.println("INDEX");
				break;
			default :
				System.out.println("THE OBJECT YOU ARE TRYING TO CREATE IS NOT A VALID OBJECT!");
				break;
		}

	}

	public static void createDatabase(String [] databaseName) {
		if (databaseName.length == 1) {
			// Cite: http://stackoverflow.com/questions/3634853/how-to-create-a-directory-in-java
			File newDir = new File(databaseName[0]);
			if (!newDir.exists()) {
			    System.out.println("Attempting to create database: " + databaseName[0] + "...");
			    boolean createDBResult = false;

			    try {
			        newDir.mkdir();
			        createDBResult = true;
			    } catch (SecurityException se){
			        //handle it
			        throw new SecurityException("You may not have permissions to create a new database! Consult your IT.");
			    }        
			    if (createDBResult) {    
			       System.out.println("Database " + databaseName[0] + " has been successfully created.");  
			    } 

			} else {
			    System.out.println("The " + databaseName[0] + " database already exists! Try using another name.");
			}
		} else {
			invalidCommand();
		}
	}

	public static void createTable (String [] commandTokens) {

		String newTablePath = "./" + commandTokens[0] + "/" + commandTokens[1];
		File newTable = new File(newTablePath);
		
		if (!newTable.exists()) {
			try {
				
				newTable.createNewFile();
			} catch (IOException io) {
			
			}

			System.out.println("The " + commandTokens[0]+ " has successfully been created on the " + commandTokens[0] + ".\n" +
							   "Path: " + newTablePath);

		} else {
			System.out.println("The " + commandTokens[0] + " Table already exists in this database! \n" + 
							   "Try using another name for the Table you wish to create.");
		}
	}

}




