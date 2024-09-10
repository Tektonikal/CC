package tektonikal.crystalchams.mixin;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EndCrystalEntityRenderer;
import net.minecraft.client.render.entity.EnderDragonEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tektonikal.crystalchams.config.ChamsConfig;

import java.awt.*;

import static tektonikal.crystalchams.config.ChamsConfig.RenderMode.*;

@Mixin(EndCrystalEntityRenderer.class)
public abstract class EndCrystalEntityRendererMixin extends EntityRenderer<EndCrystalEntity> {

    @Shadow
    @Final
    private static Identifier TEXTURE;
    @Shadow
    @Final
    @Mutable
    private static RenderLayer END_CRYSTAL = RenderLayer.getEntityTranslucent(TEXTURE);

    @Shadow
    @Final
    private ModelPart bottom;

    @Shadow
    @Final
    private static float SINE_45_DEGREES;

    @Shadow
    @Final
    private ModelPart frame;

    @Shadow
    @Final
    private ModelPart core;

    protected EndCrystalEntityRendererMixin(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Unique
    private static float getYOffset(EndCrystalEntity crystal, float tickDelta, float offset) {
        float f = (float) crystal.endCrystalAge + tickDelta;
        float g = MathHelper.sin(f * ChamsConfig.BounceSpeed) / 2.0F + 0.5F;
        g = (g * g + g) * ChamsConfig.bounce;
        return g - 1.4F + offset;
    }

    //doing the rendering myself seems a lot less janky than writing a dozen separate mixins, and besides, I doubt anyone's going to mixin to here.
    @Inject(method = "render(Lnet/minecraft/entity/decoration/EndCrystalEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("HEAD"), cancellable = true)
    private void CC$renderInject(EndCrystalEntity endCrystalEntity, float yaw, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, CallbackInfo ci) {
        if (!ChamsConfig.isActive) {
            return;
        }
        shadowOpacity = ChamsConfig.shadowOpacity;
        shadowRadius = ChamsConfig.shadowSize;
        light = ChamsConfig.lLevel != -1 ? ChamsConfig.lLevel : light;
        float j = ((float) endCrystalEntity.endCrystalAge + tickDelta) * 3.0F;
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(END_CRYSTAL);
        //bottom
        matrixStack.push();
        matrixStack.push();
        matrixStack.scale(2, 2, 2);
        matrixStack.translate(0.0F, -0.5F, 0.0F);
        int k = OverlayTexture.DEFAULT_UV;
        if (endCrystalEntity.shouldShowBottom()) {
            this.bottom.render(matrixStack, vertexConsumer, light, k);
        }
        matrixStack.pop();
        switch (ChamsConfig.renderMode){
            //for some reason the lineWidth value does absolutely nothing when I try to change it
            case WIREFRAME -> END_CRYSTAL = RenderLayer.getDebugLineStrip(10);
            case GATEWAY -> END_CRYSTAL = RenderLayer.getEndGateway();
            case CULLED -> END_CRYSTAL = RenderLayer.getItemEntityTranslucentCull(TEXTURE);
            default -> END_CRYSTAL = RenderLayer.getEntityTranslucent(TEXTURE);
        }
        //frame 1
        if (ChamsConfig.renderFrame1) {
            matrixStack.push();
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(j));
            matrixStack.translate(0.0F, 2F + getYOffset(endCrystalEntity, tickDelta, ChamsConfig.frame1Offset), 0.0F);
            matrixStack.multiply((new Quaternionf()).setAngleAxis(1.0471976F, SINE_45_DEGREES, 0.0F, SINE_45_DEGREES));
            matrixStack.scale(ChamsConfig.frame1Scale, ChamsConfig.frame1Scale, ChamsConfig.frame1Scale);
            try {
                Color col = Color.decode(ChamsConfig.frameCol);
                this.frame.render(matrixStack, vertexConsumer, light, k, col.getRed() / 255.0F, col.getGreen() / 255.0F, col.getBlue() / 255.0F, ChamsConfig.frame1Alpha);
            } catch (NumberFormatException e) {
                this.frame.render(matrixStack, vertexConsumer, light, k, 1, 0, 0, 1);
            }
            matrixStack.pop();
        }
        //frame 2
        if (ChamsConfig.renderFrame2) {
            matrixStack.push();
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(j));
            matrixStack.translate(0.0F, 2F + getYOffset(endCrystalEntity, tickDelta, ChamsConfig.frame2Offset), 0.0F);
            matrixStack.multiply((new Quaternionf()).setAngleAxis(1.0471976F, SINE_45_DEGREES, 0.0F, SINE_45_DEGREES));
            matrixStack.multiply((new Quaternionf()).setAngleAxis(1.0471976F, SINE_45_DEGREES, 0.0F, SINE_45_DEGREES));
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(j));
            matrixStack.scale(ChamsConfig.frame2Scale, ChamsConfig.frame2Scale, ChamsConfig.frame2Scale);
            try {
                Color col = Color.decode(ChamsConfig.frameCol2);
                this.frame.render(matrixStack, vertexConsumer, light, k, col.getRed() / 255.0F, col.getGreen() / 255.0F, col.getBlue() / 255.0F, ChamsConfig.frame2Alpha);
            } catch (NumberFormatException e) {
                this.frame.render(matrixStack, vertexConsumer, light, k, 1, 0, 0, 1);
            }
            matrixStack.pop();
        }
        //core
        if (ChamsConfig.renderCore) {
            matrixStack.push();
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(j));
            matrixStack.translate(0.0F, 2F + getYOffset(endCrystalEntity, tickDelta, ChamsConfig.coreOffset), 0.0F);
            matrixStack.multiply((new Quaternionf()).setAngleAxis(1.0471976F, SINE_45_DEGREES, 0.0F, SINE_45_DEGREES));
            matrixStack.multiply((new Quaternionf()).setAngleAxis(1.0471976F, SINE_45_DEGREES, 0.0F, SINE_45_DEGREES));
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(j));
            matrixStack.multiply((new Quaternionf()).setAngleAxis(1.0471976F, SINE_45_DEGREES, 0.0F, SINE_45_DEGREES));
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(j));
            matrixStack.scale(ChamsConfig.coreScale, ChamsConfig.coreScale, ChamsConfig.coreScale);
            try {
                Color col = Color.decode(ChamsConfig.col);
                this.core.render(matrixStack, vertexConsumer, light, k, col.getRed() / 255.0F, col.getGreen() / 255.0F, col.getBlue() / 255.0F, ChamsConfig.alpha);
            } catch (NumberFormatException e) {
                this.core.render(matrixStack, vertexConsumer, light, k, 1, 0, 0, 1);
            }
            matrixStack.pop();
        }
        matrixStack.pop();
        //don't care about any of this
        BlockPos blockPos = endCrystalEntity.getBeamTarget();
        if (blockPos != null) {
            float m = (float) blockPos.getX() + 0.5F;
            float n = (float) blockPos.getY() + 0.5F;
            float o = (float) blockPos.getZ() + 0.5F;
            float p = (float) ((double) m - endCrystalEntity.getX());
            float q = (float) ((double) n - endCrystalEntity.getY());
            float r = (float) ((double) o - endCrystalEntity.getZ());
            matrixStack.translate(p, q, r);
            EnderDragonEntityRenderer.renderCrystalBeam(-p, -q + getYOffset(endCrystalEntity, tickDelta, 0F), -r, tickDelta, endCrystalEntity.endCrystalAge, matrixStack, vertexConsumerProvider, light);
        }

        super.render(endCrystalEntity, yaw, tickDelta, matrixStack, vertexConsumerProvider, light);
        ci.cancel();
    }
}
