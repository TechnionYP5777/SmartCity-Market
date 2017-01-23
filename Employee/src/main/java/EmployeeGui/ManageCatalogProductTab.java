package EmployeeGui;

import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.ResourceBundle;

import com.google.common.eventbus.Subscribe;

import BasicCommonClasses.CatalogProduct;
import BasicCommonClasses.Ingredient;
import BasicCommonClasses.Location;
import BasicCommonClasses.Manufacturer;
import BasicCommonClasses.SmartCode;
import EmployeeContracts.IManager;
import EmployeeImplementations.Manager;
import GuiUtils.RadioButtonEnabler;
import SMExceptions.SMException;
import UtilsContracts.BarcodeScanEvent;
import UtilsContracts.IBarcodeEventHandler;
import UtilsImplementations.BarcodeEventHandler;
import UtilsImplementations.InjectionFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

/**
 * ManageCatalogProductTab - This class is the controller for the Manage catalog
 * products Tab all action of this tab should be here.
 * 
 * @author Shimon Azulay
 */
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
	private ComboBox<String> productManufacturerCombo;

	@FXML
	private TextField productPriceTextField;

	@FXML
	private TextField productIngredientsTextField;

	@FXML
	private TextField productLocationTextField;

	@FXML
	private Button runTheOperationButton;

	IManager manager = InjectionFactory.getInstance(Manager.class);

	IBarcodeEventHandler barcodeEventHandler = InjectionFactory.getInstance(BarcodeEventHandler.class);

	RadioButtonEnabler radioButtonContainerManageCatalogProduct = new RadioButtonEnabler();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		barcodeEventHandler.register(this);
		barcodeTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.matches("\\d*")) {
				barcodeTextField.setText(newValue.replaceAll("[^\\d]", ""));
			}
			enableRunOperation();
		});
		productNameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			enableRunOperation();
		});
		productDescriptionTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			enableRunOperation();
		});
		;
		productManufacturerCombo.getItems().addAll("תנובה", "מאפיות ברמן", "עלית", "אסם", "בייגל-בייגל");
		productManufacturerCombo.setValue("תנובה");
		productPriceTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.matches("((\\d*)|(\\d+\\.\\d*))")) {
				productPriceTextField.setText(oldValue);
			}
			enableRunOperation();
		});
		productIngredientsTextField.textProperty().addListener((observable, oldValue, newValue) -> {

		});
		productLocationTextField.textProperty().addListener((observable, oldValue, newValue) -> {

		});

		radioButtonContainerManageCatalogProduct.addRadioButtons(
				(Arrays.asList((new RadioButton[] { addCatalogProductRadioButton, removeCatalogProductRadioButton }))));

		enableRunOperation();

	}

	private void enableRunOperation() {
		runTheOperationButton
				.setDisable(barcodeTextField.getText().isEmpty() || (addCatalogProductRadioButton.isSelected()
						&& (productNameTextField.getText().isEmpty() || productPriceTextField.getText().isEmpty())));
	}

	@FXML
	void addCatalogProductRadioButtonPressed(ActionEvent event) {
		radioButtonContainerManageCatalogProduct.selectRadioButton(addCatalogProductRadioButton);
		addCatalogProductParamPane.setVisible(true);

	}

	@FXML
	void removeCatalogProductRadioButtonPressed(ActionEvent event) {
		radioButtonContainerManageCatalogProduct.selectRadioButton(removeCatalogProductRadioButton);
		addCatalogProductParamPane.setVisible(false);
	}

	@FXML
	void runTheOperationButtonPressed(ActionEvent event) {

		try {
			if (addCatalogProductRadioButton.isSelected()) {

				Manufacturer manufacturer = getManufacturer();

				CatalogProduct catalogProduct = new CatalogProduct(Long.parseLong(barcodeTextField.getText()),
						productNameTextField.getText(), new HashSet<Ingredient>(), manufacturer,
						productDescriptionTextField.getText().isEmpty() ? "N/A" : productDescriptionTextField.getText(),
						Double.parseDouble(productPriceTextField.getText()), "", new HashSet<Location>());

				manager.addProductToCatalog(catalogProduct);

			} else if (removeCatalogProductRadioButton.isSelected()) {

				manager.removeProductFromCatalog(new SmartCode(Long.parseLong(barcodeTextField.getText()), null));
			}

		} catch (SMException e) {
			EmployeeGuiExeptionHandler.handle(e);
		}
	}

	private Manufacturer getManufacturer() {
		Manufacturer manufacturer = null;
		String man = productManufacturerCombo.getValue();

		switch (man) {
		case "תנובה":
			manufacturer = new Manufacturer(1, man);
			break;

		case "מאפיות ברמן":
			manufacturer = new Manufacturer(2, man);
			break;

		case "עלית":
			manufacturer = new Manufacturer(3, man);
			break;

		case "אסם":
			manufacturer = new Manufacturer(4, man);
			break;

		case "בייגל-בייגל":
			manufacturer = new Manufacturer(5, man);
			break;
		}
		return manufacturer;
	}

	@Subscribe
	public void barcodeScanned(BarcodeScanEvent ¢) {
		barcodeTextField.setText(Long.toString(¢.getBarcode()));

	}

}
