package dev.enginecrafter77.livelyrealms.generation.expression;

import com.google.common.collect.ImmutableMap;

import javax.annotation.Nullable;
import java.util.Map;

public class SymbolExpressionRegistry implements SymbolExpressionProvider {
	private final Map<String, SymbolExpression> expressions;
	private final int cellSize;

	public SymbolExpressionRegistry(Map<String, SymbolExpression> expressions, int cellSize)
	{
		this.expressions = expressions;
		this.cellSize = cellSize;
	}

	@Override
	public int getCellSize()
	{
		return this.cellSize;
	}

	@Nullable
	@Override
	public SymbolExpression getExpression(String symbol)
	{
		return this.expressions.get(symbol);
	}

	public static SymbolExpressionRegistryBuilder builder()
	{
		return new SymbolExpressionRegistryBuilder();
	}

	public static class SymbolExpressionRegistryBuilder
	{
		private final ImmutableMap.Builder<String, SymbolExpression> builder;
		private int cellSize;

		public SymbolExpressionRegistryBuilder()
		{
			this.builder = ImmutableMap.builder();
			this.cellSize = 1;
		}

		public SymbolExpressionRegistryBuilder withCellSize(int cellSize)
		{
			this.cellSize = cellSize;
			return this;
		}

		public SymbolExpressionRegistryBuilder express(String symbol, SymbolExpression expression)
		{
			this.builder.put(symbol, expression);
			return this;
		}

		public SymbolExpressionRegistry build()
		{
			return new SymbolExpressionRegistry(this.builder.build(), this.cellSize);
		}
	}
}
