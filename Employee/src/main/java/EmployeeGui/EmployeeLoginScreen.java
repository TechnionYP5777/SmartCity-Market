package EmployeeGui;

import java.net.URL;
import java.util.ResourceBundle;

import GuiUtils.AbstractApplicationScreen;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;


/**
 * EmployeeLoginScreen - This class is the controller for the employee login screen
 * all action of this scene should be here.
 * 
 * @author Shimon Azulay
 * @since 2016-12-26 */

public class EmployeeLoginScreen implements Initializable {

	@FXML
    private GridPane loginScreenPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	AbstractApplicationScreen.fadeTransition(loginScreenPane);
    }
}
