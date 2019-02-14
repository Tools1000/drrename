package com.github.drrename.strategy;

import java.nio.file.Path;

public class MultiWhiteSpaceRemoverStrategy extends RenamingStrategyProto {

	@Override
	public String getIdentifier() {

		return "Remove multiple whitespaces";
	}

	@Override
	public String getNameNew(final Path file) throws InterruptedException {

		return getNameNew(file.getFileName().toString());
	}

	public String getNameNew(final String fileName) {

		return fileName.replaceAll("\\s+", " ");
	}

	@Override
	public boolean isReplacing() {

		return false;
	}
}
