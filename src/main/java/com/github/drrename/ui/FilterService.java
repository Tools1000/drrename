package com.github.drrename.ui;

import java.util.concurrent.ExecutorService;

import javafx.concurrent.Task;

public class FilterService extends FilesService<Void> {

    private String fileNameFilterRegex;

    public FilterService(final ExecutorService executor) {
	if (executor != null) {
	    setExecutor(executor);
	}
    }

    @Override
    protected Task<Void> createTask() {
	return new FilterTask(getFiles(), fileNameFilterRegex);
    }

    // Getter / Setter //

    public String getFileNameFilterRegex() {
	return fileNameFilterRegex;
    }

    public void setFileNameFilterRegex(final String fileNameFilterRegex) {
	this.fileNameFilterRegex = fileNameFilterRegex;
    }

}
