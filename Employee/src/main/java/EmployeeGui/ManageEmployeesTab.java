package EmployeeGui;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;

import BasicCommonClasses.Login;
import EmployeeContracts.IManager;
import EmployeeDefs.AEmployeeException.ConnectionFailure;
import EmployeeDefs.AEmployeeException.CriticalError;
import EmployeeDefs.AEmployeeException.EmployeeNotConnected;
import EmployeeDefs.AEmployeeException.InvalidParameter;
import EmployeeDefs.AEmployeeException.WorkerAlreadyExists;
import EmployeeImplementations.Manager;
import GuiUtils.RadioButtonEnabler;
import UtilsImplementations.InjectionFactory;
import de.jensd.fx.glyphs.GlyphsBuilder;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;

/**
 * ManageEmployeesTab - manages the employee tab
 * 
 * @author aviad
 * @since 2017-01-04
 */
public class ManageEmployeesTab implements Initializable {


    @FXML
    private JFXTextField nameTxt;

    @FXML
    private JFXTextField userTxt;

    @FXML
    private JFXPasswordField passTxt;

    @FXML
    private JFXComboBox<String> securityCombo;

    @FXML
    private JFXTextField securityAnswerTxt;

    @FXML
    private JFXRadioButton workerRadioBtn;

    @FXML
    private JFXRadioButton managerRadioBtn;

    @FXML
    private JFXButton finishBtn;
    
    RadioButtonEnabler radioBtnCont = new RadioButtonEnabler();
    
    IManager manager = InjectionFactory.getInstance(Manager.class);
    
	static Logger log = Logger.getLogger(ManageCatalogProductTab.class.getName());

    @FXML
    void finishBtnPressed(ActionEvent event) {
    	try {
			manager.registerNewWorker(new Login(userTxt.getText(), passTxt.getText()));
		} catch (InvalidParameter | CriticalError | EmployeeNotConnected | ConnectionFailure | WorkerAlreadyExists e) {
			log.fatal(e.getMessage());
		}
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		nameTxt.textProperty().addListener((observable, oldValue, newValue) -> {
			enableFinishBtn();
		});
		
		userTxt.textProperty().addListener((observable, oldValue, newValue) -> {
			enableFinishBtn();
		});
		
		passTxt.textProperty().addListener((observable, oldValue, newValue) -> {
			enableFinishBtn();
		});
		
		securityAnswerTxt.textProperty().addListener((observable, oldValue, newValue) -> {
			enableFinishBtn();
		});
		
		radioBtnCont.addRadioButtons(
				(Arrays.asList((new RadioButton[] { workerRadioBtn, managerRadioBtn }))));
		
		securityCombo.getItems().addAll(
				"Q1", "Q2", "Q3", "Q4");
		
		
		RequiredFieldValidator validator = new RequiredFieldValidator();
		validator.setMessage("Input Required");
		validator.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class).glyph(FontAwesomeIcon.WARNING).size("1em")
				.styleClass("error").build());
		
		nameTxt.getValidators().add(validator);
		nameTxt.focusedProperty().addListener((o, oldVal, newVal) -> {
			if (!newVal) {
				nameTxt.validate();
			}
		});
		
		RequiredFieldValidator validator2 = new RequiredFieldValidator();
		validator2.setMessage("Input Required");
		validator2.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class).glyph(FontAwesomeIcon.WARNING).size("1em")
				.styleClass("error").build());
		
		userTxt.getValidators().add(validator2);
		userTxt.focusedProperty().addListener((o, oldVal, newVal) -> {
			if (!newVal) {
				userTxt.validate();
			}
		});
		
		RequiredFieldValidator validator3 = new RequiredFieldValidator();
		validator3.setMessage("Input Required");
		validator3.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class).glyph(FontAwesomeIcon.WARNING).size("1em")
				.styleClass("error").build());
		
		passTxt.getValidators().add(validator3);
		passTxt.focusedProperty().addListener((o, oldVal, newVal) -> {
			if (!newVal) {
				passTxt.validate();
			}
		});
		
		
		RequiredFieldValidator validator4 = new RequiredFieldValidator();
		validator4.setMessage("Input Required");
		validator4.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class).glyph(FontAwesomeIcon.WARNING).size("1em")
				.styleClass("error").build());
		
		securityAnswerTxt.getValidators().add(validator4);
		securityAnswerTxt.focusedProperty().addListener((o, oldVal, newVal) -> {
			if (!newVal) {
				securityAnswerTxt.validate();
			}
		});
	
		
		enableFinishBtn();
		
	}

	private void enableFinishBtn() {
		finishBtn.setDisable(nameTxt.getText().isEmpty() || userTxt.getText().isEmpty() || passTxt.getText().isEmpty() ||
				securityAnswerTxt.getText().isEmpty());
	}
	
	@FXML
	private void radioButtonHandling(ActionEvent ¢) {
		radioBtnCont.selectRadioButton((RadioButton) ¢.getSource());
		enableFinishBtn();
	}

}

