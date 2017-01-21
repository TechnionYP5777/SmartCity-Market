package EmployeeGui;

import java.net.URL;
import java.util.ResourceBundle;

import CommonDefs.CLIENT_TYPE;
import EmployeeCommon.EmployeeScreensParameterService;
import EmployeeContracts.IWorker;
import EmployeeDefs.AEmployeeException;
import EmployeeImplementations.Manager;
import GuiUtils.AbstractApplicationScreen;
import SMExceptions.SMException;
import UtilsImplementations.InjectionFactory;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * WorkerMenuScreen - Controller for menu screen which holds the operations
 * available for worker.
 * 
 * @author idan atias
 * @author Shimon Azulay
 * @since 2016-12-27
 */

public class EmployeeMenuScreen implements Initializable {

	@FXML
	GridPane workerMenuScreenPane;

	@FXML
	TextArea successLogArea;

	Stage primeStage = EmployeeApplicationScreen.stage;

	@FXML
	Tab managePackagesTab;

	@FXML
	Tab manageCatalogProductTab;

	IWorker worker;

	@Override
	public void initialize(URL location, ResourceBundle __) {

		manageCatalogProductTab.setDisable(InjectionFactory.getInstance(EmployeeScreensParameterService.class)
				.getClientType().equals(CLIENT_TYPE.WORKER));

		worker = InjectionFactory.getInstance(Manager.class);
		AbstractApplicationScreen.fadeTransition(workerMenuScreenPane);

		// defining behavior when stage/window is closed.
		primeStage.setOnCloseRequest(event -> {
			try {
				worker.logout();
				Platform.exit();
				System.exit(0);
			} catch (SMException e) {
				EmployeeGuiExeptionHandler.handle(e);
				Platform.exit();
				System.exit(0);
			}
		});

		// setting success log listener
		successLogArea.textProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue<?> __, Object oldValue, Object newValue) {
				successLogArea.setScrollTop(Double.MAX_VALUE); // this will
																// scroll to the
																// bottom
			}
		});
	}

	@FXML
	private void logoutButtonPressed(ActionEvent __) {
		try {
			worker.logout();
		} catch (SMException e) {
			EmployeeGuiExeptionHandler.handle(e);
		}
		AbstractApplicationScreen.setScene("/EmployeeLoginScreen/EmployeeLoginScreen.fxml");
	}

}
