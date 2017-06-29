package GuiUtils;

import CommonDefs.GuiCommonDefs;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * @author idan atias
 *
 * @since Jun 29, 2017
 * 
 * This is a class for holding the flow and functionalities of creating a dot on the store's map
 */
public class MarkLocationOnStoreMap {

    private Group root;
    private BorderPane borderPane;
    private Canvas layer1;
    private Canvas layer2;
    private GraphicsContext gc1;
    private GraphicsContext gc2;
    private ChoiceBox<?> cb;
    private int width = 400;
    private int hieght = width;    

    private void createLayers(){

        // Layers 1&2 are the same size
        layer1 = new Canvas(width, hieght);
        layer2 = new Canvas(width,hieght);

        // Obtain Graphics Contexts
        gc1 = layer1.getGraphicsContext2D();
        Image img = new Image(GuiCommonDefs.storeMapPicPath, width, hieght, true, true);
        gc1.drawImage(img, 0, 0);
        gc2 = layer2.getGraphicsContext2D();
        gc2.setFill(Color.BLUE);
        gc2.fillOval(100,100,20,20);
    }

    private void handleLayers(){
        // Handler for Layer 1
        layer1.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {          
                gc1.fillOval(e.getX(),e.getY(),20,20);
            }
        });

         // Handler for Layer 2
        layer2.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
            	gc2.clearRect(0, 0, width, hieght);
                gc2.fillOval(e.getX() - 10, e.getY() - 10, 20, 20);
            }
        });
    }

    private void addLayers(){
        // Add Layers
        borderPane.setTop(cb);        
        Pane pane = new Pane();
        pane.getChildren().add(layer1);
        pane.getChildren().add(layer2);
        layer2.toFront();
        borderPane.setCenter(pane);    
        root.getChildren().add(borderPane);
    }

    public Group run() {
        
    	System.out.println(System.getProperty("user.dir")+"");
        // Build GUI
        borderPane = new BorderPane();  
        root = new Group();
        createLayers();
        handleLayers();
        addLayers();  

        return root;
    }
}
