package dev.enginecrafter77.livelyrealms;

import org.jetbrains.annotations.Nullable;

public class ExpressedSymbol implements GrammarSymbol {
	public final SymbolExpression expression;
	public final String name;

	public ExpressedSymbol(String name, SymbolExpression expression)
	{
		this.name = name;
		this.expression = expression;
	}

	@Override
	public String getName()
	{
		return this.name;
	}

	@Override
	public @Nullable SymbolExpression getExpression()
	{
		return this.expression;
	}

	public static ExpressedSymbol of(String name, SymbolExpression expression)
	{
		return new ExpressedSymbol(name, expression);
	}
}
