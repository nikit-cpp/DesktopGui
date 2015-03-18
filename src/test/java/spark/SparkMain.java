package spark;

import static spark.Spark.*;

import spark.*;

public class SparkMain {

	public static void main(String[] args) {
		get("/method/:name", (req, res) -> {
			String methodName = req.params(":name");
			String parameterValue = //req.params("group_ids");
					req.params().toString();
			
			return "method=" + methodName + " parameterValue=" + parameterValue;
		});
	}

}
