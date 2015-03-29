package utils;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class IOHelperTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test1() {
		Assert.assertEquals("File - песня.mp3",IOHelper.toFileSystemSafeName("File - песня.mp3"));
	}
	
	@Test
	public void test2() {
		Assert.assertEquals("File - песня.mp3",IOHelper.toFileSystemSafeName("File/ - п\\есня.mp3"));
	}
	
	@Test
	public void test3() {
		Assert.assertEquals("Uriah Heep  - July morning.mp3",IOHelper.toFileSystemSafeName("*Uriah Heep  - July morning*.mp3"));
	}

}
