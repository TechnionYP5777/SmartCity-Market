package CartGui;

import javafx.scene.control.ListCell;

public class IntCellFormat  extends ListCell<Integer>
{
  @Override
  public void updateItem(Integer item, boolean empty)
  {
    super.updateItem(item, empty);

    if (item == null || empty) {
            setGraphic(null);
            setText(null);
            return;
        }
    this.setText("int: " + Integer.toString(item));
    setGraphic(null);
  }
}
