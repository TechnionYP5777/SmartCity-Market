package GuiUtils;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.RadioButton;

/**
 * Use this class to select only the gui selected radio button
 * @author shimon azulay
 *
 */

public class RadioButtonEnabler {

	ArrayList<RadioButton> radioButtonContainer = new ArrayList<>();

	public void addRadioButton(RadioButton radioButton) {
		radioButtonContainer.add(radioButton);
	}
	
	public void addRadioButtons(List<RadioButton> radiobuttons) {
		radioButtonContainer.addAll(radiobuttons);
	}

	public void selectRadioButton(RadioButton button) {
		
		radioButtonContainer.forEach(btn -> {
			btn.setSelected(btn.getText().equals(button.getText()));			
		});
		
	}

}
