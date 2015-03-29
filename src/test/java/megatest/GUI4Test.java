package megatest;

import java.awt.EventQueue;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import events.DownloadEvent;
import events.NextSong;
import events.PlayEvent;
import events.PlayFinished;
import gui.MainWindow;

import org.apache.log4j.Logger;
import org.fest.swing.core.BasicRobot;
import org.fest.swing.core.Robot;
import org.fest.swing.edt.FailOnThreadViolationRepaintManager;
import org.fest.swing.edt.GuiActionRunner;
import org.fest.swing.edt.GuiQuery;
import org.fest.swing.fixture.FrameFixture;
import org.fest.swing.fixture.JPanelFixture;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import com.github.nikit.cpp.player.Song;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import static org.fest.swing.launcher.ApplicationLauncher.application;
import static org.fest.swing.testing.FestSwingTestCaseTemplate.*;
import service.DownloadServiceException;
import vk.VkPlayListBuilderException;

public class GUI4Test extends ShowWindow {
	

	@Before
	public void setUp() throws IOException {
		super.setUp();
		
		downloadTriggered = false;
		playTriggered = false;
		playFinished = false;
		nextTriggered = false;
		downloadTriggeredCount = 0;
		playTriggeredCount = 0;
	}

	@After
	public void tearDown() throws InterruptedException {
		 super.tearDown();
	}

	
	@Test
	public void testBugPlaySecondSongAfterFirst() throws IOException, InterruptedException {
		LOGGER.debug("Log4J stub for show thread");
				
		window.panel("null.contentPane").list().doubleClickItem(0);
		Thread.sleep(500);
		window.panel("null.contentPane").list().doubleClickItem(1);
		Assert.assertTrue(downloadTriggered);
		Assert.assertTrue(playTriggered);
		downloadTriggered = false;
		Thread.sleep(500);
		Assert.assertFalse(downloadTriggered);
		Assert.assertEquals(2, downloadTriggeredCount);

		System.out.println("downloadTriggeredCount="+downloadTriggeredCount);
	}
	
	
	private boolean downloadTriggered;
	private int downloadTriggeredCount;
	private int playTriggeredCount;
	private boolean playTriggered;
	private boolean playFinished;
	private boolean nextTriggered;
	
	@Subscribe
	public void onDownload(DownloadEvent e) throws DownloadServiceException {
		final String s = e.getSong().toString();
		final String message = "Downloading '" + s + "'";
		LOGGER.debug(message);

		downloadTriggered = true;
	}
	
	@Subscribe
	public void onPlay(PlayEvent e) throws DownloadServiceException {
		final Song s = e.getSong();
		final String message = "Playing '" + s + "'";
		LOGGER.debug(message);

		playTriggered = true;
		playTriggeredCount++;
	}
	
	@Subscribe
	public void onPlayFinished(PlayFinished e){
		LOGGER.debug("Play finished");
		playFinished = true;
	}

	@Subscribe
	public void next(NextSong e){
		nextTriggered = true;
	}
	
	@Subscribe
	public void download(DownloadEvent e) {
		downloadTriggeredCount++;
	}
}
