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
import GuiUtils.AbstractApplicationScreen;
import GuiUtils.DialogMessagesService;
import GuiUtils.RadioButtonEnabler;
import SMExceptions.SMException;
import UtilsContracts.BarcodeScanEvent;
import UtilsContracts.IBarcodeEventHandler;
import UtilsContracts.SmartcodeScanEvent;
import UtilsImplementations.BarcodeEventHandler;
import UtilsImplementations.InjectionFactory;
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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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

	@FXML
	VBox workerMenuScreenPane;

	@FXML
	VBox scanOrTypeCodePane;

	@FXML
	VBox productAfterScanPane;

	@FXML
	VBox smartcodeOperationsPane;

	@FXML
	VBox barcodeOperationsPane;

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
	DatePicker datePickerForSmartCode;

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

	RadioButtonEnabler radioButtonContainerSmarcodeOperations = new RadioButtonEnabler();

	RadioButtonEnabler radioButtonContainerBarcodeOperations = new RadioButtonEnabler();

	Stage primeStage = EmployeeApplicationScreen.stage;

	IWorker worker;

	CatalogProduct catalogProduct;

	IBarcodeEventHandler barcodeEventHandler;

	int amountInStore = -1;

	int amountInWarehouse = -1;

	@Override
	public void initialize(URL location, ResourceBundle __) {
		AbstractApplicationScreen.fadeTransition(workerMenuScreenPane);
		barcodeEventHandler = InjectionFactory.getInstance(BarcodeEventHandler.class);
		// TODO enable this after solve singleton
		barcodeEventHandler.register(this);
		barcodeTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			addProductParametersToQuickView("N/A", "N/A", "N/A", "N/A", "N/A");
			amountInWarehouse = amountInStore = -1;
			showScanCodePane(true);
		});

		datePicker.setValue(LocalDate.now());

		worker = TempWorkerPassingData.worker;

		// defining behavior when stage/window is closed.
		primeStage.setOnCloseRequest(event -> {
			try {
				if (worker.isServerReachable() && worker.isLoggedIn())
					worker.logout();
			} catch (SMException e) {
				if (e instanceof AEmployeeException.EmployeeNotConnected)
					return;
				EmployeeGuiExeptionHandler.handle(e);
			}
		});

		// setting success log listener
		successLogArea.textProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue<?> __, Object oldValue, Object newValue) {
				successLogArea.setScrollTop(Double.MAX_VALUE); // this will
																// scroll to the
																// bottom
			}
		});

		radioButtonContainerSmarcodeOperations.addRadioButtons(
				(Arrays.asList((new RadioButton[] { printSmartCodeRadioButton, addPackageToStoreRadioButton,
						removePackageFromStoreRadioButton, removePackageFromWarhouseRadioButton }))));

		radioButtonContainerBarcodeOperations
				.addRadioButtons((Arrays.asList((new RadioButton[] { addPakageToWarhouseRadioButton }))));

		showScanCodePane(true);
	}

	private void printToSuccessLog(String msg) {
		successLogArea.appendText(new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss").format(new Date()) + " :: " + msg + "\n");
	}

	private void addProductParametersToQuickView(String productName, String productBarcode,
			String productExpirationDate, String amountInStore, String amountInWarehouse) {
		smartCodeValLabel.setText(productBarcode);

		productNameValLabel.setText(productName);

		expirationDateValLabel.setText(productExpirationDate);

		amoutInStoreValLabel.setText(amountInStore);

		AmountInWarehouseValLabel.setText(amountInWarehouse);

	}

	private void enableRunTheOperationButton() {

		if (barcodeOperationsPane.isVisible()) {
			if (addPakageToWarhouseRadioButton.isSelected()) {

			}
		} else {

			if (removePackageFromStoreRadioButton.isSelected()) {
				runTheOperationButton.setDisable(amountInStore < editPackagesAmountSpinner.getValue());

			} else if (removePackageFromWarhouseRadioButton.isSelected()) {
				runTheOperationButton.setDisable(amountInWarehouse < editPackagesAmountSpinner.getValue());

			} else if (addPackageToStoreRadioButton.isSelected()) {
				runTheOperationButton.setDisable(amountInWarehouse < editPackagesAmountSpinner.getValue());

			}
		}
	}

	private void showScanCodePane(boolean show) {

		scanOrTypeCodePane.setVisible(show);
		productAfterScanPane.setVisible(!show);
		datePickerForSmartCode.setVisible(show);
		if (show) {
			scanOrTypeCodePane.toFront();
		} else {
			datePickerForSmartCode.toFront();
		}

		datePickerForSmartCode.setValue(null);
	}

	private void showSmartCodeOperationsPane(boolean show) {
		smartcodeOperationsPane.setVisible(show);
		barcodeOperationsPane.setVisible(!show);
		if (show) {
			smartcodeOperationsPane.toFront();
		} else {
			barcodeOperationsPane.toFront();
		}
	}

	private void getProductCatalog(String barcode) throws SMException {

		catalogProduct = null;
		catalogProduct = worker.viewProductFromCatalog(Long.parseLong(barcodeTextField.getText()));

	}

	private void barcodeEntered(String barcode) throws SMException {

		getProductCatalog(barcodeTextField.getText());
		showScanCodePane(false);
		showSmartCodeOperationsPane(false);

		addProductParametersToQuickView(barcodeTextField.getText(), catalogProduct.getName(), "N/A", "N/A", "N/A");

	}

	private void smartcodeEntered(SmartCode smartcode) throws SMException {

		getProductCatalog(barcodeTextField.getText());

		int amountInStore = worker
				.getProductPackageAmount(new ProductPackage(smartcode, 1, new Location(0, 0, PlaceInMarket.STORE)));
		int amountInWarehouse = worker
				.getProductPackageAmount(new ProductPackage(smartcode, 1, new Location(0, 0, PlaceInMarket.WAREHOUSE)));

		this.amountInStore = amountInStore;
		this.amountInWarehouse = amountInWarehouse;

		showScanCodePane(false);
		showSmartCodeOperationsPane(true);

		addProductParametersToQuickView(barcodeTextField.getText(), catalogProduct.getName(),
				smartcode.getExpirationDate().toString(), Integer.toString(amountInStore),
				Integer.toString(amountInWarehouse));

	}

	@FXML
	private void searchCodeButtonPressed(ActionEvent __) {
		try {
			LocalDate expirationDate = datePickerForSmartCode.getValue();

			if (expirationDate == null) {

				barcodeEntered(barcodeTextField.getText());

			} else {

				smartcodeEntered(new SmartCode(Long.parseLong(barcodeTextField.getText()), expirationDate));
			}

		} catch (SMException e) {
			EmployeeGuiExeptionHandler.handle(e);
			return;
		}
	}

	@FXML
	private void radioButtonHandling(ActionEvent ¢) {
		radioButtonContainerSmarcodeOperations.selectRadioButton((RadioButton) ¢.getSource());
		radioButtonContainerBarcodeOperations.selectRadioButton((RadioButton) ¢.getSource());
		enableRunTheOperationButton();
	}

	@FXML
	private void runTheOperationButtonPressed(ActionEvent __) {

		SmartCode smartcode = new SmartCode(Long.parseLong(barcodeTextField.getText()), datePicker.getValue());
		/*
		 * TODO - find what's wrong with using getValue on the spinner. it keeps
		 * throwing exception about not being able to case int to doulbe (or the
		 * opposite)
		 */

		// int amountVal = editPackagesAmountSpinner.getValue();
		int amountVal = 1;

		try {
			if (addPakageToWarhouseRadioButton.isSelected()) {

				// init
				Location loc = new Location(0, 0, PlaceInMarket.WAREHOUSE);
				ProductPackage pp = new ProductPackage(smartcode, amountVal, loc);

				// exec
				worker.addProductToWarehouse(pp);

				printToSuccessLog(("Added (" + amountVal + ") " + "product packages (" + pp + ") to warehouse"));

			} else if (addPackageToStoreRadioButton.isSelected()) {

				// init
				Location loc = new Location(0, 0, PlaceInMarket.STORE);
				ProductPackage pp = new ProductPackage(smartcode, amountVal, loc);

				// exec
				worker.placeProductPackageOnShelves(pp);

				printToSuccessLog(("Added (" + amountVal + ") " + "product packages (" + pp + ") to store"));

			} else if (removePackageFromStoreRadioButton.isSelected()) {

				// init
				Location loc = new Location(0, 0, PlaceInMarket.STORE);
				ProductPackage pp = new ProductPackage(smartcode, amountVal, loc);

				// exec
				worker.removeProductPackageFromStore(pp);

				printToSuccessLog(("Removed (" + amountVal + ") " + "product packages (" + pp + ") from store"));

			} else if (removePackageFromWarhouseRadioButton.isSelected()) {
				Location loc = new Location(0, 0, PlaceInMarket.WAREHOUSE);
				ProductPackage pp = new ProductPackage(smartcode, amountVal, loc);
				worker.removeProductPackageFromStore(pp);
				printToSuccessLog(("Removed (" + amountVal + ") " + "product packages (" + pp + ") from warehouse"));
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
		// TODO enable this after solve singleton
		barcodeEventHandler.unregister(this);
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

	@Subscribe
	public void barcodeScanned(BarcodeScanEvent ¢) {
		barcodeTextField.setText(Long.toString(¢.getBarcode()));
		enableRunTheOperationButton();

	}

	@Subscribe
	public void smartcoseScanned(SmartcodeScanEvent ¢) {
		SmartCode smartcode = ¢.getSmarCode();
		barcodeTextField.setText(Long.toString(smartcode.getBarcode()));
		enableRunTheOperationButton();

	}
}
