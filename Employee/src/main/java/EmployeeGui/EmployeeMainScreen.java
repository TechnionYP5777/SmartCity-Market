package EmployeeGui;

import java.net.URL;
import java.util.ResourceBundle;

import GuiUtils.AbstractApplicationScreen;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

/**
 * EmployeeMainScreen - This class is the controller for the employee main screen
 * all action of this scene should be here.
 * 
 * @author Shimon Azulay
 * @since 2016-12-26 */

public class EmployeeMainScreen implements Initializable {

	@FXML
	private VBox mainScreenPane;

	@Override
	public void initialize(URL location, ResourceBundle __) {
		AbstractApplicationScreen.fadeTransition(mainScreenPane);
	}

	@FXML
	public void mouseClicked(MouseEvent __) {
		AbstractApplicationScreen.setScene("/EmployeeLoginScreen/EmployeeLoginScreen.fxml");
	}

}
