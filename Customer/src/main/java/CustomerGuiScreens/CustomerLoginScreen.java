package CustomerGuiScreens;

import java.net.URL;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import CustomerContracts.ICustomer;
import CustomerContracts.IRegisteredCustomer;
import CustomerGuiHelpers.TempCustomerPassingData;
import CustomerGuiHelpers.TempRegisteredCustomerPassingData;
import CustomerImplementations.Customer;
import CustomerImplementations.RegisteredCustomer;
import GuiUtils.AbstractApplicationScreen;
import GuiUtils.DialogMessagesService;
import SMExceptions.SMException;
import UtilsContracts.IForgotPasswordHandler;
import UtilsImplementations.InjectionFactory;
import UtilsImplementations.StackTraceUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import GuiUtils.ForgetPasswordWizard;

import com.jfoenix.controls.JFXTextField;

import BasicCommonClasses.Login;
import ClientServerApi.ClientServerDefs;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXButton;

/**
 * CartLogiScreen - Controller for customer login screen
 * 
 * @author Lior Ben Ami
 * @author Shimon Azulay
 * @since 2017-01-16
 */
public class CustomerLoginScreen implements Initializable {

	protected static Logger log = Logger.getLogger(CustomerLoginScreen.class.getName());

	private String username = "";
	private String password = "";
	private static Login guestLogin = new Login(ClientServerDefs.anonymousCustomerUsername,
			ClientServerDefs.anonymousCustomerUsername);
	@FXML
	private GridPane loginScreenPane;

	@FXML
	private JFXButton loginButton;

	@FXML
	private JFXTextField userNameTextField;

	@FXML
	private JFXPasswordField passwordField;

	@FXML
	private JFXButton registerButton;

	@FXML
	private JFXButton guestLoginButton;

	@FXML
	private ImageView icon;

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
		TempCustomerPassingData.customer = null;
	}

	@FXML
	private void backButtonPressed(MouseEvent __) {
		AbstractApplicationScreen.setScene("/CustomerWelcomeScreen/CustomerWelcomeScreen.fxml");
	}

	@FXML
	private void loginButtonPressed(ActionEvent __) {
		IRegisteredCustomer regCustomer = InjectionFactory.getInstance(RegisteredCustomer.class);
		try {
			if (username.equals(guestLogin.getUserName())) {
				DialogMessagesService.showErrorDialog(
						String.format("The user: \"{0}\" can access only as a guest user.", username), null, "");
				return;
			}
			regCustomer.login(username, password, true);
		} catch (SMException e) {
			log.fatal(e);
			log.debug(StackTraceUtil.getStackTrace(e));
			e.showInfoToUser();
			return;
		} catch (Exception e) {
			DialogMessagesService.showErrorDialog(
					e + "", null, "");
			return;
		}
		//TempRegisteredCustomerPassingData.regCustomer = regCustomer;
		TempCustomerPassingData.customer = null;
		TempCustomerPassingData.customer = regCustomer;
		TempCustomerPassingData.isRegistered = true;
		AbstractApplicationScreen.setScene("/CustomerMainScreen/CustomerMainScreen.fxml");
	}

	private void enableLoginButtonCheck() {
		loginButton.setDisable(username.isEmpty() || password.isEmpty());
	}

	@FXML
	private void guestLoginButtonPressed(ActionEvent __) {
		ICustomer customer = InjectionFactory.getInstance(Customer.class);
		try {
			customer.login(guestLogin.getUserName(), guestLogin.getUserName(), true);
		} catch (SMException e) {
			log.fatal(e);
			log.debug(StackTraceUtil.getStackTrace(e));
			e.showInfoToUser();
			return;
		} catch (Exception e) {
			DialogMessagesService.showErrorDialog(
					e + "", null, "");
			return;
		}
		TempCustomerPassingData.customer = customer;

		//TempRegisteredCustomerPassingData.regCustomer = null;

		TempCustomerPassingData.isRegistered = false;
		AbstractApplicationScreen.setScene("/CustomerMainScreen/CustomerMainScreen.fxml");
	}

	@FXML
	private void registerButtonPressed(ActionEvent __) {

		AbstractApplicationScreen.setScene("/CustomerRegistrationScreens/CustomerRegistration_PersonalInfoScreen.fxml");
	}

	@FXML
	void forgotPassButtonPressed(MouseEvent __) {
		try {
			IForgotPasswordHandler forgot = InjectionFactory.getInstance(Customer.class);
			ForgetPasswordWizard.start(forgot);
		} catch (Exception e) {
			// TODO
		}
	}
}