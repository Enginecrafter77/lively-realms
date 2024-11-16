package dev.enginecrafter77.livelyrealms;

public final class EpsilonNonterminal implements GrammarNonterminal {
	public static final EpsilonNonterminal INSTANCE = new EpsilonNonterminal();

	private EpsilonNonterminal() {}

	@Override
	public boolean expand(GrammarTermResolver resolver, StructureMap map, CellPosition position)
	{
		return true;
	}

	public static EpsilonNonterminal get()
	{
		return INSTANCE;
	}
}
