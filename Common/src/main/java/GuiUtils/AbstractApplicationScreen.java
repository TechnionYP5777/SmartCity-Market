package GuiUtils;

import java.io.IOException;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * AbstractApplicationScreen - This abstract should be extended by class that will hold stage
 * it represents common functionality of GUI Screen 
 * 
 * @author Shimon Azulay
 * @since 2016-12-26 */

public abstract class AbstractApplicationScreen extends Application {
	
	protected static Stage stage;
	
	public static void setScene(String sceneName) {
		Parent parent;
		try {
			parent = FXMLLoader.load(AbstractApplicationScreen.class.getResource(sceneName));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		stage.setScene(new Scene(parent));
	}
	
	
	public static void fadeTransition(Node n){
        FadeTransition x=new FadeTransition(new Duration(2000),n);
        x.setFromValue(0);
        x.setToValue(100);
        x.setCycleCount(1);
        x.setInterpolator(Interpolator.LINEAR);
        x.play();
    }

}
