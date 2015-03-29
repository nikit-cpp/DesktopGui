package vk;

import static org.junit.Assert.*;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Need access to vk.com
 * @author nik
 *
 */
public class VkPlayListBuilderTest {
	
	static String masterUrl = "https://api.vk.com"; 

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetGidFromShortName() throws VkPlayListBuilderException, ParserConfigurationException {
		String s = "legends_of_rock";
		VkPlayListBuilder cxp = new VkPlayListBuilder(masterUrl);
		
		int groupId = cxp.getGidFromShortName(s);
		
		Assert.assertEquals(11081630, groupId);
	}
	
	@Test
	public void testGetGidFromShortNameAnother() throws VkPlayListBuilderException, ParserConfigurationException {
		String s = "rockmetal80";
		VkPlayListBuilder cxp = new VkPlayListBuilder(masterUrl);
		
		int groupId = cxp.getGidFromShortName(s);
		
		Assert.assertEquals(64183, groupId);
	}

	@Test
	public void testGetPlayListsFromGroup() throws VkPlayListBuilderException, ParserConfigurationException {
		int gid = 11081630;
		VkPlayListBuilder cxp = new VkPlayListBuilder(masterUrl);
		
		cxp.getPlayListsFromGroup(gid);
	}
	
}
