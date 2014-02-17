package ro.swl.engine.writer.template;

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

import ro.swl.engine.generator.GenerationContext;
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
 * @param <T>
 */
public class DefaultResourceWriter implements ResourceWriter {

	protected File destinationFile;
	protected File destinationFolder;
	protected File sourceFile;
	protected Resource modelResource;


	public DefaultResourceWriter(Resource modelResource) {
		this.sourceFile = modelResource.getTemplateFile();
		this.modelResource = modelResource;
	}


	private File calculateDestinationFile() {
		String outputFilePath = modelResource.getOutputFilePath();
		// String "folder/file" will be converted to a directory path
		return new File(destinationFolder, outputFilePath);
	}


	@Override
	public void write(GenerationContext ctxt) throws WriteException {
		this.destinationFolder = ctxt.getDestinationDir();
		this.destinationFile = calculateDestinationFile();

		try {
			if (sourceFile.isDirectory()) {
				destinationFile.mkdirs();
				return;
			} else {
				destinationFile.createNewFile();
			}
			internalWrite();
		} catch (IOException e) {
			throw new WriteException("Exception while writing resource :" + destinationFile, e);
		}
	}


	protected void internalWrite() throws FileNotFoundException, IOException {
		OutputStream out = new BufferedOutputStream(new FileOutputStream(destinationFile));
		InputStream in = new BufferedInputStream(new FileInputStream(sourceFile));
		IOUtils.copy(in, out);
		in.close();
		out.close();
	}
}
