package org.lucee.extension.esapi.util;

import java.io.IOException;
import java.io.InputStream;

import org.lucee.extension.esapi.functions.FunctionSupport;

import lucee.commons.io.res.Resource;
import lucee.loader.engine.CFMLEngineFactory;

public class PropertyDeployer {
	private static boolean deployed = false;

	private static Resource create(String srcPath, String name, Resource dir) {
		if (!dir.exists()) dir.mkdirs();

		Resource f = dir.getRealResource(name);
		if (!f.exists()) createFileFromResource(srcPath + name, f);
		return f;

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

		Resource dir; // TODO better way to get a dir
		try {
			dir = CFMLEngineFactory.getInstance().getSystemUtil().getTempDirectory();
		}
		catch (IOException e) {
			dir = CFMLEngineFactory.getInstance().getSystemUtil().getSystemDirectory();
		}
		dir = dir.getRealResource("properties");
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
