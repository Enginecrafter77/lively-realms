package dev.enginecrafter77.livelyrealms.generation.expression;

import dev.enginecrafter77.livelyrealms.generation.ReadableCellPosition;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class SingleBlockExpression implements SymbolExpression {
	private final BlockState block;

	public SingleBlockExpression(BlockState block)
	{
		this.block = block;
	}

	@Override
	public void build(SymbolExpressionContext context, ReadableCellPosition position)
	{
		context.getLevel().setBlockAndUpdate(context.getCellAnchorBlockPos(position), this.block);
	}

	@Override
	public String toString()
	{
		return String.format("T(%s)", this.block.getBlockHolder().getRegisteredName());
	}

	public static SingleBlockExpression of(Block block)
	{
		return SingleBlockExpression.of(block.defaultBlockState());
	}

	public static SingleBlockExpression of(BlockState blockState)
	{
		return new SingleBlockExpression(blockState);
	}
}
