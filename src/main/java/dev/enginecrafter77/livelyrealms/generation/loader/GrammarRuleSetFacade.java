package dev.enginecrafter77.livelyrealms.generation.loader;

import dev.enginecrafter77.livelyrealms.generation.Grammar;
import dev.enginecrafter77.livelyrealms.generation.GrammarRule;

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

	public void use(GrammarRule rule)
	{
		this.grammarBuilder.withRule(rule);
	}

	public void use(GrammarRule rule, String named)
	{
		this.grammarBuilder.withRule(named, rule);
	}
}
