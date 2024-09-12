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
    private static float getYOffset(EndCrystalEntity crystal, float tickDelta, float offset, float bounceSpeed, float bounceHeight) {
        float f = (float) crystal.endCrystalAge + tickDelta;
        float g = MathHelper.sin(f * bounceSpeed) / 2.0F + 0.5F;
        g = (g * g + g) * bounceHeight;
        return g - 1.4F + offset;
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
        //TODO: make independent
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
        switch (ChamsConfig.CONFIG.instance().renderLayer) {
            //for some reason the lineWidth value does absolutely nothing when I try to change it
            case WIREFRAME -> END_CRYSTAL = RenderLayer.getDebugLineStrip(10);
            case GATEWAY -> END_CRYSTAL = RenderLayer.getEndGateway();
            case CULLED -> END_CRYSTAL = RenderLayer.getItemEntityTranslucentCull(TEXTURE);
            default -> END_CRYSTAL = RenderLayer.getEntityTranslucent(TEXTURE);
        }
        //frame 1
        if (ChamsConfig.CONFIG.instance().renderFrame1) {
            matrixStack.push();
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(j * ChamsConfig.CONFIG.instance().frame1RotationSpeed));
            matrixStack.translate(0.0F, 2F + getYOffset(endCrystalEntity, tickDelta, ChamsConfig.CONFIG.instance().frame1Offset, ChamsConfig.CONFIG.instance().frame1BounceSpeed, ChamsConfig.CONFIG.instance().frame1BounceHeight), 0.0F);
            matrixStack.multiply((new Quaternionf()).setAngleAxis(1.0471976F, SINE_45_DEGREES, 0.0F, SINE_45_DEGREES));
            matrixStack.scale(ChamsConfig.CONFIG.instance().frame1Scale, ChamsConfig.CONFIG.instance().frame1Scale, ChamsConfig.CONFIG.instance().frame1Scale);
            try {
                Color col = ChamsConfig.CONFIG.instance().frame1Color;
                this.frame.render(matrixStack, vertexConsumer, light, k, col.getRed() / 255.0F, col.getGreen() / 255.0F, col.getBlue() / 255.0F, ChamsConfig.CONFIG.instance().frame1Alpha);
            } catch (NumberFormatException e) {
                this.frame.render(matrixStack, vertexConsumer, light, k, 1, 0, 0, 1);
            }
            matrixStack.pop();
        }
        //frame 2
        if (ChamsConfig.CONFIG.instance().renderFrame2) {
            matrixStack.push();
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(j * ChamsConfig.CONFIG.instance().frame2RotationSpeed));
            matrixStack.translate(0.0F, 2F + getYOffset(endCrystalEntity, tickDelta, ChamsConfig.CONFIG.instance().frame2Offset, ChamsConfig.CONFIG.instance().frame2BounceSpeed, ChamsConfig.CONFIG.instance().frame2BounceHeight), 0.0F);
            matrixStack.multiply((new Quaternionf()).setAngleAxis(1.0471976F, SINE_45_DEGREES, 0.0F, SINE_45_DEGREES));
            matrixStack.multiply((new Quaternionf()).setAngleAxis(1.0471976F, SINE_45_DEGREES, 0.0F, SINE_45_DEGREES));
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(j * ChamsConfig.CONFIG.instance().frame2RotationSpeed));
            matrixStack.scale(ChamsConfig.CONFIG.instance().frame2Scale, ChamsConfig.CONFIG.instance().frame2Scale, ChamsConfig.CONFIG.instance().frame2Scale);
            try {
                Color col = ChamsConfig.CONFIG.instance().frame2Color;
                this.frame.render(matrixStack, vertexConsumer, light, k, col.getRed() / 255.0F, col.getGreen() / 255.0F, col.getBlue() / 255.0F, ChamsConfig.CONFIG.instance().frame2Alpha);
            } catch (NumberFormatException e) {
                this.frame.render(matrixStack, vertexConsumer, light, k, 1, 0, 0, 1);
            }
            matrixStack.pop();
        }
        //core
        if (ChamsConfig.CONFIG.instance().renderCore) {
            matrixStack.push();
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(j * ChamsConfig.CONFIG.instance().coreRotationSpeed));
            matrixStack.translate(0.0F, 2F + getYOffset(endCrystalEntity, tickDelta, ChamsConfig.CONFIG.instance().coreOffset, ChamsConfig.CONFIG.instance().coreBounceSpeed, ChamsConfig.CONFIG.instance().coreBounceHeight), 0.0F);
            matrixStack.multiply((new Quaternionf()).setAngleAxis(1.0471976F, SINE_45_DEGREES, 0.0F, SINE_45_DEGREES));
            matrixStack.multiply((new Quaternionf()).setAngleAxis(1.0471976F, SINE_45_DEGREES, 0.0F, SINE_45_DEGREES));
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(j * ChamsConfig.CONFIG.instance().coreRotationSpeed));
            matrixStack.multiply((new Quaternionf()).setAngleAxis(1.0471976F, SINE_45_DEGREES, 0.0F, SINE_45_DEGREES));
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(j * ChamsConfig.CONFIG.instance().coreRotationSpeed));
            matrixStack.scale(ChamsConfig.CONFIG.instance().coreScale, ChamsConfig.CONFIG.instance().coreScale, ChamsConfig.CONFIG.instance().coreScale);
            try {
                Color col = ChamsConfig.CONFIG.instance().coreColor;
                this.core.render(matrixStack, vertexConsumer, light, k, col.getRed() / 255.0F, col.getGreen() / 255.0F, col.getBlue() / 255.0F, ChamsConfig.CONFIG.instance().coreAlpha);
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
            EnderDragonEntityRenderer.renderCrystalBeam(-p, -q + getYOffset(endCrystalEntity, tickDelta, 0F, 0.2F, 1), -r, tickDelta, endCrystalEntity.endCrystalAge, matrixStack, vertexConsumerProvider, light);
        }

        super.render(endCrystalEntity, yaw, tickDelta, matrixStack, vertexConsumerProvider, light);
        ci.cancel();
    }
    @Unique
    private static Color getRainbowCol(int delay, float speed) {
        return getRainbow(((System.currentTimeMillis() + delay) % 10000L / 10000.0f) * speed);
    }

    //https://github.com/Splzh/ClearHitboxes/blob/main/src/main/java/splash/utils/ColorUtils.java !!
    @Unique
    private static Color getRainbow(double percent) {
        double offset = Math.PI * 2 / 3;
        double pos = percent * (Math.PI * 2);
        float red = (float) ((Math.sin(pos) * 127) + 128);
        float green = (float) ((Math.sin(pos + offset) * 127) + 128);
        float blue = (float) ((Math.sin(pos + offset * 2) * 127) + 128);
        return new Color((int) (red), (int) (green), (int) (blue), 255);
    }
}
