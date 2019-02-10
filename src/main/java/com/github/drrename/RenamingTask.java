package com.github.drrename;

import java.util.List;

import com.github.drrename.strategy.RenamingStrategy;

import javafx.concurrent.Task;

public class RenamingTask extends Task<Void> {

	private final List<RenamingBean> elements;
	private final RenamingStrategy strategy;

	public RenamingTask(final List<RenamingBean> elements, final RenamingStrategy strategy) {

		super();
		this.elements = elements;
		this.strategy = strategy;
	}

	@Override
	protected Void call() throws Exception {

		for(final RenamingBean b : elements) {
			b.apply(strategy);
		}
		return null;
	}
}
