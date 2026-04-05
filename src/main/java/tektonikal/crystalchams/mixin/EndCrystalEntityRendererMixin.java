package tektonikal.crystalchams.mixin;

import dev.isxander.yacl3.api.ListOptionEntry;
import dev.isxander.yacl3.impl.ListOptionEntryImpl;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.debug.DebugHudEntries;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.*;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.debug.EntityHitboxDebugRenderer;
import net.minecraft.client.render.entity.*;
import net.minecraft.client.render.entity.model.EndCrystalEntityModel;
import net.minecraft.client.render.entity.state.EndCrystalEntityRenderState;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tektonikal.crystalchams.CrystalChams;
import tektonikal.crystalchams.config.*;
import tektonikal.crystalchams.util.Easings;

import java.awt.*;
import java.util.Map;

import static net.minecraft.client.render.entity.EnderDragonEntityRenderer.CRYSTAL_BEAM_TEXTURE;
import static tektonikal.crystalchams.CrystalChams.*;

@Mixin(EndCrystalEntityRenderer.class)
public abstract class EndCrystalEntityRendererMixin extends EntityRenderer<EndCrystalEntity, EndCrystalEntityRenderState> {

    @Unique
    private static final float PI_DIV_3 = 1.0471976F;
    @Unique
    private static final float STUPID_SCALE = 0.875F;
    @Unique
    private static final float STUPID_SCALE_FACTOR_TWO = 1.53125F;
    @Unique
    private static final float SINE_45_DEGREES = (float)Math.sin((Math.PI / 4D));
    @Mutable
    @Shadow
    @Final
    private static Identifier TEXTURE;


    @Shadow
    @Final
    private EndCrystalEntityModel model;

    @Shadow
    public static float getYOffset(float f) {
        return 0;
    }

    @Unique
    private ModelPart orphanedAndEuthanizedFrame;
    @Unique
    private ModelPart orphanedAndEuthanizedCore;
    @Unique
    private EntityHitboxDebugRenderer entityHitboxDebugRenderer;

    public EndCrystalEntityRendererMixin(EntityRendererFactory.Context context) {
        super(context);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void ough(EntityRendererFactory.Context context, CallbackInfo ci){
        orphanedAndEuthanizedFrame = new ModelPart(((ModelPartAccessor) ((Object) model.outerGlass)).cuboids(), Map.of());
        orphanedAndEuthanizedCore = new ModelPart(((ModelPartAccessor) ((Object) model.cube)).cuboids(), Map.of());
        entityHitboxDebugRenderer = new EntityHitboxDebugRenderer(mc);
    }

    @Inject(method = "render(Lnet/minecraft/client/render/entity/state/EndCrystalEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/state/CameraRenderState;)V", at = @At("HEAD"), cancellable = true)
    private void CC$renderInject(EndCrystalEntityRenderState endCrystalEntityRenderState, MatrixStack matrixStack, OrderedRenderCommandQueue orderedRenderCommandQueue, CameraRenderState cameraRenderState, CallbackInfo ci) {
        if (!ChamsConfig.o_modEnabled.pendingValue()) {
            shadowOpacity = 1F;
            shadowRadius = 0.5F;
            //TODO: shadow color
            return;
        }
//        BlockPos blockPos = endCrystalEntity.getBeamTarget();
        float realAge = endCrystalEntityRenderState.age + MinecraftClient.getInstance().getRenderTickCounter().getDynamicDeltaTicks();
        int overlay = OverlayTexture.DEFAULT_UV;
        shadowOpacity = ChamsConfig.o_shadowAlpha.pendingValue() * 2F;
        shadowRadius = ChamsConfig.o_shadowRadius.pendingValue();
//        updateAnimation(endCrystalEntity, realAge);
        //base
        matrixStack.push();
        matrixStack.push();

        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotation((float) Math.toRadians(ChamsConfig.o_baseRotation.pendingValue())));
        matrixStack.translate(0.0F, ChamsConfig.o_baseOffset.pendingValue(), 0.0F);
        float scale = ChamsConfig.o_baseScale.pendingValue() * 2;
        matrixStack.scale(scale, scale, scale);
        matrixStack.translate(0.0F, -0.5F, 0.0F);
        if ((endCrystalEntityRenderState.baseVisible && ChamsConfig.o_baseRenderMode.pendingValue() == BaseRenderMode.DEFAULT) || ChamsConfig.o_baseRenderMode.pendingValue() == BaseRenderMode.ALWAYS) {
            int col = getColor(ChamsConfig.o_baseColor.pendingValue(), ChamsConfig.o_baseAlpha.pendingValue(), ChamsConfig.o_baseRainbow.pendingValue(), ChamsConfig.o_baseRainbowDelay.pendingValue(), ChamsConfig.o_baseRainbowSpeed.pendingValue(), ChamsConfig.o_baseRainbowSaturation.pendingValue(), ChamsConfig.o_baseRainbowBrightness.pendingValue());
            orderedRenderCommandQueue.submitModelPart(model.base, matrixStack, getLayer(RenderMode.DEFAULT, ChamsConfig.o_beamCulling.pendingValue(), TEXTURE), packLight(ChamsConfig.o_baseBlockLightLevel.pendingValue(), ChamsConfig.o_baseSkyLightLevel.pendingValue(), endCrystalEntityRenderState.light), overlay, null, col, null);
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
                        matrixStack.translate(0.0F, 2F + CrystalChams.getYOffset(realAge, controller.o_offset.pendingValue(), controller.o_bounceSpeed.pendingValue(), controller.o_bounceHeight.pendingValue(), controller.o_delay.pendingValue()), 0.0F);
                        float scaleFac = (float) (controller.o_scale.pendingValue() * 2 * Math.pow(STUPID_SCALE, index));
                        matrixStack.scale(scaleFac, scaleFac, scaleFac);
                        for (int i = 0; i < index + 1; i++) {
                            if (i == 0) {
                                matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(((realAge + controller.o_delay.pendingValue()) % 360) * controller.o_rotationSpeed.pendingValue() * 3));
                                matrixStack.multiply(new Quaternionf().setAngleAxis(PI_DIV_3, SINE_45_DEGREES, 0.0F, SINE_45_DEGREES));
                            } else {
                                matrixStack.multiply(new Quaternionf().setAngleAxis(PI_DIV_3, SINE_45_DEGREES, 0.0F, SINE_45_DEGREES));
                                matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(((realAge + controller.o_delay.pendingValue()) % 360) * controller.o_rotationSpeed.pendingValue() * 3));
                            }
                        }
                    } else {
                        matrixStack.translate(0F, 1F, 0F);
                        float guhhh = controller.o_scale.pendingValue() * 4;
                        matrixStack.scale(guhhh, guhhh, guhhh);
                    }
                    updateAlpha(controller);
