package dev.enginecrafter77.livelyrealms.entity.goal;

import dev.enginecrafter77.livelyrealms.LivelyRealmsMod;
import dev.enginecrafter77.livelyrealms.entity.cap.AssignedWorkStep;
import dev.enginecrafter77.livelyrealms.entity.cap.WorkHandler;
import dev.enginecrafter77.livelyrealms.generation.plan.BuildStep;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector3d;

import javax.annotation.Nonnull;
import java.util.EnumSet;

public class WorkOnMapGoal extends Goal {
	private static final Logger LOGGER = LogManager.getLogger(WorkOnMapGoal.class);

	private final PathfinderMob entity;
	private final Vector3d hotspot;

	private int workTicks;
	@Nonnull
	private State state;

	public WorkOnMapGoal(PathfinderMob entity)
	{
		this.hotspot = new Vector3d();
		this.entity = entity;
		this.state = State.DONE;
		this.setFlags(EnumSet.of(Flag.MOVE));
	}

	@Override
	public boolean canUse()
	{
		WorkHandler holder = this.entity.getCapability(LivelyRealmsMod.CAPABILITY_WORKER);
		if(holder == null)
			return false;
		return holder.getAssignedStep() != null;
	}

	@Override
	public boolean canContinueToUse()
	{
		WorkHandler holder = this.entity.getCapability(LivelyRealmsMod.CAPABILITY_WORKER);
		if(holder == null)
			return false;
		AssignedWorkStep task = holder.getAssignedStep();
		if(task == null)
			return false;
		return this.state != State.DONE;
	}

	@Override
	public void tick()
	{
		WorkHandler holder = this.entity.getCapability(LivelyRealmsMod.CAPABILITY_WORKER);
		if(holder == null)
			throw new IllegalStateException();
		AssignedWorkStep task = holder.getAssignedStep();
		if(task == null)
			throw new IllegalStateException();

		BuildStep step = task.step();
		if(step.action().isComplete(task.getContext()))
		{
			holder.setAssignedStep(null);
			this.state = State.DONE;
			LOGGER.info("Work already done, skipping step");
			return;
		}

		if(this.state == State.MOVING)
		{
			if(step.action().hasHotspot())
			{
				step.action().getHotspot(task.getContext(), this.hotspot);
				double activationDistance = step.action().getActivationDistance();
				if(!this.entity.getNavigation().isInProgress())
				{
					this.entity.getNavigation().setMaxVisitedNodesMultiplier(10F);
					boolean found = this.entity.getNavigation().moveTo(this.hotspot.x, this.hotspot.y, this.hotspot.z, (int)Math.floor(activationDistance), this.entity.getAttributeValue(Attributes.MOVEMENT_SPEED));
					LOGGER.info("Entered new path into pathfinder. Found? {}", found);
				}
				if(this.entity.getNavigation().isDone())
				{
					this.entity.getNavigation().resetMaxVisitedNodesMultiplier();
					this.workTicks = 0;
					this.state = State.WORKING;
					LOGGER.info("Moved to work, starting work...");
				}
			}
			else
			{
				this.state = State.WORKING;
			}
		}
		if(this.state == State.WORKING)
		{
			if(++this.workTicks > 5)
			{
				step.action().perform(task.getContext());
				holder.setAssignedStep(null);
				this.state = State.DONE;
				this.entity.swing(InteractionHand.MAIN_HAND);
				LOGGER.info("Work finished");
			}
		}
	}

	@Override
	public boolean requiresUpdateEveryTick()
	{
		return this.state == State.WORKING;
	}

	@Override
	public void start()
	{
		super.start();
		this.state = State.MOVING;
	}

	public static enum State {MOVING, WORKING, DONE}
}
