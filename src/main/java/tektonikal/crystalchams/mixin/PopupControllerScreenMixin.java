package tektonikal.crystalchams.mixin;

import dev.isxander.yacl3.gui.YACLScreen;
import dev.isxander.yacl3.gui.controllers.PopupControllerScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

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
        //TODO: this got fixed in newer YACL
        if (backgroundYaclScreen.config.title().equals(Text.of("Custom End Crystals"))) {
            backgroundYaclScreen.tick();
        }
    }
}
