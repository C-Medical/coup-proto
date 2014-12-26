package jp.co.medicoup.front.top;

import java.util.HashMap;
import java.util.Map;

import jp.co.medicoup.base.front.provide.PageResult;
import jp.co.medicoup.base.front.provide.annotation.GET;

import org.joda.time.DateTime;

public class IndexController {

	@GET
	public PageResult first() {
		final Map<String, Object> variables = new HashMap<>();
		variables.put("now", DateTime.now());
		return PageResult.withVariables("top/index", variables);
	}
}
