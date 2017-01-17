package CartGui;

import BasicCommonClasses.CartProduct;
import javafx.scene.control.ListCell;

public class CartProductFormatCell extends ListCell<CartProduct>  {

	public CartProductFormatCell() {    }
    
    @Override protected void updateItem(CartProduct p, boolean empty) {
        // calling super here is very important - don't skip this!
        super.updateItem(p, empty);
          
        //for now - only its name will be shown
        setText(p.getCatalogProduct().getName());
    }
}



    
