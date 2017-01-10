package EmployeeGui;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.ResourceBundle;

import BasicCommonClasses.CatalogProduct;
import BasicCommonClasses.Location;
import BasicCommonClasses.PlaceInMarket;
import BasicCommonClasses.ProductPackage;
import BasicCommonClasses.SmartCode;
import EmployeeCommon.TempWorkerPassingData;
import EmployeeContracts.IWorker;
import EmployeeDefs.AEmployeeException;
import EmployeeDefs.EmployeeGuiDefs;
import GuiUtils.AbstractApplicationScreen;
import GuiUtils.DialogMessagesService;
import GuiUtils.RadioButtonEnabler;
import SMExceptions.SMException;
import UtilsImplementations.BarcodeScanner;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

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
	
	@FXML
	GridPane quickProductDetailsPane;
	
	@FXML
	HBox addPackageToWarehouseParametersPane;

	@FXML
	TextField barcodeTextField;

	@FXML
	Button showMoreDetailsButton;

	@FXML
	Label productNameValLabel;

	@FXML
	Label expirationDateValLabel;

	@FXML
	Label amoutInStoreValLabel;

	@FXML
	Label AmountInWarehouseValLabel;

	@FXML
	Label expirationDateLabel;

	@FXML
	CheckBox printTheSmartCodeCheckBox;

	@FXML
	Button runTheOperationButton;

	@FXML
	Spinner<Integer> editPackagesAmountSpinner;

	@FXML
	DatePicker datePicker;

	@FXML
	RadioButton printSmartCodeRadioButton;

	@FXML
	RadioButton addPackageToStoreRadioButton;

	@FXML
	RadioButton removePackageFromStoreRadioButton;

	@FXML
	RadioButton removePackageFromWarhouseRadioButton;

	@FXML
	RadioButton addPakageToWarhouseRadioButton;

	RadioButtonEnabler radioButtonContainer = new RadioButtonEnabler();

	Stage primeStage = EmployeeApplicationScreen.stage;

	IWorker worker;

	@Override
	public void initialize(URL location, ResourceBundle __) {
		AbstractApplicationScreen.fadeTransition(workerMenuScreenPane);
		barcodeTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			enableRunTheOperationButton();
		});
		datePicker.setValue(LocalDate.now());
		worker = TempWorkerPassingData.worker;
		
		enableRunTheOperationButton();
		addPackageToWarehouseButtonPressedCheck();

		//defining behavior when stage/window is closed.
		primeStage.setOnCloseRequest(event -> {
			try {
				worker.logout();
			} catch (SMException e){
				if (e instanceof AEmployeeException.EmployeeNotConnected)
					return;
				EmployeeGuiExeptionHandler.handle(e);
			}
		});
		
		RadioButton[] radioButtonsArray = { printSmartCodeRadioButton, addPackageToStoreRadioButton, 
				removePackageFromStoreRadioButton, removePackageFromWarhouseRadioButton, addPakageToWarhouseRadioButton };
		
		radioButtonContainer.addRadioButtons((Arrays.asList(radioButtonsArray)));
		
	}

	private void enableRunTheOperationButton() {
		runTheOperationButton.setDisable(barcodeTextField.getText().isEmpty());
	}

	@FXML
	private void scanBarcodePressed(ActionEvent __) {
		CatalogProduct catalogProduct = null;
		String barcode = null;

		try {
			// TODO Shimon - add Waiting for scanner dialog
			barcode = new BarcodeScanner().getBarcodeFromScanner();
		} catch (IOException e1) {
			DialogMessagesService.showErrorDialog(EmployeeGuiDefs.scanFailureDialogTitle, null,
					EmployeeGuiDefs.criticalErrorMsg);
		}

		try {
			catalogProduct = worker.viewProductFromCatalog(Long.parseLong(barcode));
		} catch (SMException e) {
			EmployeeGuiExeptionHandler.handle(e);
		}
		if (catalogProduct != null)
			DialogMessagesService.showInfoDialog(catalogProduct.getName(),
					"Description: " + catalogProduct.getDescription(),
					"Barcode: " + catalogProduct.getBarcode() + "\n" + "Manufacturer: "
							+ catalogProduct.getManufacturer().getName() + "\n" + "Price: "
							+ catalogProduct.getPrice());

	}

	@FXML
	private void searchBarcodePressed(ActionEvent __) {
		CatalogProduct catalogProduct = null;
		try {
			catalogProduct = worker.viewProductFromCatalog(Long.parseLong(barcodeTextField.getText()));
		} catch (SMException e) {
			EmployeeGuiExeptionHandler.handle(e);
		}
		if (catalogProduct != null)
			DialogMessagesService.showInfoDialog(catalogProduct.getName(),
					"Description: " + catalogProduct.getDescription(),
					"Barcode: " + catalogProduct.getBarcode() + "\n" + "Manufacturer: "
							+ catalogProduct.getManufacturer().getName() + "\n" + "Price: "
							+ catalogProduct.getPrice());

	}

	@FXML
	private void radioButtonHandling(ActionEvent event) {
		radioButtonContainer.selectRadioButton((RadioButton) event.getSource());
		addPackageToWarehouseButtonPressedCheck();
	}

	// @FXML
	// private void printSmartCodeRadioButtonPressed(ActionEvent __) {
	//
	//
	// }
	//
	// @FXML
	// private void addPakageToWarhouseRadioButtonPressed(ActionEvent __) {
	//
	// addPakageToWarhouseRadioButton.setSelected(true);
	//
	// addPackageToStoreRadioButton.setSelected(false);
	//
	// removePackageFromStoreRadioButton.setSelected(false);
	//
	// }
	//
	// @FXML
	// private void addPackageToShelvesRadioButtonPressed(ActionEvent __) {
	//
	// addPakageToWarhouseRadioButton.setSelected(false);
	//
	// addPackageToStoreRadioButton.setSelected(true);
	//
	// removePackageFromStoreRadioButton.setSelected(false);
	// }
	//
	// @FXML
	// private void removePackageFromStoreRadioButtonPressed(ActionEvent __) {
	//
	// addPakageToWarhouseRadioButton.setSelected(false);
	//
	// addPackageToStoreRadioButton.setSelected(false);
	//
	// removePackageFromStoreRadioButton.setSelected(true);
	// }

	@FXML
	private void runTheOperationButtonPressed(ActionEvent __) {

		SmartCode smartcode = new SmartCode(Long.parseLong(barcodeTextField.getText()), datePicker.getValue());

		try {

			if (addPakageToWarhouseRadioButton.isSelected())
				worker.addProductToWarehouse((new ProductPackage(smartcode, editPackagesAmountSpinner.getValue(),
						new Location(0, 0, PlaceInMarket.WAREHOUSE))));
			else {
				ProductPackage productPackage = new ProductPackage(smartcode, editPackagesAmountSpinner.getValue(),
						new Location(0, 0, PlaceInMarket.STORE));
				if (addPackageToStoreRadioButton.isSelected())
					worker.placeProductPackageOnShelves(productPackage);
				else
					worker.removeProductPackageFromStore(productPackage);
			}

		} catch (SMException e) {
			EmployeeGuiExeptionHandler.handle(e);
		}
	}

	@FXML
	private void logoutButtonPressed(ActionEvent __) {
		try {
			worker.logout();
		} catch (SMException e) {
			EmployeeGuiExeptionHandler.handle(e);
		}

		AbstractApplicationScreen.setScene("/EmployeeLoginScreen/EmployeeLoginScreen.fxml");
	}

	@FXML
	private void showMoreDetailsButtonPressed(ActionEvent __) {

	}

	// Enable Disable Show more info, date picker. Change prompt text

	private void addPackageToWarehouseButtonPressedCheck() {
		if (addPakageToWarhouseRadioButton.isSelected()) {
			barcodeTextField.setPromptText(EmployeeGuiDefs.barcodeCodePrompt);
			disableAddPackageToWarehouseButtonParameters(false);
		} else {
			barcodeTextField.setPromptText(EmployeeGuiDefs.smartCodePrompt);
			disableAddPackageToWarehouseButtonParameters(true);
		}
	}

	private void disableAddPackageToWarehouseButtonParameters(boolean disable) {
		addPackageToWarehouseParametersPane.setDisable(disable);
		quickProductDetailsPane.setDisable(!disable);

	}

}
