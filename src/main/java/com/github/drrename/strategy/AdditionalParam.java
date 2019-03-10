package com.github.drrename.strategy;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AdditionalParam {

    private final String descriptionLong;
    private final String descriptionShort;
    private final StringProperty value;

    public AdditionalParam(final String descriptionShort, final String descriptionLong) {

	super();
	this.descriptionLong = descriptionLong;
	this.descriptionShort = descriptionShort;
	this.value = new SimpleStringProperty();
    }

    // Getter / Setter //

    public StringProperty valueProperty() {

	return this.value;
    }

    public String getValue() {

	return this.valueProperty().get();
    }

    public void setValue(final String value) {

	this.valueProperty().set(value);
    }

    public String getDescriptionLong() {
	return descriptionLong;
    }

    public String getDescriptionShort() {
	return descriptionShort;
    }

}
