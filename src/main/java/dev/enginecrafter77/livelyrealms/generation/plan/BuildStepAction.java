package dev.enginecrafter77.livelyrealms.generation.plan;

public interface BuildStepAction {
	public void perform(BuildContext context);
	public boolean isComplete(BuildContext context);
}
