package GuiUtils;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;

/**
 * Use this class to create GUI Error, info, confirmation GUI dialogs.
 * 
 * If Header is not necessary pass null.
 * 
 * @author Shimon Azulay
 * @since 2016-12-27
 *
 */
public class DialogMessagesService {

	public static void showInfoDialog(String title, String header, String content) {
		alertCreator(AlertType.INFORMATION, title, header, content);
	}

	public static void showErrorDialog(String title, String header, String content) {
		alertCreator(AlertType.ERROR, title, header, content);
	}

	public static void showConfirmationDialog(String title, String header, String content) {
		alertCreator(AlertType.CONFIRMATION, title, header, content);
	}

	private static void alertCreator(AlertType t, String title, String header, String content) {
		JFXDialogLayout dialogContent = new JFXDialogLayout();
		dialogContent.setHeading(new Text(title + "\n" + header));
		dialogContent.setBody(new Text(content));
		
		JFXButton close = new JFXButton("Close");
		close.getStyleClass().add("JFXButton");
		
		dialogContent.setActions(close);
		
		JFXDialog dialog = new JFXDialog((StackPane) AbstractApplicationScreen.stage.getScene().getRoot(),
				dialogContent, JFXDialog.DialogTransition.CENTER);

		close.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				dialog.close();			
			}
		});
		
		dialog.show();

		// Alert alert = new Alert(t);
		// alert.initModality(Modality.WINDOW_MODAL);
		// alert.setTitle(title);
		// alert.setHeaderText(header);
		// alert.setContentText(content);
		// alert.show();
	}
}
