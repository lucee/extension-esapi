package org.lucee.extension.esapi.log;

import org.owasp.esapi.LogFactory;
import org.owasp.esapi.Logger;

import lucee.loader.engine.CFMLEngineFactory;
import lucee.loader.util.Util;
import lucee.runtime.config.Config;

public class LogFactoryImpl implements LogFactory {
	private Config config;

	public LogFactoryImpl() {
		this.getConfig();
	}

	private Config getConfig() {
		if (this.config == null) {
			this.config = CFMLEngineFactory.getInstance().getThreadConfig();
		}

		return this.config;
	}

	@Override
	public Logger getLogger(Class clazz) {
		return this.getLogger(clazz.getName());
	}

	@Override
	public Logger getLogger(String name) {
		this.getConfig();
		return new LoggerImpl(this, "ESAPI:" + name);
	}

	// $FF: synthetic method
	static Config access$0(LogFactoryImpl var0) {
		return var0.getConfig();
	}

	private static class LoggerImpl implements Logger {
		private String name;
		final LogFactoryImpl factory;

		public LoggerImpl(LogFactoryImpl factory, String name) {
			this.factory = factory;
			this.name = name;
		}

		@Override
		public void always(EventType et, String msg) {
			this.trace(et, msg);
		}

		@Override
		public void always(EventType et, String msg, Throwable t) {
			this.trace(et, msg, t);
		}

		@Override
		public int getESAPILevel() {
			int l = this.getLogLevel();
			if (2 == l) {
				return 200;
			}
			else if (4 == l) {
				return 800;
			}
			else if (5 == l) {
				return 1000;
			}
			else if (1 == l) {
				return 400;
			}
			else if (l == 0) {
				return 100;
			}
			else {
				return 3 == l ? 600 : 800;
			}
		}

		@Override
		public boolean isDebugEnabled() {
			return this.getLogLevel() >= 2;
		}

		@Override
		public boolean isErrorEnabled() {
			return this.getLogLevel() >= 4;
		}

		@Override
		public boolean isFatalEnabled() {
			return this.getLogLevel() >= 5;
		}

		@Override
		public boolean isInfoEnabled() {
			return this.getLogLevel() >= 1;
		}

		@Override
		public boolean isTraceEnabled() {
			return this.getLogLevel() >= 0;
		}

		@Override
		public boolean isWarningEnabled() {
			return this.getLogLevel() >= 3;
		}

		@Override
		public void setLevel(int level) {
		}

		@Override
		public void debug(EventType et, String msg) {
			this.log(2, et, msg, (Throwable) null);
		}

		@Override
		public void debug(EventType et, String msg, Throwable t) {
			this.log(2, et, msg, t);
		}

		@Override
		public void error(EventType et, String msg) {
			this.log(4, et, msg, (Throwable) null);
		}

		@Override
		public void error(EventType et, String msg, Throwable t) {
			this.log(4, et, msg, t);
		}

		@Override
		public void fatal(EventType et, String msg) {
			this.log(5, et, msg, (Throwable) null);
		}

		@Override
		public void fatal(EventType et, String msg, Throwable t) {
			this.log(5, et, msg, t);
		}

		@Override
		public void info(EventType et, String msg) {
			this.log(1, et, msg, (Throwable) null);
		}

		@Override
		public void info(EventType et, String msg, Throwable t) {
			this.log(1, et, msg, t);
		}

		@Override
		public void trace(EventType et, String msg) {
			this.log(0, et, msg, (Throwable) null);
		}

		@Override
		public void trace(EventType et, String msg, Throwable t) {
			this.log(0, et, msg, t);
		}

		@Override
		public void warning(EventType et, String msg) {
			this.log(3, et, msg, (Throwable) null);
		}

		@Override
		public void warning(EventType et, String msg, Throwable t) {
			this.log(3, et, msg, t);
		}

		private int getLogLevel() {
			Config c = LogFactoryImpl.access$0(factory);
			return c == null ? 4 : c.getLog("application").getLogLevel();
		}

		private void log(int level, EventType et, String msg, Throwable t) {
			Config c = LogFactoryImpl.access$0(factory);
			if (c == null) {
				System.err.println(msg);
				if (t != null) {
					t.printStackTrace();
				}
			}
			else if (t != null) {
				if (Util.isEmpty(msg)) {
					c.getLog("application").log(level, this.name, t);
				}
				else {
					c.getLog("application").log(level, this.name, msg, t);
				}
			}
			else {
				c.getLog("application").log(level, this.name, msg);
			}

		}
	}
}