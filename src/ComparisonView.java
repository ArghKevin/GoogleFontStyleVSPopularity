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
 * https://stackoverflow.com/questions/2501861/how-can-i-remove-a-jpanel-from-a-jframe
 * https://stackoverflow.com/questions/5652344/how-can-i-use-a-custom-font-in-java
 * https://www.javaprogramto.com/2019/03/java-uimanager.html
 * 
 * 
 * Date:
 * 2024-05-08
 * 
 * Purpose of class:
 * Provide a view for comparing font families.
 */

// ComparisonView is a JFrame
public class ComparisonView extends JFrame {
	private final int WINDOW_MIN_WIDTH = 960; // A ComparisonView has a minimum height
	private final int WINDOW_MIN_HEIGHT = 540; // A ComparisonView has a minimum width
	private ArrayList<FontFamily> fonts; // A ComparisonView has a list of fonts
	private Font textFont; // A ComparisonView has a preferred text font
	private final int TEXT_SIZE = 20; // A ComparisonView has a constant text size.

	/**
	 * Walk the file tree.
	 */
	static void walk(File dir, ArrayList<File> list) {
		File[] dirContents = dir.listFiles(); // List the files in the directory.
		// For each of the items in the directory:
		for (int i = 0; i < dirContents.length; i++) {
			// If it's a file, and its name ends with '.pb', add it to the list.
			if (dirContents[i].isFile() && dirContents[i].getName().endsWith(".pb")) {
				list.add(dirContents[i]);
			// If it's a directory, recurse through it.
			} else if (dirContents[i].isDirectory()) {
				walk(dirContents[i], list);
			}
			// Otherwise, it's an irregular file. Ignore it.
		}
	}

	/**
	 * Initialize GUI and comparison.
	 */
	public ComparisonView() {
		/* Set text font. Register the included ttf file as a font
		in the current environment. This is akin to adding it to the map
		of font family names and file names. */
		try {
			GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
			environment.registerFont(Font.createFont(Font.TRUETYPE_FONT,
				new File("./DejaVuSansCondensed.ttf")));
		/* If either IOException or FontFormatException are thrown, error out. */
		} catch (IOException|FontFormatException e) {
			System.out.println(e.getMessage());
			System.exit(-1);
		}
		
		/* Set default text font to included DejaVu Sans Condensed. */
		textFont = new Font("DejaVu Sans Condensed", Font.PLAIN, TEXT_SIZE);
		getContentPane().setFont(textFont);
		UIManager.put("Label.font", textFont);
		UIManager.put("Panel.font", textFont);


		setTitle("Google Fonts Style vs. Popularity"); // Window title
		setMinimumSize(new Dimension(WINDOW_MIN_WIDTH, WINDOW_MIN_HEIGHT)); // Minimum window size
		/* On window close, kill the program. */
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		pack(); // Pack the GUI
		setVisible(true); // Make the window visible

		/* In metadata/*.pb, each font is described.
		Add each of the files in that directory to a list. */
		ArrayList<File> metadataList = new ArrayList<File>();
		try {
			walk(new File("."), metadataList);
		} catch (Exception e) {
			System.out.println(e.getMessage()); // Print error message
			System.exit(-1); // Exit with error
		}
		
		/* Open families.csv and popularity.json. If either are missing,
		the program cannot run. */
		File styleFile = new File("families.csv");
		if (!styleFile.exists()) {
			System.out.println("families.csv doesn't exist.");
			System.exit(-1);
		}
		File popularityFile = new File("popularity.json");
		if (!popularityFile.exists()) {
			System.exit(-1);
		}
		
		/* Panel to show while waiting for FontFamily data structures to populate. */
		JPanel waiting = new JPanel();	
		JLabel waitingLabel = new JLabel("Memory structures take 10-15 seconds to set up. My apologies.");
		waiting.add(waitingLabel);
		this.add(waiting, BorderLayout.CENTER);
		setVisible(true);
		
		/* Construct Readers from each of the metadata files. */
		CSVReader style = new CSVReader(styleFile);
		JSONReader popularity = new JSONReader(popularityFile);
		// Create FontFamily objects from each METADATA.pb file.
		fonts = new ArrayList<FontFamily>();
		/* For each metadata file, parse a FontFamily. */
		for (File file : metadataList) {
			JSONReader metadata = new JSONReader(file);
			metadata.setMetadata();
			fonts.add(new FontFamily(metadata, popularity, style));
		}

		waiting.remove(waitingLabel); // Remove waiting message.
		JPanel author = new JPanel();
		JLabel authorLabel = new JLabel("Written by Kian Agheli");
		author.add(authorLabel);
		add(author, BorderLayout.SOUTH); // Add author panel.

		LineGraph graph = new LineGraph(fonts); // Draw a line graph of the font views metadata.
		add(graph, BorderLayout.NORTH); // Add the line graph.

		pack(); // Pack the window.
		setVisible(true); // Re-draw the window.
	}

	public static void main(String[] argv) {
		new ComparisonView(); // Call the constructor.
	}
}
