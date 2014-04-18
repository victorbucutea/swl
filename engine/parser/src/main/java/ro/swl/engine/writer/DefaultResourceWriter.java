package ro.swl.engine.writer;

import static ro.swl.engine.generator.GlobalContext.getGlobalCtxt;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;

import ro.swl.engine.generator.model.Resource;
import ro.swl.engine.writer.ui.WriteException;


/**
 * Basic form of writer. It will just calculate the destination file by walking
 * up the resource tree, and it will simply copy the contents of the template
 * file into the destination folder.
 * 
 * 
 * @author VictorBucutea
 * 
 */
public class DefaultResourceWriter implements ResourceWriter {

	protected File sourceFile;
	protected Resource resource;
	private boolean isDir;


	public DefaultResourceWriter(File sourceTemplate, Resource res, boolean isDirectory) {
		this.sourceFile = sourceTemplate;
		this.resource = res;
		this.isDir = isDirectory;
	}


	@Override
	public void write() throws WriteException {
		String outputFilePath = resource.getOutputFilePath();
		File destinationDir = getGlobalCtxt().getDestinationDir();
		// String "folder/file" will be converted to a directory path
		File destinationFile = new File(destinationDir, outputFilePath);

		try {
			if (isDir) {
				destinationFile.mkdirs();
				return;
			} else {
				destinationFile.createNewFile();
			}
			preWrite(destinationFile);
			internalWrite(destinationFile);
			postWrite(destinationFile);

		} catch (IOException e) {
			throw new WriteException("Exception while writing resource :" + destinationFile, e);
		}
	}


	protected void postWrite(File destinationFile) {
	}


	protected void preWrite(File destinationFile) {
	}


	protected void internalWrite(File destinationFile) throws FileNotFoundException, IOException {
		OutputStream out = new BufferedOutputStream(new FileOutputStream(destinationFile));
		InputStream in = new BufferedInputStream(new FileInputStream(sourceFile));
		IOUtils.copy(in, out);
		in.close();
		out.close();
	}



}
