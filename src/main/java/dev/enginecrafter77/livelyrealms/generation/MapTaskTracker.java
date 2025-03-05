package dev.enginecrafter77.livelyrealms.generation;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

import java.util.*;

public class MapTaskTracker implements INBTSerializable<ListTag> {
	private final Map<ImmutableCellPosition, CellMutationTask> activeTasks;
	private final CellMutationContext context;

	public MapTaskTracker(CellMutationContext context)
	{
		this.activeTasks = new HashMap<ImmutableCellPosition, CellMutationTask>();
		this.context = context;
	}

	private void registerTask(CellMutationTask task)
	{
		ImmutableCellPosition key = ImmutableCellPosition.copyOf(task.getCellPosition());
		if(this.activeTasks.containsKey(key))
			throw new IllegalStateException(String.format("Cell at %s is already being mutated!", key));
		this.activeTasks.put(key, task);
	}

	@Nullable
	public CellMutationTask getTask(ReadableCellPosition position)
	{
		return this.activeTasks.get(ImmutableCellPosition.copyOf(position));
	}

	public Collection<CellMutationTask> allActiveTasks()
	{
		return this.activeTasks.values();
	}

	public SymbolAcceptor mutationAcceptor()
	{
		return new SymbolAcceptor() {
			@Override
			public void acceptSymbol(ReadableCellPosition cell, String symbol)
			{
				CellMutationTask task = CellMutationTask.create(MapTaskTracker.this.context, cell, symbol);
				MapTaskTracker.this.registerTask(task);
			}
		};
	}

	@UnknownNullability
	@Override
	public ListTag serializeNBT(HolderLookup.Provider provider)
	{
		ListTag tag = new ListTag();
		for(CellMutationTask task : this.allActiveTasks())
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
			CellMutationTask task = new CellMutationTask(this.context);
			task.deserializeNBT(provider, tag);
			this.registerTask(task);
		}
	}
}
