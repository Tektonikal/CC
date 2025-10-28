package tektonikal.crystalchams.config;

import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.utils.Dimension;
import dev.isxander.yacl3.gui.AbstractWidget;
import dev.isxander.yacl3.gui.YACLScreen;
import dev.isxander.yacl3.gui.controllers.TickBoxController;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.ColorHelper;
import tektonikal.crystalchams.CrystalChams;

public class CustomTickBoxController extends TickBoxController {
    public CustomTickBoxController(Option<Boolean> option) {
        super(option);
    }

    @Override
    public AbstractWidget provideWidget(YACLScreen screen, Dimension<Integer> widgetDimension) {
        return new CustomTickBoxElement(this, screen, widgetDimension);
    }

    public static class CustomTickBoxElement extends TickBoxControllerElement {
        float enabledState = control.option().stateManager().get() ? 1 : 0;

        public CustomTickBoxElement(TickBoxController control, YACLScreen screen, Dimension<Integer> dim) {
            super(control, screen, dim);
        }

        @Override
        public void render(DrawContext graphics, int mouseX, int mouseY, float delta) {
            super.render(graphics, mouseX, mouseY, delta);
            enabledState = (float) CrystalChams.ease(enabledState, control.option().stateManager().get() ? 1 : 0, 10F);
        }

        @Override
        protected void drawHoveredControl(DrawContext graphics, int mouseX, int mouseY, float delta) {
                int outlineSize = 10;
                int outlineX1 = getDimension().xLimit() - getXPadding() - outlineSize;
                int outlineY1 = getDimension().centerY() - outlineSize / 2;
                int outlineX2 = getDimension().xLimit() - getXPadding();
                int outlineY2 = getDimension().centerY() + outlineSize / 2;

                int color = getValueColor();
                int shadowColor = multiplyColor(color, 0.25f);

                drawOutline(graphics, outlineX1 + 1, outlineY1 + 1, outlineX2 + 1, outlineY2 + 1, 1, shadowColor);
                drawOutline(graphics, outlineX1, outlineY1, outlineX2, outlineY2, 1, color);
            if (ChamsConfig.o_showAnimations.pendingValue()) {
                graphics.fill(outlineX1 + 3, outlineY1 + 3, outlineX2 - 1, outlineY2 - 1, enabledState >= 0.5F ? ColorHelper.Argb.lerp(enabledState, 0x00000000, shadowColor) : 0x00000000);
                graphics.fill(outlineX1 + 2, outlineY1 + 2, outlineX2 - 2, outlineY2 - 2, ColorHelper.Argb.lerp(enabledState, 0x00000000, color));
            }
            else{
                if (control.option().pendingValue()) {
                    graphics.fill(outlineX1 + 3, outlineY1 + 3, outlineX2 - 1, outlineY2 - 1, shadowColor);
                    graphics.fill(outlineX1 + 2, outlineY1 + 2, outlineX2 - 2, outlineY2 - 2, color);
                }
            }
        }

    }
}
