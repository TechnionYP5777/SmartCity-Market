package ForgetPasswordScreens;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

/**
 * QuestionScreenController - controller for password retrieve questions.
 * 
 * @author Shimon Azulay
 * @since 2017-04-09 
 *
 */
public class QuestionScreenController implements Initializable {

    @FXML
    private Label questionLbl;

    @FXML
    private JFXTextField answerText;

    @FXML
    private JFXButton nextBtn;

    @FXML
    private JFXButton prevBtn;

    @FXML
    void nextBtnPressed(ActionEvent __) {

    }

    @FXML
    void prevBtnPressed(ActionEvent __) {

    }

	@Override
	public void initialize(URL location, ResourceBundle __) {
		
		
	}

}
