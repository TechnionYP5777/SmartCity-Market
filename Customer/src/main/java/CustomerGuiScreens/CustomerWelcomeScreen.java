package CustomerGuiScreens;

import java.net.URL;
import java.util.ResourceBundle;

import GuiUtils.AbstractApplicationScreen;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.InputEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/** 
 * CustomerWelcomeScreen - This class is the controller for the customer welcome screen
 * all action of this scene should be here.
 * 
 * @author Lior Ben Ami
 * @author Shimon Azulay
 * @since 2017-01-16 */
public class CustomerWelcomeScreen implements Initializable {

	@FXML
	private StackPane customerWelcomeScreenPane;
	
	@FXML
	private VBox vbox;


	@Override
	public void initialize(URL location, ResourceBundle __) {
		AbstractApplicationScreen.fadeTransition(customerWelcomeScreenPane);   
	}
	

	@FXML
	public void mouseOrKeyboardPressed(InputEvent __) {
		AbstractApplicationScreen.setScene("/CustomerLoginScreen/CustomerLoginScreen.fxml");
	}
}
