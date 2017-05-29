package GuiUtils;

import javafx.beans.property.*;
import javafx.collections.*;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.Stack;

import org.apache.log4j.Logger;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import UtilsContracts.IForgotPasswordHandler;
import UtilsImplementations.ForgotPasswordHandler.NoSuchUserName;
import UtilsImplementations.ForgotPasswordHandler.WrongAnswer;
import UtilsImplementations.StackTraceUtil;

/**
 * This class displays a survey using a wizard
 * 
 * @author Shimon Azulay
 * @since 20.5.17
 */
public class ForgetPasswordUtil {

	public static IForgotPasswordHandler forgotPasswordHandler;
	public static Logger log = Logger.getLogger(ForgetPasswordUtil.class.getName());
	public static String question = "";

	public static void start(IForgotPasswordHandler forgotPasswordHandler) throws Exception {
		ForgetPasswordUtil.forgotPasswordHandler = forgotPasswordHandler;
		Stage stage = new Stage();
		stage.setTitle("Restore Password Wizard");
		stage.setScene(new Scene(new SurveyWizard(stage), 400, 250));
		stage.show();
	}
}

/**
 * basic wizard infrastructure class
 */
class Wizard extends StackPane {
	private static final int UNDEFINED = -1;
	private ObservableList<WizardPage> pages = FXCollections.observableArrayList();
	private Stack<Integer> history = new Stack<>();
	private int curPageIdx = UNDEFINED;

	Wizard(WizardPage... nodes) {
		pages.addAll(nodes);
		navTo(0);
		setStyle("-fx-padding: 10; -fx-background-color: cornsilk;");
	}

	void nextPage() {
		if (hasNextPage())
			navTo(curPageIdx + 1);
	}

	void priorPage() {
		if (hasPriorPage())
			navTo(history.pop(), false);
	}

	boolean hasNextPage() {
		return (curPageIdx < pages.size() - 1);
	}

	boolean hasPriorPage() {
		return !history.isEmpty();
	}

	void navTo(int nextPageIdx, boolean pushHistory) {
		if (nextPageIdx < 0 || nextPageIdx >= pages.size())
			return;
		if (curPageIdx != UNDEFINED && pushHistory)
			history.push(curPageIdx);

		WizardPage nextPage = pages.get(nextPageIdx);
		curPageIdx = nextPageIdx;
		getChildren().clear();
		getChildren().add(nextPage);
		nextPage.manageButtons();
	}

	void navTo(int nextPageIdx) {
		navTo(nextPageIdx, true);
	}

	void navTo(String id) {
		if (id != null)
			pages.stream().filter(page -> id.equals(page.getId())).findFirst()
					.ifPresent(page -> navTo(pages.indexOf(page)));
	}

	public void finish() {
	}

	public void cancel() {
	}
}

/**
 * basic wizard page class
 */
abstract class WizardPage extends VBox {
	Button priorButton = new Button("_Previous");
	Button nextButton = new Button("N_ext");
	Button cancelButton = new Button("Cancel");
	Button finishButton = new Button("_Finish");

	WizardPage(String title) {
		Label label = new Label(title);
		label.setStyle("-fx-font-weight: bold; -fx-padding: 0 0 5 0;");
		setId(title);
		setSpacing(5);
		setStyle(
				"-fx-padding:10; -fx-background-color: honeydew; -fx-border-color: derive(honeydew, -30%); -fx-border-width: 3;");

		Region spring = new Region();
		VBox.setVgrow(spring, Priority.ALWAYS);
		getChildren().addAll(getContent(), spring, getButtons());

		priorButton.setOnAction(event -> priorPage());
		nextButton.setOnAction(event -> nextPage());
		cancelButton.setOnAction(event -> getWizard().cancel());
		finishButton.setOnAction(event -> getWizard().finish());
	}

	HBox getButtons() {
		Region spring = new Region();
		HBox.setHgrow(spring, Priority.ALWAYS);
		HBox buttonBar = new HBox(5);
		cancelButton.setCancelButton(true);
		finishButton.setDefaultButton(true);
		buttonBar.getChildren().addAll(spring, priorButton, nextButton, cancelButton, finishButton);
		return buttonBar;
	}

	abstract Parent getContent();

	boolean hasNextPage() {
		return getWizard().hasNextPage();
	}

	boolean hasPriorPage() {
		return getWizard().hasPriorPage();
	}

	void nextPage() {
		getWizard().nextPage();
	}

