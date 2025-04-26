package dev.enginecrafter77.livelyrealms.generation.loader;

import com.google.common.collect.ImmutableSet;
import dev.enginecrafter77.livelyrealms.generation.*;
import groovy.lang.Closure;
import groovy.lang.DelegatesTo;
import net.minecraft.core.Direction;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.function.Consumer;

public class GrammarRuleFacade {
	private final Grammar.GrammarBuilder grammarBuilder;
	private final CellNeighborhoodMatcher.CellNeighborhoodMatcherBuilder matcherBuilder;
	private final String symbol;
	@Nullable
	private String name;

	public GrammarRuleFacade(Grammar.GrammarBuilder grammarBuilder, String symbol)
	{
		this.grammarBuilder = grammarBuilder;
		this.matcherBuilder = CellNeighborhoodMatcher.builder();
		this.symbol = symbol;
		this.name = null;

		// Default to placing at empty space
		this.at(MinecraftStructureMap.EPSILON);
	}

	public GrammarRuleFacade named(String name)
	{
		this.name = name;
		return this;
	}

	public GrammarRuleFacade at(String symbol)
	{
		this.matcherBuilder.match(ImmutableCellPosition.ZERO).equals(symbol);
		return this;
	}

	public GrammarRuleFacade when(CellMatcher matcher)
	{
		return new WhereClauseConfiguration().matches(matcher);
	}

	public GrammarRuleFacade when(Consumer<CellMatcherEnvironment> action)
	{
		return new WhereClauseConfiguration().matches(action);
	}

	public GrammarRuleFacade when(@DelegatesTo(CellMatcherEnvironment.class) Closure<?> closure)
	{
		return new WhereClauseConfiguration().matches(closure);
	}

	public WhereClauseConfiguration where(Direction direction)
	{
		return (new WhereClauseConfiguration()).then(direction);
	}

	public WhereClauseConfiguration and(Direction direction)
	{
		return this.where(direction);
	}

	void push()
	{
		GrammarRule rule = new SingleSubstitutionRule(this.matcherBuilder.build(), this.symbol);
		this.grammarBuilder.withRule(this.name, rule);
	}

	public class WhereClauseConfiguration
	{
		private final CellPosition offset;
		@Nullable
		private Direction lastDirection;

		public WhereClauseConfiguration()
		{
			this.offset = new CellPosition();
			this.lastDirection = null;
		}

		public WhereClauseConfiguration by(int steps)
		{
			if(this.lastDirection == null)
				throw new IllegalStateException();
			this.offset.add(this.lastDirection.getNormal().multiply(steps-1));
			return this;
		}

		public WhereClauseConfiguration then(Direction direction)
		{
			this.lastDirection = direction;
			this.offset.add(direction.getNormal());
			return this;
		}

		public GrammarRuleFacade is(String symbol)
		{
			return this.matches(CellMatcher.isSymbol(symbol));
		}

		public GrammarRuleFacade in(Collection<String> symbols)
		{
			return this.matches(CellMatcher.isSymbolIn(ImmutableSet.copyOf(symbols)));
		}

		public GrammarRuleFacade matches(CellMatcher matcher)
		{
			GrammarRuleFacade.this.matcherBuilder.match(this.offset).using(matcher);
			return GrammarRuleFacade.this;
		}

		public GrammarRuleFacade matches(Consumer<CellMatcherEnvironment> action)
		{
			return this.matches(CellMatcherEnvironment.wrap(action));
		}

		public GrammarRuleFacade matches(@DelegatesTo(CellMatcherEnvironment.class) Closure<?> closure)
		{
			closure.setResolveStrategy(Closure.DELEGATE_FIRST);
			return this.matches(ClosureConsumerAction.make(closure));
		}
	}
}
