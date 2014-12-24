package jp.co.medicoup.base.front.servlet.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import jp.co.medicoup.base.front.WebApiRegistry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author izumi_j
 *
 */
public class CoupServletContextListener implements ServletContextListener {
	private static final Logger logger = LoggerFactory.getLogger(CoupServletContextListener.class);

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		logger.info("#contextInitialized");
		WebApiRegistry.initialize();
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		logger.info("#contextDestroyed");
	}

}
