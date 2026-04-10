package tektonikal.crystalchams.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.isxander.yacl3.api.ListOptionEntry;
import dev.isxander.yacl3.impl.ListOptionEntryImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.object.crystal.EndCrystalModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.debug.EntityHitboxDebugRenderer;
import net.minecraft.client.renderer.entity.EndCrystalRenderer;
import net.minecraft.client.renderer.entity.EnderDragonRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EndCrystalRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tektonikal.crystalchams.CrystalChams;
import tektonikal.crystalchams.config.ChamsConfig;
import tektonikal.crystalchams.config.ModelPartController;
import tektonikal.crystalchams.config.ModelPartOptions;
import tektonikal.crystalchams.config.RenderMode;
import tektonikal.crystalchams.util.Easings;

import java.awt.*;
import java.util.Map;

import static tektonikal.crystalchams.CrystalChams.*;

@Mixin(EndCrystalRenderer.class)
public abstract class EndCrystalRendererMixin extends EntityRenderer<EndCrystal, EndCrystalRenderState> {

    @Unique
    private static final float PI_DIV_3 = 1.0471976F;
    @Unique
    private static final float STUPID_SCALE = 0.875F;
    @Unique
    private static final float STUPID_SCALE_FACTOR_TWO = 1.53125F;
    @Unique
    private static final float SINE_45_DEGREES = (float) Math.sin((Math.PI / 4D));
    @Mutable
    @Shadow
    @Final
    private static Identifier END_CRYSTAL_LOCATION;


    @Shadow
    @Final
    private EndCrystalModel model;
    @Unique
    private ModelPart orphanedAndEuthanizedFrame;
    @Unique
    private ModelPart orphanedAndEuthanizedCore;
    @Unique
    private EntityHitboxDebugRenderer entityHitboxDebugRenderer;
    public EndCrystalRendererMixin(EntityRendererProvider.Context context) {
        super(context);
    }

    @Shadow
    public static float getY(float f) {
        return 0;
    }

    //MAKE SURE THAT IT'S THE age and not endCrystalAge!!!!!!!!!!!!!!!!!!!!!
    @Unique
    private static float getAnimatedValue(boolean animate, Easings function, float ageAndDelta, float delay, float duration, float startValue, float targetValue) {
        if (animate) {
            return Mth.lerp(function.getFunction().apply((double) Mth.clamp((ageAndDelta - (delay * 20)) / (20 * duration), 0, 1)).floatValue(), startValue, targetValue);
        } else {
            return targetValue;
        }
    }

    @Unique
    private static int getColor(Color col, float alpha) {
        return ARGB.color((int) (alpha * 255.0F), col.getRed(), col.getGreen(), col.getBlue());
    }

