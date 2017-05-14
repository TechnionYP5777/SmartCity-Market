package CustomerGuiHelpers;

import BasicCommonClasses.Ingredient;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

/**
 * IngredientCellFactory Class - implements the call method for the ingredientsCheckListView
 * 
 * @author Lior Ben Ami
 * @since 2017-05-06
 */
public class IngredientCellFactory implements Callback<ListView<Ingredient>, ListCell<Ingredient>>
{
	@Override
	public ListCell<Ingredient> call(ListView<Ingredient> param) {
		return new IngredientCell();
	}
}
