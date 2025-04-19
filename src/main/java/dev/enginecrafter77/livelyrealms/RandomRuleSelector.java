package dev.enginecrafter77.livelyrealms;

import dev.enginecrafter77.livelyrealms.generation.*;

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
	public int select(GenerationProfile profile, GeneratorContext context, ReadableCellPosition expansionFor, List<Grammar.GrammarRuleEntry> availableRules)
	{
		return this.rng.nextInt(availableRules.size());
	}
}
