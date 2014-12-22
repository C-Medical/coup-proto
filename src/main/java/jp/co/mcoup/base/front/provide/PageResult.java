package jp.co.mcoup.base.front.provide;

import java.util.Collections;
import java.util.Map;

/**
 *
 * @author izumi_j
 *
 */
public final class PageResult {

	public static PageResult withVariables(Map<String, ?> variables) {
		return new PageResult(variables);
	}

	public static PageResult redirectTo() {
		return null;
	}

	private final Map<String, ?> variables;

	private PageResult(Map<String, ?> variables) {
		this.variables = Collections.unmodifiableMap(variables);
	}

	/**
	 * @return variables
	 */
	public Map<String, ?> getVariables() {
		return variables;
	}
}
