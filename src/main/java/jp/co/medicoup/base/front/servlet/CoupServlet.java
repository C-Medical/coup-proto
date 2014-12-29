package jp.co.medicoup.base.front.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.co.medicoup.base.AppConstants;
import jp.co.medicoup.base.front.servlet.handler.AjaxHandler;
import jp.co.medicoup.base.front.servlet.handler.PageHandler;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author izumi_j
 *
 */
@SuppressWarnings("serial")
public final class CoupServlet extends HttpServlet {

    public interface RequestHandler {
        void process(HttpServletRequest req, HttpServletResponse resp) throws IOException;
    }

    private static final Logger logger = LoggerFactory.getLogger(CoupServlet.class);

    private RequestHandler pageHandler;
    private AjaxHandler ajaxHandler;

    @Override
    public void init(ServletConfig sc) throws ServletException {
        pageHandler = new PageHandler(sc.getServletContext());
        ajaxHandler = new AjaxHandler(sc.getServletContext());
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        handle(req, resp);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        handle(req, resp);
    }

    /**
     * @param req
     * @param resp
     * @throws IOException
     */
    private void handle(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final String requestUrl = req.getRequestURI();
        logger.trace("Request URL = {}", requestUrl);

        resp.setCharacterEncoding(AppConstants.CHARSET);
        if (StringUtils.startsWithIgnoreCase(requestUrl, "/page")) {
            pageHandler.process(req, resp);
        } else if (StringUtils.startsWithIgnoreCase(requestUrl, "/ajax")) {
            ajaxHandler.process(req, resp);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
