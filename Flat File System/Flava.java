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
				    	invalidCommand();
				    }
				} else {
			        // Parse better
			        FlavaSQLParsecutor fp = new FlavaSQLParsecutor(tokenizeInput(line));
			        if (fp.isCommandExecutable()) {
			        	System.out.println("CONSTRUCTED");
			        	fp.execute();
			        }
			    } 
			} catch (Exception e) {
				invalidCommand();
			}
		}
	}

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
		
		Boolean containsOption = containsValidOption(input, validOptions);
		String option = determineValidOption(lowerCaseInput, validOptions);
		ArrayDeque<String> tokens = new ArrayDeque<String>(
			(containsOption) ? Arrays.asList(input.substring(0, input.indexOf(option)).split(" ")) :
			Arrays.asList(input.split(" "))
		);

		if (containsOption) {
			// basic idea is to determine where the first option is and create 2 substrings
			tokenArrayLength += 2;
			String optionCommands = input.substring(lowerCaseInput.indexOf(option), 
				   									lowerCaseInput.length());
			tokens.addAll(Arrays.asList(option, getParenthesisParameters(optionCommands)));

			// Can probably just send the option commands since its a shorter string
			if (containsValidOption(input, validExtendedOptions)) {
				tokenArrayLength += 2;
				String extendedOption = determineValidOption(lowerCaseInput, validExtendedOptions);
				int extendedOptionIndex = optionCommands.indexOf(extendedOption);
				String extendedOptionCommands = optionCommands.substring(extendedOptionIndex, optionCommands.length()); 
				
				// This fixes the option array in case the option == index 
				if (option.equals("index")) {
					tokens.removeLast();
					tokens.removeLast();
					tokens.addAll(Arrays.asList(optionCommands.substring(0, extendedOptionIndex).split(" ")));
				}
				tokens.addAll(Arrays.asList(extendedOption, getParenthesisParameters(extendedOptionCommands)));
			}
		}
		System.out.println("STRING: " + tokens.toString());
		//tokens.addAll(Arrays.asList());

		
			// System.out.println("objectParameters : " + objectParameters + "\n" +
			// 				   "option : " + option + "\n" +
			// 				   "optionCommands : " + optionCommands);
		// Split objectParameters as usual

		// System.out.println(lowerCaseInput + "\n" +
		// 				   containsOption + " : " + option + "\n" +
		// 				   containsExtendedOption + " : " + extendedOption);

		// String lowerCaseInput = input.toLowerCase(),
		// 	   tier1Commands = input;
		// int tokenArrayLength = 3;
		// Boolean containsOption = containsValidOption(input.toLowerCase()),
		// 		containsWhere = false;
		// String [] optionArray = new String[2]; 
		// String [] extendedOptionsArray = new String[2];

		// if (containsOption) {
		// 	String option = determineValidOption(lowerCaseInput.toLowerCase()),
		// 		   optionCommands = input.substring(lowerCaseInput.indexOf(option), lowerCaseInput.length()),
		// 	       optionParameters = getParenthesisParameters(optionCommands),
		// 	       lowerCaseOptions = optionCommands.toLowerCase();
		// 	tier1Commands = input.substring(0, lowerCaseInput.indexOf(option));
		// 	tokenArrayLength += 2;
		// 	optionArray = new String [] {option, optionParameters};
		// 	containsWhere = lowerCaseOptions.contains("where");

		// 	if (containsWhere) {
		// 		String whereCommands = optionCommands.substring(lowerCaseOptions.indexOf("where")),
		// 		       whereParameters = getParenthesisParameters(whereCommands);
		// 	    tokenArrayLength += 2;

		// 	    extendedOptionsArray = new String [] {"where", whereParameters};
		// 	}
		// }

		// ArrayList<String> tokenList = new ArrayList<String>(Arrays.asList(tier1Commands.split(" ")));
		// tokenList.addAll(Arrays.asList(optionArray));
		// tokenList.addAll(Arrays.asList(extendedOptionsArray));
		// tokenList.removeAll(Collections.singleton(null));
		// String [] commandTokens = tokenList.toArray(new String[tokenArrayLength]);
		// printStringArray(commandTokens, "token");
		// return commandTokens;
		return tokens.toArray(new String[tokens.size()]);
	}

	public static String getParenthesisParameters (String command) {
		try {
			return command.substring(command.indexOf("("), command.indexOf(")") + 1);
		} catch (Exception e) {
			invalidCommand();
		}
		return "INVALID PARAMETERS";
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

	public static void invalidCommand () {
		System.out.println("THE COMMAND YOU ENTERED CANNOT BE PARSED!");
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



