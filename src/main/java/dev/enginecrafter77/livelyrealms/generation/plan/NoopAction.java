package dev.enginecrafter77.livelyrealms.generation.plan;

public final class NoopAction extends BuildStepAction {
	public static final NoopAction INSTANCE = new NoopAction();

	private NoopAction() {}

	@Override
	public void perform(BuildContext context) {}

	@Override
	public boolean isComplete(BuildContext context)
	{
		return true;
	}
}
