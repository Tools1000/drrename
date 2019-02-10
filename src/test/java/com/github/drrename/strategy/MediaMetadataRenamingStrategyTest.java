package com.github.drrename.strategy;

import static org.junit.Assert.fail;

import java.io.File;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.drrename.AbstractRenamingStrategyTest;

public class MediaMetadataRenamingStrategyTest extends AbstractRenamingStrategyTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {

	}

	@Override
	protected RenamingStrategy getStrategy() {

		return new MediaMetadataRenamingStrategy();
	}

	@Override
	protected File getTestFile() {

		return testFile01;
	}

	protected static final File testFile01 = new File("src/test/resources/UPPERCASE.txt");

	@Test
	public void test() {

		fail("Not yet implemented");
	}
}
