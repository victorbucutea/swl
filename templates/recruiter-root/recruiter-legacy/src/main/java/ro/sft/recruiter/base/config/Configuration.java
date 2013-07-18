package ro.sft.recruiter.base.config;

import com.google.inject.Binder;
import com.google.inject.Module;

/**
 * Configuration will be held in GUICE and injected into interested resources
 * 
 * @author VictorBucutea
 * 
 */
public class Configuration implements Module {

	public void configure(Binder binder) {
		configureExternalAdServices(binder);
	}

	private void configureExternalAdServices(Binder binder) {

	}

}
