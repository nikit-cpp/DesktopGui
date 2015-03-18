package spark;

import static spark.Spark.*;

import spark.*;

public class SparkMain {
	// curl http://localhost:4567/method/groups.getById?group_ids=legends_of_rock
	public static void main(String[] args) {
		get("/method/:methodName", (req, res) -> {
			String methodName = req.params(":methodName");
			String parameterValue = req.queryParams("group_ids");
			
			return "method=" + methodName + " parameterValue=" + parameterValue;
		});
	}

}
