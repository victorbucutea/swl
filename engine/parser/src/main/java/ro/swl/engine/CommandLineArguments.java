package ro.swl.engine;


import java.util.Iterator;

import ro.swl.engine.generator.Skeleton;

import com.martiansoftware.jsap.FlaggedOption;
import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPException;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.Parameter;
import com.martiansoftware.jsap.ParseException;
import com.martiansoftware.jsap.StringParser;


public class CommandLineArguments {


	private static final String PROJECT_NAME = "project-name";
	private static final String BASE_PACKAGE = "base-package";
	private static final String SKELETON = "skeleton";
	private static final String DEST_FOLDER = "destination-dir";
	private static final String MODEL_FILE = "model-file";
	private static final String INIT_VERSION = "initial-version";
	private JSAPResult result;


	@SuppressWarnings("rawtypes")
	public CommandLineArguments(String args[]) throws JSAPException {
		Parameter skeleton = new FlaggedOption(SKELETON)//@formatter:off
						        .setStringParser(new SkeletonParser())
						        .setDefault("jee-angular-jaxrs-ejb-mysql")
						        .setRequired(true)
						        .setShortFlag('s')
						        .setLongFlag(SKELETON)
						        .setHelp("The skeleton used to generate the project. You can find the list of skeletons in the 'skeletons/' directory." +
						        		"\nThe value of the parameter is the name of the root folder (e.g. jee-angular-jaxrs-ejb-mysql).");//@formatter:on


		Parameter prjName = new FlaggedOption(PROJECT_NAME)//@formatter:off
							    .setStringParser(JSAP.STRING_PARSER)
							    .setRequired(true)
							    .setShortFlag('n')
							    .setLongFlag(PROJECT_NAME)
							    .setHelp("The name of the project.");//@formatter:on

		Parameter basePkg = new FlaggedOption(BASE_PACKAGE)//@formatter:off
							    .setStringParser(JSAP.STRING_PARSER)
							    .setRequired(true)
							    .setShortFlag('p')
							    .setLongFlag(BASE_PACKAGE)
							    .setHelp("The base package for all generated code (e.g. 'ro.swl.somepkg')");//@formatter:on

		Parameter destFolder = new FlaggedOption(DEST_FOLDER)//@formatter:off
							    .setStringParser(JSAP.STRING_PARSER)
							    .setDefault(".")
							    .setShortFlag('d')
							    .setLongFlag(DEST_FOLDER)
							    .setHelp("The destination folder of the project.");//@formatter:on


		Parameter modelFile = new FlaggedOption(MODEL_FILE)//@formatter:off
							    .setStringParser(JSAP.STRING_PARSER)
							    .setDefault("model.swdl")
							    .setShortFlag('f')
							    .setLongFlag(MODEL_FILE)
							    .setHelp("The 'model' file used for generating the project.");//@formatter:on

		Parameter initVersion = new FlaggedOption(INIT_VERSION)//@formatter:off
							    .setStringParser(JSAP.STRING_PARSER)
							    .setDefault("0.1")
							    .setShortFlag('v')
							    .setLongFlag(INIT_VERSION)
							    .setHelp("The initial version of the generated project.");//@formatter:on

		JSAP jsap = new JSAP();
		jsap.registerParameter(skeleton);
		jsap.registerParameter(prjName);
		jsap.registerParameter(basePkg);
		jsap.registerParameter(destFolder);
		jsap.registerParameter(modelFile);
		jsap.registerParameter(initVersion);

		result = jsap.parse(args);

		if (!result.success()) {
			for (Iterator errs = result.getErrorMessageIterator(); errs.hasNext();) {
				System.err.println("Error: " + errs.next());
			}

			System.err.println("                " + jsap.getUsage());
			System.err.println();
			System.err.println(jsap.getHelp());
		}
	}


	public Skeleton getSkeleton() {
		return (Skeleton) result.getObject(SKELETON);
	}


	public String getBasePackage() {
		return result.getString(BASE_PACKAGE);
	}


	public String getProjectName() {
		return result.getString(PROJECT_NAME);
	}


	public String getDestinationDir() {
		return result.getString(DEST_FOLDER);
	}


	public String getModelFile() {
		return result.getString(MODEL_FILE);
	}


	public String getInitialVersion() {
		return result.getString(INIT_VERSION);
	}

	private static class SkeletonParser extends StringParser {

		@Override
		public Object parse(String arg0) throws ParseException {
			return new Skeleton(arg0);
		}

	}
}
