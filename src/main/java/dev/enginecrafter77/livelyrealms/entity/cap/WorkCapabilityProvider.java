package dev.enginecrafter77.livelyrealms.entity.cap;

import dev.enginecrafter77.livelyrealms.LivelyRealmsMod;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.Nullable;

public class WorkCapabilityProvider implements ICapabilityProvider<Entity, Void, WorkHandler> {
	@Nullable
	@Override
	public WorkHandler getCapability(Entity object, @Nullable Void context)
	{
		if(object.level().isClientSide())
			return null;
		WorkStepLocatorHolder locatorHolder = object.getData(LivelyRealmsMod.AT_WORK_CONTAINER);
		return new WorkStepHolder(locatorHolder, (ServerLevel)object.level());
	}
}
