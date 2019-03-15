package com.github.drrename.ui;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.drrename.DrRenameApplication2;
import com.github.drrename.event.AvailableRenamingStrategyEvent;
import com.github.drrename.strategy.RenamingStrategy;
import com.github.events1000.api.Event;
import com.github.events1000.api.Events;
import com.github.events1000.listener.api.AbstractSynchronousEventListener;
import com.github.events1000.listener.api.SynchronousEventListener;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.concurrent.Worker.State;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import net.sf.kerner.utils.pair.PairSame;
import net.sf.kerner.utils.pair.PairSameImpl;

public class MainController2 implements Initializable {

    private static final Logger logger = LoggerFactory.getLogger(MainController2.class);
    @FXML
    private TextField textFieldStartDirectory;
    @FXML
    private TextField textFieldFileFilter;
    @FXML
    private TextField textFieldReplacementStringFrom;
    @FXML
    private TextField textFieldReplacementStringTo;
    private final RenamingService2 renamingService;
    private final PreviewService previewService;
    private final ListFilesService listFilesService;
    // private final FilterService filterService;
    @FXML
    private ListView<Control> content1;
    @FXML
    private ListView<Control> content2;
    @FXML
    private Pane workingCover;
    private ChangeListener<? super String> textFieldChangeListener;
    private ChangeListener<? super String> replaceStringFromChangeListener;
    private ChangeListener<? super String> replaceStringToChangeListener;
    private ChangeListener<? super Object> updateViewListener;
    private List<RenamingBean> entries;
    @FXML
    private ComboBox<RenamingStrategy> comboBoxRenamingStrategy;
    @FXML
    private Button buttonGo;
    private final BooleanProperty workingProperty;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Label additionalParamDescription;
    @FXML
    private TextField additionalParamValue;
    private String currentInputString;
    private static final Color vColor = Color.web("#f5f5f5");
    private final SynchronousEventListener newStrategyAvailableListener = new AbstractSynchronousEventListener(
	    AvailableRenamingStrategyEvent.EVENT_TOPIC) {

	@Override
	public void handle(final Event e) {

	    if (e instanceof AvailableRenamingStrategyEvent) {
		comboBoxRenamingStrategy.getItems().add(((AvailableRenamingStrategyEvent) e).getData());
		comboBoxRenamingStrategy.getSelectionModel().selectFirst();
	    }
	}
    };
    // private final SynchronousEventListener newFilesEntryListener = new
    // AbstractSynchronousEventListener(NewFileEntryEvent.EVENT_TOPIC) {
    //
    // @Override
    // public void handle(final Event e) {
    //
    // if(e instanceof NewFileEntryEvent) {
    // final PairSame<TextField> files = ((NewFileEntryEvent)e).getData();
    // try {
    // // mimic some niceness
    // Thread.sleep(40);
    // } catch(final InterruptedException e1) {
    // // ignore
    // }
    // Platform.runLater(() -> {
    // content1.getItems().add(files.getFirst());
    // content2.getItems().add(files.getSecond());
    // });
    // }
    // }
    // };
    private ChangeListener<? super String> textFieldFileFilterChangeListener;

    public MainController2() {

	renamingService = new RenamingService2();
	previewService = new PreviewService(DrRenameApplication2.LOW_PRIORITY_THREAD_POOL_EXECUTOR);
	listFilesService = new ListFilesService();
	workingProperty = new SimpleBooleanProperty();
	// filterService = new FilterService();
	entries = new ArrayList<>();
	init();
    }

    // private void startFilterServce() {
    // startService(filterService);

    // }

