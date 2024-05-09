import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Arrays;

/*
 * @author
 * Kian Agheli
 * 
 * References:
 * 
 * Date:
 * 2024-05-08
 * 
 * Purpose of class:
 * Read from and interpret CSV files.
 */

public class CSVReader extends Reader {
	String[] header; // A CSV file has-a header. One line, multiple fields.
	/* Array of Hash map from header to value for each
	line past the header. */
	ArrayList<HashMap<String,String>> body; // A CSV file has-a body. Multiple lines, multiple fields.

	public CSVReader(File file) {
		super(file); // Call parent constructor
		body = new ArrayList<HashMap<String,String>>();
		parse();
	}

	/*
	 * Parse CSV file.
	 */
	public void parse() {
		String[] lines = this.getContents().split("\n");
		this.header = lines[0].split(",");
		System.out.printf("%d\n", header.length);

		/* Iterate over all lines of the body. */
		for (int i = 1; i < lines.length; i++) {
			String[] fields = lines[i].split(",");
			HashMap<String,String> map = new HashMap<String,String>();
			/* For each of the columns, add the associated
			value in this row. */
			for (int j = 0; j < header.length; j++) {
				map.put(header[j], fields[j]);
			}
			body.add(map);
		}

		for (String head : this.header) {
			System.out.printf("%s\t", head);
		}
		System.out.println();
		for (HashMap<String,String> map : body) {
			for (int i = 0; i < header.length; i++) {
				System.out.printf("%s\t", map.get(header[i]));
			}
			System.out.println();
		}
	}
}
