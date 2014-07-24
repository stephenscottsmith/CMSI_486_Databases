/*  
This is a flat file database management system from the command line
for my database system class (CMSI 486). I wrote it using Java 7. 
**/

import java.util.Scanner;
import java.util.Arrays;
import java.io.File;
import java.io.IOException;
import java.io.FilenameFilter;

public class Flava {
	private static String currentDatabase = "main.fdb";
	private static String [] databaseArray;
	private static String [] tableArray;

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
		if (directoryExists("./data")) {
			File databaseDirectory = new File("./data");
		} else {
			createDatabaseItem("data", "folder", "data");
			createDatabaseItem("./data/test.fdb", "database", "test");
		}
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

	public static void updateDatabasesArray (Boolean printArray) {
		databaseArray = getFilesInPath(("./data/"), ".fdb");
		printStringArray(databaseArray, "database");
	}

	public static void updateTablesArray () {
		tableArray = getFilesInPath(("./data/" + currentDatabase), ".ftl");
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

	public static void prompt () {
		System.out.print(">> ");
	}

	public static void invalidCommand () {
		System.out.println("THE COMMAND YOU ENTERED CANNOT BE PARSED!");
	}
}




