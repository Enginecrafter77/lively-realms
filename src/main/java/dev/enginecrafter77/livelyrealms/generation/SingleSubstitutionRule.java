package dev.enginecrafter77.livelyrealms.generation;

import dev.enginecrafter77.livelyrealms.generation.lattice.EnumerableSymbolLattice;
import dev.enginecrafter77.livelyrealms.generation.lattice.SparseSymbolLattice;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;

public record SingleSubstitutionRule(EnumerableSymbolLattice condition, String output) implements GrammarRule {
	@Override
	public boolean isApplicable(GrammarContext context, ReadableCellPosition position)
	{
		return this.condition.match(context.getEnvironment(), position);
	}

	@Override
	public void apply(SymbolAcceptor acceptor, GrammarContext context, ReadableCellPosition position)
	{
		acceptor.acceptSymbol(position, this.output);
	}

	@Override
	public Component describe()
	{
		return Component.literal("Place " + this.output);
	}

	public static SingleSubstitutionRuleBuilder replace(String in, String out)
	{
		return SingleSubstitutionRule.put(out).at(in);
	}

	public static SingleSubstitutionRuleBuilder put(String output)
	{
		return new SingleSubstitutionRuleBuilder(output);
	}

	public static class SingleSubstitutionRuleBuilder
	{
		private final SparseSymbolLattice.SparseSymbolLatticeBuilder matchBuilder;
		private final String output;

		public SingleSubstitutionRuleBuilder(String output)
		{
			this.matchBuilder = SparseSymbolLattice.builder();
			this.output = output;
		}

		public SingleSubstitutionRuleBuilder where(ReadableCellPosition position, String symbol)
		{
			this.matchBuilder.set(position, symbol);
			return this;
		}

		public SingleSubstitutionRuleBuilder where(Direction direction, String symbol, int distance)
		{
			return this.where(CellPosition.copyOf(direction.getNormal().multiply(distance)), symbol);
		}

		public SingleSubstitutionRuleBuilder where(Direction direction, String symbol)
		{
			return this.where(direction, symbol, 1);
		}

		public SingleSubstitutionRuleBuilder at(String symbol)
		{
			return this.where(CellPosition.ORIGIN, symbol);
		}

		public SingleSubstitutionRule build()
		{
			return new SingleSubstitutionRule(this.matchBuilder.build(), this.output);
		}
	}
}
