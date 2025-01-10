package dev.enginecrafter77.livelyrealms;

import org.joml.Vector3i;
import org.joml.Vector3ic;

public class NaturalVoxelIndexer implements VoxelIndexer {
    private final Vector3ic space;

    public NaturalVoxelIndexer(Vector3ic space)
    {
        this.space = space;
    }

    @Override
    public int toIndex(Vector3ic position)
    {
        if(position.x() > this.space.x() || position.z() > this.space.z() || position.y() > this.space.y())
            throw new IllegalArgumentException("Position out of space bounds");
        int row = this.space.x();
        int floor = row * this.space.z();
        return position.y() * floor + position.z() * row + position.x();
    }

    @Override
    public void fromIndex(int index, Vector3i out)
    {
        int floor = index / this.space.y();
        index %= this.space.y();
        int row = index / this.space.z();
        index %= this.space.z();
        out.set(index, floor, row);
    }

    public static NaturalVoxelIndexer in(Vector3ic space)
    {
        return new NaturalVoxelIndexer(space);
    }
}
