package com.github.drrename.ui;

import java.util.concurrent.ExecutorService;

import javafx.concurrent.Task;

public class PreviewService extends StrategyService<Void> {

    public PreviewService(final ExecutorService lowPrioExecutor) {

	setExecutor(lowPrioExecutor);
    }

    @Override
    protected Task<Void> createTask() {

	return new PreviewTask(getFiles(), getRenamingStrategy());
    }
}
