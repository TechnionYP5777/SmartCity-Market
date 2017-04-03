package EmployeeGui;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXPasswordField;

import CommonDefs.CLIENT_TYPE;
import EmployeeCommon.EmployeeScreensParameterService;
import EmployeeContracts.IManager;
import EmployeeImplementations.Manager;
import GuiUtils.AbstractApplicationScreen;
import SMExceptions.SMException;
import UtilsContracts.IPersistentStore;
import UtilsImplementations.InjectionFactory;
import UtilsImplementations.XmlPersistentStore;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

/**
 * EmployeeLoginScreen - This class is the controller for the employee login
 * screen all action of this scene should be here.
 * 
 * @author Shimon Azulay
 * @since 2016-12-26
 */

public class EmployeeLoginScreen implements Initializable {

	@FXML
	private GridPane loginScreenPane;
	@FXML
	private Button loginButton;
	@FXML
	private Button backButton;
	@FXML
	private TextField userNameTextField;
	@FXML
	private JFXPasswordField passwordField;
	
	private IPersistentStore persistenceStore;

	@Override
	public void initialize(URL location, ResourceBundle __) {
		AbstractApplicationScreen.fadeTransition(loginScreenPane);
		userNameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			enableLoginButtonCheck();
		});

		passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
			enableLoginButtonCheck();
		});
		persistenceStore = InjectionFactory.getInstance(XmlPersistentStore.class);
	
		restoreState();
		
		enableLoginButtonCheck();
		
	}
	
	private void restoreState() {
		try {
			// TODO change textbox to history
			userNameTextField.setText(persistenceStore.restoreObject(new EmployeeUsers(), EmployeeUsers.class).users[0]);
		} catch (Exception e) {
			// TODO handle exception
			e.printStackTrace();
		}
	}

	@FXML
	private void backButtonPressed(ActionEvent __) {
		AbstractApplicationScreen.setScene("/EmployeeMainScreen/EmployeeMainScreen.fxml");
	}

	@FXML
	private void loginButtonPressed(ActionEvent __) {
		if (loginButton.isDisable())
			return;
		IManager employee = InjectionFactory.getInstance(Manager.class);
		CLIENT_TYPE employeeType = null;
		try {
			employeeType = employee.login(userNameTextField.getText(), passwordField.getText());
		} catch (SMException e) {
			EmployeeGuiExeptionHandler.handle(e);
			return;
		}
		InjectionFactory.getInstance(EmployeeScreensParameterService.class).setClientType(employeeType);

		AbstractApplicationScreen.setScene("/EmployeeMenuScreen/EmployeeMenuScreen.fxml");

	}

	private void enableLoginButtonCheck() {
		loginButton.setDisable(userNameTextField.getText().isEmpty() || passwordField.getText().isEmpty());
	}
	
	
}
