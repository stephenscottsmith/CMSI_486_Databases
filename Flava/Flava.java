/*  
	This is a flat file database management system from the command line
	for my database system class (CMSI 486). I wrote it using Java 7. 
**/

import java.util.Scanner;
import java.util.Arrays;
import java.util.ArrayList;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.File;
import java.io.IOException;
import java.io.FilenameFilter;
import java.util.Collections;
import java.util.ArrayDeque;

/**
 * This is Flava!
 */
public class Flava {
	private static String databaseDirectoryString = "./data/";
	private static String databaseDirectory;
	private static String currentGlobalDatabase = "test.fdb/";
	private static ArrayList<String> databaseList;
	// TODO: This is redundant b/c it's also in Parsecutor
	static ArrayList<String> validOptions = new ArrayList<String>(Arrays.asList("index", "values", "schema"));
	static ArrayList<String> validExtendedOptions = new ArrayList<String>(Arrays.asList("where","schema"));

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
				    	listItems(commandTokens[1], true);
				    } else if (commandTokens[0].equalsIgnoreCase("use")) {
				    	changeGlobalDatabase(commandTokens[1]);
				    } else if (line.equalsIgnoreCase("which db")) {
				    	whichDatabase();
				    } else {
				    	invalidCommand("Flava " + 55);
				    }
				} else {
			        // Parse better
			        FlavaSQLParsecutor fp = new FlavaSQLParsecutor(tokenizeInput(line));
			        if (fp.isCommandExecutable()) {
			        	System.out.println("Flava 61 : CREATED FP");
			        	fp.execute();
			        }
			    } 
			} catch (Exception e) {
				invalidCommand("Flava " + 65);
			}
		}
	}

	/** This initiates startup. */
	public static void initiateStartup () {
		File dataFile = new File("./data/");
		if (!dataFile.exists()) {
			try {
				System.out.println("Attempting to initialize Flava data directory...");
				dataFile.mkdir();
			} catch (SecurityException io) {

			}
			System.out.println("Flava data directory has been successfully created!\nAttempting to create test database...");
			FlavaSQLParsecutor fp = new FlavaSQLParsecutor(new String [] {"create", "database", "test"});
			fp.execute();
			System.out.println("The test database has been successfully created!");
		}
		updateDatabaseList();
	}

	public static String [] tokenizeInput (String input) {
		String lowerCaseInput = input.toLowerCase();
		int tokenArrayLength = 3;
		Boolean containsOption = containsValidOption(lowerCaseInput, validOptions);
		String optionCommands = "";
		String option = determineValidOption(lowerCaseInput, validOptions);
		String extendedOptionCommands = "";
		int extendedOptionIndex = 0;
		ArrayList<String> tokens = new ArrayList<String>(
			(containsOption) ? Arrays.asList(input.substring(0, input.indexOf(option)).split(" ")) :
			Arrays.asList(input.split(" "))
		);

		if (containsOption) {
			// basic idea is to determine where the first option is and create 2 substrings
			optionCommands = input.substring(lowerCaseInput.indexOf(option), 
				   									lowerCaseInput.length());
			tokens.addAll(Arrays.asList(option, getParenthesisParameters(optionCommands)));

			// Can probably just send the option commands since its a shorter string
			if (containsValidOption(input, validExtendedOptions) && !option.equals("schema")) {
				System.out.println("CONTAINS EXTENDED)");
				tokenArrayLength += 2;
				String extendedOption = determineValidOption(lowerCaseInput, validExtendedOptions);
				extendedOptionIndex = optionCommands.indexOf(extendedOption);
				extendedOptionCommands = optionCommands.substring(extendedOptionIndex, optionCommands.length()); 
				tokens.addAll(Arrays.asList(extendedOption, getParenthesisParameters(extendedOptionCommands)));
			}
		}

		// This fixes the option array in case the option == index 
		if (option.equals("index")) {
			extendedOptionIndex = (extendedOptionIndex == 0) ? optionCommands.length() : extendedOptionIndex;  
			tokens.set((tokens.indexOf("index") + 1), 
					   optionCommands.substring(0, extendedOptionIndex).split(" ")[1]);
		}
		System.out.println("STRING: " + tokens.toString());

		return tokens.toArray(new String[tokens.size()]);
	}

	public static String getParenthesisParameters (String command) {
		if (command.indexOf("(") > -1) {	
			return command.substring(command.indexOf("("), command.indexOf(")") + 1);
		}
		return "INVALID";
	}

	public static String determineValidOption (String input, ArrayList<String> options) {
		for (String option : options) {
			if (input.contains(option)) {
				return option;
			}	
		}
		return "NO OPTION";
	}

	public static Boolean containsValidOption (String input, ArrayList<String> options) {
		for (String option : options) {
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
			System.out.println("There are currently 0 " + type + ".");
		}
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

	

	public static void prompt () {
		System.out.print(">> ");
	}

	public static void invalidCommand (String lineNumber) {
		System.out.println(lineNumber + " : THE COMMAND YOU ENTERED CANNOT BE PARSED AND/OR EXECUTED!");
	}



	/** listItems can list the databases in Flava or the tables of a specified database */
	public static ArrayList<String> listItems (String type, Boolean print) {
		String path = "./data/" + (type.equals("dbs") ? "" : currentGlobalDatabase),
			   filter = (type.equals("dbs") ? ".fdb" : ".ftl");
		File directory = new File(path);
		String [] itemsArray = directory.list(new FilterAwareFilenameFilter(filter));
		if (print) {
			printStringArray(itemsArray, type);
		}
		return new ArrayList<String>(Arrays.asList(itemsArray));
	}

	public static void updateDatabaseList () {
		databaseList = listItems("dbs", false);
		for (String s : databaseList) {
			System.out.println(s);
		}
	}

	public static void changeGlobalDatabase (String newGlobalDatabase) {
		newGlobalDatabase += ".fdb";
		if (databaseList.contains(newGlobalDatabase)) {
			currentGlobalDatabase = newGlobalDatabase;
			whichDatabase();
		} else {
			System.out.println("The database " + newGlobalDatabase + 
				" does not exist in the system!");
		}
	}

	public static void whichDatabase () {
		System.out.println("Using " + currentGlobalDatabase);
	}

	public static String getCurrentGlobalDatabase () {
		return currentGlobalDatabase + "/";
	}

}



