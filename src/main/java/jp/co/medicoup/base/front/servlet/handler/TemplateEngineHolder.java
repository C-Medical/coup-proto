package jp.co.medicoup.base.front.servlet.handler;

import jp.co.medicoup.base.AppConstants;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.StandardTemplateModeHandlers;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

/**
 *
 * @author izumi_j
 *
 */
public final class TemplateEngineHolder {

	private TemplateEngineHolder() {
	}

	private static final TemplateEngine templateEngine;
	static {
		final ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver();
		templateResolver.setTemplateMode(StandardTemplateModeHandlers.HTML5.getTemplateModeName());
		templateResolver.setPrefix("/WEB-INF/template/");
		templateResolver.setSuffix(".html");
		templateResolver.setCharacterEncoding(AppConstants.CHARSET);
		templateResolver.setCacheTTLMs(3600000L);
		templateResolver.setCacheable(false);

		templateEngine = new TemplateEngine();
		templateEngine.setTemplateResolver(templateResolver);
	}

	public static TemplateEngine get() {
		return templateEngine;
	}

}
