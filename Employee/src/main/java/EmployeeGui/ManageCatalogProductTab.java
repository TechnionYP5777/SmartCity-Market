package EmployeeGui;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.ResourceBundle;

import com.google.common.eventbus.Subscribe;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;

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
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

/**
 * ManageCatalogProductTab - This class is the controller for the Manage catalog
 * products Tab all action of this tab should be here.
 * 
 * @author Shimon Azulay
 */
public class ManageCatalogProductTab implements Initializable {

	@FXML
	private VBox rootPane;
	
	@FXML
	private JFXRadioButton addCatalogProductRadioButton;

	@FXML
	private JFXRadioButton removeCatalogProductRadioButton;

	@FXML
	private JFXTextField barcodeTextField;

	@FXML
	private GridPane addCatalogProductParamPane;

	@FXML
	private JFXTextField productNameTextField;

	@FXML
	private JFXTextField productDescriptionTextField;

	@FXML
	private ComboBox<String> productManufacturerCombo;

	@FXML
	private JFXTextField productPriceTextField;

	@FXML
	private JFXTextField productIngredientsTextField;

	@FXML
	private JFXTextField productLocationTextField;

	@FXML
	private Button runTheOperationButton;

	IManager manager = InjectionFactory.getInstance(Manager.class);

	IBarcodeEventHandler barcodeEventHandler = InjectionFactory.getInstance(BarcodeEventHandler.class);

	RadioButtonEnabler radioButtonContainerManageCatalogProduct = new RadioButtonEnabler();

	@Override
	public void initialize(URL location, ResourceBundle __) {
		barcodeEventHandler.register(this);
		barcodeTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.matches("\\d*"))
				barcodeTextField.setText(newValue.replaceAll("[^\\d]", ""));
			enableRunOperation();
		});
		productNameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			enableRunOperation();
		});
		productDescriptionTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			enableRunOperation();
		});
		productManufacturerCombo.getItems().addAll("תנובה", "מאפיות ברמן", "עלית", "אסם", "בייגל-בייגל");
		productManufacturerCombo.setValue("תנובה");
		productPriceTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.matches("((\\d*)|(\\d+\\.\\d*))"))
				productPriceTextField.setText(oldValue);
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
	void addCatalogProductRadioButtonPressed(ActionEvent __) {
		radioButtonContainerManageCatalogProduct.selectRadioButton(addCatalogProductRadioButton);
		addCatalogProductParamPane.setVisible(true);
		enableRunOperation();

	}

	@FXML
	void removeCatalogProductRadioButtonPressed(ActionEvent __) {
		radioButtonContainerManageCatalogProduct.selectRadioButton(removeCatalogProductRadioButton);
		addCatalogProductParamPane.setVisible(false);
		enableRunOperation();
	}

	@FXML
	void runTheOperationButtonPressed(ActionEvent __) {

		try {
			if (addCatalogProductRadioButton.isSelected()) {
				manager.addProductToCatalog((new CatalogProduct(Long.parseLong(barcodeTextField.getText()),
						productNameTextField.getText(), new HashSet<Ingredient>(), getManufacturer(),
						productDescriptionTextField.getText().isEmpty() ? "N/A" : productDescriptionTextField.getText(),
						Double.parseDouble(productPriceTextField.getText()), "", new HashSet<Location>())));
			
				printToSuccessLog(("Added new product '" + productNameTextField.getText() + "' to catalog"));	
			} else if (removeCatalogProductRadioButton.isSelected()) {
				manager.removeProductFromCatalog(new SmartCode(Long.parseLong(barcodeTextField.getText()), null));
				
				printToSuccessLog(("Remove product '" + productNameTextField.getText() + "' from catalog"));	
			}
		} catch (SMException e) {
			EmployeeGuiExeptionHandler.handle(e);
		} 
	}

	private Manufacturer getManufacturer() {
		Manufacturer $ = null;
		String man = productManufacturerCombo.getValue();

		switch (man) {
		case "תנובה":
			$ = new Manufacturer(1, man);
			break;

		case "מאפיות ברמן":
			$ = new Manufacturer(2, man);
			break;

		case "עלית":
			$ = new Manufacturer(3, man);
			break;

		case "אסם":
			$ = new Manufacturer(4, man);
			break;

		case "בייגל-בייגל":
			$ = new Manufacturer(5, man);
			break;
		}
		return $;
	}

	private void printToSuccessLog(String msg) {
		((TextArea) rootPane.getScene().lookup("#successLogArea"))
				.appendText(new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss").format(new Date()) + " :: " + msg + "\n");
	}
	
	@Subscribe
	public void barcodeScanned(BarcodeScanEvent ¢) {
		barcodeTextField.setText(Long.toString(¢.getBarcode()));

	}

}
