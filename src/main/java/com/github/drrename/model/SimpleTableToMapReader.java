package com.github.drrename.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import net.sf.kerner.utils.exception.ExceptionFileFormat;

public class SimpleTableToMapReader {

    public Multimap<String, String> read(final BufferedReader reader) throws IOException {
	final Multimap<String, String> result = ArrayListMultimap.create();
	String line = null;
	while ((line = reader.readLine()) != null) {
	    final String[] ss = line.split("\\s");
	    if (ss.length < 2) {
		throw new ExceptionFileFormat("Not enough columns (" + ss.length + ")");
	    }
	    final String key = ss[0];
	    for (int i = 1; i < ss.length; i++) {
		result.put(key, ss[i]);
	    }
	}
	return result;
    }

    public Multimap<String, String> read(final File file) throws IOException {
	final FileReader reader = new FileReader(file);
	try {
	    return read(reader);
	} finally {
	    if (reader != null) {
		reader.close();
	    }
	}
    }

    public Multimap<String, String> read(final Reader reader) throws IOException {
	return read(new BufferedReader(reader));
    }

}
