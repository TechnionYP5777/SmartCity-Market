package CustomerGuiScreens;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.google.common.eventbus.Subscribe;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXPopup.PopupHPosition;
import com.jfoenix.controls.JFXPopup.PopupVPosition;

import BasicCommonClasses.CartProduct;
import BasicCommonClasses.CatalogProduct;
import BasicCommonClasses.Ingredient;
import BasicCommonClasses.Location;
import BasicCommonClasses.Sale;
import BasicCommonClasses.SmartCode;
import CustomerContracts.ICustomer;
import CustomerContracts.IRegisteredCustomer;
import CustomerGuiHelpers.CustomerProductCellFormat;
import CustomerGuiHelpers.TempCustomerPassingData;
import CustomerGuiHelpers.TempRegisteredCustomerPassingData;
import GuiUtils.AbstractApplicationScreen;
import GuiUtils.DialogMessagesService;
import GuiUtils.MarkLocationOnStoreMap;
import SMExceptions.CommonExceptions.CriticalError;
import SMExceptions.SMException;
import UtilsContracts.IBarcodeEventHandler;
import UtilsContracts.IConfiramtionDialog;
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
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
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
	IRegisteredCustomer registeredCustomer;

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
	Label saleLbl;

	@FXML
	Label descriptionTextArea;

	@FXML
	ImageView addButton;

	@FXML
	ImageView removeButton;

	@FXML
	ImageView specialSaleImg;

	@FXML
	Button removeAllButton;

	@FXML
	ImageView productInfoImage;

	@FXML
	JFXTextField searchField;

	@FXML
	ImageView checkOut;

	@FXML
	Label yourListHeader;

	@FXML
	JFXCheckBox offerSalesChk;

	@FXML
	JFXListView<String> allerList;

	@FXML
	JFXTextField searchCatalogProduct;

	@FXML
	HBox toShowCatalog;

	@FXML
	Label saleTxtLbl;

	@FXML
	JFXButton showLocation;

	SmartCode scannedSmartCode;

	CatalogProduct catalogProduct;

	ObservableList<CartProduct> productsObservableList = FXCollections.<CartProduct>observableArrayList();

	FilteredList<CartProduct> filteredProductList;

	// Lock smartCodeLocker;

	boolean flag = true;

	IBarcodeEventHandler barcodeEventHandler = InjectionFactory.getInstance(BarcodeEventHandler.class);

	HashSet<CatalogProduct> marketCatalog;

	/**
	 * the productInfoPane has three visible mode: 0 - Invisible (default) 1 -
	 * SCANNED_PRODUCT: when the pane present scanned product info and actions 2
	 * - PRESSED_PRODUCT: when the pane present pressed (on the listView Cell)
	 * product info and actions
	 */
	private enum ProductInfoPaneVisibleMode {
		SCANNED_PRODUCT, PRESSED_PRODUCT, FROM_CATALOG
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
		case FROM_CATALOG:
			removeAllButton.setDisable(false);
			removeAllButton.setVisible(false);
			addButton.setDisable(false);
			addButton.setVisible(false);
			removeButton.setDisable(false);
			removeButton.setVisible(false);
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
		if (p != null) {
			productNameLabel.setText(p.getName());
			manufacturerLabel.setText(p.getManufacturer().getName());
			priceLabel.setText(String.format("%1$.2f", p.getPrice()));
			amountLabel.setText(amount != null ? amount + "" : "N/A");
			descriptionTextArea.setText(amount != null ? scannedSmartCode.getExpirationDate() + "" : "N/A");
			allerList.getItems().clear();
			p.getIngredients().forEach(ingr -> {
				allerList.getItems().add(ingr.getName());
			});

			if (p.getSpecialSale().isValid()) {
				URL imageSaleUrl = null;
				try {
					imageSaleUrl = new File("../Common/src/main/resources/CustomerMainScreen/special.gif").toURI()
							.toURL();
				} catch (MalformedURLException e) {
					throw new RuntimeException();
				}
				saleLbl.setText(p.getSpecialSale().getSaleAsString());
				specialSaleImg.setVisible(true);
				specialSaleImg.setImage(new Image(imageSaleUrl + "", 290, 230, true, false));

			} else if (p.getSale().isValid()) {
				URL imageSaleUrl = null;
				try {
					imageSaleUrl = new File("../Common/src/main/resources/CustomerMainScreen/sale.gif").toURI().toURL();
				} catch (MalformedURLException e) {
					throw new RuntimeException();
				}

				saleLbl.setText(p.getSale().getSaleAsString());
				specialSaleImg.setVisible(true);
				specialSaleImg.setImage(new Image(imageSaleUrl + "", 290, 230, true, false));
			} else {
				saleLbl.setText(p.getSale().getSaleAsString());
				specialSaleImg.setVisible(false);
			}
			productInfoImage.setVisible(true);
			URL imageUrl = null;
			try {
				imageUrl = new File("../Common/src/main/resources/ProductsPictures/" + p.getBarcode() + ".jpg").toURI()
						.toURL();
			} catch (MalformedURLException e) {
				throw new RuntimeException();
			}
			productInfoImage.setImage(new Image(imageUrl + "", 290, 230, true, false));
			showLocation.setDisable(false);
		} else {
			productNameLabel.setText("N/A");
			manufacturerLabel.setText("N/A");
			priceLabel.setText("N/A");
			amountLabel.setText("N/A");
			descriptionTextArea.setText("N/A");
			allerList.getItems().clear();
			productInfoImage.setVisible(false);
			saleLbl.setText("N/A");
			specialSaleImg.setVisible(false);
			showLocation.setDisable(true);
		}
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
		enableSearchAndCheckout();
	}

	private void enableSearchAndCheckout() {
		boolean disable = productsListView.getItems().isEmpty();
		searchField.setVisible(!disable);
		searchField.setDisable(disable);
		checkOut.setVisible(!disable);
		checkOut.setDisable(disable);
	}

	private void logoutAndExit() {
		flag = true;
		DialogMessagesService.showConfirmationDialog("Already Leaving?", null, "", this);
	}

	JFXPopup catalogPopup;

	JFXListView<String> catalogList;

	HashMap<String, CatalogProduct> catalogs;

	ObservableList<String> dataCatalogs;

	FilteredList<String> filteredDataCatalogs;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void initialize(URL location, ResourceBundle __) {
		AbstractApplicationScreen.fadeTransition(customerMainScreenPane);
		barcodeEventHandler.register(this);
		registeredCustomer = TempRegisteredCustomerPassingData.regCustomer;
		customer = TempCustomerPassingData.customer != null ? TempCustomerPassingData.customer
				: TempRegisteredCustomerPassingData.regCustomer;
		if (registeredCustomer == null) {
			offerSalesChk.setVisible(false);
			offerSalesChk.setDisable(true);
		}
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
				if (newValue != null) {
					catalogProduct = newValue.getCatalogProduct();
					updateProductInfoPaine(catalogProduct, newValue.getTotalAmount(),
							ProductInfoPaneVisibleMode.PRESSED_PRODUCT);
				}

			}
		});

		productsListView.depthProperty().set(1);
		productsListView.setExpanded(true);

		allerList.depthProperty().set(1);
		allerList.setExpanded(true);
		allerList.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener() {
			@Override
			public void changed(ObservableValue observable, Object oldvalue, Object newValue) {
				Platform.runLater(new Runnable() {
					public void run() {
						allerList.getSelectionModel().select(-1);

					}
				});

			}
		});

		Label lbl1 = new Label("Catalog Products");
		JFXButton close = new JFXButton("Close");
		close.getStyleClass().add("JFXButton");

		close.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent __) {
				catalogPopup.hide();
			}
		});

		catalogList = new JFXListView<String>();
		catalogList.setMinWidth(600);
		catalogList.setMaxWidth(600);
		catalogList.setMaxHeight(400);

		HBox manubtnContanier = new HBox();
		manubtnContanier.getChildren().addAll(close);
		manubtnContanier.setSpacing(10);
		manubtnContanier.setAlignment(Pos.CENTER_RIGHT);
		VBox manuContainer = new VBox();
		manuContainer.getChildren().addAll(lbl1, catalogList, manubtnContanier);
		manuContainer.setPadding(new Insets(10, 50, 50, 50));
		manuContainer.setSpacing(10);

		catalogPopup = new JFXPopup(manuContainer);

		catalogList.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				catalogProduct = catalogs.get(catalogList.getSelectionModel().getSelectedItem());
				catalogPopup.hide();
				updateProductInfoPaine(catalogProduct, null, ProductInfoPaneVisibleMode.FROM_CATALOG);
			}
		});

		catalogList.setDepth(1);
		catalogList.setExpanded(true);

		searchCatalogProduct.textProperty().addListener(obs -> {
			String filter = searchCatalogProduct.getText();
			filteredDataCatalogs
					.setPredicate(filter == null || filter.length() == 0 ? s -> true : s -> s.contains(filter));
			if (filter.isEmpty() || catalogList.getItems().isEmpty()) {
				catalogPopup.hide();
			} else {
				catalogPopup.show(toShowCatalog, PopupVPosition.TOP, PopupHPosition.LEFT);
			}

		});

		updateProductInfoPaine(null, null, ProductInfoPaneVisibleMode.FROM_CATALOG);

		createCatalogList();

		enableSearchAndCheckout();
	}

	private void createCatalogList() {

		catalogs = new HashMap<String, CatalogProduct>();

		try {
			customer.getMarketCatalog().forEach(cat -> {
				catalogs.put(cat.getName() + " Barcode: " + cat.getBarcode(), cat);
			});
		} catch (CriticalError e) {
			log.fatal(e);
			log.debug(StackTraceUtil.stackTraceToStr(e));
			e.showInfoToUser();
		}

		dataCatalogs = FXCollections.observableArrayList();

		dataCatalogs.setAll(catalogs.keySet());

		filteredDataCatalogs = new FilteredList<>(dataCatalogs, s -> true);

		catalogList.setItems(filteredDataCatalogs);
	}

	@FXML
	void onLocationPressed(ActionEvent event) {
		Location loc = catalogProduct.getLocations().iterator().next();
		DialogMessagesService.showConfirmationWithCloseDialog(
				"Product Location Is: " + "(col=" + loc.getX() + ", row=" + loc.getY() + ")", null,
				new MarkLocationOnStoreMap().run(loc.getX(), loc.getY()), null);
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
			customer.addProductToCart(scannedSmartCode, catalogProduct, 1);
		} catch (SMException e) {
			log.fatal(e);
			log.debug(StackTraceUtil.stackTraceToStr(e));
			e.showInfoToUser();
			return;
		}
		updateCartProductsInfo();
		updateProductInfoTexts(null, null);
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
		updateProductInfoTexts(null, null);
	}

	@FXML
	public void removeAllButtonPressed(ActionEvent __) {
		try {
			customer.removeAllItemsOfCartProduct(scannedSmartCode);
			removeAllButton.setVisible(false);
			removeAllButton.setDisable(true);
		} catch (SMException e) {
			log.fatal(e);
			log.debug(StackTraceUtil.stackTraceToStr(e));
			e.showInfoToUser();
			return;
		}
		updateCartProductsInfo();
		updateProductInfoTexts(null, null);
	}

	class SpecialSaleHandler implements IConfiramtionDialog {

		Sale sale;

		SpecialSaleHandler(Sale sale) {
			this.sale = sale;
		}

		@Override
		public void onYes() {
			registeredCustomer.addSpecialSale(sale, true);

		}

		@Override
		public void onNo() {
			registeredCustomer.addSpecialSale(sale, false);
		}

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
				catalogProduct.setSale(customer.getSaleForProduct(catalogProduct.getBarcode()));
				if (registeredCustomer != null) {
					/* Checking for special sell */
					Sale specialSale = registeredCustomer.getSpecailSaleForProduct(catalogProduct.getBarcode());

					if (specialSale.isValid()) {
						DialogMessagesService.showConfirmationDialog("Special Sale just for you!", null,
								specialSale.getSaleAsString() + "\n" + "If you want to take the sale, click 'Yes' "
										+ "add the products to your grocery list",
								new SpecialSaleHandler(specialSale));
						catalogProduct.setSpecialSale(specialSale);
					}
				}
				if (registeredCustomer != null)

					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							checkIngredients(catalogProduct);
						}
					});
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

	private void checkIngredients(
			CatalogProduct catalogProduct) /* throws CriticalError */ {
		ArrayList<String> markedIngredients = new ArrayList<String>();
		HashSet<Ingredient> productIngredients = catalogProduct.getIngredients();
		HashSet<Ingredient> customerAllergens = ((IRegisteredCustomer) customer).getCustomerAllergens();
		for (Ingredient ingredient : customerAllergens)
			if (productIngredients.contains(ingredient))
				markedIngredients.add(ingredient.getName());
		if (!(markedIngredients.isEmpty())) {
			StringBuilder markedIngredientsSB = new StringBuilder();
			Integer i = 1;
			for (String ing : markedIngredients) {
				if (i > 1)
					markedIngredientsSB.append(", ");
				markedIngredientsSB.append(i++ + ". " + ing);
			}
			DialogMessagesService.showInfoDialog("Marked Ingredients Alert",
					"This product contains some of your marked Ingredients:\n", markedIngredientsSB.toString());
		}
	}

	@Subscribe
	public void smartcodeScanned(SmartcodeScanEvent ¢) {
		SmartCode smartCode = ¢.getSmarCode();
		scannedSmartCode = smartCode;

		smartcodeScannedHandler();
	}

	Map<Sale, Boolean> getTakenSales() {
		if (registeredCustomer != null && offerSalesChk.isSelected()) {
			HashMap<Long, CartProduct> shoppingList = customer.getCartProductCache();
			HashMap<Sale, Boolean> specialSales = registeredCustomer.getSpecialSales();

			for (Sale sale : specialSales.keySet()) {
				if (shoppingList.containsKey(sale.getProductBarcode())
						&& shoppingList.get(sale.getProductBarcode()).getTotalAmount() > sale.getAmountOfProducts()) {
					specialSales.put(sale, true);
				} else {
					specialSales.put(sale, false);
				}
			}

			return specialSales;
		}
		return new HashMap<Sale, Boolean>();
	}

	@Override
	public void onYes() {
		try {
			if (flag)
				customer.logout();
			else {
				customer.checkOutGroceryList(getTakenSales());
			}

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
		smartcodeScanned(new SmartcodeScanEvent(new SmartCode(1234, LocalDate.now())));
	}
}
