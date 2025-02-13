package dev.enginecrafter77.livelyrealms.generation;

import dev.enginecrafter77.livelyrealms.generation.lattice.EnumerableSymbolLattice;
import dev.enginecrafter77.livelyrealms.generation.lattice.LatticeCell;
import dev.enginecrafter77.livelyrealms.generation.lattice.ReducedSymbolLattice;

public class SubstitutionRule implements GrammarRule {
	private final EnumerableSymbolLattice matchLattice;
	private final EnumerableSymbolLattice replacement;

	public SubstitutionRule(EnumerableSymbolLattice matchLattice, EnumerableSymbolLattice replacement)
	{
		this.matchLattice = matchLattice;
		this.replacement = replacement;
	}

	@Override
	public boolean isApplicable(GrammarContext context, ReadableCellPosition position)
	{
		return this.matchLattice.match(context.getEnvironment(), position);
	}

	@Override
	public void apply(SymbolAcceptor acceptor, GrammarContext context, ReadableCellPosition position)
	{
		CellPosition insertPos = new CellPosition();
		for(LatticeCell cell : this.replacement)
		{
			String symbol = cell.getSymbol();
			if(symbol == null)
				continue;
			insertPos.set(position);
			insertPos.add(cell.getPosition());
			acceptor.acceptSymbol(insertPos, symbol);
		}
	}

	public static SubstitutionRuleBuilder builder()
	{
		return new SubstitutionRuleBuilder();
	}

	public static class SubstitutionRuleBuilder
	{
		private final ReducedSymbolLattice.ReducedSymbolLatticeBuilder matchBuilder;
		private final ReducedSymbolLattice.ReducedSymbolLatticeBuilder replacementBuilder;

		public SubstitutionRuleBuilder()
		{
			this.matchBuilder = ReducedSymbolLattice.builder();
			this.replacementBuilder = ReducedSymbolLattice.builder();
		}

		public SubstitutionRuleBuilder match(ReadableCellPosition position, String symbol)
		{
			this.matchBuilder.set(position, symbol);
			return this;
		}

		public SubstitutionRuleBuilder put(ReadableCellPosition position, String symbol)
		{
			this.replacementBuilder.set(position, symbol);
			return this;
		}

		public SubstitutionRuleBuilder identity()
		{
			this.replacementBuilder.copy(this.matchBuilder.build());
			return this;
		}

		public SubstitutionRule build()
		{
			return new SubstitutionRule(this.matchBuilder.build(), this.replacementBuilder.build());
		}
	}
}
