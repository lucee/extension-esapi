package org.lucee.extension.esapi.log;

import org.owasp.esapi.LogFactory;
import org.owasp.esapi.Logger;

public class NirvanaLogFactory implements LogFactory {
	@Override
	public Logger getLogger(Class clazz) {
		return this.getLogger(clazz.getName());
	}

	@Override
	public Logger getLogger(String name) {
		return new LoggerImpl(this, "ESAPI:" + name);
	}

	private static class LoggerImpl implements Logger {
		private String name;
		final NirvanaLogFactory factory;

		public LoggerImpl(NirvanaLogFactory factory, String name) {
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
			return Integer.MAX_VALUE;
		}

		@Override
		public boolean isDebugEnabled() {
			return false;
		}

		@Override
		public boolean isErrorEnabled() {
			return false;
		}

		@Override
		public boolean isFatalEnabled() {
			return false;
		}

		@Override
		public boolean isInfoEnabled() {
			return false;
		}

		@Override
		public boolean isTraceEnabled() {
			return false;
		}

		@Override
		public boolean isWarningEnabled() {
			return false;
		}

		@Override
		public void setLevel(int level) {
		}

		@Override
		public void debug(EventType et, String msg) {
		}

		@Override
		public void debug(EventType et, String msg, Throwable t) {
		}

		@Override
		public void error(EventType et, String msg) {
		}

		@Override
		public void error(EventType et, String msg, Throwable t) {
		}

		@Override
		public void fatal(EventType et, String msg) {
		}

		@Override
		public void fatal(EventType et, String msg, Throwable t) {
		}

		@Override
		public void info(EventType et, String msg) {
		}

		@Override
		public void info(EventType et, String msg, Throwable t) {
		}

		@Override
		public void trace(EventType et, String msg) {
		}

		@Override
		public void trace(EventType et, String msg, Throwable t) {
		}

		@Override
		public void warning(EventType et, String msg) {
		}

		@Override
		public void warning(EventType et, String msg, Throwable t) {
		}
	}

}