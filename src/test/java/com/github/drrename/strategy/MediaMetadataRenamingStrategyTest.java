package com.github.drrename.strategy;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class MediaMetadataRenamingStrategyTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {

    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {

    }

    @Before
    public void setUp() throws Exception {

	s = new MediaMetadataRenamingStrategy();
    }

    @After
    public void tearDown() throws Exception {

	s = null;
	input = null;
    }

    private MediaMetadataRenamingStrategy s;
    private String input;

    @Test
    public void test01() {
	final String newName = s.processTag("2015:12:10 aa:36:20", "didntwork");
	assertThat(newName, is("didntwork"));
    }

    @Test
    public void test02() {
	final String newName = s.processTag("2015:12:10 12:36:20", "didntwork");
	assertThat(newName, is("20151210-123620"));
    }
}
