package EmployeeGui;

import java.net.URL;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.google.common.eventbus.Subscribe;

import BasicCommonClasses.CatalogProduct;
import BasicCommonClasses.Location;
import BasicCommonClasses.PlaceInMarket;
import BasicCommonClasses.ProductPackage;
import BasicCommonClasses.SmartCode;
import EmployeeContracts.IWorker;
import EmployeeImplementations.Manager;
import GuiUtils.DialogMessagesService;
import GuiUtils.RadioButtonEnabler;
import SMExceptions.SMException;
import SmartcodeParser.SmartcodePrint;
import UtilsContracts.BarcodeScanEvent;
import UtilsContracts.IBarcodeEventHandler;
import UtilsContracts.SmartcodeScanEvent;
import UtilsImplementations.BarcodeEventHandler;
import UtilsImplementations.InjectionFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

/**
 * ManagePackagesTab - This class is the controller for the Manage Packages Tab
 * all action of this tab should be here.
 * 
 * @author Shimon Azulay
 */

public class ManagePackagesTab implements Initializable {

	static Logger log = Logger.getLogger(ManagePackagesTab.class.getName());

	@FXML
	VBox rootPane;

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

	@FXML
	Button searchCodeButton;

	RadioButtonEnabler radioButtonContainerSmarcodeOperations = new RadioButtonEnabler();

	RadioButtonEnabler radioButtonContainerBarcodeOperations = new RadioButtonEnabler();

	CatalogProduct catalogProduct;

	int amountInStore = -1;

	int amountInWarehouse = -1;

	LocalDate expirationDate;

	IBarcodeEventHandler barcodeEventHandler = InjectionFactory.getInstance(BarcodeEventHandler.class);

