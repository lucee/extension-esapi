package org.lucee.extension.esapi;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

import org.owasp.esapi.errors.ConfigurationException;
import org.owasp.esapi.reference.DefaultSecurityConfiguration;

public class CustomSecurityConfiguration extends DefaultSecurityConfiguration {
	public boolean getIntrusionDetectorDisabled() {
		return false;
	}

	@Override
	public String getAccessControlImplementation() {
		// TODO Auto-generated method stub
		return super.getAccessControlImplementation();
	}

	@Override
	public List<String> getAdditionalAllowedCipherModes() {
		// TODO Auto-generated method stub
		return super.getAdditionalAllowedCipherModes();
	}

	@Override
	public boolean getAllowMixedEncoding() {
		// TODO Auto-generated method stub
		return super.getAllowMixedEncoding();
	}

	@Override
	public boolean getAllowMultipleEncoding() {
		// TODO Auto-generated method stub
		return super.getAllowMultipleEncoding();
	}

	@Override
	public List<String> getAllowedExecutables() {
		// TODO Auto-generated method stub
		return super.getAllowedExecutables();
	}

	@Override
	public List<String> getAllowedFileExtensions() {
		// TODO Auto-generated method stub
		return super.getAllowedFileExtensions();
	}

	@Override
	public int getAllowedFileUploadSize() {
		// TODO Auto-generated method stub
		return super.getAllowedFileUploadSize();
	}

	@Override
	public int getAllowedLoginAttempts() {
		// TODO Auto-generated method stub
		return super.getAllowedLoginAttempts();
	}

	@Override
	public String getApplicationName() {
		// TODO Auto-generated method stub
		return super.getApplicationName();
	}

	@Override
	public String getAuthenticationImplementation() {
		// TODO Auto-generated method stub
		return super.getAuthenticationImplementation();
	}

	@Override
	public Boolean getBooleanProp(String arg0) throws ConfigurationException {
		// TODO Auto-generated method stub
		return super.getBooleanProp(arg0);
	}

	@Override
	public byte[] getByteArrayProp(String arg0) throws ConfigurationException {
		// TODO Auto-generated method stub
		return super.getByteArrayProp(arg0);
	}

	@Override
	public String getCharacterEncoding() {
		// TODO Auto-generated method stub
		return super.getCharacterEncoding();
	}

	@Override
	public String getCipherTransformation() {
		// TODO Auto-generated method stub
		return super.getCipherTransformation();
	}

	@Override
	public List<String> getCombinedCipherModes() {
		// TODO Auto-generated method stub
		return super.getCombinedCipherModes();
	}

	@Override
	public List<String> getDefaultCanonicalizationCodecs() {
		// TODO Auto-generated method stub
		return super.getDefaultCanonicalizationCodecs();
	}

	@Override
	public String getDigitalSignatureAlgorithm() {
		// TODO Auto-generated method stub
		return super.getDigitalSignatureAlgorithm();
	}

	@Override
	public int getDigitalSignatureKeyLength() {
		// TODO Auto-generated method stub
		return super.getDigitalSignatureKeyLength();
	}

	@Override
	public boolean getDisableIntrusionDetection() {
		// TODO Auto-generated method stub
		return super.getDisableIntrusionDetection();
	}

	@Override
	protected Properties getESAPIProperties() {
		// TODO Auto-generated method stub
		return super.getESAPIProperties();
	}

	@Override
	protected boolean getESAPIProperty(String key, boolean def) {
		// TODO Auto-generated method stub
		return super.getESAPIProperty(key, def);
	}

	@Override
	protected int getESAPIProperty(String arg0, int arg1) {
		// TODO Auto-generated method stub
		return super.getESAPIProperty(arg0, arg1);
	}

	@Override
	protected List<String> getESAPIProperty(String key, List<String> def) {
		// TODO Auto-generated method stub
		return super.getESAPIProperty(key, def);
	}

	@Override
	protected String getESAPIProperty(String key, String def) {
		// TODO Auto-generated method stub
		return super.getESAPIProperty(key, def);
	}

	@Override
	protected byte[] getESAPIPropertyEncoded(String arg0, byte[] arg1) {
		// TODO Auto-generated method stub
		return super.getESAPIPropertyEncoded(arg0, arg1);
	}

	@Override
	public String getEncoderImplementation() {
		// TODO Auto-generated method stub
		return super.getEncoderImplementation();
	}

	@Override
	public String getEncryptionAlgorithm() {
		// TODO Auto-generated method stub
		return super.getEncryptionAlgorithm();
	}

	@Override
	public String getEncryptionImplementation() {
		// TODO Auto-generated method stub
		return super.getEncryptionImplementation();
	}

	@Override
	public int getEncryptionKeyLength() {
		// TODO Auto-generated method stub
		return super.getEncryptionKeyLength();
	}

	@Override
	public String getExecutorImplementation() {
		// TODO Auto-generated method stub
		return super.getExecutorImplementation();
	}

	@Override
	public boolean getForceHttpOnlyCookies() {
		// TODO Auto-generated method stub
		return super.getForceHttpOnlyCookies();
	}

	@Override
	public boolean getForceHttpOnlySession() {
		// TODO Auto-generated method stub
		return super.getForceHttpOnlySession();
	}

	@Override
	public boolean getForceSecureCookies() {
		// TODO Auto-generated method stub
		return super.getForceSecureCookies();
	}

	@Override
	public boolean getForceSecureSession() {
		// TODO Auto-generated method stub
		return super.getForceSecureSession();
	}

	@Override
	public String getHTTPUtilitiesImplementation() {
		// TODO Auto-generated method stub
		return super.getHTTPUtilitiesImplementation();
	}

