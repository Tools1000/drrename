package com.github.drrename;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TestApplication extends Application {

	public static void main(final String[] args) {

		launch(args);
	}

	@Override
	public void start(final Stage stage) throws Exception {

		final Scene scene = new Scene(buildContent(), 600, 600);
		stage.setScene(scene);
		stage.show();
	}

	private Parent buildContent() {

		final VBox b1 = new VBox();
		final SplitPane sp1 = new SplitPane();
		b1.getChildren().add(sp1);
		final VBox b2 = new VBox();
		final VBox b3 = new VBox();
		sp1.getItems().addAll(b2, b3);
		final ScrollPane sp2 = new ScrollPane();
		sp2.setHbarPolicy(ScrollBarPolicy.ALWAYS);
		final ScrollPane sp3 = new ScrollPane();
		sp3.setHbarPolicy(ScrollBarPolicy.ALWAYS);
		b2.getChildren().add(sp2);
		b3.getChildren().add(sp3);
		return b1;
	}
}
