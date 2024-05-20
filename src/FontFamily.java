import java.util.ArrayList;
import java.util.HashMap;

/*
 * @author
 * Kian Agheli
 * 
 * References:
 * 
 * Date:
 * 2024-05-20
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
	final String[] popularityMetrics = {"7day", "30day", "90day", "year"};
	private HashMap<String,Long> views;
	private String license; // Distribution license.
	private String category; // Vague category.
	/* popularity.json and families.csv are shared among all families.
	Have their associated objects passed from outside. */

	/**
	 * Constructor.
	 */
	public FontFamily(JSONReader metadata, JSONReader popularity, CSVReader styles) {
		this.metadata = metadata; // Set the instance's metadata to passed value. */
		parseMetadata(metadata); // Parse the metadata.

		views = new HashMap<String,Long>(); // Instantiate views hash map.
		parsePopularity(popularity); // Parse the popularity data

		this.styles = new HashMap<String,Integer>(); // Instantiate styles hash map
		parseStyles(styles); // Parse the styles data
	}

	/**
	 * Parse metadata.
	 */
	private void parseMetadata(JSONReader metadata) {
		/* Parse the given fields. See their values described near the top of the class definition. */
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
		String popularityJson = popularity.getFamily(familyName); // Block of JSON for this family
		if (popularityJson == null) {
			/* Not all families have popularity metadata, just most. */
			return;
		}

		/* For each timeframe provided in popularity.json, parse as long and add to hash map. */
		for (String timeframe : popularityMetrics) {
			String timeframeKey = "\"" + timeframe + "\":"; // JSON key
			int timeframeIndex = popularityJson.indexOf(timeframeKey); // Index of JSON key

			String viewsKey = "\"views\": "; // JSON key for views following timeframe key
			int viewsIndex = popularityJson.indexOf(viewsKey, timeframeIndex);

			int start = viewsIndex + viewsKey.length(); // Start of data after key
			int end = popularityJson.indexOf(",", start); // End of data before next comma

			long value = 0;
			/* Parse views as long. */
			try {
				value = Long.parseLong(popularityJson.substring(start, end));
			/* If fail to parse as long, something is wrong with popularity.json.
			2^63 is a big number to overflow. */
			} catch (NumberFormatException e) {
				System.out.println("Failed to parse " + e.getMessage());
				System.exit(-1);
			}

			views.put(timeframe, value); // Add the parsed long to the hash map
		}

		/* Parse total views for given family. */
		try {
			views.put("total", Long.parseLong(JSONReader.get(popularityJson, "totalViews")));
		/* If fail to parse as long, something is wrong with popularity.json.
		2^63 is a big number to overflow. */
		} catch (NumberFormatException e) {
				System.out.println("Failed to parse " + e.getMessage());
				System.exit(-1);
		}

	}

	/**
	 * Parse styles.
	 */
	private void parseStyles(CSVReader styleCsv) {
		int len = styleCsv.getLength(); // Lines of body in CSV

		/* Styles CSV has three header fields: Family, Group/Tag, and Weight. */
		for (int i = 0; i < len; i++) {
			/* If the current row contains data for the given family, add that data. */
			if (familyName.equals(styleCsv.get(i, "Family"))) {
				try {
					/* Add the Group/Tag and the associated weight. */
					styles.put(styleCsv.get(i, "Group/Tag"),
						Integer.parseInt(styleCsv.get(i, "Weight")));
				/* If unable to parse as integer, something is wrong with families.csv. */
				} catch (NumberFormatException e) {
					System.out.println("Unable to parse " + e.getMessage());
					System.exit(-1);
				}
			}

		}
	}
	
	/**
	 * @return family name
	 */
	public String getFamilyName() {
		return familyName;
	}

	/**
	 * @return unicode subsets.
	 */
	public String[] getSubsets() {
		return subsets;
	}

	/**
	 * @return views hash map
	 */
	public HashMap<String,Long> getViews() {
		return views;
	}
}
