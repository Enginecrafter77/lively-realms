package dev.enginecrafter77.livelyrealms.structure;

import org.joml.Vector3i;
import org.joml.Vector3ic;

public class NaturalVoxelIndexer implements VoxelIndexer {
    private final Vector3ic space;

    public NaturalVoxelIndexer(Vector3ic space)
    {
        if(space.x() == 0 || space.y() == 0 || space.z() == 0)
            throw new IllegalArgumentException("An indexer space cannot have a 0 dimension!");
        this.space = space;
    }

    @Override
    public int toIndex(Vector3ic position)
    {
        if(position.x() >= this.space.x() || position.z() >= this.space.z() || position.y() >= this.space.y() || position.x() < 0 || position.y() < 0 || position.z() < 0)
            throw new IllegalArgumentException(String.format("Position (%d,%d,%d) out of space bounds (0,0,0)-(%d,%d,%d)", position.x(), position.y(), position.z(), this.space.x(), this.space.y(), this.space.z()));
        int row = this.space.x();
        int floor = row * this.space.z();
        return position.y() * floor + position.z() * row + position.x();
    }

    @Override
    public void fromIndex(int index, Vector3i out)
    {
        int rowSize = this.space.x();
        int floorSize = rowSize * this.space.z();

        int floor = index / floorSize;
        index %= floorSize;
        int row = index / rowSize;
        index %= rowSize;
        out.set(index, floor, row);
    }

    @Override
    public int volume()
    {
        return this.space.x() * this.space.y() * this.space.z();
    }

    public static NaturalVoxelIndexer in(Vector3ic space)
    {
        return new NaturalVoxelIndexer(space);
    }
}
