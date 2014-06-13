import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Arrays;
import java.io.IOException;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.util.Scanner;

public class flatdb {
	public static void main(String[] args) {
		// String newRecord = Arrays.toString(args);
		// System.out.println(newRecord);
		String commandList = "exit\t\t: Exits the FlatDB system.\n" +
							 "add\t\t: Adds new record.\n" + 
							 "delete\t\t : Deletes a record.\n";

		// // http://stackoverflow.com/questions/1625234/how-to-append-text-to-an-existing-file-in-java
		// try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("flat.txt", true)))) {
		//     out.println(newRecord);
		// } catch (IOException e) {
		//     //exception handling left as an exercise for the reader
		// }
		Scanner sc = new Scanner(System.in);

		for (prompt(); sc.hasNextLine(); prompt()) {

		    String line = sc.nextLine().replaceAll("\n", "");

		    // split line into arguments
		    String[] commands = line.split(" ");    
		    System.out.println("Commands length: " + commands.length);
		    // process arguments
		    if (commands.length == 0) {
		    	System.out.println("Continuing");
		    	continue;
		    } else if (commands.length == 1) {
		        if (commands[0].equalsIgnoreCase("exit")) {
		        	System.exit(0);
		        } else {
		        	System.out.println("Your command was not a valid FlatDB command! \n" + commandList);
		        }
		    } else if (commands.length == 2) {
		        // do stuff with parameters
		    } 
		}
	}

	public static void prompt () {
		System.out.print(">> ");
	}

	// add new element

	// find element

	// update element

	// delete element

	// delete all elements
}
