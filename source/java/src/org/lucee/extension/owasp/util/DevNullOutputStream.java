package org.lucee.extension.owasp.util;

import java.io.OutputStream;
import java.io.Serializable;

/**
* dev null output stream, write data to nirvana
*/
public final class DevNullOutputStream extends OutputStream implements Serializable {
	
	private static final long serialVersionUID = -6738851699671626485L;
	public static final DevNullOutputStream DEV_NULL_OUTPUT_STREAM=new DevNullOutputStream();
	
	/**
	 * Constructor of the class
	 */
	private DevNullOutputStream() {}
	
   @Override
   public void close(){}

   @Override
   public void flush() {}

   @Override
   public void write(byte[] b, int off, int len) {}

   @Override
   public void write(byte[] b) {}

   @Override
   public void write(int b) {}

}