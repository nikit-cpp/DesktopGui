package megatest;

// http://stackoverflow.com/questions/14179746/unit-test-a-servlet-with-an-embedded-jetty/14181904#14181904
// http://git.eclipse.org/c/jetty/org.eclipse.jetty.project.git/tree/examples/embedded/src/main/java/org/eclipse/jetty/embedded/MinimalServlets.java

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.junit.Test;

public class VkEmulator {
	
	static Logger LOGGER = Logger.getLogger(VkEmulator.class);
	private static int port = 8079;
	private Server server;
	
	@Test
	public void start() throws Exception {
		server = new Server(port);

		ServletHandler servletHandler = new ServletHandler();
		
        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setDirectoriesListed(true);
        resource_handler.setResourceBase(".");
 
        HandlerList handlers = new HandlerList();
        // Порядок важен
        handlers.setHandlers(new Handler[] { resource_handler, servletHandler });
		
		server.setHandler(handlers);

		servletHandler.addServletWithMapping(HelloServlet.class, "/*");
		server.start();

		server.join();
		LOGGER.debug("Goodbye, America!");
		LOGGER.debug("VkEmulator started");
	}

	@SuppressWarnings("serial")
	// curl https://api.vk.com/method/wall.get.xml?owner_id=-11081630 > ./src/test/resources/wall.get.xml
	// 
	public static class HelloServlet extends HttpServlet {
		
		private String type = "text/xml";
		
		@Override
		protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			
			String requestUri = request.getRequestURI();
			String queryString = request.getQueryString();
			
			LOGGER.debug("requestUri is " + requestUri);
			LOGGER.debug("queryString is " + queryString);
			
			response.setContentType(type);
			response.setStatus(HttpServletResponse.SC_OK);

			if (requestUri.equals("/method/groups.getById.xml") && queryString.equals("group_ids=rockmetal80")) {
				response.getWriter().write(FileUtils.readFileToString(new File(
						"src/test/resources/groups.getById.xml")));
				LOGGER.debug("will be groups.getById.xml");
			}
			else if(requestUri.equals("/method/wall.get.xml") && queryString.equals("owner_id=-64183")) {
				response.getWriter().write(FileUtils.readFileToString(new File(
						"src/test/resources/wall.get.xml")));
				LOGGER.debug("will be wall.get.xml");
			}

			else{
				response.getWriter().println("some error in Megatest, check log4j's log");
				LOGGER.debug("unexpected request " + request.getRequestURL() + "?" + request.getQueryString() +", may be error in src/test/resources/spring-config.xml");
			}
		}
	}

	public void stop() throws Exception {
		server.stop();
		LOGGER.debug("VkEmulator stopped");
	}
}