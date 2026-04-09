package tektonikal.crystalchams.mixin;

import net.minecraft.client.DeltaTracker;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(DeltaTracker.Timer.class)
public interface RenderTickCounterAccessor {
//    @Accessor("tickDelta")
//    float getTickDelta();
}
