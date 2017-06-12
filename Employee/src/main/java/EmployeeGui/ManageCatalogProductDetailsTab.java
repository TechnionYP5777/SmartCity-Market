package EmployeeGui;

import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXPopup.PopupHPosition;
import com.jfoenix.controls.JFXPopup.PopupVPosition;

import BasicCommonClasses.Ingredient;
import BasicCommonClasses.Manufacturer;
import EmployeeContracts.IManager;
import EmployeeDefs.AEmployeeException.ConnectionFailure;
import SMExceptions.CommonExceptions.CriticalError;
import UtilsContracts.IConfiramtionDialog;
import UtilsContracts.IEventBus;
import EmployeeDefs.AEmployeeException.EmployeeNotConnected;
import EmployeeDefs.AEmployeeException.IngredientStillInUse;
import EmployeeDefs.AEmployeeException.InvalidParameter;
import EmployeeDefs.AEmployeeException.ManfacturerStillInUse;
import EmployeeDefs.AEmployeeException.ParamIDAlreadyExists;
import EmployeeDefs.AEmployeeException.ParamIDDoesNotExist;
import EmployeeGuiContracts.IngredientEvent;
import EmployeeGuiContracts.ManufacturerEvent;
import EmployeeImplementations.Manager;
import GuiUtils.DialogMessagesService;
import UtilsImplementations.InjectionFactory;
import UtilsImplementations.ProjectEventBus;
import UtilsImplementations.StackTraceUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * ManageCatalogProductDetailsTab - This class is the controller for the Manage
 * Products details all action of this tab should be here.
 * 
 * @since 04.13.17
 * @author Shimon Azulay
 */

public class ManageCatalogProductDetailsTab implements Initializable {

	static Logger log = Logger.getLogger(ManagePackagesTab.class.getName());

	IManager manager = InjectionFactory.getInstance(Manager.class);

	@FXML
	private VBox rootPane;

	@FXML
	private JFXListView<String> manufacturerList;

	@FXML
	private JFXListView<String> ingredientsList;

	@FXML
	private JFXButton renameManu;

	@FXML
	private JFXButton renameIngr;

	@FXML
	private JFXButton addManuBtn;

	@FXML
	private JFXButton removeManuBtn;

	@FXML
	private JFXButton addIngrBtn;

	@FXML
	private JFXButton removeIngrBtn;

	private HashSet<String> selectedManu = new HashSet<String>();

	private HashSet<String> selectedIngr = new HashSet<String>();

	private HashMap<String, Manufacturer> manufacturars;

	private HashMap<String, Ingredient> ingredients;

	@FXML
	private JFXTextField filterManu;

	@FXML
	private JFXTextField filterIngr;

	ObservableList<String> dataManu;

	FilteredList<String> filteredDataManu;

	ObservableList<String> dataIngr;

	FilteredList<String> filteredDataIngr;

	JFXTextField newManu;

	JFXTextField newIngr;

	JFXButton okNewManu;

	JFXButton okNewIngr;

	JFXButton closeNewManu;

	JFXButton closeNewIngr;

	JFXButton closeRenameManu;

	JFXButton closeRenameIngr;

	JFXTextField renameManuLbl;

	JFXTextField renameIngrLbl;

	JFXButton okRenameIngr;

	JFXButton okRenameManu;

	JFXPopup popupNewManu;

	JFXPopup popupNewIngr;

	JFXPopup popupRenameManu;

	JFXPopup popupRenameIngr;
	
	IEventBus eventBus;

	void renameIngrPressed() {
		long id = ingredients.get(selectedIngr.iterator().next()).getId();
		eventBus.post(new IngredientEvent());
		try {
			manager.editIngredient(new Ingredient(id, renameIngrLbl.getText()));
		} catch (InvalidParameter | CriticalError | EmployeeNotConnected | ConnectionFailure | ParamIDDoesNotExist e) {
			log.debug(StackTraceUtil.getStackTrace(e));
			log.fatal(e);
			e.showInfoToUser();
		}
		selectedIngr.clear();
		createIngredientList();
		enableButtons();
		enableAddButtons();

	}

	void renameManuPressed() {
		long id = manufacturars.get(selectedManu.iterator().next()).getId();
		try {
			manager.editManufacturer(new Manufacturer(id, renameManuLbl.getText()));
			eventBus.post(new ManufacturerEvent());
		} catch (InvalidParameter | CriticalError | EmployeeNotConnected | ConnectionFailure | ParamIDDoesNotExist e) {
			log.fatal(e);
			log.debug(StackTraceUtil.getStackTrace(e));
			e.showInfoToUser();
		}
		selectedManu.clear();
		createManufacturerList();
		enableButtons();
		enableAddButtons();
	}

