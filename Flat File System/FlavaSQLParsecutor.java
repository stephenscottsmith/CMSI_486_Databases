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
	// What would it's properites be?
	// 1. Main Command (i.e. Create, Delete, etc.)
	// 2. Type (i.e. database, table, "on" is same thing as table)
	// 3. Optional Token (i.e. index, values)
	// 4. If 3 is present, then 4th property is necessary (i.e. index name, values)
	
	// What Happens:
	// String is sent to Flava
	// FlavaSQLParsecutor parse

	public FlavaSQLParsecutor () {

	}

	public static void parseCommand (String [] commandTokens) {
		// Validate if command is a good command
		try {	
			if (isCommandTokenLengthValid(commandTokens.length) &&
				isValidToken(validCommands, commandTokens[0]) &&
				isValidToken(validObjects, commandTokens[1])) {
				System.out.println("VALID COMMAND! YAY!");
			} else {
				System.out.println("FUCK");
			}
		} catch (Exception e) {
			System.out.println("INVALID COMMAND ENTERED. TRY AGAIN.");
		}
		for (String s : commandTokens) {
			System.out.println(s);
		}
		// If it is a valid command, then try to execute the command
	}

	public static Boolean isCommandTokenLengthValid (int length) {
		return (length == 3 || length == 5 || length == 7);
	}

	public static Boolean isValidToken (ArrayList<String> validTokens, String token) {
		return validTokens.contains(token);
	}


	////////////////
	public static boolean directoryExists (String path) {
		File directory = new File(path);
		return directory.exists();
	}

	public static void createDatabase (String databaseName) {
		createDatabaseItem(("./data/" + databaseName + ".fdb"), "database", databaseName);
	}

	public static void createTable (String databaseName, String tableName) {
		createDatabaseItem(("./data/" + databaseName + ".fdb/" +  tableName + ".ftl"), "table", tableName);
	}

	public static void createDatabaseItem (String directoryPath, String type, String name) {
		// Cite: http://stackoverflow.com/questions/3634853/how-to-create-a-directory-in-java
		File newDir = new File(directoryPath);

		if (!newDir.exists()) {
		    System.out.println("Attempting to create " + type + ": "  + name + "...");

		    try {
		        newDir.mkdir();
		        if (type == "table") {
		        	try {
		        		newDir.createNewFile();
		        	} catch (IOException io) {
		        		//throw new IOException("Cannot create table!");
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
	/////////////

}