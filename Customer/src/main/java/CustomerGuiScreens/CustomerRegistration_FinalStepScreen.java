package CustomerGuiScreens;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;

import GuiUtils.AbstractApplicationScreen;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;

public class CustomerRegistration_FinalStepScreen implements Initializable {

	@FXML
	private GridPane finalStepScreenPane;
	
    @FXML
    private JFXButton final_backButton;

    @FXML
    private JFXCheckBox final_acceptChecKBox;

    @FXML
    private JFXButton final_registerButton;

    @FXML
    void backButtonPressed(ActionEvent event) {
		AbstractApplicationScreen.setScene("/CustomerRegistrationScreens/CustomerRegistration_IngredientsScreen.fxml");
    }

    @Override
   	public void initialize(URL location, ResourceBundle resources) {
   		AbstractApplicationScreen.fadeTransition(finalStepScreenPane);

   	}
   
    @FXML
    void registerButtonPressed(ActionEvent event) {
    	//todo lior: register
    }
}
