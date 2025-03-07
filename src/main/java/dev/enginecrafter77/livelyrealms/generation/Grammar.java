package dev.enginecrafter77.livelyrealms.generation;

import com.google.common.collect.ImmutableSet;

import java.util.Set;

public record Grammar(Set<GrammarRule> rules, String startingSymbol) {
	public static final String DEFAULT_STARTING_SYMBOL = "start";

	public static GrammarBuilder builder()
	{
		return new GrammarBuilder();
	}

	public static class GrammarBuilder
	{
		private final ImmutableSet.Builder<GrammarRule> ruleBuilder;
		private String startingSymbol;

		public GrammarBuilder()
		{
			this.ruleBuilder = ImmutableSet.builder();
			this.startingSymbol = DEFAULT_STARTING_SYMBOL;
		}

		public GrammarBuilder withRule(GrammarRule rule)
		{
			this.ruleBuilder.add(rule);
			return this;
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
