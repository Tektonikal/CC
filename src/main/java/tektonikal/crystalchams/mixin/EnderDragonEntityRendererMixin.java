package tektonikal.crystalchams.mixin;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.EndCrystalEntityRenderer;
import net.minecraft.client.render.entity.EnderDragonEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.state.EnderDragonEntityRenderState;
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

//import static tektonikal.crystalchams.CrystalChams.renderCustomBeam;

@Mixin(EnderDragonEntityRenderer.class)
public abstract class EnderDragonEntityRendererMixin extends EntityRenderer<EnderDragonEntity, EnderDragonEntityRenderState> {

    protected EnderDragonEntityRendererMixin(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Redirect(method = "updateRenderState(Lnet/minecraft/entity/boss/dragon/EnderDragonEntity;Lnet/minecraft/client/render/entity/state/EnderDragonEntityRenderState;F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/EndCrystalEntityRenderer;getYOffset(F)F"))
    private float CC$BeamOffsetRedirect(float f) {
        if (ChamsConfig.o_modEnabled.pendingValue()) {
            return CrystalChams.getYOffset(f, ChamsConfig.o_coreOffset.pendingValue(), ChamsConfig.o_coreBounceSpeed.pendingValue(), ChamsConfig.o_coreBounceHeight.pendingValue(), ChamsConfig.o_coreDelay.pendingValue());
        } else {
            return EndCrystalEntityRenderer.getYOffset(f);
        }
    }

    @Inject(method = "renderCrystalBeam", at = @At(value = "HEAD"), cancellable = true)
    private static void CC$BeamRenderInject(float dx, float dy, float dz, float tickProgress, MatrixStack matrices, OrderedRenderCommandQueue queue, int light, CallbackInfo ci) {
        if (!ChamsConfig.o_modEnabled.pendingValue()) {
            return;
        }
        if (!ChamsConfig.o_renderBeam.pendingValue()) {
            ci.cancel();
            return;
        }
        CrystalChams.renderCustomBeam(dx, dy, dz, tickProgress, matrices, queue, light, 0);
        ci.cancel();
    }
}
