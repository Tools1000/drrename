package com.github.drrename.ui.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.drrename.RenamingService;
import com.github.drrename.model.ImageDateRenamingStrategy;
import com.github.drrename.model.RenamingStrategy;
import com.github.drrename.model.SimpleReplaceRenamingStrategy;
import com.github.drrename.model.ToLowerCaseRenamingStrategy;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class MainController implements Initializable {

    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    static List<RenamingStrategy> getRenamingStrategies() {
	final List<RenamingStrategy> result = new ArrayList<>();
	result.add(new SimpleReplaceRenamingStrategy());
	result.add(new ImageDateRenamingStrategy());
	result.add(new ToLowerCaseRenamingStrategy());

	return result;
    }

    private final RenamingService sr = new RenamingService();

    @FXML
    private CheckBox checkBox;
    @FXML
    private ComboBox<RenamingStrategy> comboBoxRenamingStrategy;

    @FXML
    private TextField textFieldReplacementStringFrom;

    @FXML
    private TextField textFieldReplacementStringTo;

    @FXML
    private TextField textFieldStartDirectory;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Button buttonGo;

    @FXML
    private VBox tilePane;

    private boolean working;

    @FXML
    private void handleButtonActionGo(final ActionEvent event) {

	if (working) {
	    buttonGo.setText("Cancelling");
	    buttonGo.setDisable(true);
	    sr.cancel();
	} else {
	    sr.reset();
	    sr.start();
	}
    }

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
	comboBoxRenamingStrategy.setItems(FXCollections.observableArrayList(getRenamingStrategies()));
	comboBoxRenamingStrategy.getSelectionModel().selectFirst();
	comboBoxRenamingStrategy.getSelectionModel().selectedItemProperty()
		.addListener(new ChangeListener<RenamingStrategy>() {

		    @Override
		    public void changed(final ObservableValue<? extends RenamingStrategy> observable,
			    final RenamingStrategy oldValue, final RenamingStrategy newValue) {
			textFieldReplacementStringFrom.setDisable(!newValue.isReplacing());
			textFieldReplacementStringTo.setDisable(!newValue.isReplacing());
		    }
		});
	sr.renamingStrategyProperty().bind(comboBoxRenamingStrategy.valueProperty());
	sr.replacementStringFromProperty().bind(textFieldReplacementStringFrom.textProperty());
	sr.replacementStringToProperty().bind(textFieldReplacementStringTo.textProperty());
	sr.pathProperty().bind(textFieldStartDirectory.textProperty());
	sr.recursiveProperty().bind(checkBox.selectedProperty());
	sr.setPane(tilePane);
	progressBar.progressProperty().bind(sr.progressProperty());
	sr.setOnFailed(e -> {
	    if (logger.isErrorEnabled()) {
		logger.error(sr.getException().getLocalizedMessage(), sr.getException());
	    }
	    setWorking(false);
	});
	sr.setOnCancelled(e -> setWorking(false));
	sr.setOnRunning(e -> {
	    setWorking(true);
	    tilePane.getChildren().clear();
	});
	sr.setOnSucceeded(e -> setWorking(false));
    }

    void setWorking(final boolean working) {
	Platform.runLater(() -> setWorkingFX(working));

    }

    private void setWorkingFX(final boolean working) {
	buttonGo.setDisable(false);
	checkBox.setDisable(working);
	textFieldReplacementStringFrom.setDisable(working);
	textFieldReplacementStringTo.setDisable(working);
	textFieldStartDirectory.setDisable(working);
	comboBoxRenamingStrategy.setDisable(working);
	if (working) {
	    buttonGo.setText("Cancel");
	} else {
	    buttonGo.setText("Go");
	}
	progressBar.setVisible(working);
	this.working = working;

    }

}
