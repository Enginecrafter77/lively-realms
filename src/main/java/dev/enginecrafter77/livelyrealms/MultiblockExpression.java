package dev.enginecrafter77.livelyrealms;

import net.minecraft.core.BlockPos;
import org.joml.Vector3i;

public class MultiblockExpression implements SymbolExpression {
	private final Structure struct;

	public MultiblockExpression(Structure struct)
	{
		this.struct = struct;
	}

	@Override
	public void build(SymbolExpressionContext context, ReadableCellPosition position)
	{
		BlockPos.MutableBlockPos bps = new BlockPos.MutableBlockPos();
		BlockPos anchor = context.getCellAnchorBlockPos(position);
		int cellSize = context.getExpressionProvider().getCellSize();

		Vector3i pos = new Vector3i();
		for(pos.y = 0; pos.y < cellSize; ++pos.y)
		{
			for(pos.z = 0; pos.z < cellSize; ++pos.z)
			{
				for(pos.x = 0; pos.x < cellSize; ++pos.x)
				{
					bps.set(anchor);
					bps.move(pos.x, pos.y, pos.z);

					Structure.StructureBlock structureBlock = this.struct.getBlockAt(pos);
					context.getLevel().setBlockAndUpdate(bps, structureBlock.getBlockState());
				}
			}
		}
	}

	public static MultiblockExpression of(Structure struct)
	{
		return new MultiblockExpression(struct);
	}
}
