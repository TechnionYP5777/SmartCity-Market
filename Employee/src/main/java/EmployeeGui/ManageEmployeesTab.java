package EmployeeGui;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;

import BasicCommonClasses.ForgotPasswordData;
import BasicCommonClasses.Login;
import EmployeeContracts.IManager;
import EmployeeDefs.AEmployeeException.ConnectionFailure;
import SMExceptions.CommonExceptions.CriticalError;
import UtilsContracts.IConfiramtionDialog;
import EmployeeDefs.AEmployeeException.EmployeeNotConnected;
import EmployeeDefs.AEmployeeException.InvalidParameter;
import EmployeeDefs.AEmployeeException.WorkerAlreadyExists;
import EmployeeDefs.AEmployeeException.WorkerDoesNotExist;
import EmployeeImplementations.Manager;
import GuiUtils.DialogMessagesService;
import GuiUtils.RadioButtonEnabler;
import GuiUtils.SecurityQuestions;
import UtilsImplementations.InjectionFactory;
import UtilsImplementations.StackTraceUtil;
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
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

/**
 * ManageEmployeesTab - manages the employee tab
 * 
 * @author Shimon Azulay
 * @since 2017-01-04
 */
public class ManageEmployeesTab implements Initializable {
	
	@FXML
	private StackPane rootPane;

	@FXML
	private JFXListView<String> employeesList;

	@FXML
	private JFXTextField searchEmployee;

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

	@FXML
	private JFXButton removeEmployeesBtn;

	RadioButtonEnabler radioBtnCont = new RadioButtonEnabler();

	IManager manager = InjectionFactory.getInstance(Manager.class);

	static Logger log = Logger.getLogger(ManageEmployeesTab.class.getName());

	private HashSet<String> selectedEmployees = new HashSet<String>();

	private ObservableList<String> dataEmployees;

	private FilteredList<String> filteredDataEmployees;

	private Map<String, Boolean> employeesInSystem;

	private boolean helpRetVal;

	@FXML
	void finishBtnPressed(ActionEvent __) {
		String userName = userTxt.getText();
		try {
			manager.registerNewWorker(new Login(userName, passTxt.getText(), new ForgotPasswordData(
					securityCombo.getSelectionModel().getSelectedItem(), securityAnswerTxt.getText())));
			String logMessage = "registration Employee: " + userName + " succeeded.";
			printToSuccessLog(logMessage);
		} catch (InvalidParameter | CriticalError | EmployeeNotConnected | ConnectionFailure | WorkerAlreadyExists e) {
			log.fatal(e);
			log.debug(StackTraceUtil.stackTraceToStr(e));
			e.showInfoToUser();
		}
		createEmployeesList();
		enableRemoveButton();
	}

