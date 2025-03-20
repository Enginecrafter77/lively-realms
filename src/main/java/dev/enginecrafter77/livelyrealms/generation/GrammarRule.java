package dev.enginecrafter77.livelyrealms.generation;

import net.minecraft.network.chat.Component;

import java.util.function.Predicate;

public interface GrammarRule {
	public boolean isApplicable(GrammarContext context, ReadableCellPosition position);
	public void apply(SymbolAcceptor acceptor, GrammarContext context, ReadableCellPosition position);
	public Component describe();

	public static Predicate<GrammarRule> applicable(GrammarContext context, ReadableCellPosition position)
	{
		return (GrammarRule rule) -> rule.isApplicable(context, position);
	}
}
