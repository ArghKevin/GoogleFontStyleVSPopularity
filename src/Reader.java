import java.io.*;
import java.nio.file.Files;
import java.nio.charset.Charset;
import java.util.List;

/*
 * @author
 * Kian Agheli
 * 
 * References:
 * https://www.baeldung.com/java-scanner
 * 
 * Date:
 * 2024-05-08
 * 
 * Purpose of class:
 * Read from a file.
 */

class Reader {
	private File file; // A Reader has-a file.
	private String contents; // A Reader has-a set of contents.
	
	public Reader(File file) {
		this.file = file;
		contents = null;
		try {
			/* Read the contents of the input file. Assume UTF-8.
			Files.readAllLines() automatically closes the file. */
			List<String> lines = Files.readAllLines(file.toPath(), Charset.forName("UTF-8"));
			/* Combine list into one string. */
			contents = String.join("\n", lines);
		/* On exception, exit. */
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}
	}
	
	/**
	 * @return the contents of the file.
	 */
	String getContents() {
		return contents;
	}
}
