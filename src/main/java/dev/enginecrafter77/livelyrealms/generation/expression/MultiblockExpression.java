package dev.enginecrafter77.livelyrealms.generation.expression;

import dev.enginecrafter77.livelyrealms.generation.ReadableCellPosition;
import dev.enginecrafter77.livelyrealms.structure.Structure;
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
		int cellSize = context.getGenerationProfile().expressionProvider().getCellSize();

		Vector3i cellPosition = new Vector3i();
		Vector3i absolutePosition = new Vector3i();
		BlockPos.MutableBlockPos absoluteBlockPosition = new BlockPos.MutableBlockPos();
		for(cellPosition.y = 0; cellPosition.y < cellSize; ++cellPosition.y)
		{
			for(cellPosition.z = 0; cellPosition.z < cellSize; ++cellPosition.z)
			{
				for(cellPosition.x = 0; cellPosition.x < cellSize; ++cellPosition.x)
				{
					context.getPositionInsideCell(position, cellPosition, absolutePosition);
					absoluteBlockPosition.set(absolutePosition.x, absolutePosition.y, absolutePosition.z);
					Structure.StructureBlock structureBlock = this.struct.getBlockAt(cellPosition);
					context.getLevel().setBlockAndUpdate(absoluteBlockPosition, structureBlock.getBlockState());
				}
			}
		}
	}

	public static MultiblockExpression of(Structure struct)
	{
		return new MultiblockExpression(struct);
	}
}
