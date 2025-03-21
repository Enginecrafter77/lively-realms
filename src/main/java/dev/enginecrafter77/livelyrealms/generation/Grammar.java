package dev.enginecrafter77.livelyrealms.generation;

import com.google.common.collect.ImmutableSet;
import dev.enginecrafter77.livelyrealms.RandomRuleSelector;
import dev.enginecrafter77.livelyrealms.RuleSelector;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

public record Grammar(Set<GrammarRuleEntry> rules, String startingSymbol, RuleSelector ruleSelector) {
	public static final String DEFAULT_STARTING_SYMBOL = "start";

	public Optional<GrammarRuleEntry> findRule(String name)
	{
		return this.rules.stream().filter(r -> Objects.equals(r.name, name)).findAny();
	}

	public Stream<GrammarRuleEntry> findApplicableRules(GrammarContext context, ReadableCellPosition cell)
	{
		return this.rules.stream().filter(r -> r.rule().isApplicable(context, cell));
	}

	public static GrammarBuilder builder()
	{
		return new GrammarBuilder();
	}

	public static record GrammarRuleEntry(GrammarRule rule, @Nullable String name)
	{
		public boolean isNamed()
		{
			return this.name != null;
		}

		public static Predicate<GrammarRuleEntry> named()
		{
			return GrammarRuleEntry::isNamed;
		}
	}

	public static class GrammarBuilder
	{
		private final ImmutableSet.Builder<GrammarRuleEntry> ruleBuilder;
		private RuleSelector ruleSelector;
		private String startingSymbol;

		public GrammarBuilder()
		{
			this.ruleBuilder = ImmutableSet.builder();
			this.startingSymbol = DEFAULT_STARTING_SYMBOL;
			this.ruleSelector = new RandomRuleSelector();
		}

		public GrammarBuilder withRuleSelector(RuleSelector ruleSelector)
		{
			this.ruleSelector = ruleSelector;
			return this;
		}

		public GrammarBuilder withRule(@Nullable String name, GrammarRule rule)
		{
			this.ruleBuilder.add(new GrammarRuleEntry(rule, name));
			return this;
		}

		public GrammarBuilder withRule(GrammarRule rule)
		{
			return this.withRule(null, rule);
		}

		public GrammarBuilder withStartingSymbol(String startingSymbol)
		{
			this.startingSymbol = startingSymbol;
			return this;
		}

		public Grammar build()
		{
			return new Grammar(this.ruleBuilder.build(), this.startingSymbol, this.ruleSelector);
		}
	}
}
