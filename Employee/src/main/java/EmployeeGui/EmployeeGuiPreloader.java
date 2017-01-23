package EmployeeGui;

import javafx.application.Platform;
import javafx.application.Preloader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class EmployeeGuiPreloader extends Preloader {
	private static final double WIDTH = 200;
	private static final double HEIGHT = 200;

	private Stage preloaderStage;
	private Scene scene;

	private ProgressBar progress;

	public EmployeeGuiPreloader() {
	    // Constructor is called before everything.
	    System.out.println(EmployeeApplicationScreen.STEP() + "EmployeeGuiPreloader constructor called, thread: " + Thread.currentThread().getName());
	}

	@Override
	public void init() throws Exception {
		System.out.println(
				EmployeeApplicationScreen.STEP() + "EmployeeGuiPreloader#init, thread: "
						+ Thread.currentThread().getName());

		// If preloader has complex UI it's initialization can be done in
		// EmployeeApplicationScreen#init
		Platform.runLater(() -> {
			Label title = new Label("SmartMarket is loading\nplease wait...");
			title.setTextAlignment(TextAlignment.CENTER);
			progress = new ProgressBar();
			progress.setProgress(0);
			
			VBox root = new VBox(title, progress);
			root.setAlignment(Pos.CENTER);

			scene = new Scene(root, WIDTH, HEIGHT);
		});
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		System.out.println(EmployeeApplicationScreen.STEP() + "EmployeeApplicationScreen#start (showing preloader stage), thread: "
				+ Thread.currentThread().getName());

		this.preloaderStage = primaryStage;

		// Set preloader scene and show stage.
		preloaderStage.setScene(scene);
		preloaderStage.show();
	}

	@Override
	public void handleApplicationNotification(PreloaderNotification info) {
		// Handle application notification in this point (see
		// EmployeeApplicationScreen#init).
		if (info instanceof ProgressNotification)
			progress.setProgress(((ProgressNotification) info).getProgress());
	}

	@Override
	public void handleStateChangeNotification(StateChangeNotification info) {
		switch (info.getType()) {
		case BEFORE_LOAD:
			System.out.println(EmployeeApplicationScreen.STEP() + "BEFORE_LOAD");
			break;
		case BEFORE_INIT:
			System.out.println(EmployeeApplicationScreen.STEP() + "BEFORE_INIT");
			break;
		case BEFORE_START:
			System.out.println(EmployeeApplicationScreen.STEP() + "BEFORE_START");
			preloaderStage.hide();
			break;
		}
	}
}