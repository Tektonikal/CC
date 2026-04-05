package tektonikal.crystalchams.mixin;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EndCrystalRenderer;
import net.minecraft.client.renderer.entity.EnderDragonRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EnderDragonRenderState;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tektonikal.crystalchams.CrystalChams;
import tektonikal.crystalchams.config.ChamsConfig;

//import static tektonikal.crystalchams.CrystalChams.renderCustomBeam;

@Mixin(EnderDragonRenderer.class)
public abstract class EnderDragonRendererMixin extends EntityRenderer<EnderDragon, EnderDragonRenderState> {

    protected EnderDragonRendererMixin(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Redirect(method = "extractRenderState(Lnet/minecraft/world/entity/boss/enderdragon/EnderDragon;Lnet/minecraft/client/renderer/entity/state/EnderDragonRenderState;F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/EndCrystalRenderer;getY(F)F"))
    private float CC$BeamOffsetRedirect(float f) {
        if (ChamsConfig.o_modEnabled.pendingValue()) {
            return CrystalChams.getYOffset(f, ChamsConfig.o_coreOffset.pendingValue(), ChamsConfig.o_coreBounceSpeed.pendingValue(), ChamsConfig.o_coreBounceHeight.pendingValue(), ChamsConfig.o_coreDelay.pendingValue());
        } else {
            return EndCrystalRenderer.getY(f);
        }
    }

    @Inject(method = "submitCrystalBeams", at = @At(value = "HEAD"), cancellable = true)
    private static void CC$BeamRenderInject(float dx, float dy, float dz, float tickProgress, PoseStack matrices, SubmitNodeCollector queue, int light, CallbackInfo ci) {
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
