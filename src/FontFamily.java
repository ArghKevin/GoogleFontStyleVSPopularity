import java.util.ArrayList;
import java.util.HashMap;

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
 * Store and operate on font family metadata.
 */

public class FontFamily {
	/* JSONReader from which metadata is pulled. */
	private JSONReader metadata;
	/* Taken from first line of METADATA.pb */
	private String familyName;
	/* Taken from fonts/tags/all/families.csv */
	private HashMap<String,Integer> styles;
	/* Taken from top level of METADATA.pb */
	private String dateAdded;
	/* Unicode ranges. */
	private String[] subsets;
	private String designer;
	/* total views, 7day views, 30day views, 90day views, year views.
	Taken from popularity.json. */
	private HashMap<String,Long> views;
	private String license;
	private String category;
	/* popularity.json and families.csv are shared among all families.
	Have their associated objects passed from outside. */

	/**
	 * Constructor.
	 */
	public FontFamily(JSONReader metadata, JSONReader popularity, CSVReader styles) {
		this.metadata = metadata;
		parseMetadata(metadata);
		parsePopularity(popularity);
		parseStyles(styles);
	}

	/**
	 * Parse metadata.
	 */
	private void parseMetadata(JSONReader metadata) {
		familyName = metadata.get("name");
		dateAdded = metadata.get("date_added");
		subsets = metadata.getAll("subsets");
		designer = metadata.get("designer");
		license = metadata.get("license");
		category = metadata.get("category");
	}

	/**
	 * Parse popularity.
	 */
	private void parsePopularity(JSONReader popularity) {
	}

	/**
	 * Parse styles.
	 */
	private void parseStyles(CSVReader styles) {
	}
	
	public String getFamilyName() {
		return familyName;
	}
	public String[] getSubsets() {
		return subsets;
	}
}
