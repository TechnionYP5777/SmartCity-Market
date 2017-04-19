package CustomerGuiScreens;

import java.net.URL;
import java.util.ResourceBundle;

import CustomerContracts.ICustomer;
import CustomerContracts.ACustomerExceptions.AuthenticationError;
import CustomerContracts.ACustomerExceptions.CriticalError;
import CustomerGuiHelpers.TempCustomerPassingData;
import CustomerImplementations.Customer;
import GuiUtils.AbstractApplicationScreen;
import UtilsImplementations.InjectionFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;


import com.jfoenix.controls.JFXTextField;

import BasicCommonClasses.Login;
import ClientServerApi.ClientServerDefs;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXButton;;


/**
 * CartLogiScreen - Controller for customer login screen
 * 
 * @author Lior Ben Ami
 * @since 2017-01-16
 */
public class CustomerLoginScreen implements Initializable {
	
	private String username = "";
	private String password = "";
	private static Login guestLogin = new Login(ClientServerDefs.anonymousCustomerUsername, ClientServerDefs.anonymousCustomerUsername);
	@FXML
	private GridPane loginScreenPane;
	@FXML
	private JFXButton loginButton;
	@FXML
	private Button backButton;
	@FXML
	private JFXTextField userNameTextField;
	@FXML
	private JFXPasswordField passwordField;
	
	@FXML
	private JFXButton registerButton;
	
	@FXML
	private JFXButton guestLoginButton;
	
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
	private void backButtonPressed(ActionEvent __) {
		AbstractApplicationScreen.setScene("/CustomerWelcomeScreen/CustomerWelcomeScreen.fxml");
	}

	@FXML
	private void loginButtonPressed(ActionEvent __) {
		ICustomer customer = InjectionFactory.getInstance(Customer.class);
		try {
			if (username.equals(guestLogin.getUserName()) ) {
				Alert alert = new Alert(AlertType.ERROR , String.format("The user: \"{0}\" can access only as a guest user.", username));
				alert.showAndWait();
				return;
			}
			customer.login(username, password);
		} catch (CriticalError e) {
			Alert alert = new Alert(AlertType.ERROR , "A problem had occured. Please Try again or let us know if it continues.");
			alert.showAndWait();
			return;
		} catch (AuthenticationError e) {
			Alert alert = new Alert(AlertType.ERROR , "Wrong user name or password.");
			alert.showAndWait();
			return;
		}
		catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR , e + "");
			alert.showAndWait();
			return;
		}
		TempCustomerPassingData.customer = customer;
		AbstractApplicationScreen.setScene("/CustomerMainScreen/CustomerMainScreen.fxml");
	}

	private void enableLoginButtonCheck() {
		loginButton.setDisable(username.isEmpty() || password.isEmpty());
	}
	
	@FXML
	private void guestLoginButtonPressed(ActionEvent __) {
		ICustomer customer = InjectionFactory.getInstance(Customer.class);
		try {
			customer.login(guestLogin.getUserName(), guestLogin.getUserName());
		} catch (CriticalError e) {
			Alert alert = new Alert(AlertType.ERROR , "A problem had occured. Please Try again or let us know if it continues.");
			alert.showAndWait();
			return;
		} catch (AuthenticationError e) {
			Alert alert = new Alert(AlertType.ERROR , "Wrong user name or password.");
			alert.showAndWait();
			return;
		}
		catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR , e + "");
			alert.showAndWait();
			return;
		}
		TempCustomerPassingData.customer = customer;
		AbstractApplicationScreen.setScene("/CustomerMainScreen/CustomerMainScreen.fxml");
	}
	
	@FXML
	private void registerButtonPressed(ActionEvent __) {
		
		AbstractApplicationScreen.setScene("/CustomerRegistrationScreens/CustomerRegistration_PersonalInfoScreen.fxml");
	}
	
}