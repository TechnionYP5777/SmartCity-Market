package CustomerGuiScreens;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;

import GuiUtils.AbstractApplicationScreen;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class CustomerRegistration_FinalStepScreen {

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

    @FXML
    void registerButtonPressed(ActionEvent event) {
    	//todo lior: register
    }

}
