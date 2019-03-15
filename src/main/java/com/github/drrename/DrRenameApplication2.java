package com.github.drrename;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.drrename.event.AvailableRenamingStrategyEvent;
import com.github.drrename.strategy.ExtensionByMimeStrategy;
import com.github.drrename.strategy.MediaMetadataRenamingStrategy;
import com.github.drrename.strategy.MultiWhiteSpaceRemoverStrategy;
import com.github.drrename.strategy.RegexReplaceRenamingStrategy;
import com.github.drrename.strategy.RenamingStrategy;
import com.github.drrename.strategy.SimpleReplaceRenamingStrategy;
import com.github.drrename.strategy.ToLowerCaseRenamingStrategy;
import com.github.drrename.ui.MainController2;
import com.github.events1000.api.Events;
import com.github.ktools1000.AnotherThreadFactory;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Alexander Kerner
 *
 */
public class DrRenameApplication2 extends Application {

    private final static Logger logger = LoggerFactory.getLogger(DrRenameApplication2.class);

    public static ExecutorService LOW_PRIORITY_THREAD_POOL_EXECUTOR = Executors.newFixedThreadPool(1,
	    new AnotherThreadFactory(Thread.MIN_PRIORITY));

    public static void main(final String[] args) {

	launch(args);
    }

    @Override
    public void start(final Stage stage) throws Exception {

	if (logger.isInfoEnabled()) {
	    logger.info("Application version " + getClass().getPackage().getImplementationVersion());
	}
	final FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainView2.fxml"));
	final MainController2 controller = loader.getController();

	final Parent root = loader.load();
	final Scene scene = new Scene(root, 600, 600);
	String s = getClass().getPackage().getImplementationVersion();
	if (s == null) {
	    s = "dev version";
	}
	stage.setTitle("Dr.Rename " + s);
	stage.setScene(scene);
	stage.show();
	fireInitEvents();
    }

    @Override
    public void stop() throws Exception {
	LOW_PRIORITY_THREAD_POOL_EXECUTOR.shutdown();
	super.stop();
    }

    private void fireInitEvents() {

	// notify the UI about available renaming strategies
	getRenamingStrategies().forEach(s -> fireAvailableStrategyEvent(s));
    }

    private void fireAvailableStrategyEvent(final RenamingStrategy s) {

	Events.getInstance().emit(new AvailableRenamingStrategyEvent(s));
    }

    static List<RenamingStrategy> getRenamingStrategies() {

	final List<RenamingStrategy> result = new ArrayList<>();
	result.add(new SimpleReplaceRenamingStrategy());
	result.add(new RegexReplaceRenamingStrategy());
	result.add(new ToLowerCaseRenamingStrategy());
	result.add(new MediaMetadataRenamingStrategy());
	result.add(new MultiWhiteSpaceRemoverStrategy());
	result.add(new ExtensionByMimeStrategy());
	return result;
    }
}
