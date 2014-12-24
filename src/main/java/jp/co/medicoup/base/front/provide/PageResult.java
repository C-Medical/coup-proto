package jp.co.medicoup.base.front.provide;

import java.util.Collections;
import java.util.Map;

/**
 *
 * @author izumi_j
 *
 */
public final class PageResult {

	public static PageResult withVariables(String templateName, Map<String, ?> variables) {
		return new PageResult(templateName, variables, null, null);
	}

	public static PageResult redirectTo(String url) {
		return new PageResult(null, null, url, null);
	}

	public static PageResult redirectTo(String url, Map<String, ?> params) {
		return new PageResult(null, null, url, params);
	}

	private final String templateName;
	private final Map<String, ?> variables;

	private final String redirectUrl;
	private final Map<String, ?> redirectParams;

	public PageResult(String templateName, Map<String, ?> variables, String redirectUrl, Map<String, ?> redirectParams) {
		this.templateName = templateName;
		this.variables = variables != null ? Collections.unmodifiableMap(variables) : null;
		this.redirectUrl = redirectUrl;
		this.redirectParams = redirectParams != null ? Collections.unmodifiableMap(redirectParams) : null;
	}

	public String getTemplateName() {
		return templateName;
	}

	public Map<String, ?> getVariables() {
		return variables;
	}

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public Map<String, ?> getRedirectParams() {
		return redirectParams;
	}

}
