package com.github.drrename.ui;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.concurrent.Task;

public class ListFilesTask extends Task<List<RenamingBean>> {

    private final static Logger logger = LoggerFactory.getLogger(ListFilesTask.class);

    private final Path path;

    private final List<File> files;

    private final String fileNameFilterRegex;

    public ListFilesTask(final Path path, final String fileNameFilterRegex) {

	this.path = Objects.requireNonNull(path);
	this.files = null;
	this.fileNameFilterRegex = fileNameFilterRegex;
    }

    public ListFilesTask(final List<File> files, final String fileNameFilterRegex) {

	this.files = Objects.requireNonNull(files);
	this.path = null;
	this.fileNameFilterRegex = fileNameFilterRegex;
    }

    @Override
    protected List<RenamingBean> call() throws Exception {

	if (files != null)
	    return getEntries(files);
	return getEntries(path);
    }

    List<RenamingBean> getEntries(final List<File> files) throws IOException {
	final List<RenamingBean> entries = new ArrayList<>();

	for (final File f : files) {
	    if (Thread.interrupted()) {
		break;
	    }
	    if (FilterTask.matches(f.getName(), fileNameFilterRegex)) {
		entries.add(new RenamingBean(f.toPath()));
	    }
	}
	return entries;

    }

    List<RenamingBean> getEntries(final Path dir) throws IOException {

	// final DirectoryStream.Filter<Path> filter = new
	// DirectoryStream.Filter<Path>() {
	//
	// @Override
	// public boolean accept(final Path file) throws IOException {
	//
	// return Files.isRegularFile(file);
	// }
	// };

	final List<RenamingBean> entries = new ArrayList<>();

	if (Files.isDirectory(dir)) {
	    try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
		for (final Iterator<Path> it = stream.iterator(); it.hasNext();) {
		    if (Thread.interrupted()) {
			break;
		    }
		    final Path next = it.next();
		    if (FilterTask.matches(next.getFileName().toString(), fileNameFilterRegex)) {
			try {
			    entries.add(new RenamingBean(next));
			} catch (final Exception e) {
			    if (logger.isErrorEnabled()) {
				logger.error(e.getLocalizedMessage(), e);
			    }
			}
		    }
		}
	    }
	} else {
	    try {
		if (FilterTask.matches(dir.getFileName().toString(), fileNameFilterRegex)) {
		    entries.add(new RenamingBean(dir));
		}
	    } catch (final IllegalArgumentException e) {
		if (logger.isDebugEnabled()) {
		    logger.debug(e.getLocalizedMessage(), e);
		}
	    }
	}
	return entries;

    }
}
