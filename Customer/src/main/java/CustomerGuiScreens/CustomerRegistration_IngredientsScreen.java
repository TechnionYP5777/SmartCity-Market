package CustomerGuiScreens;

import com.jfoenix.controls.JFXButton;

import BasicCommonClasses.CustomerProfile;
import CustomerGuiHelpers.TempCustomerProfilePassingData;
import GuiUtils.AbstractApplicationScreen;
import UtilsImplementations.InjectionFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

import org.controlsfx.control.CheckListView;

public class CustomerRegistration_IngredientsScreen implements Initializable {

	@FXML
	private GridPane ingredientsScreenPane;
	
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
    	AbstractApplicationScreen.setScene("/CustomerRegistrationScreens/CustomerRegistration_PersonalInfoScreen.fxml");
    }

    @Override
	public void initialize(URL location, ResourceBundle resources) {
		AbstractApplicationScreen.fadeTransition(ingredientsScreenPane);

	}
}

