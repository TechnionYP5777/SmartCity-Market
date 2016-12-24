package EmployeeGui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class EmployeeMainScreen extends Application {

	Scene mainScreen;
	Scene loginScreen;
	Stage primaryStage;

	@Override
	public void start(Stage primaryStage) {
		try {
			this.primaryStage = primaryStage;
			Parent mainScreenRoot = FXMLLoader
					.load(getClass().getResource("/EmployeeMainScreen/EmployeeMainScreen.fxml"));
			Parent loginScreenRoot = FXMLLoader
					.load(getClass().getResource("/EmployeeLoginScreen/EmployeeLoginScreen.fxml"));

			mainScreen = new Scene(mainScreenRoot);
			loginScreen = new Scene(loginScreenRoot);
			
			mainScreenRoot.setOnMouseClicked(e -> {
				primaryStage.setScene(loginScreen);
			});
			
			primaryStage.setTitle("Smart Market Beta");
			primaryStage.setScene(mainScreen);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
