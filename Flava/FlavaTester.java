public class FlavaTester {
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		// Create a table to store a shit ton of records
		String [] createTableCommand = new String [] {"create", "table", "lmu", "schema", 
													  "(firstName string, lastName string, age long, isStudent boolean)"};
		FlavaSQLParsecutor fp = new FlavaSQLParsecutor(createTableCommand);
		fp.execute();
		String [] insertCommand = new String [5];
		insertCommand[0] = "insert";
		insertCommand[1] = "on";
		insertCommand[2] = "lmu";
		insertCommand[3] = "values";

		// Now add a shit ton of records
		for (int i = 0; i < 1000000; i++) {
			insertCommand[4] = "(Steve, Smith, " + i + ", true)";
			fp = new FlavaSQLParsecutor(insertCommand);
			fp.execute();
			// System.out.println(i);
		}
		long end = System.currentTimeMillis();
		System.out.println((end - start) + " milliseconds!");
	}
}