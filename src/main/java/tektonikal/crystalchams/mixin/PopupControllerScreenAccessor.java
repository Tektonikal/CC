package tektonikal.crystalchams.mixin;

import dev.isxander.yacl3.gui.YACLScreen;
import dev.isxander.yacl3.gui.controllers.PopupControllerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PopupControllerScreen.class)
public interface PopupControllerScreenAccessor {
    @Accessor(remap = false, value = "backgroundYaclScreen")
    YACLScreen getBackgroundYaclScreen();
}
