package tektonikal.crystalchams.mixin.yacl;

import dev.isxander.yacl3.api.Controller;
import dev.isxander.yacl3.api.utils.Dimension;
import dev.isxander.yacl3.gui.AbstractWidget;
import dev.isxander.yacl3.gui.controllers.LabelController;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = LabelController.class, remap = false)
public abstract class LabelControllerMixin implements Controller<Component> {


    @Mixin(value = LabelController.LabelControllerElement.class, remap = false)
    public abstract static class LabelControllerElementMixin extends AbstractWidget {
        @Shadow
        protected abstract int getYPadding();

        @Shadow
        protected abstract int getXPadding();

        @Shadow
        @Final
        LabelController this$0;

        public LabelControllerElementMixin(Dimension<Integer> dim) {
            super(dim);
        }

//        @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawText(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/OrderedText;IIIZ)I"))
//        private int ough(DrawContext graphics, TextRenderer textRenderer, OrderedText text, int x, int y, int color, boolean shadow){
//            if((CrystalChams.mc.currentScreen instanceof EvilYACLScreen || CrystalChams.mc.currentScreen instanceof SecondaryYACLScreen)){
//                graphics.drawCenteredTextWithShadow(textRenderer, text, getDimension().centerX(), y + getYPadding(), this$0.option().available() ? -1 : 0xFFA0A0A0);
//                return 0;
//            }
//            else{
//                return graphics.drawText(textRenderer, text, getDimension().x() + getXPadding(), y + getYPadding(), this$0.option().available() ? -1 : 0xFFA0A0A0, true);
//            }
//        }
    }
}
