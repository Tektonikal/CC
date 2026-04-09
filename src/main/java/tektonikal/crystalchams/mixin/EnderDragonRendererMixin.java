package tektonikal.crystalchams.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EndCrystalRenderer;
import net.minecraft.client.renderer.entity.EnderDragonRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EnderDragonRenderState;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
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

    @Inject(method = "submitCrystalBeams", at = @At(value = "HEAD"), cancellable = true)
    private static void CC$BeamRenderInject(float deltaX, float deltaY, float deltaZ, float timeInTicks, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, CallbackInfo ci) {
        if (!ChamsConfig.o_modEnabled.pendingValue()) {
            return;
        }
        if (!ChamsConfig.o_renderBeam.pendingValue()) {
            ci.cancel();
            return;
        }
        CrystalChams.renderCustomBeam(deltaX, deltaY, deltaZ, timeInTicks, poseStack, submitNodeCollector, lightCoords, 0);
        ci.cancel();
    }

    @Redirect(method = "extractRenderState(Lnet/minecraft/world/entity/boss/enderdragon/EnderDragon;Lnet/minecraft/client/renderer/entity/state/EnderDragonRenderState;F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/EndCrystalRenderer;getY(F)F"))
    private float CC$BeamOffsetRedirect(float timeInTicks) {
        if (ChamsConfig.o_modEnabled.pendingValue()) {
            return CrystalChams.getYOffset(timeInTicks, ChamsConfig.o_coreOffset.pendingValue(), ChamsConfig.o_coreBounceSpeed.pendingValue(), ChamsConfig.o_coreBounceHeight.pendingValue(), ChamsConfig.o_coreDelay.pendingValue());
        } else {
            return EndCrystalRenderer.getY(timeInTicks);
        }
    }
}
