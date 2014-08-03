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
	static ArrayList <String> validCommands = new ArrayList<String>(Arrays.asList("create", 
		"delete", "insert", "select", "update"));
	static ArrayList <String> validObjects = new ArrayList<String>(Arrays.asList("database", 
		"table", "on"));
	static ArrayList <String> validOptions = new ArrayList<String>(Arrays.asList("index", 
		"values", "schema"));
	ArrayList <String> validOptionParameters = new ArrayList<String>(Arrays.asList("where", "schema"));

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

				if (tokenLength > 3 && optionTokensValid(Arrays.copyOfRange(commandTokens, 3, 5))) {

					// Set optional tokens
					this.option = commandTokens[3].toLowerCase();
					this.optionParameter = commandTokens[4];

					if (tokenLength > 5 && extendedTokensValid(Arrays.copyOfRange(commandTokens, 5, tokenLength))) {

						// Set extended option tokens
						this.extendedOption = commandTokens[5].toLowerCase();
						this.extendedOptionParameter = commandTokens[6];

					}
				}
			}
			System.out.println(this.command + " " + this.objectType + " " + this.objectParameter + " " + this.option + " " + this.optionParameter + " " + this.extendedOption + " " + this.extendedOptionParameter);
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
				break;
			case "update" :
				System.out.println("updating");
				break;
		}
	}

	// DELETE SECTION // 


	// END DELETE SECTION //

	

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
		System.out.println("Folder path: " + folderPath);
		return folderPath;
	}

	public String getFilePath (String fileType) {
		String fileName = (tokenLength == 3) ? objectParameter : optionParameter;
		//System.out.println(getFolderPath() + fileName + fileType);
		return getFolderPath() + fileName + fileType;
	}

	// CREATE SECTION //
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
				} else if (option.equals("index")) {
					System.out.println("IDXD PATH : " + getFilePath(".idxd"));
					File indexDataFile = new File(getFilePath(".idxd")),
						 indexSchemaFile = new File(getFilePath(".idxs"));
					indexDataFile.createNewFile();
					indexSchemaFile.createNewFile();
				}
			} catch (IOException io) {
				System.out.println("WHY HERE");
			}
		} else {
			System.out.println("The " + this.objectType + " " + this.objectParameter + 
				" already exists! Cannot create a duplicate " + this.objectType);
		}
				
	}
	// END CREATE SECTION //

	public void deleteDatabaseItem () {
		String pathString = getFolderPath();
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
				System.out.println("Cannot delete " + this.objectType + " " + this.objectParameter + "!");
			}
		} else {
			System.out.println("The " + this.objectType + " " + this.objectParameter + 
				" does not exist!");
		}

	}

	public String toString () {
		return this.command + " " + this.objectType + " " + this.objectParameter + " " + 
			   this.option + " " + this.optionParameter + " " + this.extendedOption + " " +
			   this.extendedOptionParameter;
	}
}