	void addIngPressed() {
		try {
			if (ingredients.containsKey(newIngr.getText())) {
				throw new ParamIDAlreadyExists();
			}
			manager.addIngredient(new Ingredient(0, newIngr.getText()));
			eventBus.post(new IngredientEvent());
		} catch (InvalidParameter | CriticalError | EmployeeNotConnected | ConnectionFailure | ParamIDAlreadyExists e) {
			log.fatal(e);
			log.debug(StackTraceUtil.getStackTrace(e));
			e.showInfoToUser();
		}
		selectedIngr.clear();
		createIngredientList();
		enableButtons();
		enableAddButtons();
	}

	void addManuPressed() {
		try {
			if (manufacturars.containsKey(newManu.getText())) {
				throw new ParamIDAlreadyExists();
			}
			manager.addManufacturer(new Manufacturer(0, newManu.getText()));
			eventBus.post(new ManufacturerEvent());
		} catch (InvalidParameter | CriticalError | EmployeeNotConnected | ConnectionFailure | ParamIDAlreadyExists e) {
			log.fatal(e);
			log.debug(StackTraceUtil.getStackTrace(e));
			e.showInfoToUser();
		}
		selectedManu.clear();
		createManufacturerList();
		enableButtons();
		enableAddButtons();
	}

	@FXML
	void removeIngrPressed(ActionEvent __) {
		DialogMessagesService.showConfirmationDialog("Remove Ingredients", null, "Are You Sure?",
				new removeIngrHandler());
	}

	private void removeIngrHandle() {
		selectedIngr.forEach(ing -> {
			try {
				manager.removeIngredient(ingredients.get(ing), false);
				eventBus.post(new IngredientEvent());
			} catch (InvalidParameter | CriticalError | EmployeeNotConnected | ConnectionFailure | ParamIDDoesNotExist
					| IngredientStillInUse e) {
				log.fatal(e);
				log.debug(StackTraceUtil.getStackTrace(e));
				e.showInfoToUser();
			}
		});
		selectedIngr.clear();
		createIngredientList();
		enableButtons();
		enableAddButtons();
	}

	@FXML
	void removeManuPress(ActionEvent __) {
		DialogMessagesService.showConfirmationDialog("Remove Manufacturers", null, "Are You Sure?",
				new removeManuHandler());
	}

	private void removeManuHandle() {
		selectedManu.forEach(man -> {
			try {
				manager.removeManufacturer(manufacturars.get(man));
				eventBus.post(new ManufacturerEvent());
			} catch (InvalidParameter | CriticalError | EmployeeNotConnected | ConnectionFailure | ParamIDDoesNotExist
					| ManfacturerStillInUse e) {
				log.fatal(e);
				log.debug(StackTraceUtil.getStackTrace(e));
				e.showInfoToUser();
			}
		});
		selectedManu.clear();
		createManufacturerList();
		enableButtons();
		enableAddButtons();
	}

