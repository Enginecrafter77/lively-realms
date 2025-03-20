package dev.enginecrafter77.livelyrealms;

import dev.enginecrafter77.livelyrealms.generation.GenerationProfile;
import dev.enginecrafter77.livelyrealms.generation.Grammar;
import dev.enginecrafter77.livelyrealms.generation.GrammarContext;
import dev.enginecrafter77.livelyrealms.generation.ReadableCellPosition;

import java.util.List;
import java.util.Random;

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
	public int select(GenerationProfile profile, GrammarContext context, ReadableCellPosition expansionFor, List<Grammar.GrammarRuleEntry> availableRules)
	{
		return this.rng.nextInt(availableRules.size());
	}
}
