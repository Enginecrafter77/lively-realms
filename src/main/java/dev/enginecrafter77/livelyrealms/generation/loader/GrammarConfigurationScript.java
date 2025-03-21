package dev.enginecrafter77.livelyrealms.generation.loader;

import dev.enginecrafter77.livelyrealms.generation.GenerationProfile;
import dev.enginecrafter77.livelyrealms.generation.Grammar;
import dev.enginecrafter77.livelyrealms.generation.expression.MultiblockExpression;
import dev.enginecrafter77.livelyrealms.generation.expression.SymbolExpressionRegistry;
import dev.enginecrafter77.livelyrealms.structure.LitematicaStructureLoader;
import dev.enginecrafter77.livelyrealms.structure.Structure;
import groovy.lang.Closure;
import groovy.lang.DelegatesTo;
import groovy.lang.Script;

import java.io.File;
import java.io.IOException;

public abstract class GrammarConfigurationScript extends Script {
	private static final LitematicaStructureLoader LOADER = new LitematicaStructureLoader();

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
		closure.setDelegate(new ExpressionSetFacade(this.expressionRegistryBuilder));
		closure.run();
	}

	public MultiblockExpression structure(String path)
	{
		try
		{
			Structure structure = LOADER.load(new File(path).toPath());
			return MultiblockExpression.of(structure);
		}
		catch(IOException exc)
		{
			throw new RuntimeException(exc);
		}
	}
}
