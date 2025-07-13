package tektonikal.crystalchams.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.utils.Dimension;
import dev.isxander.yacl3.api.utils.MutableDimension;
import dev.isxander.yacl3.gui.DescriptionWithName;
import dev.isxander.yacl3.gui.OptionDescriptionWidget;
import dev.isxander.yacl3.gui.OptionListWidget;
import dev.isxander.yacl3.gui.YACLScreen;
import dev.isxander.yacl3.gui.tab.ListHolderWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.pack.PackScreen;
import net.minecraft.client.gui.tab.Tab;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tektonikal.crystalchams.CrystalChams;
import tektonikal.crystalchams.config.ChamsConfig;
import tektonikal.crystalchams.config.MutableOptionGroupImpl;
import tektonikal.crystalchams.config.RenderMode;
import tektonikal.crystalchams.interfaces.ElementListWidgetExtInterface;

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

        @Shadow
        @Final
        private ScreenRect rightPaneDim;

        @Inject(method = "<init>", at = @At("TAIL"), remap = false)
        private void cc$OUGHHHHHHH(YACLScreen screen, ConfigCategory category, ScreenRect tabArea, CallbackInfo ci, @Local(ordinal = 1) int padding, @Local(ordinal = 2) int paddedWidth, @Local MutableDimension<Integer> actionDim) {
            if (this.screen.config.title().equals(Text.of("Custom End Crystals"))) {
                packsButton = ButtonWidget.builder(Text.literal("Resource Packs"), btn -> {
                    screen.finishOrSave();
                    MinecraftClient.getInstance().setScreen(new PackScreen(MinecraftClient.getInstance().getResourcePackManager(), resourcePackManager -> {
                        MinecraftClient.getInstance().options.refreshResourcePacks(resourcePackManager);
                        MinecraftClient.getInstance().setScreen(screen);
                    }, MinecraftClient.getInstance().getResourcePackDir(), Text.translatable("resourcePack.title")));
                }).position(undoButton.getX(), undoButton.getY()).size(actionDim.width(), actionDim.height()).build();

                resetAnimButton = ButtonWidget.builder(Text.literal("Reset Animation"), btn -> CrystalChams.entity.age = 0).position(undoButton.getX(), undoButton.getY() - 22).size(actionDim.width(), actionDim.height()).build();

                descriptionWidget = new OptionDescriptionWidget(
                        () -> new ScreenRect(
                                screen.width / 3 * 2 + padding,
                                tabArea.getTop() + padding,
                                paddedWidth,
                                saveFinishedButton.getY() - 2 - padding
                        ),
                        DescriptionWithName.of(Text.of(""), OptionDescription.of())
                );
                //force done button to save
                actionDim = Dimension.ofInt(screen.width / 3 * 2 + screen.width / 6, screen.height - padding - 20, paddedWidth, 20);
                saveFinishedButton = ButtonWidget.builder(Text.literal("Done"), btn -> {
                            screen.finishOrSave();
                            ChamsConfig.CONFIG.save();
                        }).position(actionDim.x() - actionDim.width() / 2, actionDim.y())
                        .size(actionDim.width(), actionDim.height()).build();
            }
        }

        @Inject(method = "forEachChild", at = @At(value = "HEAD"), cancellable = true)
        private void CC$oughhh(Consumer<ClickableWidget> consumer, CallbackInfo ci) {
            if (this.screen.config.title().equals(Text.of("Custom End Crystals"))) {
                //this is terrible but I don't care anymore
                consumer.accept(optionList);
//              consumer.accept(undoButton);
//              consumer.accept(searchField);
                consumer.accept(resetAnimButton);
                consumer.accept(cancelResetButton);
                consumer.accept(packsButton);
                consumer.accept(descriptionWidget);
                consumer.accept(saveFinishedButton);
                ci.cancel();
            }
        }

        @Inject(method = "updateButtons", at = @At("TAIL"), remap = false)
        private void CC$updateButtons(CallbackInfo ci) {
            if (this.screen.config.title().equals(Text.of("Custom End Crystals"))) {
                cancelResetButton.setTooltip(null);
            }
        }

        @Inject(method = "renderBackground", at = @At("TAIL"))
        private void CC$renderBackground(DrawContext drawContext, CallbackInfo ci) {
            double amount = optionList.getList().getScrollAmount();
            if (screen.config.title().equals(Text.of("Custom End Crystals"))) {
                int currentTab = screen.tabNavigationBar != null
                        ? screen.tabNavigationBar.getTabs().indexOf(screen.tabManager.getCurrentTab())
                        : 0;
                if (currentTab == -1)
                    currentTab = 0;
                screen.config.categories().get(currentTab).groups().forEach(group -> {
                    if (group instanceof MutableOptionGroupImpl) {
                        group.options().forEach(option -> {
                            if (option.name().equals(Text.of("Render Mode"))) {
                                Option<?> optionToAdd = null;
                                if (option.equals(ChamsConfig.o_baseRenderLayer)) {
                                    optionToAdd = ChamsConfig.o_baseCulling;
                                } else if (option.equals(ChamsConfig.o_coreRenderLayer)) {
                                    optionToAdd = ChamsConfig.o_coreCulling;
                                } else if (option.equals(ChamsConfig.o_frame1RenderLayer)) {
                                    optionToAdd = ChamsConfig.o_frame1Culling;
                                } else if (option.equals(ChamsConfig.o_frame2RenderLayer)) {
                                    optionToAdd = ChamsConfig.o_frame2Culling;
                                }
                                if (((RenderMode) option.stateManager().get()).canCull()) {
                                    if (!((MutableOptionGroupImpl) group).options.contains(optionToAdd)) {
                                        ((MutableOptionGroupImpl) group).options.add(optionToAdd);
                                        optionList.getList().refreshOptions();
                                        optionList.getList().setScrollAmount(amount);
                                        ((ElementListWidgetExtInterface) optionList.getList()).setSmoothScrollAmount(amount);
                                    }
                                } else {
                                    if (((MutableOptionGroupImpl) group).options.contains(optionToAdd)) {
                                        ((MutableOptionGroupImpl) group).options.remove(optionToAdd);
                                        optionList.getList().refreshOptions();
                                        optionList.getList().setScrollAmount(amount);
                                        ((ElementListWidgetExtInterface) optionList.getList()).setSmoothScrollAmount(amount);
                                    }
                                }
                            }
                            if (option.equals(ChamsConfig.o_coreRainbow)) {
                                if ((boolean) option.stateManager().get()) {
                                    if (!((MutableOptionGroupImpl) group).options.contains(ChamsConfig.o_coreRainbowSpeed)) {
                                        ((MutableOptionGroupImpl) group).options.add(ChamsConfig.o_coreRainbowSpeed);
                                        ((MutableOptionGroupImpl) group).options.add(ChamsConfig.o_coreRainbowDelay);
                                        ((MutableOptionGroupImpl) group).options.add(ChamsConfig.o_coreRainbowBrightness);
                                        ((MutableOptionGroupImpl) group).options.add(ChamsConfig.o_coreRainbowSaturation);
                                        optionList.getList().refreshOptions();
                                        optionList.getList().setScrollAmount(amount);
                                        ((ElementListWidgetExtInterface) optionList.getList()).setSmoothScrollAmount(amount);
                                    }
                                } else {
                                    if (((MutableOptionGroupImpl) group).options.contains(ChamsConfig.o_coreRainbowSpeed)) {
                                        ((MutableOptionGroupImpl) group).options.remove(ChamsConfig.o_coreRainbowSpeed);
                                        ((MutableOptionGroupImpl) group).options.remove(ChamsConfig.o_coreRainbowDelay);
                                        ((MutableOptionGroupImpl) group).options.remove(ChamsConfig.o_coreRainbowBrightness);
                                        ((MutableOptionGroupImpl) group).options.remove(ChamsConfig.o_coreRainbowSaturation);
                                        optionList.getList().refreshOptions();
                                        optionList.getList().setScrollAmount(amount);
                                        ((ElementListWidgetExtInterface) optionList.getList()).setSmoothScrollAmount(amount);
                                    }
                                }
                            }
                        });
                    }
                });
            //TODO: fuck around with these numbers a bit more later
            CrystalChams.crystalRotX = (float) CrystalChams.ease(CrystalChams.crystalRotX, Math.atan((((rightPaneDim.getLeft() + rightPaneDim.getRight()) / 2F) - (MinecraftClient.getInstance().mouse.getX() * MinecraftClient.getInstance().getWindow().getScaledWidth() / MinecraftClient.getInstance().getWindow().getWidth())) / 40F), 15F);
            CrystalChams.crystalRotY = (float) CrystalChams.ease(CrystalChams.crystalRotY, Math.atan((((rightPaneDim.getTop() + rightPaneDim.getBottom()) / 2F) - (MinecraftClient.getInstance().mouse.getY() * MinecraftClient.getInstance().getWindow().getScaledHeight() / MinecraftClient.getInstance().getWindow().getHeight())) / 40F), 15F);
            if(Float.isNaN(CrystalChams.crystalRotX)) {
                CrystalChams.crystalRotX = 0;
            }
            if(Float.isNaN(CrystalChams.crystalRotY)) {
                CrystalChams.crystalRotY = 0;
            }
            drawContext.getMatrices().push();
            drawContext.getMatrices().translate((rightPaneDim.getLeft() + rightPaneDim.getRight()) / 2F, (rightPaneDim.getTop() + rightPaneDim.getBottom()) / 2F, 100.0);
            drawContext.getMatrices().scale(100 - (MinecraftClient.getInstance().options.getGuiScale().getValue() * 12.5F), 100 - (MinecraftClient.getInstance().options.getGuiScale().getValue() * 12.5F), -(100 - (MinecraftClient.getInstance().options.getGuiScale().getValue() * 12.5F)));
            drawContext.getMatrices().multiply(RotationAxis.POSITIVE_Z.rotation((float) Math.PI));
            drawContext.getMatrices().multiply(RotationAxis.POSITIVE_X.rotation(CrystalChams.crystalRotY * 25.0F * (float) (Math.PI / 180.0)));
            drawContext.getMatrices().multiply(RotationAxis.NEGATIVE_Y.rotation(CrystalChams.crystalRotX * 35.0F * (float) (Math.PI / 180.0)));
            MinecraftClient.getInstance().getEntityRenderDispatcher().getRenderer(CrystalChams.entity).render(CrystalChams.entity, 0, ((RenderTickCounter.Dynamic) MinecraftClient.getInstance().getRenderTickCounter()).tickDelta, drawContext.getMatrices(), MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers(), 255);
            drawContext.draw();
            drawContext.getMatrices().pop();
//            drawContext.fill(rightPaneDim.getLeft(), rightPaneDim.getTop(), rightPaneDim.getRight(), rightPaneDim.getBottom(), 0x80FFFFFF);
            }
        }
    }
}
