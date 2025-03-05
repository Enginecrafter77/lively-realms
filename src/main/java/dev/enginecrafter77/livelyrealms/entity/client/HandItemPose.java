package dev.enginecrafter77.livelyrealms.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelPart;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

@OnlyIn(Dist.CLIENT)
public class HandItemPose {
	private final Matrix4f armTranslate;
	private final Matrix4f armRotation;
	private final Matrix4f handTranslate;
	private final Matrix4f transform;
	private final Quaternionf armRotationQuaternion;

	public HandItemPose()
	{
		this.armRotationQuaternion = new Quaternionf();
		this.armTranslate = new Matrix4f();
		this.armRotation = new Matrix4f();
		this.handTranslate = new Matrix4f();
		this.transform = new Matrix4f();
	}

	public void update(ModelPart arm, ModelPart hand)
	{
		this.handTranslate.identity();
		this.armTranslate.identity();
		this.armRotation.identity();
		this.armRotationQuaternion.identity();

		this.armTranslate.translate(arm.x / 16F, arm.y / 16F, arm.z / 16F);
		this.handTranslate.translate((hand.x + Math.signum(hand.x)) / 16F, hand.y / 16F, hand.z / 16F);
		this.armRotationQuaternion.rotationZYX(arm.zRot, arm.yRot, arm.xRot);
		this.armRotation.rotate(this.armRotationQuaternion);

		this.transform.identity();
		this.transform.mul(this.armTranslate);
		this.transform.mul(this.handTranslate);
		this.transform.mul(this.armRotation);
	}

	public void writeTo(PoseStack poseStack)
	{
		poseStack.mulPose(this.transform);
	}
}
