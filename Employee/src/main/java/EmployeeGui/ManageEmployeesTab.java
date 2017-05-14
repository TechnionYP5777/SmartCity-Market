package EmployeeGui;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.stream.IntStream;

import org.apache.log4j.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * ManageEmployeesTab - manages the employee tab
 * 
 * @author Shimon Azulay
 * @since 2017-01-04
 */
public class ManageEmployeesTab implements Initializable {

	@FXML
	private JFXListView<String> employeesList;

	@FXML
	private JFXTextField searchEmployee;

	@FXML
	private StackPane stackPane;

	@FXML
	private VBox waitingPane;

	@FXML
	private VBox detailsPane;

	@FXML
	private Label emplyeeTitleLbl;

	@FXML
	private Label emplyeeNameLbl;

	@FXML
	private Label employeeUser;

	@FXML
	private JFXButton removeBtn;

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

	static Logger log = Logger.getLogger(ManageEmployeesTab.class.getName());
	
//	private HashMap<String,>
	
	

	@FXML
	void finishBtnPressed(ActionEvent event) {
		try {
			manager.registerNewWorker(new Login(userTxt.getText(), passTxt.getText()));
		} catch (InvalidParameter | CriticalError | EmployeeNotConnected | ConnectionFailure | WorkerAlreadyExists e) {
			log.fatal(e.getStackTrace() + "\n" + e.getStackTrace() + "\n" + e.getMessage());
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

		radioBtnCont.addRadioButtons((Arrays.asList((new RadioButton[] { workerRadioBtn, managerRadioBtn }))));

		securityCombo.getItems().addAll("Q1", "Q2", "Q3", "Q4");

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

		createEmployeesList();
		
		employeesList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

		    @Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		    	employeeSelected(newValue);
		    }
		});

		enableFinishBtn();

	}
	
	private void employeeSelected(String employee) {
		
	}

	private void createEmployeesList() {
		
		ObservableList<String> data = FXCollections.observableArrayList();
		
	    IntStream.range(0, 1000).mapToObj(Integer::toString).forEach(data::add);
	    
	    try {
			manager.getAllWorkers();
		} catch (CriticalError | EmployeeNotConnected | ConnectionFailure e) {
			log.fatal(e.getStackTrace() + "\n" + e.getMessage());
		}

	    FilteredList<String> filteredData = new FilteredList<>(data, s -> true);

	    searchEmployee.textProperty().addListener(obs->{
	        String filter = searchEmployee.getText(); 
	        if(filter == null || filter.length() == 0) {
	            filteredData.setPredicate(s -> true);
	        }
	        else {
	            filteredData.setPredicate(s -> s.contains(filter));
	        }
	    });

	    employeesList.setItems(filteredData);
	    
	}

	private void enableFinishBtn() {
		finishBtn.setDisable(nameTxt.getText().isEmpty() || userTxt.getText().isEmpty() || passTxt.getText().isEmpty()
				|| securityAnswerTxt.getText().isEmpty());
	}

	@FXML
	private void radioButtonHandling(ActionEvent ¢) {
		radioBtnCont.selectRadioButton((RadioButton) ¢.getSource());
		enableFinishBtn();
	}

	@FXML
	void removeBtnPressed(ActionEvent event) {

	}

}
