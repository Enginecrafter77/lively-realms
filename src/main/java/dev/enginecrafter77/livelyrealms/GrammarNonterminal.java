package dev.enginecrafter77.livelyrealms;

public interface GrammarNonterminal {
	public boolean expand(GrammarTermResolver resolver, StructureMap map, CellPosition position);
}
