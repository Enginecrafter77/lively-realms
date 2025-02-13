package dev.enginecrafter77.livelyrealms.structure;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3i;
import org.joml.Vector3ic;

import java.util.ArrayList;
import java.util.List;

public class CompoundStructure implements Structure {
    private final List<OffsetStructure> structures;
    private final Vector3ic size;

    public CompoundStructure(List<OffsetStructure> structures, Vector3ic size)
    {
        this.structures = structures;
        this.size = size;
    }

    @Override
    public StructureBlock getBlockAt(Vector3ic position)
    {
        for(OffsetStructure struct : this.structures)
        {
            if(struct.contains(position))
                return struct.getBlockAt(position);
        }
        return new StructureBlock() {
            @Override
            public Vector3ic getPosition()
            {
                return position;
            }

            @Override
            public BlockState getBlockState()
            {
                return Blocks.AIR.defaultBlockState();
            }

            @Nullable
            @Override
            public BlockEntity getBlockEntity()
            {
                return null;
            }
        };
    }

    @Override
    public Vector3ic getSize()
    {
        return this.size;
    }

    public static CompoundStructureBuilder builder()
    {
        return new CompoundStructureBuilder();
    }

    public static class CompoundStructureBuilder
    {
        private final List<OffsetStructure> structures;

        public CompoundStructureBuilder()
        {
            this.structures = new ArrayList<OffsetStructure>();
        }

        public CompoundStructureBuilder add(Structure structure, Vector3ic offset)
        {
            this.structures.add(new OffsetStructure(structure, offset));
            this.normalizeOffsets();
            return this;
        }

        private void normalizeOffsets()
        {
            Vector3i min = new Vector3i();
            for(OffsetStructure struct : this.structures)
                min.min(struct.offset);

            Vector3i origin = new Vector3i(0, 0, 0);
            Vector3i diff = new Vector3i();
            min.min(origin, diff);
            diff.negate();

            for(OffsetStructure struct : this.structures)
                struct.offset.add(diff);
        }

        public void calculateSize(Vector3i size)
        {
            size.set(0, 0, 0);

            Vector3i end = new Vector3i();
            for(OffsetStructure struct : this.structures)
            {
                end.set(struct.offset);
                end.add(struct.getSize());
                size.max(end);
            }
        }

        public CompoundStructure build()
        {
            Vector3i size = new Vector3i();
            this.calculateSize(size);
            return new CompoundStructure(this.structures, size);
        }
    }

    public static class OffsetStructure implements Structure
    {
        private final Structure structure;
        private final Vector3i offset;

        public OffsetStructure(Structure structure, Vector3ic offset)
        {
            this.structure = structure;
            this.offset = new Vector3i(offset);
        }

        public Vector3ic getOffset()
        {
            return this.offset;
        }

        public boolean contains(Vector3ic position)
        {
            Vector3i relativePos = new Vector3i(position);
            relativePos.sub(this.offset);
            Vector3ic size = this.getSize();

            return relativePos.x >= 0 && relativePos.y >= 0 && relativePos.z >= 0 &&
                    relativePos.x < size.x() && relativePos.y < size.y() && relativePos.z < size.z();
        }

        @Override
        public StructureBlock getBlockAt(Vector3ic position) {
            StructureBlock sup = this.structure.getBlockAt(position);
            return new StructureBlock() {
                @Override
                public Vector3ic getPosition() {
                    Vector3i pos = new Vector3i(sup.getPosition());
                    pos.add(offset);
                    return pos;
                }

                @Override
                public BlockState getBlockState()
                {
                    return sup.getBlockState();
                }

                @Override
                public @Nullable BlockEntity getBlockEntity() {
                    return sup.getBlockEntity();
                }
            };
        }

        @Override
        public Vector3ic getSize() {
            return this.structure.getSize();
        }
    }
}
