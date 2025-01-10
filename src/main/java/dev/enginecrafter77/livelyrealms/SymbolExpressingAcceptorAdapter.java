package dev.enginecrafter77.livelyrealms;

public class SymbolExpressingAcceptorAdapter implements SymbolAcceptor {
	private final SymbolExpressionContext context;
	private final SymbolAcceptor delegated;

	public SymbolExpressingAcceptorAdapter(SymbolAcceptor delegated, SymbolExpressionContext context)
	{
		this.delegated = delegated;
		this.context = context;
	}

	@Override
	public void acceptSymbol(ReadableCellPosition cell, String symbol)
	{
		this.delegated.acceptSymbol(cell, symbol);

		SymbolExpression expression = this.context.getExpressionProvider().getExpression(symbol);
		if(expression != null)
			expression.build(this.context, cell);
	}
}
