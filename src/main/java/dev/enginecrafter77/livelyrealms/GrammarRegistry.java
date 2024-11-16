package dev.enginecrafter77.livelyrealms;

import com.google.common.collect.ImmutableMap;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class GrammarRegistry implements GrammarTermResolver {
	public final Map<String, GrammarNonterminal> nonterminals;
	public final Map<String, String> aliases;

	public GrammarRegistry(Map<String, GrammarNonterminal> nonterminals, Map<String, String> aliases)
	{
		this.nonterminals = nonterminals;
		this.aliases = aliases;
	}

	public String resolveAlias(String alias)
	{
		while(this.aliases.containsKey(alias))
			alias = this.aliases.get(alias);
		return alias;
	}

	@Override
	public Optional<GrammarNonterminal> getNonterminal(String name)
	{
		name = this.resolveAlias(name);
		if(Objects.equals(name, EPSILON))
			return Optional.of(EpsilonNonterminal.INSTANCE);
		return Optional.ofNullable(this.nonterminals.get(name));
	}

	public static GrammarRegistryBuilder builder()
	{
		return new GrammarRegistryBuilder();
	}

	public static class GrammarRegistryBuilder
	{
		private final ImmutableMap.Builder<String, GrammarNonterminal> nonterminals;
		private final ImmutableMap.Builder<String, String> aliases;

		public GrammarRegistryBuilder()
		{
			this.nonterminals = ImmutableMap.builder();
			this.aliases = ImmutableMap.builder();
		}

		public GrammarRegistryBuilder registerNonterminal(String name, GrammarNonterminal nonterminal)
		{
			this.nonterminals.put(name, nonterminal);
			return this;
		}

		public GrammarRegistryBuilder registerTerminal(String name, GrammarTerminal terminal)
		{
			return this.registerNonterminal(name, SingularNonterminal.of(terminal));
		}

		public GrammarRegistryBuilder alias(String alias, String to)
		{
			this.aliases.put(alias, to);
			return this;
		}

		public GrammarRegistry build()
		{
			return new GrammarRegistry(this.nonterminals.build(), this.aliases.build());
		}
	}
}
