package com.github.nikit.cpp.player;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PlayListTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testNormallyGetNext() {
		Song s1 = new Song("AC", "high", "url1");
		Song s2 = new Song("DC", "hell", "url2");
		PlayList pl = new PlayList();
		pl.addSong(s1);
		pl.addSong(s2);
				
		Assert.assertEquals(s2, pl.getNextSong(s1));
	}
	
	
	@Test
	public void testNormallyGetPrev() {
		Song s1 = new Song("AC", "high", "url1");
		Song s2 = new Song("DC", "hell", "url2");
		PlayList pl = new PlayList();
		pl.addSong(s1);
		pl.addSong(s2);
				
		Assert.assertEquals(s1, pl.getPrevSong(s2));
	}

	@Test
	public void testOutGetNext() {
		Song s1 = new Song("AC", "high", "url1");
		Song s2 = new Song("DC", "hell", "url2");
		PlayList pl = new PlayList();
		pl.addSong(s1);
		pl.addSong(s2);
				
		Assert.assertEquals(null, pl.getNextSong(s2));
	}
	
	@Test
	public void testOutGetPrev() {
		Song s1 = new Song("AC", "high", "url1");
		Song s2 = new Song("DC", "hell", "url2");
		PlayList pl = new PlayList();
		pl.addSong(s1);
		pl.addSong(s2);
				
		Assert.assertEquals(null, pl.getPrevSong(s1));
	}


	@Test
	public void testGetNextForNull() {
		Song s1 = new Song("AC", "high", "url1");
		Song s2 = new Song("DC", "hell", "url2");
		PlayList pl = new PlayList();
		pl.addSong(s1);
		pl.addSong(s2);
				
		Assert.assertEquals(null, pl.getNextSong(null));
	}
	
	@Test
	public void testGetPrevForNull() {
		Song s1 = new Song("AC", "high", "url1");
		Song s2 = new Song("DC", "hell", "url2");
		PlayList pl = new PlayList();
		pl.addSong(s1);
		pl.addSong(s2);
				
		Assert.assertEquals(null, pl.getPrevSong(null));
	}

}
