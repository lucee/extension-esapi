package org.lucee.extension.esapi.util;

import java.io.IOException;
import java.io.InputStream;

import lucee.commons.io.res.Resource;
import lucee.loader.engine.CFMLEngineFactory;

import org.lucee.extension.esapi.functions.FunctionSupport;

public class PropertyDeployer {
	private static boolean deployed=false;
	
	private static Resource create(String srcPath, String name, Resource dir) {
		if(!dir.exists())dir.mkdirs();
		
		Resource f = dir.getRealResource(name);
		if (!f.exists())
			createFileFromResource(srcPath+name, f);
		return f;
		
	}
	static void createFileFromResource(String resource, Resource file) {
		try{
			file.delete();
			
			InputStream is = FunctionSupport.class.getResourceAsStream(resource);
			if(is==null) throw new IOException("file ["+resource+"] does not exist.");
			file.createNewFile();
			CFMLEngineFactory.getInstance().getIOUtil().copy(is, file, true);
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void deployIfNecessary() {
		if(deployed) return;
		
		Resource dir=CFMLEngineFactory.getInstance().getSystemUtil().getSystemDirectory();
        dir = dir.getRealResource("properties");
		System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%% "+dir);
		create("/org/lucee/extension/esapi/resource/","ESAPI.properties",dir);
		System.setProperty("org.owasp.esapi.resources", dir.toString());
		
		deployed=true;
	}

}
