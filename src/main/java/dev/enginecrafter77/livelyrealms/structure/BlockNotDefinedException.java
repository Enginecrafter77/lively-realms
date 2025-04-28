package dev.enginecrafter77.livelyrealms.structure;

import org.joml.Vector3i;
import org.joml.Vector3ic;

public class BlockNotDefinedException extends RuntimeException {
	public final Vector3i position;
	public final Structure structure;

	public BlockNotDefinedException(Structure structure, Vector3ic position)
	{
		super(String.format("Block at %s is not defined", position));
		this.position = new Vector3i(position);
		this.structure = structure;
	}
}
