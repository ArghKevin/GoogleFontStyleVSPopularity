import java.util.HashMap;
import java.io.*;

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
 * Read from and interpret JSON files.
 */

public class JSONReader extends Reader {
	public JSONReader(File file) {
		super(file);
	}
	
	/*
	 * Search for and return the first
	 * match.
	 */
	public String get(String field) {
		return null;
	}

	/*
	 * Search for and return all matches.
	 */
	public String[] getAll(String field) {
		return null;
	}
	
	/*
	 * Search for and return a table of fields.
	 */
	public String getTable(HashMap<String,String> name) {
		return null;
	}
}
