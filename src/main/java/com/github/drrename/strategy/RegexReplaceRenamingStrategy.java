package com.github.drrename.strategy;

import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RegexReplaceRenamingStrategy extends RenamingStrategyProto {

	private static final Logger logger = LoggerFactory.getLogger(RegexReplaceRenamingStrategy.class);

	public RegexReplaceRenamingStrategy() {

		// setAdditionalParam(new AdditionalParam("Regex Group"));
	}

	public int getGroup() {

		return Integer.parseInt(getAdditionalParam().get().getValue());
	}

	@Override
	public String getIdentifier() {

		return "Regex Replace";
	}

	public String getNameNew(final String string) throws InterruptedException {

		if(Thread.currentThread().isInterrupted())
			throw new InterruptedException("Cancelled");
		try {
			final Pattern pattern = Pattern.compile(getReplacementStringFrom());
			final Matcher matcher = pattern.matcher(string);
			// if(matcher.matches() && (matcher.groupCount() > 0) && hasAdditionalParam()) {
			// final StringBuilder sb = new StringBuilder();
			// for(int i = 0; i < matcher.groupCount(); i++) {
			// final String s = matcher.group(getGroup());
			// if(getGroup() == i) {
			// System.err.println(getReplacementStringTo());
			// sb.append(getReplacementStringTo());
			// } else {
			// sb.append(s);
			// }
			// }
			// return sb.toString();
			// }
			return string.replaceFirst(getReplacementStringFrom(), getReplacementStringTo());
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
