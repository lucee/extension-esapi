package org.lucee.extension.esapi.log;

import org.owasp.esapi.LogFactory;
import org.owasp.esapi.Logger;

import lucee.commons.io.log.Log;
import lucee.loader.engine.CFMLEngineFactory;
import lucee.loader.util.Util;
import lucee.runtime.config.Config;

public class LogFactoryImpl implements LogFactory {

	private Config config;

	public LogFactoryImpl() {
		getConfig();
	}

	private Config getConfig() {
		if (config == null) this.config = CFMLEngineFactory.getInstance().getThreadConfig();
		return config;
	}

	@Override
	public Logger getLogger(Class clazz) {
		return getLogger(clazz.getName());
	}

	@Override
	public Logger getLogger(String name) {
		getConfig(); // take any chance to get config
		return new LoggerImpl("ESAPI:" + name);
	}

	private class LoggerImpl implements Logger {

		private String name;

		public LoggerImpl(String name) {
			this.name = name;

		}

		@Override
		public void always(EventType et, String msg) {
			trace(et, msg);
		}

		@Override
		public void always(EventType et, String msg, Throwable t) {
			trace(et, msg, t);
		}

		@Override
		public int getESAPILevel() {
			int l = getLogLevel();
			if (Log.LEVEL_DEBUG == l) return Logger.DEBUG;
			if (Log.LEVEL_ERROR == l) return Logger.ERROR;
			if (Log.LEVEL_FATAL == l) return Logger.FATAL;
			if (Log.LEVEL_INFO == l) return Logger.INFO;
			if (Log.LEVEL_TRACE == l) return Logger.TRACE;
			if (Log.LEVEL_WARN == l) return Logger.WARNING;

			return Logger.ERROR;
		}

		@Override
		public boolean isDebugEnabled() {
			return getLogLevel() >= Log.LEVEL_DEBUG;
		}

		@Override
		public boolean isErrorEnabled() {
			return getLogLevel() >= Log.LEVEL_ERROR;
		}

		@Override
		public boolean isFatalEnabled() {
			return getLogLevel() >= Log.LEVEL_FATAL;
		}

		@Override
		public boolean isInfoEnabled() {
			return getLogLevel() >= Log.LEVEL_INFO;
		}

		@Override
		public boolean isTraceEnabled() {
			return getLogLevel() >= Log.LEVEL_TRACE;
		}

		@Override
		public boolean isWarningEnabled() {
			return getLogLevel() >= Log.LEVEL_WARN;
		}

		@Override
		public void setLevel(int level) {
			// we ignore this, level is controlled with the application log
		}

		@Override
		public void debug(EventType et, String msg) {
			log(Log.LEVEL_DEBUG, et, msg, null);
		}

		@Override
		public void debug(EventType et, String msg, Throwable t) {
			log(Log.LEVEL_DEBUG, et, msg, t);
		}

		@Override
		public void error(EventType et, String msg) {
			log(Log.LEVEL_ERROR, et, msg, null);
		}

		@Override
		public void error(EventType et, String msg, Throwable t) {
			log(Log.LEVEL_ERROR, et, msg, t);
		}

		@Override
		public void fatal(EventType et, String msg) {
			log(Log.LEVEL_FATAL, et, msg, null);
		}

		@Override
		public void fatal(EventType et, String msg, Throwable t) {
			log(Log.LEVEL_FATAL, et, msg, t);
		}

		@Override
		public void info(EventType et, String msg) {
			log(Log.LEVEL_INFO, et, msg, null);
		}

		@Override
		public void info(EventType et, String msg, Throwable t) {
			log(Log.LEVEL_INFO, et, msg, t);
		}

		@Override
		public void trace(EventType et, String msg) {
			log(Log.LEVEL_TRACE, et, msg, null);
		}

		@Override
		public void trace(EventType et, String msg, Throwable t) {
			log(Log.LEVEL_TRACE, et, msg, t);
		}

		@Override
		public void warning(EventType et, String msg) {
			log(Log.LEVEL_WARN, et, msg, null);
		}

		@Override
		public void warning(EventType et, String msg, Throwable t) {
			log(Log.LEVEL_WARN, et, msg, t);
		}

		private int getLogLevel() {
			Config c = getConfig();
			if (c == null) return Log.LEVEL_ERROR;
			return c.getLog("application").getLogLevel();
		}

		private void log(int level, EventType et, String msg, Throwable t) {
			Config c = getConfig();
			if (c == null) {
				System.err.println(msg);
				if (t != null) t.printStackTrace();
			}
			else if (t != null) {
				if (Util.isEmpty(msg)) c.getLog("application").log(level, name, t);
				else c.getLog("application").log(level, name, msg, t);
			}
			else c.getLog("application").log(level, name, msg);
		}

	}
}
