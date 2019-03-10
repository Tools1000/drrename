package com.github.drrename.ui;

import java.util.Objects;

import com.github.drrename.strategy.RenamingStrategy;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public abstract class StrategyService<V> extends FilesService<V> {

    protected final ObjectProperty<RenamingStrategy> renamingStrategy;

    public StrategyService() {

	this.renamingStrategy = new SimpleObjectProperty<>();
    }

    public ObjectProperty<RenamingStrategy> renamingStrategyProperty() {

	return this.renamingStrategy;
    }

    public RenamingStrategy getRenamingStrategy() {

	return this.renamingStrategyProperty().get();
    }

    public void setRenamingStrategy(final RenamingStrategy renamingStrategy) {

	this.renamingStrategyProperty().set(Objects.requireNonNull(renamingStrategy));
    }
}
