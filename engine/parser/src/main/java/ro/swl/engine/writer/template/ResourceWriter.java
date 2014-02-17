package ro.swl.engine.writer.template;

import ro.swl.engine.generator.GenerationContext;
import ro.swl.engine.writer.ui.WriteException;



public interface ResourceWriter {

	void write(GenerationContext ctxt) throws WriteException;

}