package com.github.drrename.ui;

import java.util.List;
import java.util.Objects;

import com.github.drrename.RenamingTask;

import javafx.concurrent.Task;

public class RenamingService2 extends StrategyService<Void> {

	private List<RenamingBean> events;

	public RenamingService2() {

		super();
	}

	@Override
	protected Task<Void> createTask() {

		return new RenamingTask(Objects.requireNonNull(events), Objects.requireNonNull(getRenamingStrategy()));
	}
}