	@Override
	public void initialize(URL location, ResourceBundle __) {
		
		eventBus = InjectionFactory.getInstance(ProjectEventBus.class);

		createManufacturerList();
		createIngredientList();

		filterManu.textProperty().addListener(obs -> {
			String filter = filterManu.getText();
			filteredDataManu.setPredicate(filter == null || filter.length() == 0 ? s -> true : s -> s.contains(filter));
		});

		manufacturerList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		// for multiple selection
		manufacturerList.addEventFilter(MouseEvent.MOUSE_PRESSED, evt -> {
			Node node = evt.getPickResult().getIntersectedNode();
			while (node != null && node != manufacturerList && !(node instanceof ListCell)) {
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

				ObservableList<String> selectedItems = manufacturerList.getSelectionModel().getSelectedItems();

				selectedManu.clear();
				selectedManu.addAll(selectedItems);
				enableButtons();

			}
		});

		manufacturerList.setDepth(1);
		manufacturerList.setExpanded(true);

		filterIngr.textProperty().addListener(obs -> {
			String filter = filterIngr.getText();
			filteredDataIngr.setPredicate(filter == null || filter.length() == 0 ? s -> true : s -> s.contains(filter));
		});

		ingredientsList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		// for multiple selection
		ingredientsList.addEventFilter(MouseEvent.MOUSE_PRESSED, evt -> {
			Node node = evt.getPickResult().getIntersectedNode();
			while (node != null && node != ingredientsList && !(node instanceof ListCell)) {
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

				ObservableList<String> selectedItems = ingredientsList.getSelectionModel().getSelectedItems();

				selectedIngr.clear();
				selectedIngr.addAll(selectedItems);
				enableButtons();

			}
		});

		ingredientsList.setDepth(1);
		ingredientsList.setExpanded(true);

		Label lbl1 = new Label("Insert New Manufacturar");
		newManu = new JFXTextField();
		okNewManu = new JFXButton("Done!");
		closeNewManu = new JFXButton("Close");
		okNewManu.getStyleClass().add("JFXButton");
		closeNewManu.getStyleClass().add("JFXButton");
		okNewManu.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent __) {
				addManuPressed();
				popupNewManu.hide();
			}
		});

		closeNewManu.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent __) {
				popupNewManu.hide();
			}
		});
		HBox manubtnContanier = new HBox();
		manubtnContanier.getChildren().addAll(okNewManu, closeNewManu);
		manubtnContanier.setSpacing(10);
		manubtnContanier.setAlignment(Pos.CENTER);
		VBox manuContainer = new VBox();
		manuContainer.getChildren().addAll(lbl1, newManu, manubtnContanier);
		manuContainer.setPadding(new Insets(10, 50, 50, 50));
		manuContainer.setSpacing(10);

		popupNewManu = new JFXPopup(manuContainer);
		addManuBtn.setOnMouseClicked(e -> {
			newManu.setText("");
			popupNewManu.show(addManuBtn, PopupVPosition.TOP, PopupHPosition.LEFT);
		});

		newManu.textProperty().addListener((observable, oldValue, newValue) -> enableAddButtons());

		Label lbl2 = new Label("Insert New Ingredient");
		newIngr = new JFXTextField();
		okNewIngr = new JFXButton("Done!");
		closeNewIngr = new JFXButton("Close");
		closeNewIngr.getStyleClass().add("JFXButton");
		okNewIngr.getStyleClass().add("JFXButton");
		okNewIngr.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent __) {
				addIngPressed();
				popupNewIngr.hide();
			}
		});

		closeNewIngr.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent __) {
				popupNewIngr.hide();
			}
		});

		HBox ingrbtnContanier = new HBox();
		ingrbtnContanier.getChildren().addAll(okNewIngr, closeNewIngr);
		ingrbtnContanier.setSpacing(10);
		ingrbtnContanier.setAlignment(Pos.CENTER);
		VBox ingrContainer = new VBox();
		ingrContainer.getChildren().addAll(lbl2, newIngr, ingrbtnContanier);
		ingrContainer.setPadding(new Insets(10, 50, 50, 50));
		ingrContainer.setSpacing(10);

		popupNewIngr = new JFXPopup(ingrContainer);
		addIngrBtn.setOnMouseClicked(e -> {
			newIngr.setText("");
			popupNewIngr.show(addIngrBtn, PopupVPosition.TOP, PopupHPosition.LEFT);
		});

		newIngr.textProperty().addListener((observable, oldValue, newValue) -> enableAddButtons());

		Label lbl3 = new Label("Rename Selected Manufacturar");
		renameManuLbl = new JFXTextField();
		okRenameManu = new JFXButton("Done!");
		closeRenameManu = new JFXButton("Close");
		closeRenameManu.getStyleClass().add("JFXButton");
		okRenameManu.getStyleClass().add("JFXButton");
		okRenameManu.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent __) {
				renameManuPressed();
				popupRenameManu.hide();
			}
		});

		closeRenameManu.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent __) {
				popupRenameManu.hide();
			}
		});

		HBox manuRenamebtnContanier = new HBox();
		manuRenamebtnContanier.getChildren().addAll(okRenameManu, closeRenameManu);
		manuRenamebtnContanier.setSpacing(10);
		manuRenamebtnContanier.setAlignment(Pos.CENTER);

		VBox renameManuContainer = new VBox();
		renameManuContainer.getChildren().addAll(lbl3, renameManuLbl, manuRenamebtnContanier);
		renameManuContainer.setPadding(new Insets(10, 50, 50, 50));
		renameManuContainer.setSpacing(10);

		popupRenameManu = new JFXPopup(renameManuContainer);
		renameManu.setOnMouseClicked(e -> {
			renameManuLbl.setText("");
			popupRenameManu.show(renameManu, PopupVPosition.TOP, PopupHPosition.LEFT);
		});

		renameManuLbl.textProperty().addListener((observable, oldValue, newValue) -> enableAddButtons());

		Label lbl4 = new Label("Rename Selected Ingredient");
		renameIngrLbl = new JFXTextField();
		okRenameIngr = new JFXButton("Done!");
		closeRenameIngr = new JFXButton("Close");
		closeRenameIngr.getStyleClass().add("JFXButton");
		okRenameIngr.getStyleClass().add("JFXButton");
		okRenameIngr.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent __) {
				renameIngrPressed();
				popupRenameIngr.hide();
			}
		});

		closeRenameIngr.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent __) {
				popupRenameIngr.hide();
			}
		});

		HBox ingrRenamebtnContanier = new HBox();
		ingrRenamebtnContanier.getChildren().addAll(okRenameIngr, closeRenameIngr);
		ingrRenamebtnContanier.setSpacing(10);
		ingrRenamebtnContanier.setAlignment(Pos.CENTER);

		VBox renameIngrContainer = new VBox();
		renameIngrContainer.getChildren().addAll(lbl4, renameIngrLbl, ingrRenamebtnContanier);
		renameIngrContainer.setPadding(new Insets(10, 50, 50, 50));
		renameIngrContainer.setSpacing(10);

		popupRenameIngr = new JFXPopup(renameIngrContainer);
		renameIngr.setOnMouseClicked(e -> {
			renameIngrLbl.setText("");
			popupRenameIngr.show(renameIngr, PopupVPosition.TOP, PopupHPosition.LEFT);
		});

		renameIngrLbl.textProperty().addListener((observable, oldValue, newValue) -> enableAddButtons());

		enableButtons();
		enableAddButtons();

	}

	private void enableAddButtons() {
		okNewManu.setDisable(newManu.getText().isEmpty());
		okNewIngr.setDisable(newIngr.getText().isEmpty());
		okRenameManu.setDisable(renameManuLbl.getText().isEmpty());
		okRenameIngr.setDisable(renameIngrLbl.getText().isEmpty());
	}

	private void createManufacturerList() {

		manufacturars = new HashMap<String, Manufacturer>();

		try {
			manager.getAllManufacturers()
					.forEach(manufacturer -> manufacturars.put(manufacturer.getName(), manufacturer));
		} catch (InvalidParameter | CriticalError | EmployeeNotConnected | ConnectionFailure e) {
			log.fatal(e);
			log.debug(StackTraceUtil.getStackTrace(e));
			e.showInfoToUser();
		}

		dataManu = FXCollections.observableArrayList();

		dataManu.setAll(manufacturars.keySet());

		// IntStream.range(0,
		// 1000).mapToObj(Integer::toString).forEach(dataManu::add);

		filteredDataManu = new FilteredList<>(dataManu, s -> true);

		manufacturerList.setItems(filteredDataManu);

	}

	private void createIngredientList() {
		ingredients = new HashMap<String, Ingredient>();
		try {
			manager.getAllIngredients().forEach(ingredient -> ingredients.put(ingredient.getName(), ingredient));
		} catch (InvalidParameter | CriticalError | ConnectionFailure e) {
			log.fatal(e);
			log.debug(StackTraceUtil.getStackTrace(e));
			e.showInfoToUser();
		}

		dataIngr = FXCollections.observableArrayList();

		dataIngr.setAll(ingredients.keySet());

		// IntStream.range(0,
		// 1000).mapToObj(Integer::toString).forEach(dataIngr::add);

		filteredDataIngr = new FilteredList<>(dataIngr, s -> true);

		ingredientsList.setItems(filteredDataIngr);
	}

	private void enableButtons() {
		addManuBtn.setDisable(!selectedManu.isEmpty());
		removeManuBtn.setDisable(selectedManu.isEmpty());
		addIngrBtn.setDisable(!selectedIngr.isEmpty());
		removeIngrBtn.setDisable(selectedIngr.isEmpty());
		renameManu.setDisable(selectedManu.isEmpty() || selectedManu.size() > 1);
		renameIngr.setDisable(selectedIngr.isEmpty() || selectedIngr.size() > 1);

	}

	class removeManuHandler implements IConfiramtionDialog {

		@Override
		public void onYes() {
			removeManuHandle();
		}

		@Override
		public void onNo() {
		}

	}

	class removeIngrHandler implements IConfiramtionDialog {

		@Override
		public void onYes() {
			removeIngrHandle();
		}

		@Override
		public void onNo() {
		}

	}

}
