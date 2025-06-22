package tektonikal.crystalchams.mixin;

import dev.isxander.yacl3.gui.LowProfileButtonWidget;
import dev.isxander.yacl3.gui.YACLScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tektonikal.crystalchams.CrystalChams;

import java.awt.*;

@Mixin(ClickableWidget.class)
public abstract class ButtonWidgetMixin{
    @Shadow private int x;
    @Shadow private int y;
    @Shadow protected int width;
    @Shadow protected int height;

    @Shadow public boolean active;

    @Shadow public abstract boolean isSelected();

    @Unique
    float hoverProgress;
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/ClickableWidget;renderWidget(Lnet/minecraft/client/gui/DrawContext;IIF)V", shift = At.Shift.AFTER))
    private void CC$OUGHHHH(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci){
        if(MinecraftClient.getInstance().currentScreen instanceof YACLScreen && ((YACLScreen) MinecraftClient.getInstance().currentScreen).config.title().equals(Text.of("Custom End Crystals")) && ((ClickableWidget)(Object)this) instanceof ButtonWidget && !(((ClickableWidget)(Object)this) instanceof LowProfileButtonWidget)){
                hoverProgress = (float) CrystalChams.ease(hoverProgress, isSelected() && active ? 1 : 0, 10F);
            context.drawBorder(x, y, width, height, new Color(hoverProgress, hoverProgress, hoverProgress).getRGB());
        }
    }
}
