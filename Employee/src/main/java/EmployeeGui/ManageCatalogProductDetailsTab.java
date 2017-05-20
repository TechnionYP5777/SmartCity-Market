package EmployeeGui;

import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXPopup.PopupHPosition;
import com.jfoenix.controls.JFXPopup.PopupVPosition;

import BasicCommonClasses.Ingredient;
import BasicCommonClasses.Manufacturer;
import EmployeeContracts.IManager;
import EmployeeDefs.AEmployeeException.ConnectionFailure;
import EmployeeDefs.AEmployeeException.CriticalError;
import EmployeeDefs.AEmployeeException.EmployeeNotConnected;
import EmployeeDefs.AEmployeeException.IngredientStillInUse;
import EmployeeDefs.AEmployeeException.InvalidParameter;
import EmployeeDefs.AEmployeeException.ManfacturerStillInUse;
import EmployeeDefs.AEmployeeException.ParamIDAlreadyExists;
import EmployeeDefs.AEmployeeException.ParamIDDoesNotExist;
import EmployeeImplementations.Manager;
import UtilsImplementations.InjectionFactory;
import UtilsImplementations.StackTraceUtil;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

/**
 * ManageCatalogProductDetailsTab - This class is the controller for the Manage
 * Products details all action of this tab should be here.
 * 
 * @since 04.13.17
 * @author Shimon Azulay
 */

public class ManageCatalogProductDetailsTab implements Initializable {

	static Logger log = Logger.getLogger(ManagePackagesTab.class.getName());

	IManager manager = InjectionFactory.getInstance(Manager.class);

	@FXML
	private VBox rootPane;

	@FXML
	private JFXListView<String> manufacturerList;

	@FXML
	private JFXListView<String> ingredientsList;
	
    @FXML
    private JFXButton renameManu;
    
    @FXML
    private JFXButton renameIngr;

	@FXML
	private JFXButton addManuBtn;

	@FXML
	private JFXButton removeManuBtn;

	@FXML
	private JFXButton addIngrBtn;

	@FXML
	private JFXButton removeIngrBtn;

	private HashSet<String> selectedManu = new HashSet<String>();

	private HashSet<String> selectedIngr = new HashSet<String>();

	private HashMap<String, Manufacturer> manufacturars;

	private HashMap<String, Ingredient> ingredients;

	@FXML
	private JFXTextField filterManu;

	@FXML
	private JFXTextField filterIngr;

	ObservableList<String> dataManu;

	FilteredList<String> filteredDataManu;

	ObservableList<String> dataIngr;

	FilteredList<String> filteredDataIngr;

	JFXTextField newManu;

	JFXTextField newIngr;

	JFXButton okNewManu;

	JFXButton okNewIngr;
	
	JFXTextField renameManuLbl;

	JFXTextField renameIngrLbl;
	
	JFXButton okRenameIngr;
	
	JFXButton okRenameManu;
	
  
    void renameIngrPressed() {
      	long id = ingredients.get(selectedIngr.iterator().next()).getId();
    	try {
			manager.editIngredient(new Ingredient(id, renameIngrLbl.getText()));
		} catch (InvalidParameter | CriticalError | EmployeeNotConnected | ConnectionFailure | ParamIDDoesNotExist e) {
			log.debug(StackTraceUtil.getStackTrace(e));
			log.fatal(e.getMessage());
			e.showInfoToUser();
		}
		selectedIngr.clear();
		createIngredientList();
    }


    void renameManuPressed() {
    	long id = manufacturars.get(selectedManu.iterator().next()).getId();
    	try {
			manager.editManufacturer(new Manufacturer(id, renameManuLbl.getText()));
		} catch (InvalidParameter | CriticalError | EmployeeNotConnected | ConnectionFailure | ParamIDDoesNotExist e) {
			log.debug(StackTraceUtil.getStackTrace(e));
			log.fatal(e.getMessage());
			e.showInfoToUser();
		}
    	selectedManu.clear();
		createManufacturerList();
    }

	void addIngPressed() {
		try {
			manager.addIngredient(new Ingredient(0, newIngr.getText()));
		} catch (InvalidParameter | CriticalError | EmployeeNotConnected | ConnectionFailure | ParamIDAlreadyExists e) {
			log.fatal(e.getMessage());
			e.showInfoToUser();
		}
		selectedIngr.clear();
		createIngredientList();
	}

