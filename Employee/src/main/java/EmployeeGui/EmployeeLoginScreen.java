package EmployeeGui;

import java.net.URL;
import java.util.ResourceBundle;

import GuiUtils.AbstractApplicationScreen;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;


/**
 * EmployeeLoginScreen - This class is the controller for the employee login screen
 * all action of this scene should be here.
 * 
 * @author Shimon Azulay
 * @since 2016-12-26 */

public class EmployeeLoginScreen implements Initializable {
	
	private String username;
	private String password;

	@FXML
    private GridPane loginScreenPane;
	
	@FXML
	private Button backButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	AbstractApplicationScreen.fadeTransition(loginScreenPane);
    }
    
	@FXML
    private void backButtonClicked(ActionEvent e) {
		AbstractApplicationScreen.setScene("/EmployeeMainScreen/EmployeeMainScreen.fxml");
	}
	
	
}
