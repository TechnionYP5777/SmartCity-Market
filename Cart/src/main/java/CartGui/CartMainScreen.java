package CartGui;

import java.net.URL;
import java.util.ResourceBundle;

import CartContracts.ICart;
import GuiUtils.AbstractApplicationScreen;
import SMExceptions.SMException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * CartMainScreen - Controller for main screen which holds the main operations available for cart durring shopping.
 * 
 * @author Lior Ben Ami
 * @since 2017-01-11
 */
public class CartMainScreen implements Initializable {

	// Main screen panes
	@FXML
	GridPane cartMainScreenPain;

	Stage primeStage = CartApplicationScreen.stage;
	ICart cart;
	
	@Override
	public void initialize(URL location, ResourceBundle __) {
		
		AbstractApplicationScreen.fadeTransition(cartMainScreenPain);
		//defining behavior when stage/window is closed.
		cart = TempCartPassingData.cart;
				
		//todo: add this:
		/*primeStage.setOnCloseRequest(event -> {
			try {
				cart.logout();
			} catch (SMException e){
				//todo: continue from here
			}
		});*/
	}
	
	@FXML
	public void removeProductButtonPressed(ActionEvent __) {
		
	}

	@FXML
	public void addProductButtonPressed(ActionEvent __) {
		
	}
	
}