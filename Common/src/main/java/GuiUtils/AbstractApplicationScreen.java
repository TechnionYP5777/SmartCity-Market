package GuiUtils;

import java.io.IOException;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
	
	public static Stage stage;
	public static double stageHieght = GuiDefs.stageHeight;
	public static double stageWidth = GuiDefs.stageWidth;
	
	public static void setScene(String sceneName) {
		Parent parent;
		try {
			parent = FXMLLoader.load(AbstractApplicationScreen.class.getResource(sceneName));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		Scene scene = new Scene(parent);
		stage.setScene(scene);

		// setting up stage props
		stage.setHeight(stageHieght);
		stage.setWidth(stageWidth);

		// setting up stage(=window) size listeners
		scene.widthProperty().addListener(new ChangeListener<Number>() {
		    @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
		    	stage.setWidth(stageWidth);
		    }
		});
		scene.heightProperty().addListener(new ChangeListener<Number>() {
		    @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
		    	stage.setHeight(stageHieght);
		    }
		});
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
