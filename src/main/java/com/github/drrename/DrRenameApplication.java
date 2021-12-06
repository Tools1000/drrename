package com.github.drrename;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class DrRenameApplication extends Application {

    private final static Logger logger = LoggerFactory.getLogger(DrRenameApplication.class);

    public static void main(final String[] args) {
	launch(args);

    }

    @Override
    public void start(final Stage stage) throws Exception {
	if (logger.isInfoEnabled()) {
	    logger.info("Application version " + getClass().getPackage().getImplementationVersion());
	}
	final FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainView.fxml"));
	final Parent root = loader.load();
	final Scene scene = new Scene(root, 400, 300);
	String s = getClass().getPackage().getImplementationVersion();
	if (s == null) {
	    s = "dev version";
	}
	stage.setTitle("Dr.Rename " + s);
	stage.setScene(scene);
	stage.show();

    }

}
