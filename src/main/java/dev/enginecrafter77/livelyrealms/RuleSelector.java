package dev.enginecrafter77.livelyrealms;

import dev.enginecrafter77.livelyrealms.generation.GenerationProfile;
import dev.enginecrafter77.livelyrealms.generation.Grammar;
import dev.enginecrafter77.livelyrealms.generation.GrammarContext;
import dev.enginecrafter77.livelyrealms.generation.ReadableCellPosition;

import java.util.List;

public interface RuleSelector {
	public int select(GenerationProfile profile, GrammarContext context, ReadableCellPosition expansionFor, List<Grammar.GrammarRuleEntry> availableRules);

	public default RuleSelector or(RuleSelector other)
	{
		return (profile, context, expansionFor, availableRules) -> {
			int selected = RuleSelector.this.select(profile, context, expansionFor, availableRules);
			if(selected == -1)
				selected = other.select(profile, context, expansionFor, availableRules);
			return selected;
		};
	}
}
