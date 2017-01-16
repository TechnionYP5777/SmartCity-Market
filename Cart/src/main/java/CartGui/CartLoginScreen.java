package CartGui;

import java.net.URL;
import java.util.ResourceBundle;

import CartContracts.ICart;
import CartDI.CartDiConfigurator;
import CartImplemantations.Cart;
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
		ICart cart = InjectionFactory.getInstance(Cart.class, new CartDiConfigurator());
		try {
			cart.login(username, password);
		} catch (SMException e) {
			//todo: handler
		}
		TempCartPassingData.cart = cart;
		AbstractApplicationScreen.setScene("/CartMainScreen/CartMainScreen.fxml");

	}

	private void enableLoginButtonCheck() {
		loginButton.setDisable(username.isEmpty() || password.isEmpty());
	}
	
}
