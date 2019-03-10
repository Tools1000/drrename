package com.github.drrename.strategy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExtensionByMimeStrategy extends RenamingStrategyProto {

    private final static Logger logger = LoggerFactory.getLogger(ExtensionByMimeStrategy.class);

    @Override
    public String getIdentifier() {

	return "Extension by MIME type";
    }

    @Override
    public String getNameNew(final Path file) throws IOException, InterruptedException {
	final String mimeType = Files.probeContentType(file);
	final String baseName = FilenameUtils.getBaseName(file.getFileName().toString());
	if (mimeType.contains("jpeg"))
	    return baseName + ".jpg";
	if (mimeType.contains("mp4"))
	    return baseName + ".mp4";
	if (mimeType.contains("quicktime"))
	    return baseName + ".mov";
	if (mimeType.contains("heif"))
	    return baseName + ".heic";
	if (mimeType.contains("png"))
	    return baseName + ".png";
	if (mimeType.contains("gif"))
	    return baseName + ".gif";
	if (mimeType.contains("zip"))
	    return baseName + ".zip";
	if (logger.isDebugEnabled()) {
	    logger.debug(file.getFileName() + ": Failed to parse type (" + mimeType + ")");
	}
	return file.getFileName().toString();
    }

    @Override
    public boolean isReplacing() {
	return false;
    }

}
