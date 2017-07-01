package GuiUtils;

import javafx.application.Platform;
import javafx.scene.layout.StackPane;

/**
 * StackPaneService - Service to handle the stack pane.
 * 
 * @author Shimon Azulay
 * @since 2016-12-10
 */
public class StackPaneService {

	/**
	 * Use this static method to bring to front pane by id in a stackPane container
	 * @param  parent- stackPane container
	 * @param  paneIdToBringToFront- pane to bring to front
	 */
	public static void bringToFront(StackPane parent, final String paneIdToBringToFront) {
		parent.getChildren().forEach(node -> Platform.runLater(!node.getId().equals(paneIdToBringToFront) ? new Runnable() {
			@Override
			public void run() {
				node.setVisible(false);
			}
		} : new Runnable() {
			@Override
			public void run() {
				node.setVisible(true);
				node.toFront();
			}
		}));
	}
}
