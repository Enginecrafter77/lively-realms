package dev.enginecrafter77.livelyrealms.entity;

import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.EnumMap;
import java.util.Map;

public class EntityDwarf extends PathfinderMob {
	public EntityDwarf(EntityType<EntityDwarf> type, Level level)
	{
		super(type, level);
		this.setLeftHanded(false);
	}

	@Override
	protected void registerGoals()
	{
		super.registerGoals();
		this.goalSelector.addGoal(0, new RandomStrollGoal(this, 0.5D));
		this.goalSelector.addGoal(1, new RandomLookAroundGoal(this));
	}

	public static AttributeSupplier attributes()
	{
		return Mob.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 20)
				.add(Attributes.MOVEMENT_SPEED, 0.25D)
				.build();
	}
}