	void addManuPressed() {
		try {
			manager.addManufacturer(new Manufacturer(0, newManu.getText()));
		} catch (InvalidParameter | CriticalError | EmployeeNotConnected | ConnectionFailure | ParamIDAlreadyExists e) {
			log.fatal(e.getMessage());
			e.showInfoToUser();
		}
		selectedManu.clear();
		createManufacturerList();
	}

	@FXML
	void removeIngrPressed(ActionEvent __) {
		selectedIngr.forEach(ing -> {
			try {
				manager.removeIngredient(ingredients.get(ing), false);
			} catch (InvalidParameter | CriticalError | EmployeeNotConnected | ConnectionFailure | ParamIDDoesNotExist
					| IngredientStillInUse e) {
				log.fatal(e.getMessage());
				e.showInfoToUser();
			}
		});
		selectedIngr.clear();
		createIngredientList();
	}

	@FXML
	void removeManuPress(ActionEvent __) {
		selectedManu.forEach(man -> {
			try {
				manager.removeManufacturer(manufacturars.get(man));
			} catch (InvalidParameter | CriticalError | EmployeeNotConnected | ConnectionFailure | ParamIDDoesNotExist
					| ManfacturerStillInUse e) {
				log.fatal(e.getMessage());
				e.showInfoToUser();
			}
		});
		selectedManu.clear();
		createManufacturerList();
	}