    private void init() {

	Events.getInstance().registerListener(newStrategyAvailableListener);
	// Events.getInstance().registerListener(newFilesEntryListener);
	textFieldChangeListener = (e, o, n) -> {
	    currentInputString = n;
	    Platform.runLater(() -> {
		updateInputView(textFieldStartDirectory.getText());
		updateOutputView();
	    });
	};
	textFieldFileFilterChangeListener = (v, o, n) -> {
	    updateInputView(textFieldStartDirectory.getText());
	};
	replaceStringFromChangeListener = (e, o, n) -> Platform.runLater(() -> updateOutputView());
	replaceStringToChangeListener = (e, o, n) -> Platform.runLater(() -> updateOutputView());
	updateViewListener = (e, o, n) -> Platform.runLater(() -> updateOutputView());
	listFilesService.runningProperty().addListener((e, o, n) -> {
	    setWorking(n);
	});
	// previewService.runningProperty().addListener((e, o, n) -> {
	// setWorking(n);
	// });
	renamingService.runningProperty().addListener((e, o, n) -> {
	    setWorking(n);
	});
	renamingService.setOnSucceeded(e -> {
	    startService(previewService);
	});
    }

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {

	textFieldStartDirectory.textProperty().addListener(textFieldChangeListener);
	textFieldReplacementStringFrom.textProperty().addListener(replaceStringFromChangeListener);
	textFieldReplacementStringTo.textProperty().addListener(replaceStringToChangeListener);
	additionalParamValue.textProperty().addListener(updateViewListener);
	comboBoxRenamingStrategy.getSelectionModel().selectedItemProperty()
		.addListener(new ChangeListener<RenamingStrategy>() {

		    @Override
		    public void changed(final ObservableValue<? extends RenamingStrategy> observable,
			    final RenamingStrategy oldValue, final RenamingStrategy newValue) {

			if (newValue != null) {
			    textFieldReplacementStringFrom.setDisable(!newValue.isReplacing());
			    textFieldReplacementStringTo.setDisable(!newValue.isReplacing());
			}
			if ((oldValue != null) && oldValue.hasAdditionalParam()) {
			    additionalParamDescription.setVisible(false);
			    additionalParamValue.setVisible(false);
			    oldValue.getAdditionalParam().get().valueProperty().unbind();
			}
			if ((newValue != null) && newValue.hasAdditionalParam()) {
			    additionalParamDescription.setVisible(true);
			    additionalParamValue.setVisible(true);
			    final String s = newValue.getAdditionalParam().get().getDescriptionShort();
			    // additionalParamDescription.setText(s.length() >= 10 ? s.substring(0, 10) :
			    // s);
			    additionalParamDescription.setText(s);
			    additionalParamDescription
				    .setTooltip(new Tooltip(newValue.getAdditionalParam().get().getDescriptionLong()));
			    newValue.getAdditionalParam().get().valueProperty()
				    .bindBidirectional(additionalParamValue.textProperty());
			}
			updateOutputView();
		    }
		});
	progressBar.progressProperty().bind(renamingService.progressProperty());
	renamingService.setOnFailed(e -> {
	    if (logger.isErrorEnabled()) {
		logger.error(renamingService.getException().getLocalizedMessage(), renamingService.getException());
	    }
	});
	Platform.runLater(() -> {
	    getListViewScrollBar(content1).valueProperty()
		    .bindBidirectional(getListViewScrollBar(content2).valueProperty());
	});
	textFieldFileFilter.textProperty().addListener(textFieldFileFilterChangeListener);

	// content1.prefWidthProperty().bind(scroll1.widthProperty());
	// content2.prefWidthProperty().bind(scroll2.widthProperty());
	// scrollPane.prefHeightProperty().bind(contentPane.heightProperty());
	// content1.setOnDragDetected(new EventHandler<MouseEvent>() {
	// @Override
	// public void handle(final MouseEvent event) {
	// /* drag was detected, start a drag-and-drop gesture */
	// /* allow any transfer mode */
	// final Dragboard db = content1.startDragAndDrop(TransferMode.ANY);
	//
	// System.err.println(db.getFiles());
	//
	// event.consume();
	// }
	// });
	content1.setOnDragOver(new EventHandler<DragEvent>() {
	    @Override
	    public void handle(final DragEvent event) {
		if ((event.getGestureSource() != content1) && event.getDragboard().hasFiles()) {
		    /* allow for both copying and moving, whatever user chooses */
		    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
		}
		event.consume();
	    }
	});
	// content1.setOnDragEntered(new EventHandler<DragEvent>() {
	// @Override
	// public void handle(final DragEvent event) {
	// System.err.println("Drag entered " + event);
	// event.consume();
	// }
	// });
	content1.setOnDragDropped(new EventHandler<DragEvent>() {
	    @Override
	    public void handle(final DragEvent event) {
		final Dragboard db = event.getDragboard();
		boolean success = false;
		if (db.hasFiles()) {
		    updateInputView(db.getFiles());
		    success = true;
		}
		/*
		 * let the source know whether the string was successfully transferred and used
		 */
		event.setDropCompleted(success);

		event.consume();
	    }
	});
	// content1.setOnDragDone(new EventHandler<DragEvent>() {
	// @Override
	// public void handle(final DragEvent event) {
	// System.err.println("Drag done " + event);
	// event.consume();
	// }
	// });

	// content1.getStyleClass().remove("list-cell");
	// content1.setStyle("-fx-padding: 0px;");
    }

