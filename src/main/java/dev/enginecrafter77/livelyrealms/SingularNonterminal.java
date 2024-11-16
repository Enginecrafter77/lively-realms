package dev.enginecrafter77.livelyrealms;

public class SingularNonterminal implements GrammarNonterminal {
	private final GrammarTerminal terminal;

	public SingularNonterminal(GrammarTerminal terminal)
	{
		this.terminal = terminal;
	}

	@Override
	public boolean expand(GrammarTermResolver resolver, StructureMap map, CellPosition position)
	{
		map.addTerminal(position, this.terminal);
		return true;
	}

	@Override
	public String toString()
	{
		return String.format("S(%s)", this.terminal);
	}

	public static SingularNonterminal of(GrammarTerminal terminal)
	{
		return new SingularNonterminal(terminal);
	}
}
