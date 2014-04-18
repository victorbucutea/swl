package ro.swl.engine.generator;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class GlobalContext {

	public static String PACKAGE = "basePackage";

	public static String PROJECT_NAME = "projectName";

	public static String AUTO_DETECT_PACKAGE = "auto_package";

	public static String INIT_VERSION = "version";

	private Map<String, String> creationProps = new HashMap<String, String>();

	private Map<String, String> generatedTypesRegistry = new HashMap<String, String>();

	private Set<String> modules = new HashSet<String>();

	private File destinationDir;

	private static GlobalContext instance;


	public static GlobalContext getGlobalCtxt() {
		if (instance == null) {
			instance = new GlobalContext();
		}
		return instance;
	}


	private GlobalContext() {
		creationProps.put(AUTO_DETECT_PACKAGE, "false");
	}


	public String getDefaultPackage() {
		return getGenerationProp(PACKAGE);
	}



	public void setDefaultPackage(String basePkg) {
		creationProps.put(PACKAGE, basePkg);
	}


	public String getGenerationProp(String prop) {
		return creationProps.get(prop);
	}


	public void setProperty(String propName, String propValue) {
		creationProps.put(propName, propValue);
	}


	public boolean isAutoDetectPackage() {
		return Boolean.valueOf(creationProps.get(AUTO_DETECT_PACKAGE));
	}


	public void setDestinationDir(File destinationDir) {
		this.destinationDir = destinationDir;
	}


	public File getDestinationDir() {
		return this.destinationDir;
	}


	public void registerGeneratedType(String name, String fqName) {
		this.generatedTypesRegistry.put(name, fqName);
	}


	public String getFqNameForRegisteredType(String name) {
		return generatedTypesRegistry.get(name);
	}


	public void setProjectName(String string) {
		this.creationProps.put(PROJECT_NAME, string);
	}


	public String getProjectName() {
		return creationProps.get(PROJECT_NAME);
	}


	public void setInitialVersion(String version) {
		creationProps.put(INIT_VERSION, version);
	}


	public String getInitialVersion() {
		return creationProps.get(INIT_VERSION);
	}


	public Set<String> getModules() {
		return modules;
	}


	public void registerModule(String module) {
		modules.add(module);
	}


	public Map<String, String> getProperties() {
		return new HashMap<String, String>(creationProps);
	}


}
