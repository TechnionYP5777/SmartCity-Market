package EmployeeGui;

import java.net.URL;
import java.util.ResourceBundle;

import GuiUtils.AbstractApplicationScreen;
import GuiUtils.StackPaneService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * WorkerMenuScreen - Controller for menu screen which holds the operations
 * available for worker.
 * 
 * @author idan atias
 * @author Shimon Azulay
 * @since 2016-12-27
 */

public class WorkerMenuScreen implements Initializable {

	@FXML
	private GridPane workerMenuScreenPane;

	@FXML
	private Button viewCatalogProductButton;

	@FXML
	private StackPane stackPane;

	@FXML
	private VBox viewCatalogProductPane;

	@FXML
	private VBox addProductPackagePane;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		AbstractApplicationScreen.fadeTransition(workerMenuScreenPane);

	}

	@FXML
	private void viewCatalogProductButtonPressed(ActionEvent e) {
		StackPaneService.bringToFront(stackPane, viewCatalogProductPane.getId());
	}

	@FXML
	private void addProductPackageButtonPressed(ActionEvent e) {
		StackPaneService.bringToFront(stackPane, addProductPackagePane.getId());
	}

}