	IWorker worker = InjectionFactory.getInstance(Manager.class);

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		barcodeEventHandler.register(this);
		barcodeTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.matches("\\d*")) {
				barcodeTextField.setText(newValue.replaceAll("[^\\d]", ""));
			}
			showScanCodePane(true);
			resetParams();
			searchCodeButton.setDisable(newValue.isEmpty());
		});

		editPackagesAmountSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue == null || newValue < 1) {
				editPackagesAmountSpinner.getValueFactory().setValue(oldValue);
			}
			enableRunTheOperationButton();
		});

		editPackagesAmountSpinner.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null && !newValue) {
				editPackagesAmountSpinner.increment(0);
				enableRunTheOperationButton();
			}
		});

		final Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
			@Override
			public DateCell call(final DatePicker datePicker) {
				return new DateCell() {
					@Override
					public void updateItem(LocalDate item, boolean empty) {
						super.updateItem(item, empty);

						if (item.isBefore(LocalDate.now())) {
							setDisable(true);
							setStyle("-fx-background-color: #EEEEEE;");
						}
					}
				};
			}
		};
		datePicker.setDayCellFactory(dayCellFactory);
		datePicker.setValue(LocalDate.now());

		radioButtonContainerSmarcodeOperations.addRadioButtons(
				(Arrays.asList((new RadioButton[] { printSmartCodeRadioButton, addPackageToStoreRadioButton,
						removePackageFromStoreRadioButton, removePackageFromWarhouseRadioButton }))));

		radioButtonContainerBarcodeOperations
				.addRadioButtons((Arrays.asList((new RadioButton[] { addPakageToWarhouseRadioButton }))));

		showScanCodePane(true);

	}

	@FXML
	void mouseClikedOnBarcodeField(MouseEvent event) {
		showScanCodePane(true);
		resetParams();
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
		log.info("===============================enableRunTheOperationButton======================================");
		if (barcodeOperationsPane.isVisible()) {
			log.info("barcode pane is visible");
			log.info("addPakageToWarhouseRadioButton is selected: " + addPakageToWarhouseRadioButton.isSelected());
			runTheOperationButton.setDisable(!addPakageToWarhouseRadioButton.isSelected());
		} else {
			log.info("barcode pane is invisible");

			if (removePackageFromStoreRadioButton.isSelected()) {
				log.info("removePackageFromStoreRadioButton");
				log.info("amount in store: " + amountInStore + " ; amount in spinner: "
						+ editPackagesAmountSpinner.getValue());
				runTheOperationButton.setDisable(amountInStore < editPackagesAmountSpinner.getValue());

			} else if (removePackageFromWarhouseRadioButton.isSelected()) {
				log.info("removePackageFromWarhouseRadioButton");
				log.info("amount in werehouse: " + amountInWarehouse + " ; amount in spinner: "
						+ editPackagesAmountSpinner.getValue());
				runTheOperationButton.setDisable(amountInWarehouse < editPackagesAmountSpinner.getValue());

			} else if (addPackageToStoreRadioButton.isSelected()) {
				log.info("addPackageToStoreRadioButton");
				log.info("amount in werehouse: " + amountInWarehouse + " ; amount in spinner: "
						+ editPackagesAmountSpinner.getValue());
				runTheOperationButton.setDisable(amountInWarehouse < editPackagesAmountSpinner.getValue());

			} else if (printSmartCodeRadioButton.isSelected()) {
				log.info("printSmartCodeRadioButton");
				runTheOperationButton.setDisable(false);
			} else {
				runTheOperationButton.setDisable(true);
			}
		}
		log.info("===============================enableRunTheOperationButton======================================");
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
	}

	private void resetParams() {
		datePickerForSmartCode.setValue(null);
		this.expirationDate = null;
		addProductParametersToQuickView("N/A", "N/A", "N/A", "N/A", "N/A");
		amountInWarehouse = amountInStore = -1;
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

		addProductParametersToQuickView(catalogProduct.getName(), barcodeTextField.getText(), "N/A", "N/A", "N/A");

	}

	private void smartcodeEntered(SmartCode smartcode) throws SMException {
		log.info("===============================smartcodeEntered======================================");
		getProductCatalog(barcodeTextField.getText());

		int amountInStore = worker
				.getProductPackageAmount(new ProductPackage(smartcode, 1, new Location(0, 0, PlaceInMarket.STORE)));
		int amountInWarehouse = worker
				.getProductPackageAmount(new ProductPackage(smartcode, 1, new Location(0, 0, PlaceInMarket.WAREHOUSE)));

		this.amountInStore = amountInStore;
		this.amountInWarehouse = amountInWarehouse;

		showScanCodePane(false);
		showSmartCodeOperationsPane(true);

		addProductParametersToQuickView(catalogProduct.getName(), barcodeTextField.getText(),
				smartcode.getExpirationDate().toString(), Integer.toString(amountInStore),
				Integer.toString(amountInWarehouse));
		log.info("amount in store: " + amountInStore);
		log.info("amount in werehouse: " + amountInWarehouse);
		log.info("===============================smartcodeEntered======================================");

	}

	@FXML
	private void searchCodeButtonPressed(ActionEvent __) {
		try {
			LocalDate expirationDate = (this.expirationDate == null) ? datePickerForSmartCode.getValue()
					: this.expirationDate;

			if (expirationDate == null) {

				barcodeEntered(barcodeTextField.getText());

			} else {

				smartcodeEntered(new SmartCode(Long.parseLong(barcodeTextField.getText()), expirationDate));
			}

			this.expirationDate = expirationDate;

		} catch (SMException e) {
			EmployeeGuiExeptionHandler.handle(e);
			return;
		}

		enableRunTheOperationButton();
	}

	@FXML
	private void radioButtonHandling(ActionEvent ¢) {
		radioButtonContainerSmarcodeOperations.selectRadioButton((RadioButton) ¢.getSource());
		enableRunTheOperationButton();
	}

	@FXML
	private void radioButtonHandlingBarcode(ActionEvent ¢) {
		radioButtonContainerBarcodeOperations.selectRadioButton((RadioButton) ¢.getSource());
		enableRunTheOperationButton();
	}

	@FXML
	private void runTheOperationButtonPressed(ActionEvent __) {

		SmartCode smartcode = new SmartCode(Long.parseLong(barcodeTextField.getText()), datePicker.getValue());
		int amountVal = editPackagesAmountSpinner.getValue();
		log.info("===============================runTheOperationButtonPressed======================================");
		log.info("amount in spinner: " + amountVal);

		try {
			if (barcodeOperationsPane.isVisible()) {
				log.info("barcode pane is visible");
				if (addPakageToWarhouseRadioButton.isSelected()) {
					log.info("addPakageToWarhouseRadioButton");
					// init
					Location loc = new Location(0, 0, PlaceInMarket.WAREHOUSE);
					ProductPackage pp = new ProductPackage(smartcode, amountVal, loc);

					// exec
					worker.addProductToWarehouse(pp);

					// printToSuccessLog(("Added (" + amountVal + ") " +
					// "product
					// packages (" + pp + ") to warehouse"));
					this.expirationDate = datePicker.getValue();
					searchCodeButtonPressed(null);
				}
			} else {

				if (addPackageToStoreRadioButton.isSelected()) {
					log.info("addPackageToStoreRadioButton");
					// init
					Location loc = new Location(0, 0, PlaceInMarket.STORE);
					ProductPackage pp = new ProductPackage(smartcode, amountVal, loc);

					// exec
					worker.placeProductPackageOnShelves(pp);

					// printToSuccessLog(("Added (" + amountVal + ") " +
					// "product
					// packages (" + pp + ") to store"));

					searchCodeButtonPressed(null);

				} else if (removePackageFromStoreRadioButton.isSelected()) {
					log.info("removePackageFromStoreRadioButton");
					// init
					Location loc = new Location(0, 0, PlaceInMarket.STORE);
					ProductPackage pp = new ProductPackage(smartcode, amountVal, loc);

					// exec
					worker.removeProductPackageFromStore(pp);

					// printToSuccessLog(("Removed (" + amountVal + ") " +
					// "product
					// packages (" + pp + ") from store"));

					searchCodeButtonPressed(null);

				} else if (removePackageFromWarhouseRadioButton.isSelected()) {
					log.info("removePackageFromWarhouseRadioButton");
					Location loc = new Location(0, 0, PlaceInMarket.WAREHOUSE);
					ProductPackage pp = new ProductPackage(smartcode, amountVal, loc);
					worker.removeProductPackageFromStore(pp);
					// printToSuccessLog(("Removed (" + amountVal + ") " +
					// "product
					// packages (" + pp + ") from warehouse"));

					searchCodeButtonPressed(null);

				} else if (printSmartCodeRadioButton.isSelected()) {
					new SmartcodePrint(smartcode, amountVal).print();
				}
			}

		} catch (SMException e) {
			EmployeeGuiExeptionHandler.handle(e);
		}
		log.info("===============================runTheOperationButtonPressed======================================");
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

	// void printToSuccessLog(String msg) {
	// ((TextArea) rootPane.getParent().lookup("#successLogArea"))
	// .appendText(new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss").format(new
	// Date()) + " :: " + msg + "\n");
	// }

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
