package CustomerGuiScreens;

import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;

import BasicCommonClasses.Ingredient;
import CustomerContracts.ICustomer;
import CustomerGuiHelpers.TempCustomerProfilePassingData;
import CustomerImplementations.Customer;
import GuiUtils.AbstractApplicationScreen;
import SMExceptions.SMException;
import UtilsImplementations.InjectionFactory;
import UtilsImplementations.StackTraceUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

/**
 * CustomerRegistration_IngredientsScreen - Controller for the second screen of the customer registration screens.
 * 											This screen handle the ingredients choices.
 * 
 * @author Lior Ben Ami
 * @since 2017-04
 */
public class CustomerRegistration_IngredientsScreen implements Initializable {
	
	protected static Logger log = Logger.getLogger(CustomerRegistration_IngredientsScreen.class.getName());

	@FXML
	private GridPane ingredientsScreenPane;
	
    @FXML
    private JFXListView<String> ingredientList;
 
    @FXML
    private JFXButton ingridients_nextButton;

    @FXML
    private JFXButton ingridients_backButton;
    
    HashMap<String, Ingredient> ingredients;

	ObservableList<String> dataIngr;

	FilteredList<String> filteredDataIngr;
	
	ICustomer customer;	


	@Override
	public void initialize(URL location, ResourceBundle __) {
		AbstractApplicationScreen.fadeTransition(ingredientsScreenPane);
		customer = InjectionFactory.getInstance(Customer.class);
		
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

		    	TempCustomerProfilePassingData.customerProfile.clearAllAllergens();
		    	HashSet<Ingredient> checkedIngredientsHashSet = new HashSet<Ingredient>();
		    	selectedItems.forEach(ing-> {
		    		checkedIngredientsHashSet.add(ingredients.get(ing));
		    	});
		    	TempCustomerProfilePassingData.customerProfile.setAllergens(checkedIngredientsHashSet); 

			}
		});

		ingredientList.setDepth(1);
		ingredientList.setExpanded(true);
		
		createIngredientList();
	}
	
	private void createIngredientList() {
		ingredients = new HashMap<String, Ingredient>();
		try {
			customer.getAllIngredients().forEach(ingr -> {
				ingredients.put(ingr.getName(), ingr);
			});
    	} catch (SMException e) {
			log.fatal(e);
			log.debug(StackTraceUtil.stackTraceToStr(e));
			e.showInfoToUser();
    	}

		dataIngr = FXCollections.observableArrayList();

		dataIngr.setAll(ingredients.keySet());

		filteredDataIngr = new FilteredList<>(dataIngr, s -> true);

		ingredientList.setItems(filteredDataIngr);
	}
	
	@FXML
    void ingridients_backButtonPressed(ActionEvent __) {
		AbstractApplicationScreen.setScene("/CustomerRegistrationScreens/CustomerRegistration_PersonalInfoScreen.fxml");
    }
	
    @FXML
    void ingridients_nextButtonPressed(ActionEvent __) {
    	AbstractApplicationScreen.setScene("/CustomerRegistrationScreens/CustomerRegistration_FinalStepScreen.fxml");
    }
    
	@FXML
    void cancelButtonPressed(ActionEvent __) {
		TempCustomerProfilePassingData.clear();
		AbstractApplicationScreen.setScene("/CustomerLoginScreen/CustomerLoginScreen.fxml");
	}
}