    @Unique
    private static int getColor(Color col, float alpha, boolean rainbow, float delay, float speed, float saturation, float brightness) {
        if (!rainbow) {
            return getColor(col, alpha);
        } else {
            return getColor(new Color(getRainbow(delay, speed, saturation, brightness)), alpha);
        }
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void ough(EntityRendererProvider.Context context, CallbackInfo ci) {
        orphanedAndEuthanizedFrame = new ModelPart(((ModelPartAccessor) ((Object) model.outerGlass)).Cubes(), Map.of());
        orphanedAndEuthanizedCore = new ModelPart(((ModelPartAccessor) ((Object) model.cube)).Cubes(), Map.of());
        entityHitboxDebugRenderer = new EntityHitboxDebugRenderer(mc);
    }

    @Inject(method = "submit(Lnet/minecraft/client/renderer/entity/state/EndCrystalRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V", at = @At("HEAD"), cancellable = true)
    private void CC$renderInject(EndCrystalRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera, CallbackInfo ci) {
        if (!ChamsConfig.o_modEnabled.pendingValue()) {
            shadowStrength = 1F;
            shadowRadius = 0.5F;
            //TODO: shadow color
            return;
        }
//        BlockPos blockPos = endCrystalEntity.getBeamTarget();
        float realAge = state.ageInTicks + Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaTicks();
        int overlay = OverlayTexture.NO_OVERLAY;
        shadowStrength = ChamsConfig.o_shadowAlpha.pendingValue() * 2F;
        shadowRadius = ChamsConfig.o_shadowRadius.pendingValue();
//        updateAnimation(endCrystalEntity, realAge);
        //base
        poseStack.pushPose();
        poseStack.pushPose();

        poseStack.mulPose(Axis.YP.rotation((float) Math.toRadians(ChamsConfig.o_baseRotation.pendingValue())));
        poseStack.translate(0.0F, ChamsConfig.o_baseOffset.pendingValue(), 0.0F);
        float scale = ChamsConfig.o_baseScale.pendingValue() * 2;
        poseStack.scale(scale, scale, scale);
        poseStack.translate(0.0F, -0.5F, 0.0F);
        if ((state.showsBottom && ChamsConfig.o_baseRenderMode.pendingValue() == BaseRenderMode.DEFAULT) || ChamsConfig.o_baseRenderMode.pendingValue() == BaseRenderMode.ALWAYS) {
            int col = getColor(ChamsConfig.o_baseColor.pendingValue(), ChamsConfig.o_baseAlpha.pendingValue(), ChamsConfig.o_baseRainbow.pendingValue(), ChamsConfig.o_baseRainbowDelay.pendingValue(), ChamsConfig.o_baseRainbowSpeed.pendingValue(), ChamsConfig.o_baseRainbowSaturation.pendingValue(), ChamsConfig.o_baseRainbowBrightness.pendingValue());
            submitNodeCollector.submitModelPart(model.base, poseStack, getLayer(RenderMode.DEFAULT, ChamsConfig.o_beamCulling.pendingValue(), END_CRYSTAL_LOCATION), packLight(ChamsConfig.o_baseBlockLightLevel.pendingValue(), ChamsConfig.o_baseSkyLightLevel.pendingValue(), state.lightCoords), overlay, null, col, null);
        }
        poseStack.popPose();
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
                    poseStack.pushPose();
                    int index = ChamsConfig.o_frameList.indexOf(entry);
                    if (!controller.o_funnyOption.pendingValue()) {
                        poseStack.translate(0.0F, 2F + CrystalChams.getYOffset(realAge, controller.o_offset.pendingValue(), controller.o_bounceSpeed.pendingValue(), controller.o_bounceHeight.pendingValue(), controller.o_delay.pendingValue()), 0.0F);
                        float scaleFac = (float) (controller.o_scale.pendingValue() * 2 * Math.pow(STUPID_SCALE, index));
                        poseStack.scale(scaleFac, scaleFac, scaleFac);
                        for (int i = 0; i < index + 1; i++) {
                            if (i == 0) {
                                poseStack.mulPose(Axis.YP.rotationDegrees(((realAge + controller.o_delay.pendingValue()) % 360) * controller.o_rotationSpeed.pendingValue() * 3));
                                poseStack.mulPose(new Quaternionf().setAngleAxis(PI_DIV_3, SINE_45_DEGREES, 0.0F, SINE_45_DEGREES));
                            } else {
                                poseStack.mulPose(new Quaternionf().setAngleAxis(PI_DIV_3, SINE_45_DEGREES, 0.0F, SINE_45_DEGREES));
                                poseStack.mulPose(Axis.YP.rotationDegrees(((realAge + controller.o_delay.pendingValue()) % 360) * controller.o_rotationSpeed.pendingValue() * 3));
                            }
                        }
                    } else {
                        poseStack.translate(0F, 1F, 0F);
                        float guhhh = controller.o_scale.pendingValue() * 4;
                        poseStack.scale(guhhh, guhhh, guhhh);
                    }
                    updateAlpha(controller);
//                    float alpha = controller.o_alpha.pendingValue() * (endCrystalEntity.equals(previewCrystalEntity) ? controller.alphaMultiplier : 1);
                    int col = getColor(controller.o_color.pendingValue(), controller.o_alpha.pendingValue(), controller.o_rainbow.pendingValue(), controller.o_rainbowDelay.pendingValue(), controller.o_rainbowSpeed.pendingValue(), controller.o_rainbowSaturation.pendingValue(), controller.o_rainbowBrightness.pendingValue());
                    if (controller.o_funnierOption.pendingValue()) {
                        //TODO
//                        renderFunny(endCrystalEntity, yaw, tickDelta, matrixStack, getLayer(vertexConsumerProvider, controller.o_renderLayer.pendingValue(), controller.o_culling.pendingValue(), TEXTURE), getLight(light, controller.o_lightLevel.pendingValue()), col, vertexConsumerProvider, pos);
//                        if (blockPos != null) {
//                            float m = (float) blockPos.getX() + 0.5F;
//                            float n = (float) blockPos.getY() + 0.5F;
//                            float o = (float) blockPos.getZ() + 0.5F;
//                            float p = (float) ((double) m - endCrystalEntity.getX());
//                            float q = (float) ((double) n - endCrystalEntity.getY());
//                            float r = (float) ((double) o - endCrystalEntity.getZ());
//                            matrixStack.translate(p + 2, q + 2, r + 2);
//                            EnderDragonEntityRenderer.renderCrystalBeam(-p, -q + CrystalChams.getYOffset(realAge, ChamsConfig.o_coreOffset.pendingValue(), ChamsConfig.o_coreBounceSpeed.pendingValue(), ChamsConfig.o_coreBounceHeight.pendingValue(), ChamsConfig.o_coreDelay.pendingValue()), -r, tickDelta, endCrystalEntity.endCrystalAge, matrixStack, vertexConsumerProvider, light);
//                        }
                    } else {
                        submitNodeCollector.submitModelPart(orphanedAndEuthanizedFrame, poseStack, getLayer(RenderMode.DEFAULT, controller.o_culling.pendingValue(), END_CRYSTAL_LOCATION), packLight(controller.o_blockLightLevel.pendingValue(), controller.o_skyLightLevel.pendingValue(), state.lightCoords), overlay, null, col, null);
                    }
                    poseStack.popPose();
                }
            }
        }
