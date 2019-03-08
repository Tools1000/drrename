package com.github.drrename.ui;

import java.util.List;
import java.util.Objects;

import com.github.drrename.strategy.RenamingStrategy;

import javafx.concurrent.Task;

public class PreviewTask extends Task<Void> {

	final List<RenamingBean> beans;
	final RenamingStrategy renamingStrategy;

	public PreviewTask(final List<RenamingBean> beans, final RenamingStrategy renamingStrategy) {

		this.beans = Objects.requireNonNull(beans);
		this.renamingStrategy = Objects.requireNonNull(renamingStrategy);
	}

	@Override
	protected Void call() throws Exception {

		for(final RenamingBean p : beans) {
			if(Thread.currentThread().isInterrupted())
				throw new InterruptedException("Cancelled");
			p.preview(renamingStrategy);
		}
		return null;
	}
}
