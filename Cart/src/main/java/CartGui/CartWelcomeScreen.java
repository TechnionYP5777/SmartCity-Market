package CartGui;

import java.net.URL;
import java.util.ResourceBundle;

import CartContracts.ICart;
import CartImplemantations.Cart;
import GuiUtils.AbstractApplicationScreen;
import SMExceptions.SMException;
import UtilsImplementations.InjectionFactory;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

/** 
 * CartWelcomeScreen - This class is the controller for the cart welcome screen
 * all action of this scene should be here.
 * 
 * @author Lior Ben Ami
 * @since 2017-01-16 */
public class CartWelcomeScreen implements Initializable {

	@FXML
	private StackPane cartWelcomeScreenPane;

	@Override
	public void initialize(URL location, ResourceBundle __) {
		AbstractApplicationScreen.fadeTransition(cartWelcomeScreenPane);   
	}

	@FXML
	public void mouseClicked(MouseEvent __) {
		ICart cart = InjectionFactory.getInstance(Cart.class);
		try {
			cart.login("Cart", "Cart");
		} catch (SMException e) {
			CartGuiExceptionsHandler.handle(e);	
			return;
		}
		catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR , e.toString());
			alert.showAndWait();
			return;
		}
		TempCartPassingData.cart = cart;
		AbstractApplicationScreen.setScene("/CartMainScreen/CartMainScreen.fxml");
	}
}
