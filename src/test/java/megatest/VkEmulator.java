package megatest;

// http://stackoverflow.com/questions/14179746/unit-test-a-servlet-with-an-embedded-jetty/14181904#14181904
// http://git.eclipse.org/c/jetty/org.eclipse.jetty.project.git/tree/examples/embedded/src/main/java/org/eclipse/jetty/embedded/MinimalServlets.java

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
	
	//@Test
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

		//server.join();
		LOGGER.debug("Goodbye, America!");
		LOGGER.debug("VkEmulator started");

	}
	


	@SuppressWarnings("serial")
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
			
			mapRequestWithLocalFile("https://api.vk.com/method/groups.getById.xml?group_ids=rockmetal80",	"src/test/resources/groups.getById.xml");
			mapRequestWithLocalFile("https://api.vk.com/method/wall.get.xml?owner_id=-64183", 				"src/test/resources/wall.get.xml");

			boolean matched = false;
			for(Mapping m: mapList){
				if (requestUri.equals(m.url.getPath()) && queryString.equals(m.url.getQuery())) {
					response.getWriter().write(FileUtils.readFileToString(m.file));
					LOGGER.debug("will be groups.getById.xml");
					matched = true;
					break;
				}
			}
			if(!matched) {
				response.getWriter().println("some error in Megatest, check log4j's log");
				LOGGER.debug("unexpected request " + request.getRequestURL() + "?" + request.getQueryString() +", may be error in src/test/resources/spring-config.xml");
			}
		}
		

		private void mapRequestWithLocalFile(String realVkUrl, String file) throws IOException{
			mapList.add(new Mapping(new URL(realVkUrl), new File(file)));
		}
		List<Mapping> mapList = new ArrayList<Mapping>();
	}
	
	public static class Mapping{
		public Mapping(URL url, File file) {
			super();
			this.url = url;
			this.file = file;
		}
		URL url;
		File file;
	}

	public void stop() throws Exception {
		server.stop();
		LOGGER.debug("VkEmulator stopped");
	}
}