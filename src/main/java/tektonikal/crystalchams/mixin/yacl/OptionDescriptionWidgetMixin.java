package tektonikal.crystalchams.mixin.yacl;

import dev.isxander.yacl3.gui.OptionDescriptionWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import tektonikal.crystalchams.CrystalChams;
import tektonikal.crystalchams.config.SecondaryYACLScreen;

@Mixin(OptionDescriptionWidget.class)
public abstract class OptionDescriptionWidgetMixin extends ClickableWidget {


    public OptionDescriptionWidgetMixin(int x, int y, int width, int height, Text message) {
        super(x, y, width, height, message);
    }

//    @ModifyArgs(method = "renderWidget", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;enableScissor(IIII)V"))
//    private void ough(Args args) {
//        //TODO: guh????
//        if ((CrystalChams.mc.currentScreen instanceof EvilYACLScreen || CrystalChams.mc.currentScreen instanceof SecondaryYACLScreen)) {
//            args.set(0, 0);
//            args.set(1, 0);
//            args.set(2, 9999);
//            args.set(3, 9999);
//        }
//    }

    @Inject(method = "tick()V", at = @At("TAIL"), remap = false)
    private void CC$OUGHHHHHHH(CallbackInfo ci) {
        //NOP?
    }
}
