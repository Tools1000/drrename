package com.github.drrename.strategy;

import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RegexReplaceRenamingStrategy extends RenamingStrategyProto {

	private static final Logger logger = LoggerFactory.getLogger(RegexReplaceRenamingStrategy.class);

	@Override
	public String getIdentifier() {

		return "Regex Replace";
	}

	public String getNameNew(final String string) throws InterruptedException {

		if(Thread.currentThread().isInterrupted())
			throw new InterruptedException("Cancelled");
		try {
			return string.replaceAll(getReplacementStringFrom(), getReplacementStringTo());
		} catch(final Exception e) {
			if(logger.isDebugEnabled()) {
				logger.debug(e.getLocalizedMessage());
			}
			// ignore pattern syntax exception
		}
		return string;
	}

	@Override
	public String getNameNew(final Path file) throws InterruptedException {

		return getNameNew(file.getFileName().toString());
	}

	@Override
	public boolean isReplacing() {

		return true;
	}
}
