package tektonikal.crystalchams.config;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.isxander.yacl3.api.ListOptionEntry;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionFlag;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.utils.OptionUtils;
import dev.isxander.yacl3.gui.YACLScreen;
import dev.isxander.yacl3.gui.utils.GuiUtils;
import dev.isxander.yacl3.impl.ListOptionEntryImpl;
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
import tektonikal.crystalchams.mixin.yacl.CategoryTabAccessor;

import java.util.HashSet;
import java.util.Set;

import static tektonikal.crystalchams.CrystalChams.drawPreviewCrystal;

public class SecondaryYACLScreen extends YACLScreen {
    public static float prog = 0;
    public static boolean closing = false;
    Screen parent;
    public boolean draggin = false;


    public SecondaryYACLScreen(YetAnotherConfigLib config, Screen parent) {
        super(config, parent);
        this.parent = parent;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        Window w = MinecraftClient.getInstance().getWindow();
        parent.render(context, closing ? mouseX : -1, closing ? mouseY : -1, delta);
        context.getMatrices().push();
        context.getMatrices().translate(0, w.getScaledHeight() - (w.getScaledHeight() * prog), 0);
        super.render(context, mouseX, mouseY, delta);
        drawPreviewCrystal(context, ((CategoryTabAccessor) this.tabManager.getCurrentTab()).rightPaneDim(), this);
        context.getMatrices().pop();
        prog = (float) CrystalChams.ease(prog, closing ? 0 : 1, 15F);
        if (prog <= 0.0025 && closing) {
            close();
        }
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        if (this.client.world == null) {
            this.renderPanoramaBackground(context, delta);
        }
        this.applyBlur(delta);
        this.renderDarkening(context);
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
    public void tick() {
        super.tick();
        CrystalChams.previewCrystalEntity.age++;
        CrystalChams.previewCrystalEntity.endCrystalAge++;
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
        //BAD BAD BAD
        for (ListOptionEntry<ModelPartOptions> modelPartOptionsListOptionEntry : ChamsConfig.o_frameList.options()) {
            ModelPartController controller = (ModelPartController) ((ListOptionEntryImpl.EntryController) (modelPartOptionsListOptionEntry.controller())).controller();
            controller.hovered = false;
        }
        prog = 0;
        closing = false;
        client.setScreen(parent);
    }

    public boolean contains(double x, double y, ScreenRect r) {
        return x >= r.getLeft() && x < r.getRight() && y >= r.getTop() && y < r.getBottom();
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (draggin) {
            CrystalChams.crystalTargetDraggedRotX += (float) deltaX;
            CrystalChams.crystalTargetDraggedRotY -= (float) deltaY;
        }
        if (closing) {
            return parent.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
        } else {
            return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        ScreenRect r = ((CategoryTabAccessor) this.tabManager.getCurrentTab()).rightPaneDim();
        if (contains(mouseX, mouseY, r)) {
            if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
                draggin = true;
            }
            if (button == GLFW.GLFW_MOUSE_BUTTON_MIDDLE) {
                CrystalChams.crystalTargetDraggedRotX = 0;
                CrystalChams.crystalTargetDraggedRotY = 0;
            }
        }
        if (closing) {
            return parent.mouseClicked(mouseX, mouseY, button);
        } else {
            return super.mouseClicked(mouseX, mouseY, button);
        }
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        draggin = false;
        if (closing) {
            return parent.mouseReleased(mouseX, mouseY, button);
        } else {
            return super.mouseReleased(mouseX, mouseY, button);
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE && this.shouldCloseOnEsc() && !closing) {
            closing = true;
            return true;
        } else {
            if (closing) {
                if (keyCode == GLFW.GLFW_KEY_ESCAPE && this.shouldCloseOnEsc()) {
                    close();
                }
                return parent.keyPressed(keyCode, scanCode, modifiers);
            } else {
                return super.keyPressed(keyCode, scanCode, modifiers);
            }
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        ScreenRect r = ((CategoryTabAccessor) this.tabManager.getCurrentTab()).rightPaneDim();
        if (contains(mouseX, mouseY, r)) {
            ChamsConfig.o_previewScale.stateManager().set(MathHelper.clamp((float) (ChamsConfig.o_previewScale.pendingValue() + verticalAmount * 0.25), 0, 2));
        }
        if (closing) {
            return parent.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }
}
