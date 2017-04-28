package CustomerGuiScreens;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import BasicCommonClasses.CustomerProfile;
import BasicCommonClasses.ICustomerProfile;
import CommonDefs.GuiCommonDefs;
import CustomerContracts.ICustomer;
import CustomerContracts.IRegisteredCustomer;
import CustomerGuiHelpers.TempCustomerPassingData;
import CustomerGuiHelpers.TempCustomerProfilePassingData;
import CustomerImplementations.Customer;
import GuiUtils.AbstractApplicationScreen;
import GuiUtils.DialogMessagesService;
import UtilsImplementations.InjectionFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
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
    private JFXPasswordField passwordField;
    
    @FXML
    private JFXPasswordField repeatPassField;
    
    @FXML
    private JFXTextField firstNameTextField;
    
    @FXML
    JFXTextField lastNameTextField;
    
    @FXML
    private JFXTextField phoneNumberTextField;

    @FXML
    private JFXButton nextButton;

    @FXML
    private JFXTextField emailTextField;

    @FXML
    private JFXTextField cityTextField;

    @FXML
    private JFXTextField streetTextField;
    
    @FXML
    private DatePicker birthDatePicker;
    
    ICustomerProfile customerProfile ;

    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		AbstractApplicationScreen.fadeTransition(personalInfoScreenPane);
		if (TempCustomerProfilePassingData.customerProfile == null)
			TempCustomerProfilePassingData.customerProfile = InjectionFactory.getInstance(CustomerProfile.class);
		else {
			updateFields();
		}

		userNameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			TempCustomerProfilePassingData.customerProfile.setUserName(newValue);
		});

		passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
			TempCustomerProfilePassingData.password = newValue;
		});
		
		repeatPassField.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!passwordField.getText().equals(newValue) && (newValue!= null && !newValue.equals(""))) {
				DialogMessagesService.showErrorDialog(GuiCommonDefs.registrationFieldFailureTitle, null,
						GuiCommonDefs.registrationWrongRepeatedPass);
				nextButton.setDisable(true);
			}

			else {
				nextButton.setDisable(false);
			}
		});
		
		firstNameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			TempCustomerProfilePassingData.customerProfile.setFirstName(newValue);
		});
		
		lastNameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			TempCustomerProfilePassingData.customerProfile.setLastName(newValue);
		});
		
		phoneNumberTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			TempCustomerProfilePassingData.customerProfile.setPhoneNumber(newValue);
		});
		
		emailTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			TempCustomerProfilePassingData.customerProfile.setEmailAddress(newValue);
		});		
		
		cityTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			TempCustomerProfilePassingData.customerProfile.setCity(newValue);
		});
		
		streetTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			TempCustomerProfilePassingData.customerProfile.setStreet(newValue);
		});
		
		birthDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
			TempCustomerProfilePassingData.customerProfile.setBirthdate(newValue);
		});
	}

	private void updateFields() {
		if (TempCustomerProfilePassingData.customerProfile.getUserName() != null)
			userNameTextField.setText(TempCustomerProfilePassingData.customerProfile.getUserName());
		if (TempCustomerProfilePassingData.password != null) {
			passwordField.setText(TempCustomerProfilePassingData.password);
			repeatPassField.setText(TempCustomerProfilePassingData.password);
		}
		if (TempCustomerProfilePassingData.customerProfile.getFirstName() != null)
			firstNameTextField.setText(TempCustomerProfilePassingData.customerProfile.getFirstName());
		
		if (TempCustomerProfilePassingData.customerProfile.getLastName() != null)
			lastNameTextField.setText(TempCustomerProfilePassingData.customerProfile.getLastName());
		
		if (TempCustomerProfilePassingData.customerProfile.getPhoneNumber() != null)
			phoneNumberTextField.setText(TempCustomerProfilePassingData.customerProfile.getPhoneNumber());
		
		if (TempCustomerProfilePassingData.customerProfile.getEmailAddress() != null)
			emailTextField.setText(TempCustomerProfilePassingData.customerProfile.getEmailAddress());

		if (TempCustomerProfilePassingData.customerProfile.getCity() != null)
			cityTextField.setText(TempCustomerProfilePassingData.customerProfile.getCity());
		
		if (TempCustomerProfilePassingData.customerProfile.getStreet() != null)
			streetTextField.setText(TempCustomerProfilePassingData.customerProfile.getStreet());
		
		if (TempCustomerProfilePassingData.customerProfile.getBirthdate() != null)
			birthDatePicker.valueProperty().setValue(TempCustomerProfilePassingData.customerProfile.getBirthdate());
	}
	
	@FXML
    void nextButtonPressed(ActionEvent event) {
	  	if (checkFieldsVadility())
			AbstractApplicationScreen.setScene("/CustomerRegistrationScreens/CustomerRegistration_IngredientsScreen.fxml");
  	}

	private boolean checkFieldsVadility() {
		if (!passwordField.getText().equals(repeatPassField.getText())) {
			DialogMessagesService.showErrorDialog(GuiCommonDefs.registrationFieldFailureTitle, null,
					GuiCommonDefs.registrationWrongRepeatedPass);
			nextButton.setDisable(true);
			return false;
		}
		return true;
	}
}
