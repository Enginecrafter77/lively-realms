package dev.enginecrafter77.livelyrealms.generation;

import dev.enginecrafter77.livelyrealms.generation.lattice.EnumerableSymbolLattice;
import dev.enginecrafter77.livelyrealms.generation.lattice.LatticeCell;
import dev.enginecrafter77.livelyrealms.generation.lattice.SparseSymbolLattice;
import net.minecraft.network.chat.Component;

public class SubstitutionRule implements GrammarRule {
	private final EnumerableSymbolLattice matchLattice;
	private final EnumerableSymbolLattice replacement;

	public SubstitutionRule(EnumerableSymbolLattice matchLattice, EnumerableSymbolLattice replacement)
	{
		this.matchLattice = matchLattice;
		this.replacement = replacement;
	}

	@Override
	public boolean isApplicable(GeneratorContext context, ReadableCellPosition position)
	{
		return this.matchLattice.match(context.getSymbolMap(), position);
	}

	@Override
	public void apply(SymbolAcceptor acceptor, GeneratorContext context, ReadableCellPosition position)
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

	@Override
	public Component describe()
	{
		return Component.literal("Replace " + this.matchLattice + " with " + this.replacement);
	}

	public static SubstitutionRuleBuilder builder()
	{
		return new SubstitutionRuleBuilder();
	}

	public static class SubstitutionRuleBuilder
	{
		private final SparseSymbolLattice.SparseSymbolLatticeBuilder matchBuilder;
		private final SparseSymbolLattice.SparseSymbolLatticeBuilder replacementBuilder;

		public SubstitutionRuleBuilder()
		{
			this.matchBuilder = SparseSymbolLattice.builder();
			this.replacementBuilder = SparseSymbolLattice.builder();
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
			SparseSymbolLattice match = this.matchBuilder.build();
			SparseSymbolLattice replacement = this.replacementBuilder.build();
			if(match.getPositions().isEmpty())
				throw new IllegalStateException("Match lattice cannot be empty!");
			if(replacement.getPositions().isEmpty())
				throw new IllegalStateException("Replacement lattice cannot be empty!");
			return new SubstitutionRule(match, replacement);
		}
	}
}
