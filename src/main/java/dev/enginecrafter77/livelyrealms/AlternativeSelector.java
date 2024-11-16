package dev.enginecrafter77.livelyrealms;

import java.util.Collection;

@FunctionalInterface
public interface AlternativeSelector {
	public String select(Collection<String> options, GrammarTermResolver resolver, StructureMap map, CellPosition position);
}
