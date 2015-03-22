package megatest;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

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
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import static org.fest.swing.launcher.ApplicationLauncher.application;
import static org.fest.swing.testing.FestSwingTestCaseTemplate.*;
import vk.VkPlayListBuilderException;

public class FirstGUITest {


	private static Logger LOGGER = Logger.getLogger(FirstGUITest.class);
	private static VkEmulator vk;
	
	
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

	private FrameFixture window;

	@Before
	public void setUp() throws IOException {
        MainWindow frame = GuiActionRunner.execute(new GuiQuery<MainWindow>() {
            protected MainWindow executeInEDT() throws ParserConfigurationException, VkPlayListBuilderException {
                MainWindow.main(new String[0]);
                return MainWindow.getInstance();  
            }
        });
        window = new FrameFixture(frame);
	}

	@After
	public void tearDown() {
		 window.cleanUp();
	}

	@Test
	public void test() throws IOException, InterruptedException {
		LOGGER.debug("Log4J stub for show thread");
		
		//CustomPlayer mockedPlayer = Mockito.mock(CustomPlayer.class);
		
		window.scrollPane().verticalScrollBar().scrollBlockDown(60);
		window.panel("null.contentPane").list().doubleClickItem(0);
		
		//LOGGER.debug("Press Enter for exit from test");
		//System.in.read();
	}
}
