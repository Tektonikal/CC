package tektonikal.crystalchams.mixin;

import dev.isxander.yacl3.api.ListOptionEntry;
import dev.isxander.yacl3.impl.ListOptionEntryImpl;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tektonikal.crystalchams.CrystalChams;
import tektonikal.crystalchams.config.ChamsConfig;
import tektonikal.crystalchams.config.ModelPartController;
import tektonikal.crystalchams.config.ModelPartOptions;
import tektonikal.crystalchams.config.ValueAnimator;
import tektonikal.crystalchams.util.Easings;

import java.awt.*;

import static tektonikal.crystalchams.CrystalChams.*;

@Mixin(EndCrystalEntityRenderer.class)
public abstract class EndCrystalEntityRendererMixin extends EntityRenderer<EndCrystalEntity> {

    @Unique
    private static final float PI_DIV_3 = 1.0471976F;
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
    @Unique
    private static final ValueAnimator coreVerticalOffsetAnimator = new ValueAnimator(() -> ChamsConfig.o_coreOffset.pendingValue());

    public EndCrystalEntityRendererMixin(EntityRendererFactory.Context context) {
        super(context);
    }

    @Inject(method = "render(Lnet/minecraft/entity/decoration/EndCrystalEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("HEAD"), cancellable = true)
    private void CC$renderInject(EndCrystalEntity endCrystalEntity, float yaw, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, CallbackInfo ci) {
        if (!ChamsConfig.o_modEnabled.pendingValue()) {
            shadowOpacity = 1F;
            shadowRadius = 0.5F;
            return;
        }
        float realAge = endCrystalEntity.endCrystalAge + tickDelta;
        int overlay = OverlayTexture.DEFAULT_UV;
        shadowOpacity = ChamsConfig.o_shadowAlpha.pendingValue() * 2F;
        shadowRadius = ChamsConfig.o_shadowRadius.pendingValue();
        updateAnimation(endCrystalEntity, realAge);
        //base
        matrixStack.push();
        matrixStack.push();

        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotation((float) Math.toRadians(ChamsConfig.o_baseRotation.pendingValue())));
        //translate before scaling because fuck you
        matrixStack.translate(0.0F, ChamsConfig.o_baseOffset.pendingValue() - 1, 0.0F);
        matrixStack.scale(baseScale * 2, baseScale * 2, baseScale * 2);
        if ((endCrystalEntity.shouldShowBottom() && ChamsConfig.o_baseRenderMode.pendingValue() == ChamsConfig.BaseRenderMode.DEFAULT) || ChamsConfig.o_baseRenderMode.pendingValue() == ChamsConfig.BaseRenderMode.ALWAYS) {
            int col = getColor(ChamsConfig.o_baseColor.pendingValue(), ChamsConfig.o_baseAlpha.pendingValue(), ChamsConfig.o_baseRainbow.pendingValue(), ChamsConfig.o_baseRainbowDelay.pendingValue(), ChamsConfig.o_baseRainbowSpeed.pendingValue(), ChamsConfig.o_baseRainbowSaturation.pendingValue(), ChamsConfig.o_baseRainbowBrightness.pendingValue());
            bottom.renderWithoutChildren(matrixStack, getLayer(vertexConsumerProvider, ChamsConfig.o_baseRenderLayer.pendingValue(), ChamsConfig.o_baseCulling.pendingValue(), TEXTURE), getLight(light, ChamsConfig.o_baseLightLevel.pendingValue()), overlay, col);
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
                        matrixStack.translate(0.0F, 2F + CrystalChams.getYOffset(realAge, controller.o_offset.pendingValue(), controller.o_bounceSpeed.pendingValue(), controller.o_bounceHeight.pendingValue(), controller.o_tickDelay.pendingValue()), 0.0F);
                        float scaleFac = (float) (controller.o_scale.pendingValue() * 2 * Math.pow(0.875, index));
                        matrixStack.scale(scaleFac, scaleFac, scaleFac);
                        for (int i = 0; i < index + 1; i++) {
                            if (i == 0) {
                                matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(((realAge + controller.o_tickDelay.pendingValue()) % 360) * controller.o_rotationSpeed.pendingValue() * 3));
                                matrixStack.multiply(new Quaternionf().setAngleAxis(PI_DIV_3, SINE_45_DEGREES, 0.0F, SINE_45_DEGREES));
                            } else {
                                matrixStack.multiply(new Quaternionf().setAngleAxis(PI_DIV_3, SINE_45_DEGREES, 0.0F, SINE_45_DEGREES));
                                matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(((realAge + controller.o_tickDelay.pendingValue()) % 360) * controller.o_rotationSpeed.pendingValue() * 3));
                            }
                        }
                    } else {
                        matrixStack.translate(0F, 1F, 0F);
                        matrixStack.scale(4, 4, 4);
                    }
                    int col = getColor(controller.o_color.pendingValue(), controller.o_alpha.pendingValue() * controller.alphaMultiplier, controller.o_rainbow.pendingValue(), controller.o_rainbowDelay.pendingValue(), controller.o_rainbowSpeed.pendingValue(), controller.o_rainbowSaturation.pendingValue(), controller.o_rainbowBrightness.pendingValue());
                    frame.renderWithoutChildren(matrixStack, getLayer(vertexConsumerProvider, controller.o_renderLayer.pendingValue(), controller.o_culling.pendingValue(), TEXTURE), getLight(light, controller.o_lightLevel.pendingValue()), overlay, col);
                    matrixStack.pop();
                }
            }
        }
        //core
        if (ChamsConfig.o_renderCore.pendingValue()) {
            matrixStack.push();
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(((realAge + ChamsConfig.o_coreDelay.pendingValue()) * (ChamsConfig.o_coreRotationSpeed.pendingValue() * 3)) % 360));
            matrixStack.translate(0.0F, 2F + CrystalChams.getYOffset(realAge, ChamsConfig.o_coreOffset.pendingValue(), ChamsConfig.o_coreBounceSpeed.pendingValue(), ChamsConfig.o_coreBounceHeight.pendingValue(), ChamsConfig.o_coreDelay.pendingValue()), 0.0F);
            matrixStack.multiply((new Quaternionf()).setAngleAxis(PI_DIV_3, SINE_45_DEGREES, 0.0F, SINE_45_DEGREES));
            matrixStack.multiply((new Quaternionf()).setAngleAxis(PI_DIV_3, SINE_45_DEGREES, 0.0F, SINE_45_DEGREES));
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(((realAge + ChamsConfig.o_coreDelay.pendingValue()) * (ChamsConfig.o_coreRotationSpeed.pendingValue() * 3)) % 360));
            matrixStack.multiply((new Quaternionf()).setAngleAxis(PI_DIV_3, SINE_45_DEGREES, 0.0F, SINE_45_DEGREES));
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(((realAge + ChamsConfig.o_coreDelay.pendingValue()) * (ChamsConfig.o_coreRotationSpeed.pendingValue() * 3)) % 360));
            float v = ChamsConfig.o_coreScaleAnimation.pendingValue() ? MathHelper.lerp(ChamsConfig.o_coreScaleEasing.pendingValue().getFunction().apply((double) MathHelper.clamp((endCrystalEntity.age + tickDelta - (ChamsConfig.o_coreScaleDelay.pendingValue() * 20)) / (20 * ChamsConfig.o_coreScaleAnimDuration.pendingValue()), 0, 1)).floatValue(), ChamsConfig.o_coreStartScale.pendingValue(), coreScale) : coreScale;
            matrixStack.scale(v * 1.53125F, v * 1.53125F, v * 1.53125F);
            int col = getColor(ChamsConfig.o_coreColor.pendingValue(),
                    getAnimatedValue(ChamsConfig.o_coreAlphaAnimation.pendingValue(), ChamsConfig.o_coreAlphaEasing.pendingValue(), endCrystalEntity.age + tickDelta, ChamsConfig.o_coreAlphaDelay.pendingValue(), ChamsConfig.o_coreAlphaAnimDuration.pendingValue(), ChamsConfig.o_coreStartOpacity.pendingValue(), ChamsConfig.o_coreAlpha.pendingValue()),
                    ChamsConfig.o_coreRainbow.pendingValue(), ChamsConfig.o_coreRainbowDelay.pendingValue(), ChamsConfig.o_coreRainbowSpeed.pendingValue(), ChamsConfig.o_coreRainbowSaturation.pendingValue(), ChamsConfig.o_coreRainbowBrightness.pendingValue());
            core.renderWithoutChildren(matrixStack, getLayer(vertexConsumerProvider, ChamsConfig.o_coreRenderLayer.pendingValue(), ChamsConfig.o_coreCulling.pendingValue(), TEXTURE), getLight(light, ChamsConfig.o_coreLightLevel.pendingValue()), overlay, col);
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
            EnderDragonEntityRenderer.renderCrystalBeam(-p, -q + CrystalChams.getYOffset(realAge, ChamsConfig.o_coreOffset.pendingValue(), ChamsConfig.o_coreBounceSpeed.pendingValue(), ChamsConfig.o_coreBounceHeight.pendingValue(), ChamsConfig.o_coreDelay.pendingValue()), -r, tickDelta, endCrystalEntity.endCrystalAge, matrixStack, vertexConsumerProvider, light);
        }
        super.render(endCrystalEntity, yaw, tickDelta, matrixStack, vertexConsumerProvider, light);
        if (ChamsConfig.o_renderHitbox.pendingValue() && !CrystalChams.mc.getEntityRenderDispatcher().shouldRenderHitboxes()) {
            //potentially add custom line renderer layer to here
            EntityRenderDispatcher.renderHitbox(matrixStack, vertexConsumerProvider.getBuffer(RenderLayer.getLines()), endCrystalEntity, tickDelta, 1, 1, 1);
        }
        coreVerticalOffsetAnimator.update();
        ci.cancel();
    }

    //MAKE SURE THAT IT'S THE age and not endCrystalAge!!!!!!!!!!!!!!!!!!!!!
    @Unique
    private static float getAnimatedValue(boolean animate, Easings function, float ageAndDelta, float delay, float duration, float startValue, float targetValue) {
        if(animate) {
            return MathHelper.lerp(function.getFunction().apply((double) MathHelper.clamp((ageAndDelta - (delay * 20)) / (20 * duration), 0, 1)).floatValue(), startValue, targetValue);
        }
        else{
            return targetValue;
        }
    }

    @Unique
    private static int getLight(int light, int value) {
        return value != -1 ? value : light;
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
    @Unique
    private static int getColor(Color col, float alpha, boolean rainbow, float delay, float speed, float saturation, float brightness) {
        if(!rainbow){
            return getColor(col, alpha);
        }
        else{
            return getColor(new Color(CrystalChams.getRainbow(delay, speed, saturation, brightness)), alpha);
        }
    }

}
