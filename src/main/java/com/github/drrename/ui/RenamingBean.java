package com.github.drrename.ui;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.drrename.strategy.RenamingStrategy;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class RenamingBean {

    private final static Logger logger = LoggerFactory.getLogger(RenamingBean.class);

    private final ObjectProperty<Path> oldPath;
    private final StringProperty newPath;
    private final ObjectProperty<Throwable> exception;
    private final BooleanProperty filtered;
    private final BooleanProperty changing;

    public RenamingBean(final Path path) {

	this.oldPath = new SimpleObjectProperty<>(Objects.requireNonNull(path));
	if (!Files.isReadable(path))
	    throw new IllegalArgumentException("Cannot read " + path);
	if (Files.isDirectory(path))
	    throw new IllegalArgumentException(path + " is a directory");
	this.newPath = new SimpleStringProperty();
	exception = new SimpleObjectProperty<>();
	this.filtered = new SimpleBooleanProperty();
	this.changing = new SimpleBooleanProperty();
	newPath.addListener((v, o, n) -> {
	    changing.set(!getOldPath().getFileName().toString().equals(n));
	});
	filtered.addListener((v, o, n) -> {
	    if ((n != null) && n) {
		changing.set(false);
	    }
	});
    }

    public void preview(final RenamingStrategy strategy) {

	try {
	    final String s = strategy.getNameNew(getOldPath());
	    Platform.runLater(() -> newPath.set(s));
	} catch (final Exception e) {
	    if (logger.isDebugEnabled()) {
		logger.debug(e.getLocalizedMessage(), e);
	    }
	    Platform.runLater(() -> this.exception.set(e));
	}
    }

    public void rename(final RenamingStrategy strategy) {

	try {
	    final Path s = strategy.rename(getOldPath(), null);
	    Platform.runLater(() -> oldPath.set(s));
	    Platform.runLater(() -> newPath.set(s.getFileName().toString()));
	    Platform.runLater(() -> setFiltered(false));
	    Platform.runLater(() -> setChanging(false));
	} catch (final Exception e) {
	    Platform.runLater(() -> this.exception.set(e));
	}
    }

    @Override
    public String toString() {
	return getOldPath().toString();
    }

    // Getter / Setter //

    public StringProperty getNewPath() {

	return newPath;
    }

    public ObjectProperty<Throwable> getException() {

	return exception;
    }

    public BooleanProperty filteredProperty() {
	return this.filtered;
    }

    public boolean isFiltered() {
	return this.filteredProperty().get();
    }

    public void setFiltered(final boolean filtered) {
	// if (filtered) {
	// System.err.println(this + " filtered");
	// }
	this.filteredProperty().set(filtered);
    }

    public BooleanProperty changingProperty() {
	return this.changing;
    }

    public boolean isChanging() {
	return this.changingProperty().get();
    }

    public void setChanging(final boolean changing) {
	this.changingProperty().set(changing);
    }

    public ObjectProperty<Path> oldPathProperty() {
	return this.oldPath;
    }

    public Path getOldPath() {
	return this.oldPathProperty().get();
    }

    public void setOldPath(final Path oldPath) {
	this.oldPathProperty().set(oldPath);
    }

}
