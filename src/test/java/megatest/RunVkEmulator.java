package megatest;

import static org.junit.Assert.*;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RunVkEmulator {
	
	static Logger LOGGER = Logger.getLogger(RunVkEmulator.class);

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() throws Exception {
		VkEmulator emu = new VkEmulator();
		emu.start();
		LOGGER.debug("Input Enter here for exit from test");
		System.in.read();
		emu.stop();
	}

}
