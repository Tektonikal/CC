package tektonikal.crystalchams.mixin.yacl;

import dev.isxander.yacl3.gui.YACLScreen;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(YACLScreen.CategoryTab.class)
public interface CategoryTabAccessor {
    @Accessor("rightPaneDim")
    ScreenRectangle rightPaneDim();
}
