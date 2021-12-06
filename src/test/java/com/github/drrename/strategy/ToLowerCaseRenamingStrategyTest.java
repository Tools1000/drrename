package com.github.drrename.strategy;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.drrename.AbstractRenamingStrategyTest;

public class ToLowerCaseRenamingStrategyTest extends AbstractRenamingStrategyTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {

	}

	@Override
	protected RenamingStrategy getStrategy() {

		return new ToLowerCaseRenamingStrategy();
	}

	@Override
	protected File getTestFile() {

		return testFile01;
	}

	protected static final File testFile01 = new File("src/test/resources/UPPERCASE.txt");

	@Test
	public void test01() throws IOException, InterruptedException {

		assertEquals("test", s.getNameNew(Paths.get("/home/TEST")));
	}

	@Test
	public void test02() throws IOException, InterruptedException {

		renamedFile = s.rename(testFile01.toPath(), null);
		assertThat(Files.exists(renamedFile), is(true));
		assertThat(renamedFile.getFileName().toString(), is(testFile01.getName().toLowerCase()));
	}
}
