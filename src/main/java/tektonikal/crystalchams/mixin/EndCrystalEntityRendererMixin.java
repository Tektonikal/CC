package tektonikal.crystalchams.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EndCrystalEntityRenderer;
import net.minecraft.client.render.entity.EnderDragonEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tektonikal.crystalchams.CrystalChams;
import tektonikal.crystalchams.config.ChamsConfig;
import tektonikal.crystalchams.interfaces.EndCrystalEntityMixinInterface;

import java.awt.*;

import static tektonikal.crystalchams.CrystalChams.getLayer;

@Mixin(EndCrystalEntityRenderer.class)
public abstract class EndCrystalEntityRendererMixin extends EntityRenderer<EndCrystalEntity> {

    @Mutable
    @Shadow
    @Final
    private static Identifier TEXTURE;

    @Shadow
    @Final
    private ModelPart bottom;

    @Shadow
    @Final
    private ModelPart frame;

    @Shadow
    @Final
    private ModelPart core;

    @Shadow
    @Final
    private static float SINE_45_DEGREES;
    //replace this with config values later, maybe
    @Unique
    private static float baseScale = ChamsConfig.CONFIG.instance().baseScale;
    @Unique
    private static float coreScale = ChamsConfig.CONFIG.instance().coreScale;
    @Unique
    private static float frame1Scale = ChamsConfig.CONFIG.instance().frame1Scale;
    @Unique
    private static float frame2Scale = ChamsConfig.CONFIG.instance().frame2Scale;
    @Unique
    private static float frame1Offset = ChamsConfig.CONFIG.instance().frame1Offset;
    @Unique
    private static float frame1RotationSpeed = ChamsConfig.CONFIG.instance().frame1RotationSpeed;
    @Unique
    private static float frame1BounceHeight = ChamsConfig.CONFIG.instance().frame1BounceHeight;
    @Unique
    private static float frame1BounceSpeed = ChamsConfig.CONFIG.instance().frame1BounceSpeed;
    @Unique
    private static float frame1TickDelay = ChamsConfig.CONFIG.instance().frame1TickDelay;
    @Unique
    private static float frame1Alpha = ChamsConfig.CONFIG.instance().frame1Alpha;
    @Unique
    private static float frame1LightLevel = ChamsConfig.CONFIG.instance().frame1LightLevel;
    @Unique
    private static float[] frame1Colors = new float[]{ChamsConfig.CONFIG.instance().frame1Color.getRed(), ChamsConfig.CONFIG.instance().frame1Color.getGreen(), ChamsConfig.CONFIG.instance().frame1Color.getBlue()};
    @Unique
    private static float frame1RainbowSpeed = ChamsConfig.CONFIG.instance().frame1RainbowSpeed;

    public EndCrystalEntityRendererMixin(EntityRendererFactory.Context context) {
        super(context);
    }

