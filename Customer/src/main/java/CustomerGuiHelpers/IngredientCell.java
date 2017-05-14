package CustomerGuiHelpers;

import BasicCommonClasses.Ingredient;
import javafx.scene.control.cell.CheckBoxListCell;

/**
 * IngredientCell Class - implements the updateItem method for the ingredientsCheckListView
 * 
 * @author Lior Ben Ami
 * @since 2017-05-06
 */
public class IngredientCell extends CheckBoxListCell<Ingredient> {
	@Override
    public void updateItem(Ingredient item, boolean empty)
    {
	       if (item == null || empty)
	        	return;
		super.updateItem(item, empty);
 
        setText(item.getName());
    }
}
