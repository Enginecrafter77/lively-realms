package dev.enginecrafter77.livelyrealms.entity.client;

import dev.enginecrafter77.livelyrealms.LivelyRealmsMod;
import dev.enginecrafter77.livelyrealms.entity.EntityDwarf;
import dev.enginecrafter77.livelyrealms.entity.model.ModelDwarf;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class DwarfRenderer extends MobRenderer<EntityDwarf, ModelDwarf> {
	public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(LivelyRealmsMod.MODID, "textures/entity/dwarf.png");

	public DwarfRenderer(EntityRendererProvider.Context context)
	{
		super(context, ModelDwarf.bake(context), 0.25F);
	}

	@Override
	public ResourceLocation getTextureLocation(EntityDwarf entityDwarf)
	{
		return TEXTURE;
	}
}
