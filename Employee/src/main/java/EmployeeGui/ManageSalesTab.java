package EmployeeGui;

import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.google.common.eventbus.Subscribe;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;

import BasicCommonClasses.CatalogProduct;
import BasicCommonClasses.Sale;
import EmployeeContracts.IManager;
import EmployeeDefs.AEmployeeException.ConnectionFailure;
import EmployeeDefs.AEmployeeException.EmployeeNotConnected;
import EmployeeDefs.AEmployeeException.InvalidParameter;
import EmployeeDefs.AEmployeeException.ParamIDDoesNotExist;
import EmployeeDefs.AEmployeeException.ParamIDStillInUse;
import EmployeeDefs.AEmployeeException.ProductNotExistInCatalog;
import EmployeeImplementations.Manager;
import GuiUtils.DialogMessagesService;
import SMExceptions.CommonExceptions.CriticalError;
import SMExceptions.SMException;
import UtilsContracts.BarcodeScanEvent;
import UtilsContracts.IConfiramtionDialog;
import UtilsContracts.IEventBus;
import UtilsImplementations.InjectionFactory;
import UtilsImplementations.ProjectEventBus;
import UtilsImplementations.StackTraceUtil;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ManageSalesTab implements Initializable {

	@FXML
	private JFXTextField barcodeField;

	@FXML
	private VBox scannedProductPane;

	@FXML
	private VBox chooseSalePane;

	@FXML
	private HBox discountPane;
	
	@FXML
	private ImageView searchIcon;

	@FXML
	private Label productNamelbl;
	
	@FXML
	private Label barcodeLbl;

	@FXML
	private VBox minBuyPane;

	@FXML
	private JFXButton addProductSale;

	@FXML
	private JFXListView<String> singleList;

	@FXML
	private JFXButton removeSingleBtn;

	@FXML
	private JFXTextField filterSingle;

	@FXML
	private JFXTextField amount;

	@FXML
	private JFXTextField price;
	
	private HashSet<String> selectedSingle = new HashSet<String>();

	private FilteredList<String> filteredDataSingle;

	private ObservableList<String> dataSingle;

	private HashMap<String, Integer> singles;

	IManager manager;

	IEventBus eventBus;

	CatalogProduct currentCatalogProduct = null;

	static Logger log = Logger.getLogger(ManageSalesTab.class.getName());

	@FXML
	void removeSinglePress(ActionEvent event) {
		DialogMessagesService.showConfirmationDialog("Remove Sale", null, "Are You Sure?", new removeSingleHandler());
	}
	
	@FXML
	private void addProductSalePressed(ActionEvent event) {
	
			try {
				manager.createNewSale(new Sale(-1, currentCatalogProduct.getBarcode(), Integer.parseInt(amount.getText()),Double.parseDouble(price.getText())));
			} catch (InvalidParameter | CriticalError | EmployeeNotConnected | ConnectionFailure
					| NumberFormatException e) {
			
				log.fatal(e);
				log.debug(StackTraceUtil.stackTraceToStr(e));
				// TODO
				//e.showInfoToUser();
			
			}
	
		createSingleList();
		enableAddSaleButton();
		enableRemoveButtons();
		clearData();
	}

	@FXML
	void searchClicked(MouseEvent event) {
		showProductDetails();
		enableProductDetailAndSale(true);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		eventBus = InjectionFactory.getInstance(ProjectEventBus.class);
		eventBus.register(this);
		
		barcodeField.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.matches("\\d*")) 
				barcodeField.setText(newValue.replaceAll("[^\\d]", ""));
			enableProductDetailAndSale(false);
			clearData();
			enableAddSaleButton();
			searchIcon.setDisable(barcodeField.getText().isEmpty());
		});

		manager = InjectionFactory.getInstance(Manager.class);

		filterSingle.textProperty().addListener(obs -> {
			String filter = filterSingle.getText();
			filteredDataSingle
					.setPredicate(filter == null || filter.length() == 0 ? s -> true : s -> s.contains(filter));
		});

		singleList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		// for multiple selection
		singleList.addEventFilter(MouseEvent.MOUSE_PRESSED, evt -> {
			Node node = evt.getPickResult().getIntersectedNode();
			while (node != null && node != singleList && !(node instanceof ListCell)) {
				node = node.getParent();
			}
			if (node instanceof ListCell) {
				evt.consume();
				ListCell<?> cell = (ListCell<?>) node;
				ListView<?> lv = cell.getListView();
				lv.requestFocus();
				if (!cell.isEmpty()) {
					int index = cell.getIndex();
					if (cell.isSelected()) {
						lv.getSelectionModel().clearSelection(index);
					} else {
						lv.getSelectionModel().select(index);
					}
				}

				ObservableList<String> selectedItems = singleList.getSelectionModel().getSelectedItems();

				selectedSingle.clear();
				selectedSingle.addAll(selectedItems);
				enableRemoveButtons();

			}
		});

		singleList.setDepth(1);
		singleList.setExpanded(true);

		createSingleList();

		amount.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.matches("[1-9]\\d*") && !newValue.isEmpty())
				amount.setText(oldValue);
			enableAddSaleButton();
		});

		price.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.matches("((\\d*)|(\\d+\\.\\d*))"))
				price.setText(oldValue);
			enableAddSaleButton();
		});

		enableAddSaleButton();
		enableRemoveButtons();
		searchIcon.setDisable(barcodeField.getText().isEmpty());
		enableProductDetailAndSale(false);
	}
	
	private void enableProductDetailAndSale(boolean enable) {
		amount.setDisable(!enable);
		price.setDisable(!enable);
	}
	
	private void clearData() {
		currentCatalogProduct = null;
		barcodeField.setText("");
		amount.setText("");
		price.setText("");
		productNamelbl.setText("N/A");
		barcodeLbl.setText("N/A");	
	}

	private void createSingleList() {

		singles = new HashMap<String, Integer>();

		dataSingle = FXCollections.observableArrayList();

		try {
			manager.getAllSales().forEach((val) -> {
				singles.put(generateKey(val), val.getId());
			});
		} catch (InvalidParameter | CriticalError | EmployeeNotConnected | ConnectionFailure e) {
			log.fatal(e);
			log.debug(StackTraceUtil.stackTraceToStr(e));
			e.showInfoToUser();
		}

		dataSingle.addAll(singles.keySet());

		filteredDataSingle = new FilteredList<>(dataSingle, s -> true);

		singleList.setItems(filteredDataSingle);

	}

	private String generateKey(Sale sale) {	
		String ret = "Error with sale id: ";
		try {
			CatalogProduct p = manager.viewProductFromCatalog(sale.getProductBarcode());
			ret = "Product: " + p.getName() + " with barcode: " + p.getBarcode() + " Has " + sale.getSaleAsString();
		} catch (InvalidParameter | CriticalError | EmployeeNotConnected | ProductNotExistInCatalog
				| ConnectionFailure e) {
			log.fatal(e);
			log.debug(StackTraceUtil.stackTraceToStr(e));
			e.showInfoToUser();
			return ret + sale.getId();
		}
		return ret;
	}

	private void enableAddSaleButton() {

		addProductSale.setDisable(currentCatalogProduct == null || amount.getText().isEmpty() || price.getText().isEmpty());
	}

	private void enableRemoveButtons() {
		removeSingleBtn.setDisable(selectedSingle.isEmpty());
	}

	class removeSingleHandler implements IConfiramtionDialog {

		@Override
		public void onYes() {
			removeSingleHandle();
		}

		@Override
		public void onNo() {
		}

	}

	private void removeSingleHandle() {
		selectedSingle.forEach(sale -> {
			try {
				manager.removeSale(singles.get(sale));
			} catch (InvalidParameter | CriticalError | EmployeeNotConnected | ConnectionFailure
					| ParamIDDoesNotExist | ParamIDStillInUse e) {
				log.fatal(e);
				log.debug(StackTraceUtil.stackTraceToStr(e));
				e.showInfoToUser();
			}
		});
		selectedSingle.clear();
		createSingleList();
		enableRemoveButtons();
	}

	@Subscribe
	public void barcodeScanned(BarcodeScanEvent ¢) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {

				barcodeField.setText(Long.toString(¢.getBarcode()));
				showProductDetails();
			}
		});

	}

	private void showProductDetails() {
		try {
			currentCatalogProduct = manager.viewProductFromCatalog(Long.parseLong(barcodeField.getText()));
			productNamelbl.setText(currentCatalogProduct.getName());
			barcodeLbl.setText(currentCatalogProduct.getBarcode() + "");

			enableAddSaleButton();
			enableRemoveButtons();

		} catch (SMException e) {
			log.fatal(e);
			log.debug(StackTraceUtil.stackTraceToStr(e));
			e.showInfoToUser();
		}
	}

}