//        //core
        if (ChamsConfig.o_renderCore.pendingValue()) {
            poseStack.pushPose();
            poseStack.mulPose(Axis.YP.rotationDegrees(((realAge + ChamsConfig.o_coreDelay.pendingValue()) * (ChamsConfig.o_coreRotationSpeed.pendingValue() * 3)) % 360));
            poseStack.translate(0.0F, 2F + CrystalChams.getYOffset(realAge, ChamsConfig.o_coreOffset.pendingValue(), ChamsConfig.o_coreBounceSpeed.pendingValue(), ChamsConfig.o_coreBounceHeight.pendingValue(), ChamsConfig.o_coreDelay.pendingValue()), 0.0F);
            poseStack.mulPose((new Quaternionf()).setAngleAxis(PI_DIV_3, SINE_45_DEGREES, 0.0F, SINE_45_DEGREES));
            poseStack.mulPose((new Quaternionf()).setAngleAxis(PI_DIV_3, SINE_45_DEGREES, 0.0F, SINE_45_DEGREES));
            poseStack.mulPose(Axis.YP.rotationDegrees(((realAge + ChamsConfig.o_coreDelay.pendingValue()) * (ChamsConfig.o_coreRotationSpeed.pendingValue() * 3)) % 360));
            poseStack.mulPose((new Quaternionf()).setAngleAxis(PI_DIV_3, SINE_45_DEGREES, 0.0F, SINE_45_DEGREES));
            poseStack.mulPose(Axis.YP.rotationDegrees(((realAge + ChamsConfig.o_coreDelay.pendingValue()) * (ChamsConfig.o_coreRotationSpeed.pendingValue() * 3)) % 360));
//            float alsoScale = STUPID_SCALE_FACTOR_TWO * (ChamsConfig.o_coreScaleAnimation.pendingValue() ? MathHelper.lerp(ChamsConfig.o_coreScaleEasing.pendingValue().getFunction().apply((double) MathHelper.clamp((endCrystalEntity.age + tickDelta - (ChamsConfig.o_coreScaleDelay.pendingValue() * 20)) / (20 * ChamsConfig.o_coreScaleAnimDuration.pendingValue()), 0, 1)).floatValue(), ChamsConfig.o_coreStartScale.pendingValue(), coreScale) : coreScale);
            float alsoScale = (STUPID_SCALE_FACTOR_TWO * STUPID_SCALE) * ChamsConfig.o_coreScale.pendingValue();
            poseStack.scale(alsoScale, alsoScale, alsoScale);
            int col = getColor(ChamsConfig.o_coreColor.pendingValue(),
                    getAnimatedValue(ChamsConfig.o_coreAlphaAnimation.pendingValue(), ChamsConfig.o_coreAlphaEasing.pendingValue(), realAge, ChamsConfig.o_coreAlphaDelay.pendingValue(), ChamsConfig.o_coreAlphaAnimDuration.pendingValue(), ChamsConfig.o_coreStartAlpha.pendingValue(), ChamsConfig.o_coreAlpha.pendingValue()),
                    ChamsConfig.o_coreRainbow.pendingValue(), ChamsConfig.o_coreRainbowDelay.pendingValue(), ChamsConfig.o_coreRainbowSpeed.pendingValue(), ChamsConfig.o_coreRainbowSaturation.pendingValue(), ChamsConfig.o_coreRainbowBrightness.pendingValue());
            submitNodeCollector.submitModelPart(orphanedAndEuthanizedCore, poseStack, getLayer(RenderMode.DEFAULT, ChamsConfig.o_coreCulling.pendingValue(), END_CRYSTAL_LOCATION), packLight(ChamsConfig.o_coreBlockLightLevel.pendingValue(), ChamsConfig.o_coreSkyLightLevel.pendingValue(), state.lightCoords), overlay, null, col, null);
            poseStack.popPose();
        }
        poseStack.popPose();
        Vec3 vec3d = state.beamOffset;
        if (vec3d != null) {
            float g = (float) vec3d.x;
            float h = (float) vec3d.y;
            float i = (float) vec3d.z;
            poseStack.translate(vec3d);
            EnderDragonRenderer.submitCrystalBeams(
                    -g, -h + CrystalChams.getYOffset(realAge, ChamsConfig.o_coreOffset.pendingValue(), ChamsConfig.o_coreBounceSpeed.pendingValue(), ChamsConfig.o_coreBounceHeight.pendingValue(), ChamsConfig.o_coreDelay.pendingValue()), -i, state.ageInTicks, poseStack, submitNodeCollector, state.lightCoords
            );
        }
        super.submit(state, poseStack, submitNodeCollector, camera);
        ci.cancel();
    }

    @Unique
    private void updateAlpha(ModelPartController control) {
        if (control.hovered || hoveredIndex == -1) {
            control.alphaMultiplier = (float) ease(control.alphaMultiplier, 1, 5);
        } else {
            control.alphaMultiplier = (float) ease(control.alphaMultiplier, 0.25, 5);
        }
    }

