package com.github.drrename.ui;

import java.util.Objects;

import com.github.drrename.RenamingTask;

import javafx.concurrent.Task;

public class RenamingService2 extends StrategyService<Void> {

    public RenamingService2() {

	super();
    }

    @Override
    protected Task<Void> createTask() {

	return new RenamingTask(Objects.requireNonNull(getFiles(), "Files must not be null"),
		Objects.requireNonNull(getRenamingStrategy(), "Strategy must not be null"));
    }
}