	@Override
	public void initialize(URL location, ResourceBundle __) {

		createManufacturerList();
		createIngredientList();

		filterManu.textProperty().addListener(obs -> {
			String filter = filterManu.getText();
			filteredDataManu.setPredicate(filter == null || filter.length() == 0 ? s -> true : s -> s.contains(filter));
		});

		manufacturerList.setCellFactory(CheckBoxListCell.forListView(new Callback<String, ObservableValue<Boolean>>() {
			@Override
			public ObservableValue<Boolean> call(String item) {
				BooleanProperty observable = new SimpleBooleanProperty();
				observable.set(selectedManu.contains(item));

				observable.addListener((obs, wasSelected, isNowSelected) -> {
					if (isNowSelected)
						selectedManu.add(item);
					else
						selectedManu.remove(item);
					enableButtons();

				});
				return observable;
			}
		}));

		filterIngr.textProperty().addListener(obs -> {
			String filter = filterIngr.getText();
			filteredDataIngr.setPredicate(filter == null || filter.length() == 0 ? s -> true : s -> s.contains(filter));
		});

		ingredientsList.setCellFactory(CheckBoxListCell.forListView(new Callback<String, ObservableValue<Boolean>>() {
			@Override
			public ObservableValue<Boolean> call(String item) {
				BooleanProperty observable = new SimpleBooleanProperty();
				observable.set(selectedIngr.contains(item));
				observable.addListener((obs, wasSelected, isNowSelected) -> {
					if (isNowSelected)
						selectedIngr.add(item);
					else
						selectedIngr.remove(item);
					enableButtons();

				});
				return observable;
			}
		}));

		Label lbl1 = new Label("Insert New Manufacturar");
		newManu = new JFXTextField();
		okNewManu = new JFXButton("Done!");
		okNewManu.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent __) {
				addManuPressed();
			}
		});

		VBox manuContainer = new VBox();
		manuContainer.getChildren().addAll(lbl1, newManu, okNewManu);
		manuContainer.setPadding(new Insets(10, 50, 50, 50));
		manuContainer.setSpacing(10);

		JFXPopup popup1 = new JFXPopup(manuContainer);
		addManuBtn.setOnMouseClicked(e -> popup1.show(addManuBtn, PopupVPosition.TOP, PopupHPosition.LEFT));

		newManu.textProperty().addListener((observable, oldValue, newValue) -> enableAddButtons());

		Label lbl2 = new Label("Insert New Ingredient");
		newIngr = new JFXTextField();
		okNewIngr = new JFXButton("Done!");
		okNewIngr.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent __) {
				addIngPressed();
			}
		});

		VBox ingrContainer = new VBox();
		ingrContainer.getChildren().addAll(lbl2, newIngr, okNewIngr);
		ingrContainer.setPadding(new Insets(10, 50, 50, 50));
		ingrContainer.setSpacing(10);

		JFXPopup popup2 = new JFXPopup(ingrContainer);
		addIngrBtn.setOnMouseClicked(e -> popup2.show(addIngrBtn, PopupVPosition.TOP, PopupHPosition.LEFT));

		newIngr.textProperty().addListener((observable, oldValue, newValue) -> enableAddButtons());
		
		
		
		Label lbl3 = new Label("Rename Selected Manufacturar");
		renameManuLbl = new JFXTextField();
		okRenameManu = new JFXButton("Done!");
		okRenameManu.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent __) {
				renameManuPressed();
			}
		});

		VBox renameManuContainer = new VBox();
		renameManuContainer.getChildren().addAll(lbl3, renameManuLbl, okRenameManu);
		renameManuContainer.setPadding(new Insets(10, 50, 50, 50));
		renameManuContainer.setSpacing(10);

		JFXPopup popup3 = new JFXPopup(renameManuContainer);
		renameManu.setOnMouseClicked(e -> popup3.show(renameManu, PopupVPosition.TOP, PopupHPosition.LEFT));

		renameManuLbl.textProperty().addListener((observable, oldValue, newValue) -> enableAddButtons());
		
		Label lbl4 = new Label("Rename Selected Ingredient");
		renameIngrLbl = new JFXTextField();
		okRenameIngr = new JFXButton("Done!");
		okRenameIngr.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent __) {
				renameIngrPressed();
			}
		});

		VBox renameIngrContainer = new VBox();
		renameIngrContainer.getChildren().addAll(lbl4, renameIngrLbl, okRenameIngr);
		renameIngrContainer.setPadding(new Insets(10, 50, 50, 50));
		renameIngrContainer.setSpacing(10);

		JFXPopup popup4 = new JFXPopup(renameIngrContainer);
		renameIngr.setOnMouseClicked(e -> popup4.show(renameIngr, PopupVPosition.TOP, PopupHPosition.LEFT));

		renameIngrLbl.textProperty().addListener((observable, oldValue, newValue) -> enableAddButtons());
		
		
		enableButtons();
		enableAddButtons();

	}

	private void enableAddButtons() {
		okNewManu.setDisable(newManu.getText().isEmpty());
		okNewIngr.setDisable(newIngr.getText().isEmpty());
		okRenameManu.setDisable(renameManuLbl.getText().isEmpty());
		okRenameIngr.setDisable(renameIngrLbl.getText().isEmpty());
	}

	private void createManufacturerList() {

		manufacturars = new HashMap<String, Manufacturer>();

		try {
			manager.getAllManufacturers().forEach(manufacturer -> manufacturars.put(manufacturer.getName(), manufacturer));
		} catch (InvalidParameter | CriticalError | EmployeeNotConnected | ConnectionFailure e) {
			log.fatal(e.getMessage());
			e.showInfoToUser();
		}

		dataManu = FXCollections.observableArrayList();

		dataManu.setAll(manufacturars.keySet());

		// IntStream.range(0,
		// 1000).mapToObj(Integer::toString).forEach(dataManu::add);

		filteredDataManu = new FilteredList<>(dataManu, s -> true);

		manufacturerList.setItems(filteredDataManu);

	}

	private void createIngredientList() {
		ingredients = new HashMap<String, Ingredient>();
		try {
			manager.getAllIngredients().forEach(ingredient -> ingredients.put(ingredient.getName(), ingredient));
		} catch (InvalidParameter | CriticalError | EmployeeNotConnected | ConnectionFailure e) {
			log.fatal(e.getMessage());
			e.showInfoToUser();
		}

		dataIngr = FXCollections.observableArrayList();

		dataIngr.setAll(ingredients.keySet());

		// IntStream.range(0,
		// 1000).mapToObj(Integer::toString).forEach(dataIngr::add);

		filteredDataIngr = new FilteredList<>(dataIngr, s -> true);

		ingredientsList.setItems(filteredDataIngr);
	}

	private void enableButtons() {
		addManuBtn.setDisable(!selectedManu.isEmpty());
		removeManuBtn.setDisable(selectedManu.isEmpty());
		addIngrBtn.setDisable(!selectedIngr.isEmpty());
		removeIngrBtn.setDisable(selectedIngr.isEmpty());
		renameManu.setDisable(selectedManu.isEmpty() || selectedManu.size() > 1 );
		renameIngr.setDisable(selectedIngr.isEmpty() || selectedIngr.size() > 1 );

	}
	


}
