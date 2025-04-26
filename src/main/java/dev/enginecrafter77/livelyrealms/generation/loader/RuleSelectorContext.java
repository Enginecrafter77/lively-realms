package dev.enginecrafter77.livelyrealms.generation.loader;

import dev.enginecrafter77.livelyrealms.RuleSelector;
import dev.enginecrafter77.livelyrealms.generation.GenerationProfile;
import dev.enginecrafter77.livelyrealms.generation.GeneratorContext;
import dev.enginecrafter77.livelyrealms.generation.Grammar;
import dev.enginecrafter77.livelyrealms.generation.ReadableCellPosition;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class RuleSelectorContext {
	public final GenerationProfile profile;
	public final GeneratorContext context;
	public final ReadableCellPosition position;
	public final List<Grammar.GrammarRuleEntry> rules;

	private int selected;

	public RuleSelectorContext(GenerationProfile profile, GeneratorContext context, ReadableCellPosition position, List<Grammar.GrammarRuleEntry> rules)
	{
		this.profile = profile;
		this.context = context;
		this.position = position;
		this.rules = rules;
		this.selected = -1;
	}

	public void select(int index)
	{
		if(index < 0 || index >= this.rules.size())
			throw new IllegalArgumentException("Cannot select index <0 or >N");
		this.selected = index;
	}

	public void reset()
	{
		this.selected = -1;
	}

	public void select(String name)
	{
		for(int index = 0; index < this.rules.size(); ++index)
		{
			Grammar.GrammarRuleEntry entry = this.rules.get(index);
			if(Objects.equals(entry.name(), name))
			{
				this.select(index);
				return;
			}
		}
		this.reset();
	}

	public void select(Grammar.GrammarRuleEntry rule)
	{
		for(int index = 0; index < this.rules.size(); ++index)
		{
			Grammar.GrammarRuleEntry entry = this.rules.get(index);
			if(entry == rule) // intentional identity check
			{
				this.select(index);
				return;
			}
		}
		this.reset();
	}

	static RuleSelector wrap(Consumer<RuleSelectorContext> action)
	{
		return new RuleSelectorContextConsumer(action);
	}

	public static class RuleSelectorContextConsumer implements RuleSelector
	{
		private final Consumer<RuleSelectorContext> action;

		public RuleSelectorContextConsumer(Consumer<RuleSelectorContext> action)
		{
			this.action = action;
		}

		@Override
		public int select(GenerationProfile profile, GeneratorContext context, ReadableCellPosition expansionFor, List<Grammar.GrammarRuleEntry> availableRules)
		{
			RuleSelectorContext delegate = new RuleSelectorContext(profile, context, expansionFor, availableRules);
			this.action.accept(delegate);
			return delegate.selected;
		}
	}
}
