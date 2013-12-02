package ro.swl.engine.generator;

import java.util.HashMap;
import java.util.Map;



public class GenerationContext {

	public static String PACKAGE = "package";

	public static String AUTO_DETECT_PACKAGE = "auto_package";

	private String currentModule = "";

	private String currentPackage = "";

	private Map<String, String> generationProps = new HashMap<String, String>();


	public GenerationContext() {
		generationProps.put(AUTO_DETECT_PACKAGE, "false");
	}


	public String getCurrentModule() {
		return currentModule;
	}


	public String getCurrentPackage() {
		return currentPackage;
	}


	public void setCurrentPackage(String packageName) {
		this.currentPackage = packageName;
	}


	public void setCurrentModule(String currentModule) {
		this.currentModule = currentModule;
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
}
