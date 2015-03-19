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
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.Test;

public class MegaTest {
	
	@Test
	public void test() throws Exception {
		Server server = new Server(8080);

		ServletHandler handler = new ServletHandler();
		server.setHandler(handler);

		ServletHolder sh = new ServletHolder(new HelloServlet("text/xml", FileUtils.readFileToString(new File("src/test/resources/responce.txt"))));
		handler.addServletWithMapping(sh, "/*");
		server.start();

		server.join();
	}

	@SuppressWarnings("serial")
	public static class HelloServlet extends HttpServlet {
		
		private String content;
		private String type;
		
		public HelloServlet(String content, String type) {
			this.content = content;
			this.type = type;
		}
		@Override
		protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			response.setContentType(content);
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().write(type);
		}
	}
}