    private void setWorking(final boolean n) {

	workingProperty.set(n);
	// workingCover.setVisible(n);
	comboBoxRenamingStrategy.setDisable(n);
	textFieldReplacementStringFrom.setDisable(n);
	textFieldReplacementStringTo.setDisable(n);
	buttonGo.setText(n ? "Cancel" : "Go");
    }

    private void updateInputView(final String rootDirectory) {

	cancelCurrentOperation();
	clearView();
	initListFilesService(rootDirectory);
	startService(listFilesService);
    }

    private void updateInputView(final List<File> singleFiles) {

	cancelCurrentOperation();
	clearView();
	initListFilesService(singleFiles);
	startService(listFilesService);
    }

    private void initListFilesService(final String rootDirectory) {

	listFilesService.setPath(Paths.get(rootDirectory));
	initListFilesService();
    }

    private void initListFilesService(final List<File> singleFiles) {

	listFilesService.setFiles(singleFiles);
	initListFilesService();
    }

    private void initListFilesService() {

	listFilesService.setFileNameFilterRegex(textFieldFileFilter.getText().trim());
	listFilesService.setOnFailed(e -> {
	    listFilesService.getException().printStackTrace();
	});
	listFilesService.setOnSucceeded(buildNewListFilesSucceededCallback());
    }

    private EventHandler<WorkerStateEvent> buildNewListFilesSucceededCallback() {

	return e -> {
	    entries = listFilesService.getValue();
	    // System.err.println(entries);
	    if (entries != null) {
		for (final RenamingBean b : entries) {
		    addToContent(b);
		}
		updateOutputView();
	    }
	    // startService(filterService);
	};
    }

    private void clearView() {

	entries = new ArrayList<>();
	content1.getItems().clear();
	content2.getItems().clear();
    }

    private void cancelCurrentOperation() {
	cancelCurrentOperation(null);
    }

    private void cancelCurrentOperation(final Runnable callback) {

	final CountDownLatch c = new CountDownLatch(3);

	renamingService.setOnCancelled(e -> {
	    c.countDown();
	});
	// filterService.setOnCancelled(e -> {
	// c.countDown();
	// });
	previewService.setOnCancelled(e -> {
	    c.countDown();
	});
	listFilesService.setOnCancelled(e -> {
	    c.countDown();
	});

	renamingService.cancel();
	// filterService.cancel();
	previewService.cancel();
	listFilesService.cancel();

	if (callback != null) {
	    try {
		c.await();
		callback.run();
	    } catch (final InterruptedException e1) {
		e1.printStackTrace();
	    }
	}
    }

