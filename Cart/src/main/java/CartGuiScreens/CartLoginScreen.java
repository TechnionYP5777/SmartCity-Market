package CartGuiScreens;

import java.net.URL;
import java.util.ResourceBundle;

import CartContracts.ACartExceptions.AuthenticationError;
import CartContracts.ACartExceptions.CriticalError;
import CartGuiHelpers.TempCartPassingData;
import CartContracts.ICart;
import CartImplemantations.Cart;
import GuiUtils.AbstractApplicationScreen;
import UtilsImplementations.InjectionFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

/**
 * CartLogiScreen - Controller for cart login screen
 * 
 * @author Lior Ben Ami
 * @since 2017-01-16
 */
public class CartLoginScreen implements Initializable {
	
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
		TempCartPassingData.cart = null;
	}

	@FXML
	private void backButtonPressed(ActionEvent __) {
		AbstractApplicationScreen.setScene("/CartWelcomeScreen/CartWelcomeScreen.fxml");
	}

	@FXML
	private void loginButtonPressed(ActionEvent __) {
		ICart cart = InjectionFactory.getInstance(Cart.class);
		try {
			cart.login(username, password);
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
		TempCartPassingData.cart = cart;
		AbstractApplicationScreen.setScene("/CartMainScreen/CartMainScreen.fxml");
	}

	private void enableLoginButtonCheck() {
		loginButton.setDisable(username.isEmpty() || password.isEmpty());
	}
	
}
