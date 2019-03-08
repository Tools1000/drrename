package com.github.drrename.ui;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import javafx.concurrent.Task;

public class ListFilesTask extends Task<List<RenamingBean>> {

	private final Path path;

	public ListFilesTask(final Path path) {

		this.path = Objects.requireNonNull(path);
	}

	@Override
	protected List<RenamingBean> call() throws Exception {

		return getEntries(path);
	}

	static List<RenamingBean> getEntries(final Path dir) throws IOException {

		// final DirectoryStream.Filter<Path> filter = new DirectoryStream.Filter<Path>() {
		//
		// @Override
		// public boolean accept(final Path file) throws IOException {
		//
		// return Files.isRegularFile(file);
		// }
		// };
		final List<RenamingBean> entries = new ArrayList<>();
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
			for(final Iterator<Path> it = stream.iterator(); it.hasNext();) {
				if(Thread.interrupted()) {
					break;
				}
				entries.add(new RenamingBean(it.next()));
			}
		}
		return entries;
	}
}
