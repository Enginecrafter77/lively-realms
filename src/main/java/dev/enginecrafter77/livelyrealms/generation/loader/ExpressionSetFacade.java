package dev.enginecrafter77.livelyrealms.generation.loader;

import dev.enginecrafter77.livelyrealms.generation.expression.MultiblockExpression;
import dev.enginecrafter77.livelyrealms.generation.expression.SymbolExpression;
import dev.enginecrafter77.livelyrealms.generation.expression.SymbolExpressionRegistry;
import dev.enginecrafter77.livelyrealms.structure.Structure;

public class ExpressionSetFacade {
	private final SymbolExpressionRegistry.SymbolExpressionRegistryBuilder builder;

	public ExpressionSetFacade(SymbolExpressionRegistry.SymbolExpressionRegistryBuilder builder)
	{
		this.builder = builder;
	}

	public void cellSize(int cellSize)
	{
		this.builder.withCellSize(cellSize);
	}

	public ExpressionFacade express(String symbol)
	{
		return new ExpressionFacade(symbol);
	}

	public class ExpressionFacade
	{
		private final String symbol;

		public ExpressionFacade(String symbol)
		{
			this.symbol = symbol;
		}

		public void using(SymbolExpression expression)
		{
			ExpressionSetFacade.this.builder.express(this.symbol, expression);
		}

		public void using(Structure structure)
		{
			this.using(MultiblockExpression.of(structure));
		}
	}
}
