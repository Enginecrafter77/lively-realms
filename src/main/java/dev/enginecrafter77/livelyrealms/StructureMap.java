package dev.enginecrafter77.livelyrealms;

import java.util.Optional;

public interface StructureMap {
	public void addTerminal(CellPosition cell, GrammarTerminal terminal);
	public void addNonterminal(CellPosition cell, GrammarNonterminal nonterminal);
	public Optional<GrammarTerminal> getTerminalAt(CellPosition position);
	public Optional<GrammarNonterminal> getNonterminalAt(CellPosition position);
}
