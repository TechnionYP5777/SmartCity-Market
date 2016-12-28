package EmployeeGui;

import java.net.URL;
import java.util.ResourceBundle;

import GuiUtils.AbstractApplicationScreen;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

/**
 * WorkerMenuScreen - Controller for menu screen which holds the operations
 * available for worker.
 * 
 * @author idan atias
 * @since 2016-12-27
 */

public class WorkerMenuScreen implements Initializable {

	@FXML
	private GridPane workerMenuScreenPane;

	@FXML
	private Button viewCatalogProductButton;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		AbstractApplicationScreen.fadeTransition(workerMenuScreenPane);

	}

}
