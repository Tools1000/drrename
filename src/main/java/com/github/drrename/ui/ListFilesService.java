package com.github.drrename.ui;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class ListFilesService extends Service<List<RenamingBean>> {

    private Path path;

    private List<File> files;

    private String fileNameFilterRegex;

    @Override
    protected Task<List<RenamingBean>> createTask() {

	if (files != null)
	    return new ListFilesTask(files, fileNameFilterRegex);
	else
	    return new ListFilesTask(path, fileNameFilterRegex);
    }

    @Override
    public String toString() {

	return getClass().getSimpleName() + " [" + path + "]";
    }

    // Getter / Setter //

    public Path getPath() {

	return path;
    }

    public void setPath(final Path path) {

	this.path = Objects.requireNonNull(path);
	this.files = null;
    }

    public List<File> getFiles() {
	return files;
    }

    public void setFiles(final List<File> files) {
	this.files = files;
	this.path = null;
    }

    public String getFileNameFilterRegex() {
	return fileNameFilterRegex;
    }

    public void setFileNameFilterRegex(final String fileNameFilterRegex) {
	this.fileNameFilterRegex = fileNameFilterRegex;
    }

}
