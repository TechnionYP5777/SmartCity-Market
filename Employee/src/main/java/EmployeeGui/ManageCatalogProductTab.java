package EmployeeGui;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.controlsfx.control.CheckComboBox;

import com.google.common.eventbus.Subscribe;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;

import BasicCommonClasses.CatalogProduct;
import BasicCommonClasses.Ingredient;
import BasicCommonClasses.Location;
import BasicCommonClasses.Manufacturer;
import BasicCommonClasses.SmartCode;
import EmployeeContracts.IManager;
import EmployeeDefs.AEmployeeException.ConnectionFailure;
import EmployeeDefs.AEmployeeException.CriticalError;
import EmployeeDefs.AEmployeeException.EmployeeNotConnected;
import EmployeeDefs.AEmployeeException.InvalidParameter;
import EmployeeImplementations.Manager;
import GuiUtils.RadioButtonEnabler;
import SMExceptions.SMException;
import UtilsContracts.BarcodeScanEvent;
import UtilsContracts.IBarcodeEventHandler;
import UtilsImplementations.BarcodeEventHandler;
import UtilsImplementations.InjectionFactory;
import de.jensd.fx.glyphs.GlyphsBuilder;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
 * @since 2017-01-04
 */
public class ManageCatalogProductTab implements Initializable {
	
	static Logger log = Logger.getLogger(ManageCatalogProductTab.class.getName());

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
	private CheckComboBox<String> ingridientsCombo;

	@FXML
	private JFXTextField productPriceTextField;

	// @FXML
	// private JFXTextField productLocationTextField;

	private HashMap<String, Manufacturer> manufacturars;

	private HashMap<String, Ingredient> ingredients;

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
		productNameTextField.textProperty().addListener((observable, oldValue, newValue) -> enableRunOperation());
		productDescriptionTextField.textProperty().addListener((observable, oldValue, newValue) -> enableRunOperation());

		createManufacturerMap();
		productManufacturerCombo.getItems().addAll( manufacturars.keySet() /*
				"תנובה", "מאפיות ברמן", "עלית", "אסם", "בייגל-בייגל"*/);

		productManufacturerCombo.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(@SuppressWarnings("rawtypes") ObservableValue __, String s, String t1) {
				enableRunOperation();
			}
		});

		productPriceTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.matches("((\\d*)|(\\d+\\.\\d*))"))
				productPriceTextField.setText(oldValue);
			enableRunOperation();
		});

		createIngredientMap();
		ingridientsCombo.getItems().addAll(ingredients.keySet()/* "תנובה", "מאפיות ברמן", "עלית", "אסם",
				"בייגל-בייגל"*/);

		// productLocationTextField.textProperty().addListener((observable,
		// oldValue, newValue) -> {
		// });
		radioButtonContainerManageCatalogProduct.addRadioButtons(
				Arrays.asList(new RadioButton[] { addCatalogProductRadioButton, removeCatalogProductRadioButton }));

		RequiredFieldValidator validator = new RequiredFieldValidator();
		validator.setMessage("Input Required");
		validator.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class).glyph(FontAwesomeIcon.WARNING).size("1em")
				.styleClass("error").build());
		
		barcodeTextField.getValidators().add(validator);
		barcodeTextField.focusedProperty().addListener((o, oldVal, newVal) -> {
			if (!newVal)
				barcodeTextField.validate();
		});
		
		RequiredFieldValidator validator2 = new RequiredFieldValidator();
		validator2.setMessage("Input Required");
		validator2.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class).glyph(FontAwesomeIcon.WARNING).size("1em")
				.styleClass("error").build());
		
		productNameTextField.getValidators().add(validator2);
		productNameTextField.focusedProperty().addListener((o, oldVal, newVal) -> {
			if (!newVal)
				productNameTextField.validate();
		});
		
		RequiredFieldValidator validator3 = new RequiredFieldValidator();
		validator3.setMessage("Input Required");
		validator3.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class).glyph(FontAwesomeIcon.WARNING).size("1em")
				.styleClass("error").build());
		
		productPriceTextField.getValidators().add(validator3);
		productPriceTextField.focusedProperty().addListener((o, oldVal, newVal) -> {
			if (!newVal)
				productPriceTextField.validate();
		});
		
		

		enableRunOperation();
	}

	private void enableRunOperation() {
		runTheOperationButton
				.setDisable(barcodeTextField.getText().isEmpty() || (addCatalogProductRadioButton.isSelected()
						&& (productNameTextField.getText().isEmpty() || productPriceTextField.getText().isEmpty())));
	}

	private void createManufacturerMap() {

		manufacturars = new HashMap<String, Manufacturer>();

		try {
			manager.getAllManufacturers().forEach(manufacturer -> manufacturars.put(manufacturer.getName(), manufacturer));
		} catch (InvalidParameter | CriticalError | EmployeeNotConnected | ConnectionFailure e) {
			log.fatal(e.getMessage());
		}

	}

	private void createIngredientMap() {
		ingredients = new HashMap<String, Ingredient>();
		try {
			manager.getAllIngredients().forEach(ingredient -> ingredients.put(ingredient.getName(), ingredient));
		} catch (InvalidParameter | CriticalError | EmployeeNotConnected | ConnectionFailure e) {
			log.fatal(e.getMessage());
		}
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
				manager.addProductToCatalog(new CatalogProduct(Long.parseLong(barcodeTextField.getText()), productNameTextField.getText(),
						new HashSet<Ingredient>(), getManufacturer(),
						productDescriptionTextField.getText().isEmpty() ? "N/A" : productDescriptionTextField.getText(),
						Double.parseDouble(productPriceTextField.getText()), "", new HashSet<Location>()));

				printToSuccessLog("Added new product '" + productNameTextField.getText() + "' to catalog");
			} else if (removeCatalogProductRadioButton.isSelected()) {
				manager.removeProductFromCatalog(new SmartCode(Long.parseLong(barcodeTextField.getText()), null));

				printToSuccessLog("Remove product '" + productNameTextField.getText() + "' from catalog");
			}
		} catch (SMException e) {
			e.showInfoToUser();
		}
	}

	private Manufacturer getManufacturer() {
		Manufacturer $ = null;
		String man = productManufacturerCombo.getValue();

		switch (man) {
		case "אסם":
			$ = new Manufacturer(4, man);
			break;
		case "בייגל-בייגל":
			$ = new Manufacturer(5, man);
			break;
		case "מאפיות ברמן":
			$ = new Manufacturer(2, man);
			break;
		case "עלית":
			$ = new Manufacturer(3, man);
			break;
		case "תנובה":
			$ = new Manufacturer(1, man);
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
