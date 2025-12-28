package tektonikal.crystalchams.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.isxander.yacl3.gui.YACLScreen;
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
import tektonikal.crystalchams.config.EvilYACLScreen;
import tektonikal.crystalchams.config.SecondaryYACLScreen;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Shadow
    @Nullable
    public Screen currentScreen;


    @Shadow
    @Final
    private Window window;

    @ModifyReturnValue(method = "getFramerateLimit", at = @At("RETURN"))
    private int CC$fixFPSLimit(int original) {
        return currentScreen instanceof EvilYACLScreen || currentScreen instanceof SecondaryYACLScreen ? this.window.getFramerateLimit() : original;
    }
}
