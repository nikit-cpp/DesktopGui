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
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.junit.Test;

public class MegaTest {
	
	static Logger LOGGER = Logger.getLogger(MegaTest.class);
	
	@Test
	public void test() throws Exception {
		Server server = new Server(8080);

		ServletHandler handler = new ServletHandler();
		server.setHandler(handler);

		handler.addServletWithMapping(HelloServlet.class, "/*");
		server.start();

		server.join();
		LOGGER.debug("Goodbye, America!");
	}

	@SuppressWarnings("serial")
	public static class HelloServlet extends HttpServlet {
		
		private String type = "text/xml";
		
		@Override
		protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			
			String requestUri = request.getRequestURI();
			String queryString = request.getQueryString();
			
			LOGGER.debug("requestUri is " + requestUri);
			LOGGER.debug("queryString is" + queryString);
			
			response.setContentType(type);
			response.setStatus(HttpServletResponse.SC_OK);

			if(requestUri.equals("/method/wall.get.xml") && queryString.equals("owner_id=-11081630")) {
				response.getWriter().write(FileUtils.readFileToString(new File(
						"src/test/resources/responce.txt")));
			}else{
				response.getWriter().println("some error in Megatest");
			}
		}
	}
}