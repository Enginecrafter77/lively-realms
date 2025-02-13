package dev.enginecrafter77.livelyrealms.structure;

import org.joml.Vector3i;
import org.joml.Vector3ic;

public interface VoxelIndexer {
    public int toIndex(Vector3ic position);
    public void fromIndex(int index, Vector3i out);
    public int volume();
}
