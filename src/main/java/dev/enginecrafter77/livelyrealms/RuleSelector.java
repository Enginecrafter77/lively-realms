package dev.enginecrafter77.livelyrealms;

import dev.enginecrafter77.livelyrealms.generation.*;

import javax.annotation.Nullable;
import java.util.*;

public interface RuleSelector {
	@Nullable
	public GrammarRule select(GeneratorContext context, ReadableCellPosition expansionFor, Set<Grammar.GrammarRuleEntry> availableRules);

	public default RuleSelector or(RuleSelector... others)
	{
		return this.or(Arrays.asList(others));
	}

	public default RuleSelector or(List<RuleSelector> others)
	{
		return (context, expansionFor, availableRules) -> {
			GrammarRule selected;
			RuleSelector selector = this;
			Iterator<RuleSelector> itr = others.iterator();
			while((selected = selector.select(context, expansionFor, availableRules)) == null && itr.hasNext())
				selector = itr.next();
			return selected;
		};
	}

	public static RuleSelector undefined()
	{
		return (context, expansionFor, availableRules) -> null;
	}
}
