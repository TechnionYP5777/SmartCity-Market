package EmployeeGui;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import EmployeeCommon.TempWorkerPassingData;
import EmployeeContracts.IWorker;
import EmployeeDefs.AEmployeeException;
import GuiUtils.AbstractApplicationScreen;
import SMExceptions.SMException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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

	IWorker worker;

	
	@Override
	public void initialize(URL location, ResourceBundle __) {
		AbstractApplicationScreen.fadeTransition(workerMenuScreenPane);
		worker = TempWorkerPassingData.worker;

		// defining behavior when stage/window is closed.
		primeStage.setOnCloseRequest(event -> {
			try {
				if (worker.isServerReachable() && worker.isLoggedIn())
					worker.logout();
			} catch (SMException e) {
				if (e instanceof AEmployeeException.EmployeeNotConnected)
					return;
				EmployeeGuiExeptionHandler.handle(e);
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

	private void printToSuccessLog(String msg) {
		successLogArea.appendText(new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss").format(new Date()) + " :: " + msg + "\n");
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