//                    float alpha = controller.o_alpha.pendingValue() * (endCrystalEntity.equals(previewCrystalEntity) ? controller.alphaMultiplier : 1);
                    int col = getColor(controller.o_color.pendingValue(), 1, controller.o_rainbow.pendingValue(), controller.o_rainbowDelay.pendingValue(), controller.o_rainbowSpeed.pendingValue(), controller.o_rainbowSaturation.pendingValue(), controller.o_rainbowBrightness.pendingValue());
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
                        orderedRenderCommandQueue.submitModelPart(orphanedAndEuthanizedFrame, matrixStack, getLayer(RenderMode.DEFAULT, controller.o_culling.pendingValue(), TEXTURE), packLight(controller.o_blockLightLevel.pendingValue(), controller.o_skyLightLevel.pendingValue(), endCrystalEntityRenderState.light), overlay, null, col, null);
                    }
                    matrixStack.pop();
                }
            }
        }
//        //core
        if (ChamsConfig.o_renderCore.pendingValue()) {
            matrixStack.push();
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(((realAge + ChamsConfig.o_coreDelay.pendingValue()) * (ChamsConfig.o_coreRotationSpeed.pendingValue() * 3)) % 360));
            matrixStack.translate(0.0F, 2F + CrystalChams.getYOffset(realAge, ChamsConfig.o_coreOffset.pendingValue(), ChamsConfig.o_coreBounceSpeed.pendingValue(), ChamsConfig.o_coreBounceHeight.pendingValue(), ChamsConfig.o_coreDelay.pendingValue()), 0.0F);
            matrixStack.multiply((new Quaternionf()).setAngleAxis(PI_DIV_3, SINE_45_DEGREES, 0.0F, SINE_45_DEGREES));
            matrixStack.multiply((new Quaternionf()).setAngleAxis(PI_DIV_3, SINE_45_DEGREES, 0.0F, SINE_45_DEGREES));
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(((realAge + ChamsConfig.o_coreDelay.pendingValue()) * (ChamsConfig.o_coreRotationSpeed.pendingValue() * 3)) % 360));
            matrixStack.multiply((new Quaternionf()).setAngleAxis(PI_DIV_3, SINE_45_DEGREES, 0.0F, SINE_45_DEGREES));
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(((realAge + ChamsConfig.o_coreDelay.pendingValue()) * (ChamsConfig.o_coreRotationSpeed.pendingValue() * 3)) % 360));
//            float alsoScale = STUPID_SCALE_FACTOR_TWO * (ChamsConfig.o_coreScaleAnimation.pendingValue() ? MathHelper.lerp(ChamsConfig.o_coreScaleEasing.pendingValue().getFunction().apply((double) MathHelper.clamp((endCrystalEntity.age + tickDelta - (ChamsConfig.o_coreScaleDelay.pendingValue() * 20)) / (20 * ChamsConfig.o_coreScaleAnimDuration.pendingValue()), 0, 1)).floatValue(), ChamsConfig.o_coreStartScale.pendingValue(), coreScale) : coreScale);
            float alsoScale = (STUPID_SCALE_FACTOR_TWO * STUPID_SCALE) * ChamsConfig.o_coreScale.pendingValue();
            matrixStack.scale(alsoScale, alsoScale, alsoScale);
            int col = getColor(ChamsConfig.o_coreColor.pendingValue(),
                    getAnimatedValue(ChamsConfig.o_coreAlphaAnimation.pendingValue(), ChamsConfig.o_coreAlphaEasing.pendingValue(), realAge, ChamsConfig.o_coreAlphaDelay.pendingValue(), ChamsConfig.o_coreAlphaAnimDuration.pendingValue(), ChamsConfig.o_coreStartAlpha.pendingValue(), ChamsConfig.o_coreAlpha.pendingValue()),
                    ChamsConfig.o_coreRainbow.pendingValue(), ChamsConfig.o_coreRainbowDelay.pendingValue(), ChamsConfig.o_coreRainbowSpeed.pendingValue(), ChamsConfig.o_coreRainbowSaturation.pendingValue(), ChamsConfig.o_coreRainbowBrightness.pendingValue());
            orderedRenderCommandQueue.submitModelPart(orphanedAndEuthanizedCore, matrixStack, getLayer(RenderMode.DEFAULT, ChamsConfig.o_coreCulling.pendingValue(), TEXTURE), packLight(ChamsConfig.o_coreBlockLightLevel.pendingValue(), ChamsConfig.o_coreSkyLightLevel.pendingValue(), endCrystalEntityRenderState.light), overlay, null, col, null);
            matrixStack.pop();
        }
        matrixStack.pop();
        Vec3d vec3d = endCrystalEntityRenderState.beamOffset;
        if (vec3d != null) {
            float g = (float)vec3d.x;
            float h = (float)vec3d.y;
            float i = (float)vec3d.z;
            matrixStack.translate(vec3d);
            EnderDragonEntityRenderer.renderCrystalBeam(
                    -g, -h + CrystalChams.getYOffset(realAge, ChamsConfig.o_coreOffset.pendingValue(), ChamsConfig.o_coreBounceSpeed.pendingValue(), ChamsConfig.o_coreBounceHeight.pendingValue(), ChamsConfig.o_coreDelay.pendingValue()), -i, endCrystalEntityRenderState.age, matrixStack, orderedRenderCommandQueue, endCrystalEntityRenderState.light
            );
        }
        super.render(endCrystalEntityRenderState, matrixStack, orderedRenderCommandQueue, cameraRenderState);
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

    //MAKE SURE THAT IT'S THE age and not endCrystalAge!!!!!!!!!!!!!!!!!!!!!
    @Unique
    private static float getAnimatedValue(boolean animate, Easings function, float ageAndDelta, float delay, float duration, float startValue, float targetValue) {
        if (animate) {
            return MathHelper.lerp(function.getFunction().apply((double) MathHelper.clamp((ageAndDelta - (delay * 20)) / (20 * duration), 0, 1)).floatValue(), startValue, targetValue);
        } else {
            return targetValue;
        }
    }


    @Unique
    private static int getColor(Color col, float alpha) {
        return ColorHelper.getArgb((int) (alpha * 255.0F), col.getRed(), col.getGreen(), col.getBlue());
    }

    @Unique
    private static int getColor(Color col, float alpha, boolean rainbow, float delay, float speed, float saturation, float brightness) {
        if (!rainbow) {
            return getColor(col, alpha);
        } else {
            return getColor(new Color(getRainbow(delay, speed, saturation, brightness)), alpha);
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
