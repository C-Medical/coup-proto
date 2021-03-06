package jp.co.medicoup.base.front.servlet.handler;

import java.io.IOException;
import java.util.Map.Entry;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.co.medicoup.base.front.WebApi;
import jp.co.medicoup.base.front.WebApiRegistry;
import jp.co.medicoup.base.front.provide.PageResult;
import jp.co.medicoup.base.front.servlet.CoupServlet.RequestHandler;

import org.thymeleaf.context.WebContext;

/**
 *
 * @author izumi_j
 *
 */
public final class PageHandler implements RequestHandler {

    private final ServletContext servletContext;// servlet-api-3.0からはrequestから取れるよ

    public PageHandler(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final WebApi api = WebApiRegistry.getWebApi(req.getRequestURI());

        if (api == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        if (!api.isSupportedMethod(req.getMethod())) {
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return;
        }

        @SuppressWarnings("unchecked")
        final PageResult result = (PageResult) api.execute(req.getParameterMap());

        if (result.isRedirect()) {
            final StringBuilder parameter = new StringBuilder();
            for (Entry<String, ?> entry : result.getRedirectParams().entrySet()) {
                parameter.append(parameter.length() == 0 ? "?" : "&");
                parameter.append(entry.getKey()).append("=").append(entry.getValue());
            }
            resp.sendRedirect(result.getRedirectUrl() + parameter.toString());
        } else {
            final WebContext ctx = new WebContext(req, resp, servletContext);
            ctx.setVariables(result.getVariables());
            resp.setContentType("text/html");
            TemplateEngineHolder.get().process(result.getTemplateName(), ctx, resp.getWriter());
            resp.setStatus(HttpServletResponse.SC_OK);
        }
    }
}
