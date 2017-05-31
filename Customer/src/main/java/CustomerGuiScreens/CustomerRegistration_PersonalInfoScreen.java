package CustomerGuiScreens;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
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
import GuiUtils.SecurityQuestions;
import SMExceptions.SMException;
import UtilsImplementations.InjectionFactory;
import UtilsImplementations.StackTraceUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.layout.GridPane;

/**
 * CustomerRegistration_PersonalInfoScreen - Controller for the first screen of
 * the customer registration screens. This screen handles the username, pass and
 * personal info.
 * 
 * @author Lior Ben Ami
 * @since 2017-04
 */
public class CustomerRegistration_PersonalInfoScreen implements Initializable {

	protected static Logger log = Logger.getLogger(CustomerRegistration_PersonalInfoScreen.class.getName());

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
	private JFXDatePicker birthDatePicker;

	@FXML
	private JFXComboBox<String> securityQuestionComboBox;

	@FXML
	private JFXTextField securityAnswerTextField;

	ICustomerProfile customerProfile;

	ICustomer customer;
	
	private void enableNextButton() {
		nextButton.setDisable(userNameTextField.getText().isEmpty() || passwordField.getText().isEmpty()
				|| repeatPassField.getText().isEmpty() || firstNameTextField.getText().isEmpty()
				|| lastNameTextField.getText().isEmpty() || phoneNumberTextField.getText().isEmpty()
				|| emailTextField.getText().isEmpty() || cityTextField.getText().isEmpty()
				|| streetTextField.getText().isEmpty() || securityAnswerTextField.getText().isEmpty() || 
				securityQuestionComboBox.getSelectionModel().getSelectedItem().toString().isEmpty());
	}


	@Override
	public void initialize(URL location, ResourceBundle __) {

		AbstractApplicationScreen.fadeTransition(personalInfoScreenPane);

		updateFields();

		customer = InjectionFactory.getInstance(Customer.class);

		userNameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			if (validNewUserName(newValue))
				TempCustomerProfilePassingData.customerProfile.setUserName(newValue);
			else
				DialogMessagesService.showErrorDialog(GuiCommonDefs.registrationFieldFailureTitle, null,
						GuiCommonDefs.registrationUsedUserName);

		});

		passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
			TempCustomerProfilePassingData.password = newValue;
			enableNextButton();

		});

		firstNameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			TempCustomerProfilePassingData.customerProfile.setFirstName(newValue);
			enableNextButton();
		});

		lastNameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			TempCustomerProfilePassingData.customerProfile.setLastName(newValue);
			enableNextButton();
		});

		phoneNumberTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			TempCustomerProfilePassingData.customerProfile.setPhoneNumber(newValue);
			enableNextButton();
		});

		emailTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			TempCustomerProfilePassingData.customerProfile.setEmailAddress(newValue);
			enableNextButton();
		});

		cityTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			TempCustomerProfilePassingData.customerProfile.setCity(newValue);
			enableNextButton();
		});

		streetTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			TempCustomerProfilePassingData.customerProfile.setStreet(newValue);
			enableNextButton();
		});

		birthDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
			TempCustomerProfilePassingData.customerProfile.setBirthdate(newValue);
			enableNextButton();
		});

		securityQuestionComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
			TempCustomerProfilePassingData.sequrityQuestion = newValue;
			enableNextButton();
		});

		securityQuestionComboBox.setButtonCell(new ListCell<String>() {

			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setStyle(null);
					setText(null);
				} else {
					setText(item.toString());
				}
			}
		});

		securityAnswerTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			TempCustomerProfilePassingData.sequrityAnswer = newValue;
			enableNextButton();
		});
		
		
		birthDatePicker.setValue(LocalDate.now());
		
		enableNextButton();
		
	}

	private boolean validNewUserName(String username) {
		Boolean res = false;
		try {
			res = customer.isFreeUsername(username);
		} catch (SMException e) {
			log.fatal(e);
			log.debug(StackTraceUtil.getStackTrace(e));
			e.showInfoToUser();
		}
		return res;
	}

	private void updateFields() {
		getSecurityQuestions();
		if (TempCustomerProfilePassingData.customerProfile == null) {
			TempCustomerProfilePassingData.customerProfile = InjectionFactory.getInstance(CustomerProfile.class);
			return;
		}
		// else

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

		if (TempCustomerProfilePassingData.sequrityQuestion != null)
			;
		securityQuestionComboBox.setValue(TempCustomerProfilePassingData.sequrityQuestion);

		if (TempCustomerProfilePassingData.sequrityAnswer != null)
			securityAnswerTextField.setText(TempCustomerProfilePassingData.sequrityAnswer);
	}

	private void getSecurityQuestions() {
		securityQuestionComboBox.getItems().addAll(SecurityQuestions.getQuestions());
	}

	@FXML
	void nextButtonPressed(ActionEvent __) {
		if (checkFieldsVadility())
			AbstractApplicationScreen
					.setScene("/CustomerRegistrationScreens/CustomerRegistration_IngredientsScreen.fxml");
	}

	@FXML
	void cancelButtonPressed(ActionEvent __) {
		TempCustomerProfilePassingData.clear();
		AbstractApplicationScreen.setScene("/CustomerLoginScreen/CustomerLoginScreen.fxml");
	}

	private boolean checkFieldsVadility() {
		if (passwordField.getText().equals(repeatPassField.getText()))
			return true;
		DialogMessagesService.showErrorDialog(GuiCommonDefs.registrationFieldFailureTitle, null,
				GuiCommonDefs.registrationWrongRepeatedPass);
		return false;
	}

}
