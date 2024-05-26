import javax.swing.JTextArea;
import java.awt.*;

/*
 * @author
 * Kian Agheli
 *
 * References:
 * https://docs.oracle.com/javase/tutorial/uiswing/components/textarea.html
 *
 * Date:
 * 2024-05-25
 *
 * Purpose of class:
 * Provide an area to show metadata.
 */

public class MetadataView extends JTextArea {
	// A MetadataView has-a default text.
	private final String defaultText = "Toggle a font family using a colorful button below.\n" + 
		"Each color corresponds with a line on the graph to the right.\n" + 
		"The graph tracks monthly views.";

	public MetadataView() {
		super(); // Call parent constructor
		setEditable(false); // Disable editing of the widget.
		setFocusable(false); // Disable focusing of the widget.
		setText(defaultText); // Set to the default text.
	}

	/* Reset the MetadataView. Set the text to the default. */
	public void reset() {
		setText(defaultText);
	}
}
