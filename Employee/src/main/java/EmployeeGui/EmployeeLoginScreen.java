package EmployeeGui;

import java.net.URL;
import java.util.ResourceBundle;

import EmployeeContracts.IWorker;
import EmployeeDI.WorkerDiConfigurator;
import EmployeeDefs.AEmployeeExceptions.AuthenticationError;
import EmployeeDefs.AEmployeeExceptions.CriticalError;
import EmployeeDefs.AEmployeeExceptions.EmployeeAlreadyConnected;
import EmployeeDefs.AEmployeeExceptions.InvalidParameter;
import EmployeeDefs.EmployeeGuiDefs;
import EmployeeImplementations.Worker;
import GuiUtils.AbstractApplicationScreen;
import GuiUtils.DialogMessagesService;
import UtilsImplementations.InjectionFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
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
	private RadioButton loginAsWorkerButton;
	@FXML
	private RadioButton loginAsManagerButton;
	@FXML
	private TextField userNameTextField;
	@FXML
	private PasswordField passwordField;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
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
	}

	@FXML
	private void backButtonPressed(ActionEvent e) {
		AbstractApplicationScreen.setScene("/EmployeeMainScreen/EmployeeMainScreen.fxml");
	}

	@FXML
	private void loginButtonPressed(ActionEvent event) {
		if (loginAsWorkerButton.isSelected()) {
			IWorker worker = InjectionFactory.getInstance(Worker.class, new WorkerDiConfigurator());
			try {
				worker.login(username, password);
			} catch (InvalidParameter e) {
				// TODO
			} catch (CriticalError e) {
				// TODO
			} catch (EmployeeAlreadyConnected e) {
				DialogMessagesService.showErrorDialog(EmployeeGuiDefs.loginFailureDialogTitle, null,
						EmployeeGuiDefs.userAlreadyConnectedFailureMessage);
				return;
			} catch (AuthenticationError e) {
				DialogMessagesService.showErrorDialog(EmployeeGuiDefs.loginFailureDialogTitle, null,
						EmployeeGuiDefs.wrongUserNamePasswordFailureMessage);
				return;
			}
			AbstractApplicationScreen.setScene("/WorkerMenuScreen/WorkerMenuScreen.fxml");

		} else {
			// TODO add manager
		}

	}

	private void enableLoginButtonCheck() {
		loginButton.setDisable(username.isEmpty() || password.isEmpty());
	}

	@FXML
	private void loginAsWorkerPressed(ActionEvent e) {
		loginAsManagerButton.setSelected(false);
	}

	@FXML
	private void loginAsManagerPressed(ActionEvent e) {
		loginAsWorkerButton.setSelected(false);
	}
}
