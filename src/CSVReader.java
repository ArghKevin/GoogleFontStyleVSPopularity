import java.io.*;

/*
 * @author
 * Kian Agheli
 * 
 * References:
 * 
 * Date:
 * 2024-04-28
 * 
 * Purpose of class:
 * Read from and interpret CSV files.
 */

public class CSVReader extends Reader {
	String[] header; // A CSV file has-a header. One line, multiple fields.
	String[][] body; // A CSV file has-a body. Multiple lines, multiple fields.

	public CSVReader(File file) {
		super(file); // Call parent constructor
	}
}
