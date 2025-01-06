package org.lucee.extension.esapi.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.lucee.extension.esapi.functions.FunctionSupport;

import lucee.commons.io.res.Resource;
import lucee.loader.engine.CFMLEngineFactory;
import lucee.runtime.config.Config;
import lucee.runtime.config.ConfigServer;
import lucee.runtime.config.ConfigWeb;
import lucee.runtime.util.IO;

public class PropertyDeployer {
	private static boolean deployed = false;

	private static Resource create(String srcPath, String name, Resource dir) {
		if (!dir.exists()) dir.mkdirs();

		Resource f = dir.getRealResource(name);
		if (!f.exists() || fileDiff(srcPath + name, f)) createFileFromResource(srcPath + name, f);
		return f;

	}

	static boolean fileDiff(String resource, Resource file) {
		try {
			file.delete();

			InputStream is = FunctionSupport.class.getResourceAsStream(resource);
			if (is == null) throw new IOException("file [" + resource + "] does not exist.");

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			IO io = CFMLEngineFactory.getInstance().getIOUtil();
			io.copy(is, baos, true, true);
			byte[] input = baos.toByteArray();
			boolean diff = baos.toByteArray().length != file.length();
			return diff;
		}
		catch (IOException e) {
			return true;
		}
	}

	static void createFileFromResource(String resource, Resource file) {
		try {
			file.delete();

			InputStream is = FunctionSupport.class.getResourceAsStream(resource);
			if (is == null) throw new IOException("file [" + resource + "] does not exist.");
			file.createNewFile();
			CFMLEngineFactory.getInstance().getIOUtil().copy(is, file, true);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void deployIfNecessary() {
		if (deployed) return;

		Config config = CFMLEngineFactory.getInstance().getThreadConfig();
		Resource dir = null;
		if (config instanceof ConfigWeb) {
			dir = ((ConfigWeb) config).getConfigServerDir().getRealResource("esapi");
		}
		else if (config instanceof ConfigServer) {
			dir = config.getConfigDir().getRealResource("esapi");
		}
		else {
			try {
				dir = CFMLEngineFactory.getInstance().getSystemUtil().getTempDirectory().getRealResource("esapi224");
			}
			catch (IOException e) {
				dir = CFMLEngineFactory.getInstance().getSystemUtil().getSystemDirectory().getRealResource("esapi224");
			}
		}
		// create the new property files
		String file = dir.getReal("ESAPI.properties");
		create("/org/lucee/extension/esapi/resource/", "ESAPI.properties", dir);
		create("/org/lucee/extension/esapi/resource/", "validation.properties", dir);

		System.setProperty("org.owasp.esapi.resources", dir.toString());
		System.setProperty("org.owasp.esapi.opsteam", file);
		System.setProperty("org.owasp.esapi.devteam", file);
		System.setProperty("org.owasp.esapi.logSpecial.discard", "true");
		deployed = true;
	}

}
