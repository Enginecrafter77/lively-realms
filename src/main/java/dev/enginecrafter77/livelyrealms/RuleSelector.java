package dev.enginecrafter77.livelyrealms;

import dev.enginecrafter77.livelyrealms.generation.*;

import java.util.List;

public interface RuleSelector {
	public int select(GeneratorContext context, ReadableCellPosition expansionFor, List<Grammar.GrammarRuleEntry> availableRules);

	public default RuleSelector or(RuleSelector other)
	{
		return (context, expansionFor, availableRules) -> {
			int selected = RuleSelector.this.select(context, expansionFor, availableRules);
			if(selected == -1)
				selected = other.select(context, expansionFor, availableRules);
			return selected;
		};
	}
}
