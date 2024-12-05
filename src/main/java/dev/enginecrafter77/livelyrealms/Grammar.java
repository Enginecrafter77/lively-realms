package dev.enginecrafter77.livelyrealms;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import java.util.Map;
import java.util.Set;

public class Grammar {
	public final Set<GrammarRule> rules;
	public final Map<String, GrammarSymbol> symbols;

	public Grammar(Map<String, GrammarSymbol> symbols, Set<GrammarRule> rules)
	{
		this.symbols = symbols;
		this.rules = rules;
	}

	public static GrammarBuilder builder()
	{
		return new GrammarBuilder();
	}

	public static class GrammarBuilder
	{
		private final ImmutableSet.Builder<GrammarRule> ruleBuilder;
		private final ImmutableMap.Builder<String, GrammarSymbol> symbolBuilder;

		public GrammarBuilder()
		{
			this.ruleBuilder = ImmutableSet.builder();
			this.symbolBuilder = ImmutableMap.builder();
		}

		public GrammarBuilder withRule(GrammarRule rule)
		{
			this.ruleBuilder.add(rule);
			return this;
		}

		public GrammarBuilder withSymbol(GrammarSymbol symbol)
		{
			this.symbolBuilder.put(symbol.getName(), symbol);
			return this;
		}

		public GrammarBuilder withAbstractSymbol(String name)
		{
			return this.withSymbol(AbstractSymbol.make(name));
		}

		public GrammarBuilder withExpressedSymbol(String name, SymbolExpression expression)
		{
			return this.withSymbol(ExpressedSymbol.of(name, expression));
		}

		public GrammarBuilder withEpsilon()
		{
			return this.withSymbol(EpsilonSymbol.INSTANCE);
		}

		public Grammar build()
		{
			return new Grammar(this.symbolBuilder.build(), this.ruleBuilder.build());
		}
	}
}
