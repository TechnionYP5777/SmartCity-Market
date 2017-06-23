package EmployeeGui;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.jfoenix.controls.JFXTabPane;

import CommonDefs.CLIENT_TYPE;
import EmployeeCommon.EmployeeScreensParameterService;
import EmployeeContracts.IWorker;
import EmployeeImplementations.Manager;
import GuiUtils.AbstractApplicationScreen;
import GuiUtils.DialogMessagesService;
import SMExceptions.SMException;
import UtilsContracts.IConfiramtionDialog;
import UtilsImplementations.InjectionFactory;
import UtilsImplementations.StackTraceUtil;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
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
//
public class EmployeeMenuScreen implements Initializable {

	protected static Logger log = Logger.getLogger(EmployeeMenuScreen.class.getName());

	@FXML
	GridPane workerMenuScreenPane;

	@FXML
	TextArea successLogArea;

	Stage primeStage = EmployeeApplicationScreen.stage;

	@FXML
	JFXTabPane tabsPane;

	@FXML
	Tab managePackagesTab;

	@FXML
	Tab manageCatalogProductTab;

	@FXML
	Tab manageCatalogProductDetailsTab;

	@FXML
	Tab manageEmployeesTab;
	
	@FXML
	Tab manageSalesTab;

	@FXML
	Button logout;

	IWorker worker;

	@Override
	public void initialize(URL location, ResourceBundle __) {

		if (InjectionFactory.getInstance(EmployeeScreensParameterService.class).getClientType()
				.equals(CLIENT_TYPE.WORKER)) {
			tabsPane.getTabs().remove(manageCatalogProductTab);
			tabsPane.getTabs().remove(manageCatalogProductDetailsTab);
			tabsPane.getTabs().remove(manageEmployeesTab);
			tabsPane.getTabs().remove(manageSalesTab);

		}

		tabsPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

		tabsPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
			@Override
			public void changed(ObservableValue<? extends Tab> ov, Tab __, Tab t1) {
				// if (t1 == managePackagesTab) {
				// FXMLLoader loader = new FXMLLoader(
				// getClass().getResource("/ManagePackagesTab/ManagePackagesTab.fxml"));
				// try {
				// loader.load();
				// } catch (IOException e) {
				// }
				// ITabPaneHandler ctrl = loader.getController();
				// ctrl.tabSelected();
				// }
			}
		});

		worker = InjectionFactory.getInstance(Manager.class);
		AbstractApplicationScreen.fadeTransition(workerMenuScreenPane);

		// setting success log and it's listener
		successLogArea.setEditable(false);
		successLogArea.textProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue<?> __, Object oldValue, Object newValue) {
				successLogArea.setScrollTop(Double.MAX_VALUE); // this will
																// scroll to the
																// bottom
			}
		});
	}

	public void printToSuccessLog(String msg) {
		successLogArea.setEditable(true);
		successLogArea.appendText(new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss").format(new Date()) + " :: " + msg + "\n");
		successLogArea.setEditable(false);
	}

	@FXML
	private void logoutButtonPressed(MouseEvent __) {
		DialogMessagesService.showConfirmationDialog("Logout", null, "Are You Sure?", new LogoutHandler());
	}

	void logoutHandle() {
		try {
			if (worker.isLoggedIn())
				worker.logout();
		} catch (SMException e) {
			log.fatal(e);
			log.debug(StackTraceUtil.stackTraceToStr(e));
			e.showInfoToUser();
		}
		AbstractApplicationScreen.setScene("/EmployeeLoginScreen/EmployeeLoginScreen.fxml");
	}

	class LogoutHandler implements IConfiramtionDialog {

		@Override
		public void onYes() {
			logoutHandle();
		}

		@Override
		public void onNo() {
		}

	}
}
