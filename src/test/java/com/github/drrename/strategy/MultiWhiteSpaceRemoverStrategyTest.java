package com.github.drrename.strategy;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class MultiWhiteSpaceRemoverStrategyTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {

	}

	@Before
	public void setUp() throws Exception {

		s = new MultiWhiteSpaceRemoverStrategy();
	}

	@After
	public void tearDown() throws Exception {

	}

	private MultiWhiteSpaceRemoverStrategy s;

	@Test
	public void test01() {

		final String in = "abc";
		assertThat(s.getNameNew(in), is(in));
	}

	@Test
	public void test02() {

		final String in = "ab c";
		assertThat(s.getNameNew(in), is(in));
	}

	@Test
	public void test03() {

		final String in = "ab\tc";
		assertThat(s.getNameNew(in), is("ab c"));
	}

	@Test
	public void test04() {

		final String in = "ab  c";
		assertThat(s.getNameNew(in), is("ab c"));
	}
}
