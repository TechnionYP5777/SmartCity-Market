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
import CustomerGuiHelpers.TempCustomerProfilePassingData;
import CustomerImplementations.Customer;
import GuiUtils.AbstractApplicationScreen;
import GuiUtils.DialogMessagesService;
import SMExceptions.SMException;
import UtilsImplementations.InjectionFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.GridPane;


/**
 * CustomerRegistration_PersonalInfoScreen - Controller for the first screen of the customer registration screens.
 * 											This screen handles the username, pass and personal info.
 * 
 * @author Lior Ben Ami
 * @since 2017-04
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
	public void initialize(URL location, ResourceBundle __) {
		
		AbstractApplicationScreen.fadeTransition(personalInfoScreenPane);
		if (TempCustomerProfilePassingData.customerProfile != null)
			updateFields();
		else
			TempCustomerProfilePassingData.customerProfile = InjectionFactory.getInstance(CustomerProfile.class);

		userNameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			if (validNewUserName(newValue))
				TempCustomerProfilePassingData.customerProfile.setUserName(newValue);
			else
				DialogMessagesService.showErrorDialog(GuiCommonDefs.registrationFieldFailureTitle, null,
						GuiCommonDefs.registrationUsedUserName);
				
		});

		passwordField.textProperty().addListener((observable, oldValue, newValue) -> TempCustomerProfilePassingData.password = newValue);
		
		repeatPassField.textProperty().addListener((observable, oldValue, newValue) -> {
			if (passwordField.getText().equals(newValue) || newValue == null || "".equals(newValue))
				nextButton.setDisable(false);
			else {
				DialogMessagesService.showErrorDialog(GuiCommonDefs.registrationFieldFailureTitle, null,
						GuiCommonDefs.registrationWrongRepeatedPass);
				nextButton.setDisable(true);
			}
		});
		
		firstNameTextField.textProperty().addListener((observable, oldValue, newValue) -> TempCustomerProfilePassingData.customerProfile.setFirstName(newValue));
		
		lastNameTextField.textProperty().addListener((observable, oldValue, newValue) -> TempCustomerProfilePassingData.customerProfile.setLastName(newValue));
		
		phoneNumberTextField.textProperty().addListener((observable, oldValue, newValue) -> TempCustomerProfilePassingData.customerProfile.setPhoneNumber(newValue));
		
		emailTextField.textProperty().addListener((observable, oldValue, newValue) -> TempCustomerProfilePassingData.customerProfile.setEmailAddress(newValue));		
		
		cityTextField.textProperty().addListener((observable, oldValue, newValue) -> TempCustomerProfilePassingData.customerProfile.setCity(newValue));
		
		streetTextField.textProperty().addListener((observable, oldValue, newValue) -> TempCustomerProfilePassingData.customerProfile.setStreet(newValue));
		
		birthDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> TempCustomerProfilePassingData.customerProfile.setBirthdate(newValue));
	}

	private boolean validNewUserName(String username) {
		ICustomer customer = InjectionFactory.getInstance(Customer.class);	
		Boolean res = false;
		try {
			res= customer.isFreeUsername(username);
		} catch (SMException e) {
			e.showInfoToUser();
		}
		return res;
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
    void nextButtonPressed(ActionEvent __) {
	  	if (checkFieldsVadility())
			AbstractApplicationScreen.setScene("/CustomerRegistrationScreens/CustomerRegistration_IngredientsScreen.fxml");
  	}

	private boolean checkFieldsVadility() {
		if (passwordField.getText().equals(repeatPassField.getText()))
			return true;
		DialogMessagesService.showErrorDialog(GuiCommonDefs.registrationFieldFailureTitle, null,
				GuiCommonDefs.registrationWrongRepeatedPass);
		nextButton.setDisable(true);
		return false;
	}
}
