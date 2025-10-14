package tektonikal.crystalchams.mixin;

import dev.isxander.yacl3.api.utils.Dimension;
import dev.isxander.yacl3.gui.AbstractWidget;
import dev.isxander.yacl3.gui.YACLScreen;
import dev.isxander.yacl3.gui.controllers.ControllerWidget;
import net.minecraft.client.gui.DrawContext;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tektonikal.crystalchams.CrystalChams;
import tektonikal.crystalchams.config.ChamsConfig;

import java.awt.*;

@Mixin(ControllerWidget.class)
public abstract class ControllerWidgetMixin extends AbstractWidget {
    @Shadow(remap = false)
    @Final
    protected YACLScreen screen;
    @Unique
    float hoverProgress = 0;

    public ControllerWidgetMixin(Dimension<Integer> dim) {
        super(dim);
    }

    @Shadow(remap = false)
    public abstract boolean isHovered();

    @Shadow(remap = false)
    protected abstract boolean isAvailable();

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Ldev/isxander/yacl3/gui/controllers/ControllerWidget;drawButtonRect(Lnet/minecraft/client/gui/DrawContext;IIIIZZ)V", shift = At.Shift.AFTER))
    private void CC$OUGHHH(DrawContext graphics, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (CrystalChams.isThisMyScreen(screen) && ChamsConfig.CONFIG.instance().showAnimations) {
            hoverProgress = (float) CrystalChams.ease(hoverProgress, isAvailable() && isHovered() ? 1 : 0, 10F);
            drawOutline(graphics, getDimension().x(), getDimension().y(), getDimension().xLimit(), getDimension().yLimit(), 1, new Color(hoverProgress, hoverProgress, hoverProgress).getRGB());
        }
    }
}
