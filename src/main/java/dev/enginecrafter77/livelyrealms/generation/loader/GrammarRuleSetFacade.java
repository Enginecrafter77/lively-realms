package dev.enginecrafter77.livelyrealms.generation.loader;

import dev.enginecrafter77.livelyrealms.generation.Grammar;

import java.util.ArrayList;
import java.util.List;

public class GrammarRuleSetFacade {
	private final Grammar.GrammarBuilder grammarBuilder;

	private final List<GrammarRuleFacade> activeBuilders;

	public GrammarRuleSetFacade(Grammar.GrammarBuilder grammarBuilder)
	{
		this.activeBuilders = new ArrayList<>();
		this.grammarBuilder = grammarBuilder;
	}

	void evaluate()
	{
		this.activeBuilders.forEach(GrammarRuleFacade::push);
	}

	public GrammarRuleFacade put(String symbol)
	{
		GrammarRuleFacade facade = new GrammarRuleFacade(this.grammarBuilder, symbol);
		this.activeBuilders.add(facade);
		return facade;
	}
}
