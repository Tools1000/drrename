package com.github.drrename.strategy;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class RegexReplaceRenamingStrategyTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {

	}

	@Before
	public void setUp() throws Exception {

		s = new RegexReplaceRenamingStrategy();
	}

	@After
	public void tearDown() throws Exception {

	}

	private RegexReplaceRenamingStrategy s;
	static final String testName01 = "abc-1-abc.txt";

	@Test
	public void test01() throws Exception {

		s.setReplacementStringFrom("(\\d+)");
		final String newName = s.getNameNew(testName01);
		assertThat(newName, is("abc--abc.txt"));
	}

	@Test
	public void test02() throws Exception {

		s.setReplacementStringFrom("(\\d+)");
		s.setReplacementStringTo("XX");
		final String newName = s.getNameNew(testName01);
		assertThat(newName, is("abc-XX-abc.txt"));
	}
}
