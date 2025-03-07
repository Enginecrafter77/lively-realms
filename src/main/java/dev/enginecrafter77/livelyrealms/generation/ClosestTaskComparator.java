package dev.enginecrafter77.livelyrealms.generation;

import dev.enginecrafter77.livelyrealms.generation.plan.BuildContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3d;
import org.joml.Vector3dc;

import java.util.Comparator;

public class ClosestTaskComparator implements Comparator<CellMutationTask> {
	private final Vector3dc positionTo;

	public ClosestTaskComparator(Vector3dc positionTo)
	{
		this.positionTo = new Vector3d(positionTo);
	}

	public ClosestTaskComparator(double x, double y, double z)
	{
		this.positionTo = new Vector3d(x, y, z);
	}

	private void getCellCenter(CellMutationTask task, Vector3d out)
	{
		GenerationProfile profile = task.getGridContext().getGenerationProfile();
		int cellSize = profile.expressionProvider().getCellSize();
		BuildContext context = task.getContext();
		BlockPos anchor = context.anchor();

		out.set(cellSize, cellSize, cellSize);
		out.div(2D);
		out.add(anchor.getX(), anchor.getY(), anchor.getZ());
	}

	private double distToTask(CellMutationTask task, Vector3d tmp)
	{
		this.getCellCenter(task, tmp);
		return this.positionTo.distance(tmp);
	}

	@Override
	public int compare(CellMutationTask o1, CellMutationTask o2)
	{
		Vector3d tmp = new Vector3d();
		double d1 = this.distToTask(o1, tmp);
		double d2 = this.distToTask(o2, tmp);
		return Double.compare(d1, d2);
	}

	public static ClosestTaskComparator to(Vector3dc point)
	{
		return new ClosestTaskComparator(point);
	}

	public static ClosestTaskComparator to(double x, double y, double z)
	{
		return new ClosestTaskComparator(x, y, z);
	}

	public static ClosestTaskComparator to(Vec3 point)
	{
		return new ClosestTaskComparator(point.x, point.y, point.z);
	}
}
