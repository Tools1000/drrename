package com.github.drrename.strategy;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Optional;

/**
 * A strategy for renaming files.
 *
 * @author Alexander Kerner
 *
 */
public interface RenamingStrategy {

	/**
	 * Returns a more detailed help text on what this strategy is doing and if and how it can be configured.
	 *
	 * @return a more detailed help text on what this strategy is doing
	 */
	String getHelpText();

	/**
	 * Returns a human-readable identifier for this strategy.
	 *
	 * @return a human-readable identifier for this strategy
	 */
	String getIdentifier();

	/**
	 * Returns the new file name to which given file would be renamed if {@link #rename(Path, BasicFileAttributes)} would be called.
	 * Note: the file name will remain unchanged until {@link #rename(Path, BasicFileAttributes)} is actually called.
	 *
	 * @param file
	 *            the file that should be renamed
	 * @return the new file name to which given file would be renamed if {@link #rename(Path, BasicFileAttributes)} would be called
	 * @throws IOException
	 *             if some IO error occurs
	 * @throws InterruptedException
	 *             if this strategy is interrupted
	 */
	String getNameNew(Path file) throws IOException, InterruptedException;

	/**
	 * Returns {@code true}, if this strategy is replacing certain characters of a file name with some other characters.
	 *
	 * @see #setReplacementStringFrom(String)
	 * @see #setReplacementStringTo(String)
	 *
	 * @return {@code true}, if this strategy is replacing; {@code false} otherwise
	 */
	boolean isReplacing();

	boolean hasAdditionalParam();

	Optional<AdditionalParam> getAdditionalParam();

	Path rename(Path file, BasicFileAttributes attrs) throws IOException, InterruptedException;

	void setReplacementStringFrom(String replacement);

	void setReplacementStringTo(String replacement);
}
