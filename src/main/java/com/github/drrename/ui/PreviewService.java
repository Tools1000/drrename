package com.github.drrename.ui;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.github.ktools1000.AnotherThreadFactory;

import javafx.concurrent.Task;

public class PreviewService extends StrategyService<Void> {

	public static Executor LOW_PRIORITY_THREAD_POOL_EXECUTOR = Executors.newFixedThreadPool(1, new AnotherThreadFactory(Thread.MIN_PRIORITY));

	public PreviewService() {

		setExecutor(LOW_PRIORITY_THREAD_POOL_EXECUTOR);
	}

	@Override
	protected Task<Void> createTask() {

		return new PreviewTask(getFiles(), getRenamingStrategy());
	}
}
