package CustomerGuiScreens;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.google.common.eventbus.Subscribe;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;

import BasicCommonClasses.CartProduct;
import BasicCommonClasses.CatalogProduct;
import BasicCommonClasses.Ingredient;
import BasicCommonClasses.Sale;
import BasicCommonClasses.SmartCode;
import CustomerContracts.ACustomerExceptions.CustomerNotConnected;
import CustomerContracts.ACustomerExceptions.InvalidParameter;
import CustomerContracts.ACustomerExceptions.ProductCatalogDoesNotExist;
import CustomerContracts.ICustomer;
import CustomerContracts.IRegisteredCustomer;
import CustomerGuiHelpers.CustomerProductCellFormat;
import CustomerGuiHelpers.TempCustomerPassingData;
import CustomerGuiHelpers.TempRegisteredCustomerPassingData;
import GuiUtils.AbstractApplicationScreen;
import GuiUtils.DialogMessagesService;
import SMExceptions.CommonExceptions.CriticalError;
import SMExceptions.SMException;
import UtilsContracts.IBarcodeEventHandler;
import UtilsContracts.IConfiramtionDialog;
import UtilsContracts.IConfiramtionWithAbortDialog;
import UtilsContracts.SmartcodeScanEvent;
import UtilsImplementations.BarcodeEventHandler;
import UtilsImplementations.InjectionFactory;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * CustomerMainScreen - Controller for main screen which holds the main
 * operations available for customer during shopping.
 * 
 * @author Lior Ben Ami
 * @author Aviad Cohen
 * @author Shimon Azulay
 * @since 2017-01-11
 */
public class CustomerMainScreen implements Initializable, IConfiramtionDialog {

	protected static Logger log = Logger.getLogger(CustomerMainScreen.class.getName());

	Stage primeStage = CustomerApplicationScreen.stage;

	ICustomer customer;

	// Main screen panes
	@FXML
	GridPane customerMainScreenPane;

	@FXML
	JFXListView<CartProduct> productsListView;

	@FXML
	Label productsNumberTextField;

	@FXML
	Label totalSumTextField;

	@FXML
	GridPane productInfoPane;

	@FXML
	Label productNameLabel;

	@FXML
	Label manufacturerLabel;

	@FXML
	Label priceLabel;

	@FXML
	Label amountLabel;

	@FXML
	Label descriptionTextArea;

	@FXML
	ImageView addButton;

	@FXML
	ImageView removeButton;

	@FXML
	Button removeAllButton;

	@FXML
	ImageView productInfoImage;

	@FXML
	JFXTextField searchField;

	@FXML
	Label yourListHeader;
	
	@FXML
	JFXCheckBox offerSalesChk;
	
	@FXML
	JFXComboBox<String> salesCombo;
	
	@FXML
	VBox salesPane;
	
	@FXML
	VBox noSalesPane;
	
	@FXML
	StackPane stackPaneSalesPane;

	SmartCode scannedSmartCode;

	CatalogProduct catalogProduct;

	ObservableList<CartProduct> productsObservableList = FXCollections.<CartProduct>observableArrayList();

	FilteredList<CartProduct> filteredProductList;

	// Lock smartCodeLocker;

	boolean flag = true;

	IBarcodeEventHandler barcodeEventHandler = InjectionFactory.getInstance(BarcodeEventHandler.class);

	/**
	 * the productInfoPane has three visible mode: 0 - Invisible (default) 1 -
	 * SCANNED_PRODUCT: when the pane present scanned product info and actions 2
	 * - PRESSED_PRODUCT: when the pane present pressed (on the listView Cell)
	 * product info and actions
	 */
	private enum ProductInfoPaneVisibleMode {
		SCANNED_PRODUCT, PRESSED_PRODUCT
	}

	private void addOrRemoveScannedProduct(CatalogProduct p, Integer amount) {
		updateProductInfoPaine(p, amount, ProductInfoPaneVisibleMode.SCANNED_PRODUCT);
	}

	private void updateProductInfoPaine(CatalogProduct p, Integer amount, ProductInfoPaneVisibleMode m) {
		updateProductInfoTexts(p, amount);
		switch (m) {
		case SCANNED_PRODUCT: {
			removeAllButton.setDisable(true);
			removeAllButton.setVisible(false);
			addButton.setDisable(false);
			addButton.setVisible(true);
			enableRemoveButton();
			break;
		}
		case PRESSED_PRODUCT: {
			removeAllButton.setDisable(false);
			removeAllButton.setVisible(true);
			addButton.setDisable(true);
			addButton.setVisible(false);
			removeButton.setDisable(true);
			removeButton.setVisible(false);
			break;
		}
		}
		showSalePane(p);
		setAbilityAndVisibilityOfProductInfoPane(true);
	}
	
