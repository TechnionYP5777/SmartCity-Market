package EmployeeGui;

import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.google.common.eventbus.Subscribe;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;

import BasicCommonClasses.CatalogProduct;
import EmployeeContracts.IManager;
import EmployeeDefs.AEmployeeException.ConnectionFailure;
import EmployeeDefs.AEmployeeException.EmployeeNotConnected;
import EmployeeDefs.AEmployeeException.InvalidParameter;
import EmployeeDefs.AEmployeeException.ParamIDDoesNotExist;
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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.Spinner;
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
    private Label productNamelbl;

    @FXML
    private JFXComboBox<String> saleTypeCombo;

    @FXML
    private JFXCheckBox groupSaleChk;

    @FXML
    private VBox minBuyPane;

    @FXML
    private Spinner<Integer> minAmount;
    
    @FXML
    private Spinner<Integer> discountAmount;

    @FXML
    private JFXButton addProductSale;

    @FXML
    private JFXListView<String> singleList;

    @FXML
    private JFXButton removeSingleBtn;

    @FXML
    private JFXTextField filterSingle;

    @FXML
    private JFXListView<String> groupList;

    @FXML
    private JFXButton removeGroupBtn;

    @FXML
    private JFXTextField filterGroup;
    
    private HashSet<String> selectedSingle = new HashSet<String>();
    
    private HashSet<String> selectedGroup = new HashSet<String>(); 
    
    private FilteredList<String> filteredDataSingle;
    
    private FilteredList<String> filteredDataGroup;
    
    private ObservableList<String> dataSingle;
    
    private ObservableList<String> dataGroup;
    
	private HashMap<Long, Integer> singles;

	private HashMap<Long, Integer> groups;
	
	IManager manager;
	
	IEventBus eventBus;
	
	CatalogProduct currentCatalogProduct = null;
	
	static Logger log = Logger.getLogger(ManageSalesTab.class.getName());

    @FXML
    void removeGroupPressed(ActionEvent event) {
    	DialogMessagesService.showConfirmationDialog("Remove Group Sale", null, "Are You Sure?",
				new removeGroupHandler());
    }

    @FXML
    void removeSinglePress(ActionEvent event) {
    	DialogMessagesService.showConfirmationDialog("Remove Sale", null, "Are You Sure?",
				new removeSingleHandler());
    }
    
    @FXML
    void groupSaleChkPressed(MouseEvent event) {
    	enablePanes();
    }
    
    private void enablePanes() {
    	minBuyPane.setDisable(!groupSaleChk.isSelected());
    	discountPane.setVisible(saleTypeCombo.getSelectionModel().getSelectedItem().equals("% discount"));
    	chooseSalePane.setDisable(currentCatalogProduct == null);
    }
    
    @FXML
    void searchClicked(MouseEvent event) {
    	showProductDetails();
    }


	@Override
	public void initialize(URL location, ResourceBundle resources) {

		eventBus = InjectionFactory.getInstance(ProjectEventBus.class);
		eventBus.register(this);
		
		manager = InjectionFactory.getInstance(Manager.class);
		
		filterSingle.textProperty().addListener(obs -> {
			String filter = filterSingle.getText();
			filteredDataSingle.setPredicate(filter == null || filter.length() == 0 ? s -> true : s -> s.contains(filter));
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
		
		
		filterGroup.textProperty().addListener(obs -> {
			String filter = filterGroup.getText();
			filteredDataGroup.setPredicate(filter == null || filter.length() == 0 ? s -> true : s -> s.contains(filter));
		});

		groupList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		// for multiple selection
		groupList.addEventFilter(MouseEvent.MOUSE_PRESSED, evt -> {
			Node node = evt.getPickResult().getIntersectedNode();
			while (node != null && node != groupList && !(node instanceof ListCell)) {
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

				ObservableList<String> selectedItems = groupList.getSelectionModel().getSelectedItems();

				selectedGroup.clear();
				selectedGroup.addAll(selectedItems);
				enableRemoveButtons();

			}
		});

		groupList.setDepth(1);
		groupList.setExpanded(true);
		

		
	//	minAmount.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);

		minAmount.valueProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue == null || newValue < 1)
				minAmount.getValueFactory().setValue(oldValue);
			enableAddSaleButton();
		});

		minAmount.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null && !newValue) {
				minAmount.increment(0);
				enableAddSaleButton();
			}
		});
		
		//discountAmount.getValueFactory().setValue(1);
		
		discountAmount.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);

		discountAmount.valueProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue == null || newValue < 1 || newValue > 100)
				discountAmount.getValueFactory().setValue(oldValue);
			enableAddSaleButton();
		});

		discountAmount.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null && !newValue) {
				discountAmount.increment(0);
				enableAddSaleButton();
			}
		});
		
		//TODO enable this functions when server supports commands
		//createSingleList();
		//createGroupList();
		createTypesCombo();
		enableAddSaleButton();
		enableRemoveButtons();
		enablePanes();
	}
	
	private void createTypesCombo() {
		saleTypeCombo.getItems().addAll("1+1", "% discount");
		saleTypeCombo.getSelectionModel().select(0);
		
		saleTypeCombo.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(@SuppressWarnings("rawtypes") ObservableValue __, String s, String t1) {
				enablePanes();
			}
		});
		
	}
	
	private void createSingleList() {
		
		singles = new HashMap<Long, Integer>();
		
		dataSingle = FXCollections.observableArrayList();
		
		try {
			manager.getAllSales().forEach((i, val) -> {
				//TODO Shimon fix here
				//singles.put(val.getProduct().getBarcode(), i);
				// TODO change Sale class to return Type of sale
				//dataSingle.add("Product: " + val.getProduct().getName());
			});
		} catch (InvalidParameter | CriticalError | EmployeeNotConnected | ConnectionFailure e) {
			log.fatal(e);
			log.debug(StackTraceUtil.stackTraceToStr(e));
			e.showInfoToUser();
		}

		filteredDataSingle = new FilteredList<>(dataSingle, s -> true);

		singleList.setItems(filteredDataSingle);

	}
	
	private void createGroupList() {
		
		groups = new HashMap<Long, Integer>();
		
		dataGroup = FXCollections.observableArrayList();
		
		try {
			manager.getAllSales().forEach((i, val) -> {
				//TODO Shimon fix here
				//groups.put(val.getProduct().getBarcode(), i);
				// TODO change Sale class to return Type of sale
				//dataGroup.add("Product: " + val.getProduct().getName());
			});
		} catch (InvalidParameter | CriticalError | EmployeeNotConnected | ConnectionFailure e) {
			log.fatal(e);
			log.debug(StackTraceUtil.stackTraceToStr(e));
			e.showInfoToUser();
		}

		filteredDataGroup = new FilteredList<>(dataGroup, s -> true);

		groupList.setItems(filteredDataGroup);
		
	}
	
	private void enableAddSaleButton() {
		
		addProductSale.setDisable(currentCatalogProduct == null);
		
	}
	
	private void enableRemoveButtons() {
		removeSingleBtn.setDisable(selectedSingle.isEmpty());
		removeGroupBtn.setDisable(selectedGroup.isEmpty());
	}
	
	class removeGroupHandler implements IConfiramtionDialog {

		@Override
		public void onYes() {
			//TODO Shimon remove this
		}

		@Override
		public void onNo() {
		}

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
			manager.removeSale(singles.get(getBarcodeFromTextCell(sale)));
		} catch (InvalidParameter | CriticalError | EmployeeNotConnected | ConnectionFailure | ParamIDDoesNotExist e) {
			log.fatal(e);
			log.debug(StackTraceUtil.stackTraceToStr(e));
			e.showInfoToUser();
		}
				
		
		});
		selectedSingle.clear();
		createSingleList();
		enableRemoveButtons();
	}
	
	
	private long getBarcodeFromTextCell(String sale) {
		// TODO 
		return 1;
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
			
			enableAddSaleButton();
			enableRemoveButtons();
			enablePanes();
			
		} catch (SMException e) {
			log.fatal(e);
			log.debug(StackTraceUtil.stackTraceToStr(e));
			e.showInfoToUser();
		}
	}
	


}

