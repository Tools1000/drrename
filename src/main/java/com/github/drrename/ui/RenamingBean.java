package com.github.drrename.ui;

import java.nio.file.Path;
import java.util.Objects;

import com.github.drrename.strategy.RenamingStrategy;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class RenamingBean {

	private final Path oldPath;
	private final StringProperty newPath;
	private final ObjectProperty<Throwable> exception;

	public RenamingBean(final Path path) {

		this.oldPath = Objects.requireNonNull(path);
		this.newPath = new SimpleStringProperty();
		exception = new SimpleObjectProperty<>();
	}

	public StringProperty getNewPath() {

		return newPath;
	}

	public ObjectProperty<Throwable> getException() {

		return exception;
	}

	public Path getOldPath() {

		return oldPath;
	}

	public void preview(final RenamingStrategy strategy) {

		try {
			final String s = strategy.getNameNew(oldPath);
			Platform.runLater(() -> newPath.set(s));
		} catch(final Exception e) {
			Platform.runLater(() -> this.exception.set(e));
		}
	}

	public void rename(final RenamingStrategy strategy) {

		try {
			final String s = strategy.rename(oldPath, null).getFileName().toString();
			Platform.runLater(() -> newPath.set(s));
		} catch(final Exception e) {
			Platform.runLater(() -> this.exception.set(e));
		}
	}
}
