package dev.enginecrafter77.livelyrealms;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class MultiNonterminal implements GrammarNonterminal {
	private final Map<CellPosition, String> nonterminals;

	public MultiNonterminal(Map<CellPosition, String> nonterminals)
	{
		this.nonterminals = nonterminals;
	}

	@Override
	public boolean expand(GrammarTermResolver termResolver, StructureMap map, CellPosition position)
	{
		CellPosition newpos = new CellPosition();
		for(Map.Entry<CellPosition, String> entry : this.nonterminals.entrySet())
		{
			newpos.set(position);
			newpos.add(entry.getKey());
			map.addNonterminal(newpos.copy(), termResolver.getNonterminal(entry.getValue()).get());
		}
		return true;
	}

	public static MultiNonterminalBuilder builder()
	{
		return new MultiNonterminalBuilder();
	}

	public static class MultiNonterminalBuilder
	{
		private final ImmutableMap.Builder<CellPosition, String> builder;

		public MultiNonterminalBuilder()
		{
			this.builder = ImmutableMap.builder();
		}

		public MultiNonterminalBuilder add(CellPosition position, String entry)
		{
			this.builder.put(position, entry);
			return this;
		}

		public MultiNonterminal build()
		{
			return new MultiNonterminal(this.builder.build());
		}
	}
}
