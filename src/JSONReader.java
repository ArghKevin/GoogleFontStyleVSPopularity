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
 * https://stackoverflow.com/questions/8938498/get-the-index-of-a-pattern-in-a-string-using-regex
 * https://howtodoinjava.com/java/regex/start-end-of-string/
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
	private boolean isMetadata;

	public JSONReader(File file) {
		super(file); // Call constructor.
		isMetadata = false; // By default, not metadata.
	}
	
	private String composeKey(String key) {
		/* Call static version. This has the benefit of reducing code size.
		Rather than write two separate static and instantiated versions, the
		instantiated version may call the static version. */
		return composeKey(key, this.isMetadata);
	}

	private static String composeKey(String key, boolean isMetadata) {
		/* JSON usually has quotes surrounding keys. Google's metadata doesn't. */
		if (!isMetadata) {
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
		/* Call static version. */
		return JSONReader.get(this.getContents(), key, isMetadata);
	}

	public static String get(String json, String key) {
		/* No isMetadata boolean passed, presumably irrelevant. */
		return JSONReader.get(json, key, false);
	}

	/**
	 * Get a value given a key and a block of JSON to search.
	 */
	public static String get(String json, String key, boolean isMetadata) {
		key = composeKey(key, isMetadata); // Compose a key
		int start = json.indexOf(key); // Index of key
		if (start == -1) {
			/* No match. */
			return null;
		}
		
		start += key.length(); // Start of data past key
		/* This JSON is pretty-printed. */
		int nl = json.indexOf("\n", start);
		
		/* Extract substring. Remove any quotation marks and commas. */
		String substring = json.substring(start, nl);
		return substring.replaceAll("[\",]", "");
	}

	/**
	 * Search for and return all matches.
	 */
	public String[] getAll(String key) {
		key = composeKey(key); // Compose key
		
		ArrayList<String> list = new ArrayList<String>(); // ArrayList to compose output array
		int lastMatch = 0; // Previous match's index. Necessary for iteration through file contents.
		/* While matches exist, continue. */
		while ((lastMatch = this.getContents().indexOf(key, lastMatch)) != -1) {
			lastMatch += key.length(); // Data starts after key.
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
	 * @return a block of JSON associated with a font family.
	 * Heavily reliant on the format of popularity.json. Not
	 * suitable for generic JSON parsing.
	 */
	public String getFamily(String family) {
		/* Taking advantage of the specific structure 
		of popularity.json. Find the index of the family
		name declaration. Then, all items until the next line
		starting with '  }' are included. */
		String contents = this.getContents();
		int start = contents.indexOf("   \"family\": \"" + family + "\",");
		if (start == -1) {
			/* No match. */
			return null;
		}

		/* Restring input starting from family name declaration. */
		contents = contents.substring(start);

		String[] lines = contents.split("\n");
		int i = 0;
		/* In this format, an end block is guaranteed.
		Iterate through lines until the desired level of end
		block is found. */
		while (!lines[i].startsWith("  }")) {
			i++;
		}

		/* For each of the lines in the block preceding the end block,
		add to output. */
		String result = "";
		for (int j = 0; j < i; j++) {
			result += lines[j] + "\n";
		}

		return result;
	}
	
	/**
	 * Specify that this is the special pb metadata format. Almost JSON.
	 * See https://googlefonts.github.io/gf-guide/metadata.html. */
	public void setMetadata() {
		this.isMetadata = true;
	}
}
