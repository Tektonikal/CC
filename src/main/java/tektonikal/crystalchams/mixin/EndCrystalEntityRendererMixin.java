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
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.RotationAxis;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tektonikal.crystalchams.CrystalChams;
import tektonikal.crystalchams.config.ChamsConfig;

import java.awt.*;

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

    //doing the rendering myself seems a lot less janky than writing a dozen separate mixins, and besides, I doubt anyone's going to mixin to here.
    @Inject(method = "render(Lnet/minecraft/entity/decoration/EndCrystalEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("HEAD"), cancellable = true)
    private void CC$renderInject(EndCrystalEntity endCrystalEntity, float yaw, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, CallbackInfo ci) {
        if (!ChamsConfig.CONFIG.instance().modEnabled) {
            return;
        }
        shadowOpacity = ChamsConfig.CONFIG.instance().shadowAlpha;
        shadowRadius = ChamsConfig.CONFIG.instance().shadowRadius;
        light = ChamsConfig.CONFIG.instance().lightLevel != -1 ? ChamsConfig.CONFIG.instance().lightLevel : light;
        float j = ((float) endCrystalEntity.endCrystalAge + tickDelta) * 3.0F;
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(END_CRYSTAL);
        //bottom
        matrixStack.push();
        matrixStack.push();
        matrixStack.scale(2, 2, 2);
        matrixStack.translate(0.0F, -0.5F, 0.0F);
        int overlay = OverlayTexture.DEFAULT_UV;
        if (endCrystalEntity.shouldShowBottom()) {
            this.bottom.render(matrixStack, vertexConsumer, light, overlay);
        }
        matrixStack.pop();
        switch (ChamsConfig.CONFIG.instance().renderLayer) {
            //debug line strip is always 1 pixel wide, and the normal one is fucked up
            case WIREFRAME -> END_CRYSTAL = RenderLayer.getDebugLineStrip(10);
            case GATEWAY -> END_CRYSTAL = RenderLayer.getEndGateway();
            case CULLED -> END_CRYSTAL = RenderLayer.getItemEntityTranslucentCull(TEXTURE);
            default -> END_CRYSTAL = RenderLayer.getEntityTranslucent(TEXTURE);
        }
        //frame 1
        if (ChamsConfig.CONFIG.instance().renderFrame1) {
            matrixStack.push();
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(j * ChamsConfig.CONFIG.instance().frame1RotationSpeed));
            matrixStack.translate(0.0F, 2 + CrystalChams.getYOffset(endCrystalEntity.endCrystalAge, tickDelta, ChamsConfig.CONFIG.instance().frame1Offset, ChamsConfig.CONFIG.instance().frame1BounceSpeed, ChamsConfig.CONFIG.instance().frame1BounceHeight), 0.0F);
            matrixStack.multiply((new Quaternionf()).setAngleAxis(1.0471976F, SINE_45_DEGREES, 0.0F, SINE_45_DEGREES));
            matrixStack.scale(ChamsConfig.CONFIG.instance().frame1Scale, ChamsConfig.CONFIG.instance().frame1Scale, ChamsConfig.CONFIG.instance().frame1Scale);
            if (ChamsConfig.CONFIG.instance().frame1Rainbow) {
                Color col = CrystalChams.getRainbowCol(ChamsConfig.CONFIG.instance().frame1RainbowDelay, ChamsConfig.CONFIG.instance().frame1RainbowSpeed);
                this.frame.render(matrixStack, vertexConsumer, light, overlay, getColor(col, ChamsConfig.CONFIG.instance().frame1Alpha));
            } else {
                try {
                    Color col = ChamsConfig.CONFIG.instance().frame1Color;
                    this.frame.render(matrixStack, vertexConsumer, light, overlay, getColor(col, ChamsConfig.CONFIG.instance().frame1Alpha));
                } catch (NumberFormatException e) {
                    this.frame.render(matrixStack, vertexConsumer, light, overlay, ColorHelper.Argb.getArgb(1, 0, 0, 1));
                }
            }
            matrixStack.pop();
        }
        //frame 2
        if (ChamsConfig.CONFIG.instance().renderFrame2) {
            matrixStack.push();
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(j * ChamsConfig.CONFIG.instance().frame2RotationSpeed));
            matrixStack.translate(0.0F, 2F + CrystalChams.getYOffset(endCrystalEntity.endCrystalAge, tickDelta, ChamsConfig.CONFIG.instance().frame2Offset, ChamsConfig.CONFIG.instance().frame2BounceSpeed, ChamsConfig.CONFIG.instance().frame2BounceHeight), 0.0F);
            matrixStack.multiply((new Quaternionf()).setAngleAxis(1.0471976F, SINE_45_DEGREES, 0.0F, SINE_45_DEGREES));
            matrixStack.multiply((new Quaternionf()).setAngleAxis(1.0471976F, SINE_45_DEGREES, 0.0F, SINE_45_DEGREES));
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(j * ChamsConfig.CONFIG.instance().frame2RotationSpeed));
            matrixStack.scale(ChamsConfig.CONFIG.instance().frame2Scale, ChamsConfig.CONFIG.instance().frame2Scale, ChamsConfig.CONFIG.instance().frame2Scale);
            if (ChamsConfig.CONFIG.instance().frame2Rainbow) {
                Color col = CrystalChams.getRainbowCol(ChamsConfig.CONFIG.instance().frame2RainbowDelay, ChamsConfig.CONFIG.instance().frame2RainbowSpeed);
                this.frame.render(matrixStack, vertexConsumer, light, overlay, getColor(col, ChamsConfig.CONFIG.instance().frame2Alpha));
            } else {
                try {
                    Color col = ChamsConfig.CONFIG.instance().frame2Color;
                    this.frame.render(matrixStack, vertexConsumer, light, overlay, getColor(col, ChamsConfig.CONFIG.instance().frame2Alpha));
                } catch (NumberFormatException e) {
                    this.frame.render(matrixStack, vertexConsumer, light, overlay, ColorHelper.Argb.getArgb(1, 0, 0, 1));
                }
            }
            matrixStack.pop();
        }
        //core
        if (ChamsConfig.CONFIG.instance().renderCore) {
            matrixStack.push();
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(j * ChamsConfig.CONFIG.instance().coreRotationSpeed));
            matrixStack.translate(0.0F, 2F + CrystalChams.getYOffset(endCrystalEntity.endCrystalAge, tickDelta, ChamsConfig.CONFIG.instance().coreOffset, ChamsConfig.CONFIG.instance().coreBounceSpeed, ChamsConfig.CONFIG.instance().coreBounceHeight), 0.0F);
            matrixStack.multiply((new Quaternionf()).setAngleAxis(1.0471976F, SINE_45_DEGREES, 0.0F, SINE_45_DEGREES));
            matrixStack.multiply((new Quaternionf()).setAngleAxis(1.0471976F, SINE_45_DEGREES, 0.0F, SINE_45_DEGREES));
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(j * ChamsConfig.CONFIG.instance().coreRotationSpeed));
            matrixStack.multiply((new Quaternionf()).setAngleAxis(1.0471976F, SINE_45_DEGREES, 0.0F, SINE_45_DEGREES));
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(j * ChamsConfig.CONFIG.instance().coreRotationSpeed));
            matrixStack.scale(ChamsConfig.CONFIG.instance().coreScale, ChamsConfig.CONFIG.instance().coreScale, ChamsConfig.CONFIG.instance().coreScale);
            if (ChamsConfig.CONFIG.instance().coreRainbow) {
                Color col = CrystalChams.getRainbowCol(ChamsConfig.CONFIG.instance().coreRainbowDelay, ChamsConfig.CONFIG.instance().coreRainbowSpeed);
                this.core.render(matrixStack, vertexConsumer, light, overlay, getColor(col, ChamsConfig.CONFIG.instance().coreAlpha));
            } else {
                try {
                    Color col = ChamsConfig.CONFIG.instance().coreColor;
                    this.core.render(matrixStack, vertexConsumer, light, overlay, getColor(col, ChamsConfig.CONFIG.instance().coreAlpha));
                } catch (NumberFormatException e) {
                    this.core.render(matrixStack, vertexConsumer, light, overlay, ColorHelper.Argb.getArgb(1, 0, 0, 1));
                }
            }
            matrixStack.pop();
        }
        matrixStack.pop();
        BlockPos blockPos = endCrystalEntity.getBeamTarget();
        if (blockPos != null) {
            float m = (float)blockPos.getX() + 0.5F;
            float n = (float)blockPos.getY() + 0.5F;
            float o = (float)blockPos.getZ() + 0.5F;
            float p = (float)((double)m - endCrystalEntity.getX());
            float q = (float)((double)n - endCrystalEntity.getY());
            float r = (float)((double)o - endCrystalEntity.getZ());
            matrixStack.translate(p, q, r);
            EnderDragonEntityRenderer.renderCrystalBeam(-p, -q + CrystalChams.getYOffset(endCrystalEntity.endCrystalAge, tickDelta, ChamsConfig.CONFIG.instance().coreOffset, ChamsConfig.CONFIG.instance().coreBounceSpeed, ChamsConfig.CONFIG.instance().coreBounceHeight), -r, tickDelta, endCrystalEntity.endCrystalAge, matrixStack, vertexConsumerProvider, light);
        }
        super.render(endCrystalEntity, yaw, tickDelta, matrixStack, vertexConsumerProvider, light);
        ci.cancel();
    }

    @Unique
    private static int getColor(Color col, float alpha) {
        return ColorHelper.Argb.getArgb((int) (alpha * 255.0F), col.getRed(), col.getGreen(), col.getBlue());
    }
}
