package dev.enginecrafter77.livelyrealms;

import com.google.common.collect.ImmutableSet;

import java.util.Set;

public class Grammar {
	public final Set<GrammarRule> rules;

	public Grammar(Set<GrammarRule> rules)
	{
		this.rules = rules;
	}

	public static GrammarBuilder builder()
	{
		return new GrammarBuilder();
	}

	public static class GrammarBuilder
	{
		private final ImmutableSet.Builder<GrammarRule> ruleBuilder;

		public GrammarBuilder()
		{
			this.ruleBuilder = ImmutableSet.builder();
		}

		public GrammarBuilder withRule(GrammarRule rule)
		{
			this.ruleBuilder.add(rule);
			return this;
		}

		public Grammar build()
		{
			return new Grammar(this.ruleBuilder.build());
		}
	}
}
