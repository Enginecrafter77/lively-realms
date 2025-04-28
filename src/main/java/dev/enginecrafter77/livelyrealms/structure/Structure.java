package dev.enginecrafter77.livelyrealms.structure;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Vector3ic;

import javax.annotation.Nullable;

public interface Structure {
	public boolean isBlockDefined(Vector3ic position);

	public BlockState getBlockAt(Vector3ic position);

	@Nullable
	public BlockEntity getBlockEntityAt(Vector3ic position);

	public Vector3ic getSize();
}
