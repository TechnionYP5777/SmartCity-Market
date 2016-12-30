package EmployeeGui;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import BasicCommonClasses.CatalogProduct;
import BasicCommonClasses.Location;
import BasicCommonClasses.PlaceInMarket;
import BasicCommonClasses.ProductPackage;
import BasicCommonClasses.SmartCode;
import EmployeeCommon.TempWorkerPassingData;
import EmployeeContracts.IWorker;
import GuiUtils.AbstractApplicationScreen;
import GuiUtils.DialogMessagesService;
import SMExceptions.SMException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

/**
 * WorkerMenuScreen - Controller for menu screen which holds the operations
 * available for worker.
 * 
 * @author idan atias
 * @author Shimon Azulay
 * @since 2016-12-27
 */

public class WorkerMenuScreen implements Initializable {

	// Main screen panes
	@FXML
	GridPane workerMenuScreenPane;

	// view catalog product controls
	@FXML
	VBox viewCatalogProductPane;

	@FXML
	Button viewCatalogProductButton;

	@FXML
	TextField barcodeTextField;

	@FXML
	Button searchBarcode;

	// edit product package controls
	@FXML
	VBox addProductPackagePane;

	@FXML
	TextField editPackagesBarcodeTextField;

	@FXML
	Button runTheOperationButton;

	@FXML
	Spinner<Integer> editPackagesAmountSpinner;

	@FXML
	DatePicker editPackagesDatePicker;

	@FXML
	RadioButton addPakageToWarhouseRadioButton;

	@FXML
	RadioButton addPackageToShelvesRadioButton;

	@FXML
	RadioButton removePackageFromStoreRadioButton;

	IWorker worker;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		AbstractApplicationScreen.fadeTransition(workerMenuScreenPane);
		barcodeTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			enableSearchBarcodeButtonCheck();
		});
		editPackagesBarcodeTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			enableRunTheOperationButton();
		});
		editPackagesDatePicker.setValue(LocalDate.now());
		worker = TempWorkerPassingData.worker;
	}

	private void enableSearchBarcodeButtonCheck() {
		searchBarcode.setDisable(barcodeTextField.getText().isEmpty());
	}

	private void enableRunTheOperationButton() {
		runTheOperationButton.setDisable(editPackagesBarcodeTextField.getText().isEmpty());
	}

	@FXML
	private void searchBarcodePressed(ActionEvent event) {
		CatalogProduct catalogProduct = null;
		try {
			catalogProduct = worker.viewProductFromCatalog(Integer.parseInt(barcodeTextField.getText()));
		} catch (SMException e){
			EmployeeGuiExeptionHandler.handle(e);
			e.printStackTrace();
		}
		if (catalogProduct == null) {
			return;
		}
		DialogMessagesService.showInfoDialog(catalogProduct.getName(),
				"Description: " + catalogProduct.getDescription(),
				"Barcode: " + catalogProduct.getBarcode() + "\n" + "Manufacturer: "
						+ catalogProduct.getManufacturer().getName() + "\n" + "Price: " + catalogProduct.getPrice());

	}

	@FXML
	private void addPakageToWarhouseRadioButtonPressed(ActionEvent event) {

		addPakageToWarhouseRadioButton.setSelected(true);

		addPackageToShelvesRadioButton.setSelected(false);

		removePackageFromStoreRadioButton.setSelected(false);
	}

	@FXML
	private void addPackageToShelvesRadioButtonPressed(ActionEvent event) {

		addPakageToWarhouseRadioButton.setSelected(false);

		addPackageToShelvesRadioButton.setSelected(true);

		removePackageFromStoreRadioButton.setSelected(false);
	}

	@FXML
	private void removePackageFromStoreRadioButtonPressed(ActionEvent event) {

		addPakageToWarhouseRadioButton.setSelected(false);

		addPackageToShelvesRadioButton.setSelected(false);

		removePackageFromStoreRadioButton.setSelected(true);
	}

	@FXML
	private void runTheOperationButtonPressed(ActionEvent event) {

		SmartCode smartcode = new SmartCode(Long.parseLong(editPackagesBarcodeTextField.getText()),
				editPackagesDatePicker.getValue(), "java.time");

		try {

			if (addPakageToWarhouseRadioButton.isSelected()) {

				// TODO location x y
				ProductPackage productPackage = new ProductPackage(smartcode, editPackagesAmountSpinner.getValue(),
						new Location(0, 0, PlaceInMarket.WAREHOUSE));

				worker.addProductToWarehouse(productPackage);

			} else if (addPackageToShelvesRadioButton.isSelected()) {

				// TODO location x y
				ProductPackage productPackage = new ProductPackage(smartcode, editPackagesAmountSpinner.getValue(),
						new Location(0, 0, PlaceInMarket.STORE));

				worker.placeProductPackageOnShelves(productPackage);

			} else {

				// TODO location is store or warehouse and what about x y ?
				ProductPackage productPackage = new ProductPackage(smartcode, editPackagesAmountSpinner.getValue(),
						new Location(0, 0, PlaceInMarket.STORE));

				worker.removeProductPackageFromStore(productPackage);
			}

		} catch (SMException e){
			EmployeeGuiExeptionHandler.handle(e);
			e.printStackTrace();
		}
	}
	
	@FXML
	private void logoutButtonPressed(ActionEvent event) {
		try {
			worker.logout();
		} catch (SMException e){
			EmployeeGuiExeptionHandler.handle(e);
			e.printStackTrace();
		}

		AbstractApplicationScreen.setScene("/EmployeeMainScreen/EmployeeLoginScreen.fxml");
	}

}
