package tektonikal.crystalchams.mixin.yacl;

import com.llamalad7.mixinextras.sugar.Local;
import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.utils.MutableDimension;
import dev.isxander.yacl3.gui.OptionDescriptionWidget;
import dev.isxander.yacl3.gui.OptionListWidget;
import dev.isxander.yacl3.gui.WidgetAndType;
import dev.isxander.yacl3.gui.YACLScreen;
import dev.isxander.yacl3.gui.utils.GuiUtils;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.tabs.Tab;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.packs.PackSelectionScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tektonikal.crystalchams.CrystalChams;
import tektonikal.crystalchams.config.SecondaryYACLScreen;

import java.util.Calendar;
import java.util.function.Consumer;

@Mixin(YACLScreen.class)
public abstract class YACLSCreenMixin extends Screen {

    @Shadow(remap = false)
    @Final
    public YetAnotherConfigLib config;

    protected YACLSCreenMixin(Component title) {
        super(title);
    }

    @Mixin(value = YACLScreen.CategoryTab.class, remap = false)
    public abstract static class CategoryTabMixin implements Tab {
        @Mutable
        @Shadow
        @Final
        public Button undoButton;
        @Shadow
        @Final
        public Button cancelResetButton;
        @Mutable
        @Shadow
        @Final
        public Button saveFinishedButton;
        @Unique
        Button packsButton;
        @Unique
        Button resetAnimButton;
        @Shadow(remap = false)
        @Final
        private YACLScreen screen;
        @Shadow(remap = false)
        private WidgetAndType<OptionListWidget> optionList;
        @Shadow(remap = false)
        private OptionDescriptionWidget descriptionWidget;

        @Inject(method = "<init>", at = @At("TAIL"), remap = false)
        private void cc$OUGHHHHHHH(YACLScreen screen, ConfigCategory category, ScreenRectangle tabArea, CallbackInfo ci, @Local(ordinal = 1) int padding, @Local(ordinal = 2) int paddedWidth, @Local MutableDimension<Integer> actionDim) {
            //TODO
            if ((CrystalChams.mc.screen instanceof YACLScreen || CrystalChams.mc.screen instanceof SecondaryYACLScreen)) {
                packsButton = Button.builder(Component.literal("Resource Packs"), btn -> {
                    screen.finishOrSave();
                    CrystalChams.mc.setScreen(new PackSelectionScreen(CrystalChams.mc.getResourcePackRepository(), resourcePackManager -> {
                        CrystalChams.mc.options.updateResourcePacks(resourcePackManager);
                        CrystalChams.mc.setScreen(screen);
                    }, CrystalChams.mc.getResourcePackDirectory(), Component.translatable("resourcePack.title")));
                }).pos(undoButton.getX(), undoButton.getY()).size(actionDim.width(), actionDim.height()).build();

                resetAnimButton = Button.builder(Component.literal("Reset Animation"), btn -> CrystalChams.previewCrystalEntity.tickCount = 0).pos(undoButton.getX(), undoButton.getY() - 22).size(actionDim.width(), actionDim.height()).build();

                this.descriptionWidget = new OptionDescriptionWidget(
                        () -> new ScreenRectangle(
                                screen.width / 3 * 2 + padding,
                                tabArea.top() + padding,
                                paddedWidth,
                                packsButton.getY() - 1 - tabArea.top() - padding * 2
                        ),
                        null
                );
                undoButton.setPosition(cancelResetButton.getX(), cancelResetButton.getY() - 22);
                //force done button to save
//                actionDim = Dimension.ofInt(screen.width / 3 * 2 + screen.width / 6, screen.height - padding - 20, paddedWidth, 20);
//                saveFinishedButton = ButtonWidget.builder(Text.literal("Done"), btn -> {
//                    CrystalChams.beamProgress = 0;
//                            screen.finishOrSave();
//                        }).position(actionDim.x() - actionDim.width() / 2, actionDim.y())
//                        .size(actionDim.width(), actionDim.height()).build();
            }
        }

        @Inject(method = "visitChildren", at = @At(value = "HEAD"), cancellable = true)
        private void CC$oughhh(Consumer<AbstractWidget> consumer, CallbackInfo ci) {
            if ((CrystalChams.mc.screen instanceof YACLScreen || CrystalChams.mc.screen instanceof SecondaryYACLScreen)) {
                //this is terrible but I don't care anymore
                consumer.accept(optionList.getWidget());
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
        private void CC$renderBackground(GuiGraphicsExtractor graphics, CallbackInfo ci) {
            if (Calendar.getInstance().get(Calendar.MONTH) == Calendar.APRIL && Calendar.getInstance().get(Calendar.DAY_OF_MONTH) == 1) {
                graphics.blit(Identifier.parse("crystalchams:custom/bg.png"), 0, 0, 1920, 1080, 1920, 1080, CrystalChams.mc.getWindow().getGuiScaledWidth(), CrystalChams.mc.getWindow().getGuiScaledHeight());
            }
        }
    }
}
