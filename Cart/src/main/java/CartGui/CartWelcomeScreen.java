package CartGui;

import java.net.URL;
import java.util.ResourceBundle;

import CartContracts.ICart;
import CartContracts.ACartExceptions.AuthenticationError;
import CartContracts.ACartExceptions.CriticalError;
import CartDI.CartDiConfigurator;
import CartImplemantations.Cart;
import GuiUtils.AbstractApplicationScreen;
import UtilsImplementations.InjectionFactory;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * CartWelcomeScreen - This class is the controller for the cart welcome screen
 * all action of this scene should be here.
 * 
 * @author Lior Ben Ami
 * @since 2017-01-16 */
public class CartWelcomeScreen implements Initializable {

	@FXML
	private StackPane cartWelcomeScreenPane;
	@FXML
	private VBox vbox;

	@Override
	public void initialize(URL location, ResourceBundle __) {
		AbstractApplicationScreen.fadeTransition(cartWelcomeScreenPane);

//        MediaPlayer player = new MediaPlayer( new Media(getClass().getResource("/EmployeeMainScreen/store.mp4").toExternalForm()));
//        MediaView mediaView = new MediaView(player);
//        mediaView.setFitWidth(1600); mediaView.setFitHeight(900);
//        mediaView.setPreserveRatio(false);
//        mainScreenPane.getChildren().add(mediaView);
//        player.setMute(true);;
//        player.setCycleCount(MediaPlayer.INDEFINITE);   
//        player.play();
        
        vbox.toFront();
	}

	@FXML
	public void mouseClicked(MouseEvent __) {
		ICart cart = InjectionFactory.getInstance(Cart.class);
		try {
			cart.login("Cart", "Cart");
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
			Alert alert = new Alert(AlertType.ERROR , e.toString());
			alert.showAndWait();
			return;
		}
		TempCartPassingData.cart = cart;
		AbstractApplicationScreen.setScene("/CartMainScreen/CartMainScreen.fxml");
	}
}
