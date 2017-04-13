package EmployeeGui;

import java.net.URL;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.jfoenix.controls.JFXListView;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 * ManageCatalogProductDetailsTab - This class is the controller for the Manage Products details
 * all action of this tab should be here.
 * @since 04.13.17
 * @author Shimon Azulay
 */

public class ManageCatalogProductDetailsTab implements Initializable {
	
	static Logger log = Logger.getLogger(ManagePackagesTab.class.getName());

	@FXML
    private JFXListView<?> manufacturerList;

    @FXML
    private JFXListView<?> ingredientsList;

    @FXML
    void addIngPressed(ActionEvent event) {

    }

    @FXML
    void addManuPressed(ActionEvent event) {

    }

    @FXML
    void removeIngrPressed(ActionEvent event) {

    }

    @FXML
    void removeManuPress(ActionEvent event) {

    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
	
	

}
