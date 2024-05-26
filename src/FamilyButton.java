import javax.swing.JToggleButton;

/*
 * @author
 * Kian Agheli
 *
 * References:
 * https://www.geeksforgeeks.org/java-swing-jtogglebutton-class/
 *
 * Date:
 * 2024-05-25
 *
 * Purpose of class:
 * Provide an interative button to toggle FontFamily metadata.
 */

public class FamilyButton extends JToggleButton {
	private FontFamily family; // A FamilyButton has-a font family

	public FamilyButton(FontFamily family)
	{
		this.family = family; // Set family to provided
	}

	/**
	 * Return the FontFamily associated with the instantiated button.
	 */
	public FontFamily getFamily() {
		return family;
	}
}
