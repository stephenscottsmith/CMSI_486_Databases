/*
	This is a SQL Parser and Executor class for the Flava database
	engine.
*/

import java.util.Arrays;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import java.io.FilenameFilter;

public class FlavaSQLParsecutor {
	static ArrayList <String> validCommands = new ArrayList<String>(Arrays.asList("create", "delete", "insert", "select", "update"));
	static ArrayList <String> validObjects = new ArrayList<String>(Arrays.asList("database", "table", "on"));
	static ArrayList <String> validOptions = new ArrayList<String>(Arrays.asList("index", "values", "schema"));
	ArrayList <String> validOptionParameters = new ArrayList<String>(Arrays.asList(""));
	
	// What Happens:
	// Tokenized string is sent to Parsecutor
	// Parsecutor must then validate that the command is valid both syntatically
	// and semantically
	// Parsecutor then tries to execute the command

	public FlavaSQLParsecutor () {

	}

	public static void parseCommand (String [] commandTokens) {
		// TODO 1: Validate command more
		try {	
			if (isCommandTokenLengthValid(commandTokens.length) &&
				validCommands.contains(commandTokens[0]) &&
				validObjects.contains(commandTokens[1])) {
				System.out.println("VALID COMMAND! YAY!");
				
				executeCommand(commandTokens);
			} else {
				System.out.println("FUCK");
			}
		} catch (Exception e) {
			System.out.println("INVALID COMMAND ENTERED. TRY AGAIN.");
		}
		// for (String s : commandTokens) {
		// 	System.out.println(s);
		// }
	}

	public static void executeCommand (String [] commandTokens) {
		String command = commandTokens[0].toLowerCase();
		switch (command) {
			case "create" :
				create(commandTokens);
				break;
			case "delete" :
				//delete();
				break;
			case "select" :
				System.out.println("selecting");
				break;
			case "insert" :
				System.out.println("inserting");
				break;
			case "update" :
				System.out.println("updating");
				break;

		}
	}

	public static void create (String [] commandTokens) {
		String objectType = commandTokens[1].toLowerCase(),
			   objectParameter = commandTokens[2];
		switch (objectType) {
			case "database" :
				createDatabase(objectParameter);
				break;
			case "table" :
				createTable(Flava.getCurrentGlobalDatabase(), objectParameter);
				break;
			case "on" :
				System.out.println("NEED TO IMPLEMENT!");
				break;
		}
	}

	public static Boolean isCommandTokenLengthValid (int length) {
		return (length == 3 || length == 5 || length == 7);
	}

	////////////////
	public static boolean directoryExists (String path) {
		File directory = new File(path);
		return directory.exists();
	}


	// CREATE SECTION //
	public static void createDatabase (String databaseName) {
		createDatabaseItem(("./data/" + databaseName + ".fdb"), "database", databaseName);
	}

	public static void createTable (String databaseName, String tableName) {
		System.out.println(("./data/" + databaseName + ".fdb/" +  tableName + ".ftl"));
		System.out.println(tableName);
		createDatabaseItem(("./data/" + databaseName + "/" +  tableName + ".ftl"), "table", tableName);
	}

	public static void createDatabaseItem (String directoryPath, String type, String name) {
		// Cite: http://stackoverflow.com/questions/3634853/how-to-create-a-directory-in-java
		File typeFolder = new File(directoryPath);

		if (!typeFolder.exists()) {
		    System.out.println("Attempting to create " + type + ": "  + name + "...");

		    try {
		        typeFolder.mkdir();
		        if (type == "table") {
		        	try {
		        		File tableSchemaFile = new File(typeFolder.getPath()+ "/" + name + ".schema");
		        		tableSchemaFile.createNewFile();

		        		File tableDataFile = new File(typeFolder.getPath() + "/" + name + ".data");
		        		tableDataFile.createNewFile();
		        	} catch (IOException io) {
		        		//throw new IOException("Cannot create table!");
		        		System.out.println("Failed to create table!");
		        	}	
		        }
		    } catch (SecurityException se) {
		        throw new SecurityException("You may not have permissions to create a new " + type + "! Consult your IT.");
		    }        
    
	       	System.out.println("The " + type + " " + name + " has been successfully created.");  
		} else {
		    System.out.println("The " + type + " " + name + " already exists! Try using another name.");
		}
	}
	// END CREATE SECTION //

}