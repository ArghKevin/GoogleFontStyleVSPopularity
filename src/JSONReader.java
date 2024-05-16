import java.util.HashMap;
import java.util.ArrayList;
import java.io.*;

/*
 * @author
 * Kian Agheli
 * 
 * References:
 * https://googlefonts.github.io/gf-guide/metadata.html
 * https://stackoverflow.com/questions/3880274/how-to-convert-the-object-to-string-in-java
 * 
 * Date:
 * 2024-05-08
 * 
 * Purpose of class:
 * Read from and interpret JSON files.
 */

public class JSONReader extends Reader {
	/* metadata.pb is not exactly JSON, but so close to it
	that it's practically a subset of JSON with fewer quotation
	marks. If set, this is a pb file. */
	private boolean pb;

	public JSONReader(File file) {
		super(file);
		pb = false;
	}
	
	private String composeKey(String key) {
		if (!pb) {
			key = "\"" + key + "\"";
		}
		key += ": ";
		
		return key;
	}
	
	/**
	 * Search for and return the first
	 * match.
	 */
	public String get(String key) {
		key = composeKey(key);
		
		int start = this.getContents().indexOf(key);
		if (start == -1) {
			/* No match. */
			return null;
		}
		
		start += key.length();
		/* This JSON is pretty-printed. */
		int nl = this.getContents().indexOf("\n", start);
		
		/* Extract substring. Remove any quotation marks and commas. */
		String substring = this.getContents().substring(start, nl);
		return substring.replaceAll("[\",]", "");
	}

	/**
	 * Search for and return all matches.
	 */
	public String[] getAll(String key) {
		key = composeKey(key);
		
		ArrayList<String> list = new ArrayList<String>();
		int lastMatch = 0;
		while ((lastMatch = this.getContents().indexOf(key, lastMatch)) != -1) {
			lastMatch += key.length();
			int start = lastMatch;
			/* This JSON is pretty-printed. */
			int nl = this.getContents().indexOf("\n", start);
			if (nl == -1) {
				/* EOF. */
				nl = this.getContents().length();
			}
			
			/* Extract substring. Remove any quotation marks and commas. */
			String substring = this.getContents().substring(start, nl);
			list.add(substring.replaceAll("[\",]", ""));
		}
		
		/* The caller doesn't need the capabilites of an ArrayList, only
		those of an array. */
		String[] array = new String[list.size()];
		list.toArray(array);
		return array;
	}
	
	/**
	 * Search for and return a table of fields.
	 */
	public String getTable(HashMap<String,String> name) {
		return null;
	}
	
	/**
	 * Specify that this is the special PB format.
	 * See https://googlefonts.github.io/gf-guide/metadata.html. */
	public void setPb() {
		this.pb = true;
	}
}
