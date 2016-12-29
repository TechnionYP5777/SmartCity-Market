package GuiUtils;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

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

	static public void showInfoDialog(String title, String header, String content) {
		alertCreator(AlertType.INFORMATION, title, header, content);
	}

	static public void showErrorDialog(String title, String header, String content) {
		alertCreator(AlertType.ERROR, title, header, content);
	}

	static public void showConfirmationDialog(String title, String header, String content) {
		alertCreator(AlertType.CONFIRMATION, title, header, content);
	}

	static private void alertCreator(AlertType alertType, String title, String header, String content) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
	}
}
