package EmployeeGui;

import EmployeeContracts.IWorker;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class EmployeeMainScreen extends Application {

	private IWorker worker;
	private String username;
	private String password;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			
			Parent root = FXMLLoader.load(getClass().getResource("/EmployeeMainScreen/EmployeeMainScreen.fxml"));

			Scene scene = new Scene(root, 300, 275);
	
			primaryStage.setTitle("Smart Market Beta");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	

	public static void main(String[] args) {
		launch(args);
	}
}
