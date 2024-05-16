import java.util.ArrayList;
import java.io.*;
import javax.swing.*;
import java.awt.*;

/*
 * @author
 * Kian Agheli
 * 
 * References:
 * https://zetcode.com/java/listdirectory/
 * https://stackoverflow.com/questions/4871051/how-to-get-the-current-working-directory-in-java
 * 
 * Date:
 * 2024-05-08
 * 
 * Purpose of class:
 * Provide a view for comparing font families.
 */

public class ComparisonView extends JFrame {
	private final int WINDOW_MIN_WIDTH = 960;
	private final int WINDOW_MIN_HEIGHT = 540;
	private ArrayList<FontFamily> fonts;

	/**
	 * Walk the file tree.
	 */
	static void walk(File dir, ArrayList<File> list) {
		File[] dirContents = dir.listFiles();
		for (int i = 0; i < dirContents.length; i++) {
			if (dirContents[i].isFile()) {
				if (dirContents[i] != null) {
					list.add(dirContents[i]);
				}
			} else if (dirContents[i].isDirectory()) {
				walk(dirContents[i], list);
			}
			/* Otherwise, an irregular file. Ignore it. */
		}
	}

	/**
	 * Initialize GUI and comparison.
	 */
	public ComparisonView() {
		setTitle("Google Fonts Style vs. Popularity"); // Window title
		setMinimumSize(new Dimension(WINDOW_MIN_WIDTH,
			WINDOW_MIN_HEIGHT)); // Minimum window size
		/* On window close, kill the program. */
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		pack(); // Pack the GUI
		setVisible(true); // Make the window visible

		/* In $(find . | grep METADATA.pb), each
		family is described in JSON. Human name is provided on first line.
		For each METADATA.pb, instantiate a FontFamily object, keep
		in an ArrayList. */	
		//System.out.println(System.getProperty("user.dir"));
		ArrayList<File> fileList = new ArrayList<File>();
		try {
			walk(new File("."), fileList);
		} catch (Exception e) {
			System.out.println(e.getMessage()); // Print error message
			System.exit(1); // Exit with error
		}
		
		ArrayList<File> metadataList = new ArrayList<File>();
		/* Enhanced for loop. */
		for (File file : fileList) {
			if (file.getPath().endsWith(".pb")) {
				metadataList.add(file);
			}
		}
		
		File styleFile = new File("families.csv");
		if (!styleFile.exists()) {
			System.exit(-1);
		}
		
		File popularityFile = new File("popularity.json");
		if (!popularityFile.exists()) {
			System.exit(-1);
		}
		
		
		CSVReader style = new CSVReader(styleFile);
		JSONReader popularity = new JSONReader(popularityFile);
		/* Create FontFamily objects from each METADATA.pb file. */
		fonts = new ArrayList<FontFamily>();
		for (File file : metadataList) {
			JSONReader metadata = new JSONReader(file);
			metadata.setPb();
			fonts.add(new FontFamily(metadata, popularity, style));
		}
		
		for (FontFamily font : fonts) {
			System.out.println(font.getFamilyName());
			for (String subset : font.getSubsets()) {
				System.out.println(subset);
			}
		}
	}

	public static void main(String[] argv) {
		new ComparisonView();
	}
}
