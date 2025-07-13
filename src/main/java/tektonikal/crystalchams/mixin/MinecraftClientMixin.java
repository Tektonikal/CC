package tektonikal.crystalchams.mixin;

import dev.isxander.yacl3.gui.YACLScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Overlay;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.Window;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Shadow @Nullable public ClientWorld world;

    @Shadow @Nullable public Screen currentScreen;

    @Shadow private @Nullable Overlay overlay;

    @Shadow @Final private Window window;

    @Inject(method = "getFramerateLimit", at = @At("HEAD"), cancellable = true)
    private void CC$getFramerateLimit(CallbackInfoReturnable<Integer> cir) {
        //I don't wanna talk about it
        cir.setReturnValue(this.world != null || (this.currentScreen == null && this.overlay == null) || (this.currentScreen instanceof YACLScreen && ((YACLScreen) this.currentScreen).config.title().equals(Text.of("Custom End Crystals"))) ? this.window.getFramerateLimit() : 60);
    }
}
