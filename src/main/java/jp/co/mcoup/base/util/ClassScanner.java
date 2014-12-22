package jp.co.mcoup.base.util;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * パス上のクラスをスキャンします。
 *
 * @author izumi_j
 *
 */
public final class ClassScanner {
	private static final Logger logger = LoggerFactory.getLogger(ClassScanner.class);

	private ClassScanner() {
	}

	/**
	 * クラス毎のコールバック。
	 *
	 * @author izumi_j
	 */
	public interface ClassVisitor {
		/**
		 * クラス名で　{@link #visit(Class)}　すべきかを判定します。
		 *
		 * @param className
		 * @return 受け入れる場合にtrue
		 */
		boolean accept(String className);

		/**
		 * 　ロードされたクラスに対する処理を実施します。
		 *
		 * @param clazz
		 */
		void visit(Class<?> clazz);
	}

	/**
	 * @param rootPath
	 * @param classVisitor
	 */
	public static void scan(final String rootPath, final ClassVisitor classVisitor) {
		final StopWatch sw = new StopWatch();
		sw.start();
		try {
			Enumeration<URL> resourceUrls = Thread.currentThread().getContextClassLoader().getResources(rootPath);
			while (resourceUrls.hasMoreElements()) {
				URL url = resourceUrls.nextElement();
				if (isJarResource(url)) {
					logger.debug("Scan jar protocol. {}", url);
					new JarWalker().walk(url, classVisitor);
				} else {
					logger.debug("Scan file protocol. {}", url);
					new ClassFileWalker().walk(url, classVisitor, rootPath);
				}
			}
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
		sw.stop();
		logger.info("Scan finished. time = {}ms", sw.getTime());
	}

	/**
	 * For class files.
	 */
	private static class ClassFileWalker {

		public void walk(final URL url, final ClassVisitor classVisitor, final String parentPath) {
			walkInternal(url, classVisitor, parentPath);
		}

		private void walkInternal(final URL url, final ClassVisitor classVisitor, final String parentPath) {
			File dir = new File(url.getFile());
			for (File file : dir.listFiles()) {
				try {
					processEntry(file, classVisitor, parentPath);
				} catch (MalformedURLException e) {
					logger.warn("Error occurred when walking class files.", e);
				} catch (ClassNotFoundException e) {
					logger.warn("Error occurred when walking class files.", e);
				}
			}
		}

		private void processEntry(final File file, final ClassVisitor classVisitor, final String parentPath)
				throws MalformedURLException, ClassNotFoundException {
			if (file.isDirectory()) {
				this.walkInternal(file.toURI().toURL(), classVisitor, parentPath + "/" + file.getName());
			} else {
				if (file.getName().endsWith(".class")) {
					final String className = fileNameToClassName(parentPath + "/" + file.getName());
					if (!classVisitor.accept(className)) {
						return;
					}
					final Class<?> clazz = loadClass(className);
					if (clazz != null) {
						classVisitor.visit(clazz);
					}
				}
			}
		}
	}

	/**
	 * For jar.
	 */
	private static class JarWalker {

		public void walk(URL url, ClassVisitor classVisitor) {
			JarFile jarFile = null;
			try {
				JarURLConnection jarURLConnection = (JarURLConnection)url.openConnection();
				jarFile = jarURLConnection.getJarFile();
				Enumeration<JarEntry> jarEntries = jarFile.entries();
				while (jarEntries.hasMoreElements()) {
					processEntry(jarEntries.nextElement(), classVisitor);
				}

			} catch (IOException e) {
				logger.warn("Error occurred when walking jars.", e);
			} finally {
				if (jarFile != null) {
					try {
						jarFile.close();
					} catch (IOException e) {
						logger.warn("Error occurred when closing jarFile.", e);
					}
				}
			}
		}

		private void processEntry(final JarEntry jarEntry, final ClassVisitor classVisitor) {
			if (jarEntry.getName().endsWith(".class")) {
				final String className = fileNameToClassName(jarEntry.getName());
				if (!classVisitor.accept(className)) {
					return;
				}
				final Class<?> clazz = loadClass(className);
				if (clazz != null) {
					classVisitor.visit(clazz);
				}
			}
		}
	}

	/**
	 * @param url
	 * @return true if url represents jar
	 */
	private static boolean isJarResource(URL url) {
		String protocol = url.getProtocol();
		return ("jar".equals(protocol) || "zip".equals(protocol) || "wsjar".equals(protocol));
	}

	/**
	 * @param source
	 * @return jp/co/worksap/companyac/Hoge.class to jp.co.worksap.companyac.Hoge
	 */
	private static String fileNameToClassName(String source) {
		return StringUtils.removeEnd(StringUtils.replace(source, "/", "."), ".class");
	}

	/**
	 * @param className
	 * @return class
	 */
	private static Class<?> loadClass(String className) {
		try {
			return ClassUtils.getClass(className);
		} catch (Throwable e) {
			logger.warn("Failed to load {}.", e);
			return null;
		}
	}
}
