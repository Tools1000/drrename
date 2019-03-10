package com.github.drrename.ui;

import javafx.concurrent.Task;

public class FilterService extends FilesService<Void> {

    private String fileNameFilterRegex;

    public FilterService() {
	setExecutor(PreviewService.LOW_PRIORITY_THREAD_POOL_EXECUTOR);
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
