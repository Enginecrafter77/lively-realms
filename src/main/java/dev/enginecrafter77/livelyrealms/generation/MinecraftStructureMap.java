package dev.enginecrafter77.livelyrealms.generation;

import dev.enginecrafter77.livelyrealms.LivelyRealmsMod;
import dev.enginecrafter77.livelyrealms.generation.lattice.SymbolLattice;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

import javax.annotation.Nonnull;
import java.util.UUID;

public class MinecraftStructureMap implements GrammarContext, GenerationProfileHolder, CellMutationContext, INBTSerializable<CompoundTag> {
	public static final String EPSILON = "epsilon";

	private final Level level;

	private final VirtualStructureMap symbolMap;

	private final MapTaskTracker taskTracker;

	@Nonnull
	private UUID id;

	@Nonnull
	private ResourceLocation profileName;

	@Nonnull
	private BlockPos anchor;

	@Nullable
	private StructureGenerationContext structureGenerationContext;

	public MinecraftStructureMap(Level level)
	{
		this.taskTracker = new MapTaskTracker(this);
		this.id = UUID.randomUUID();
		this.level = level;
		this.anchor = BlockPos.ZERO;
		this.profileName = ResourceLocation.fromNamespaceAndPath(LivelyRealmsMod.MODID, "nonexistent_generation_profile");
		this.symbolMap = new VirtualStructureMap(EPSILON);
		this.structureGenerationContext = null;
	}

	public UUID getId()
	{
		return this.id;
	}

	public ResourceLocation getProfileName()
	{
		return this.profileName;
	}

	public BlockPos getAnchor()
	{
		return this.anchor;
	}

	public MapTaskTracker getTaskTracker()
	{
		return this.taskTracker;
	}

	@Override
	public VirtualStructureMap getSymbolMap()
	{
		return this.symbolMap;
	}

	@Nonnull
	@Override
	public StructureGenerationContext getGenerationContext()
	{
		if(this.structureGenerationContext == null)
			this.structureGenerationContext = new StructureGenerationContext(this.level, this.anchor, this.getGenerationProfile());
		return this.structureGenerationContext;
	}

	@Nonnull
	@Override
	public GenerationProfile getGenerationProfile()
	{
		return LivelyRealmsMod.GENERATION_PROFILE_REGISTRY.getOptional(this.profileName).orElseThrow();
	}

	@UnknownNullability
	@Override
	public CompoundTag serializeNBT(HolderLookup.Provider provider)
	{
		CompoundTag tag = new CompoundTag();
		tag.putUUID("id", this.id);
		tag.putString("profile", this.profileName.toString());
		tag.put("map", this.symbolMap.serializeNBT(provider));
		tag.putIntArray("anchor", new int[] {this.anchor.getX(), this.anchor.getY(), this.anchor.getZ()});
		tag.put("tasks", this.taskTracker.serializeNBT(provider));
		return tag;
	}

	@Override
	public void deserializeNBT(HolderLookup.Provider provider, CompoundTag compoundTag)
	{
		int[] anchorArray = compoundTag.getIntArray("anchor");
		this.id = compoundTag.getUUID("id");
		this.profileName = ResourceLocation.parse(compoundTag.getString("profile"));
		this.symbolMap.deserializeNBT(provider, compoundTag.getCompound("map"));
		this.anchor = new BlockPos(anchorArray[0], anchorArray[1], anchorArray[2]);
		this.taskTracker.deserializeNBT(provider, compoundTag.getList("tasks", Tag.TAG_COMPOUND));
		this.structureGenerationContext = null;
	}

	@Override
	public SymbolLattice getEnvironment()
	{
		return this.symbolMap;
	}

	public static MinecraftStructureMap create(Level level, BlockPos anchor, ResourceLocation profile)
	{
		MinecraftStructureMap map = new MinecraftStructureMap(level);
		map.anchor = anchor;
		map.profileName = profile;
		return map;
	}
}
