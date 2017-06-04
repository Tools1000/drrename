package com.github.drrename.model;

import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RegexReplaceRenamingStrategy extends RenamingStrategyProto {

    private static final Logger logger = LoggerFactory.getLogger(RegexReplaceRenamingStrategy.class);

    @Override
    public String getIdentifier() {
	return "Regex Replace";
    }

    @Override
    public String getNameNew(final Path file) throws InterruptedException {
	if (Thread.currentThread().isInterrupted()) {
	    throw new InterruptedException("Cancelled");
	}
	try {
	    final Pattern pattern = Pattern.compile(getReplacementStringFrom());
	    final Matcher matcher = pattern.matcher(file.getFileName().toString());
	    if (matcher.matches() && matcher.groupCount() > 0) {
		return file.getFileName().toString().replaceAll(matcher.group(1), getReplacementStringTo());
	    } else {
		return file.getFileName().toString().replaceFirst(getReplacementStringFrom(), getReplacementStringTo());
	    }

	} catch (final PatternSyntaxException e) {
	    if (logger.isErrorEnabled()) {
		logger.error(e.getLocalizedMessage());
	    }
	}
	return file.getFileName().toString();
    }

    @Override
    public boolean isReplacing() {
	return true;
    }

}
