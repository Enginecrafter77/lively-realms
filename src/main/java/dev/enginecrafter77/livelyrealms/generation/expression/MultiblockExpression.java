package dev.enginecrafter77.livelyrealms.generation.expression;

import dev.enginecrafter77.livelyrealms.generation.plan.StraightForwardBuildPlan;
import dev.enginecrafter77.livelyrealms.generation.plan.StructureBuildPlan;
import dev.enginecrafter77.livelyrealms.structure.Structure;

public class MultiblockExpression implements SymbolExpression {
	private final Structure struct;

	public MultiblockExpression(Structure struct)
	{
		this.struct = struct;
	}

	@Override
	public StructureBuildPlan getBuildPlan()
	{
		return new StraightForwardBuildPlan(this.struct);
	}

	public static MultiblockExpression of(Structure structure)
	{
		return new MultiblockExpression(structure);
	}
}
