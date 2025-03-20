package dev.enginecrafter77.livelyrealms.entity.model;

import dev.enginecrafter77.livelyrealms.LivelyRealmsMod;
import dev.enginecrafter77.livelyrealms.entity.EntityDwarf;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.LayerDefinitions;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class ModelHumanoidDwarf extends HumanoidModel<EntityDwarf> {
	public static final ModelLayerLocation MODEL = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(LivelyRealmsMod.MODID, "dwarf"), "humanoid");
	public static final ModelLayerLocation INNER_ARMOR = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(LivelyRealmsMod.MODID, "dwarf"), "humanoid_inner_armor");
	public static final ModelLayerLocation OUTER_ARMOR = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(LivelyRealmsMod.MODID, "dwarf"), "humanoid_outer_armor");

	public ModelHumanoidDwarf(ModelPart root)
	{
		super(root);
	}

	public static LayerDefinition createBodyLayer()
	{
		MeshDefinition meshdefinition = HumanoidModel.createMesh(CubeDeformation.NONE, 0F);
		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	public static LayerDefinition createInnerArmorLayer()
	{
		MeshDefinition meshdefinition = HumanoidModel.createMesh(LayerDefinitions.INNER_ARMOR_DEFORMATION, 0F);
		return LayerDefinition.create(meshdefinition, 64, 32);
	}

	public static LayerDefinition createOuterArmorLayer()
	{
		MeshDefinition meshdefinition = HumanoidModel.createMesh(LayerDefinitions.OUTER_ARMOR_DEFORMATION, 0F);
		return LayerDefinition.create(meshdefinition, 64, 32);
	}

	public static ModelHumanoidDwarf bake(EntityRendererProvider.Context context)
	{
		return new ModelHumanoidDwarf(context.bakeLayer(MODEL));
	}
}
