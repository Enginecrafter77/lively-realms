package dev.enginecrafter77.livelyrealms.generation.expression;

import dev.enginecrafter77.livelyrealms.generation.plan.ClearAreaForStructurePlan;
import dev.enginecrafter77.livelyrealms.generation.plan.SimpleStructureBuildPlan;
import dev.enginecrafter77.livelyrealms.generation.plan.BuildPlan;
import dev.enginecrafter77.livelyrealms.generation.plan.StagedBuildPlan;
import dev.enginecrafter77.livelyrealms.structure.FilteredStructure;
import dev.enginecrafter77.livelyrealms.structure.Structure;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Vector3ic;

import javax.annotation.Nullable;

public class MultiblockExpression implements SymbolExpression {
	private final Structure struct;

	public MultiblockExpression(Structure struct)
	{
		this.struct = struct;
	}

	@Override
	public BuildPlan getBuildPlan()
	{
		return StagedBuildPlan.of(new ClearAreaForStructurePlan(this.struct), new SimpleStructureBuildPlan(this.struct));
	}

	public static MultiblockExpression of(Structure structure)
	{
		return new MultiblockExpression(structure);
	}
}
