package EmployeeGui;

import java.net.URL;
import java.util.ResourceBundle;

import EmployeeCommon.EmployeeScreensParameterService;
import EmployeeCommon.IEmployeeScreensParameterService;
import GuiUtils.AbstractApplicationScreen;
import UtilsImplementations.InjectionFactory;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.InputEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

/**
 * EmployeeMainScreen - This class is the controller for the employee main
 * screen all action of this scene should be here.
 * 
 * @author Shimon Azulay
 * @since 2016-12-26
 */
@SuppressWarnings("unused")
public class EmployeeMainScreen implements Initializable {

	@FXML
	private StackPane mainScreenPane;
	@FXML
	private VBox vbox;

	@Override
	public void initialize(URL location, ResourceBundle __) {
		AbstractApplicationScreen.fadeTransition(mainScreenPane);
		IEmployeeScreensParameterService employeeScreensParameterService = InjectionFactory
				.getInstance(EmployeeScreensParameterService.class);

		if (employeeScreensParameterService.getNotShowMainScreenVideo()) {
			MediaPlayer player = new MediaPlayer(
					new Media(getClass().getResource("/EmployeeMainScreen/vid.mp4").toExternalForm()));
			MediaView mediaView = new MediaView(player);
			final DoubleProperty width = mediaView.fitWidthProperty();
			final DoubleProperty height = mediaView.fitHeightProperty();

			width.bind(Bindings.selectDouble(mediaView.sceneProperty(), "width"));
			height.bind(Bindings.selectDouble(mediaView.sceneProperty(), "height"));

			mediaView.setPreserveRatio(false);

			mainScreenPane.getChildren().add(mediaView);
			player.setCycleCount(MediaPlayer.INDEFINITE);
			player.play();
		}
		vbox.toFront();
		vbox.setFocusTraversable(true);

	}

	@FXML
	public void mouseOrKeyboardPressed(InputEvent __) {
		AbstractApplicationScreen.setScene("/EmployeeLoginScreen/EmployeeLoginScreen.fxml");
	}

}
