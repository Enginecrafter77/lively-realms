package dev.enginecrafter77.livelyrealms.structure;

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

    @Nullable
    private OffsetStructure findRegion(Vector3ic position)
    {
        for(OffsetStructure struct : this.structures)
        {
            if(struct.contains(position))
                return struct;
        }
        return null;
    }

    @Override
    public boolean isBlockDefined(Vector3ic position)
    {
        OffsetStructure region = this.findRegion(position);
        return region != null && region.isBlockDefined(position);
    }

    @Override
    public BlockState getBlockAt(Vector3ic position)
    {
        OffsetStructure region = this.findRegion(position);
        if(region == null)
            throw new BlockNotDefinedException(this, position);
        return region.getBlockAt(position);
    }

    @Nullable
    @Override
    public BlockEntity getBlockEntityAt(Vector3ic position)
    {
        OffsetStructure region = this.findRegion(position);
        if(region == null)
            throw new BlockNotDefinedException(this, position);
        return region.getBlockEntityAt(position);
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
            return this.isRelativePositionInBounds(this.relativePosition(position));
        }

        private Vector3i relativePosition(Vector3ic absolutePosition)
        {
            Vector3i out = new Vector3i();
            out.set(absolutePosition);
            out.sub(this.offset);
            return out;
        }

        private boolean isRelativePositionInBounds(Vector3ic relative)
        {
            Vector3ic size = this.getSize();
            return relative.minComponent() >= 0 && relative.x() < size.x() && relative.y() < size.y() && relative.z() < size.z();
        }

        @Override
        public boolean isBlockDefined(Vector3ic position)
        {
            return this.contains(position) && this.structure.isBlockDefined(this.relativePosition(position));
        }

        @Nullable
        @Override
        public BlockEntity getBlockEntityAt(Vector3ic position)
        {
            Vector3i relative = this.relativePosition(position);
            if(!this.isRelativePositionInBounds(relative))
                throw new BlockNotDefinedException(this, position);
            return this.structure.getBlockEntityAt(relative);
        }

        @Override
        public BlockState getBlockAt(Vector3ic position)
        {
            Vector3i relative = this.relativePosition(position);
            if(!this.isRelativePositionInBounds(relative))
                throw new BlockNotDefinedException(this, position);
            return this.structure.getBlockAt(relative);
        }

        @Override
        public Vector3ic getSize() {
            return this.structure.getSize();
        }
    }
}
