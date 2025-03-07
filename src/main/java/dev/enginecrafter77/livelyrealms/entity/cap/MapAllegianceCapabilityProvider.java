package dev.enginecrafter77.livelyrealms.entity.cap;

import dev.enginecrafter77.livelyrealms.LivelyRealmsMod;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.Nullable;

public class MapAllegianceCapabilityProvider implements ICapabilityProvider<Entity, Void, MapAllegianceHolder> {
	@Nullable
	@Override
	public MapAllegianceHolder getCapability(Entity object, @Nullable Void context)
	{
		return object.getData(LivelyRealmsMod.AT_MAP_ALLEGIANCE);
	}
}
