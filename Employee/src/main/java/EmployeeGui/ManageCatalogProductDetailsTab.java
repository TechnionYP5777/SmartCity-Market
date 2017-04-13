package EmployeeGui;

import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXPopup.PopupHPosition;
import com.jfoenix.controls.JFXPopup.PopupVPosition;
import com.jfoenix.controls.JFXRippler;
import com.jfoenix.controls.JFXRippler.RipplerMask;
import com.jfoenix.controls.JFXRippler.RipplerPos;

import EmployeeContracts.IManager;
import EmployeeImplementations.Manager;
import UtilsImplementations.InjectionFactory;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.layout.StackPane;
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

	@FXML
	void addIngPressed(ActionEvent event) {

	}

	@FXML
	void addManuPressed(ActionEvent event) {

	}

	@FXML
	void removeIngrPressed(ActionEvent event) {

	}

	@FXML
	void removeManuPress(ActionEvent event) {

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
		
		enableButtons();

	}

	private void createManuList() {

	}

	private void createIngrList() {

	}

	private void enableButtons() {
		addManuBtn.setDisable(!selectedManu.isEmpty());
		removeManuBtn.setDisable(selectedManu.isEmpty());
		addIngrBtn.setDisable(!selectedIngr.isEmpty());
		removeIngrBtn.setDisable(selectedIngr.isEmpty());

	}

}