    @Inject(method = "render(Lnet/minecraft/entity/decoration/EndCrystalEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("HEAD"), cancellable = true)
    private void CC$renderInject(EndCrystalEntity endCrystalEntity, float yaw, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, CallbackInfo ci) {
        float j = ((endCrystalEntity.endCrystalAge) + tickDelta);
        updateAnimation(endCrystalEntity, j);
        if (!ChamsConfig.CONFIG.instance().modEnabled) {
            shadowOpacity = 1F;
            shadowRadius = 0.5F;
            return;
        }
        shadowOpacity = ChamsConfig.CONFIG.instance().shadowAlpha * 2F;
        shadowRadius = ChamsConfig.CONFIG.instance().shadowRadius;
        //base
        matrixStack.push();
        matrixStack.push();
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotation((float) (Math.PI / 180.0) * ChamsConfig.CONFIG.instance().baseRotation));
        matrixStack.translate(0.0F, ChamsConfig.CONFIG.instance().baseOffset, 0.0F);
        matrixStack.scale(baseScale * 2, baseScale * 2, baseScale * 2);
        int overlay = OverlayTexture.DEFAULT_UV;
        if ((endCrystalEntity.shouldShowBottom() && ChamsConfig.CONFIG.instance().showBaseMode == ChamsConfig.BaseRenderMode.DEFAULT) || ChamsConfig.CONFIG.instance().showBaseMode == ChamsConfig.BaseRenderMode.ALWAYS) {
            if (ChamsConfig.CONFIG.instance().baseRainbow) {
                Color col = new Color(CrystalChams.getRainbow(ChamsConfig.CONFIG.instance().baseRainbowDelay, ChamsConfig.CONFIG.instance().baseRainbowSpeed, ChamsConfig.CONFIG.instance().baseRainbowSaturation, ChamsConfig.CONFIG.instance().baseRainbowBrightness));
                this.bottom.renderWithoutChildren(matrixStack, getLayer(vertexConsumerProvider, ChamsConfig.CONFIG.instance().baseRenderMode, ChamsConfig.CONFIG.instance().baseCulling, TEXTURE), ChamsConfig.CONFIG.instance().baseLightLevel != -1 ? ChamsConfig.CONFIG.instance().baseLightLevel : light, overlay, getColor(col, ChamsConfig.CONFIG.instance().baseAlpha));
            } else {
                try {
                    Color col = ChamsConfig.CONFIG.instance().baseColor;
                    this.bottom.renderWithoutChildren(matrixStack, getLayer(vertexConsumerProvider, ChamsConfig.CONFIG.instance().baseRenderMode, ChamsConfig.CONFIG.instance().baseCulling, TEXTURE), ChamsConfig.CONFIG.instance().baseLightLevel != -1 ? ChamsConfig.CONFIG.instance().baseLightLevel : light, overlay, getColor(col, ChamsConfig.CONFIG.instance().baseAlpha));
                } catch (NumberFormatException e) {
                    this.bottom.renderWithoutChildren(matrixStack, getLayer(vertexConsumerProvider, ChamsConfig.CONFIG.instance().baseRenderMode, ChamsConfig.CONFIG.instance().baseCulling, TEXTURE), ChamsConfig.CONFIG.instance().baseLightLevel != -1 ? ChamsConfig.CONFIG.instance().baseLightLevel : light, overlay, ColorHelper.Argb.getArgb(1, 0, 0, 1));
                }
            }
        }
        matrixStack.pop();
        //frame 1
        if (ChamsConfig.CONFIG.instance().renderFrame1) {
            matrixStack.push();
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(((j + ChamsConfig.CONFIG.instance().frame1TickDelay) % 360) * (ChamsConfig.CONFIG.instance().frame1RotationSpeed * 3)));
            matrixStack.translate(0.0F, 2F + CrystalChams.getYOffset(endCrystalEntity.endCrystalAge + tickDelta, frame1Offset, ChamsConfig.CONFIG.instance().frame1BounceSpeed, frame1BounceHeight, frame1TickDelay), 0.0F);
            matrixStack.multiply((new Quaternionf()).setAngleAxis(1.0471976F, SINE_45_DEGREES, 0.0F, SINE_45_DEGREES));
            matrixStack.scale(frame1Scale * 2, frame1Scale * 2, frame1Scale * 2);
            Color col = new Color(frame1Colors[0] / 255.0F, frame1Colors[1] / 255.0F, frame1Colors[2] / 255.0F);
            this.frame.renderWithoutChildren(matrixStack, getLayer(vertexConsumerProvider, ChamsConfig.CONFIG.instance().frame1RenderLayer, ChamsConfig.CONFIG.instance().frame1Culling, TEXTURE), ChamsConfig.CONFIG.instance().frame1LightLevel != -1 ? (int) frame1LightLevel : light, overlay, getColor(col, frame1Alpha));
            matrixStack.pop();
        }
        //frame 2
        if (ChamsConfig.CONFIG.instance().renderFrame2) {
            matrixStack.push();
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((j + ChamsConfig.CONFIG.instance().frame2TickDelay) * (ChamsConfig.CONFIG.instance().frame2RotationSpeed * 3)));
            matrixStack.translate(0.0F, 2F + CrystalChams.getYOffset(endCrystalEntity.endCrystalAge + tickDelta, ChamsConfig.CONFIG.instance().frame2Offset, ChamsConfig.CONFIG.instance().frame2BounceSpeed, ChamsConfig.CONFIG.instance().frame2BounceHeight, ChamsConfig.CONFIG.instance().frame2TickDelay), 0.0F);
            matrixStack.multiply((new Quaternionf()).setAngleAxis(1.0471976F, SINE_45_DEGREES, 0.0F, SINE_45_DEGREES));
            matrixStack.multiply((new Quaternionf()).setAngleAxis(1.0471976F, SINE_45_DEGREES, 0.0F, SINE_45_DEGREES));
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((j + ChamsConfig.CONFIG.instance().frame2TickDelay) * (ChamsConfig.CONFIG.instance().frame2RotationSpeed * 3)));
            matrixStack.scale(frame2Scale * 1.75F, frame2Scale * 1.75F, frame2Scale * 1.75F);
            if (ChamsConfig.CONFIG.instance().frame2Rainbow) {
                Color col = new Color(CrystalChams.getRainbow(ChamsConfig.CONFIG.instance().frame2RainbowDelay, ChamsConfig.CONFIG.instance().frame2RainbowSpeed, ChamsConfig.CONFIG.instance().frame2RainbowSaturation, ChamsConfig.CONFIG.instance().frame2RainbowBrightness));
                this.frame.renderWithoutChildren(matrixStack, getLayer(vertexConsumerProvider, ChamsConfig.CONFIG.instance().frame2RenderLayer, ChamsConfig.CONFIG.instance().frame2Culling, TEXTURE), ChamsConfig.CONFIG.instance().frame2LightLevel != -1 ? ChamsConfig.CONFIG.instance().frame2LightLevel : light, overlay, getColor(col, ChamsConfig.CONFIG.instance().frame2Alpha));
            } else {
                try {
                    Color col = ChamsConfig.CONFIG.instance().frame2Color;
                    this.frame.renderWithoutChildren(matrixStack, getLayer(vertexConsumerProvider, ChamsConfig.CONFIG.instance().frame2RenderLayer, ChamsConfig.CONFIG.instance().frame2Culling, TEXTURE), ChamsConfig.CONFIG.instance().frame2LightLevel != -1 ? ChamsConfig.CONFIG.instance().frame2LightLevel : light, overlay, getColor(col, ChamsConfig.CONFIG.instance().frame2Alpha));
                } catch (NumberFormatException e) {
                    this.frame.renderWithoutChildren(matrixStack, getLayer(vertexConsumerProvider, ChamsConfig.CONFIG.instance().frame2RenderLayer, ChamsConfig.CONFIG.instance().frame2Culling, TEXTURE), ChamsConfig.CONFIG.instance().frame2LightLevel != -1 ? ChamsConfig.CONFIG.instance().frame2LightLevel : light, overlay, ColorHelper.Argb.getArgb(1, 0, 0, 1));
                }
            }
            matrixStack.pop();
        }
        //core
        if (ChamsConfig.CONFIG.instance().renderCore) {
            matrixStack.push();
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(((j + ChamsConfig.CONFIG.instance().coreTickDelay) * (ChamsConfig.CONFIG.instance().coreRotationSpeed * 3)) % 360));
            matrixStack.translate(0.0F, 2F + CrystalChams.getYOffset(endCrystalEntity.endCrystalAge + tickDelta, ChamsConfig.CONFIG.instance().coreOffset, ChamsConfig.CONFIG.instance().coreBounceSpeed, ChamsConfig.CONFIG.instance().coreBounceHeight, ChamsConfig.CONFIG.instance().coreTickDelay), 0.0F);
            matrixStack.multiply((new Quaternionf()).setAngleAxis(1.0471976F, SINE_45_DEGREES, 0.0F, SINE_45_DEGREES));
            matrixStack.multiply((new Quaternionf()).setAngleAxis(1.0471976F, SINE_45_DEGREES, 0.0F, SINE_45_DEGREES));
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(((j + ChamsConfig.CONFIG.instance().coreTickDelay) * (ChamsConfig.CONFIG.instance().coreRotationSpeed * 3)) % 360));
            matrixStack.multiply((new Quaternionf()).setAngleAxis(1.0471976F, SINE_45_DEGREES, 0.0F, SINE_45_DEGREES));
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(((j + ChamsConfig.CONFIG.instance().coreTickDelay) * (ChamsConfig.CONFIG.instance().coreRotationSpeed * 3)) % 360));
            float v = ChamsConfig.CONFIG.instance().coreScaleAnimation ? MathHelper.lerp(ChamsConfig.CONFIG.instance().coreScaleEasing.getFunction().apply((double) MathHelper.clamp((endCrystalEntity.age + tickDelta - (ChamsConfig.CONFIG.instance().coreScaleDelay * 20)) / (20 * ChamsConfig.CONFIG.instance().coreScaleAnimDuration), 0, 1)).floatValue(), ChamsConfig.CONFIG.instance().coreStartScale, coreScale) : coreScale;
            matrixStack.scale(v * 1.53125F, v * 1.53125F, v * 1.53125F);
            Color col = new Color(CrystalChams.getRainbow(ChamsConfig.CONFIG.instance().coreRainbowDelay, ChamsConfig.CONFIG.instance().coreRainbowSpeed, ChamsConfig.CONFIG.instance().coreRainbowSaturation, ChamsConfig.CONFIG.instance().coreRainbowBrightness));
            this.core.renderWithoutChildren(matrixStack, getLayer(vertexConsumerProvider, ChamsConfig.CONFIG.instance().coreRenderLayer, ChamsConfig.CONFIG.instance().coreCulling, TEXTURE), ChamsConfig.CONFIG.instance().coreLightLevel != -1 ? ChamsConfig.CONFIG.instance().coreLightLevel : light, overlay, getColor(
                    ChamsConfig.CONFIG.instance().coreRainbow ? col : ChamsConfig.CONFIG.instance().coreColor,
                    ChamsConfig.CONFIG.instance().coreAlphaAnimation ? MathHelper.lerp(ChamsConfig.CONFIG.instance().coreAlphaEasing.getFunction().apply((double) MathHelper.clamp((endCrystalEntity.age + tickDelta - (ChamsConfig.CONFIG.instance().coreAlphaDelay * 20)) / (20 * ChamsConfig.CONFIG.instance().coreAlphaAnimDuration), 0, 1)).floatValue(), ChamsConfig.CONFIG.instance().coreStartAlpha, ChamsConfig.CONFIG.instance().coreAlpha) : ChamsConfig.CONFIG.instance().coreAlpha));
            matrixStack.pop();
        }
        matrixStack.pop();
        BlockPos blockPos = endCrystalEntity.getBeamTarget();
        if (blockPos != null) {
            float m = (float) blockPos.getX() + 0.5F;
            float n = (float) blockPos.getY() + 0.5F;
            float o = (float) blockPos.getZ() + 0.5F;
            float p = (float) ((double) m - endCrystalEntity.getX());
            float q = (float) ((double) n - endCrystalEntity.getY());
            float r = (float) ((double) o - endCrystalEntity.getZ());
            matrixStack.translate(p, q, r);
            EnderDragonEntityRenderer.renderCrystalBeam(-p, -q + CrystalChams.getYOffset(endCrystalEntity.endCrystalAge + tickDelta, ChamsConfig.CONFIG.instance().coreOffset, ChamsConfig.CONFIG.instance().coreBounceSpeed, ChamsConfig.CONFIG.instance().coreBounceHeight, ChamsConfig.CONFIG.instance().coreTickDelay), -r, tickDelta, endCrystalEntity.endCrystalAge, matrixStack, vertexConsumerProvider, light);
        }
        super.render(endCrystalEntity, yaw, tickDelta, matrixStack, vertexConsumerProvider, light);
        ci.cancel();
    }

    @Unique
    private static void updateAnimation(EndCrystalEntity entity, float tickAndDelta) {
//        EndCrystalEntityMixinInterface e = ((EndCrystalEntityMixinInterface) entity);
        baseScale = (float) ease(baseScale, ChamsConfig.CONFIG.instance().baseScale, 7.5F);
        coreScale = (float) ease(coreScale, ChamsConfig.CONFIG.instance().coreScale, 7.5F);
        frame2Scale = (float) ease(frame2Scale, ChamsConfig.CONFIG.instance().frame2Scale, 7.5F);
        frame1Offset = (float) ease(frame1Offset, ChamsConfig.CONFIG.instance().frame1Offset, 7.5F);
//        frame1RotationSpeed = (float) ease(frame1RotationSpeed, ChamsConfig.CONFIG.instance().frame1RotationSpeed, 15F);
        frame1BounceHeight = (float) ease(frame1BounceHeight, ChamsConfig.CONFIG.instance().frame1BounceHeight, 7.5F);
//        frame1BounceSpeed = (float) ease(frame1BounceSpeed, ChamsConfig.CONFIG.instance().frame1BounceSpeed, 7.5F);
        frame1TickDelay = (float) ease(frame1TickDelay, ChamsConfig.CONFIG.instance().frame1TickDelay, 7.5F);
        frame1Scale = (float) ease(frame1Scale, ChamsConfig.CONFIG.instance().frame1Scale, 7.5F);
        frame1Alpha = (float) ease(frame1Alpha, ChamsConfig.CONFIG.instance().frame1Alpha, 7.5F);
        frame1LightLevel = (float) ease(frame1LightLevel, ChamsConfig.CONFIG.instance().frame1LightLevel, 7.5F);
        if (ChamsConfig.CONFIG.instance().frame1Rainbow) {
            Color col = new Color(CrystalChams.getRainbow(ChamsConfig.CONFIG.instance().frame1RainbowDelay, ChamsConfig.CONFIG.instance().frame1RainbowSpeed, ChamsConfig.CONFIG.instance().frame1RainbowSaturation, ChamsConfig.CONFIG.instance().frame1RainbowBrightness));
            frame1Colors[0] = (float) ease(frame1Colors[0], col.getRed(), 7.5F);
            frame1Colors[1] = (float) ease(frame1Colors[1], col.getGreen(), 7.5F);
            frame1Colors[2] = (float) ease(frame1Colors[2], col.getBlue(), 7.5F);
        } else {
            frame1Colors[0] = (float) ease(frame1Colors[0], ChamsConfig.CONFIG.instance().frame1Color.getRed(), 7.5F);
            frame1Colors[1] = (float) ease(frame1Colors[1], ChamsConfig.CONFIG.instance().frame1Color.getGreen(), 7.5F);
            frame1Colors[2] = (float) ease(frame1Colors[2], ChamsConfig.CONFIG.instance().frame1Color.getBlue(), 7.5F);
        }
//        frame1RainbowSpeed = (float) ease(frame1RainbowSpeed, ChamsConfig.CONFIG.instance().frame1RainbowSpeed, 7.5F);
    }

    @Unique
    private static double ease(double start, double end, float speed) {
        return (start + (end - start) * (1 - Math.exp(-(1.0F / MinecraftClient.getInstance().getCurrentFps()) * speed)));
    }

    @Unique
    private static int getColor(Color col, float alpha) {
        return ColorHelper.Argb.getArgb((int) (alpha * 255.0F), col.getRed(), col.getGreen(), col.getBlue());
    }

}
