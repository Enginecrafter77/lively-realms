package dev.enginecrafter77.livelyrealms;

import org.jetbrains.annotations.Nullable;

public class EpsilonSymbol implements GrammarSymbol {
	public static final EpsilonSymbol INSTANCE = new EpsilonSymbol();

	@Override
	public String getName()
	{
		return GrammarSymbol.EPSILON;
	}

	@Nullable
	@Override
	public SymbolExpression getExpression()
	{
		return null;
	}
}
