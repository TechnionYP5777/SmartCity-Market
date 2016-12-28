package EmployeeGui;

import java.net.URL;
import java.util.ResourceBundle;

import BasicCommonClasses.CatalogProduct;
import EmployeeContracts.IWorker;
import EmployeeDefs.AEmployeeExceptions.CriticalError;
import EmployeeDefs.AEmployeeExceptions.EmployeeNotConnected;
import EmployeeDefs.AEmployeeExceptions.InvalidParameter;
import EmployeeDefs.AEmployeeExceptions.ProductNotExistInCatalog;
import EmployeeDefs.AEmployeeExceptions.UnknownSenderID;
import EmployeeDefs.EmployeeGuiDefs;
import GuiUtils.AbstractApplicationScreen;
import GuiUtils.DialogMessagesService;
import GuiUtils.StackPaneService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
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

	@FXML
	GridPane workerMenuScreenPane;

	@FXML
	Button viewCatalogProductButton;

	@FXML
	StackPane stackPane;

	@FXML
	TextField barcodeTextField;

	String barcode = "";

	@FXML
	Button searchBarcode;

	@FXML
	VBox viewCatalogProductPane;

	@FXML
	VBox addProductPackagePane;

	IWorker worker;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		AbstractApplicationScreen.fadeTransition(workerMenuScreenPane);
		barcodeTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			barcode = newValue;
			enableLoginButtonCheck();
		});

	}

	private void enableLoginButtonCheck() {
		searchBarcode.setDisable(barcode.isEmpty());
	}

	@FXML
	private void viewCatalogProductButtonPressed(ActionEvent e) {
		StackPaneService.bringToFront(stackPane, viewCatalogProductPane.getId());
	}

	@FXML
	private void addProductPackageButtonPressed(ActionEvent e) {
		StackPaneService.bringToFront(stackPane, addProductPackagePane.getId());
	}

	@FXML
	private void searchBarcodePressed(ActionEvent event) {
		CatalogProduct catalogProduct = null;
		try {
			catalogProduct = worker.viewProductFromCatalog(Integer.parseInt(barcode));
		} catch (NumberFormatException e) {
			// TODO
			e.printStackTrace();
		} catch (InvalidParameter e) {
			// TODO
			e.printStackTrace();
		} catch (UnknownSenderID e) {
			// TODO
			e.printStackTrace();
		} catch (CriticalError e) {
			// TODO
			e.printStackTrace();
		} catch (EmployeeNotConnected e) {
			// TODO
			e.printStackTrace();
		} catch (ProductNotExistInCatalog e) {
			DialogMessagesService.showErrorDialog(EmployeeGuiDefs.viewProductFailed, null,
					EmployeeGuiDefs.productNotExistsInCatalog);
		}
		if (catalogProduct == null) {
			return;
		}
		DialogMessagesService.showInfoDialog(catalogProduct.getName(),
				"Description: " + catalogProduct.getDescription(),
				"Barcode: " + catalogProduct.getBarcode() + "\n" + "Manufacturer: "
						+ catalogProduct.getManufacturer().getName() + "\n" + "Price: " + catalogProduct.getPrice());

	}

}
