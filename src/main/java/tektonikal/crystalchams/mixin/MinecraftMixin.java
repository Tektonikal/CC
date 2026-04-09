package tektonikal.crystalchams.mixin;

import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Shadow
    @Nullable
    public Screen screen;


    @Shadow
    @Final
    private Window window;

//    @ModifyReturnValue(method = "getFramerateLimit", at = @At("RETURN"))
//    private int CC$fixFPSLimit(int original) {
//        return currentScreen instanceof EvilYACLScreen || currentScreen instanceof SecondaryYACLScreen ? this.window.getFramerateLimit() : original;
//    }
}
