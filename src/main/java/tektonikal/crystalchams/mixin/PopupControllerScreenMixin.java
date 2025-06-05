package tektonikal.crystalchams.mixin;

import dev.isxander.yacl3.gui.YACLScreen;
import dev.isxander.yacl3.gui.controllers.ControllerPopupWidget;
import dev.isxander.yacl3.gui.controllers.PopupControllerScreen;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PopupControllerScreen.class)
public abstract class PopupControllerScreenMixin extends Screen {
    @Shadow(remap = false)
    @Final
    private YACLScreen backgroundYaclScreen;


    @Shadow(remap = false) @Final private ControllerPopupWidget<?> controllerPopup;

    protected PopupControllerScreenMixin(Text title) {
        super(title);
    }

//    @Inject(method = "render", at = @At("HEAD"), cancellable = true, remap = false)
//    private void ough(DrawContext graphics, int mouseX, int mouseY, float delta, CallbackInfo ci) {
//        if (backgroundYaclScreen.config.title().equals(Text.of("Custom End Crystals"))) {
//            //TODO: i'll let xander gaslight the other options, i can just get the mouse position myself
//            controllerPopup.renderBackground(graphics, mouseX, mouseY, delta);
//            this.backgroundYaclScreen.render(graphics, mouseX, mouseY, delta);
//            super.render(graphics, mouseX, mouseY, delta);
//            ci.cancel();
//        }
//    }

    @Override
    public void tick() {
        if (backgroundYaclScreen.config.title().equals(Text.of("Custom End Crystals"))) {
            backgroundYaclScreen.tick();
        }
    }
    @Mixin(PopupControllerScreen.class)
    public interface PopupControllerScreenAccessor {
    @Accessor(remap = false)
    YACLScreen getBackgroundYaclScreen();
    }
}