	private void showSalePane(CatalogProduct p) {
		try {
			List<Sale> sales = customer.getSalesForProduct(p.getBarcode());
			salesCombo.getItems().clear();
			
			sales.forEach(sale -> {
				if (sale.getType().equals(Sale.SaleType.PercentageDiscount)) {
					salesCombo.getItems().add(sale.getDiscount() + " % Discount");
				} else if (sale.getType().equals(Sale.SaleType.OnePlusOneDiscount)) {
					salesCombo.getItems().add("1 + 1");
				} 
				// TODO group sale
			});			
		} catch (CriticalError | CustomerNotConnected | InvalidParameter | ProductCatalogDoesNotExist e) {
			log.fatal(e);
			log.debug(StackTraceUtil.stackTraceToStr(e));
			e.showInfoToUser();
		}
		
		if (salesCombo.getItems().isEmpty()) {
			noSalesPane.setVisible(true);
			salesPane.setVisible(false);
			noSalesPane.toFront();
		} else {
			noSalesPane.setVisible(false);
			salesPane.setVisible(true);
			salesPane.toFront();
			salesCombo.getItems().add("None");
		}
	}
	
	private void saleSelectionChanged() {
		if (salesCombo.getItems().equals("1 + 1")) {
			DialogMessagesService.showConfirmationWithAbortDialog("1 + 1 Sale", null, "Please Scan One More Product",
					new onePlusOnePopup());
		} 
		// TODO add case for group - open popup 
	}
	
	class onePlusOnePopup implements IConfiramtionWithAbortDialog {

		@Override
		public void abort() {
			// TODO Handle abort
			
		}


	}

	private void enableRemoveButton() {
		boolean flag = false;
		if (!customer.getCartProductCache().isEmpty()
				&& customer.getCartProductCache().get(scannedSmartCode.getBarcode()) != null)
			flag = customer.getCartProductCache().get(scannedSmartCode.getBarcode()).getPackages()
					.containsKey(scannedSmartCode);
		removeButton.setDisable(!flag);
		removeButton.setVisible(flag);

	}

	private void updateProductInfoTexts(CatalogProduct p, Integer amount) {
		productNameLabel.setText(p.getName());
		manufacturerLabel.setText(p.getManufacturer().getName());
		priceLabel.setText(String.format("%1$.2f", p.getPrice()));
		amountLabel.setText(amount + "");
		descriptionTextArea.setText(scannedSmartCode.getExpirationDate() + "");
		URL imageUrl = null;
		try {
			imageUrl = new File("../Common/src/main/resources/ProductsPictures/" + p.getBarcode() + ".jpg").toURI()
					.toURL();
		} catch (MalformedURLException e) {
			throw new RuntimeException();
		}
		productInfoImage.setImage(new Image(imageUrl + "", 290, 230, true, false));
	}

	private void setAbilityAndVisibilityOfProductInfoPane(boolean visibilty) {
		productInfoPane.setDisable(!visibilty);
		productInfoPane.setVisible(visibilty);
	}

	private void updateCartProductsInfo() {
		syncListViewWithCart();
		productsNumberTextField.setText(customer.getCartProductsNum() + "");
		totalSumTextField.setText(String.format("%.2f", customer.getTotalSum()));
	}

	private void syncListViewWithCart() {
		HashMap<Long, CartProduct> shoppingList = customer.getCartProductCache();
		productsObservableList.clear();
		shoppingList.forEach((key, value) -> productsObservableList.add(value));

		filteredProductList = new FilteredList<>(productsObservableList, s -> true);
		productsListView.setItems(filteredProductList);
	}

	private void logoutAndExit() {
		flag = true;
		DialogMessagesService.showConfirmationDialog("Already Leaving?", null, "", this);
	}

	@Override
	public void initialize(URL location, ResourceBundle __) {
		AbstractApplicationScreen.fadeTransition(customerMainScreenPane);
		barcodeEventHandler.register(this);
		customer = TempCustomerPassingData.customer != null ? TempCustomerPassingData.customer
				: TempRegisteredCustomerPassingData.regCustomer;

		filteredProductList = new FilteredList<>(productsObservableList, s -> true);

		searchField.textProperty().addListener(obs -> {
			String filter = searchField.getText();

			filteredProductList.setPredicate((filter == null || filter.length() == 0) ? s -> true
					: s -> s.getCatalogProduct().getName().contains(filter));
		});

		productsListView.setItems(filteredProductList);
		productsListView.setCellFactory(new Callback<ListView<CartProduct>, ListCell<CartProduct>>() {

			@Override
			public ListCell<CartProduct> call(ListView<CartProduct> __) {
				return new CustomerProductCellFormat();
			}
		});

		productsListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<CartProduct>() {

			@Override
			public void changed(ObservableValue<? extends CartProduct> __, CartProduct oldValue, CartProduct newValue) {
				updateProductInfoPaine(newValue.getCatalogProduct(), newValue.getTotalAmount(),
						ProductInfoPaneVisibleMode.PRESSED_PRODUCT);

			}
		});

