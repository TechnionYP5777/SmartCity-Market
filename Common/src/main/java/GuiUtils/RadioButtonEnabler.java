package GuiUtils;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.RadioButton;

/**
 * Use this class to select only the gui selected radio button
 * 
 * @author shimon azulay
 * @since 2016-12-27
 */

public class RadioButtonEnabler {

	ArrayList<RadioButton> radioButtonContainer = new ArrayList<>();

	RadioButton selectedRadioButton;

	public void addRadioButton(RadioButton ¢) {
		radioButtonContainer.add(¢);
	}

	public void addRadioButtons(List<RadioButton> ¢) {
		radioButtonContainer.addAll(¢);
	}

	public void selectRadioButton(RadioButton b) {

		radioButtonContainer.forEach(btn -> btn.setSelected(btn.getText().equals(b.getText())));

	}

	public RadioButton getSelected() {

		radioButtonContainer.forEach(btn -> {
			if (btn.isSelected())
				selectedRadioButton = btn;
		});
		return selectedRadioButton;
	}

}
