package EmployeeGui;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.ResourceBundle;

import com.google.common.eventbus.Subscribe;

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
import UtilsContracts.BarcodeEventHandlerDiConfigurator;
import UtilsContracts.BarcodeScanEvent;
import UtilsContracts.IBarcodeEventHandler;
import UtilsImplementations.BarcodeEventHandler;
import UtilsImplementations.InjectionFactory;
//import UtilsImplementations.BarcodeScanner;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
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
	TextArea successLogArea;
	
	@FXML
	Label smartCodeValLabel;

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

	CatalogProduct catalogProduct;
	
	IBarcodeEventHandler barcodeEventHandler;

	int amountInStore = -1;

	int amountInWarehouse = -1;

	@Override
	public void initialize(URL location, ResourceBundle __) {
		AbstractApplicationScreen.fadeTransition(workerMenuScreenPane);
		barcodeEventHandler = InjectionFactory.getInstance(BarcodeEventHandler.class,
				new BarcodeEventHandlerDiConfigurator());
		// TODO enable this after solve singleton
		//barcodeEventHandler.register(this);
		barcodeTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			addProductParametersToQuickView("N/A", "N/A", "N/A", "N/A", "N/A");
			amountInStore = -1;
			amountInWarehouse = -1;
			enableRunTheOperationButton();

		});
		datePicker.setValue(LocalDate.now());
		worker = TempWorkerPassingData.worker;

		enableRunTheOperationButton();
		addPackageToWarehouseButtonPressedCheck();

		// defining behavior when stage/window is closed.
		primeStage.setOnCloseRequest(event -> {
			try {
				if (EmployeeApplicationScreen.isEmployeeLoggedIn){
					worker.logout();
				}
			} catch (SMException e) {
				if (e instanceof AEmployeeException.EmployeeNotConnected)
					return;
				EmployeeGuiExeptionHandler.handle(e);
			}
		});

		//setting success log listener
		successLogArea.textProperty().addListener(new ChangeListener<Object>(){
		    @Override
			public void changed(ObservableValue<?> observable, Object oldValue,
		            Object newValue) {
		    	successLogArea.setScrollTop(Double.MAX_VALUE); //this will scroll to the bottom
		    }
		});

		RadioButton[] radioButtonsArray = { printSmartCodeRadioButton, addPackageToStoreRadioButton,
				removePackageFromStoreRadioButton, removePackageFromWarhouseRadioButton,
				addPakageToWarhouseRadioButton };

		radioButtonContainer.addRadioButtons((Arrays.asList(radioButtonsArray)));

	}
	
	private void printToSuccessLog(String msg) {
		String timeStamp = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss").format(new Date());
		successLogArea.appendText(timeStamp + " :: " + msg + "\n");
	}

	private void addProductParametersToQuickView(String productName, String productBarcode,
			String productExpirationDate, String amountInStore, String amountInWarehouse) {
		smartCodeValLabel.setText(productBarcode);

		productNameValLabel.setText(productName);

		// TODO should get expiration date from the smart code
		expirationDateValLabel.setText(productExpirationDate);

		// TODO get amounts from server
		amoutInStoreValLabel.setText(amountInStore);
		AmountInWarehouseValLabel.setText(amountInWarehouse);

	}

	private void enableRunTheOperationButton() {
		if (addPakageToWarhouseRadioButton.isSelected()) {
			runTheOperationButton.setDisable(barcodeTextField.getText().equals(""));
			runTheOperationButton.setTooltip(
					barcodeTextField.getText().equals("") ? new Tooltip(EmployeeGuiDefs.typeBarcode) : new Tooltip(""));
		} else {
			showMoreDetailsButton.setDisable(smartCodeValLabel.getText().equals("N/A"));
			if (removePackageFromStoreRadioButton.isSelected()) {
				runTheOperationButton.setDisable(amountInStore < editPackagesAmountSpinner.getValue());
				runTheOperationButton.setTooltip(new Tooltip(EmployeeGuiDefs.notEnoughAmountInStore));
				return;
			} else if (removePackageFromWarhouseRadioButton.isSelected()) {
				runTheOperationButton.setDisable(amountInWarehouse < editPackagesAmountSpinner.getValue());
				runTheOperationButton.setTooltip(new Tooltip(EmployeeGuiDefs.notEnoughAmountInWarehouse));
				return;
			} else if (addPackageToStoreRadioButton.isSelected()) {
				runTheOperationButton.setDisable(amountInWarehouse < editPackagesAmountSpinner.getValue());
				runTheOperationButton.setTooltip(new Tooltip(EmployeeGuiDefs.notEnoughAmountInStore));
				return;
			}
			runTheOperationButton.setDisable(smartCodeValLabel.getText().equals("N/A"));
			runTheOperationButton.setTooltip(
					new Tooltip(smartCodeValLabel.getText().equals("N/A") ? EmployeeGuiDefs.typeSmartCode : ""));
		}
	}

	@FXML
	private void searchBarcodePressed(ActionEvent __) {
		if (!addPakageToWarhouseRadioButton.isSelected()) {
			catalogProduct = null;
			try {
				catalogProduct = worker.viewProductFromCatalog(Long.parseLong(barcodeTextField.getText()));

				// TODO parse the expiration date from the smart code
				SmartCode smartcode = new SmartCode(Long.parseLong(barcodeTextField.getText()), LocalDate.now());

				amountInStore = worker.getProductPackageAmount(
						new ProductPackage(smartcode, 0, new Location(0, 0, PlaceInMarket.STORE)));

				amountInWarehouse = worker.getProductPackageAmount(
						new ProductPackage(smartcode, 0, new Location(0, 0, PlaceInMarket.WAREHOUSE)));

			} catch (SMException e) {
				EmployeeGuiExeptionHandler.handle(e);
				return;
			}

			if (catalogProduct != null) {
				// TODO parse the expiration date
				addProductParametersToQuickView(barcodeTextField.getText(), catalogProduct.getName(), "N/A",
						Integer.toString(amountInStore), Integer.toString(amountInWarehouse));
			}
			runTheOperationButton.setDisable(false);
		}
	}

	@FXML
	private void radioButtonHandling(ActionEvent event) {
		radioButtonContainer.selectRadioButton((RadioButton) event.getSource());
		addPackageToWarehouseButtonPressedCheck();
		enableRunTheOperationButton();
	}

	@FXML
	private void runTheOperationButtonPressed(ActionEvent __) {

		SmartCode smartcode = new SmartCode(Long.parseLong(barcodeTextField.getText()), datePicker.getValue());
		/*TODO - find what's wrong with using getValue on the spinner. it keeps throwing exception about not
		 * being able to case int to doulbe (or the opposite)
		 */

		//int amountVal = editPackagesAmountSpinner.getValue();
		int amountVal = 1;

		try {
			if (addPakageToWarhouseRadioButton.isSelected()){
				
				//init
				Location loc = new Location(0, 0, PlaceInMarket.WAREHOUSE);
				ProductPackage pp = new ProductPackage(smartcode, amountVal, loc);
				
				//exec
				worker.addProductToWarehouse(pp);
				
				//log
				String successLogMsg = "Added (" + amountVal + ") ";
				successLogMsg += "product packages (" + pp.toString() + ") to warehouse";
				printToSuccessLog(successLogMsg);
			
			} else if (addPackageToStoreRadioButton.isSelected()) {
				
				//init
				Location loc = new Location(0, 0, PlaceInMarket.STORE);
				ProductPackage pp = new ProductPackage(smartcode, amountVal, loc);
				
				//exec
				worker.placeProductPackageOnShelves(pp);
				
				//log
				String successLogMsg = "Added (" + amountVal + ") ";
				successLogMsg += "product packages (" + pp.toString() + ") to store";
				printToSuccessLog(successLogMsg);
			
			} else if (removePackageFromStoreRadioButton.isSelected()) {
				
				//init
				Location loc = new Location(0, 0, PlaceInMarket.STORE);
				ProductPackage pp = new ProductPackage(smartcode, amountVal, loc);
				
				//exec
				worker.removeProductPackageFromStore(pp);
				
				//log
				String successLogMsg = "Removed (" + amountVal + ") ";
				successLogMsg += "product packages (" + pp.toString() + ") from store";
				printToSuccessLog(successLogMsg);
			
			} else if (removePackageFromWarhouseRadioButton.isSelected()) {
				Location loc = new Location(0, 0, PlaceInMarket.WAREHOUSE);
				ProductPackage pp = new ProductPackage(smartcode, amountVal, loc);
				
				//exec
				worker.removeProductPackageFromStore(pp);
				
				//log
				String successLogMsg = "Removed (" + amountVal + ") ";
				successLogMsg += "product packages (" + pp.toString() + ") from warehouse";
				printToSuccessLog(successLogMsg);
			} else {
				// TODO print the smart code
			}
		} catch (SMException e) {
			EmployeeGuiExeptionHandler.handle(e);
		}
	}

	@FXML
	private void logoutButtonPressed(ActionEvent __) {
		try {
			worker.logout();
			EmployeeApplicationScreen.isEmployeeLoggedIn = false;
		} catch (SMException e) {
			EmployeeGuiExeptionHandler.handle(e);
		}
		// TODO enable this after solve singleton
		// barcodeEventHandler.unregister(this);
		AbstractApplicationScreen.setScene("/EmployeeLoginScreen/EmployeeLoginScreen.fxml");
	}

	@FXML
	private void showMoreDetailsButtonPressed(ActionEvent __) {
		if (catalogProduct != null)
			DialogMessagesService.showInfoDialog(catalogProduct.getName(),
					"Description: " + catalogProduct.getDescription(),
					"Barcode: " + catalogProduct.getBarcode() + "\n" + "Manufacturer: "
							+ catalogProduct.getManufacturer().getName() + "\n" + "Price: "
							+ catalogProduct.getPrice());

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
	
	@Subscribe
	public void barcodeScanned(BarcodeScanEvent event) {
		
		if (/* it is smartcode */ true) {
			barcodeTextField.setText(event.getBarcode());
			enableRunTheOperationButton();
			searchBarcodePressed(null);
		}
	}

}