    private void addToContent(final RenamingBean b) {

	final PairSame<Control> entry = buildRenameEntryNode(b);
	content1.getItems().add(entry.getFirst());
	content2.getItems().add(entry.getSecond());
    }

    private void updateOutputView() {

	startService(previewService);
    }
    // private Callable<String> calcValue2(final RenamingBean f) {
    //
    // return () -> {
    // if(!f.getOldPath().getFileName().toString().equals(f.getNewPath().get()))
    // return "-fx-background-color: orange;";
    // return null;
    // };
    // }
    // private void restoreService(final Service<?> service, final Runnable
    // callback) {
    //
    // service.setOnReady(e2 -> {
    // if(callback != null) {
    // callback.run();
    // }
    // });
    // service.setOnCancelled(e1 -> {
    // service.reset();
    // });
    // if(!service.cancel()) {
    // service.reset();
    // if(callback != null) {
    // callback.run();
    // }
    // }
    // }
    // private void restoreService(final FilesService<?> service, final Runnable
    // callback) {
    //
    // restoreService((Service<?>)service, () -> {
    // service.setFiles(entries);
    // if(callback != null) {
    // callback.run();
    // }
    // });
    // }
    // private void restoreService(final StrategyService<?> service, final Runnable
    // callback) {
    //
    // restoreService((FilesService<?>)service, () -> {
    // final Optional<RenamingStrategy> s = initAndGetStrategy();
    // if(s.isPresent()) {
    // service.setRenamingStrategy(s.get());
    // if(callback != null) {
    // callback.run();
    // }
    // }
    // });
    // }
    // private void restartService(final StrategyService<?> service, final Runnable
    // callback) {
    //
    // restoreService(service, () -> {
    // if(callback != null) {
    // callback.run();
    // }
    // service.start();
    // });
    // }
    // private void restartService(final FilesService<?> service, final Runnable
    // callback) {
    //
    // restoreService(service, () -> {
    // if(callback != null) {
    // callback.run();
    // }
    // service.start();
    // });
    // }

    // private void restartService(final Service<?> service, final Runnable
    // callback) {
    //
    // restoreService(service, () -> {
    // callback.run();
    // service.start();
    // });
    // }
    private Optional<RenamingStrategy> initAndGetStrategy() {

	final RenamingStrategy strategy = comboBoxRenamingStrategy.getSelectionModel().getSelectedItem();
	if (strategy == null)
	    return Optional.empty();
	strategy.setReplacementStringFrom(textFieldReplacementStringFrom.getText());
	strategy.setReplacementStringTo(textFieldReplacementStringTo.getText());
	return Optional.of(strategy);
    }

    static PairSame<Control> buildRenameEntryNode(final RenamingBean f) {

	final Control tLeft = buildLeft(f);
	final Control tRight = buildRight(f);
	// final Animation animation = buildAnimation(f, tLeft, tRight);
	// tRight.textProperty().addListener((e, o, n) -> {
	// animation.play();
	// });
	return new PairSameImpl<>(tLeft, tRight);
    }

    static Control buildLeft(final RenamingBean f) {

	final Label tLeft = new Label();
	tLeft.setPadding(new Insets(2, 2, 2, 2));
	tLeft.textProperty().bind(Bindings.createObjectBinding(() -> calcJohn(f), f.oldPathProperty()));
	// tLeft.setPrefWidth(TextUtils.computeTextWidth(tLeft.getFont(),
	// tLeft.getText(), 0.0D) + 40); // due to paddings, insets, n stuff, we need a
	// little bit more space
	// tLeft.setStyle("-fx-text-box-border: transparent; -fx-background-color:
	// -fx-control-inner-background; -fx-background-insets: 0;");
	// tLeft.setEditable(false);
	// tLeft.setBackground(new Background(new BackgroundFill(vColor,
	// CornerRadii.EMPTY, Insets.EMPTY)));
	// f.filteredProperty().addListener((v, o, n) -> {
	// tLeft.setStyle(calcStyle(f));
	// });
	tLeft.styleProperty().bind(Bindings.createObjectBinding(() -> calcStyleLeft(f), f.filteredProperty()));

	return tLeft;
    }

