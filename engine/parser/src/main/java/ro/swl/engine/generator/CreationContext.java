package ro.swl.engine.generator;



public class CreationContext {


	private String currentModule = "";

	private String currentPackage = "";


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


}
