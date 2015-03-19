package megatest;

// http://stackoverflow.com/questions/14179746/unit-test-a-servlet-with-an-embedded-jetty/14181904#14181904
// http://git.eclipse.org/c/jetty/org.eclipse.jetty.project.git/tree/examples/embedded/src/main/java/org/eclipse/jetty/embedded/MinimalServlets.java

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.junit.Test;

public class MegaTest {
	
	@Test
	public void test() throws Exception {
		Server server = new Server(8080);

		ServletHandler handler = new ServletHandler();
		server.setHandler(handler);

		handler.addServletWithMapping(HelloServlet.class, "/*");

		server.start();

		server.join();
	}

	@SuppressWarnings("serial")
	public static class HelloServlet extends HttpServlet {
		@Override
		protected void doGet(HttpServletRequest request,
				HttpServletResponse response) throws ServletException,
				IOException {
			response.setContentType("text/html");
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().println("<h1>Hello from HelloServlet</h1>");
		}
	}
}