package EmployeeGui;

import java.net.URL;
import java.util.ResourceBundle;

import GuiUtils.AbstractApplicationScreen;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;


/**
 * EmployeeMainScreen - This class is the controller for the employee main screen
 * all action of this scene should be here.
 * 
 * @author Shimon Azulay
 * @since 2016-12-26 */

public class EmployeeMainScreen implements Initializable {

	@FXML
	private StackPane mainScreenPane;
	@FXML
	private VBox vbox;

	@Override
	public void initialize(URL location, ResourceBundle __) {
		AbstractApplicationScreen.fadeTransition(mainScreenPane);

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
		AbstractApplicationScreen.setScene("/EmployeeLoginScreen/EmployeeLoginScreen.fxml");
	}

}