		productsListView.depthProperty().set(1);
		productsListView.setExpanded(true);
		
		
		salesCombo.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(@SuppressWarnings("rawtypes") ObservableValue __, String s, String t1) {
				saleSelectionChanged();
			}
		});
		
		
		setAbilityAndVisibilityOfProductInfoPane(false);
	}

	@FXML
	public void purchaseButtonPressed(MouseEvent __) {
		flag = false;
		DialogMessagesService.showConfirmationDialog("Checkout grocery list?", null, "", this);
	}

	@FXML
	public void cancelButtonPressed(MouseEvent __) {
		logoutAndExit();
	}

	@FXML
	public void addButtonPressed(MouseEvent __) {
		try {
			customer.addProductToCart(scannedSmartCode, 1);
		} catch (SMException e) {
			log.fatal(e);
			log.debug(StackTraceUtil.stackTraceToStr(e));
			e.showInfoToUser();
			return;
		}
		updateCartProductsInfo();
		setAbilityAndVisibilityOfProductInfoPane(false);
	}

	@FXML
	public void removeButtonPressed(MouseEvent __) {
		try {
			customer.returnProductToShelf(scannedSmartCode, 1);
		} catch (SMException e) {
			log.fatal(e);
			log.debug(StackTraceUtil.stackTraceToStr(e));
			e.showInfoToUser();
			return;
		}
		updateCartProductsInfo();
		setAbilityAndVisibilityOfProductInfoPane(false);
	}

	@FXML
	public void removeAllButtonPressed(ActionEvent __) {
		try {
			customer.removeAllItemsOfCartProduct(scannedSmartCode);
		} catch (SMException e) {
			log.fatal(e);
			log.debug(StackTraceUtil.stackTraceToStr(e));
			e.showInfoToUser();
			return;
		}
		updateCartProductsInfo();
		setAbilityAndVisibilityOfProductInfoPane(false);
	}

	private void smartcodeScannedHandler() {
		Integer amount;
		CartProduct cartPtoduct = customer.getCartProduct(scannedSmartCode);
		if (cartPtoduct != null) {
			catalogProduct = cartPtoduct.getCatalogProduct();
			amount = cartPtoduct.getTotalAmount();
		} else {
			try {
				catalogProduct = customer.viewCatalogProduct(scannedSmartCode);
				//checkIngredients(catalogProduct);
				if (TempCustomerPassingData.customer == null)
					checkIngredients(catalogProduct);
			} catch (SMException e) {
				log.fatal(e);
				log.debug(StackTraceUtil.stackTraceToStr(e));
				e.showInfoToUser();
				return;
			}
			amount = 0;
		}
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				addOrRemoveScannedProduct(catalogProduct, amount);
			}
		});
	}

	private void checkIngredients(CatalogProduct catalogProduct) throws CriticalError  {
		ArrayList<String> markedIngredients = new ArrayList<String>();
		HashSet<Ingredient> productIngredients = catalogProduct.getIngredients();
		HashSet<Ingredient> customerAllergens = ((IRegisteredCustomer)customer).getCustomerAllergens();
		for (Ingredient ingredient:  customerAllergens) 
			if (productIngredients.contains(ingredient))
				markedIngredients.add(ingredient.getName());
		if (!markedIngredients.isEmpty()) {
			String alert = "This product contains some of your marked Ingredients:\n" + markedIngredients.toString();
			DialogMessagesService.showInfoDialog("Marked Ingredients Alert", null, alert);
		}
	}

	@Subscribe
	public void smartcodeScanned(SmartcodeScanEvent ¢) {
		SmartCode smartCode = ¢.getSmarCode();
		scannedSmartCode = smartCode;
		smartcodeScannedHandler();
	}

	@Override
	public void onYes() {
		try {
			if (flag) 
				customer.logout();
			else 
				customer.checkOutGroceryList();
			
		} catch (SMException e) {
			log.fatal(e);
			log.debug(StackTraceUtil.stackTraceToStr(e));
			e.showInfoToUser();
			Platform.exit();
			System.exit(0);
		}
		TempCustomerPassingData.customer = null;
		TempRegisteredCustomerPassingData.regCustomer = null;
		AbstractApplicationScreen.setScene("/CustomerWelcomeScreen/CustomerWelcomeScreen.fxml");
	}

	@Override
	public void onNo() {
		// Nothing to do
	}

	@FXML
	void forceScanProduct(ActionEvent __) {
		smartcodeScanned(new SmartcodeScanEvent(new SmartCode(1234567890, LocalDate.now())));
	}
	

	@FXML
	void forceScanProduct2(ActionEvent __) {
		smartcodeScanned(new SmartcodeScanEvent(new SmartCode(423324, LocalDate.now())));
	}
}
