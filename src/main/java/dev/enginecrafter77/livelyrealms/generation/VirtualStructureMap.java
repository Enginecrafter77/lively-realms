package dev.enginecrafter77.livelyrealms.generation;

import com.google.common.collect.ImmutableList;
import dev.enginecrafter77.livelyrealms.generation.lattice.MutableSparseSymbolLattice;
import dev.enginecrafter77.livelyrealms.generation.lattice.MutableSymbolLattice;
import dev.enginecrafter77.livelyrealms.generation.lattice.SymbolLattice;
import dev.enginecrafter77.livelyrealms.structure.LitematicaBitVector;
import dev.enginecrafter77.livelyrealms.structure.NaturalVoxelIndexer;
import dev.enginecrafter77.livelyrealms.structure.VoxelIndexer;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.*;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;
import org.joml.Vector3i;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.Vector;
import java.util.stream.Collectors;

public class VirtualStructureMap implements MutableSymbolLattice, INBTSerializable<CompoundTag> {
	private final MutableSparseSymbolLattice lattice;

	@Nonnull
	private final String epsilon;

	public VirtualStructureMap(@Nonnull String epsilon)
	{
		this.lattice = new MutableSparseSymbolLattice();
		this.epsilon = epsilon;
	}

	public Set<String> symbolSet()
	{
		Set<String> symbols = this.lattice.getPositions().stream().map(this::getSymbolAt).collect(Collectors.toSet());
		symbols.add(this.epsilon);
		return symbols;
	}

	public void getSize(Vector3i originOffset, Vector3i size)
	{
		Vector3i min = new Vector3i(0, 0, 0);
		Vector3i max = new Vector3i(0, 0, 0);
		for(ReadableCellPosition position : this.lattice.getPositions())
		{
			min.min(position);
			max.max(position);
		}
		originOffset.set(min);

		size.set(max);
		size.add(1, 1, 1);
		size.sub(originOffset);
	}

	@Override
	public void setSymbolAt(ReadableCellPosition position, @Nullable String symbol)
	{
		if(symbol == null)
			return;
		if(Objects.equals(symbol, this.epsilon))
			symbol = null;
		this.lattice.setSymbolAt(position, symbol);
	}

	@Override
	public String getSymbolAt(ReadableCellPosition position)
	{
		String symbol = this.lattice.getSymbolAt(position);
		if(symbol == null)
			return this.epsilon;
		return symbol;
	}

	@Override
	public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider)
	{
		Vector3i offset = new Vector3i();
		Vector3i size = new Vector3i();
		this.getSize(offset, size);

		List<String> symbols = ImmutableList.copyOf(this.symbolSet());
		VoxelIndexer indexer = new NaturalVoxelIndexer(size);
		LitematicaBitVector bitVector = LitematicaBitVector.forEntries(symbols.size(), indexer.volume());
		CellPosition position = new CellPosition();
		for(int index = 0; index < indexer.volume(); ++index)
		{
			indexer.fromIndex(index, position);
			position.add(offset);
			String symbol = this.getSymbolAt(position);
			int palettePosition = symbols.indexOf(symbol);
			if(palettePosition == -1)
				throw new UnsupportedOperationException(String.format("Symbol %s missing from palette", symbol));
			bitVector.set(index, palettePosition);
		}

		ListTag paletteTag = new ListTag();
		symbols.stream().map(StringTag::valueOf).forEach(paletteTag::add);

		CompoundTag tag = new CompoundTag();
		tag.putIntArray("offset", new int[]{offset.x, offset.y, offset.z});
		tag.putIntArray("size", new int[]{size.x, size.y, size.z});
		tag.put("palette", paletteTag);
		tag.put("cells", bitVector.serializeNBT(provider));
		return tag;
	}

	@Override
	public void deserializeNBT(HolderLookup.Provider provider, CompoundTag compoundTag)
	{
		Vector3i offset = new Vector3i();
		Vector3i size = new Vector3i();

		offset.set(compoundTag.getIntArray("offset"));
		size.set(compoundTag.getIntArray("size"));
		List<String> palette = compoundTag.getList("palette", Tag.TAG_STRING).stream().map(Tag::getAsString).toList();

		VoxelIndexer indexer = new NaturalVoxelIndexer(size);
		LitematicaBitVector bv = LitematicaBitVector.forEntries(palette.size(), indexer.volume());
		bv.deserializeNBT(provider, (LongArrayTag)compoundTag.get("cells"));

		CellPosition position = new CellPosition();
		for(int index = 0; index < indexer.volume(); ++index)
		{
			indexer.fromIndex(index, position);
			position.add(offset);
			int symbolIndex = bv.get(index);
			String symbol = palette.get(symbolIndex);
			this.setSymbolAt(position, symbol);
		}
	}
}
