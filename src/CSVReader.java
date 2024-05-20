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
 * 2024-05-20
 * 
 * Purpose of class:
 * Read from and interpret CSV files.
 */

public class CSVReader extends Reader {
	private String[] header; // A CSV file has-a header. One line, multiple fields.
	// Array of HashMaps. Headers are keys, values are on each line of the CSV following the header.
	private ArrayList<HashMap<String,String>> body; // A CSV file has-a body. Multiple lines, multiple fields.

	public CSVReader(File file) {
		super(file); // Call parent constructor
		/* Construct the body to be filled. */
		body = new ArrayList<HashMap<String,String>>();
		/* Parse the contents of the file, store in body. */
		parse();
	}

	/**
	 * Parse CSV file.
	 */
	public void parse() {
		/* Split the input on newlines. */
		String[] lines = this.getContents().split("\n");
		/* Split the first line by commas, assign to header array. */
		this.header = lines[0].split(",");

		/* Iterate over all lines of the body.
		First line is header, count from 1 instead of 0. */
		for (int i = 1; i < lines.length; i++) {
			/* Some lines have commas within parentheses.
			Angkor,"/South East Asian (Thai, Khmer, Lao)/Looped",100
			Remove everything within parentheses. We need to escape
			not on a language level, but on a function call level.
			Double-backslash for a literal backslash, which is then
			used to escape the parentheses in the regex library. */
			lines[i] = lines[i].replaceAll("\\(.*\\)", "");

			/* CSV, Comma-Separated Values. */
			String[] fields = lines[i].split(",");
			/* Allocate a new HashMap for the given line of the CSV. */
			HashMap<String,String> map = new HashMap<String,String>();

			/* Associate each of the line's fields with its header. */
			for (int j = 0; j < header.length; j++) {
				map.put(header[j], fields[j]);
			}
			/* Add the HashMap to the list. */
			body.add(map);
		}
	}
	
	/**
	 * @return the length of the body.
	 */
	public int getLength() {
		return body.size();
	}
	
	/**
	 * @return the value associated with the given key on the given line.
	 */
	public String get(int i, String key) {
		return body.get(i).get(key);
	}
}
