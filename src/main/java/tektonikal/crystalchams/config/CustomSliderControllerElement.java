package tektonikal.crystalchams.config;

import dev.isxander.yacl3.api.utils.Dimension;
import dev.isxander.yacl3.gui.YACLScreen;
import dev.isxander.yacl3.gui.controllers.ControllerWidget;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import org.joml.Matrix4f;
import tektonikal.crystalchams.CrystalChams;
import tektonikal.crystalchams.stupidfuckingboilerplate.CustomFloatSliderController;
import tektonikal.crystalchams.stupidfuckingboilerplate.ICustomSliderController;

public class CustomSliderControllerElement extends ControllerWidget<ICustomSliderController<?>> {
    private final double min, max, interval;

    private float interpolation;

    private double interpolatedValue = control.pendingValue();
    private float focusedTime = 0;
    private float fasterFocusedTime = 0;

    private Dimension<Integer> sliderBounds;

    private boolean mouseDown = false;

    public CustomSliderControllerElement(ICustomSliderController<?> option, YACLScreen screen, Dimension<Integer> dim, double min, double max, double interval) {
        super(option, screen, dim);
        this.min = min;
        this.max = max;
        this.interval = interval;
        setDimension(dim);
    }


    @Override
    public void render(DrawContext graphics, int mouseX, int mouseY, float delta) {
        super.render(graphics, mouseX, mouseY, delta);
        interpolatedValue = CrystalChams.ease(interpolatedValue, control.pendingValue(), 20);
        focusedTime = (float) CrystalChams.ease(focusedTime, (this.hovered || this.focused) && this.isAvailable() ? 1 : 0, 7.5F);
        fasterFocusedTime = (float) CrystalChams.ease(fasterFocusedTime, this.hovered || this.focused ? 1 : 0, 12.5F);
        calculateInterpolation();
        // track
        fillFloat(graphics, MathHelper.lerp(focusedTime, (float) sliderBounds.xLimit(), (float) sliderBounds.x()), sliderBounds.centerY() - 1, sliderBounds.xLimit(), sliderBounds.centerY(), -1);
        // track shadow
        fillFloat(graphics, MathHelper.lerp(focusedTime, (float) sliderBounds.xLimit(), (float) sliderBounds.x()) + 1, sliderBounds.centerY(), sliderBounds.xLimit() + 1, sliderBounds.centerY() + 1, 0xFF404040);
        if (focusedTime > 0.05) {
            // thumb shadow
            fillFloat(graphics, getThumbX() - (float) getThumbWidth() / 2 + 1, MathHelper.lerp(fasterFocusedTime, (float) sliderBounds.centerY(), (float) sliderBounds.y()), getThumbX() + (float) getThumbWidth() / 2 + 1, MathHelper.lerp(fasterFocusedTime, (float) sliderBounds.centerY(), (float) sliderBounds.yLimit()) + 1, 0xFF404040);
            // thumb
            fillFloat(graphics, getThumbX() - ((float) getThumbWidth() / 2), MathHelper.lerp(fasterFocusedTime, (float) sliderBounds.centerY(), (float) sliderBounds.y()) - 1, getThumbX() + ((float) getThumbWidth() / 2), MathHelper.lerp(fasterFocusedTime, (float) sliderBounds.centerY(), (float) sliderBounds.yLimit()), -1);
        }
    }


    @Override
    protected void drawValueText(DrawContext graphics, int mouseX, int mouseY, float delta) {
        graphics.getMatrices().push();
        Text valueText = ((CustomFloatSliderController) (control)).valueFormatter.format((float) interpolatedValue);
        textRenderer.draw(valueText, (getDimension().xLimit() - textRenderer.getWidth(valueText) - getXPadding() - (sliderBounds.width() + 6 + getThumbWidth() / 2f) * focusedTime), getTextY(), getValueColor(), true, graphics.getMatrices().peek().getPositionMatrix(), graphics.getVertexConsumers(), TextRenderer.TextLayerType.NORMAL, 0, 15728880);
        graphics.getMatrices().pop();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!isAvailable() || button != 0 || !sliderBounds.isPointInside((int) mouseX, (int) mouseY))
            return false;

        mouseDown = true;

        setValueFromMouse(mouseX);
        return true;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (!isAvailable() || button != 0 || !mouseDown)
            return false;

        setValueFromMouse(mouseX);
        return true;
    }

    public void incrementValue(double amount) {
        control.setPendingValue(MathHelper.clamp(control.pendingValue() + interval * amount, min, max));
        calculateInterpolation();
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, /*? if >1.20.2 {*/ double horizontal, /*?}*/ double vertical) {
        if (!isAvailable() || (!isMouseOver(mouseX, mouseY)) || (!Screen.hasShiftDown() && !Screen.hasControlDown()))
            return false;

        incrementValue(vertical);
        return true;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (isAvailable() && mouseDown)
            playDownSound();
        mouseDown = false;

        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!focused)
            return false;

        switch (keyCode) {
            case InputUtil.GLFW_KEY_LEFT -> incrementValue(-1);
            case InputUtil.GLFW_KEY_RIGHT -> incrementValue(1);
            default -> {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return super.isMouseOver(mouseX, mouseY) || mouseDown;
    }

    protected void setValueFromMouse(double mouseX) {
        double value = (mouseX - sliderBounds.x()) / sliderBounds.width() * control.range();
        control.setPendingValue(roundToInterval(value));
        calculateInterpolation();
    }

    protected double roundToInterval(double value) {
        return MathHelper.clamp(min + (interval * Math.round(value / interval)), min, max); // extremely imprecise, requires clamping
    }

    @Override
    protected int getHoveredControlWidth() {
        return sliderBounds.width() + getUnhoveredControlWidth() + 6 + getThumbWidth() / 2;
    }

    protected void calculateInterpolation() {
        interpolation = MathHelper.clamp((float) ((interpolatedValue - control.min()) * 1 / control.range()), 0f, 1f);
    }

    @Override
    public void setDimension(Dimension<Integer> dim) {
        super.setDimension(dim);
        int trackWidth = dim.width() / 3;
        if (optionNameString.isEmpty())
            trackWidth = dim.width() / 2;

        sliderBounds = Dimension.ofInt(dim.xLimit() - getXPadding() - getThumbWidth() / 2 - trackWidth, dim.centerY() - 5, trackWidth, 10);
    }

    protected float getThumbX() {
        return MathHelper.lerp(focusedTime, sliderBounds.xLimit(), sliderBounds.x() + sliderBounds.width() * interpolation);
    }

    protected int getThumbWidth() {
        return 4;
    }

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
