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
		Structure filtered = this.getFilteredStructure();
		return StagedBuildPlan.of(new ClearAreaForStructurePlan(filtered), new SimpleStructureBuildPlan(filtered));
	}

	public Structure getFilteredStructure()
	{
		return FilteredStructure.filter(this.struct, this::filterStructure);
	}

	private boolean filterStructure(Structure structure, Vector3ic position)
	{
		return this.isExplicitBlock(structure.getBlockAt(position));
	}

	private boolean isExplicitBlock(BlockState state)
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
