package dev.enginecrafter77.livelyrealms.generation;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

public class GenerationGridWorldData extends SavedData {
	private final Map<UUID, MinecraftStructureMap> maps;
	private final Level level;

	public GenerationGridWorldData(Level level)
	{
		this.maps = new TreeMap<UUID, MinecraftStructureMap>();
		this.level = level;
	}

	public MinecraftStructureMap createMap(BlockPos anchor, ResourceLocation profile)
	{
		MinecraftStructureMap map = MinecraftStructureMap.create(this.level, anchor, profile);
		this.maps.put(map.getId(), map);
		return map;
	}

	@Nullable
	public MinecraftStructureMap get(UUID key)
	{
		return this.maps.get(key);
	}

	public Collection<MinecraftStructureMap> allMaps()
	{
		return this.maps.values();
	}

	public static GenerationGridWorldData load(Level level, CompoundTag compoundTag, HolderLookup.Provider provider)
	{
		GenerationGridWorldData data = new GenerationGridWorldData(level);
		ListTag mapList = compoundTag.getList("maps", Tag.TAG_COMPOUND);
		for(int index = 0; index < mapList.size(); ++index)
		{
			CompoundTag mapTag = mapList.getCompound(index);
			MinecraftStructureMap map = new MinecraftStructureMap(level);
			map.deserializeNBT(provider, mapTag);
			data.maps.put(map.getId(), map);
		}
		return data;
	}

	@Override
	public CompoundTag save(CompoundTag compoundTag, HolderLookup.Provider provider)
	{
		ListTag mapList = new ListTag();
		for(MinecraftStructureMap map : this.maps.values())
		{
			CompoundTag mapTag = map.serializeNBT(provider);
			mapList.add(mapTag);
		}
		compoundTag.put("maps", mapList);
		return compoundTag;
	}

	@Nonnull
	public static GenerationGridWorldData get(ServerLevel level)
	{
		Factory<GenerationGridWorldData> factory = new Factory<GenerationGridWorldData>(() -> new GenerationGridWorldData(level), (CompoundTag compoundTag, HolderLookup.Provider provider) -> GenerationGridWorldData.load(level, compoundTag, provider));
		return level.getDataStorage().computeIfAbsent(factory, "generation_grid");
	}
}
