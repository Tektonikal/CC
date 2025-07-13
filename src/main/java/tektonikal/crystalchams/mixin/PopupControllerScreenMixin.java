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

    protected PopupControllerScreenMixin(Text title) {
        super(title);
    }
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
