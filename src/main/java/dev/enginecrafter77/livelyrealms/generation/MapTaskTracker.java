package dev.enginecrafter77.livelyrealms.generation;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

import java.util.*;
import java.util.stream.Stream;

public class MapTaskTracker implements INBTSerializable<ListTag> {
	private final Map<ImmutableCellPosition, CellMutationTask> activeTasks;
	private final CellMutationContext context;
	private final DirtyFlagHandler dirtyFlagHandler;

	public MapTaskTracker(CellMutationContext context, DirtyFlagHandler dirtyFlagHandler)
	{
		this.activeTasks = new HashMap<ImmutableCellPosition, CellMutationTask>();
		this.dirtyFlagHandler = dirtyFlagHandler;
		this.context = context;
	}

	private void registerTask(CellMutationTask task)
	{
		ImmutableCellPosition key = ImmutableCellPosition.copyOf(task.getCellPosition());
		if(this.activeTasks.containsKey(key))
			throw new IllegalStateException(String.format("Cell at %s is already being mutated!", key));
		this.activeTasks.put(key, task);
		this.dirtyFlagHandler.markDirty();
	}

	@Nullable
	public CellMutationTask getTask(ReadableCellPosition position)
	{
		return this.activeTasks.get(ImmutableCellPosition.copyOf(position));
	}

	public void removeTask(ReadableCellPosition position)
	{
		this.activeTasks.remove(ImmutableCellPosition.copyOf(position));
		this.dirtyFlagHandler.markDirty();
	}

	public Collection<CellMutationTask> allTasks()
	{
		return this.activeTasks.values();
	}

	public Stream<CellMutationTask> allActiveTasks()
	{
		return this.allTasks().stream().filter(CellMutationTask::isActive);
	}

	public SymbolAcceptor mutationAcceptor()
	{
		return new SymbolAcceptor() {
			@Override
			public void acceptSymbol(ReadableCellPosition cell, String symbol)
			{
				CellMutationTask task = CellMutationTask.create(MapTaskTracker.this.context, cell, symbol, MapTaskTracker.this.dirtyFlagHandler);
				MapTaskTracker.this.registerTask(task);
			}
		};
	}

	@UnknownNullability
	@Override
	public ListTag serializeNBT(HolderLookup.Provider provider)
	{
		ListTag tag = new ListTag();
		for(CellMutationTask task : this.allTasks())
			tag.add(task.serializeNBT(provider));
		return tag;
	}

	@Override
	public void deserializeNBT(HolderLookup.Provider provider, @Nullable ListTag tags)
	{
		if(tags == null)
			return;
		this.activeTasks.clear();
		for(int index = 0; index < tags.size(); ++index)
		{
			CompoundTag tag = tags.getCompound(index);
			CellMutationTask task = new CellMutationTask(this.context, this.dirtyFlagHandler);
			task.deserializeNBT(provider, tag);
			this.registerTask(task);
		}
	}
}
