package CustomerGuiScreens;

import java.net.URL;
import java.util.ResourceBundle;

import CustomerImplementations.CustomerDefs;
import GuiUtils.AbstractApplicationScreen;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.InputEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

/**
 * CustomerWelcomeScreen - This class is the controller for the customer welcome
 * screen all action of this scene should be here.
 * 
 * @author Lior Ben Ami
 * @author Shimon Azulay
 * @since 2017-01-16
 */
public class CustomerWelcomeScreen implements Initializable {

	@FXML
	private StackPane customerWelcomeScreenPane;

	@FXML
	private VBox vbox;

	@Override
	public void initialize(URL location, ResourceBundle __) {
		AbstractApplicationScreen.fadeTransition(customerWelcomeScreenPane);

		if (CustomerDefs.showVideo) {

			MediaPlayer player = new MediaPlayer(
					new Media(getClass().getResource("/CustomerWelcomeScreen/vid.mp4").toExternalForm()));
			MediaView mediaView = new MediaView(player);
			final DoubleProperty width = mediaView.fitWidthProperty();
			final DoubleProperty height = mediaView.fitHeightProperty();

			width.bind(Bindings.selectDouble(mediaView.sceneProperty(), "width"));
			height.bind(Bindings.selectDouble(mediaView.sceneProperty(), "height"));

			mediaView.setPreserveRatio(false);

			customerWelcomeScreenPane.getChildren().add(mediaView);
			player.setCycleCount(MediaPlayer.INDEFINITE);
			player.play();
		}
		vbox.toFront();
		vbox.setFocusTraversable(true);
	}

	@FXML
	public void mouseOrKeyboardPressed(InputEvent __) {
		AbstractApplicationScreen.setScene("/CustomerLoginScreen/CustomerLoginScreen.fxml");
	}
}
