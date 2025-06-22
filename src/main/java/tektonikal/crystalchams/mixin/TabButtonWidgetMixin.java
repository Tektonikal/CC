package tektonikal.crystalchams.mixin;

import dev.isxander.yacl3.gui.YACLScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.TabButtonWidget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import tektonikal.crystalchams.CrystalChams;

@Mixin(TabButtonWidget.class)
public abstract class TabButtonWidgetMixin extends ClickableWidget {

    @Unique
    float hoverProgress;
    @Unique
    float selectedProgress;

    @Unique
    ButtonTextures altTextures = new ButtonTextures(
            Identifier.ofVanilla("widget/tab_selected"),
            Identifier.ofVanilla("widget/tab"),
            Identifier.ofVanilla("widget/tab_selected"),
            Identifier.ofVanilla("widget/tab")
    );

    public TabButtonWidgetMixin(int x, int y, int width, int height, Text message) {
        super(x, y, width, height, message);
    }

    @Shadow
    public abstract boolean isCurrentTab();

    @ModifyArgs(method = "renderWidget", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIII)V"))
    public void CC$OUGH(Args args) {
        if (MinecraftClient.getInstance().currentScreen instanceof YACLScreen && ((YACLScreen) MinecraftClient.getInstance().currentScreen).config.title().equals(Text.of("Custom End Crystals"))) {

            args.set(0, altTextures.get(this.isCurrentTab(), this.isSelected()));
        }
    }

    @Inject(method = "renderWidget", at = @At("TAIL"))
    public void render(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        hoverProgress = (float) CrystalChams.ease(hoverProgress, hovered && this.active ? 1 : 0, 10F);
        selectedProgress = (float) CrystalChams.ease(selectedProgress, isCurrentTab() && this.active ? 1 : 0, 10F);
    }

    @Inject(method = "drawCurrentTabLine", at = @At("HEAD"), cancellable = true)
    private void drawCurrentTabLine(DrawContext context, TextRenderer textRenderer, int color, CallbackInfo ci) {
        if (MinecraftClient.getInstance().currentScreen instanceof YACLScreen && ((YACLScreen) MinecraftClient.getInstance().currentScreen).config.title().equals(Text.of("Custom End Crystals"))) {
            int i = Math.min(textRenderer.getWidth(this.getMessage()), this.getWidth() - 4);
            float j = this.getX() + ((this.getWidth() - i) / 2F);
            int k = this.getY() + this.getHeight() - 2;
            fillFloat(context, MathHelper.lerp(hoverProgress * selectedProgress, j + (i / 2F), j), k - (hoverProgress * selectedProgress) + 1, MathHelper.lerp(hoverProgress * selectedProgress, j + (i / 2F), (j + i)), k + 1, ColorHelper.Argb.lerp(hoverProgress * selectedProgress, 0x00000000, this.active ? -1 : -6250336));
            ci.cancel();
        }
    }

    @Inject(method = "renderWidget", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIII)V", shift = At.Shift.AFTER))
    public void CC$OUGHHHHHHHHH(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (MinecraftClient.getInstance().currentScreen instanceof YACLScreen && ((YACLScreen) MinecraftClient.getInstance().currentScreen).config.title().equals(Text.of("Custom End Crystals"))) {
            drawBorder(context, this.getX() + 1, isCurrentTab() ? this.getY() + 1 : this.getY() + 5, this.width - 2, isCurrentTab() ? this.height - 2 : this.height - 7, ColorHelper.Argb.lerp(hoverProgress, 0x00333333, 0xFFFFFFFF), !isCurrentTab());
        }
    }

    @Unique
    public void drawBorder(DrawContext context, float x, float y, float width, float height, int color, boolean bottom) {
        fillFloat(context, x, y, x + width, y + 1, color);
        if (bottom) {
            fillFloat(context, x + 1, y + height - 1, x + width - 1, y + height, color);
        }
        fillFloat(context, x, y + 1, x + 1, y + height, color);
        fillFloat(context, x + width - 1, y + 1, x + width, y + height, color);
    }

    @Unique
    public void fillFloat(DrawContext context, float x1, float y1, float x2, float y2, int color) {
        Matrix4f matrix4f = context.getMatrices().peek().getPositionMatrix();
        VertexConsumer vertexConsumer = context.getVertexConsumers().getBuffer(RenderLayer.getGui());
        vertexConsumer.vertex(matrix4f, x1, y1, 0).color(color);
        vertexConsumer.vertex(matrix4f, x1, y2, 0).color(color);
        vertexConsumer.vertex(matrix4f, x2, y2, 0).color(color);
        vertexConsumer.vertex(matrix4f, x2, y1, 0).color(color);
        context.draw();
    }
}
