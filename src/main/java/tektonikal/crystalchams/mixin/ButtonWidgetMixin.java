package tektonikal.crystalchams.mixin;

import dev.isxander.yacl3.gui.LowProfileButtonWidget;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tektonikal.crystalchams.CrystalChams;
import tektonikal.crystalchams.config.ChamsConfig;

import java.awt.*;

@Mixin(ClickableWidget.class)
public abstract class ButtonWidgetMixin{
    @Shadow private int x;
    @Shadow private int y;
    @Shadow protected int width;
    @Shadow protected int height;

    @Shadow public boolean active;


    @Shadow private boolean focused;
    @Shadow protected boolean hovered;


    @Unique
    float hoverProgress;
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/ClickableWidget;renderWidget(Lnet/minecraft/client/gui/DrawContext;IIF)V", shift = At.Shift.AFTER))
    private void CC$OUGHHHH(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci){
        if(CrystalChams.isThisMyScreen() && ((ClickableWidget)(Object)this) instanceof ButtonWidget && !(((ClickableWidget)(Object)this) instanceof LowProfileButtonWidget) && ChamsConfig.o_showAnimations.pendingValue()){
                hoverProgress = (float) CrystalChams.ease(hoverProgress, (hovered || focused && CrystalChams.mc.getNavigationType().isKeyboard()) && active ? 1 : 0, 10F);
            context.drawBorder(x, y, width, height, new Color(hoverProgress, hoverProgress, hoverProgress).getRGB());
        }
    }
}
