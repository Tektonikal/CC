package tektonikal.crystalchams.mixin.yacl;

import dev.isxander.yacl3.gui.AbstractWidget;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AbstractWidget.class)
public class AbstractWidgetMixin {
    //TODO: hover animation for list entries
//    @Unique
//    private float hoverProgress;
//
//    @Inject(method = "drawOutline", at = @At("HEAD"))
//    private void ough(DrawContext graphics, int x1, int y1, int x2, int y2, int width, int color, CallbackInfo ci){
//        graphics.drawHorizontalLine(0, 1000, 20, -1);
//    }
//    @Inject(method = "drawButtonRect", at = @At("HEAD"))
//    private void oughh(DrawContext graphics, int x1, int y1, int x2, int y2, boolean hovered, boolean enabled, CallbackInfo ci){
//        hoverProgress = (float) CrystalChams.ease(hoverProgress, hovered && enabled ? 1 : 0, 10F);
//    }
}