    private static String calcJohn(final RenamingBean f) {
	return f.getOldPath().getFileName().toString();
    }

    static Control buildRight(final RenamingBean f) {

	final Label tRight = new Label();
	tRight.setPadding(new Insets(2, 2, 2, 2));
	tRight.setMaxWidth(Double.POSITIVE_INFINITY);
	// tRight.setStyle("-fx-text-box-border: transparent;");
	// tRight.setEditable(false);
	// tRight.textProperty().addListener(new ChangeListener<String>() {
	//
	// @Override
	// public void changed(final ObservableValue<? extends String> ob, final String
	// o, final String n) {
	//
	// final double w = TextUtils.computeTextWidth(tRight.getFont(), n, 0.0D) + 40;
	// // due to paddings, insets, n stuff, we need a little bit more space
	// tRight.setPrefWidth(w);
	// }
	// });
	// tRight.setBackground(new Background(new BackgroundFill(vColor,
	// CornerRadii.EMPTY, Insets.EMPTY)));
	tRight.textProperty().bind(Bindings.createStringBinding(getNewPath(f), f.getException(), f.getNewPath()));
	tRight.styleProperty().bind(
		Bindings.createObjectBinding(() -> calcStyleRight(f), f.changingProperty(), f.filteredProperty()));
	return tRight;
    }

    private static String calcStyleLeft(final RenamingBean f) {
	final StringBuilder sb = new StringBuilder();

	if (f.isFiltered()) {
	    sb.append(filteredStyle());
	}

	if (sb.toString().length() > 0)
	    return sb.toString();

	return defaultStyle();

    }

    private static String calcStyleRight(final RenamingBean f) {
	final StringBuilder sb = new StringBuilder();

	if (f.isFiltered()) {
	    sb.append(filteredStyle());
	}
	if (f.isChanging()) {
	    sb.append(changingStyle());
	}

	if (sb.toString().length() > 0)
	    return sb.toString();

	return defaultStyle();

    }

    private static String defaultStyle() {
	// System.err.println("Return default style");
	return null;
    }

    private static String filteredStyle() {
	// System.err.println("Return filtered style");
	// return "-fx-background-color: linear-gradient(#E4EAA2, #9CD672);";
	return "-fx-text-fill: #a9a9a9;";
    }

    private static String changingStyle() {
	// System.err.println("Return filtered style");
	// return "-fx-background-color: linear-gradient(#E4EAA2, #9CD672);";
	return "-fx-background-color: limegreen; -fx-font-weight: bold;";
	// return "-fx-text-fill: #a9a9a9;";
    }

    static Callable<String> getNewPath(final RenamingBean f) {

	return () -> {
	    if (f.getException().get() != null)
		return f.getException().get().getLocalizedMessage();
	    return f.getNewPath().getValue() == null ? null : f.getNewPath().getValue();
	};
    }

    private static Animation buildAnimation(final RenamingBean f, final TextField tLeft, final TextField tRight) {

	final Animation animation;
	if (!f.getOldPath().getFileName().toString().equals(f.getNewPath().get())) {
	    animation = buildNewNameAnimation(tRight);
	} else {
	    animation = buildResetAnimation(tLeft);
	}
	return animation;
    }

    static Animation buildResetAnimation(final Region tRight) {

	return new Transition() {

	    {
		setCycleDuration(Duration.millis(2000));
		setInterpolator(Interpolator.EASE_IN);
	    }

	    @Override
	    protected void interpolate(final double frac) {

		final Color vColor = new Color(0.60, 0.90, 0.60, 1 - frac);
		tRight.setBackground(new Background(new BackgroundFill(vColor, CornerRadii.EMPTY, Insets.EMPTY)));
	    }
	};
    }

