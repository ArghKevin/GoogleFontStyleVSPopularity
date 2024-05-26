import java.awt.event.*;

/*
 * @author
 * Kian Agheli
 *
 * References:
 *
 * Date:
 * 2024-05-25
 *
 * Purpose of class:
 * Listen for state changes in an instantiation of FamilyButton.
 */

// FamilyButtonListener is an ItemListener
public class FamilyButtonListener implements ItemListener
{
	private MetadataView metadataView; // A FamilyButtonListener has a metadataView
	private FamilyButton[] buttons; // A FamilyButtonListener has an array of buttons
	private int buttonIndex; // A FamilyButtonListener has a button at a specified index in the array

	public FamilyButtonListener(FamilyButton[] buttons, int buttonIndex, MetadataView metadataView) {
		/* Set each of the instance's fields to the given objects. */
		this.buttons = buttons;
		this.buttonIndex = buttonIndex;
		this.metadataView = metadataView;
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		/* Get the state of the button. */
		int state = e.getStateChange();
		/* If the button is selected: */
		if (state == ItemEvent.SELECTED) {
			/* Deselect all other buttons. */
			for (int i = 0; i < buttons.length; i++) {
				if (i != buttonIndex) {
					buttons[i].setSelected(false);
				}
			}

			/* Set the metadata on display to the metadata of the FontFamily
			associated with the selected button. */
			metadataView.setText(buttons[buttonIndex].getFamily().toString());
		/* If the button is deselected: */
		} else {
			/* Check if every button is deselected. */
			boolean selected = true;
			for (int i = 0; i < buttons.length; i++) {
				if (!buttons[i].isSelected()) {
					selected = false;
				}
			}

			/* If every button is deselected, reset the metadata to display
			the default text. */
			if (!selected) {
				metadataView.reset();
			}
		}
	}
}
