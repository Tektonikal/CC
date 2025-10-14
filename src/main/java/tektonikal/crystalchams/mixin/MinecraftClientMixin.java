package tektonikal.crystalchams.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Overlay;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.Window;
import net.minecraft.client.world.ClientWorld;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tektonikal.crystalchams.CrystalChams;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Shadow @Nullable public ClientWorld world;

    @Shadow @Nullable public Screen currentScreen;

    @Shadow private @Nullable Overlay overlay;

    @Shadow @Final private Window window;

    @Inject(method = "getFramerateLimit", at = @At("HEAD"), cancellable = true)
    private void CC$getFramerateLimit(CallbackInfoReturnable<Integer> cir) {
        //TODO: modifyargs/modify return value
        cir.setReturnValue(this.world != null || (this.currentScreen == null && this.overlay == null) || CrystalChams.isThisMyScreen(currentScreen) ? this.window.getFramerateLimit() : 60);
    }
}
