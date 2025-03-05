package dev.enginecrafter77.livelyrealms.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.enginecrafter77.livelyrealms.LivelyRealmsMod;
import dev.enginecrafter77.livelyrealms.entity.EntityDwarf;
import dev.enginecrafter77.livelyrealms.entity.client.HandItemPose;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class ModelDwarf extends HierarchicalModel<EntityDwarf> implements ArmedModel, HeadedModel {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(LivelyRealmsMod.MODID, "dwarf"), "main");

	private final HandItemPose handPose;
	private final ModelPart head;
	private final ModelPart body;
	private final ModelPart leftArm;
	private final ModelPart rightArm;
	private final ModelPart leftLeg;
	private final ModelPart rightLeg;
	private final ModelPart leftHand;
	private final ModelPart rightHand;

	public ModelDwarf(ModelPart root)
	{
		this.handPose = new HandItemPose();
		this.body = root.getChild("body");
		this.head = this.body.getChild("head");
		this.leftLeg = this.body.getChild("left_leg");
		this.rightLeg = this.body.getChild("right_leg");
		this.leftArm = this.body.getChild("left_arm");
		this.rightArm = this.body.getChild("right_arm");
		this.leftHand = this.leftArm.getChild("left_hand");
		this.rightHand = this.rightArm.getChild("right_hand");
	}

	public static LayerDefinition createBodyLayer()
	{
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 9.0F, 0.0F));
		PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, -6.0F, 0.0F));
		PartDefinition head_r1 = head.addOrReplaceChild("head_r1", CubeListBuilder.create().texOffs(4, 7).addBox(-3.5F, -7.0F, -3.5F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition torso = body.addOrReplaceChild("torso", CubeListBuilder.create().texOffs(1, 21).addBox(-5.0F, -6.0F, -3.5F, 10.0F, 6.0F, 7.0F, new CubeDeformation(0.0F))
				.texOffs(0, 34).addBox(-5.0F, 0.0F, -4.3F, 10.0F, 7.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition right_arm = body.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(50, 0).addBox(0.0F, -1.3F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, -4.5F, 0.0F));
		PartDefinition right_hand = right_arm.addOrReplaceChild("right_hand", CubeListBuilder.create(), PartPose.offset(1.5F, 9.0F, 0.0F));
		PartDefinition left_arm = body.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(36, 0).addBox(-3.0F, -1.3F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, -4.5F, 0.0F));
		PartDefinition left_hand = left_arm.addOrReplaceChild("left_hand", CubeListBuilder.create(), PartPose.offset(-1.5F, 9.0F, 0.0F));
		PartDefinition right_leg = body.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(42, 27).addBox(-2.0F, 7.0F, -4.5F, 4.0F, 2.0F, 7.0F, new CubeDeformation(0.0F))
				.texOffs(28, 16).addBox(-2.0F, 0.0F, -2.5F, 4.0F, 7.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(2.5F, 6.0F, 0.0F));
		PartDefinition left_leg = body.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(46, 16).addBox(-2.0F, 0.0F, -2.5F, 4.0F, 7.0F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(42, 36).addBox(-2.0F, 7.0F, -4.5F, 4.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.5F, 6.0F, 0.0F));
		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void translateToHand(HumanoidArm humanoidArm, PoseStack poseStack)
	{
		switch(humanoidArm)
		{
		case LEFT:
			this.handPose.update(this.leftArm, this.leftHand);
			break;
		case RIGHT:
			this.handPose.update(this.rightArm, this.rightHand);
			break;
		}
		this.handPose.writeTo(poseStack);
	}

	@NotNull
	@Override
	public ModelPart getHead()
	{
		return this.head;
	}

	@Override
	public void setupAnim(EntityDwarf entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
	{
		this.head.yRot = (float)Math.toRadians(netHeadYaw);
		this.head.xRot = (float)Math.toRadians(headPitch);
		this.head.zRot = 0F;
		this.rightArm.setRotation(0F, 0F, 0F);
		this.leftArm.setRotation(0F, 0F, 0F);
		this.leftLeg.setRotation(0F, 0F, 0F);
		this.rightLeg.setRotation(0F, 0F, 0F);

		this.leftArm.xRot = Mth.cos(limbSwing * 0.6F + (float)Math.PI) * limbSwingAmount * 0.5F;
		this.rightArm.xRot = Mth.cos(limbSwing * 0.6F) * limbSwingAmount * 0.5F;
		this.leftLeg.xRot = Mth.cos(limbSwing * 0.6F) * limbSwingAmount * 0.7F;
		this.rightLeg.xRot = Mth.cos(limbSwing * 0.6F + (float)Math.PI) * limbSwingAmount * 0.7F;
		AnimationUtils.bobArms(this.rightArm, this.leftArm, ageInTicks);
		this.rightArm.zRot -= 0.1F;
		this.leftArm.zRot += 0.1F;
	}

	@Override
	public ModelPart root()
	{
		return this.body;
	}

	public static ModelDwarf bake(EntityRendererProvider.Context context)
	{
		return new ModelDwarf(context.bakeLayer(LAYER_LOCATION));
	}
}
