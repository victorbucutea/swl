package ro.swl.engine;

import static ro.swl.engine.generator.GlobalContext.getGlobalCtxt;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ro.swl.engine.generator.CreateException;
import ro.swl.engine.generator.InternalEnhancers;
import ro.swl.engine.generator.ProjectGenerator;
import ro.swl.engine.generator.Skeleton;
import ro.swl.engine.generator.Technology;
import ro.swl.engine.generator.javaee.enhancer.EJBTechnology;
import ro.swl.engine.generator.javaee.enhancer.JPATechnology;
import ro.swl.engine.generator.javaee.enhancer.JaxRsTechnology;
import ro.swl.engine.parser.ASTSwdlApp;
import ro.swl.engine.parser.ParseException;
import ro.swl.engine.parser.SWL;
import ro.swl.engine.writer.ui.WriteException;

import com.martiansoftware.jsap.JSAPException;


public class Main {



	public static void main(String[] args) throws IOException {


		try {
			CommandLineArguments clArgs = new CommandLineArguments(args);


			Skeleton sk = clArgs.getSkeleton();
			String prjName = clArgs.getProjectName();
			String basePkg = clArgs.getBasePackage();
			String destDir = clArgs.getDestinationDir();
			String modelFileStr = clArgs.getModelFile();
			String version = clArgs.getInitialVersion();

			File destDirFile = new File(destDir);
			File modelFile = new File(modelFileStr);

			if (!inputValid(sk, destDirFile, modelFile)) {
				System.exit(-1);
			}


			getGlobalCtxt().setDestinationDir(destDirFile);
			getGlobalCtxt().setProjectName(prjName);
			getGlobalCtxt().setDefaultPackage(basePkg);
			getGlobalCtxt().setInitialVersion(version);

			List<Technology> techs = new ArrayList<Technology>();
			techs.add(new InternalEnhancers());
			techs.add(new JPATechnology());
			techs.add(new JaxRsTechnology());
			techs.add(new EJBTechnology());



			ASTSwdlApp appModel = SWL.parse(modelFile);

			ProjectGenerator generator = new ProjectGenerator(sk, techs);

			generator.create(appModel);
			generator.enhance(appModel);
			generator.write(appModel);


		} catch (CreateException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (WriteException e) {
			e.printStackTrace();
		} catch (JSAPException e) {
			e.printStackTrace();
		}
	}


	private static boolean inputValid(Skeleton sk, File destDirFile, File modelFile) throws IOException {
		boolean errPresent = false;

		if (!destDirFile.exists() || !destDirFile.isDirectory()) {
			System.err.println(destDirFile + " does not exist or is not a directory");
			errPresent = true;
		}

		if (!modelFile.exists() || !modelFile.isFile()) {
			System.err.println("Cannot find " + modelFile + ". Model file does not exist.");
			errPresent = true;
		}

		if (!sk.getSkeletonRootDir().exists()) {
			System.err.println("Cannot find 'skeletons' directory. ");
			errPresent = true;
		}
		return !errPresent;
	}
}
