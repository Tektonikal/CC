package tektonikal.crystalchams.mixin;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.utils.Dimension;
import dev.isxander.yacl3.api.utils.MutableDimension;
import dev.isxander.yacl3.gui.*;
import dev.isxander.yacl3.gui.tab.ListHolderWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.screen.pack.PackScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import tektonikal.crystalchams.config.ChamsConfig;

import java.util.function.Consumer;

@Mixin(YACLScreen.CategoryTab.class)
public abstract class CategoryTabMixin {

    @Shadow
    @Final
    private YACLScreen screen;

    @Shadow
    @Final
    public ButtonWidget undoButton;

    @Shadow
    @Final
    public ButtonWidget cancelResetButton;

    @Shadow
    @Final
    public ButtonWidget saveFinishedButton;

    @Shadow
    private ListHolderWidget<OptionListWidget> optionList;

    @Shadow
    private OptionDescriptionWidget descriptionWidget;
    @Unique
    ButtonWidget packsButton;

    @Inject(method = "forEachChild", at = @At(value = "HEAD"), cancellable = true)
    private void CC$oughhh(Consumer<ClickableWidget> consumer, CallbackInfo ci) {
        if (this.screen.config.title().equals(Text.of("Custom End Crystals"))) {
            //this is terrible but i don't care anymore
            consumer.accept(optionList);
            consumer.accept(saveFinishedButton);
            consumer.accept(cancelResetButton);
            consumer.accept(undoButton);
//            consumer.accept(searchField);
            consumer.accept(descriptionWidget);
            consumer.accept(packsButton);
            ci.cancel();
        }
    }

    @Inject(method = "<init>", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void cc$OUGHHHHHHH(YACLScreen screen, ConfigCategory category, ScreenRect tabArea, CallbackInfo ci, int columnWidth, int padding, int paddedWidth, MutableDimension<Integer> actionDim) {
        packsButton = ButtonWidget.builder(Text.literal("Resource Packs"), btn -> {
                    screen.finishOrSave();
                    MinecraftClient.getInstance().setScreen(new PackScreen(MinecraftClient.getInstance().getResourcePackManager(), resourcePackManager -> {
                        MinecraftClient.getInstance().options.refreshResourcePacks(resourcePackManager);
                        MinecraftClient.getInstance().setScreen(screen);
                    }, MinecraftClient.getInstance().getResourcePackDir(), Text.translatable("resourcePack.title")));
                })
                .position(actionDim.x() - actionDim.width() / 2, undoButton.getY() - 22)
                .size(actionDim.width(), actionDim.height())
                .build();
        descriptionWidget.setOptionDescription(DescriptionWithName.of(Text.of(""), OptionDescription.of()));
    }
}
