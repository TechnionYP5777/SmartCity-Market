package CustomerGuiScreens;

import com.jfoenix.controls.JFXButton;

import BasicCommonClasses.Ingredient;
import CustomerContracts.ICustomer;
import CustomerGuiHelpers.CustomerGuiExceptionsHandler;
import CustomerGuiHelpers.IngredientCellFactory;
import CustomerGuiHelpers.TempCustomerProfilePassingData;
import CustomerImplementations.Customer;
import GuiUtils.AbstractApplicationScreen;
import SMExceptions.SMException;
import UtilsImplementations.InjectionFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;

import org.controlsfx.control.CheckListView;

public class CustomerRegistration_IngredientsScreen implements Initializable {

	@FXML
	private GridPane ingredientsScreenPane;
	
    @FXML
    private CheckListView<Ingredient> ingredientsCheckListView;

    @FXML
    private JFXButton ingridients_nextButton;

    @FXML
    private JFXButton ingridients_backButton;

	ObservableList<Ingredient> ingredientsObservableList = FXCollections.<Ingredient>observableArrayList();


    private void updateIngredientsCheckList() {
		ICustomer customer = InjectionFactory.getInstance(Customer.class);	
		try {
			ingredientsObservableList.addAll(customer.getAllIngredients());
    	} catch (SMException e) {
			CustomerGuiExceptionsHandler.handle(e);	
    	}
		ingredientsCheckListView.setItems(ingredientsObservableList);
		ingredientsCheckListView.setCellFactory(new IngredientCellFactory());
		HashSet<Ingredient> currentAllergans =  TempCustomerProfilePassingData.customerProfile.getAllergens();
		if (currentAllergans != null && currentAllergans.isEmpty()) {
			updateListViewWithChosenIngreidients(currentAllergans);
		}
	}

	private void updateListViewWithChosenIngreidients(HashSet<Ingredient> currentAllergans) {
		HashSet<Ingredient> currentIngredientsHashSet =TempCustomerProfilePassingData.customerProfile.getAllergens();
		for (Ingredient ingredient : currentIngredientsHashSet)
			ingredientsCheckListView.getCheckModel().check(ingredient);
	}

    private void updateCurrentChosenIngredients() {
    	TempCustomerProfilePassingData.customerProfile.clearAllAllergens();
    	HashSet<Ingredient> checkedIngredientsHashSet = new HashSet<Ingredient>(ingredientsCheckListView.getCheckModel().getCheckedItems()); 
    	TempCustomerProfilePassingData.customerProfile.setAllergens(checkedIngredientsHashSet);    	
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		AbstractApplicationScreen.fadeTransition(ingredientsScreenPane);
    	updateIngredientsCheckList();
	}
	
	@FXML
    void ingridients_backButtonPressed(ActionEvent event) {
		updateCurrentChosenIngredients();
		AbstractApplicationScreen.setScene("/CustomerRegistrationScreens/CustomerRegistration_PersonalInfoScreen.fxml");
    }
	
    @FXML
    void ingridients_nextButtonPressed(ActionEvent event) {
		updateCurrentChosenIngredients();
    	AbstractApplicationScreen.setScene("/CustomerRegistrationScreens/CustomerRegistration_FinalStepScreen.fxml");
    }
}

