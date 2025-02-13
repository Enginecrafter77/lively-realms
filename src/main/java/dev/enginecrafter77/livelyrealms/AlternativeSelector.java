package dev.enginecrafter77.livelyrealms;

import dev.enginecrafter77.livelyrealms.generation.CellPosition;
import dev.enginecrafter77.livelyrealms.generation.GrammarContext;

import java.util.Collection;

@FunctionalInterface
public interface AlternativeSelector {
	public String select(Collection<String> options, GrammarContext context, CellPosition position);
}
