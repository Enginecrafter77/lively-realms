package dev.enginecrafter77.livelyrealms;

import com.google.common.collect.ImmutableSet;

import java.util.Set;

public class AlternativeNonterminal implements GrammarNonterminal {
	private final Set<String> options;
	private final AlternativeSelector selector;

	public AlternativeNonterminal(Set<String> options, AlternativeSelector selector)
	{
		this.selector = selector;
		this.options = options;
	}

	@Override
	public boolean expand(GrammarTermResolver resolver, StructureMap map, CellPosition position)
	{
		String selected = this.selector.select(this.options, resolver, map, position);
		map.addNonterminal(position, resolver.getNonterminal(selected).orElseThrow());
		return true;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("A(");
		for(String option : this.options)
		{
			builder.append(option);
			builder.append('|');
		}
		builder.setLength(builder.length()-1); // remove last |
		builder.append(')');
		return builder.toString();
	}

	public static AlternativeNonterminalBuilder builder()
	{
		return new AlternativeNonterminalBuilder();
	}

	public static class AlternativeNonterminalBuilder
	{
		private final ImmutableSet.Builder<String> options;
		private AlternativeSelector selector;

		public AlternativeNonterminalBuilder()
		{
			this.options = ImmutableSet.builder();
			this.selector = RandomAlternativeSelector.get();
		}

		public AlternativeNonterminalBuilder or(String nonterminal)
		{
			this.options.add(nonterminal);
			return this;
		}

		public AlternativeNonterminalBuilder either(String... nonterminals)
		{
			for(String nonterminal : nonterminals)
				this.or(nonterminal);
			return this;
		}

		public AlternativeNonterminalBuilder orEpsilon()
		{
			this.or(GrammarTermResolver.EPSILON);
			return this;
		}

		public AlternativeNonterminalBuilder withSelector(AlternativeSelector selector)
		{
			this.selector = selector;
			return this;
		}

		public AlternativeNonterminal build()
		{
			return new AlternativeNonterminal(this.options.build(), this.selector);
		}
	}
}
