package tektonikal.crystalchams.mixin;

import net.minecraft.client.renderer.debug.EntityHitboxDebugRenderer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(EntityHitboxDebugRenderer.class)
public interface EntityHitboxDebugRendererMixin {
    @Invoker("showHitboxes")
    void yeah(Entity entity, float tickProgress, boolean inLocalServer);
}
