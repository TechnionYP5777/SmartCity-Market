package EmployeeGui;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class ManageEmployeesTab implements Initializable{

    @FXML
    private Label userImg;

    @FXML
    private JFXTextField nameTxt;

    @FXML
    private JFXTextField userTxt;

    @FXML
    private JFXPasswordField passTxt;

    @FXML
    private JFXComboBox<?> securityCombo;

    @FXML
    private JFXTextField securityAnswerTxt;

    @FXML
    private JFXRadioButton workerRadioBtn;

    @FXML
    private JFXRadioButton managerRadioBtn;

    @FXML
    private JFXButton finishBtn;

    @FXML
    void finishBtnPressed(ActionEvent event) {

    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		
	}

}

