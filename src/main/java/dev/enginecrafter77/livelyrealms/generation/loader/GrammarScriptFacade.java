package dev.enginecrafter77.livelyrealms.generation.loader;

import dev.enginecrafter77.livelyrealms.generation.Grammar;
import dev.enginecrafter77.livelyrealms.generation.MinecraftStructureMap;
import groovy.lang.Closure;
import groovy.lang.DelegatesTo;
import net.minecraft.core.Direction;

public class GrammarScriptFacade {
	private final Grammar.GrammarBuilder grammarBuilder;

	public GrammarScriptFacade(Grammar.GrammarBuilder grammarBuilder)
	{
		this.grammarBuilder = grammarBuilder;
	}

	public void startWith(String startingSymbol)
	{
		this.grammarBuilder.withStartingSymbol(startingSymbol);
	}

	public void rules(@DelegatesTo(GrammarRuleSetFacade.class) Closure<?> closure)
	{
		GrammarRuleSetFacade facade = new GrammarRuleSetFacade(this.grammarBuilder);

		closure.setProperty("empty", MinecraftStructureMap.EPSILON);
		for(Direction dir : Direction.values())
			closure.setProperty(dir.getName(), dir);

		closure.setDelegate(facade);
		closure.run();
		facade.evaluate();
	}
}
