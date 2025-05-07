package dev.enginecrafter77.livelyrealms;

import dev.enginecrafter77.livelyrealms.generation.*;

import java.util.List;
import java.util.Random;
import java.util.Set;

public class RandomRuleSelector implements RuleSelector {
	private final Random rng;

	public RandomRuleSelector(Random rng)
	{
		this.rng = rng;
	}

	public RandomRuleSelector()
	{
		this(new Random());
	}

	@Override
	public GrammarRule select(GeneratorContext context, ReadableCellPosition expansionFor, Set<Grammar.GrammarRuleEntry> availableRules)
	{
		List<GrammarRule> rules = availableRules.stream().map(Grammar.GrammarRuleEntry::rule).toList();
		return rules.get(this.rng.nextInt(rules.size()));
	}
}
