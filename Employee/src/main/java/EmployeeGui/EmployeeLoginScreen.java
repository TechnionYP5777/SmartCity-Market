package EmployeeGui;

import java.net.URL;
import java.util.ResourceBundle;

import EmployeeCommon.TempWorkerPassingData;
import EmployeeContracts.IWorker;
import EmployeeImplementations.Worker;
import GuiUtils.AbstractApplicationScreen;
import SMExceptions.SMException;
import UtilsImplementations.InjectionFactory;
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

	private String username = "";
	private String password = "";
	@FXML
	private GridPane loginScreenPane;
	@FXML
	private Button loginButton;
	@FXML
	private Button backButton;
	@FXML
	private TextField userNameTextField;
	@FXML
	private PasswordField passwordField;

	@Override
	public void initialize(URL location, ResourceBundle __) {
		AbstractApplicationScreen.fadeTransition(loginScreenPane);
		userNameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			username = newValue;
			enableLoginButtonCheck();
		});

		passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
			password = newValue;
			enableLoginButtonCheck();
		});
		enableLoginButtonCheck();
		TempWorkerPassingData.worker = null;
	}

	@FXML
	private void backButtonPressed(ActionEvent __) {
		AbstractApplicationScreen.setScene("/EmployeeMainScreen/EmployeeMainScreen.fxml");
	}

	@FXML
	private void loginButtonPressed(ActionEvent __) {
		IWorker worker = InjectionFactory.getInstance(Worker.class);
		try {
			worker.login(username, password);
		} catch (SMException e) {
			EmployeeGuiExeptionHandler.handle(e);
			return;
		}
		TempWorkerPassingData.worker = worker;
		AbstractApplicationScreen.setScene("/WorkerMenuScreen/WorkerMenuScreen.fxml");

	}

	private void enableLoginButtonCheck() {
		loginButton.setDisable(username.isEmpty() || password.isEmpty());
	}
}
