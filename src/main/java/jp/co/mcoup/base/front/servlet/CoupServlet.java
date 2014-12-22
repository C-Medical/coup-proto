package jp.co.mcoup.base.front.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.co.mcoup.base.AppConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.StandardTemplateModeHandlers;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

/**
 *
 * @author izumi_j
 *
 */
public final class CoupServlet extends HttpServlet {

	public interface RequestHandler {

	}

	private static final Logger logger = LoggerFactory.getLogger(CoupServlet.class);

	private TemplateEngine templateEngine;

	@Override
	public void init(ServletConfig sc) throws ServletException {
		final ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver();
		templateResolver.setTemplateMode(StandardTemplateModeHandlers.HTML5.getTemplateModeName());
		templateResolver.setPrefix("/WEB-INF/html/");
		templateResolver.setSuffix(".html");
		templateResolver.setCharacterEncoding(AppConstants.CHARSET);
		templateResolver.setCacheTTLMs(3600000L);
		templateResolver.setCacheable(false);

		templateEngine = new TemplateEngine();
		templateEngine.setTemplateResolver(templateResolver);
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

	}

	private void handle(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		logger.trace(req.getRequestURI());

		WebContext ctx = new WebContext(req, resp, getServletContext());
		resp.getWriter().print("hoge");

	}
}
