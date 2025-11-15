package tektonikal.crystalchams.mixin;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EndCrystalEntityRenderer;
import net.minecraft.client.render.entity.EnderDragonEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tektonikal.crystalchams.CrystalChams;
import tektonikal.crystalchams.config.ChamsConfig;

import static tektonikal.crystalchams.CrystalChams.renderCustomBeam;

@Mixin(EnderDragonEntityRenderer.class)
public abstract class EnderDragonEntityRendererMixin extends EntityRenderer<EnderDragonEntity> {

    protected EnderDragonEntityRendererMixin(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Redirect(method = "render(Lnet/minecraft/entity/boss/dragon/EnderDragonEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/EndCrystalEntityRenderer;getYOffset(Lnet/minecraft/entity/decoration/EndCrystalEntity;F)F"))
    private float CC$BeamOffsetRedirect(EndCrystalEntity crystal, float tickDelta) {
        if (ChamsConfig.o_modEnabled.pendingValue()) {
            return CrystalChams.getYOffset(crystal.endCrystalAge + tickDelta, ChamsConfig.o_coreOffset.pendingValue(), ChamsConfig.o_coreBounceSpeed.pendingValue(), ChamsConfig.o_coreBounceHeight.pendingValue(), ChamsConfig.o_coreDelay.pendingValue());
        } else {
            return EndCrystalEntityRenderer.getYOffset(crystal, tickDelta);
        }
    }

    @Inject(method = "renderCrystalBeam", at = @At(value = "HEAD"), cancellable = true)
    private static void CC$BeamRenderInject(float dx, float dy, float dz, float tickDelta, int age, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (!ChamsConfig.o_modEnabled.pendingValue()) {
            return;
        }
        if (!ChamsConfig.o_renderBeam.pendingValue()) {
            ci.cancel();
            return;
        }
        renderCustomBeam(dx, dy, dz, tickDelta, age, matrices, vertexConsumers, light, 0);
        ci.cancel();
    }
}
