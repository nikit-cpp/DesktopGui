package spark;

import static spark.Spark.*;

import spark.*;

public class SparkMain {

	public static void main(String[] args) {
		get("/method/:groups.getById", (req, res) -> {
			String methodName = req.params(":groups.getById");
			String parameterValue = req.queryParams("group_ids");
			
			return "method=" + methodName + " parameterValue=" + parameterValue;
		});
	}

}
