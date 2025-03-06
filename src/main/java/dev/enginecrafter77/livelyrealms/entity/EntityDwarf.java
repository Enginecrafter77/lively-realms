package dev.enginecrafter77.livelyrealms.entity;

import com.google.common.collect.ImmutableList;
import dev.enginecrafter77.livelyrealms.LivelyRealmsMod;
import dev.enginecrafter77.livelyrealms.entity.cap.AssignedWorkStep;
import dev.enginecrafter77.livelyrealms.entity.cap.MapAllegianceHolder;
import dev.enginecrafter77.livelyrealms.entity.cap.WorkHandler;
import dev.enginecrafter77.livelyrealms.entity.goal.WorkOnMapGoal;
import dev.enginecrafter77.livelyrealms.generation.CellMutationTask;
import dev.enginecrafter77.livelyrealms.generation.GenerationGridWorldData;
import dev.enginecrafter77.livelyrealms.generation.MinecraftStructureMap;
import dev.enginecrafter77.livelyrealms.generation.plan.BuildStep;
import dev.enginecrafter77.livelyrealms.generation.plan.PlanInterpreter;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import java.util.Objects;
import java.util.UUID;

public class EntityDwarf extends PathfinderMob {
	public EntityDwarf(EntityType<EntityDwarf> type, Level level)
	{
		super(type, level);
		this.setLeftHanded(false);
	}

	@Override
	protected InteractionResult mobInteract(Player player, InteractionHand hand)
	{
		if(!this.level().isClientSide())
		{
			MapAllegianceHolder allegianceHolder = Objects.requireNonNull(this.getCapability(LivelyRealmsMod.CAPABILITY_ALLEGIANCE));
			ItemStack handItem = player.getItemInHand(hand);
			if(handItem.is(LivelyRealmsMod.ITEM_GRAMMAR_WAND))
			{
				UUID map = handItem.get(LivelyRealmsMod.DC_ASSOCIATED_GENERATION_MAP);
				allegianceHolder.setAllegiance(map);
				return InteractionResult.SUCCESS_NO_ITEM_USED;
			}
			if(handItem.is(Items.STICK))
			{
				WorkHandler taskHolder = Objects.requireNonNull(this.getCapability(LivelyRealmsMod.CAPABILITY_WORKER));
				player.sendSystemMessage(Component.literal(String.format("A: %s / T: %s", allegianceHolder.getAllegiance(), taskHolder.getAssignedStep())));
				return InteractionResult.SUCCESS_NO_ITEM_USED;
			}
		}
		return super.mobInteract(player, hand);
	}

	@Override
	public Iterable<ItemStack> getHandSlots()
	{
		return ImmutableList.of(this.getItemBySlot(EquipmentSlot.MAINHAND), this.getItemBySlot(EquipmentSlot.OFFHAND));
	}

	@Override
	public ItemStack getItemBySlot(EquipmentSlot slot)
	{
		return switch(slot) {
			case MAINHAND -> new ItemStack(Items.GOLDEN_PICKAXE);
			case OFFHAND -> new ItemStack(Items.TORCH);
			default -> super.getItemBySlot(slot);
		};
	}

	private void findWorkTask()
	{
		if(this.level().isClientSide())
			return;
		MapAllegianceHolder allegianceHolder = Objects.requireNonNull(this.getCapability(LivelyRealmsMod.CAPABILITY_ALLEGIANCE));
		UUID allegiance = allegianceHolder.getAllegiance();
		if(allegiance == null)
			return;
		WorkHandler taskHolder = Objects.requireNonNull(this.getCapability(LivelyRealmsMod.CAPABILITY_WORKER));
		if(taskHolder.getAssignedStep() != null)
			return;
		GenerationGridWorldData grid = GenerationGridWorldData.get((ServerLevel)this.level());
		MinecraftStructureMap map = grid.get(allegiance);
		if(map == null)
			return;
		CellMutationTask task = map.getTaskTracker().allActiveTasks().findFirst().orElse(null);
		if(task == null)
			return;
		PlanInterpreter interpreter = task.getPlanInterpreter();
		if(!interpreter.hasNextStep())
			return;
		BuildStep step = interpreter.nextStep();
		AssignedWorkStep state = AssignedWorkStep.make(map, task, step);
		taskHolder.setAssignedStep(state);
	}

	@Override
	public void tick()
	{
		super.tick();
		this.findWorkTask();
	}

	@Override
	protected void registerGoals()
	{
		super.registerGoals();
		this.goalSelector.addGoal(0, new WorkOnMapGoal(this));
		//this.goalSelector.addGoal(0, new RandomStrollGoal(this, 0.5D));
		this.goalSelector.addGoal(2, new RandomLookAroundGoal(this));
	}

	public static AttributeSupplier attributes()
	{
		return Mob.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 20)
				.add(Attributes.MOVEMENT_SPEED, 0.5D)
				.add(Attributes.FOLLOW_RANGE, 128)
				.build();
	}
}
