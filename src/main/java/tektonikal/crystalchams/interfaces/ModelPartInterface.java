package tektonikal.crystalchams.interfaces;

import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public interface ModelPartInterface {
    default void renderWithoutChildren(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, int color){
    }
}
