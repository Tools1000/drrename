package com.github.drrename.strategy;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AdditionalParam {

	private final String description;
	private final StringProperty value;

	public AdditionalParam(final String description) {

		super();
		this.description = description;
		this.value = new SimpleStringProperty();
	}

	public String getDescription() {

		return description;
	}

	public StringProperty valueProperty() {

		return this.value;
	}

	public String getValue() {

		return this.valueProperty().get();
	}

	public void setValue(final String value) {

		this.valueProperty().set(value);
	}
}
