package jp.co.medicoup.base.front.servlet.handler;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.co.medicoup.base.front.WebApi;
import jp.co.medicoup.base.front.WebApiRegistry;
import jp.co.medicoup.base.front.provide.HtmlFragment;
import jp.co.medicoup.base.front.servlet.CoupServlet.RequestHandler;
import jp.co.medicoup.base.json.ObjectMapperHolder;

import org.thymeleaf.context.WebContext;

/**
 *
 * @author izumi_j
 *
 */
public final class AjaxHandler implements RequestHandler {

    private final ServletContext servletContext;// servlet-api-3.0からはrequestから取れるよ

    public AjaxHandler(ServletContext servletContext) {
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
        final Object result = api.execute(req.getParameterMap());

        if (result instanceof HtmlFragment) {
            final HtmlFragment fragment = (HtmlFragment) result;
            final WebContext ctx = new WebContext(req, resp, servletContext);
            ctx.setVariables(fragment.getVariables());
            resp.setContentType("text/html");
            TemplateEngineHolder.get().process(fragment.getTemplateName(), ctx, resp.getWriter());
        } else {
            resp.setContentType("application/json");
            ObjectMapperHolder.get().writeValue(resp.getWriter(), result);
        }
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
