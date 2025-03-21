package dev.enginecrafter77.livelyrealms.generation.expression;

import dev.enginecrafter77.livelyrealms.generation.plan.ClearAreaForStructurePlan;
import dev.enginecrafter77.livelyrealms.generation.plan.SimpleStructureBuildPlan;
import dev.enginecrafter77.livelyrealms.generation.plan.BuildPlan;
import dev.enginecrafter77.livelyrealms.generation.plan.StagedBuildPlan;
import dev.enginecrafter77.livelyrealms.structure.Structure;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class MultiblockExpression implements SymbolExpression {
	private final Structure struct;

	@Nullable
	private final ResourceLocation ignoredBlock;

	public MultiblockExpression(Structure struct, @Nullable ResourceLocation ignoredBlock)
	{
		this.struct = struct;
		this.ignoredBlock = ignoredBlock;
	}

	@Override
	public BuildPlan getBuildPlan()
	{
		return StagedBuildPlan.of(new ClearAreaForStructurePlan(this.struct, this::shouldBlockBeCleared), new SimpleStructureBuildPlan(this.struct));
	}

	private boolean shouldBlockBeCleared(BlockState state)
	{
		if(this.ignoredBlock == null)
			return true;
		return !state.getBlockHolder().getRegisteredName().equals(this.ignoredBlock.toString());
	}

	public static MultiblockExpression of(Structure structure, @Nullable ResourceLocation ignoredBlock)
	{
		return new MultiblockExpression(structure, ignoredBlock);
	}

	public static MultiblockExpression of(Structure structure)
	{
		return new MultiblockExpression(structure, null);
	}
}
