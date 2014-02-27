package ro.swl.engine.generator;

import java.io.File;
import java.util.HashMap;
import java.util.Map;


public class GlobalContext {

	public static String PACKAGE = "package";

	public static String AUTO_DETECT_PACKAGE = "auto_package";

	private Map<String, String> generationProps = new HashMap<String, String>();

	private File destinationDir;

	private File templateRootDir;

	private Map<String, String> generatedTypesRegistry = new HashMap<String, String>();

	private static GlobalContext instance;


	public static GlobalContext getGlobalCtxt() {
		if (instance == null) {
			instance = new GlobalContext();
			return instance;
		} else {
			return instance;
		}
	}


	private GlobalContext() {
		generationProps.put(AUTO_DETECT_PACKAGE, "false");
	}


	public String getDefaultPackage() {
		return getGenerationProp(PACKAGE);
	}


	public String getGenerationProp(String prop) {
		return generationProps.get(prop);
	}


	public void setProperty(String propName, String propValue) {
		generationProps.put(propName, propValue);
	}


	public boolean isAutoDetectPackage() {
		return Boolean.valueOf(generationProps.get(AUTO_DETECT_PACKAGE));
	}


	public void setDestinationDir(File destinationDir) {
		this.destinationDir = destinationDir;
	}


	public File getDestinationDir() {
		return this.destinationDir;
	}



	public File getTemplateRootDir() {
		return templateRootDir;
	}



	public void setTemplateRootDir(File templateRootDir) {
		this.templateRootDir = templateRootDir;
	}


	public void registerGeneratedType(String name, String fqName) {
		this.generatedTypesRegistry.put(name, fqName);
	}


	public String getFqNameForRegisteredType(String name) {
		return generatedTypesRegistry.get(name);
	}
}
