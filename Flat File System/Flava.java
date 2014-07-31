/*  
	This is a flat file database management system from the command line
	for my database system class (CMSI 486). I wrote it using Java 7. 
**/

import java.util.Scanner;
import java.util.Arrays;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import java.io.FilenameFilter;
import java.util.Collections;

public class Flava {
	private static String databaseDirectoryString = "./data/";
	private static String databaseDirectory;
	private static String currentGlobalDatabase = "test.fdb";
	private static String [] databaseArray;
	// TODO: This is redundant b/c it's also in Parsecutor
	static ArrayList <String> validOptions = new ArrayList<String>(Arrays.asList("index", "values", "schema"));

	public static void main(String[] args) {
		initiateStartup();
 
		// Cite: http://stackoverflow.com/questions/12396765/continuous-input-commands
		Scanner sc = new Scanner(System.in);

		for (prompt(); sc.hasNextLine(); prompt()) {
			try {
			    String line = sc.nextLine().replaceAll("\n", "");

			    // Initial Parsing
			    String [] commandTokens = line.split(" ");

			    // process arguments
			    if (commandTokens.length <= 2) {
				    if (commandTokens[0].equals("")) {
				    	continue;
				    } else if (commandTokens[0].equalsIgnoreCase("exit")) {
			        	System.out.println("Exiting Flava");
			        	System.exit(0);
				    } else if (commandTokens[0].equalsIgnoreCase("list") &&
				    		  (commandTokens[1].equalsIgnoreCase("dbs") || 
				    		   commandTokens[1].equalsIgnoreCase("tables")) ) {
				    	listItems(commandTokens[1]);
				    } else if (commandTokens[0].equalsIgnoreCase("use")) {
				    	changeGlobalDatabase(commandTokens[1]);
				    } else if (line.equalsIgnoreCase("which db")) {
				    	whichDatabase();
				    } else {
				    	invalidCommand();
				    }
				} else {
			        // Parse better
			        commandTokens = tokenizeInput(line);
			        System.out.println("Tokens to parse: " + commandTokens.length);
			        FlavaSQLParsecutor.parseCommand(commandTokens);
			    } 
			} catch (Exception e) {
				invalidCommand();
			}
		}
	}

	public static void initiateStartup () {
		if (!FlavaSQLParsecutor.directoryExists(databaseDirectoryString)) {
			FlavaSQLParsecutor.createDatabaseItem(databaseDirectoryString, "folder", "data");
			FlavaSQLParsecutor.createDatabaseItem("./data/test.fdb", "database", "test");
		}
		updateDatabaseArray();
	}

	public static String [] tokenizeInput (String input) {
		String lowerCaseInput = input.toLowerCase(),
			   tier1Commands = input;
		int tokenArrayLength = 3;
		Boolean containsOption = containsValidOption(input.toLowerCase()),
				containsWhere = false;
		String [] optionArray = new String[2]; 
		String [] whereArray = new String[2];

		if (containsOption) {
			String option = determineValidOption(lowerCaseInput.toLowerCase()),
				   optionCommands = input.substring(lowerCaseInput.indexOf(option), lowerCaseInput.length()),
			       optionParameters = getParenthesisParameters(optionCommands),
			       lowerCaseOptions = optionCommands.toLowerCase();
			tier1Commands = input.substring(0, lowerCaseInput.indexOf(option));
			tokenArrayLength += 2;
			optionArray = new String [] {option, optionParameters};
			containsWhere = lowerCaseOptions.contains("where");

			if (containsWhere) {
				String whereCommands = optionCommands.substring(lowerCaseOptions.indexOf("where")),
				       whereParameters = getParenthesisParameters(whereCommands);
			    tokenArrayLength += 2;

			    whereArray = new String [] {"where", whereParameters};
			}
		}

		ArrayList<String> tokenList = new ArrayList<String>(Arrays.asList(tier1Commands.split(" ")));
		tokenList.addAll(Arrays.asList(optionArray));
		tokenList.addAll(Arrays.asList(whereArray));
		tokenList.removeAll(Collections.singleton(null));
		String [] commandTokens = tokenList.toArray(new String[tokenArrayLength]);
		
		return commandTokens;
	}

	public static String getParenthesisParameters (String command) {
		try {
			return command.substring(command.indexOf("("), command.indexOf(")") + 1);
		} catch (Exception e) {
			invalidCommand();
		}
		return "INVALID PARAMETERS";
	}

	public static String determineValidOption (String input) {
		for (String option : validOptions) {
			if (input.contains(option)) {
				return option;
			}	
		}
		return "NO OPTION";
	}

	public static Boolean containsValidOption (String input) {
		for (String option : validOptions) {
			if (input.contains(option)) {
				return true;
			}	
		}
		return false;
	}

	public static void printStringArray (String [] stringArray, String type) {
		if (stringArray.length >= 1) {	
			for (int i = 0; i < stringArray.length; i++) {
				System.out.println(stringArray[i]);
			}
		} else {
			System.out.println("There are currently 0 " + type + "s.");
		}
	}

	public static void listDatabasesInEngine () {
		updateDatabaseArray();
		printStringArray(databaseArray, "database");
	}

	public static void updateDatabaseArray () {
		databaseArray = getFilesInPath(("./data/"), ".fdb");
	}

	public static void listTablesOnDatabase () {
		String [] tableArray = getFilesInPath(("./data/" + currentGlobalDatabase), ".ftl");
		printStringArray(tableArray, "table");
	}

	static class FilterAwareFilenameFilter implements FilenameFilter {
		private final String filter;

	    public FilterAwareFilenameFilter(String filter) {
	        this.filter = filter;
	    }

	    @Override
	    public boolean accept(File dir, String name) {
	        return name.toLowerCase().endsWith(this.filter);
	    }
	}	

	public static String [] getFilesInPath (String filePath, String filter) {
		File directory = new File(filePath);
		return directory.list(new FilterAwareFilenameFilter(filter));
	}

	public static void prompt () {
		System.out.print(">> ");
	}

	public static void invalidCommand () {
		System.out.println("THE COMMAND YOU ENTERED CANNOT BE PARSED!");
	}

	/** listItems can list the databases in Flava or the tables of a specified database */
	public static void listItems (String type) {
		if (type.equals("dbs")) {
			listDatabasesInEngine();
		} else if (type.equals("tables")) {
			listTablesOnDatabase();
		}
	}

	public static void changeGlobalDatabase (String newGlobalDatabase) {
		newGlobalDatabase += ".fdb";
		updateDatabaseArray();
		if (Arrays.asList(databaseArray).contains(newGlobalDatabase)) {
			currentGlobalDatabase = newGlobalDatabase;
			whichDatabase();
		} else {
			System.out.println("The database " + newGlobalDatabase + " does not exist in the system!");
		}
	}

	public static void whichDatabase () {
		System.out.println("Using " + currentGlobalDatabase);
	}

	public static String getCurrentGlobalDatabase () {
		return currentGlobalDatabase;
	}
}



