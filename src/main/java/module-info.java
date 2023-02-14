open module drrename {
    requires java.annotation;
    requires com.fasterxml.jackson.databind;
    requires com.jthemedetector;
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires jodd.util;
    requires metadata.extractor;
    requires net.rgielen.fxweaver.core;
    requires net.rgielen.fxweaver.spring.boot.autoconfigure;
    requires org.apache.commons.io;
    requires org.apache.commons.lang3;
    requires org.apache.commons.text;
    requires org.apache.tika.core;
    requires org.slf4j;
    requires spring.beans;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.cloud.openfeign.core;
    requires spring.context;
    requires spring.core;
    requires lombok;
    requires net.rgielen.fxweaver.spring;
    exports drrename;
}