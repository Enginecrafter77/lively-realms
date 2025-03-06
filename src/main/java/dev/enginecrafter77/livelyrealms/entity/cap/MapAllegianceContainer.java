package dev.enginecrafter77.livelyrealms.entity.cap;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

import java.util.Objects;
import java.util.UUID;

public class MapAllegianceContainer implements INBTSerializable<CompoundTag>, MapAllegianceHolder {
	@Nullable
	private UUID mapId;

	public MapAllegianceContainer()
	{
		this.mapId = null;
	}

	@Nullable
	@Override
	public UUID getAllegiance()
	{
		return this.mapId;
	}

	@Override
	public void setAllegiance(@Nullable UUID allegiance)
	{
		this.mapId = allegiance;
	}

	@UnknownNullability
	@Override
	public CompoundTag serializeNBT(HolderLookup.Provider provider)
	{
		CompoundTag tag = new CompoundTag();
		if(this.mapId == null)
			return tag;
		tag.put("mapId", NbtUtils.createUUID(this.mapId));
		return tag;
	}

	@Override
	public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt)
	{
		if(!nbt.contains("mapId"))
		{
			this.mapId = null;
			return;
		}
		this.mapId = NbtUtils.loadUUID(Objects.requireNonNull(nbt.get("mapId")));
	}
}
