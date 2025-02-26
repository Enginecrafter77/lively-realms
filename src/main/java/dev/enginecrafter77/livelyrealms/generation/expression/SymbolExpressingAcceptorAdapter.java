package dev.enginecrafter77.livelyrealms.generation.expression;

import dev.enginecrafter77.livelyrealms.generation.ReadableCellPosition;
import dev.enginecrafter77.livelyrealms.generation.SymbolAcceptor;
import dev.enginecrafter77.livelyrealms.generation.lattice.MutableSymbolLattice;

public class SymbolExpressingAcceptorAdapter implements SymbolAcceptor {
	private final SymbolExpressionContext context;

	public SymbolExpressingAcceptorAdapter(SymbolExpressionContext context)
	{
		this.context = context;
	}

	@Override
	public void acceptSymbol(ReadableCellPosition cell, String symbol)
	{
		SymbolExpression expression = this.context.getGenerationProfile().expressionProvider().getExpression(symbol);
		if(expression != null)
			expression.build(this.context, cell);
	}

	public static SymbolExpressingAcceptorAdapter on(SymbolExpressionContext context)
	{
		return new SymbolExpressingAcceptorAdapter(context);
	}
}
