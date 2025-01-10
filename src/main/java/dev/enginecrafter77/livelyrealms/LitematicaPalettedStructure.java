package dev.enginecrafter77.livelyrealms;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3ic;

import java.util.List;

public class LitematicaPalettedStructure implements Structure {
    private final List<BlockStateProvider> palette;
    private final LitematicaBitVector bitVector;
    private final VoxelIndexer indexer;
    private final Vector3ic size;

    public LitematicaPalettedStructure(List<BlockStateProvider> palette, LitematicaBitVector bitVector, Vector3ic size)
    {
        this.palette = palette;
        this.bitVector = bitVector;
        this.size = size;
        this.indexer = NaturalVoxelIndexer.in(size);
    }

    @Override
    public StructureBlock getBlockAt(Vector3ic position)
    {
        int index = this.indexer.toIndex(position);
        int value = this.bitVector.get(index);
        BlockStateProvider provider = this.palette.get(value);
        return new StructureBlock() {
            @Override
            public Vector3ic getPosition() {
                return position;
            }

            @Override
            public BlockState getBlockState() {
                return provider.getBlockState();
            }

            @Override
            public @Nullable BlockEntity getBlockEntity() {
                return null;
            }
        };
    }

    @Override
    public Vector3ic getSize()
    {
        return this.size;
    }
}
