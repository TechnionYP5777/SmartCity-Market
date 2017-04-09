package CustomerGuiScreens;

import com.jfoenix.controls.JFXButton;

import GuiUtils.AbstractApplicationScreen;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.controlsfx.control.CheckListView;

public class CustomerRegistration_IngredientsScreen {

    @FXML
    private CheckListView<?> ingredientsCheckListView;

    @FXML
    private JFXButton ingridients_nextButton;

    @FXML
    private JFXButton ingridients_backButton;

    @FXML
    void ingridients_nextButtonPressed(ActionEvent event) {
    	AbstractApplicationScreen.setScene("/CustomerRegistrationScreens/CustomerRegistration_FinalStepScreen.fxml");
    }

    @FXML
    void ingridients_backButtonPressed(ActionEvent event) {
    	AbstractApplicationScreen.setScene("/CustomerRegistrationScreens/CustomerRegistration_IngredientsScreen.fxml");
    }

}

