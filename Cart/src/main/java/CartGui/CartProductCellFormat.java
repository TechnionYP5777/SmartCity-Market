package CartGui;

import BasicCommonClasses.CartProduct;
import javafx.scene.control.ListCell;

public class CartProductCellFormat  extends ListCell<CartProduct>
{
  @Override
  public void updateItem(CartProduct item, boolean empty)
  {
    super.updateItem(item, empty);

    if (item == null || empty) {
            setGraphic(null);
            setText(null);
            return;
        }
    String cellString = item.toString();
    this.setText(cellString);
    setGraphic(null);
  }
}
