package tektonikal.crystalchams.mixin.yacl;

import dev.isxander.yacl3.gui.OptionListWidget;
import dev.isxander.yacl3.gui.YACLScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = OptionListWidget.class)
public interface EvilAccessor {


    @Accessor(remap = false)
    YACLScreen getYaclScreen();
}