package gui;
import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class ConfigTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetInjected() {
	      ApplicationContext context = 
	              new ClassPathXmlApplicationContext("spring-config.xml");

	       Config jc= (Config)context.getBean("config");

	       Set<String> expected = new HashSet<String>();
	       expected.add("rockmetal80");
	       Assert.assertEquals(expected, jc.getGroupNames());
	       
	       Assert.assertEquals("/tmp", jc.getCacheFolder());
	}

}
