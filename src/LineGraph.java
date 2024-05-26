import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collections;
import java.util.Comparator;
import java.util.Calendar;
import java.util.Date;

/*
 * @author
 * Kian Agheli
 *
 * References:
 * https://stackoverflow.com/questions/8693342/drawing-a-simple-line-graph-in-java
 * https://www.geeksforgeeks.org/sorting-a-hashmap-according-to-values/
 * https://stackoverflow.com/questions/16252269/how-to-sort-a-list-arraylist
 * https://stackoverflow.com/questions/2839508/java2d-increase-the-line-width
 * https://stackoverflow.com/questions/4285464/java2d-graphics-anti-aliased
 * https://stackoverflow.com/questions/7182996/java-get-month-integer-from-date
 * https://stackoverflow.com/questions/5799140/java-get-month-string-from-integer
 *
 * Date:
 * 2024-05-25
 *
 * Purpose of class:
 * Draw a line graph.
 */

// A LineGraph is-a JPanel
public class LineGraph extends JPanel {
	private ArrayList<FontFamily> fonts; // A line graph has-a set of fonts
	private final int WIDTH = 640; // A line graph has a preferred width
	private final int HEIGHT = 480; // A line graph has a preferred height
	private final int LINES = 10; // A line graph has a maximum lines graphed at a time
	private final int LINE_WIDTH = 3; // A line graph has lines with a given pixel width
	private final int TICK_HEIGHT = 10; // A line graph has ticks with a given pixel height
	/* A line graph has an array of abbreviated month names. */
	private final String[] ABBREVIATED_MONTH = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul",
		"Aug", "Sep", "Oct", "Nov", "Dec"};
	/* A line graph has an array of unique color values to use for each of the lines.
	Made static so that it may be referenced from ComparisonView, so that the colors used for line
	drawings and their associated buttons may match. */
	private static final int[] COLORS = {0xff0000, 0x00ff00, 0x0000ff, 0xffff00, 0xff00ff, 0x00ffff,
		0xff8040, 0xff4080, 0x4080ff, 0xff8080, 0x80ff80, 0x8080ff,
		0xff4040, 0x40ff40, 0x4040ff};

	/**
	 * Constructor.
	 */
	public LineGraph(ArrayList<FontFamily> fonts) {
		this.fonts = fonts; // Set instance's list of fonts to the list passed.
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT)); // Set the preferred size of the panel.
	}

	/**
	 * Set a point at a pair of coordinates.
	 */
	void setCoordinates(int y, int x) {
	}

	/* Automatically called at construction time. */
	@Override
	public void paintComponent(Graphics g) {
		/* Cast to Graphics2D. Permits use of Graphics2D methods. */
		Graphics2D graphics = (Graphics2D) g;
		/* White background. */
		graphics.setBackground(Color.WHITE);
		/* Fill entire space allocated. No margins or padding. */
		graphics.clearRect(0, 0, getWidth(), getHeight());
		/* Enable anti-aliasing. */
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		/* Set line width. */
		graphics.setStroke(new BasicStroke(LINE_WIDTH));
		/* Draw in black. */
		graphics.setColor(Color.BLACK);
		/* Store font metrics so as to calculate width and height of each tick's text. */
		FontMetrics metrics = graphics.getFontMetrics();
		/* Store enough space on the left for 12 digits. Assuming tabular figures. */
		int xOffset = metrics.stringWidth("012345678901");
		/* Keep enough space at bottom for text with descenders and ticks. */
		int yMax = getHeight() - (metrics.getHeight() << 1) - TICK_HEIGHT;
		/* Keep enough space at right for the right half of the text "MMM". */
		int xMax = getWidth() - (metrics.stringWidth("MMM") >> 1) - TICK_HEIGHT - xOffset;
		/* Draw x axis. */
		graphics.drawLine(TICK_HEIGHT + xOffset, yMax,
			getWidth(), yMax);

		/* Store calendar, so as to get month. */
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date); // Set calendar to current date

		/* For each month in the year, draw a tick on the x axis. */
		int x = xMax;
		int xTicks = 12;
		double xSpace = (float) x / xTicks;

		for (int i = 0; i <= xTicks; i++) {
			graphics.drawLine(x + TICK_HEIGHT + xOffset, yMax,
				x + TICK_HEIGHT + xOffset, getHeight() - metrics.getHeight());
			String month = ABBREVIATED_MONTH[calendar.get(Calendar.MONTH)];
			/* Half the width for centering offset. */
			int textOffset = metrics.stringWidth(month) >> 1;
			graphics.drawString(month, x + TICK_HEIGHT + xOffset - textOffset,
				getHeight() - metrics.getHeight() / 5 * 2);
			x = (int) (x - xSpace); // Decrement x offset.
			calendar.roll(Calendar.MONTH, false); // Roll back by one month
		}

		/* Draw y axis. Subtract one third of stroke width to account for width of first tick. */
		graphics.drawLine(TICK_HEIGHT + xOffset - LINE_WIDTH / 3, 0, TICK_HEIGHT + xOffset -
			LINE_WIDTH / 3, yMax);

		int y = 0 + metrics.getHeight();
		/* Cast to double within parentheses for floating-point
		division, cast back to int to remove fractional part.
		5/4 spacing for legibility. */
		int yTicks = (int) (yMax / ((double) metrics.getHeight() * 5 / 4));
		/* Sorted values by monthly views in descending order. The greatest value is at the top. */
		long maxViews = fonts.get(0).getViews().get("30day");
		float ySpace = (float) yMax / yTicks;
		for (int i = yTicks; i >= 0; i--) {
			String text = String.format("%d", maxViews * i / yTicks);
			/* Difference between maximum string width and actual string width is offset. */
			int textOff = xOffset - metrics.stringWidth(text);
			graphics.drawString(text, textOff, y);
			y += ySpace;
		}

		/* Draw lines. */
		for (int i = 0; i < LINES; i++) {
			FontFamily font = fonts.get(i); // Keep the current font handy.
			HashMap<String,Long> views = font.getViews(); // Keep the views hashmap handy.

			/* Map of coordinates to draw. */
			HashMap<Integer,Long> coordinates = new HashMap<Integer,Long>();

			/* Use the given metrics to guesstimate the values of each month.
			The last month's views are known. */
			coordinates.put(12, views.get("30day"));

			/* Using 90day views, we may subtract 30day to get a closer approximation
			of 60day views. */
			coordinates.put(11, (views.get("90day") - views.get("30day")) / 2);

			/* Account for changes in views by deriving views 3 months ago from 90day
			views / 3. */
			coordinates.put(10, (views.get("90day") / 3));

			
			/* Guesstimate the rest of the months by yearly views / 12. */
			for (int j = 1; j <= 9; j++) {
				coordinates.put(j, (views.get("year") / 12));
			}

			graphics.setColor(new Color(COLORS[i])); // Set the drawing color.

			/* Previous x and y values. Necessary for drawing lines between points. */
			int xPrevious = 0;
			int yPrevious = 0;
			for (int xCoordinate = 1; xCoordinate <= 12; xCoordinate++) {
				/* x is ratio of month value / 12.
				Because y starts at the top, invert with subtraction from maximum pixel value.
				Ratio of given views over maximum views, multiplied by maximum pixel value.
				Circle with radius of LINE_WIDTH * 2. */
				int xCurrent = xOffset + TICK_HEIGHT + xMax * xCoordinate / 12;
				int yCurrent = yMax - (int) ((float) coordinates.get(xCoordinate) /
					maxViews * yMax);
				graphics.fillOval(xCurrent, yCurrent, LINE_WIDTH << 1, LINE_WIDTH << 1);
				
				/* Line to previous point, if set. */
				if (xPrevious == 0) {
					/* If no previous point, use current y value. */
					graphics.drawLine(xOffset + TICK_HEIGHT + xMax * (xCoordinate - 1) / 12,
						yCurrent, xCurrent, yCurrent);
				} else {
					/* Line from previous x and y to current x and y. */
					graphics.drawLine(xPrevious, yPrevious, xCurrent, yCurrent);
				}

				/* Set previous values for next iteration. */
				yPrevious = yCurrent;
				xPrevious = xCurrent;
			}
		}
	}

	/* Return the array of hex values used to color the lines on the graph.
	The background color of each FamilyButton match the associated line on the graph. */
	public static int[] getColors() {
		return COLORS;
	}
}
