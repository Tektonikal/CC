package tektonikal.crystalchams.mixin;

import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(RenderTickCounter.Dynamic.class)
public interface RenderTickCounterAccessor {
//    @Accessor("tickDelta")
//    float getTickDelta();
}
