package tektonikal.crystalchams.mixin;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import tektonikal.crystalchams.interfaces.ModelPartInterface;

@Mixin(ModelPart.class)
public abstract class ModelPartMixin implements ModelPartInterface {

    @Shadow
    protected abstract void renderCuboids(MatrixStack.Entry entry, VertexConsumer vertexConsumer, int light, int overlay, int color);


    @Unique
    @Override
    public void renderWithoutChildren(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, int color) {
        this.renderCuboids(matrices.peek(), vertices, light, overlay, color);
    }
}
