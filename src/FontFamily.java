import java.util.ArrayList;
import java.util.HashMap;

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
 * Store and operate on font family metadata.
 */

public class FontFamily {
	/* The directory containing the associated METADATA.pb */
	private String directoryName;
	/* Taken from first line of METADATA.pb */
	private String familyName;
	/* Taken from fonts/tags/all/families.csv */
	private ArrayList<String> styles;
	/* Taken from top level of METADATA.pb */
	private String dateAdded;
	/* Average glyph count of every font file specified in METADATA.pb */
	private int glyphCount;
	/* Average file size of every font file specified in METADATA.pb */
	private int fileSize;
	/* Author from METADATA.pb */
	private String author;
	/* total views, 7day views, 30day views, 90day views, year views.
	Taken from popularity.json. */
	private HashMap<String,Long> views;
}
