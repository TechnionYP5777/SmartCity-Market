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

import BasicCommonClasses.Ingredient;
import BasicCommonClasses.Manufacturer;
import EmployeeContracts.IManager;
import EmployeeDefs.AEmployeeException.ConnectionFailure;
import EmployeeDefs.AEmployeeException.CriticalError;
import EmployeeDefs.AEmployeeException.EmployeeNotConnected;
import EmployeeDefs.AEmployeeException.InvalidParameter;
import EmployeeDefs.AEmployeeException.ParamIDDoesNotExist;
import EmployeeImplementations.Manager;
import UtilsImplementations.InjectionFactory;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
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
	void addIngPressed(ActionEvent event) {

	}

	@FXML
	void addManuPressed(ActionEvent event) {

	}

	@FXML
	void removeIngrPressed(ActionEvent event) {
		selectedIngr.forEach(ing -> {
			try {
				manager.removeIngredient(ingredients.get(ing));
			} catch (InvalidParameter | CriticalError | EmployeeNotConnected | ConnectionFailure
					| ParamIDDoesNotExist e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		selectedIngr.clear();
		createIngredientList();
	}

	@FXML
	void removeManuPress(ActionEvent event) {
		selectedManu.forEach(man -> {
			try {
				manager.removeManufacturer(manufacturars.get(man));
			} catch (InvalidParameter | CriticalError | EmployeeNotConnected | ConnectionFailure
					| ParamIDDoesNotExist e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		selectedManu.clear();
		createManufacturerList();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		// TODO just for test
		for (int i = 1; i <= 20; i++) {
			String item = "Manuf " + i;
			manufacturerList.getItems().add(item);
		}

		manufacturerList.setCellFactory(CheckBoxListCell.forListView(new Callback<String, ObservableValue<Boolean>>() {
			@Override
			public ObservableValue<Boolean> call(String item) {
				BooleanProperty observable = new SimpleBooleanProperty();
				observable.addListener((obs, wasSelected, isNowSelected) -> {
					if (isNowSelected) {
						selectedManu.add(item);
					} else {
						selectedManu.remove(item);
					}
					enableButtons();

				});
				return observable;
			}
		}));

		// TODO just for test
		for (int i = 1; i <= 20; i++) {
			String item = "Ingr " + i;
			ingredientsList.getItems().add(item);
		}

		ingredientsList.setCellFactory(CheckBoxListCell.forListView(new Callback<String, ObservableValue<Boolean>>() {
			@Override
			public ObservableValue<Boolean> call(String item) {
				BooleanProperty observable = new SimpleBooleanProperty();
				observable.addListener((obs, wasSelected, isNowSelected) -> {
					if (isNowSelected) {
						selectedIngr.add(item);
					} else {
						selectedIngr.remove(item);
					}
					enableButtons();

				});
				return observable;
			}
		}));
		
		// TODO enable this later
		//createIngredientList();
		//createManufacturerList();
		enableButtons();

	}

	private void createManufacturerList() {

		manufacturars = new HashMap<String, Manufacturer>();

		try {
			manager.getAllManufacturers().forEach(manufacturer -> {
				manufacturars.put(manufacturer.getName(), manufacturer);
			});
		} catch (InvalidParameter | CriticalError | EmployeeNotConnected | ConnectionFailure e) {
			e.printStackTrace();
		}

		manufacturerList.getItems().addAll(manufacturars.keySet());

	}

	private void createIngredientList() {
		ingredients = new HashMap<String, Ingredient>();
		try {
			manager.getAllIngredients().forEach(ingredient -> {
				ingredients.put(ingredient.getName(), ingredient);
			});
		} catch (InvalidParameter | CriticalError | EmployeeNotConnected | ConnectionFailure e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ingredientsList.getItems().addAll(ingredients.keySet());
	}

	private void enableButtons() {
		addManuBtn.setDisable(!selectedManu.isEmpty());
		removeManuBtn.setDisable(selectedManu.isEmpty());
		addIngrBtn.setDisable(!selectedIngr.isEmpty());
		removeIngrBtn.setDisable(selectedIngr.isEmpty());

	}

}