	@Override
	public void initialize(URL location, ResourceBundle __) {

		userTxt.textProperty().addListener((observable, oldValue, newValue) -> enableFinishBtn());

		passTxt.textProperty().addListener((observable, oldValue, newValue) -> enableFinishBtn());

		securityAnswerTxt.textProperty().addListener((observable, oldValue, newValue) -> enableFinishBtn());

		radioBtnCont.addRadioButtons(Arrays.asList(new RadioButton[] { workerRadioBtn, managerRadioBtn }));

		securityCombo.getItems().addAll(SecurityQuestions.getQuestions());

		RequiredFieldValidator validator2 = new RequiredFieldValidator();
		validator2.setMessage("Input Required");
		validator2.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class).glyph(FontAwesomeIcon.WARNING).size("1em")
				.styleClass("error").build());

		userTxt.getValidators().add(validator2);
		userTxt.focusedProperty().addListener((o, oldVal, newVal) -> {
			if (!newVal)
				userTxt.validate();
		});

		RequiredFieldValidator validator3 = new RequiredFieldValidator();
		validator3.setMessage("Input Required");
		validator3.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class).glyph(FontAwesomeIcon.WARNING).size("1em")
				.styleClass("error").build());

		passTxt.getValidators().add(validator3);
		passTxt.focusedProperty().addListener((o, oldVal, newVal) -> {
			if (!newVal)
				passTxt.validate();
		});

		RequiredFieldValidator validator4 = new RequiredFieldValidator();
		validator4.setMessage("Input Required");
		validator4.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class).glyph(FontAwesomeIcon.WARNING).size("1em")
				.styleClass("error").build());

		securityAnswerTxt.getValidators().add(validator4);
		securityAnswerTxt.focusedProperty().addListener((o, oldVal, newVal) -> {
			if (!newVal)
				securityAnswerTxt.validate();
		});


		searchEmployee.textProperty().addListener(obs -> {
			String filter = searchEmployee.getText();
			filteredDataEmployees
					.setPredicate(filter == null || filter.length() == 0 ? s -> true : s -> s.contains(filter));
		});

		// employeesList.setCellFactory(CheckBoxListCell.forListView(new
		// Callback<String, ObservableValue<Boolean>>() {
		// @Override
		// public ObservableValue<Boolean> call(String item) {
		// BooleanProperty observable = new SimpleBooleanProperty();
		// observable.set(selectedEmployees.contains(item));
		//
		// observable.addListener((obs, wasSelected, isNowSelected) -> {
		// if (isNowSelected)
		// selectedEmployees.add(item);
		// else
		// selectedEmployees.remove(item);
		// enableRemoveButton();
		//
		// });
		// return observable;
		// }
		// }));

		employeesList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		// for multiple selection
		employeesList.addEventFilter(MouseEvent.MOUSE_PRESSED, evt -> {
			Node node = evt.getPickResult().getIntersectedNode();

			// go up from the target node until a list cell is found or it's
			// clear
			// it was not a cell that was clicked
			while (node != null && node != employeesList && !(node instanceof ListCell)) {
				node = node.getParent();
			}

			// if is part of a cell or the cell,
			// handle event instead of using standard handling
			if (node instanceof ListCell) {
				// prevent further handling
				evt.consume();

				ListCell<?> cell = (ListCell<?>) node;
				ListView<?> lv = cell.getListView();

				// focus the listview
				lv.requestFocus();

				if (!cell.isEmpty()) {
					// handle selection for non-empty cells
					int index = cell.getIndex();
					if (cell.isSelected()) {
						lv.getSelectionModel().clearSelection(index);
					} else {
						lv.getSelectionModel().select(index);
					}
				}

				ObservableList<String> selectedItems = employeesList.getSelectionModel().getSelectedItems();

				selectedEmployees.clear();
				selectedEmployees.addAll(selectedItems);
				enableRemoveButton();

			}
		});

		employeesList.setDepth(1);
		employeesList.setExpanded(true);

		securityCombo.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> __, String oldValue, String newValue) {
				enableFinishBtn();
			}
		});
		
		createEmployeesList();

		enableFinishBtn();
		enableRemoveButton();
	}

	private void enableRemoveButton() {
		removeEmployeesBtn.setDisable(isConnectedManagerSelected() || selectedEmployees.isEmpty());

	}

	private boolean isConnectedManagerSelected() {
		helpRetVal = false;
		selectedEmployees.forEach(eml -> {
			if (employeesInSystem.get(eml))
				helpRetVal = true;
		});
		return helpRetVal;
	}

	private void createEmployeesList() {

		selectedEmployees = new HashSet<String>();

		dataEmployees = FXCollections.observableArrayList();

		try {
			employeesInSystem = manager.getAllWorkers();
		} catch (CriticalError | EmployeeNotConnected | ConnectionFailure e) {
			log.fatal(e);
			log.debug(StackTraceUtil.stackTraceToStr(e));
			e.showInfoToUser();
		}

		dataEmployees.setAll(employeesInSystem.keySet());

		filteredDataEmployees = new FilteredList<>(dataEmployees, s -> true);

		employeesList.setItems(filteredDataEmployees);
	}

	private void enableFinishBtn() {
		finishBtn.setDisable(userTxt.getText().isEmpty() || passTxt.getText().isEmpty()
				|| securityCombo.getSelectionModel().getSelectedItem() == null
				|| securityCombo.getSelectionModel().getSelectedItem().isEmpty()
				|| securityAnswerTxt.getText().isEmpty());
	}

	@FXML
	private void radioButtonHandling(ActionEvent ¢) {
		radioBtnCont.selectRadioButton((RadioButton) ¢.getSource());
		enableFinishBtn();
	}

	@FXML
	void removeBtnPressed(ActionEvent __) {
		DialogMessagesService.showConfirmationDialog("Remove Selected Employees", null, "Are You Sure?",
				new removeEmployeesHandler());
	}

	void removeEmployeesHandle() {
		StringBuilder emlNames = new StringBuilder(); 
		selectedEmployees.forEach(eml -> {
			try {
				manager.removeWorker(eml);
				if (emlNames.length() >0)
					emlNames.append(", ");
				emlNames.append(eml);
			} catch (InvalidParameter | CriticalError | EmployeeNotConnected | ConnectionFailure
					| WorkerDoesNotExist e) {
				log.fatal(e);
				log.debug(StackTraceUtil.stackTraceToStr(e));
				e.showInfoToUser();
			}
		});
		createEmployeesList();
		enableRemoveButton();
		if (emlNames.length() > 0) {
			String logMessage = "remove Employees: " + emlNames.toString() + " succeeded.";
			printToSuccessLog(logMessage);
		}
	}
	
	private void printToSuccessLog(String msg) {
		((TextArea) rootPane.getScene().lookup("#successLogArea"))
				.appendText(new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss").format(new Date()) + " :: " + msg + "\n");
	}

	class removeEmployeesHandler implements IConfiramtionDialog {

		@Override
		public void onYes() {
			removeEmployeesHandle();
		}

		@Override
		public void onNo() {
		}

	}

}
