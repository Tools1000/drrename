package com.github.drrename.ui;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class ListFilesService extends Service<List<RenamingBean>> {

	private Path path;

	@Override
	protected Task<List<RenamingBean>> createTask() {

		return new ListFilesTask(path);
	}

	public Path getPath() {

		return path;
	}

	public void setPath(final Path path) {

		this.path = Objects.requireNonNull(path);
	}

	@Override
	public String toString() {

		return getClass().getSimpleName() + " [" + path + "]";
	}
}
