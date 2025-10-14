package tektonikal.crystalchams.mixin;

import dev.isxander.yacl3.gui.YACLScreen;
import net.minecraft.client.gui.ScreenRect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(YACLScreen.CategoryTab.class)
public interface CategoryTabAccessor {
    @Accessor("rightPaneDim")
    ScreenRect rightPaneDim();
}
