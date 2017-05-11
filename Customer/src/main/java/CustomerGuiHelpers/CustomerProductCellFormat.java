package CustomerGuiHelpers;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import com.jfoenix.controls.JFXListCell;

import BasicCommonClasses.CartProduct;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/**
 * CustomerProductCellFormat - This class will format the cell content
 * 
 * @author aviad
 * @since 2017-01-28
 */
public class CustomerProductCellFormat extends JFXListCell<CartProduct> {

	@Override
	public void updateItem(CartProduct item, boolean empty) {
		super.updateItem(item, empty);

		if (item == null || empty) {
			setGraphic(null);
			setText(null);
			return;
		}

		HBox hbx = new HBox(280);
		VBox vbx = new VBox(5); // spacing = 5
	
		//vbox
		Label productName = new Label("Name: " + item.getCatalogProduct().getName());
		productName.setFont(new Font(20));
		Label productAmount = new Label("Amount: " + item.getTotalAmount());
		productAmount.setFont(new Font(20));
		Label productPrice = new Label("Price: " + Double.valueOf(item.getCatalogProduct().getPrice()) + " nis");
		productPrice.setFont(new Font(20));
	    vbx.getChildren().addAll(productName, productAmount, productPrice);
	    vbx.setAlignment(Pos.CENTER_LEFT);	    

	    //image
	    long itemBarcode = item.getCatalogProduct().getBarcode();
	    URL imageUrl = null;
		try {
			imageUrl = new File("../Common/src/main/resources/ProductsPictures/" + itemBarcode + ".jpg").toURI().toURL();
		} catch (MalformedURLException e) {
			throw new RuntimeException();
		}
		Image image = new Image(imageUrl + "", 100, 100, true, false);
		ImageView productImage = new ImageView(image);
				
		hbx.setSpacing(230);
	    hbx.getChildren().addAll(vbx, productImage);

		setGraphic(hbx);
	}
}
