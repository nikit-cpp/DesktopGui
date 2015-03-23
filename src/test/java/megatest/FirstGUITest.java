package megatest;

import java.awt.EventQueue;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import events.DownloadEvent;
import events.NextSong;
import events.PlayEvent;
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

import player.PlayFinished;

import com.github.nikit.cpp.player.Song;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import static org.fest.swing.launcher.ApplicationLauncher.application;
import static org.fest.swing.testing.FestSwingTestCaseTemplate.*;
import service.DownloadServiceException;
import vk.VkPlayListBuilderException;

public class FirstGUITest {


	private static Logger LOGGER = Logger.getLogger(FirstGUITest.class);
	private static VkEmulator vk;
	private FrameFixture window;
	private MainWindow mainWindow;
	private EventBus eventBus;

	
	
	@BeforeClass
	public static void setUpOnce() throws Exception {
		FailOnThreadViolationRepaintManager.install();
		vk = new VkEmulator();
		vk.start();
	}
	
	@AfterClass
	public static void kill() throws Exception{
		vk.stop();
	}

	

	@Before
	public void setUp() throws IOException {
		mainWindow = GuiActionRunner.execute(new GuiQuery<MainWindow>() {
            protected MainWindow executeInEDT() throws ParserConfigurationException, VkPlayListBuilderException {
                MainWindow.main(new String[0]);
                return MainWindow.getInstance();  
            }
        });
        window = new FrameFixture(mainWindow);
		eventBus = MainWindow.getEventBus();
		eventBus.register(this);
	}

	@After
	public void tearDown() throws InterruptedException {
		 window.cleanUp();
		 eventBus.unregister(this);
		 mainWindow.getPlayerService().getPlayer().stop();
		 Thread.sleep(200);
	}

	@Test
	public void testPlayFirstSong() throws IOException, InterruptedException {
		LOGGER.debug("Log4J stub for show thread");
				
		//window.scrollPane().verticalScrollBar().scrollBlockDown(60);
		window.panel("null.contentPane").list().doubleClickItem(0);
		
		Thread.sleep(500);
		
		Assert.assertTrue(downloadTriggered);
		Assert.assertTrue(playTriggered);
		//window.close();
		
		//LOGGER.debug("Press Enter for exit from test");
		//System.in.read();
		
		//Thread.currentThread().join();
		//LOGGER.debug("I ah here");
	}
	
	@Test
	public void testPlaySecondSongAfterFirst() throws IOException, InterruptedException {
		LOGGER.debug("Log4J stub for show thread");
				
		//window.scrollPane().verticalScrollBar().scrollBlockDown(60);
		window.panel("null.contentPane").list().doubleClickItem(0);
		
		Assert.assertTrue(downloadTriggered);
		Assert.assertTrue(playTriggered);
		downloadTriggered = false;
		playTriggered = false;
		
		Thread.sleep(1500);
		Assert.assertTrue(nextTriggered);
		Assert.assertTrue(downloadTriggered);
		Assert.assertTrue(playTriggered);
		Assert.assertTrue(playFinished);
	}

	
	private boolean downloadTriggered = false;
	private boolean playTriggered = false;
	private boolean playFinished = false;
	private boolean nextTriggered = false;
	
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
}
