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
import com.jfoenix.validation.RequiredFieldValidator;

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
import de.jensd.fx.glyphs.GlyphsBuilder;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
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
 * @author Shimon Azulay
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
	private JFXTextField firstNameTextField;

	@FXML
	JFXTextField lastNameTextField;

	private JFXTextField phoneNumberTextField = new JFXTextField("1111");

	@FXML
	private JFXButton nextButton;

	private JFXTextField emailTextField = new JFXTextField("someEmail@gmail.com");

	private JFXTextField cityTextField = new JFXTextField("someCity");

	private JFXTextField streetTextField = new JFXTextField("someStreet");

	private JFXDatePicker birthDatePicker = new JFXDatePicker(LocalDate.now());

	@FXML
	private JFXComboBox<String> securityQuestionComboBox;

	@FXML
	private JFXTextField securityAnswerTextField;

	ICustomerProfile customerProfile;

	ICustomer customer;

	RequiredFieldValidator userValidator1;

	RequiredFieldValidator userValidator2;

	private void enableNextButton() {
		nextButton.setDisable(userNameTextField.getText().isEmpty() || passwordField.getText().isEmpty()
				|| firstNameTextField.getText().isEmpty() || lastNameTextField.getText().isEmpty()
				|| phoneNumberTextField.getText().isEmpty() || emailTextField.getText().isEmpty()
				|| cityTextField.getText().isEmpty() || streetTextField.getText().isEmpty()
				|| securityAnswerTextField.getText().isEmpty()
				|| (securityQuestionComboBox.getSelectionModel().getSelectedItem() + "").isEmpty()
				|| !validNewUserName(userNameTextField.getText()));
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

		userValidator1 = new RequiredFieldValidator();
		userValidator1.setMessage("Input Required");
		userValidator1.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class).glyph(FontAwesomeIcon.WARNING)
				.size("1em").styleClass("error").build());

		userValidator2 = new RequiredFieldValidator();
		userValidator2.setMessage("Username Already Exists");
		userValidator2.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class).glyph(FontAwesomeIcon.WARNING)
				.size("1em").styleClass("error").build());

		userNameTextField.focusedProperty().addListener((o, oldVal, newVal) -> {
			if (!newVal) {
				userNameTextField.getValidators().add(userValidator1);
				userNameTextField.validate();
			} else if (!validNewUserName(userNameTextField.getText())) {
				userNameTextField.getValidators().add(userValidator2);
				userNameTextField.validate();
			}

		});

		passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
			TempCustomerProfilePassingData.password = newValue;
			enableNextButton();
		});

		RequiredFieldValidator userValidator3 = new RequiredFieldValidator();
		userValidator3.setMessage("Input Required");
		userValidator3.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class).glyph(FontAwesomeIcon.WARNING)
				.size("1em").styleClass("error").build());
		passwordField.getValidators().add(userValidator3);

		passwordField.focusedProperty().addListener((o, oldVal, newVal) -> {
			if (!newVal)
				passwordField.validate();
		});

		firstNameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			TempCustomerProfilePassingData.customerProfile.setFirstName(newValue);
			enableNextButton();
		});

		RequiredFieldValidator val4 = new RequiredFieldValidator();
		val4.setMessage("Input Required");
		val4.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class).glyph(FontAwesomeIcon.WARNING).size("1em")
				.styleClass("error").build());
		firstNameTextField.getValidators().add(val4);

		firstNameTextField.focusedProperty().addListener((o, oldVal, newVal) -> {
			if (!newVal)
				firstNameTextField.validate();
		});

		lastNameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			TempCustomerProfilePassingData.customerProfile.setLastName(newValue);
			enableNextButton();
		});

		RequiredFieldValidator val5 = new RequiredFieldValidator();
		val5.setMessage("Input Required");
		val5.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class).glyph(FontAwesomeIcon.WARNING).size("1em")
				.styleClass("error").build());
		lastNameTextField.getValidators().add(val5);

		lastNameTextField.focusedProperty().addListener((o, oldVal, newVal) -> {
			if (!newVal)
				lastNameTextField.validate();
		});

		phoneNumberTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.matches("\\d*")) {
				phoneNumberTextField.setText(newValue.replaceAll("[^\\d]", ""));
				return;
			}
			TempCustomerProfilePassingData.customerProfile.setPhoneNumber(newValue);
			enableNextButton();
		});

		RequiredFieldValidator val6 = new RequiredFieldValidator();
		val6.setMessage("Input Required");
		val6.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class).glyph(FontAwesomeIcon.WARNING).size("1em")
				.styleClass("error").build());
		phoneNumberTextField.getValidators().add(val6);

		phoneNumberTextField.focusedProperty().addListener((o, oldVal, newVal) -> {
			if (!newVal)
				phoneNumberTextField.validate();
		});

		emailTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			TempCustomerProfilePassingData.customerProfile.setEmailAddress(newValue);
			enableNextButton();
		});

		RequiredFieldValidator val9 = new RequiredFieldValidator();
		val9.setMessage("Input Required");
		val9.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class).glyph(FontAwesomeIcon.WARNING).size("1em")
				.styleClass("error").build());
		emailTextField.getValidators().add(val9);

		emailTextField.focusedProperty().addListener((o, oldVal, newVal) -> {
			if (!newVal)
				emailTextField.validate();
		});

		cityTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			TempCustomerProfilePassingData.customerProfile.setCity(newValue);
			enableNextButton();
		});

		RequiredFieldValidator val10 = new RequiredFieldValidator();
		val10.setMessage("Input Required");
		val10.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class).glyph(FontAwesomeIcon.WARNING).size("1em")
				.styleClass("error").build());
		cityTextField.getValidators().add(val10);

		cityTextField.focusedProperty().addListener((o, oldVal, newVal) -> {
			if (!newVal)
				cityTextField.validate();
		});

		streetTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			TempCustomerProfilePassingData.customerProfile.setStreet(newValue);
			enableNextButton();
		});

		RequiredFieldValidator val7 = new RequiredFieldValidator();
		val7.setMessage("Input Required");
		val7.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class).glyph(FontAwesomeIcon.WARNING).size("1em")
				.styleClass("error").build());
		streetTextField.getValidators().add(val7);

		streetTextField.focusedProperty().addListener((o, oldVal, newVal) -> {
			if (!newVal)
				streetTextField.validate();
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
				if (!empty && item != null)
					setText(item);
				else {
					setStyle(null);
					setText(null);
				}
			}
		});

		securityAnswerTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			TempCustomerProfilePassingData.sequrityAnswer = newValue;
			enableNextButton();
		});

		RequiredFieldValidator val8 = new RequiredFieldValidator();
		val8.setMessage("Input Required");
		val8.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class).glyph(FontAwesomeIcon.WARNING).size("1em")
				.styleClass("error").build());
		securityAnswerTextField.getValidators().add(val8);

		securityAnswerTextField.focusedProperty().addListener((o, oldVal, newVal) -> {
			if (!newVal)
				securityAnswerTextField.validate();
		});
		
		insertFields();

		enableNextButton();

	}

	void insertFields() {
		TempCustomerProfilePassingData.customerProfile.setStreet(streetTextField.getText());
		TempCustomerProfilePassingData.customerProfile.setBirthdate(birthDatePicker.getValue());
		TempCustomerProfilePassingData.customerProfile.setEmailAddress(emailTextField.getText());
		TempCustomerProfilePassingData.customerProfile.setPhoneNumber(phoneNumberTextField.getText());
		TempCustomerProfilePassingData.customerProfile.setCity(cityTextField.getText());
	}

	boolean validNewUserName(String username) {
		Boolean res = false;
		try {
			res = customer.isFreeUsername(username);
		} catch (SMException e) {
			log.fatal(e);
			log.debug(StackTraceUtil.stackTraceToStr(e));
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
		if (TempCustomerProfilePassingData.password != null)
			passwordField.setText(TempCustomerProfilePassingData.password);

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

		securityQuestionComboBox.setValue(TempCustomerProfilePassingData.sequrityQuestion);

		if (TempCustomerProfilePassingData.sequrityAnswer != null)
			securityAnswerTextField.setText(TempCustomerProfilePassingData.sequrityAnswer);
	}

	private void getSecurityQuestions() {
		securityQuestionComboBox.getItems().addAll(SecurityQuestions.getQuestions());
	}

	@FXML
	void nextButtonPressed(ActionEvent __) {
		AbstractApplicationScreen.setScene("/CustomerRegistrationScreens/CustomerRegistration_IngredientsScreen.fxml");
	}

	@FXML
	void cancelButtonPressed(ActionEvent __) {
		TempCustomerProfilePassingData.clear();
		AbstractApplicationScreen.setScene("/CustomerLoginScreen/CustomerLoginScreen.fxml");
	}

}
