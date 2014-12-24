package jp.co.medicoup.base.front.servlet.handler;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.co.medicoup.base.AppConstants;
import jp.co.medicoup.base.front.WebApi;
import jp.co.medicoup.base.front.WebApiRegistry;
import jp.co.medicoup.base.front.provide.PageResult;
import jp.co.medicoup.base.front.servlet.CoupServlet.RequestHandler;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.StandardTemplateModeHandlers;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

/**
 *
 * @author izumi_j
 *
 */
public final class PageHandler implements RequestHandler {

	private final ServletContext servletContext;// servlet-api-3.0からはrequestから取れるよ
	private final TemplateEngine templateEngine;

	public PageHandler(ServletContext servletContext) {
		this.servletContext = servletContext;

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

	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		final WebApi api = WebApiRegistry.getWebApi(req.getRequestURI());

		if (api == null) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		@SuppressWarnings("unchecked")
		final PageResult result = (PageResult)api.execute(req.getParameterMap());

		final WebContext ctx = new WebContext(req, resp, servletContext);
		ctx.setVariables(result.getVariables());
		templateEngine.process(result.getTemplateName(), ctx, resp.getWriter());
		resp.setStatus(HttpServletResponse.SC_OK);
	}
}
