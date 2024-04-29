import java.io.*;

/*
 * @author
 * Kian Agheli
 * 
 * References:
 * https://jqlang.github.io/jq/manual/
 * 
 * Date:
 * 2024-04-28
 * 
 * Purpose of class:
 * Read from and interpret JSON files.
 */

public class JSONReader extends Reader {
	public JSONReader(File file) {
		super(file);
	}
	
	/* Something akin to jq's field selection. */
	public String getField(String field) {
		return null;
	}
}
