package tektonikal.crystalchams.mixin;

import net.minecraft.client.gui.components.AbstractWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(AbstractWidget.class)
public abstract class ButtonWidgetMixin {
    @Shadow
    public boolean active;
    @Shadow
    protected int width;
    @Shadow
    protected int height;
    @Shadow
    protected boolean isHovered;
    @Unique
    float hoverProgress;
    @Shadow
    private int x;
    @Shadow
    private int y;
    @Shadow
    private boolean focused;
//    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/ClickableWidget;renderWidget(Lnet/minecraft/client/gui/DrawContext;IIF)V", shift = At.Shift.AFTER))
//    private void CC$OUGHHHH(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci){
//        if((CrystalChams.mc.currentScreen instanceof EvilYACLScreen || CrystalChams.mc.currentScreen instanceof SecondaryYACLScreen) && ((ClickableWidget)(Object)this) instanceof ButtonWidget && !(((ClickableWidget)(Object)this) instanceof LowProfileButtonWidget) && ChamsConfig.o_showAnimations.pendingValue()){
//                hoverProgress = (float) CrystalChams.ease(hoverProgress, (hovered || focused && CrystalChams.mc.getNavigationType().isKeyboard()) && active ? 1 : 0, 10F);
//            context.drawStrokedRectangle(x, y, width, height, new Color(hoverProgress, hoverProgress, hoverProgress).getRGB());
//        }
//    }
}
