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
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.fest.swing.launcher.ApplicationLauncher.application;
import static org.fest.swing.testing.FestSwingTestCaseTemplate.*;
import vk.VkPlayListBuilderException;

public class FirstGUITest {


	private static Logger LOGGER = Logger.getLogger(FirstGUITest.class);

	@BeforeClass
	public static void setUpOnce() {
		FailOnThreadViolationRepaintManager.install();
	}

	private FrameFixture window;

	@Before
	public void setUp() throws IOException {
		/*application(MainWindow.class).start();
		robot = BasicRobot.robotWithCurrentAwtHierarchy();
		frame=new FrameFixture(MainWindow.getFrame());*/
		// window.show(); // shows the frame to test
		
		
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
		
	}

	@Test
	public void test() throws IOException, InterruptedException {
		LOGGER.debug("Thread = ");
		//Thread.currentThread().join();
	}
}
