package tektonikal.crystalchams.interfaces;

import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public interface ModelPartInterface {
    //This isn't needed in 1.21, but as of 1.21.4 is?
    default void renderWithoutChildren(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, int color){
    }
}
