package CartGui;

import org.apache.log4j.PropertyConfigurator;

import GuiUtils.AbstractApplicationScreen;
import javafx.stage.Stage;

/**
 * CartApplicationScreen - This class is the stage GUI class for Cart
 * 
 * @author Lior Ben Ami
 * @since 2017-01-13 */
public class CartApplicationScreen extends AbstractApplicationScreen {

	@Override
	public void start(Stage primaryStage) {
		try {
			stage = primaryStage;
			setScene("/CartMainScreen/CartMainScreen.fxml");
			stage.setTitle("Smart Market Beta");
			stage.setMaximized(true);
			stage.show();

		} catch (Exception ¢) {
			¢.printStackTrace();
		}
	}

	public static void main(String[] args) {
		/* Setting log properties */
		PropertyConfigurator.configure("../log4j.properties");
		
		launch(args);
	}

}