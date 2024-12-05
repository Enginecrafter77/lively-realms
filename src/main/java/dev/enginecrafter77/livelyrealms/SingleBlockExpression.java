package dev.enginecrafter77.livelyrealms;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class SingleBlockExpression implements SymbolExpression {
	private final BlockState block;

	public SingleBlockExpression(BlockState block)
	{
		this.block = block;
	}

	@Override
	public void build(StructureGenerationContext context, ReadableCellPosition position)
	{
		context.level.setBlockAndUpdate(context.getCellAnchorBlockPos(position), this.block);
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
