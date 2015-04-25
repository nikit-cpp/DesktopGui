package megatest;

import java.awt.EventQueue;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import events.DownloadEvent;
import events.NextSong;
import events.PlayIntent;
import events.PlayStopped;
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

import com.github.nikit.cpp.player.Song;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import static org.fest.swing.launcher.ApplicationLauncher.application;
import static org.fest.swing.testing.FestSwingTestCaseTemplate.*;
import service.DownloadServiceException;
import vk.VkPlayListBuilderException;

public class ShowWindow {


	protected static Logger LOGGER = Logger.getLogger(ShowWindow.class);
	protected static VkEmulator vk;
	protected FrameFixture window;
	protected MainWindow mainWindow;
	//protected EventBus eventBus;

	
	
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
		//eventBus = MainWindow.getEventBus();
		//eventBus.register(this);
	}

	@After
	public void tearDown() throws InterruptedException {
		 window.cleanUp();
		 //eventBus.unregister(this);
		 mainWindow.getPlayerService().getPlayer().stop();
		 Thread.sleep(200);
	}

	@Test
	public void testPlayFirstSong() throws IOException, InterruptedException {
		LOGGER.debug("Log4J stub for show thread");
				
		LOGGER.debug("Input Enter here for exit from test");
		System.in.read();
		
		//Thread.currentThread().join();
		//LOGGER.debug("I ah here");
	}
}
