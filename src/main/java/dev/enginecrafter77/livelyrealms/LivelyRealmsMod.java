package dev.enginecrafter77.livelyrealms;

import com.mojang.serialization.Lifecycle;
import dev.enginecrafter77.livelyrealms.entity.client.DwarfRenderer;
import dev.enginecrafter77.livelyrealms.entity.model.ModelDwarf;
import dev.enginecrafter77.livelyrealms.entity.EntityDwarf;
import dev.enginecrafter77.livelyrealms.generation.GenerationProfile;
import dev.enginecrafter77.livelyrealms.items.ItemGrammarWand;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.client.ClientHooks;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.*;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.w3c.dom.Attr;

import java.util.UUID;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(LivelyRealmsMod.MODID)
public class LivelyRealmsMod {
    public static final String MODID = "livelyrealms";
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final Registry<GenerationProfile> GENERATION_PROFILE_REGISTRY = new MappedRegistry<>(ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(MODID, "generation_profiles")), Lifecycle.stable());

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister<GenerationProfile> GENERATION_PROFILES = DeferredRegister.create(GENERATION_PROFILE_REGISTRY, MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MODID);
    public static final DeferredRegister.DataComponents DATA_COMPONENT_TYPES = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, MODID);
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<UUID>> DC_ASSOCIATED_GENERATION_MAP = DATA_COMPONENT_TYPES.registerComponentType("generation_map", builder -> builder.persistent(UUIDUtil.CODEC).networkSynchronized(UUIDUtil.STREAM_CODEC));

    public static final DeferredItem<ItemGrammarWand> ITEM_GRAMMAR_WAND = ITEMS.registerItem("grammar_wand", (props) -> new ItemGrammarWand(props.component(DC_ASSOCIATED_GENERATION_MAP.get(), UUID.randomUUID())));
    public static final DeferredHolder<GenerationProfile, GenerationProfile> SAMPLE_PROFILE = GENERATION_PROFILES.register("sample", GenerationProfile.using(ItemGrammarWand::configureGrammar));
	public static final DeferredHolder<EntityType<?>, EntityType<EntityDwarf>> ENTITY_TYPE_DWARF = ENTITY_TYPES.register("dwarf", () -> EntityType.Builder.of(EntityDwarf::new, MobCategory.CREATURE).sized(0.75F, 1.5F).build("dwarf"));

    public LivelyRealmsMod(IEventBus modEventBus, ModContainer modContainer)
    {
        modEventBus.register(this);
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        GENERATION_PROFILES.register(modEventBus);
        BLOCK_ENTITY_TYPES.register(modEventBus);
        DATA_COMPONENT_TYPES.register(modEventBus);
        ENTITY_TYPES.register(modEventBus);

        NeoForge.EVENT_BUS.register(StructureMapUpdater.class);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    @SubscribeEvent
    public void commonSetup(FMLCommonSetupEvent event)
    {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");

        if (Config.logDirtBlock)
            LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));

        LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber);

        Config.items.forEach((item) -> LOGGER.info("ITEM >> {}", item.toString()));
    }

    @SubscribeEvent
    public void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        if(event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES)
        {
            event.accept(ITEM_GRAMMAR_WAND);
        }
    }

    @SubscribeEvent
    public void registerRegistries(NewRegistryEvent event)
    {
        event.register(GENERATION_PROFILE_REGISTRY);
    }

    @SubscribeEvent
    public void registerEntityAttributes(EntityAttributeCreationEvent event)
    {
        event.put(ENTITY_TYPE_DWARF.get(), EntityDwarf.attributes());
    }

    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.GAME)
    public static class GameEvents
    {
        @SubscribeEvent
        public static void onServerStarting(ServerStartingEvent event)
        {
            // Do something when the server starts
            LOGGER.info("HELLO from server starting");
        }
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event)
        {
            event.registerLayerDefinition(ModelDwarf.LAYER_LOCATION, ModelDwarf::createBodyLayer);
        }

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
            EntityRenderers.register(LivelyRealmsMod.ENTITY_TYPE_DWARF.get(), DwarfRenderer::new);
        }
    }
}
