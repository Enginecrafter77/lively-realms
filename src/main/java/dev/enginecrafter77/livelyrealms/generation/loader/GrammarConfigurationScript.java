package dev.enginecrafter77.livelyrealms.generation.loader;

import dev.enginecrafter77.livelyrealms.generation.GenerationProfile;
import dev.enginecrafter77.livelyrealms.generation.Grammar;
import dev.enginecrafter77.livelyrealms.generation.expression.SymbolExpressionRegistry;
import groovy.lang.Closure;
import groovy.lang.DelegatesTo;
import groovy.lang.Script;

import java.nio.file.Path;

public abstract class GrammarConfigurationScript extends Script {
	public static final String PROPERTY_DIRECTORY = "structure_directory";

	private final Grammar.GrammarBuilder grammarBuilder;
	private final SymbolExpressionRegistry.SymbolExpressionRegistryBuilder expressionRegistryBuilder;

	public GrammarConfigurationScript()
	{
		this.grammarBuilder = Grammar.builder();
		this.expressionRegistryBuilder = SymbolExpressionRegistry.builder();
	}

	public abstract void configure();

	@Override
	public GenerationProfile run()
	{
		this.configure();
		return new GenerationProfile(this.grammarBuilder.build(), this.expressionRegistryBuilder.build());
	}

	public void grammar(@DelegatesTo(GrammarScriptFacade.class) Closure<?> closure)
	{
		closure.setDelegate(new GrammarScriptFacade(this.grammarBuilder));
		closure.run();
	}

	public void expressions(@DelegatesTo(ExpressionSetFacade.class) Closure<?> closure)
	{
		Path directory = (Path)this.getProperty(PROPERTY_DIRECTORY);
		closure.setDelegate(new ExpressionSetFacade(directory, this.expressionRegistryBuilder));
		closure.run();
	}
}
