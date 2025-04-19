package dev.enginecrafter77.livelyrealms.generation;

import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CellNeighborhoodMatcher implements CellMatcher {
	private final Map<ImmutableCellPosition, CellMatcher> matchers;

	public CellNeighborhoodMatcher(Map<ImmutableCellPosition, CellMatcher> matchers)
	{
		this.matchers = matchers;
	}

	@Override
	public boolean matches(GeneratorContext context, ReadableCellPosition cell)
	{
		CellPosition position = new CellPosition();
		for(Map.Entry<ImmutableCellPosition, CellMatcher> entry : this.matchers.entrySet())
		{
			position.set(cell);
			position.add(entry.getKey());
			if(!entry.getValue().matches(context, position))
				return false;
		}
		return true;
	}

	public static CellNeighborhoodMatcherBuilder builder()
	{
		return new CellNeighborhoodMatcherBuilder();
	}

	public static class CellNeighborhoodMatcherBuilder
	{
		private final Map<ImmutableCellPosition, CellMatcher> matchers;

		public CellNeighborhoodMatcherBuilder()
		{
			this.matchers = new HashMap<ImmutableCellPosition, CellMatcher>();
		}

		public MatchBuilder match(ReadableCellPosition position)
		{
			return new MatchBuilder(position);
		}

		private CellNeighborhoodMatcherBuilder withMatcher(ReadableCellPosition position, CellMatcher matcher)
		{
			ImmutableCellPosition key = ImmutableCellPosition.copyOf(position);
			CellMatcher existing = this.matchers.get(key);
			if(existing != null)
				matcher = existing.and(matcher);
			this.matchers.put(key, matcher);
			return this;
		}

		public CellNeighborhoodMatcher build()
		{
			return new CellNeighborhoodMatcher(ImmutableMap.copyOf(this.matchers));
		}

		public class MatchBuilder
		{
			private final ReadableCellPosition position;

			public MatchBuilder(ReadableCellPosition position)
			{
				this.position = position;
			}

			public CellNeighborhoodMatcherBuilder using(CellMatcher matcher)
			{
				return CellNeighborhoodMatcherBuilder.this.withMatcher(this.position, matcher);
			}

			public CellNeighborhoodMatcherBuilder equals(String symbol)
			{
				return this.using(CellMatcher.isSymbol(symbol));
			}

			public CellNeighborhoodMatcherBuilder isIn(Set<String> symbols)
			{
				return this.using(CellMatcher.isSymbolIn(symbols));
			}
		}
	}
}