    static Animation buildNewNameAnimation(final Region tRight) {

	return new Transition() {

	    {
		setCycleDuration(Duration.millis(2000));
		setInterpolator(Interpolator.EASE_OUT);
	    }

	    @Override
	    protected void interpolate(final double frac) {

		final Color vColor = new Color(0.60, 0.90, 0.60, frac);
		tRight.setBackground(new Background(new BackgroundFill(vColor, CornerRadii.EMPTY, Insets.EMPTY)));
	    }
	};
    }

    private ScrollBar getListViewScrollBar(final ListView<?> listView) {

	ScrollBar scrollbar = null;
	for (final Node node : listView.lookupAll(".scroll-bar")) {
	    if (node instanceof ScrollBar) {
		final ScrollBar bar = (ScrollBar) node;
		if (bar.getOrientation().equals(Orientation.VERTICAL)) {
		    scrollbar = bar;
		}
	    }
	}
	return scrollbar;
    }

    private List<RenamingStrategy> getAvailableRenamingStrategies() {

	final List<RenamingStrategy> result = new ArrayList<>();
	// check the event history if strategies have been published already
	Events.getInstance().getHistory().forEach(e -> {
	    if (e instanceof AvailableRenamingStrategyEvent) {
		result.add(((AvailableRenamingStrategyEvent) e).getData());
	    }
	});
	return result;
    }

    @FXML
    private void handleMenuItemRegexTips(final ActionEvent event) {

	try {
	    final FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/RegexTipsView.fxml"));
	    final Parent root = loader.load();
	    final Stage stage = new Stage();
	    final Scene scene = new Scene(root);
	    stage.setTitle("Regex Tips");
	    stage.setScene(scene);
	    stage.setWidth(400);
	    stage.setHeight(400);
	    stage.show();
	} catch (final IOException e) {
	    logger.error(e.getLocalizedMessage(), e);
	}
    }

    private void startService(final Service<?> service) {

	if ((service.getState() == State.RUNNING) || (service.getState() == State.SCHEDULED)) {
	    if (logger.isDebugEnabled()) {
		logger.debug("Still busy, waiting for cancel " + service);
	    }
	    service.setOnCancelled(e -> {
		if (logger.isDebugEnabled()) {
		    logger.debug("Cancelled " + service);
		}
		startService(service);
	    });
	    service.cancel();
	    return;
	}
	entries.stream().forEach(e -> e.getException().set(null));
	try {
	    service.reset();
	    if (service instanceof FilesService<?>) {
		// System.err.println("Setting files " + entries);
		((FilesService<?>) service).setFiles(entries);
	    }
	    if (service instanceof StrategyService<?>) {
		final Optional<RenamingStrategy> so = initAndGetStrategy();
		if (so.isPresent()) {
		    ((StrategyService<?>) service).setRenamingStrategy(so.get());
		} else
		    return;
	    }
	    if (service instanceof FilterService) {
		((FilterService) service).setFileNameFilterRegex(textFieldFileFilter.getText().trim());
	    }
	    service.setOnRunning(e -> {
		if (logger.isDebugEnabled()) {
		    logger.debug("Service running " + service);
		}
	    });
	    service.setOnFailed(e -> {
		if (logger.isDebugEnabled()) {
		    logger.debug("Service failed " + service + ": " + service.getException(), service.getException());
		}
	    });
	    service.start();

	} catch (final IllegalStateException e) {
	    if (logger.isDebugEnabled()) {
		logger.debug("Cannot start service " + e.toString() + ", " + service + ", " + service.getState());
	    }
	}
    }

    @FXML
    private void handleButtonActionGo(final ActionEvent event) {

	if (workingProperty.get()) {
	    cancelCurrentOperation(null);
	} else {

	    startService(renamingService);
	}
    }

}
