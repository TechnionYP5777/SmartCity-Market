package CartGui;

import BasicCommonClasses.CartProduct;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class CartProductCellFormat extends ListCell<CartProduct> {
//
//	private VBox cellContainer;
//
//	private Label productName;
//
//	private Label productAmount;
//
//	private Label productPrice;
//
//	private ImageView productImage;

	@Override
	public void updateItem(CartProduct item, boolean empty) {
		super.updateItem(item, empty);

		if (item == null || empty) {
			setGraphic(null);
			setText(null);
			return;
		}
		
		VBox vbx = new VBox(5); // spacing = 5
		Label productName = new Label(item.getCatalogProduct().getName());
		Label productAmount = new Label("1");
		Label productPrice = new Label(new Double(item.getCatalogProduct().getPrice()).toString());
		Image image = new Image("http://www.toprechesh.co.il/files/products/image1_2102_2016-02-14_13-46-36.jpg");
		ImageView productImage = new ImageView(image);
	    vbx.getChildren().addAll(productName, productAmount, productPrice, productImage);

		setGraphic(vbx);
	}
}
