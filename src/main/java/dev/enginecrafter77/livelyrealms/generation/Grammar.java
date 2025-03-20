package dev.enginecrafter77.livelyrealms.generation;

import com.google.common.collect.ImmutableSet;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Grammar {
	public static final String DEFAULT_STARTING_SYMBOL = "start";

	private final String startingSymbol;
	private final Set<GrammarRuleEntry> rules;

	private Grammar(Set<GrammarRuleEntry> rules, String startingSymbol)
	{
		this.startingSymbol = startingSymbol;
		this.rules = rules;
	}

	public String getDefaultStartingSymbol()
	{
		return this.startingSymbol;
	}

	public Collection<GrammarRuleEntry> entries()
	{
		return this.rules;
	}

	public Optional<GrammarRuleEntry> findRule(String name)
	{
		return this.entries().stream().filter(r -> Objects.equals(r.name, name)).findAny();
	}

	public Stream<GrammarRuleEntry> findApplicableRules(GrammarContext context, ReadableCellPosition cell)
	{
		return this.entries().stream().filter(r -> r.rule().isApplicable(context, cell));
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
		private String startingSymbol;

		public GrammarBuilder()
		{
			this.ruleBuilder = ImmutableSet.builder();
			this.startingSymbol = DEFAULT_STARTING_SYMBOL;
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
			return new Grammar(this.ruleBuilder.build(), this.startingSymbol);
		}
	}
}
