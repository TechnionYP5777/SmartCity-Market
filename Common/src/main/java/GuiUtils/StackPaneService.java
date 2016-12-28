package GuiUtils;

import javafx.application.Platform;
import javafx.scene.layout.StackPane;

/**
 * 
 * @author Shimon Azulay
 *
 */
public class StackPaneService {

	/**
	 * Use this static method to bring to front pane by id in a stackPane
	 * container
	 * 
	 * @param parent-
	 *            stackPane container
	 * @param paneIdToBringToFront-
	 *            pane to bring to front
	 */
	static public void bringToFront(StackPane parent, final String paneIdToBringToFront) {
		parent.getChildren().forEach(node -> {
			if (node.getId().equals(paneIdToBringToFront)) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						node.setVisible(true);
						node.toFront();
					}
				});
			} else {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						node.setVisible(false);
					}
				});
			}
		});
	}
}
