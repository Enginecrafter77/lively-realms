package dev.enginecrafter77.livelyrealms;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class SingleBlockTerminal implements GrammarTerminal {
	private final BlockState block;

	public SingleBlockTerminal(BlockState block)
	{
		this.block = block;
	}

	@Override
	public void build(StructureGenerationContext context, CellPosition position)
	{
		context.level.setBlockAndUpdate(context.getAnchorBlockPos(position), this.block);
	}

	@Override
	public String toString()
	{
		return String.format("T(%s)", this.block.getBlockHolder().getRegisteredName());
	}

	public static SingleBlockTerminal of(Block block)
	{
		return SingleBlockTerminal.of(block.defaultBlockState());
	}

	public static SingleBlockTerminal of(BlockState blockState)
	{
		return new SingleBlockTerminal(blockState);
	}
}
