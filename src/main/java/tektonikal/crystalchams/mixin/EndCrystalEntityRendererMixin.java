package tektonikal.crystalchams.mixin;

import dev.isxander.yacl3.api.Controller;
import dev.isxander.yacl3.api.ListOptionEntry;
import dev.isxander.yacl3.impl.ListOptionEntryImpl;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.*;
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
import tektonikal.crystalchams.config.ModelPartController;
import tektonikal.crystalchams.config.ModelPartOptions;

import java.awt.*;

import static tektonikal.crystalchams.CrystalChams.*;

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
    @Unique
    private static float baseScale = ChamsConfig.CONFIG.instance().baseScale;
    @Unique
    private static float coreScale = ChamsConfig.CONFIG.instance().coreScale;

    public EndCrystalEntityRendererMixin(EntityRendererFactory.Context context) {
        super(context);
    }

    @Inject(method = "render(Lnet/minecraft/entity/decoration/EndCrystalEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("HEAD"), cancellable = true)
    private void CC$renderInject(EndCrystalEntity endCrystalEntity, float yaw, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, CallbackInfo ci) {
        float realAge = ((endCrystalEntity.endCrystalAge) + tickDelta);
        updateAnimation(endCrystalEntity, realAge);
        if (!ChamsConfig.o_modEnabled.pendingValue()) {
            shadowOpacity = 1F;
            shadowRadius = 0.5F;
            return;
        }
        shadowOpacity = ChamsConfig.o_shadowAlpha.pendingValue() * 2F;
        shadowRadius = ChamsConfig.o_shadowRadius.pendingValue();
        //base
        matrixStack.push();
        matrixStack.push();
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotation((float) (Math.PI / 180.0) * ChamsConfig.o_baseRotation.pendingValue()));
        //translate before scaling because fuck you
        matrixStack.translate(0.0F, ChamsConfig.o_baseOffset.pendingValue() - 1, 0.0F);
        matrixStack.scale(baseScale * 2, baseScale * 2, baseScale * 2);
        int overlay = OverlayTexture.DEFAULT_UV;
        if ((endCrystalEntity.shouldShowBottom() && ChamsConfig.o_baseRenderMode.pendingValue() == ChamsConfig.BaseRenderMode.DEFAULT) || ChamsConfig.o_baseRenderMode.pendingValue() == ChamsConfig.BaseRenderMode.ALWAYS) {
            if (ChamsConfig.o_baseRainbow.pendingValue()) {
                Color col = new Color(CrystalChams.getRainbow(ChamsConfig.o_baseRainbowDelay.pendingValue(), ChamsConfig.o_baseRainbowSpeed.pendingValue(), ChamsConfig.o_baseRainbowSaturation.pendingValue(), ChamsConfig.o_baseRainbowBrightness.pendingValue()));
                this.bottom.renderWithoutChildren(matrixStack, getLayer(vertexConsumerProvider, ChamsConfig.o_baseRenderLayer.pendingValue(), ChamsConfig.o_baseCulling.pendingValue(), TEXTURE), ChamsConfig.o_baseLightLevel.pendingValue() != -1 ? ChamsConfig.o_baseLightLevel.pendingValue() : light, overlay, getColor(col, ChamsConfig.o_baseAlpha.pendingValue()));
            } else {
                this.bottom.renderWithoutChildren(matrixStack, getLayer(vertexConsumerProvider, ChamsConfig.o_baseRenderLayer.pendingValue(), ChamsConfig.o_baseCulling.pendingValue(), TEXTURE), ChamsConfig.o_baseLightLevel.pendingValue() != -1 ? ChamsConfig.o_baseLightLevel.pendingValue() : light, overlay, getColor(ChamsConfig.o_baseColor.pendingValue(), ChamsConfig.o_baseAlpha.pendingValue()));
            }
        }
        matrixStack.pop();
        //Frames
        if (ChamsConfig.o_renderFrames.pendingValue()) {
            for (ListOptionEntry<ModelPartOptions> modelPartOptionsListOptionEntry : ChamsConfig.o_frameList.options()) {
                ModelPartController controller = (ModelPartController) ((ListOptionEntryImpl.EntryController) (modelPartOptionsListOptionEntry.controller())).controller();
                if (controller.hovered) {
                    hoveredIndex = ChamsConfig.o_frameList.indexOf(modelPartOptionsListOptionEntry);
                    break;
                }
                hoveredIndex = -1;
            }
            for (ListOptionEntry<ModelPartOptions> entry : ChamsConfig.o_frameList.options()) {
                //THIS IS SO BAD KILLING MYSELF
                ModelPartController controller = (ModelPartController) ((ListOptionEntryImpl.EntryController) (entry.controller())).controller();

                if (controller.o_render.pendingValue()) {
                    matrixStack.push();
                    int index = ChamsConfig.o_frameList.indexOf(entry);
                    if (!controller.o_funnyOption.pendingValue()) {
                        matrixStack.translate(0.0F, 2F + CrystalChams.getYOffset(endCrystalEntity.endCrystalAge + tickDelta, controller.o_offset.pendingValue(), controller.o_bounceSpeed.pendingValue(), controller.o_bounceHeight.pendingValue(), controller.o_tickDelay.pendingValue()), 0.0F);
                        matrixStack.scale((float) (controller.o_scale.pendingValue() * 2 * Math.pow(0.875, index)), (float) (controller.o_scale.pendingValue() * 2 * Math.pow(0.875, index)), (float) (controller.o_scale.pendingValue() * 2 * Math.pow(0.875, index)));
                        for (int i = 0; i < index + 1; i++) {
                            if (i == 0) {
                                matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(((realAge + controller.o_tickDelay.pendingValue()) % 360) * controller.o_rotationSpeed.pendingValue() * 3));
                                matrixStack.multiply(new Quaternionf().setAngleAxis((float) (Math.PI / 3), SINE_45_DEGREES, 0.0F, SINE_45_DEGREES));
                            } else {
                                matrixStack.multiply(new Quaternionf().setAngleAxis((float) (Math.PI / 3), SINE_45_DEGREES, 0.0F, SINE_45_DEGREES));
                                matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(((realAge + controller.o_tickDelay.pendingValue()) % 360) * controller.o_rotationSpeed.pendingValue() * 3));
                            }
                        }
                    } else {
                        matrixStack.translate(0F, 1F, 0F);
                        matrixStack.scale(4, 4, 4);
                    }
                    this.frame.renderWithoutChildren(matrixStack, getLayer(vertexConsumerProvider, controller.o_renderLayer.pendingValue(), controller.o_culling.pendingValue(), TEXTURE), controller.o_lightLevel.pendingValue() != -1 ? controller.o_lightLevel.pendingValue() : light, overlay, getColor(controller.o_color.pendingValue(), controller.o_alpha.pendingValue() * controller.alphaMultiplier));
                    matrixStack.pop();
                }
            }
        }
        //TODO: delete this, it's only for reference
