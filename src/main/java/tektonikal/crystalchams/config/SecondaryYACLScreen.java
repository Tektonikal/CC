package tektonikal.crystalchams.config;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionFlag;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.utils.OptionUtils;
import dev.isxander.yacl3.gui.YACLScreen;
import dev.isxander.yacl3.gui.utils.GuiUtils;
import dev.isxander.yacl3.impl.utils.YACLConstants;
import dev.isxander.yacl3.platform.YACLPlatform;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.util.Window;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.lwjgl.glfw.GLFW;
import tektonikal.crystalchams.CrystalChams;
import tektonikal.crystalchams.mixin.CategoryTabAccessor;

import java.util.HashSet;
import java.util.Set;

import static tektonikal.crystalchams.CrystalChams.drawPreviewCrystal;

public class SecondaryYACLScreen extends YACLScreen {
    public static float prog = 0;
    public static boolean closing = false;
    Screen parent;

    public SecondaryYACLScreen(YetAnotherConfigLib config, Screen parent) {
        super(config, parent);
        this.parent = parent;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        Window w = MinecraftClient.getInstance().getWindow();
//        context.enableScissor(0, MathHelper.ceil(w.getScaledHeight() * prog), w.getScaledWidth(), w.getScaledHeight());
        parent.render(context, closing ? mouseX : -1, closing ? mouseY : -1, delta);
//        context.disableScissor();
        context.getMatrices().push();
        context.getMatrices().translate(0, w.getScaledHeight() - (w.getScaledHeight() * prog), 0);
//        context.enableScissor(0, 0, w.getScaledWidth(), MathHelper.ceil(w.getScaledHeight() * prog));
        super.render(context, mouseX, mouseY, delta);
        drawPreviewCrystal(context, ((CategoryTabAccessor) this.tabManager.getCurrentTab()).rightPaneDim(), this);
//        context.disableScissor();
        context.getMatrices().pop();
        prog = (float) CrystalChams.ease(prog, closing ? 0 : 1, 1F);
        if (prog <= 0.0025 && closing) {
            close();
        }
    }
    private static final Identifier DARKER_BG = YACLPlatform.mcRl("textures/gui/menu_list_background.png");

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        ScreenRect rightPaneDim = ((CategoryTabAccessor) this.tabManager.getCurrentTab()).rightPaneDim();
        if (this.client.world == null) {
            this.renderPanoramaBackground(context, delta);
        }

        this.applyBlur(delta);
        this.renderDarkening(context);
//        RenderSystem.enableBlend();
//        GuiUtils.blitGuiTex(context, DARKER_BG, rightPaneDim.getLeft(), rightPaneDim.getTop(), rightPaneDim.getRight() + 2, rightPaneDim.getBottom() + 2, rightPaneDim.width() + 2, rightPaneDim.height() + 2, 32, 32);32
//        context.getMatrices().push();
//        context.getMatrices().translate(rightPaneDim.getLeft(), rightPaneDim.getTop(), 0);
//        context.getMatrices().multiply(RotationAxis.POSITIVE_Z.rotationDegrees(90), 0, 0, 1);
//        GuiUtils.blitGuiTex(context, CreateWorldScreen.FOOTER_SEPARATOR_TEXTURE, 0, 0, 0f, 0f, rightPaneDim.height() + 1, 2, 32, 2);
//        context.getMatrices().pop();
//        RenderSystem.disableBlend();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE && this.shouldCloseOnEsc()) {
            closing = true;
            return true;
        } else {
            return super.keyPressed(keyCode, scanCode, modifiers);
        }
    }

    @Override
    public void tick() {
        //TODO: deduplicate this
        super.tick();
        CrystalChams.previewCrystalEntity.age++;
        CrystalChams.previewCrystalEntity.endCrystalAge++;
    }

    @Override
    public void finishOrSave() {
        if (pendingChanges()) {
            super.finishOrSave();
        } else {
            closing = true;
        }
    }

    @Override
    public void cancelOrReset() {
        if (pendingChanges()) { // if pending changes, button acts as a cancel button
            OptionUtils.forEachOptions(config, Option::forgetPendingValue);
            closing = true;
        } else { // if not, button acts as a reset button
            OptionUtils.forEachOptions(config, Option::requestSetDefault);
        }
    }

    @Override
    public void close() {
        prog = 0;
        closing = false;
        client.setScreen(parent);
    }
}
