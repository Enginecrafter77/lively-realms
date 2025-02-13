package dev.enginecrafter77.livelyrealms.structure;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Vector3ic;

import javax.annotation.Nullable;

public interface Structure {
	public StructureBlock getBlockAt(Vector3ic position);
	public Vector3ic getSize();

	public static interface StructureBlock
	{
		public Vector3ic getPosition();
		public BlockState getBlockState();
		@Nullable
		public BlockEntity getBlockEntity();
	}
}