	@Override
	public String getHashAlgorithm() {
		// TODO Auto-generated method stub
		return super.getHashAlgorithm();
	}

	@Override
	public int getHashIterations() {
		// TODO Auto-generated method stub
		return super.getHashIterations();
	}

	@Override
	public String getHttpSessionIdName() {
		// TODO Auto-generated method stub
		return super.getHttpSessionIdName();
	}

	@Override
	public String getIVType() {
		// TODO Auto-generated method stub
		return super.getIVType();
	}

	@Override
	public int getIntProp(String arg0) throws ConfigurationException {
		// TODO Auto-generated method stub
		return super.getIntProp(arg0);
	}

	@Override
	public String getIntrusionDetectionImplementation() {
		// TODO Auto-generated method stub
		return super.getIntrusionDetectionImplementation();
	}

	@Override
	public String getKDFPseudoRandomFunction() {
		// TODO Auto-generated method stub
		return super.getKDFPseudoRandomFunction();
	}

	@Override
	public boolean getLenientDatesAccepted() {
		// TODO Auto-generated method stub
		return super.getLenientDatesAccepted();
	}

	@Override
	public boolean getLogApplicationName() {
		// TODO Auto-generated method stub
		return super.getLogApplicationName();
	}

	@Override
	public boolean getLogEncodingRequired() {
		// TODO Auto-generated method stub
		return super.getLogEncodingRequired();
	}

	@Override
	public String getLogImplementation() {
		// TODO Auto-generated method stub
		return super.getLogImplementation();
	}

	@Override
	public boolean getLogServerIP() {
		// TODO Auto-generated method stub
		return super.getLogServerIP();
	}

	@Override
	public byte[] getMasterKey() {
		// TODO Auto-generated method stub
		return super.getMasterKey();
	}

	@Override
	public byte[] getMasterSalt() {
		// TODO Auto-generated method stub
		return super.getMasterSalt();
	}

	@Override
	public int getMaxHttpHeaderSize() {
		// TODO Auto-generated method stub
		return super.getMaxHttpHeaderSize();
	}

	@Override
	public int getMaxOldPasswordHashes() {
		// TODO Auto-generated method stub
		return super.getMaxOldPasswordHashes();
	}

	@Override
	public String getPasswordParameterName() {
		// TODO Auto-generated method stub
		return super.getPasswordParameterName();
	}

	@Override
	public String getPreferredJCEProvider() {
		// TODO Auto-generated method stub
		return super.getPreferredJCEProvider();
	}

	@Override
	public Threshold getQuota(String arg0) {
		// TODO Auto-generated method stub
		return super.getQuota(arg0);
	}

	@Override
	public String getRandomAlgorithm() {
		// TODO Auto-generated method stub
		return super.getRandomAlgorithm();
	}

	@Override
	public String getRandomizerImplementation() {
		// TODO Auto-generated method stub
		return super.getRandomizerImplementation();
	}

	@Override
	public long getRememberTokenDuration() {
		// TODO Auto-generated method stub
		return super.getRememberTokenDuration();
	}

	@Override
	public File getResourceFile(String arg0) {
		// TODO Auto-generated method stub
		return super.getResourceFile(arg0);
	}

	@Override
	public InputStream getResourceStream(String arg0) throws IOException {
		// TODO Auto-generated method stub
		return super.getResourceStream(arg0);
	}

	@Override
	public String getResponseContentType() {
		// TODO Auto-generated method stub
		return super.getResponseContentType();
	}

	@Override
	public int getSessionAbsoluteTimeoutLength() {
		// TODO Auto-generated method stub
		return super.getSessionAbsoluteTimeoutLength();
	}

	@Override
	public int getSessionIdleTimeoutLength() {
		// TODO Auto-generated method stub
		return super.getSessionIdleTimeoutLength();
	}

	@Override
	public String getStringProp(String arg0) throws ConfigurationException {
		// TODO Auto-generated method stub
		return super.getStringProp(arg0);
	}

	@Override
	public File getUploadDirectory() {
		// TODO Auto-generated method stub
		return super.getUploadDirectory();
	}

	@Override
	public File getUploadTempDirectory() {
		// TODO Auto-generated method stub
		return super.getUploadTempDirectory();
	}

	@Override
	public String getUsernameParameterName() {
		// TODO Auto-generated method stub
		return super.getUsernameParameterName();
	}

	@Override
	public String getValidationImplementation() {
		// TODO Auto-generated method stub
		return super.getValidationImplementation();
	}

	@Override
	public Pattern getValidationPattern(String arg0) {
		// TODO Auto-generated method stub
		return super.getValidationPattern(arg0);
	}

	@Override
	public File getWorkingDirectory() {
		// TODO Auto-generated method stub
		return super.getWorkingDirectory();
	}

	@Override
	protected void loadConfiguration() throws IOException {
		// TODO Auto-generated method stub
		super.loadConfiguration();
	}

	@Override
	public boolean overwritePlainText() {
		// TODO Auto-generated method stub
		return super.overwritePlainText();
	}

	@Override
	public String setCipherTransformation(String cipherXform) {
		// TODO Auto-generated method stub
		return super.setCipherTransformation(cipherXform);
	}

	@Override
	public void setResourceDirectory(String arg0) {
		// TODO Auto-generated method stub
		super.setResourceDirectory(arg0);
	}

	@Override
	protected boolean shouldPrintProperties() {
		// TODO Auto-generated method stub
		return super.shouldPrintProperties();
	}

	@Override
	public boolean useMACforCipherText() {
		// TODO Auto-generated method stub
		return super.useMACforCipherText();
	}
}