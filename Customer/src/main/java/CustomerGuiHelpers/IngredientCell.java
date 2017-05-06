package CustomerGuiHelpers;

import BasicCommonClasses.Ingredient;
import javafx.scene.control.ListCell;

/**
 * IngredientCell Class - implements the updateItem method for the ingredientsCheckListView
 * 
 * @author Lior Ben Ami
 * @since 2017-05-06
 */
public class IngredientCell extends ListCell<Ingredient> {
	@Override
    public void updateItem(Ingredient item, boolean empty)
    {
        super.updateItem(item, empty);
        if (item == null || empty)
        	return;
        this.setText(item.getName());
    }
}
