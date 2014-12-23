package jp.co.mcoup.front.gate;

import java.util.HashMap;
import java.util.Map;

import jp.co.mcoup.base.front.provide.PageResult;
import jp.co.mcoup.base.front.provide.annotation.Controller;
import jp.co.mcoup.base.front.provide.annotation.GET;
import jp.co.mcoup.base.front.provide.annotation.POST;

import org.joda.time.DateTime;

@Controller
public class IndexController {

	@GET
	@POST
	public PageResult page() {
		final Map<String, Object> variables = new HashMap<>();
		variables.put("now", DateTime.now());
		return PageResult.withVariables("gate/index", variables);
	}
}
