package tektonikal.crystalchams.mixin;

import dev.isxander.yacl3.gui.ElementListWidgetExt;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.gui.widget.Widget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import tektonikal.crystalchams.interfaces.ElementListWidgetExtInterface;

@Mixin(ElementListWidgetExt.class)
public abstract class ElementListWidgetExtMixin<E extends dev.isxander.yacl3.gui.ElementListWidgetExt.Entry<E>> extends ElementListWidget<E> implements Widget, ElementListWidgetExtInterface {
    @Shadow(remap = false) private double smoothScrollAmount;

    public ElementListWidgetExtMixin(MinecraftClient minecraftClient, int i, int j, int k, int l) {
        super(minecraftClient, i, j, k, l);
    }

    @Unique
    public void setSmoothScrollAmount(double amount) {
        this.smoothScrollAmount = amount;
    }
}
