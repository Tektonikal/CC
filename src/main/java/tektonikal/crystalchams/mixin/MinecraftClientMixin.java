package tektonikal.crystalchams.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.Window;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Shadow
    @Nullable
    public Screen currentScreen;


    @Shadow
    @Final
    private Window window;

//    @ModifyReturnValue(method = "getFramerateLimit", at = @At("RETURN"))
//    private int CC$fixFPSLimit(int original) {
//        return currentScreen instanceof EvilYACLScreen || currentScreen instanceof SecondaryYACLScreen ? this.window.getFramerateLimit() : original;
//    }
}
