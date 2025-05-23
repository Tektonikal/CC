package tektonikal.crystalchams.mixin;

import dev.isxander.yacl3.gui.DescriptionWithName;
import dev.isxander.yacl3.gui.OptionDescriptionWidget;
import dev.isxander.yacl3.gui.YACLScreen;
import dev.isxander.yacl3.gui.image.ImageRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tektonikal.crystalchams.CrystalChams;

import java.util.List;
import java.util.function.Supplier;

@Mixin(OptionDescriptionWidget.class)
public abstract class OptionDescriptionWidgetMixin extends ClickableWidget {


    public OptionDescriptionWidgetMixin(int x, int y, int width, int height, Text message) {
        super(x, y, width, height, message);
    }

    @Redirect(method = "renderWidget(Lnet/minecraft/client/gui/DrawContext;IIF)V", at = @At(value = "INVOKE", target = "Ldev/isxander/yacl3/gui/image/ImageRenderer;render(Lnet/minecraft/client/gui/DrawContext;IIIF)I"))
        private int CC$EVIL(ImageRenderer instance, DrawContext drawContext, int i, int o, int p, float v){
        //NOP
        return i;
    }
    @Inject(method = "renderWidget", at = @At(value = "HEAD"))
    public void redir(DrawContext drawContext, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (!((YACLScreen) MinecraftClient.getInstance().currentScreen).config.title().equals(Text.of("Custom End Crystals"))) {
            return;
        }
        int y = getY();


        drawContext.enableScissor(getX(), y, getX() + getWidth(), getY() + getHeight());


        float i = (float) Math.atan((((getX() + getX() + getWidth()) / 2F) - mouseX) / 40.0F);
        float j = (float) Math.atan((((y + y + getHeight()) / 2F) - mouseY) / 40.0F);
        drawContext.getMatrices().push();
        drawContext.getMatrices().translate((getX() + getX() + getWidth()) / 2F, (y + y + getHeight()) / 2F, 100.0);
        drawContext.getMatrices().scale(50, 50, -50);
        drawContext.getMatrices().multiply(RotationAxis.POSITIVE_Z.rotation((float) Math.PI));
        drawContext.getMatrices().multiply(RotationAxis.POSITIVE_X.rotation(j * 20.0F * (float) (Math.PI / 180.0)));
        drawContext.getMatrices().multiply(RotationAxis.NEGATIVE_Y.rotation(i * 20.0F * (float) (Math.PI / 180.0)));
        MinecraftClient.getInstance().getEntityRenderDispatcher().getRenderer(CrystalChams.entity).render(CrystalChams.entity, 0, ((RenderTickCounter.Dynamic) MinecraftClient.getInstance().getRenderTickCounter()).tickDelta, drawContext.getMatrices(), MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers(), 255);
        drawContext.draw();
        drawContext.getMatrices().pop();

        drawContext.disableScissor();
//        drawContext.fill(x, y, x + renderWidth, y + getHeight(), 0x80FFFFFF);
        //i don't know why, but for some reason the width is just halved on gui scale 1
    }
    @Inject(method = "tick()V", at = @At("TAIL"), remap = false)
    private void CC$OUGHHHHHHH(CallbackInfo ci){
        CrystalChams.entity.age++;
        CrystalChams.entity.endCrystalAge++;
    }
}
