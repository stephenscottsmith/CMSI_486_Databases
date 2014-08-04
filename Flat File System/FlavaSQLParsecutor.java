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
import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.io.FileWriter;

public class FlavaSQLParsecutor {
	static ArrayList <String> validCommands = new ArrayList<String>(Arrays.asList("create", 
		"delete", "insert", "select", "update"));
	static ArrayList <String> validObjects = new ArrayList<String>(Arrays.asList("database", 
		"table", "on"));
	static ArrayList <String> validOptions = new ArrayList<String>(Arrays.asList("index", 
		"values", "schema"));
	static ArrayList <String> validOptionParameters = new ArrayList<String>(Arrays.asList("where", "schema"));
	static ArrayList <String> validDatabaseTypes = new ArrayList<String>(Arrays.asList("long", "double", "char", "string", "boolean"));

	private int tokenLength;
	private String [] commandTokens;
	private String command;
	private String objectType;
	private String objectParameter;
	private String option = "";
	private String optionParameter = "";
	private String extendedOption = "";
	private String extendedOptionParameter = "";

	public FlavaSQLParsecutor (String [] commandTokens) {
		try {
			this.tokenLength = commandTokens.length;
			if (isValidTokenLength(tokenLength) &&
				objectTokensValid(Arrays.copyOfRange(commandTokens, 0, 2))) {

				// Set command and object tokens
				this.command = commandTokens[0].toLowerCase();
				this.objectType = commandTokens[1].toLowerCase();
				this.objectParameter = commandTokens[2];

				if (tokenLength > 3 && 
					optionTokensValid(Arrays.copyOfRange(commandTokens, 3, 5))) {
					// Set optional tokens
					this.option = commandTokens[3].toLowerCase();
					this.optionParameter = commandTokens[4];

					if (tokenLength > 5 && 
						extendedTokensValid(Arrays.copyOfRange(commandTokens, 5, tokenLength))) {
						// Set extended option tokens
						this.extendedOption = commandTokens[5].toLowerCase();
						this.extendedOptionParameter = commandTokens[6];
					}
				}
			}
		} catch (Exception e) {
			System.out.println("CANNOT PARSE YOUR COMMAND! IT CONTAINS INVALID SYNTAX TOKENS!");
		}
	}

	/** Constructor Validation **/
	public static Boolean isValidTokenLength (int length) {
		return (length == 3 || length == 5 || length == 7);
	}

	public static Boolean objectTokensValid (String [] objectTokens) {
		return ((validCommands.contains(objectTokens[0])) &&
				(validObjects.contains(objectTokens[1])));
	}

	public static Boolean optionTokensValid (String [] optionTokens) {
		// Need to validate structure of parameters
		return (validOptions.contains(optionTokens[0]));
	}

	public static Boolean extendedTokensValid (String [] extendedTokens) {
		// Need to validate structure of parameters
		return (extendedTokens[0].contains(extendedTokens[0]));
	}
	/** END Constructor Validation **/

	// Does the command, now that it contains correct tokens, make sense? i.e. Can you "insert on tableName"
	public Boolean isCommandExecutable () {
		return true;
	}

	public void execute () {
		switch (command) {
			case "create" :			
				createDatabaseItem();	
				// update database array
				break;
			case "delete" :
				deleteDatabaseItem();
				break;
			case "select" :
				System.out.println("selecting");
				break;
			case "insert" :
				System.out.println("inserting");
				insert();
				break;
			case "update" :
				System.out.println("updating");
				break;
		}
	}

	public String getFolderPath () {
		// TODO: REFACTOR THE STRING LITERALS!
		String folderPath = "./data/";
		if (objectType.equals("database")) {
			folderPath += objectParameter + ".fdb/";
		} else {
			String currentGlobalDatabase = Flava.getCurrentGlobalDatabase(),
				   tableExtension = ".ftl/";
			folderPath += currentGlobalDatabase + objectParameter + tableExtension;

			if (tokenLength > 3 && this.option.equals("index")) {
				System.out.println("THIS: " + tokenLength + ", " + option);
				String indexExtension = ".idx/";
				folderPath += this.optionParameter + indexExtension;
			}
		} 
		// TODO: Remove
		System.out.println("Folder path: " + folderPath);
		return folderPath;
	}

	public String getFilePath (String fileType) {
		String fileName = (option.equals("index")) ? optionParameter : objectParameter;
		// TODO: Remove
		// System.out.println("RETURNING : " + getFolderPath() + fileName + fileType);
		return getFolderPath() + fileName + fileType;
	}

	public void createDatabaseItem () {
		String pathString = getFolderPath();
		File objectFile = new File(pathString);

		if (!objectFile.exists()) {
			try {
				objectFile.mkdir();
				if (this.objectType.equals("database")) {
					Flava.updateDatabaseList();
				} else if (this.objectType.equals("table")) {
					File dataFile = new File(getFilePath(".data")),
						 schemaFile = new File(getFilePath(".schema"));
					dataFile.createNewFile();
					schemaFile.createNewFile();
					appendTextToFile(this.optionParameter, ".schema");
					// Insert the schema after it's been validated
				} else if (option.equals("index")) {
					File indexDataFile = new File(getFilePath(".idxd")),
						 indexSchemaFile = new File(getFilePath(".idxs"));
					indexDataFile.createNewFile();

					indexSchemaFile.createNewFile();
					// Insert the schema after it's been validated
					appendTextToFile(this.extendedOptionParameter, ".idxs");

				}
			} catch (IOException io) {
				System.out.println("WHY HERE");
			}
		} else {
			System.out.println("The " + (this.tokenLength > 3 ? this.option : this.objectType) + " " + 
							  (this.tokenLength > 3 ? this.optionParameter : this.objectParameter) + 
							  " already exists! Cannot create a duplicate " + this.objectType);
		}
				
	}

	public void deleteDatabaseItem () {
		File file = new File(getFolderPath());

		if (file.exists()) {
			try {
				String [] filesInFile = file.list();

				for (String fileString : filesInFile) {
				    File currentFile = new File(file.getPath(), fileString);
				    currentFile.delete();
				}
				file.delete();

			} catch (SecurityException se) {
				System.out.println("Cannot delete " + this.objectType + " " + 
								   this.objectParameter + "! You may not have " +
								   "permission to delete this item!");
			}
		} else {
			System.out.println("The " + this.objectType + " " + this.objectParameter + 
				" does not exist!");
		}
	}

	public void insert () {
		// Is object == on && does the tableName exist && is the option values &&
		// are the parameters properly formatted for the table it is being inserted
		// onto (i.e. check against schema)
		File table = new File(getFolderPath());
		if (this.objectType.equals("on") && table.exists() 
			&& this.option.equals("values") && areValuesProperlyFormatted()) {
			System.out.println("WILL INSERT");
			appendTextToFile(this.optionParameter, ".data");
		}
	}

	public void appendTextToFile (String text, String fileType) {
		PrintWriter out = null;
		try {
		    System.out.println("FILE PATH : " + getFilePath(fileType));
		    out = new PrintWriter(new BufferedWriter(new FileWriter(getFilePath(fileType), true)));
		    out.println(text);
		}catch (IOException e) {
		    System.err.println("PS 220 : Could not append text to file for some reason");
		}finally{
		    if(out != null){
		        out.close();
		    }
		} 
	}

	public Boolean areValuesProperlyFormatted () {
		
		return true;
	}

	public String toString () {
		return this.command + " " + this.objectType + " " + this.objectParameter + " " + 
			   this.option + " " + this.optionParameter + " " + this.extendedOption + " " +
			   this.extendedOptionParameter;
	}
}