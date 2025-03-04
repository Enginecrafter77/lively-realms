package dev.enginecrafter77.livelyrealms.generation.expression;

import dev.enginecrafter77.livelyrealms.generation.plan.SimpleStructureBuildPlan;
import dev.enginecrafter77.livelyrealms.generation.plan.BuildPlan;
import dev.enginecrafter77.livelyrealms.structure.Structure;

public class MultiblockExpression implements SymbolExpression {
	private final Structure struct;

	public MultiblockExpression(Structure struct)
	{
		this.struct = struct;
	}

	@Override
	public BuildPlan getBuildPlan()
	{
		return new SimpleStructureBuildPlan(this.struct);
	}

	public static MultiblockExpression of(Structure structure)
	{
		return new MultiblockExpression(structure);
	}
}
