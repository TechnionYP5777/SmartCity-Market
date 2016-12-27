package EmployeeGui;

import GuiUtils.AbstractApplicationScreen;
import javafx.stage.Stage;


/**
 * EmployeeApplicationScreen - This class is the stage GUI class for Employee
 * 
 * @author Shimon Azulay
 * @since 2016-12-26 */

public class EmployeeApplicationScreen extends AbstractApplicationScreen {

	@Override
	public void start(Stage primaryStage) {
		try {
			stage = primaryStage;
			setScene("/EmployeeMainScreen/EmployeeMainScreen.fxml");
			stage.setTitle("Smart Market Beta");
			stage.setMaximized(true);
			stage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

}
