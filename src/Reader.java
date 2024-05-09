import java.io.*;
import java.util.Scanner;

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
 * Read from a file.
 */

class Reader {
	private File file; // A Reader has-a file.
	private String contents; // A Reader has-a set of contents.
	
	public Reader(File file) {
		this.file = file;
		contents = null;
		Scanner scan = null;
		try {
			/* Scan over the contents of the input file. */
			scan = new Scanner(file);
			/* Save to object. */
			contents = scan.nextLine() + "\n";
			while (scan.hasNextLine()) {
				contents += scan.nextLine();
				contents += "\n";
			}
		/* On exception, exit. */
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.exit(1);
		} finally {
			if (scan != null) {
				scan.close();
			}
		}
	}
	
	/*
	 * @return the contents of the file.
	 */
	String getContents() {
		return contents;
	}
}
