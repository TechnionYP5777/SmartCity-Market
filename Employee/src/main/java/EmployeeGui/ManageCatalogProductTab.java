package EmployeeGui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

import com.google.common.eventbus.Subscribe;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXPopup.PopupHPosition;
import com.jfoenix.controls.JFXPopup.PopupVPosition;
import com.jfoenix.validation.RequiredFieldValidator;

import BasicCommonClasses.CatalogProduct;
import BasicCommonClasses.Ingredient;
import BasicCommonClasses.Location;
import BasicCommonClasses.Manufacturer;
import BasicCommonClasses.PlaceInMarket;
import BasicCommonClasses.SmartCode;
import EmployeeContracts.IManager;
import EmployeeDefs.AEmployeeException.ConnectionFailure;
import SMExceptions.CommonExceptions.CriticalError;
import EmployeeDefs.AEmployeeException.EmployeeNotConnected;
import EmployeeDefs.AEmployeeException.InvalidParameter;
import EmployeeGuiContracts.CatalogProductEvent;
import EmployeeGuiContracts.IngredientEvent;
import EmployeeGuiContracts.ManufacturerEvent;
import EmployeeImplementations.Manager;
import GuiUtils.DialogMessagesService;
import GuiUtils.RadioButtonEnabler;
import SMExceptions.SMException;
import UtilsContracts.BarcodeScanEvent;
import UtilsContracts.IBarcodeEventHandler;
import UtilsContracts.IConfiramtionDialog;
import UtilsContracts.IEventBus;
import UtilsImplementations.BarcodeEventHandler;
import UtilsImplementations.InjectionFactory;
import UtilsImplementations.ProjectEventBus;
import UtilsImplementations.StackTraceUtil;
import de.jensd.fx.glyphs.GlyphsBuilder;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
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
	private JFXButton ingrChooser;

	@FXML
	private JFXTextField barcodeTextField;

	@FXML
	private GridPane addCatalogProductParamPane;

	@FXML
	private JFXTextField productNameTextField;

	@FXML
	private JFXTextField productDescriptionTextField;

	@FXML
	private JFXComboBox<String> productManufacturerCombo;
	
	@FXML 
	private JFXButton locationChooser;

	JFXListView<String> ingredientList;

	JFXPopup popupIngr;

	ObservableList<String> dataIngr;

	FilteredList<String> filteredDataIngr;

	HashSet<String> selectedIngr = new HashSet<String>();

	@FXML
	private JFXTextField productPriceTextField;
	
	@FXML
	private Label locationLbl;

	// @FXML
	// private JFXTextField productLocationTextField;

	private HashMap<String, Manufacturer> manufacturars;

	private HashMap<String, Ingredient> ingredients;

	@FXML
	private JFXButton runTheOperationButton;
	
	JFXPopup popupLocation;

	IManager manager = InjectionFactory.getInstance(Manager.class);

	IBarcodeEventHandler barcodeEventHandler = InjectionFactory.getInstance(BarcodeEventHandler.class);

	RadioButtonEnabler radioButtonContainerManageCatalogProduct = new RadioButtonEnabler();

	private String addProductTxt = "Add Product";

	private String removeProductTxt = "Remove Product";

	IEventBus eventBus;

	private Location chosenLocation;

	@Override
	public void initialize(URL location, ResourceBundle __) {
		eventBus = InjectionFactory.getInstance(ProjectEventBus.class);
		eventBus.register(this);
		barcodeEventHandler.register(this);
		barcodeTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.matches("\\d*"))
				barcodeTextField.setText(newValue.replaceAll("[^\\d]", ""));
			enableRunOperation();
		});
		productNameTextField.textProperty().addListener((observable, oldValue, newValue) -> enableRunOperation());
		productDescriptionTextField.textProperty()
				.addListener((observable, oldValue, newValue) -> enableRunOperation());

		createManufacturerMap();
		productManufacturerCombo.getItems().addAll(manufacturars.keySet());

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

		Label lbl = new Label("Choose Ingredients");
		JFXTextField searchIngr = new JFXTextField();
		searchIngr.setPromptText("Search Ingredient");
		JFXButton done = new JFXButton("Done!");
		searchIngr.getStyleClass().add("JFXTextField");
		done.getStyleClass().add("JFXButton");
		done.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent __) {
				popupIngr.hide();
			}
		});

		ingredientList = new JFXListView<String>();
		ingredientList.setMaxHeight(100);

		HBox manubtnContanier = new HBox();
		manubtnContanier.getChildren().addAll(done, searchIngr);
		manubtnContanier.setSpacing(10);
		manubtnContanier.setAlignment(Pos.CENTER);
		VBox manuContainer = new VBox();
		manuContainer.getChildren().addAll(lbl, ingredientList, manubtnContanier);
		manuContainer.setPadding(new Insets(10, 50, 50, 50));
		manuContainer.setSpacing(10);

		popupIngr = new JFXPopup(manuContainer);
		ingrChooser.setOnMouseClicked(e -> popupIngr.show(ingrChooser, PopupVPosition.TOP, PopupHPosition.LEFT));

		ingredientList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		// for multiple selection
		ingredientList.addEventFilter(MouseEvent.MOUSE_PRESSED, evt -> {
			Node node = evt.getPickResult().getIntersectedNode();
			while (node != null && node != ingredientList && !(node instanceof ListCell))
				node = node.getParent();
			if (node instanceof ListCell) {
				evt.consume();

				ListCell<?> cell = (ListCell<?>) node;
				ListView<?> lv = cell.getListView();

				lv.requestFocus();

				if (!cell.isEmpty()) {
					int index = cell.getIndex();
					if (!cell.isSelected())
						lv.getSelectionModel().select(index);
					else
						lv.getSelectionModel().clearSelection(index);
				}

				ObservableList<String> selectedItems = ingredientList.getSelectionModel().getSelectedItems();

				selectedIngr.clear();
				selectedIngr.addAll(selectedItems);

			}
		});

		ingredientList.setDepth(1);
		ingredientList.setExpanded(true);

		createIngredientList();

		// productLocationTextField.textProperty().addListener((observable,
		// oldValue, newValue) -> {
		// });
		
		Label lbl1 = new Label("Choose Location");
		JFXButton close = new JFXButton("Close");
		close.getStyleClass().add("JFXTextField");
		close.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent __) {
				popupLocation.hide();
			}
		});
		ImageView locationMap = new ImageView("/ManageCatalogProductTab/storeMap.png");
		locationMap.setFitHeight(400);
		locationMap.setFitWidth(400);

		VBox locationContainer = new VBox();
		locationContainer.getChildren().addAll(lbl1, locationMap, close);
		locationContainer.setPadding(new Insets(10, 10, 10, 10));
		locationContainer.setSpacing(10);

		locationMap.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
			int xLocation = (int) e.getX(), yLocation = (int) e.getY();
			(new createStoreMapImageWithDot(xLocation, yLocation)).start();
			chosenLocation = new Location(xLocation, yLocation, PlaceInMarket.STORE);
			locationLbl.setText("(col=" + xLocation + ", row=" + yLocation + ")");
		});

		popupLocation = new JFXPopup(locationContainer);
		popupLocation.setOnShowing(e -> rootPane.getScene().setCursor(Cursor.CROSSHAIR));
		popupLocation.setOnHidden(e -> rootPane.getScene().setCursor(Cursor.DEFAULT));
		locationChooser.setOnMouseClicked(e -> popupLocation.show(locationChooser, PopupVPosition.BOTTOM, PopupHPosition.LEFT));
		

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

		changeRunOprBtnTxt();

		enableRunOperation();
	}
	
	public class createStoreMapImageWithDot extends Thread {
		int x;
		int y;
		static final int radius = 10;
		
		createStoreMapImageWithDot(int x, int y){
			this.x=x;
			this.y=y;
		}
		
		@Override
		public void run(){
			BufferedImage img = null;
			try {
			    img = ImageIO.read(new File(getClass().getResource("/ManageCatalogProductTab/storeMap.png").toURI()));
			} catch (IOException e) {
				log.error("ERROR while trying to read the storeMap pic");
				log.error(e + "");
				return;
			} catch (URISyntaxException e) {
				log.error("ERROR URL error");
				log.error(e + "");
			}
			for (int hieght = img.getHeight(), width = img.getWidth(), radius = 10, i = 0; i < width; ++i)
				for (int j = 0; j < hieght; ++j)
					if (Math.abs(x - i) <= radius && Math.abs(y - j) <= radius)
						img.setRGB(i, j, 0xFF0000);
			try {
				ImageIO.write(img, "jpg", new File("storeMapWithDot.jpg"));
			} catch (IOException e) {
				log.error("ERROR while trying to create the sotoreMapWithDot pic");
				log.error(e + "");
			}
//			} catch (URISyntaxException e) {
//				log.error("ERROR URL error");
//				log.error(e + "");
//			}
		}
	}

	private void createIngredientList() {
		ingredients = new HashMap<String, Ingredient>();
		try {
			manager.getAllIngredients().forEach(ingredient -> ingredients.put(ingredient.getName(), ingredient));
		} catch (InvalidParameter | CriticalError | ConnectionFailure e) {
			log.fatal(e);
			log.debug(StackTraceUtil.stackTraceToStr(e));
			e.showInfoToUser();
		}

		dataIngr = FXCollections.observableArrayList();

		dataIngr.setAll(ingredients.keySet());

		filteredDataIngr = new FilteredList<>(dataIngr, s -> true);

		ingredientList.setItems(filteredDataIngr);

		selectedIngr.clear();
	}

	private void changeRunOprBtnTxt() {
		runTheOperationButton.setText(addCatalogProductRadioButton.isSelected() ? addProductTxt : removeProductTxt);
	}

	private void enableRunOperation() {
		runTheOperationButton
				.setDisable(barcodeTextField.getText().isEmpty() || (addCatalogProductRadioButton.isSelected()
						&& (productNameTextField.getText().isEmpty() || productPriceTextField.getText().isEmpty())));
	}

	private void createManufacturerMap() {
		manufacturars = new HashMap<String, Manufacturer>();

		try {
			manager.getAllManufacturers()
					.forEach(manufacturer -> manufacturars.put(manufacturer.getName(), manufacturer));
		} catch (InvalidParameter | CriticalError | EmployeeNotConnected | ConnectionFailure e) {
			log.fatal(e);
			log.debug(StackTraceUtil.stackTraceToStr(e));
			e.showInfoToUser();
		}

	}

	@FXML
	void addCatalogProductRadioButtonPressed(ActionEvent __) {
		radioButtonContainerManageCatalogProduct.selectRadioButton(addCatalogProductRadioButton);
		addCatalogProductParamPane.setVisible(true);
		enableRunOperation();
		changeRunOprBtnTxt();

	}

	@FXML
	void removeCatalogProductRadioButtonPressed(ActionEvent __) {
		radioButtonContainerManageCatalogProduct.selectRadioButton(removeCatalogProductRadioButton);
		addCatalogProductParamPane.setVisible(false);
		enableRunOperation();
		changeRunOprBtnTxt();
	}

	@FXML
	void runTheOperationButtonPressed(ActionEvent __) {

		try {
			if (!addCatalogProductRadioButton.isSelected()) {
				if (removeCatalogProductRadioButton.isSelected())
					DialogMessagesService.showConfirmationDialog("Remove Catalog Product", null, "Are You Sure?",
							new removeProductHandler());
			} else {
				HashSet<Location> locationSet = new HashSet<>();
				if (chosenLocation != null)
					locationSet.add(chosenLocation);
				manager.addProductToCatalog(
						new CatalogProduct(Long.parseLong(barcodeTextField.getText()), productNameTextField.getText(),
								getSelectedIngr(), manufacturars.get(productManufacturerCombo.getValue()),
								productDescriptionTextField.getText().isEmpty() ? "N/A"
										: productDescriptionTextField.getText(),
								Double.parseDouble(productPriceTextField.getText()), "", locationSet));
				printToSuccessLog("Added new product '" + productNameTextField.getText() + "' to catalog");
				cleanFields();
			}

		} catch (SMException e) {
			log.fatal(e);
			log.debug(StackTraceUtil.stackTraceToStr(e));
			e.showInfoToUser();
		}
	}

	HashSet<Ingredient> tempIngr;

	private HashSet<Ingredient> getSelectedIngr() {
		tempIngr = new HashSet<Ingredient>();
		selectedIngr.forEach(ingr -> tempIngr.add(ingredients.get(ingr)));
		return tempIngr;
	}

	void removeProductHandle() {
		try {

			manager.removeProductFromCatalog(new SmartCode(Long.parseLong(barcodeTextField.getText()), null));
			eventBus.post(new CatalogProductEvent());
			printToSuccessLog("Remove product '" + productNameTextField.getText() + "' from catalog");

			cleanFields();

		} catch (SMException e) {
			log.fatal(e);
			log.debug(StackTraceUtil.stackTraceToStr(e));
			e.showInfoToUser();
		}
	}

	class removeProductHandler implements IConfiramtionDialog {

		@Override
		public void onYes() {
			removeProductHandle();
		}

		@Override
		public void onNo() {
		}

	}

	private void cleanFields() {
		barcodeTextField.setText("");
		productDescriptionTextField.setText("");
		productNameTextField.setText("");
		productPriceTextField.setText("");
		productManufacturerCombo.getSelectionModel().clearSelection();
		// TODO
		// Clear the ingredients
	}

	private void printToSuccessLog(String msg) {
		((TextArea) rootPane.getScene().lookup("#successLogArea"))
				.appendText(new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss").format(new Date()) + " :: " + msg + "\n");
	}

	@Subscribe
	public void barcodeScanned(BarcodeScanEvent ¢) {
		barcodeTextField.setText(Long.toString(¢.getBarcode()));
	}

	@Subscribe
	public void onIngredientEvent(IngredientEvent __) {
		createIngredientList();
		enableRunOperation();
	}

	@Subscribe
	public void onManufacturerEvent(ManufacturerEvent __) {
		createManufacturerMap();
		productManufacturerCombo.getItems().clear();
		productManufacturerCombo.getItems().addAll(manufacturars.keySet());
		enableRunOperation();
	}

}