//    @Unique
//    public void renderFunny(EndCrystalEntity endCrystalEntity, float f, float g, MatrixStack matrixStack, VertexConsumer layer, int i, int color, VertexConsumerProvider vertexConsumerProvider, BlockPos pos) {
//        matrixStack.push();
//        float h = EndCrystalEntityRenderer.getYOffset(endCrystalEntity, g);
//        float j = (endCrystalEntity.endCrystalAge + g) * 3.0F;
//        matrixStack.push();
//        matrixStack.scale(2.0F, 2.0F, 2.0F);
//        matrixStack.translate(0.0F, -0.5F, 0.0F);
//        int k = OverlayTexture.DEFAULT_UV;
//        if (endCrystalEntity.shouldShowBottom()) {
//            this.bottom.render(matrixStack, layer, i, k, color);
//        }
//        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(j));
//        matrixStack.translate(0.0F, 1.5F + h / 2.0F, 0.0F);
//        matrixStack.multiply(new Quaternionf().setAngleAxis((float) (Math.PI / 3), SINE_45_DEGREES, 0.0F, SINE_45_DEGREES));
//        this.frame.render(matrixStack, layer, i, k, color);
//        matrixStack.scale(STUPID_SCALE, STUPID_SCALE, STUPID_SCALE);
//        matrixStack.multiply(new Quaternionf().setAngleAxis((float) (Math.PI / 3), SINE_45_DEGREES, 0.0F, SINE_45_DEGREES));
//        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(j));
//        this.frame.render(matrixStack, layer, i, k, color);
//        matrixStack.scale(STUPID_SCALE, STUPID_SCALE, STUPID_SCALE);
//        matrixStack.multiply(new Quaternionf().setAngleAxis((float) (Math.PI / 3), SINE_45_DEGREES, 0.0F, SINE_45_DEGREES));
//        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(j));
//        this.core.render(matrixStack, layer, i, k, color);
//        matrixStack.pop();
//        matrixStack.pop();
////        if (pos != null) {
//////            float m = pos.getX() + 0.5F;
//////            float n = pos.getY() + 0.5F;
//////            float o = pos.getZ() + 0.5F;
//////            float p = (float)(m - endCrystalEntity.getX());
//////            float q = (float)(n - endCrystalEntity.getY());
//////            float r = (float)(o - endCrystalEntity.getZ());
//////            matrixStack.translate(p, q, r);
//////            renderCustomBeam(-p, -q + h, -r, g, endCrystalEntity.endCrystalAge, matrixStack, vertexConsumerProvider, i, 2);
//////            this.core.render(matrixStack, layer, i, k, color);
////            bottom.render(matrixStack, layer, i, k, color);
////        }
////        else{
////            frame.render(matrixStack, layer, i, k, color);
////        }
//    }
}
