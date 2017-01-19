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
				
		Alert alert = new Alert(t);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
	}
}
