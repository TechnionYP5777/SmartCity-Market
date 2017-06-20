package EmployeeGui;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import GuiUtils.AbstractApplicationScreen;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

public class SettingsScreen implements Initializable {

    @FXML
    private JFXTextField ipField;

    @FXML
    private JFXTextField portField;

    @FXML
    private JFXComboBox<String> fontSizeCombo;

    @FXML
    private JFXComboBox<String> logLevelCombo;

    @FXML
    private JFXButton applyBtn;
    
    private StackPane mainScreenPane;

    @FXML
    void applyBtnPressed(ActionEvent event) {

    }

    @FXML
    void backButtonPressed(MouseEvent event) {
    	AbstractApplicationScreen.setScene("/EmployeeLoginScreen/EmployeeLoginScreen.fxml");
    }

    @FXML
    void ipFieldPressed(ActionEvent event) {

    }

    @FXML
    void portFieldPressed(ActionEvent event) {

    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		AbstractApplicationScreen.fadeTransition(mainScreenPane);
		
		
	}

}
