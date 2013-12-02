package ro.swl.engine;

import ro.swl.engine.grammar.AngularJSGrammar;
import ro.swl.engine.grammar.Grammar;

import com.google.inject.AbstractModule;

public class GeneratorModule extends AbstractModule {

	@Override
	protected void configure() {

		bind(Grammar.class).to(AngularJSGrammar.class);

	}

}
