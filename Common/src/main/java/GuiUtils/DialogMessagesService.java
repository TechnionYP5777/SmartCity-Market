package GuiUtils;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;

import UtilsContracts.IConfiramtionDialog;
import UtilsContracts.IConfiramtionWithCloseDialog;

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
		alertCreator(title, header, content, null, null);
	}

	public static void showErrorDialog(String title, String header, String content) {
		alertCreator(title, header, content, null, null);
	}
	

	public static void showConfirmationWithCloseDialog(String title, String header, Node node,
			IConfiramtionWithCloseDialog d) {
		alertCreator(title, header, null, node, d);
	}

	public static void showConfirmationDialog(String title, String header, String content,
			IConfiramtionDialog d) {
		JFXDialogLayout dialogContent = new JFXDialogLayout();
		dialogContent.setHeading(new Text(header == null ? title : title + "\n" + header));

		dialogContent.setBody(new Text(content));

		JFXButton yes = new JFXButton("Yes");
		yes.getStyleClass().add("JFXButton");

		JFXButton no = new JFXButton("No");
		no.getStyleClass().add("JFXButton");

		dialogContent.setActions(yes, new Label("   "), no);

		JFXDialog dialog = new JFXDialog((StackPane) AbstractApplicationScreen.stage.getScene().getRoot(),
				dialogContent, JFXDialog.DialogTransition.CENTER);

		yes.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent __) {

				dialog.close();

				d.onYes();
			}
		});

		no.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent __) {

				dialog.close();

				d.onNo();
			}
		});
		
		dialog.show();
		dialog.toFront();
	}

	private static void alertCreator(String title, String header, String content, Node node, IConfiramtionWithCloseDialog d) {
		JFXDialogLayout dialogContent = new JFXDialogLayout();
		dialogContent.setHeading(new Text(header == null ? title : title + "\n" + header));
		if (content != null)
			dialogContent.setBody(new Text(content));
		
		if (node != null)
			dialogContent.setBody(node);

		JFXButton close = new JFXButton("Close");
		close.getStyleClass().add("JFXButton");

		dialogContent.setActions(close);

		JFXDialog dialog = new JFXDialog((StackPane) AbstractApplicationScreen.stage.getScene().getRoot(),
				dialogContent, JFXDialog.DialogTransition.CENTER);

		close.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent __) {
				dialog.close();
				
				if (d != null) {
					d.onClose();
				}
			}
		});
		
		dialog.show();
		dialog.toFront();
	}
}
