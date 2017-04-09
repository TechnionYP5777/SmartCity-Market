package CustomerGuiScreens;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import CustomerContracts.IRegisteredCustomer;
import CustomerGuiHelpers.TempCustomerPassingData;
import CustomerImplementations.RegisteredCustomer;

import GuiUtils.AbstractApplicationScreen;
import UtilsImplementations.InjectionFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;


/**
 * CustomerRegistration_IngredientsScreen - Controller for CustomerRegistration_IngredientsScreen
 * 	the second (of three) registration screens.
 * 
 * @author Lior Ben Ami
 * @since 2017-04-09
 */
public class CustomerRegistration_PersonalInfoScreen implements Initializable {
	
	@FXML
	private GridPane personalInfoScreenPane; 
	
    @FXML
    private JFXTextField userNameTextField;

    @FXML
    private JFXTextField phoneNumberTextField;

    @FXML
    private JFXButton nextButton;

    @FXML
    private JFXTextField lastNameTextField;

    @FXML
    private JFXPasswordField repeatPassField;

    @FXML
    private JFXTextField emailTextField;

    @FXML
    private JFXTextField cityTextField;

    @FXML
    private JFXTextField firstNameTextField;

    @FXML
    private JFXPasswordField passwordField;

    @FXML
    private JFXTextField streetTextField;
    
    IRegisteredCustomer registeredCustomer ;

   
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		AbstractApplicationScreen.fadeTransition(personalInfoScreenPane);
	}

	@FXML
    void nextButtonPressed(ActionEvent event) {
	  	if (checkFieldsVadility())
			AbstractApplicationScreen.setScene("/CustomerRegistrationScreens/CustomerRegistration_IngredientsScreen.fxml");
  	}

	private boolean checkFieldsVadility() {
		// TODO Auto-generated method stub
		return true;
	}
}