	void priorPage() {
		getWizard().priorPage();
	}

	void navTo(String id) {
		getWizard().navTo(id);
	}

	Wizard getWizard() {
		return (Wizard) getParent();
	}

	public void manageButtons() {
		if (!hasPriorPage())
			priorButton.setDisable(true);

		if (!hasNextPage())
			nextButton.setDisable(true);
	}
}

/**
 * This class shows a satisfaction survey
 */
class SurveyWizard extends Wizard {
	Stage owner;

	public SurveyWizard(Stage owner) {
		super(new UserNameScreen(), new QuestionScreen(), new GettingPasswordScreen());
		this.owner = owner;
	}

	@Override
	public void finish() {
		System.out.println("Had complaint? " + SurveyData.instance.hasComplaints.get());
		if (SurveyData.instance.hasComplaints.get())
			System.out.println("Complaints: " + (SurveyData.instance.complaints.get().isEmpty() ? "No Details"
					: "\n" + SurveyData.instance.complaints.get()));
		owner.close();
	}

	@Override
	public void cancel() {
		System.out.println("Cancelled");
		owner.close();
	}
}

/**
 * Simple placeholder class for the customer entered survey response.
 */
class SurveyData {
	BooleanProperty hasComplaints = new SimpleBooleanProperty();
	StringProperty complaints = new SimpleStringProperty();
	static SurveyData instance = new SurveyData();
}

/**
 * This class determines if the user has complaints. If not, it jumps to the
 * last page of the wizard.
 */
class UserNameScreen extends WizardPage {

	private JFXTextField usernameField;

	public UserNameScreen() {
		super("UserNameScreen");
		
		nextButton.setDisable(true);
		finishButton.setDisable(true);

	}

	@Override
	Parent getContent() {
		usernameField = new JFXTextField();

		usernameField.textProperty().addListener((observable, oldValue, newValue) -> nextButton.setDisable(newValue.isEmpty()));

		VBox vbox = new VBox(5, new Label("Enter Username"), usernameField);
		vbox.setAlignment(Pos.CENTER);
		return vbox;

	}

	@Override
	void nextPage() {
		try {
			ForgetPasswordUtil.question = ForgetPasswordUtil.forgotPasswordHandler.getForgotPasswordQuestion(usernameField.getText());
		} catch (NoSuchUserName e) {
			e.showInfoToUser();
			ForgetPasswordUtil.log.fatal(e);
			ForgetPasswordUtil.log.debug(StackTraceUtil.getStackTrace(e));
			return;
		}
		super.nextPage();
	}
}

/**
 * This page gathers more information about the complaint
 */
class QuestionScreen extends WizardPage {
	public QuestionScreen() {
		super("QuestionScreen");
	}

	private JFXTextField answerField;
	private JFXPasswordField newPassword;
	private Label question;

	@Override
	Parent getContent() {
		nextButton.setDisable(true);
		finishButton.setDisable(true);

		answerField = new JFXTextField();
		newPassword = new JFXPasswordField();
		answerField.textProperty().addListener((observable, oldValue, newValue) -> enableNext());
		
		newPassword.textProperty().addListener((observable, oldValue, newValue) -> enableNext());

		question = new Label(ForgetPasswordUtil.question);

		VBox vbox = new VBox(5, new Label("Please answer the following question:"), question, answerField, new Label("Choose new password:"), newPassword);
		vbox.setAlignment(Pos.CENTER);

		return vbox;
	}
	
	private void enableNext() {
		nextButton.setDisable(answerField.getText().isEmpty() || newPassword.getText().isEmpty());
	}
	
	@Override
	void nextPage() {
		try {
			ForgetPasswordUtil.forgotPasswordHandler.sendAnswerAndNewPassword(answerField.getText(), newPassword.getText());
		} catch (WrongAnswer | NoSuchUserName e) {
			e.showInfoToUser();
			ForgetPasswordUtil.log.fatal(e.toString());
			ForgetPasswordUtil.log.debug(StackTraceUtil.getStackTrace(e));
			return;
		}
		super.nextPage();
	}
	
}

/**
 * This page thanks the user for taking the survey
 */
class GettingPasswordScreen extends WizardPage {
	public GettingPasswordScreen() {
		super("GettingPasswordScreen");
	}
	@Override
	Parent getContent() {
		VBox vbox = new VBox(5, new Label("Thanks!\nPassword restored successfully!"));
		vbox.setAlignment(Pos.CENTER);

		return vbox;
	}
}
