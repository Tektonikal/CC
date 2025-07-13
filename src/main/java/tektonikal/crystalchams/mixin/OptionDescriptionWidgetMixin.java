package tektonikal.crystalchams.mixin;

import dev.isxander.yacl3.gui.OptionDescriptionWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tektonikal.crystalchams.CrystalChams;

@Mixin(OptionDescriptionWidget.class)
public abstract class OptionDescriptionWidgetMixin extends ClickableWidget {


    public OptionDescriptionWidgetMixin(int x, int y, int width, int height, Text message) {
        super(x, y, width, height, message);
    }

    @Inject(method = "tick()V", at = @At("TAIL"), remap = false)
    private void CC$OUGHHHHHHH(CallbackInfo ci) {
        CrystalChams.entity.age++;
        CrystalChams.entity.endCrystalAge++;
    }
}
