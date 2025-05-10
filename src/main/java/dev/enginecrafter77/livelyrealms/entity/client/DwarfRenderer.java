package dev.enginecrafter77.livelyrealms.entity.client;

import dev.enginecrafter77.livelyrealms.LivelyRealmsMod;
import dev.enginecrafter77.livelyrealms.entity.EntityDwarf;
import dev.enginecrafter77.livelyrealms.entity.model.ModelHumanoidDwarf;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DwarfRenderer extends MobRenderer<EntityDwarf, ModelHumanoidDwarf> {
	public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(LivelyRealmsMod.MODID, "textures/entity/dwarf.png");

	public DwarfRenderer(EntityRendererProvider.Context context)
	{
		super(context, ModelHumanoidDwarf.bake(context), 0.25F);
		this.addLayer(new ItemInHandLayer<EntityDwarf, ModelHumanoidDwarf>(this, context.getItemInHandRenderer()));
		this.addLayer(new HumanoidArmorLayer<EntityDwarf, ModelHumanoidDwarf, ModelHumanoidDwarf>(this, new ModelHumanoidDwarf(context.bakeLayer(ModelHumanoidDwarf.INNER_ARMOR)), new ModelHumanoidDwarf(context.bakeLayer(ModelHumanoidDwarf.OUTER_ARMOR)), context.getModelManager()));
	}

	@Override
	public ResourceLocation getTextureLocation(EntityDwarf entityDwarf)
	{
		return TEXTURE;
	}
}
