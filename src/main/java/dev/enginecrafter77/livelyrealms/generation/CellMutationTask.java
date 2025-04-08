package dev.enginecrafter77.livelyrealms.generation;

import dev.enginecrafter77.livelyrealms.generation.expression.SymbolExpression;
import dev.enginecrafter77.livelyrealms.generation.plan.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.Nullable;

import java.util.NoSuchElementException;

public class CellMutationTask implements INBTSerializable<CompoundTag>, BuildContextOwner {
	private final DirtyFlagHandler dirtyFlagHandler;
	private final GeneratorContext generatorContext;
	private final CellPosition cellPosition;
	private String toSymbol;

	@Nullable
	private BuildPlan plan;

	@Nullable
	private BuildContext buildContext;

	@Nullable
	private PlanInterpreter planInterpreter;

	public CellMutationTask(GeneratorContext generatorContext, DirtyFlagHandler dirtyFlagHandler)
	{
		this.dirtyFlagHandler = dirtyFlagHandler;
		this.cellPosition = new CellPosition();
		this.generatorContext = generatorContext;
		this.toSymbol = MinecraftStructureMap.EPSILON;
		this.planInterpreter = null;
		this.buildContext = null;
		this.plan = null;
	}

	public ReadableCellPosition getCellPosition()
	{
		return this.cellPosition;
	}

	public String getToSymbol()
	{
		return this.toSymbol;
	}

	public BuildPlan getPlan()
	{
		if(this.plan == null)
		{
			SymbolExpression expression = this.generatorContext.getGenerationProfile().expressionProvider().getExpression(this.toSymbol);
			if(expression == null)
				throw new NoSuchElementException();
			this.plan = expression.getBuildPlan();
		}
		return this.plan;
	}

	public GeneratorContext getGeneratorContext()
	{
		return this.generatorContext;
	}

	@Override
	public BuildContext getContext()
	{
		if(this.buildContext == null)
			this.buildContext = this.generatorContext.makeBuildContext(this.cellPosition);
		return this.buildContext;
	}

	protected PlanInterpreter createInterpreter()
	{
		return new ContextAwarePlanInterpreter(this.getPlan(), this);
	}

	public PlanInterpreter getPlanInterpreter()
	{
		if(this.planInterpreter == null)
			this.planInterpreter = new InterpretWrapper(this.createInterpreter());
		return this.planInterpreter;
	}

	private void invalidateComponents()
	{
		this.plan = null;
		this.buildContext = null;
		this.planInterpreter = null;
	}

	public void commit()
	{
		this.generatorContext.getSymbolMap().setSymbolAt(this.cellPosition, this.toSymbol);
		this.dirtyFlagHandler.markDirty();
	}

	public boolean isActive()
	{
		return this.getPlanInterpreter().hasNextStep();
	}

	public boolean isDone()
	{
		return !this.isActive();
	}

	public boolean validate()
	{
		if(!this.isDone())
			return false;
		ContextAwarePlanInterpreter newInterpreter = new ContextAwarePlanInterpreter(this.getPlan(), this);
		if(newInterpreter.hasNextStep())
		{
			this.planInterpreter = newInterpreter;
			this.dirtyFlagHandler.markDirty();
			return false;
		}
		return true;
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

	public static CellMutationTask create(GeneratorContext context, ReadableCellPosition position, String toSymbol, DirtyFlagHandler dirtyFlagHandler)
	{
		CellMutationTask task = new CellMutationTask(context, dirtyFlagHandler);
		task.cellPosition.set(position);
		task.toSymbol = toSymbol;
		return task;
	}

	private class InterpretWrapper implements PlanInterpreter
	{
		private final PlanInterpreter delegated;

		public InterpretWrapper(PlanInterpreter delegated)
		{
			this.delegated = delegated;
		}

		@Override
		public boolean hasNextStep()
		{
			return this.delegated.hasNextStep();
		}

		@Override
		public BuildStep nextStep()
		{
			BuildStep next = this.delegated.nextStep();
			CellMutationTask.this.dirtyFlagHandler.markDirty();
			return next;
		}

		@Override
		public @Nullable BuildStep lastStep()
		{
			return this.delegated.lastStep();
		}

		@Override
		public void saveState(CompoundTag tag)
		{
			this.delegated.saveState(tag);
		}

		@Override
		public void restoreState(CompoundTag tag)
		{
			this.delegated.restoreState(tag);
		}
	}
}
