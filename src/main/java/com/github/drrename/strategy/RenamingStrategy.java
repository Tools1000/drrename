package com.github.drrename.strategy;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

public interface RenamingStrategy {

	String getHelpText();

	String getIdentifier();

	String getNameNew(Path file) throws IOException, InterruptedException;

	boolean isReplacing();

	void rename(Path file, BasicFileAttributes attrs) throws IOException, InterruptedException;

	void setReplacementStringFrom(String replacement);

	void setReplacementStringTo(String replacement);
}