package dev.enginecrafter77.livelyrealms.generation.plan;

import org.joml.Vector3d;

public abstract class BuildStepAction {
	public abstract void perform(BuildContext context);
	public abstract boolean isComplete(BuildContext context);

	public boolean hasHotspot()
	{
		return false;
	}

	public double getActivationDistance()
	{
		return 6D;
	}

	public void getHotspot(BuildContext context, Vector3d dest) {}
}
