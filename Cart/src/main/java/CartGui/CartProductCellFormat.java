package CartGui;

import BasicCommonClasses.CartProduct;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
//import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class CartProductCellFormat extends ListCell<CartProduct> {

	@Override
	public void updateItem(CartProduct item, boolean empty) {
		super.updateItem(item, empty);

		if (item == null || empty) {
			setGraphic(null);
			setText(null);
			return;
		}

		VBox vbx = new VBox(5); // spacing = 5
		Label productName = new Label("Name: " + item.getCatalogProduct().getName());
		Label productAmount = new Label("Amount: " + item.getTotalAmount());
		Label productPrice = new Label("Price: " + new Double(item.getCatalogProduct().getPrice()).toString() + " nis");
		Image image = new Image("http://www.toprechesh.co.il/files/products/image1_2102_2016-02-14_13-46-36.jpg");
		ImageView productImage = new ImageView(image);
	    vbx.getChildren().addAll(productName, productAmount, productPrice, productImage);
	    vbx.setAlignment(Pos.CENTER);

		setGraphic(vbx);
	}
}
