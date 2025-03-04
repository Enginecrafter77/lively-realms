package dev.enginecrafter77.livelyrealms.generation;

import dev.enginecrafter77.livelyrealms.generation.expression.SymbolExpression;
import dev.enginecrafter77.livelyrealms.generation.plan.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.Nullable;

import java.util.NoSuchElementException;
import java.util.Objects;

public class CellMutationTask implements INBTSerializable<CompoundTag>, BuildContextOwner {
	private final CellMutationContext gridContext;
	private final CellPosition cellPosition;
	private String toSymbol;

	@Nullable
	private BuildPlan plan;

	@Nullable
	private BuildContext buildContext;

	@Nullable
	private PlanInterpreter planInterpreter;

	public CellMutationTask(CellMutationContext gridContext)
	{
		this.cellPosition = new CellPosition();
		this.gridContext = gridContext;
		this.toSymbol = MinecraftStructureMap.EPSILON;
		this.planInterpreter = null;
		this.buildContext = null;
		this.plan = null;
	}

	public BuildPlan getPlan()
	{
		if(this.plan == null)
		{
			SymbolExpression expression = this.gridContext.getGenerationContext().getGenerationProfile().expressionProvider().getExpression(this.toSymbol);
			if(expression == null)
				throw new NoSuchElementException();
			this.plan = BuildPlanBuilder.begin().stage(expression.getBuildPlan()).beginStage().step(new CommitSymbolBuildStep()).endStage().build();
		}
		return this.plan;
	}

	@Override
	public BuildContext getContext()
	{
		if(this.buildContext == null)
		{
			BlockPos anchor = this.gridContext.getGenerationContext().getBlockPositionInsideCell(this.cellPosition, BlockPos.ZERO);
			this.buildContext = new BuildContext(this.gridContext.getGenerationContext().level, anchor);
		}
		return this.buildContext;
	}

	public PlanInterpreter getPlanInterpreter()
	{
		if(this.planInterpreter == null)
			this.planInterpreter = new ContextAwarePlanInterpreter(this.getPlan(), this);
		return this.planInterpreter;
	}

	private void invalidateComponents()
	{
		this.plan = null;
		this.buildContext = null;
		this.planInterpreter = null;
	}

	@Override
	public CompoundTag serializeNBT(HolderLookup.Provider provider)
	{
		CompoundTag tag = new CompoundTag();
		tag.putIntArray("cell", new int[] {this.cellPosition.x, this.cellPosition.y, this.cellPosition.z});
		tag.putString("symbol", this.toSymbol);
		this.getPlanInterpreter().saveState(tag);
		return tag;
	}

	@Override
	public void deserializeNBT(HolderLookup.Provider provider, CompoundTag compoundTag)
	{
		this.invalidateComponents();
		int[] cellPos = compoundTag.getIntArray("cell");
		this.toSymbol = compoundTag.getString("symbol");
		this.cellPosition.set(cellPos);
		this.getPlanInterpreter().restoreState(compoundTag);
	}

	public static CellMutationTask create(CellMutationContext context, ReadableCellPosition position, String toSymbol)
	{
		CellMutationTask task = new CellMutationTask(context);
		task.cellPosition.set(position);
		task.toSymbol = toSymbol;
		return task;
	}

	private class CommitSymbolBuildStep implements BuildStep
	{
		@Override
		public void perform(BuildContext context)
		{
			CellMutationTask.this.gridContext.getSymbolMap().setSymbolAt(CellMutationTask.this.cellPosition, CellMutationTask.this.toSymbol);
		}

		@Override
		public boolean isComplete(BuildContext context)
		{
			String existing = CellMutationTask.this.gridContext.getSymbolMap().getSymbolAt(CellMutationTask.this.cellPosition);
			return Objects.equals(existing, CellMutationTask.this.toSymbol);
		}
	}
}
