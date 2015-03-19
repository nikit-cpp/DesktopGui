package megatest;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import gui.MainWindow;

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

import vk.VkPlayListBuilderException;

public class FirstGUITest {
	 
	  private JPanelFixture window;
	 
	  /*@BeforeClass public static void setUpOnce() {
	    FailOnThreadViolationRepaintManager.install();
	  }
	 
	  @Before public void setUp() throws IOException {
		  MainWindow frame = GuiActionRunner.execute(new GuiQuery<MainWindow>() {
	        protected MainWindow executeInEDT() throws ParserConfigurationException, VkPlayListBuilderException {
	          return new MainWindow();  
	        }
	    });
	    window = new JPanelFixture(null, frame);
	    //window.show(); // shows the frame to test
	  }
	 
	  @After public void tearDown() {
	    //window.cleanUp();
	  }*/
	 
	  /*@Test public void shouldCopyTextInLabelWhenClickingButton() {
	    window.textBox("textToCopy").enterText("Some random text");
	    window.button("copyButton").click();
	    window.label("copiedText").requireText("Some random text");
	  }*/
	  
	  @Test public void test() throws IOException, InterruptedException {
		  application(MainWindow.class).start();
		    Thread.currentThread().join();
		  }
	}
