package tektonikal.crystalchams.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.utils.Dimension;
import dev.isxander.yacl3.api.utils.MutableDimension;
import dev.isxander.yacl3.gui.DescriptionWithName;
import dev.isxander.yacl3.gui.OptionDescriptionWidget;
import dev.isxander.yacl3.gui.OptionListWidget;
import dev.isxander.yacl3.gui.YACLScreen;
import dev.isxander.yacl3.gui.tab.ListHolderWidget;
import dev.isxander.yacl3.gui.utils.GuiUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.pack.PackScreen;
import net.minecraft.client.gui.tab.Tab;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tektonikal.crystalchams.CrystalChams;

import java.util.Calendar;
import java.util.function.Consumer;

@Mixin(YACLScreen.class)
public abstract class YACLSCreenMixin extends Screen {

    @Shadow(remap = false)
    @Final
    public YetAnotherConfigLib config;

    protected YACLSCreenMixin(Text title) {
        super(title);
    }

    @Mixin(YACLScreen.CategoryTab.class)
    public abstract static class CategoryTabMixin implements Tab {
        @Mutable
        @Shadow
        @Final
        public ButtonWidget undoButton;
        @Shadow
        @Final
        public ButtonWidget cancelResetButton;
        @Mutable
        @Shadow
        @Final
        public ButtonWidget saveFinishedButton;
        @Unique
        ButtonWidget packsButton;
        @Unique
        ButtonWidget resetAnimButton;
        @Shadow(remap = false)
        @Final
        private YACLScreen screen;
        @Shadow(remap = false)
        private ListHolderWidget<OptionListWidget> optionList;
        @Shadow(remap = false)
        private OptionDescriptionWidget descriptionWidget;

        @Inject(method = "<init>", at = @At("TAIL"), remap = false)
        private void cc$OUGHHHHHHH(YACLScreen screen, ConfigCategory category, ScreenRect tabArea, CallbackInfo ci, @Local(ordinal = 1) int padding, @Local(ordinal = 2) int paddedWidth, @Local MutableDimension<Integer> actionDim) {
            if (CrystalChams.isThisMyScreen(screen)) {
                packsButton = ButtonWidget.builder(Text.literal("Resource Packs"), btn -> {
                    screen.finishOrSave();
                    CrystalChams.mc.setScreen(new PackScreen(CrystalChams.mc.getResourcePackManager(), resourcePackManager -> {
                        CrystalChams.mc.options.refreshResourcePacks(resourcePackManager);
                        CrystalChams.mc.setScreen(screen);
                    }, CrystalChams.mc.getResourcePackDir(), Text.translatable("resourcePack.title")));
                }).position(undoButton.getX(), undoButton.getY()).size(actionDim.width(), actionDim.height()).build();

                resetAnimButton = ButtonWidget.builder(Text.literal("Reset Animation"), btn -> CrystalChams.previewCrystalEntity.age = 0).position(undoButton.getX(), undoButton.getY() - 22).size(actionDim.width(), actionDim.height()).build();

                descriptionWidget = new OptionDescriptionWidget(
                        () -> new ScreenRect(
                                screen.width / 3 * 2 + padding,
                                tabArea.getTop() + padding,
                                paddedWidth,
                                saveFinishedButton.getY() - 2 - padding
                        ),
                        DescriptionWithName.of(Text.of(""), OptionDescription.of())
                );
                undoButton.setPosition(cancelResetButton.getX(), cancelResetButton.getY() - 22);
                //force done button to save
                actionDim = Dimension.ofInt(screen.width / 3 * 2 + screen.width / 6, screen.height - padding - 20, paddedWidth, 20);
                saveFinishedButton = ButtonWidget.builder(Text.literal("Done"), btn -> {
                    CrystalChams.beamProgress = 0;
                            screen.finishOrSave();
                        }).position(actionDim.x() - actionDim.width() / 2, actionDim.y())
                        .size(actionDim.width(), actionDim.height()).build();
            }
        }

        @Inject(method = "forEachChild", at = @At(value = "HEAD"), cancellable = true)
        private void CC$oughhh(Consumer<ClickableWidget> consumer, CallbackInfo ci) {
            if (CrystalChams.isThisMyScreen(screen)) {
                //this is terrible but I don't care anymore
                consumer.accept(optionList);
                //TODO!!!!!!!!!
              consumer.accept(undoButton);
//              consumer.accept(searchField);
                consumer.accept(resetAnimButton);
                consumer.accept(cancelResetButton);
                consumer.accept(packsButton);
                consumer.accept(descriptionWidget);
                consumer.accept(saveFinishedButton);
                ci.cancel();
            }
        }


        @Inject(method = "renderBackground", at = @At("TAIL"))
        private void CC$renderBackground(DrawContext drawContext, CallbackInfo ci) {
            if (Calendar.getInstance().get(Calendar.MONTH) == Calendar.APRIL && Calendar.getInstance().get(Calendar.DAY_OF_MONTH) == 1) {
                GuiUtils.blitGuiTex(drawContext, Identifier.of("crystalchams:custom/bg.png"), 0, 0, 1920, 1080, 1920, 1080, CrystalChams.mc.getWindow().getScaledWidth(), CrystalChams.mc.getWindow().getScaledHeight());
            }
        }
    }
}
