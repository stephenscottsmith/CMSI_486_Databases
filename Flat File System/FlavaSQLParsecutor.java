/*
	This is a SQL Parser and Executor class for the Flava database
	engine.
*/

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
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
		int tokenLength = commandTokens.length;
		String command = commandTokens[0].toLowerCase(),
			   objectType = commandTokens[determineObject(tokenLength, 1, 3)].toLowerCase(),
			   objectParameter = commandTokens[determineObject(tokenLength, 2, 4)].toLowerCase();
		switch (command) {
			case "create" :			
				createDatabaseItem(objectType, objectParameter);
				
				
				// update database array
				break;
			case "delete" :
				deleteDatabaseItem(objectType, objectParameter);
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

	// DELETE SECTION // 


	// END DELETE SECTION //

	public static Boolean isCommandTokenLengthValid (int length) {
		return (length == 3 || length == 5 || length == 7);
	}

	public static String pathConstructor (String objectType, String name) {
		// TODO: REFACTOR THE STRING LITERALS!
		String path = "./data/";
		if (objectType.toLowerCase().equals("database")) {
			path += name + ".fdb/";
		} else if (objectType.toLowerCase().equals("table")) {
			path += Flava.getCurrentGlobalDatabase() + "/" + name + ".ftl/";
		} else if (objectType.toLowerCase().equals("schema")) {
			path += Flava.getCurrentGlobalDatabase() + "/" + name + ".ftl/" + name + ".schema";
		} else if (objectType.toLowerCase().equals("data")) {
			path += Flava.getCurrentGlobalDatabase() + "/" + name + ".ftl/" + name + ".data";
		} else if (objectType.toLowerCase().equals("index")) {
			path += Flava.getCurrentGlobalDatabase() + "/" + name + ".ftl/" + name + ".index";
		} 
		return path;
	}

	public static int determineObject (int commandTokenLength, int option1, int option2) {
		return (commandTokenLength == 3) ? option1 : option2;
	}

	// CREATE SECTION //
	public static void createDatabaseItem (String objectType, String objectParameter) {
		String pathString = pathConstructor(objectType, objectParameter);
		File objectFile = new File(pathString);

		if (!objectFile.exists()) {
			try {
				objectFile.mkdir();

				if (objectType.equals("table")) {
					File dataFile = new File(pathConstructor("data", objectParameter)),
						 schemaFile = new File(pathConstructor("schema", objectParameter));
					dataFile.createNewFile();
					schemaFile.createNewFile();
				}
			} catch (IOException io) {

			}
		} else {
			System.out.println("The " + objectType + " " + objectParameter + 
				" already exists! Cannot create a duplicate " + objectType);
		}
				
	}
	// END CREATE SECTION //

	public static void deleteDatabaseItem (String objectType, String objectParameter) {
		String pathString = pathConstructor(objectType, objectParameter);
		File file = new File(pathString);

		if (file.exists()) {
			try {
				String [] filesInFile = file.list();

				for (String fileString : filesInFile) {
				    File currentFile = new File(file.getPath(), fileString);
				    currentFile.delete();
				}
				file.delete();

			} catch (SecurityException se) {
				System.out.println("Cannot delete " + objectType + " " + objectParameter + "!");
			}
		} else {
			System.out.println("The " + objectType + " " + objectParameter + 
				" does not exist!");
		}

	}

}