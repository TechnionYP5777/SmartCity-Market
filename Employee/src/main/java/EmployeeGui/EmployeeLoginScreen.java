package EmployeeGui;

import java.net.URL;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.controlsfx.control.textfield.TextFields;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import CommonDefs.CLIENT_TYPE;
import EmployeeCommon.EmployeeScreensParameterService;
import EmployeeContracts.IManager;
import EmployeeImplementations.Manager;
import EmployeeImplementations.Worker;
import GuiUtils.AbstractApplicationScreen;
import GuiUtils.ForgetPasswordWizard;
import SMExceptions.SMException;
import UtilsContracts.IForgotPasswordHandler;
import UtilsContracts.IPersistentStore;
import UtilsImplementations.InjectionFactory;
import UtilsImplementations.StackTraceUtil;
import UtilsImplementations.XmlPersistentStore;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

/**
 * EmployeeLoginScreen - This class is the controller for the employee login
 * screen all action of this scene should be here.
 * 
 * @author Shimon Azulay
 * @since 2016-12-26
 */

public class EmployeeLoginScreen implements Initializable {

	protected static Logger log = Logger.getLogger(EmployeeLoginScreen.class.getName());

	@FXML
	private GridPane loginScreenPane;
	@FXML
	private Button loginButton;
	@FXML
	private Button backButton;
	@FXML
	private JFXTextField userNameTextField;
	@FXML
	private JFXPasswordField passwordField;

	private IPersistentStore persistenceStore;

	@Override
	public void initialize(URL location, ResourceBundle __) {
		AbstractApplicationScreen.fadeTransition(loginScreenPane);
		userNameTextField.textProperty().addListener((observable, oldValue, newValue) -> enableLoginButtonCheck());

		passwordField.textProperty().addListener((observable, oldValue, newValue) -> enableLoginButtonCheck());
		persistenceStore = InjectionFactory.getInstance(XmlPersistentStore.class);

		restoreState();

		enableLoginButtonCheck();

	}

	private void restoreState() {
		try {

			TextFields.bindAutoCompletion(userNameTextField,
					persistenceStore.restoreObject(new EmployeeUsers(), EmployeeUsers.class).users);

		} catch (Exception e) {
			log.fatal(e);
			log.debug(StackTraceUtil.stackTraceToStr(e));
		}
	}

	@FXML
	private void backButtonPressed(MouseEvent __) {
		AbstractApplicationScreen.setScene("/EmployeeMainScreen/EmployeeMainScreen.fxml");
	}

	@FXML
	private void loginButtonPressed(ActionEvent __) {
		if (loginButton.isDisable())
			return;
		IManager employee = InjectionFactory.getInstance(Manager.class);
		CLIENT_TYPE employeeType = null;
		try {
			employeeType = employee.login(userNameTextField.getText(), passwordField.getText(), true);
		} catch (SMException e) {
			log.fatal(e);
			log.debug(StackTraceUtil.stackTraceToStr(e));
			e.showInfoToUser();
			return;
		}
		InjectionFactory.getInstance(EmployeeScreensParameterService.class).setClientType(employeeType);

		AbstractApplicationScreen.setScene("/EmployeeMenuScreen/EmployeeMenuScreen.fxml");

	}

	@FXML
	void forgetPassPressed(MouseEvent __) {
		try {
			IForgotPasswordHandler forgot = InjectionFactory.getInstance(Worker.class);
			ForgetPasswordWizard.start(forgot);
		} catch (Exception e) {
			log.fatal(e);
			log.debug(StackTraceUtil.stackTraceToStr(e));
		}
	}

	private void enableLoginButtonCheck() {
		loginButton.setDisable(userNameTextField.getText().isEmpty() || passwordField.getText().isEmpty());
	}
	

    @FXML
    void loginAsAdmin(ActionEvent event) {
		IManager employee = InjectionFactory.getInstance(Manager.class);
		CLIENT_TYPE employeeType = null;
		try {
			employeeType = employee.login("admin", "admin", true);
		} catch (SMException e) {
			log.fatal(e);
			log.debug(StackTraceUtil.stackTraceToStr(e));
			e.showInfoToUser();
			return;
		}
		InjectionFactory.getInstance(EmployeeScreensParameterService.class).setClientType(employeeType);

		AbstractApplicationScreen.setScene("/EmployeeMenuScreen/EmployeeMenuScreen.fxml");
    }
	
  

}
