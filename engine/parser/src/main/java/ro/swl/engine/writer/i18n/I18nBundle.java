package ro.swl.engine.writer.i18n;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;

import javax.inject.Inject;


public class I18nBundle {

	@Inject
	private Set<Locale> supportedLocales = new HashSet<Locale>();

	@Inject
	private File targetDir;

	private Properties props = new Properties();

	private String bundleName = "get_from_app_name";


	//TODO avoid overriding existing properties here 
	//first read target directory and keep a map of existing values
	//also take into consideration that the ASTLabel and all other dynamically generated components 
	// will need to have the class retrieved from that existing file

	public void addProperty(String key) {
		props.put(key, "");
		props.put(key + ".cssClass", "");
	}


	public void addProperty(StringBuffer key) {
		addProperty(key.toString());
	}


	public void addPropertyWithoutCss(String key) {
		props.put(key, null);
	}


	public void writeInDirectory() throws IOException {
		for (Locale l : supportedLocales) {
			File resBundle = new File(targetDir, bundleName + "_" + l + ".properties");
			writePropsToStream(new FileOutputStream(resBundle));
		}
	}


	private void writePropsToStream(OutputStream stream) throws IOException {
		props.store(stream, " Default i18n resources ");
		stream.close();
	}


	public String getBundleName() {
		return bundleName;
	}


	public void setBundleName(String bundleName) {
		this.bundleName = bundleName;
	}
}
