package dev.enginecrafter77.livelyrealms.generation;

import dev.enginecrafter77.livelyrealms.LivelyRealmsMod;
import dev.enginecrafter77.livelyrealms.RandomRuleSelector;
import dev.enginecrafter77.livelyrealms.RuleSelector;
import dev.enginecrafter77.livelyrealms.generation.expression.CellLocator;
import dev.enginecrafter77.livelyrealms.generation.expression.ContinuousCellLocator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class MinecraftStructureMap implements GenerationProfileHolder, GeneratorContext, INBTSerializable<CompoundTag>, DirtyFlagHandler {
	public static final String EPSILON = "epsilon";

	private final DirtyFlagHandler parentDirtyFlagHandler;

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
	private CellLocator cellLocator;

	public MinecraftStructureMap(Level level, DirtyFlagHandler parentDirtyFlagHandler)
	{
		this.parentDirtyFlagHandler = parentDirtyFlagHandler;
		this.taskTracker = new MapTaskTracker(this, this);
		this.id = UUID.randomUUID();
		this.level = level;
		this.anchor = BlockPos.ZERO;
		this.profileName = ResourceLocation.fromNamespaceAndPath(LivelyRealmsMod.MODID, "nonexistent_generation_profile");
		this.symbolMap = new VirtualStructureMap(EPSILON);
		this.cellLocator = null;
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
	public void markDirty()
	{
		this.parentDirtyFlagHandler.markDirty();
	}

	@Override
	public VirtualStructureMap getSymbolMap()
	{
		return this.symbolMap;
	}

	@Override
	public CellLocator getCellLocator()
	{
		if(this.cellLocator == null)
			this.cellLocator = new ContinuousCellLocator(this.anchor, this.getGenerationProfile().expressionProvider().getCellSize());
		return this.cellLocator;
	}

	@Override
	public Level getLevel()
	{
		return this.level;
	}

	@Nonnull
	@Override
	public GenerationProfile getGenerationProfile()
	{
		return LivelyRealmsMod.GENERATION_PROFILE_REGISTRY.getOptional(this.profileName).orElseThrow();
	}

	public void seed(ReadableCellPosition cell)
	{
		this.getTaskTracker().mutationAcceptor().acceptSymbol(cell, this.getGenerationProfile().grammar().startingSymbol());
	}

	public ExpandResult expand(ReadableCellPosition cell)
	{
		if(this.getTaskTracker().getTask(cell) != null)
			return ExpandResult.TASK_PENDING;

		Grammar grammar = this.getGenerationProfile().grammar();
		Set<Grammar.GrammarRuleEntry> rules = grammar.findApplicableRules(this, cell).collect(Collectors.toUnmodifiableSet());
		if(rules.isEmpty())
			return ExpandResult.NO_RULE;
		GrammarRule selectedRule = rules.stream().map(Grammar.GrammarRuleEntry::rule).findAny().orElseThrow();
		if(rules.size() >= 2)
		{
			RuleSelector selector = grammar.ruleSelector().or(new RandomRuleSelector());
			selectedRule = selector.select(this, cell, rules);
			if(selectedRule == null)
				return ExpandResult.SELECTOR_FAILED;
		}

		SymbolAcceptor acceptor = this.getTaskTracker().mutationAcceptor();
		selectedRule.apply(acceptor, this, cell);
		this.markDirty();
		return ExpandResult.SUCCESS;
	}

	protected void invalidateCachedObjects()
	{
		this.cellLocator = null;
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
		this.invalidateCachedObjects();
	}

	public static MinecraftStructureMap create(Level level, DirtyFlagHandler parentDirtyFlagHandler, BlockPos anchor, ResourceLocation profile)
	{
		MinecraftStructureMap map = new MinecraftStructureMap(level, parentDirtyFlagHandler);
		map.anchor = anchor;
		map.profileName = profile;
		return map;
	}

	public static enum ExpandResult {SUCCESS, TASK_PENDING, NO_RULE, SELECTOR_FAILED}
}
