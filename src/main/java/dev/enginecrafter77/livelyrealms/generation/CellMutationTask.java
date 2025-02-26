package dev.enginecrafter77.livelyrealms.generation;

import dev.enginecrafter77.livelyrealms.generation.expression.StructureBuildTask;
import dev.enginecrafter77.livelyrealms.generation.expression.SymbolExpression;
import dev.enginecrafter77.livelyrealms.generation.plan.StructureBuildContext;
import dev.enginecrafter77.livelyrealms.generation.plan.StructureBuildPlan;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.Nullable;

import java.util.NoSuchElementException;

public class CellMutationTask implements INBTSerializable<CompoundTag> {
	private final CellMutationContext context;
	private final CellPosition cellPosition;
	private String toSymbol;

	@Nullable
	private StructureBuildTask task;

	public CellMutationTask(CellMutationContext context)
	{
		this.cellPosition = new CellPosition();
		this.context = context;
		this.toSymbol = MinecraftStructureMap.EPSILON;
		this.task = null;
	}

	public StructureBuildPlan getPlan()
	{
		SymbolExpression expression = this.context.getGenerationContext().getGenerationProfile().expressionProvider().getExpression(this.toSymbol);
		if(expression == null)
			throw new NoSuchElementException();
		return expression.getBuildPlan();
	}

	public StructureBuildTask getBuildTask()
	{
		if(this.task == null)
		{
			BlockPos anchor = this.context.getGenerationContext().getBlockPositionInsideCell(this.cellPosition, BlockPos.ZERO);
			StructureBuildContext buildContext = new StructureBuildContext(this.context.getGenerationContext().level, anchor);
			StructureBuildPlan plan = this.getPlan();
			this.task = new StructureBuildTask(buildContext, plan);
		}
		return this.task;
	}

	public boolean hasNextStep()
	{
		return this.getBuildTask().hasNextStep();
	}

	public void step()
	{
		this.getBuildTask().step();
		if(!this.getBuildTask().hasNextStep())
			this.context.getSymbolMap().setSymbolAt(this.cellPosition, this.toSymbol);
	}

	@Override
	public CompoundTag serializeNBT(HolderLookup.Provider provider)
	{
		CompoundTag tag = new CompoundTag();
		tag.putIntArray("cell", new int[] {this.cellPosition.x, this.cellPosition.y, this.cellPosition.z});
		tag.putString("symbol", this.toSymbol);
		this.getBuildTask().saveState(tag);
		return tag;
	}

	@Override
	public void deserializeNBT(HolderLookup.Provider provider, CompoundTag compoundTag)
	{
		int[] cellPos = compoundTag.getIntArray("cell");
		this.toSymbol = compoundTag.getString("symbol");
		this.cellPosition.set(cellPos);
		this.getBuildTask().restoreState(compoundTag);
	}

	public static CellMutationTask create(CellMutationContext context, ReadableCellPosition position, String toSymbol)
	{
		CellMutationTask task = new CellMutationTask(context);
		task.cellPosition.set(position);
		task.toSymbol = toSymbol;
		return task;
	}
}