//        //frame 1
//        if (ChamsConfig.o_renderFrame1) {
//            matrixStack.push();
//            if(!ChamsConfig.o_funnyOption){
//            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(((j + ChamsConfig.o_frame1TickDelay) % 360) * (ChamsConfig.o_frame1RotationSpeed * 3)));
//            matrixStack.translate(0.0F, 2F + CrystalChams.getYOffset(endCrystalEntity.endCrystalAge + tickDelta, frame1Offset, ChamsConfig.o_frame1BounceSpeed, frame1BounceHeight, frame1TickDelay), 0.0F);
//            matrixStack.multiply((new Quaternionf()).setAngleAxis(1.0471976F, SINE_45_DEGREES, 0.0F, SINE_45_DEGREES));
//            matrixStack.scale(frame1Scale * 2, frame1Scale * 2, frame1Scale * 2);
//            }
//            else{
//                matrixStack.translate(0.05F, 1F, 0.0F);
//                matrixStack.scale(4, 4,  4);
//            }
//            Color col = new Color(frame1Colors[0] / 255.0F, frame1Colors[1] / 255.0F, frame1Colors[2] / 255.0F);
//            this.frame.renderWithoutChildren(matrixStack, getLayer(vertexConsumerProvider, ChamsConfig.o_frame1RenderLayer, ChamsConfig.o_frame1Culling, TEXTURE), ChamsConfig.o_frame1LightLevel != -1 ? (int) frame1LightLevel : light, overlay, getColor(col, frame1Alpha));
//            matrixStack.pop();
//        }
        //core
        if (ChamsConfig.o_renderCore.pendingValue()) {
            matrixStack.push();
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(((realAge + ChamsConfig.o_coreTickDelay.pendingValue()) * (ChamsConfig.o_coreRotationSpeed.pendingValue() * 3)) % 360));
            matrixStack.translate(0.0F, 2F + CrystalChams.getYOffset(endCrystalEntity.endCrystalAge + tickDelta, ChamsConfig.o_coreOffset.pendingValue(), ChamsConfig.o_coreBounceSpeed.pendingValue(), ChamsConfig.o_coreBounceHeight.pendingValue(), ChamsConfig.o_coreTickDelay.pendingValue()), 0.0F);
            matrixStack.multiply((new Quaternionf()).setAngleAxis(1.0471976F, SINE_45_DEGREES, 0.0F, SINE_45_DEGREES));
            matrixStack.multiply((new Quaternionf()).setAngleAxis(1.0471976F, SINE_45_DEGREES, 0.0F, SINE_45_DEGREES));
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(((realAge + ChamsConfig.o_coreTickDelay.pendingValue()) * (ChamsConfig.o_coreRotationSpeed.pendingValue() * 3)) % 360));
            matrixStack.multiply((new Quaternionf()).setAngleAxis(1.0471976F, SINE_45_DEGREES, 0.0F, SINE_45_DEGREES));
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(((realAge + ChamsConfig.o_coreTickDelay.pendingValue()) * (ChamsConfig.o_coreRotationSpeed.pendingValue() * 3)) % 360));
            float v = ChamsConfig.o_coreScaleAnimation.pendingValue() ? MathHelper.lerp(ChamsConfig.o_coreScaleEasing.pendingValue().getFunction().apply((double) MathHelper.clamp((endCrystalEntity.age + tickDelta - (ChamsConfig.o_coreScaleDelay.pendingValue() * 20)) / (20 * ChamsConfig.o_coreScaleAnimDuration.pendingValue()), 0, 1)).floatValue(), ChamsConfig.o_coreStartScale.pendingValue(), coreScale) : coreScale;
            matrixStack.scale(v * 1.53125F, v * 1.53125F, v * 1.53125F);
            Color col = new Color(CrystalChams.getRainbow(ChamsConfig.o_coreRainbowDelay.pendingValue(), ChamsConfig.o_coreRainbowSpeed.pendingValue(), ChamsConfig.o_coreRainbowSaturation.pendingValue(), ChamsConfig.o_coreRainbowBrightness.pendingValue()));
            this.core.renderWithoutChildren(matrixStack, getLayer(vertexConsumerProvider, ChamsConfig.o_coreRenderLayer.pendingValue(), ChamsConfig.o_coreCulling.pendingValue(), TEXTURE), ChamsConfig.o_coreLightLevel.pendingValue() != -1 ? ChamsConfig.o_coreLightLevel.pendingValue() : light, overlay, getColor(
                    ChamsConfig.o_coreRainbow.pendingValue() ? col : ChamsConfig.o_coreColor.pendingValue(),
                    ChamsConfig.o_coreAlphaAnimation.pendingValue() ? MathHelper.lerp(ChamsConfig.o_coreAlphaEasing.pendingValue().getFunction().apply((double) MathHelper.clamp((endCrystalEntity.age + tickDelta - (ChamsConfig.o_coreAlphaDelay.pendingValue() * 20)) / (20 * ChamsConfig.o_coreAlphaAnimDuration.pendingValue()), 0, 1)).floatValue(), ChamsConfig.o_coreStartOpacity.pendingValue(), ChamsConfig.o_coreAlpha.pendingValue()) : ChamsConfig.o_coreAlpha.pendingValue()));
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
            EnderDragonEntityRenderer.renderCrystalBeam(-p, -q + CrystalChams.getYOffset(endCrystalEntity.endCrystalAge + tickDelta, ChamsConfig.o_coreOffset.pendingValue(), ChamsConfig.o_coreBounceSpeed.pendingValue(), ChamsConfig.o_coreBounceHeight.pendingValue(), ChamsConfig.o_coreTickDelay.pendingValue()), -r, tickDelta, endCrystalEntity.endCrystalAge, matrixStack, vertexConsumerProvider, light);
        }
        super.render(endCrystalEntity, yaw, tickDelta, matrixStack, vertexConsumerProvider, light);
        if (ChamsConfig.o_renderHitbox.pendingValue() && !MinecraftClient.getInstance().getEntityRenderDispatcher().shouldRenderHitboxes()) {
            //potentially add custom line renderer layer to here
            EntityRenderDispatcher.renderHitbox(matrixStack, vertexConsumerProvider.getBuffer(RenderLayer.getLines()), endCrystalEntity, tickDelta, 1, 1, 1);
        }
        ci.cancel();
    }

    @Unique
    private static void updateAnimation(EndCrystalEntity entity, float tickAndDelta) {
//        EndCrystalEntityMixinInterface e = ((EndCrystalEntityMixinInterface) entity);
        //this is a horrible way of doing it, but i am not finding a better solution. go fuck yourself
        baseScale = (float) ease(baseScale, ChamsConfig.o_baseScale.pendingValue(), PREVIEW_EASING_SPEED);
        coreScale = (float) ease(coreScale, ChamsConfig.o_coreScale.pendingValue(), PREVIEW_EASING_SPEED);
    }

    @Unique
    private static int getColor(Color col, float alpha) {
        return ColorHelper.Argb.getArgb((int) (alpha * 255.0F), col.getRed(), col.getGreen(), col.getBlue());
    }

}
