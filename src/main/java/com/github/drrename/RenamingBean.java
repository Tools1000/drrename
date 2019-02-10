package com.github.drrename;

import java.nio.file.Path;

import com.github.drrename.strategy.RenamingStrategy;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class RenamingBean {

	private final Path oldPath;
	private final StringProperty newPath;
	private final ObjectProperty<Throwable> exception;

	public RenamingBean(final Path path) {

		this.oldPath = path;
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
			newPath.set(strategy.getNameNew(oldPath));
		} catch(final Exception e) {
			this.exception.set(e);
		}
	}

	public void apply(final RenamingStrategy strategy) {

		try {
			newPath.set(strategy.rename(oldPath, null).getFileName().toString());
		} catch(final Exception e) {
			this.exception.set(e);
		}
	}
}
