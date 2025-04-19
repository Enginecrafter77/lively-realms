package dev.enginecrafter77.livelyrealms.generation;

import net.minecraft.network.chat.Component;

import java.util.function.Predicate;

public interface GrammarRule {
	public boolean isApplicable(GeneratorContext context, ReadableCellPosition position);
	public void apply(SymbolAcceptor acceptor, GeneratorContext context, ReadableCellPosition position);
	public Component describe();

	public static Predicate<GrammarRule> applicable(GeneratorContext context, ReadableCellPosition position)
	{
		return (GrammarRule rule) -> rule.isApplicable(context, position);
	}
}
