package CartGui;

import java.net.URL;
import java.util.ResourceBundle;

import GuiUtils.AbstractApplicationScreen;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * CartWelcomeScreen - This class is the controller for the cart welcome screen
 * all action of this scene should be here.
 * 
 * @author Lior Ben Ami
 * @since 2017-01-16 */
public class CartWelcomeScreen implements Initializable {

	@FXML
	private StackPane cartWelcomeScreenPane;
	@FXML
	private VBox vbox;

	@Override
	public void initialize(URL location, ResourceBundle __) {
		AbstractApplicationScreen.fadeTransition(cartWelcomeScreenPane);

//        MediaPlayer player = new MediaPlayer( new Media(getClass().getResource("/EmployeeMainScreen/store.mp4").toExternalForm()));
//        MediaView mediaView = new MediaView(player);
//        mediaView.setFitWidth(1600); mediaView.setFitHeight(900);
//        mediaView.setPreserveRatio(false);
//        mainScreenPane.getChildren().add(mediaView);
//        player.setMute(true);;
//        player.setCycleCount(MediaPlayer.INDEFINITE);   
//        player.play();
        
        vbox.toFront();
	}

	@FXML
	public void mouseClicked(MouseEvent __) {
		AbstractApplicationScreen.setScene("/CartLoginScreen/CartLoginScreen.fxml");
	}
}
