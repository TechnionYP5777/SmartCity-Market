package CartGuiHelpers;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import BasicCommonClasses.CartProduct;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
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

		HBox hbx = new HBox(5);
		VBox vbx = new VBox(5); // spacing = 5
	
		//vbox
		Label productName = new Label("Name: " + item.getCatalogProduct().getName());
		Label productAmount = new Label("Amount: " + item.getTotalAmount());
		Label productPrice = new Label("Price: " + Double.valueOf(item.getCatalogProduct().getPrice()) + " nis");
	    vbx.getChildren().addAll(productName, productAmount, productPrice);
	    vbx.setAlignment(Pos.CENTER);	    

	    //image
	    long itemBarcode = item.getCatalogProduct().getBarcode();
	    URL imageUrl = null;
		try {
			imageUrl = new File("../Common/src/main/resources/ProductsPictures/" + itemBarcode + ".jpg").toURI().toURL();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Image image = new Image(imageUrl + "", 100, 100, true, false);
		ImageView productImage = new ImageView(image);
	    hbx.getChildren().addAll(vbx, productImage);

		setGraphic(hbx);
	}
}
