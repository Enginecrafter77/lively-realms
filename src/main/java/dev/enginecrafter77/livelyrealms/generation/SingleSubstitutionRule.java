package dev.enginecrafter77.livelyrealms.generation;

import net.minecraft.network.chat.Component;

public record SingleSubstitutionRule(CellMatcher matcher, String output) implements GrammarRule {
	@Override
	public boolean isApplicable(GeneratorContext context, ReadableCellPosition position)
	{
		return this.matcher.matches(context, position);
	}

	@Override
	public void apply(SymbolAcceptor acceptor, GeneratorContext context, ReadableCellPosition position)
	{
		acceptor.acceptSymbol(position, this.output);
	}

	@Override
	public Component describe()
	{
		return Component.literal("Place " + this.output);
	}

	public static SingleSubstitutionRule replace(String in, String out)
	{
		return new SingleSubstitutionRule(CellMatcher.isSymbol(in), out);
	}
}
