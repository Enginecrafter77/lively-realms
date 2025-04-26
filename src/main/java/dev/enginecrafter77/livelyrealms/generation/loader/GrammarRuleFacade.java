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
	private final String symbol;

	@Nullable
	private CellMatcher matcher;

	@Nullable
	private String name;

	private String at;

	public GrammarRuleFacade(Grammar.GrammarBuilder grammarBuilder, String symbol)
	{
		this.grammarBuilder = grammarBuilder;
		this.matcher = null;
		this.symbol = symbol;
		this.at = MinecraftStructureMap.EPSILON; // Default to placing at empty space
		this.name = null;
	}

	public GrammarRuleFacade named(String name)
	{
		this.name = name;
		return this;
	}

	public GrammarRuleFacade at(String symbol)
	{
		this.at = symbol;
		return this;
	}

	public GrammarRuleFacade when(CellMatcher matcher)
	{
		this.matcher = matcher;
		return this;
	}

	public GrammarRuleFacade when(Consumer<CellMatcherEnvironment> action)
	{
		return this.when(CellMatcherEnvironment.wrap(action));
	}

	public GrammarRuleFacade when(@DelegatesTo(CellMatcherEnvironment.class) Closure<?> closure)
	{
		closure.setResolveStrategy(Closure.DELEGATE_FIRST);
		return this.when(ClosureConsumerAction.make(closure));
	}

	public GrammarRuleFacade and(CellMatcher matcher)
	{
		if(this.matcher == null)
			return this.when(matcher);
		this.matcher = this.matcher.and(matcher);
		return this;
	}

	public GrammarRuleFacade and(Consumer<CellMatcherEnvironment> action)
	{
		return this.and(CellMatcherEnvironment.wrap(action));
	}

	public GrammarRuleFacade and(@DelegatesTo(CellMatcherEnvironment.class) Closure<?> closure)
	{
		closure.setResolveStrategy(Closure.DELEGATE_FIRST);
		return this.and(ClosureConsumerAction.make(closure));
	}

	public OffsetMatcherBuilder where(Direction direction)
	{
		return (new OffsetMatcherBuilder()).then(direction);
	}

	public OffsetMatcherBuilder and(Direction direction)
	{
		return (new OffsetMatcherBuilder()).then(direction);
	}

	void push()
	{
		CellMatcher matcher = CellMatcher.isSymbol(this.at);
		if(this.matcher != null)
			matcher = matcher.and(this.matcher);
		GrammarRule rule = new SingleSubstitutionRule(matcher, this.symbol);
		this.grammarBuilder.withRule(this.name, rule);
	}

	public class OffsetMatcherBuilder
	{
		private final CellPosition offset;
		@Nullable
		private Direction lastDirection;

		public OffsetMatcherBuilder()
		{
			this.offset = new CellPosition();
			this.lastDirection = null;
		}

		public OffsetMatcherBuilder by(int steps)
		{
			if(this.lastDirection == null)
				throw new IllegalStateException();
			this.offset.add(this.lastDirection.getNormal().multiply(steps - 1));
			return this;
		}

		public OffsetMatcherBuilder then(Direction direction)
		{
			this.lastDirection = direction;
			this.offset.add(direction.getNormal());
			return this;
		}

		public GrammarRuleFacade is(String symbol)
		{
			return this.matches(CellMatcher.isSymbol(symbol));
		}

		public GrammarRuleFacade is(Collection<String> symbols)
		{
			return this.matches(CellMatcher.isSymbolIn(ImmutableSet.copyOf(symbols)));
		}

		public GrammarRuleFacade is_not(String symbol)
		{
			return this.matches(CellMatcher.isSymbol(symbol).not());
		}

		public GrammarRuleFacade is_not(Collection<String> symbols)
		{
			return this.matches(CellMatcher.isSymbolIn(ImmutableSet.copyOf(symbols)).not());
		}

		public GrammarRuleFacade matches(CellMatcher matcher)
		{
			return GrammarRuleFacade.this.and(matcher.offset(this.offset));
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
