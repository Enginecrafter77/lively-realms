package dev.enginecrafter77.livelyrealms.generation.plan;

public class ContextAwarePlanInterpreter extends JumpingPlanInterpreter {
	private final BuildContextOwner contextOwner;

	public ContextAwarePlanInterpreter(BuildPlan plan, BuildContextOwner owner)
	{
		super(plan);
		this.contextOwner = owner;
	}

	@Override
	protected boolean isStepAcceptable(BuildStep step)
	{
		BuildContext context = this.contextOwner.getContext();
		if(context == null)
			return true;
		return !step.isComplete(context);
	}
}
