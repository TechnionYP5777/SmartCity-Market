package EmployeeGui;

import java.net.URL;
import java.util.ResourceBundle;

import BasicCommonClasses.ProductPackage;
import EmployeeContracts.IManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class ManageCatalogProductTab implements Initializable {

	@FXML
	private RadioButton addCatalogProductRadioButton;

	@FXML
	private RadioButton removeCatalogProductRadioButton;

	@FXML
	private TextField barcodeTextField;

	@FXML
	private GridPane addCatalogProductParamPane;

	@FXML
	private TextField productNameTextField;

	@FXML
	private TextField productDescriptionTextField;

	@FXML
	private TextField productManufacturerTextField;

	@FXML
	private TextField productPriceTextField;

	@FXML
	private TextField productIngredientsTextField;

	@FXML
	private TextField productLocationTextField;

	@FXML
	private Button runTheOperationButton;

	IManager manager;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		barcodeTextField.textProperty().addListener((observable, oldValue, newValue) -> {

		});
		productNameTextField.textProperty().addListener((observable, oldValue, newValue) -> {

		});
		productDescriptionTextField.textProperty().addListener((observable, oldValue, newValue) -> {

		});
		;
		productManufacturerTextField.textProperty().addListener((observable, oldValue, newValue) -> {

		});
		productPriceTextField.textProperty().addListener((observable, oldValue, newValue) -> {

		});
		productIngredientsTextField.textProperty().addListener((observable, oldValue, newValue) -> {

		});
		productLocationTextField.textProperty().addListener((observable, oldValue, newValue) -> {

		});

	}

	@FXML
	void addCatalogProductRadioButtonPressed(ActionEvent event) {

	}

	@FXML
	void removeCatalogProductRadioButtonPressed(ActionEvent event) {

	}

	@FXML
	void runTheOperationButtonPressed(ActionEvent event) {
		if (addCatalogProductRadioButton.isSelected()) {
			
//			ProductPackage productpackage = new ProductPackage(smartCode, amount, location)
		
		} else if (removeCatalogProductRadioButton.isSelected()) {

		}
	}

}
