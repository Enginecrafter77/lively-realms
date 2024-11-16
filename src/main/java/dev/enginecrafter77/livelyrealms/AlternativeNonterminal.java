package dev.enginecrafter77.livelyrealms;

import com.google.common.collect.ImmutableSet;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class AlternativeNonterminal<P> implements GrammarNonterminal {
	private final Set<ParametrizedNonterminal<P>> options;
	private final AlternativeSelector<ParametrizedNonterminal<P>> selector;

	public AlternativeNonterminal(Set<ParametrizedNonterminal<P>> options, AlternativeSelector<ParametrizedNonterminal<P>> selector)
	{
		this.selector = selector;
		this.options = options;
	}

	@Override
	public boolean expand(GrammarTermResolver resolver, StructureMap map, CellPosition position)
	{
		map.addNonterminal(position, this.selector.select(this.options, resolver, map, position).resolve(resolver));
		return true;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("A(");
		for(ParametrizedNonterminal<P> option : this.options)
		{
			builder.append(option);
			builder.append('|');
		}
		builder.setLength(builder.length()-1); // remove last |
		builder.append(')');
		return builder.toString();
	}

	public static <P> AlternativeNonterminalBuilder<P> builder()
	{
		return new AlternativeNonterminalBuilder<P>();
	}

	public static class ParametrizedNonterminal<T>
	{
		public final String nonterminal;

		@Nullable
		public final T parameters;

		public ParametrizedNonterminal(String nonterminal, @Nullable T parameters)
		{
			this.nonterminal = nonterminal;
			this.parameters = parameters;
		}

		public GrammarNonterminal resolve(GrammarTermResolver resolver)
		{
			return resolver.getNonterminal(this.nonterminal).orElseThrow();
		}

		@Override
		public String toString()
		{
			StringBuilder rep = new StringBuilder(this.nonterminal);
			if(this.parameters != null)
			{
				rep.append('[');
				rep.append(this.parameters);
				rep.append(']');
			}
			return rep.toString();
		}
	}

	public static class AlternativeNonterminalBuilder<P>
	{
		private final ImmutableSet.Builder<ParametrizedNonterminal<P>> options;
		private AlternativeSelector<ParametrizedNonterminal<P>> selector;

		public AlternativeNonterminalBuilder()
		{
			this.options = ImmutableSet.builder();
			this.selector = RandomAlternativeSelector.get();
		}

		public AlternativeNonterminalBuilder<P> or(String nonterminal, @Nullable P parameters)
		{
			this.options.add(new ParametrizedNonterminal<P>(nonterminal, parameters));
			return this;
		}

		public AlternativeNonterminalBuilder<P> or(String nonterminal)
		{
			return this.or(nonterminal, null);
		}

		public AlternativeNonterminalBuilder<P> either(String... nonterminals)
		{
			for(String nonterminal : nonterminals)
				this.or(nonterminal);
			return this;
		}

		public AlternativeNonterminalBuilder<P> orEpsilon()
		{
			this.or(GrammarTermResolver.EPSILON);
			return this;
		}

		public AlternativeNonterminalBuilder<P> withSelector(AlternativeSelector<ParametrizedNonterminal<P>> selector)
		{
			this.selector = selector;
			return this;
		}

		public AlternativeNonterminal<P> build()
		{
			return new AlternativeNonterminal<P>(this.options.build(), this.selector);
		}
	}
}
