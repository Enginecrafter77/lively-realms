package dev.enginecrafter77.livelyrealms;

import java.util.Collection;

@FunctionalInterface
public interface AlternativeSelector<T> {
	public T select(Collection<T> options, GrammarTermResolver resolver, StructureMap map, CellPosition position);
}
