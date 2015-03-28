package megatest;

import java.awt.EventQueue;
import java.io.IOException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import static org.fest.swing.launcher.ApplicationLauncher.application;
import static org.fest.swing.testing.FestSwingTestCaseTemplate.*;
import service.DownloadServiceException;
import vk.VkPlayListBuilderException;

public class FirstGUITest extends ShowWindow {
	
	private boolean eventsHandled;

	@Before
	public void setUp() throws IOException {
		super.setUp();
		
		eventsHandled = false;
		
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
	public void testPlayFirstSong() throws IOException, InterruptedException {
		lock.lock();
		LOGGER.debug("Log4J stub for show thread");
				
		window.panel("null.contentPane").list().doubleClickItem(0);
		
		Thread.sleep(1500);
		
		while(!eventsHandled){
			notFull.await();
		}
		Assert.assertTrue(downloadTriggered);
		Assert.assertTrue(playTriggered);
		notEmpty.signal();
		lock.unlock();
	}
	
	Condition notEmpty = lock.newCondition();
	Condition notFull = lock.newCondition();
	
	@Test
	public void testPlaySecondSongAfterFirst() throws IOException, InterruptedException {
		lock.lock();
		LOGGER.debug("Log4J stub for show thread");
				
		//window.scrollPane().verticalScrollBar().scrollBlockDown(60);
		window.panel("null.contentPane").list().doubleClickItem(0);
		Thread.sleep(500);
		Assert.assertTrue(downloadTriggered);
		Assert.assertTrue(playTriggered);
		notEmpty.signal();
		lock.unlock();
		downloadTriggered = false;
		playTriggered = false;
		
		Thread.sleep(1500);
		while(!eventsHandled){
			notFull.await();
		}
		Assert.assertTrue(nextTriggered);
		Assert.assertTrue(downloadTriggered);
		Assert.assertTrue(playTriggered);
		Assert.assertTrue(playFinished);
		notEmpty.signal();
		lock.unlock();
		notEmpty.signal();
		lock.unlock();
	}
	
	@Test
	public void testPlayThirdSongAfterSecondAfterFirst() throws IOException, InterruptedException {
		lock.lock();
		LOGGER.debug("Log4J stub for show thread");
				
		//window.scrollPane().verticalScrollBar().scrollBlockDown(60);
		window.panel("null.contentPane").list().doubleClickItem(0);
		Thread.sleep(500);
		window.panel("null.contentPane").list().doubleClickItem(1);
		Thread.sleep(500);
		while(!eventsHandled){
			notFull.await();
		}
		Assert.assertTrue(downloadTriggered);
		Assert.assertTrue(playTriggered);
		notEmpty.signal();
		lock.unlock();
		downloadTriggered = false;
		playTriggered = false;
		
		Thread.sleep(2500);
		while(!eventsHandled){
			notFull.await();
		}
		Assert.assertTrue(nextTriggered);
		Assert.assertTrue(downloadTriggered);
		Assert.assertTrue(playTriggered);
		Assert.assertTrue(playFinished);
		notEmpty.signal();
		lock.unlock();
	}

	
	
	@Test
	public void testBugPlaySecondSongAfterFirst() throws IOException, InterruptedException {
		lock.lock();
		LOGGER.debug("Log4J stub for show thread");
				
		window.panel("null.contentPane").list().doubleClickItem(0);
		Thread.sleep(500);
		window.panel("null.contentPane").list().doubleClickItem(1);
		while(!eventsHandled){
			notFull.await();
		}
		Assert.assertTrue(downloadTriggered);
		Assert.assertTrue(playTriggered);
		notEmpty.signal();
		lock.unlock();
		downloadTriggered = false;
		Thread.sleep(500);
		while(!eventsHandled){
			notFull.await();
		}
		Assert.assertFalse(downloadTriggered);
		Assert.assertEquals(2, downloadTriggeredCount);
		notEmpty.signal();
		lock.unlock();
		System.out.println("downloadTriggeredCount="+downloadTriggeredCount);
	}
	
	@Test
	public void testManuallyRePlay() throws IOException, InterruptedException {
		lock.lock();
		LOGGER.debug("Log4J stub for show thread");
				
		window.panel("null.contentPane").list().doubleClickItem(0);
		Thread.sleep(4000);
		while(!eventsHandled){
			notFull.await();
		}
		Assert.assertEquals(3, downloadTriggeredCount);
		notEmpty.signal();
		lock.unlock();
		playTriggeredCount = 0;
		System.out.println("downloadTriggeredCount="+downloadTriggeredCount);
		
		playTriggered = false;
		window.panel("null.contentPane").list().doubleClickItem(0);
		Thread.sleep(4000);
		while(!eventsHandled){
			notFull.await();
		}
		Assert.assertEquals(3, playTriggeredCount);
		notEmpty.signal();
		lock.unlock();
	}


	
	private boolean downloadTriggered;
	private int downloadTriggeredCount;
	private int playTriggeredCount;
	private boolean playTriggered;
	private boolean playFinished;
	private boolean nextTriggered;
	
	//@AllowConcurrentEvents
	@Subscribe
	public void onDownload(DownloadEvent e) throws DownloadServiceException {
		lock.lock();
		final String s = e.getSong().toString();
		final String message = "Downloading '" + s + "'";
		LOGGER.debug(message);

		downloadTriggered = true;
		
		eventsHandled = true;
		notFull.signal();
		lock.unlock();
	}
	
	//@AllowConcurrentEvents
	@Subscribe
	public void onPlay(PlayEvent e) throws DownloadServiceException {
		lock.lock();
		final Song s = e.getSong();
		final String message = "Playing '" + s + "'";
		LOGGER.debug(message);

		playTriggered = true;
		playTriggeredCount++;
		
		eventsHandled = true;
		notFull.signal();
		lock.unlock();
	}
	
	//@AllowConcurrentEvents
	@Subscribe
	public void onPlayFinished(PlayFinished e){
		lock.lock();
		LOGGER.debug("Play finished");
		playFinished = true;
		
		eventsHandled = true;
		notFull.signal();
		lock.unlock();
	}

	//@AllowConcurrentEvents
	@Subscribe
	public void next(NextSong e){
		lock.lock();
		nextTriggered = true;
		
		eventsHandled = true;
		notFull.signal();
		lock.unlock();
	}
	
	//@AllowConcurrentEvents
	@Subscribe
	public void download(DownloadEvent e) {
		lock.lock();
		downloadTriggeredCount++;
		
		eventsHandled = true;
		notFull.signal();
		lock.unlock();
	}
}
