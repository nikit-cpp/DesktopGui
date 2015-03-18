package spark;

import static spark.Spark.*;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import spark.*;

public class SparkVkEmulatorTest {
	// curl http://localhost:4567/method/groups.getById?group_ids=legends_of_rock
	/*public static void main(String[] args) {
		get("/method/:methodName", (req, res) -> {
			String methodName = req.params(":methodName");
			String parameterValue = req.queryParams("group_ids");
			
			return "method=" + methodName + " parameterValue=" + parameterValue;
		});
	}*/
	
	// https://api.vk.com/method/wall.get.xml?owner_id=-11081630
	// http://localhost:4567/method/wall.get.xml?owner_id=-11081630
	@Test
	public void testGettingWall() {
		get("/method/:methodName", (req, res) -> {
			String methodName = req.params(":methodName");
			String parameterValue = req.queryParams("owner_id");
			
			if (methodName.equals("wall.get.xml") && parameterValue.equals("-11081630")) {
				res.type("text/xml");
				return FileUtils.readFileToString(new File("src/test/resources/responce.txt"), "UTF8");
			}
			
			
			return "method=" + methodName + " parameterValue=" + parameterValue;
		});
	}

}
