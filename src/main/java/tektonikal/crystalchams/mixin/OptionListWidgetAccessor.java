package tektonikal.crystalchams.mixin;


import com.google.common.collect.ImmutableList;
import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionGroup;
import dev.isxander.yacl3.gui.*;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tektonikal.crystalchams.config.LinkedOptionImpl;

import java.util.List;

@Mixin(value = OptionListWidget.class)
public interface OptionListWidgetAccessor {

    @Accessor(remap = false)
    YACLScreen getYaclScreen();

    @Mixin(value = OptionListWidget.OptionEntry.class)
    abstract class OptionListWidgetEntryMixin {
        @Shadow(remap = false)
        @Final
        public AbstractWidget widget;
        @Shadow(remap = false)
        @Final
        private TextScaledButtonWidget resetButton;
        @Shadow(remap = false)
        @Final
        public Option<?> option;
        @Unique
        private TextScaledButtonWidget applyAllButton;

        protected OptionListWidgetEntryMixin(TextScaledButtonWidget applyAllButton) {
            this.applyAllButton = applyAllButton;
        }

        @Inject(method = "<init>", at = @At("TAIL"), remap = false)
        private void onInit(OptionListWidget this$0, Option<?> option, ConfigCategory category, OptionGroup group, OptionListWidget.GroupSeparatorEntry groupSeparatorEntry, AbstractWidget widget, CallbackInfo ci) {
            if (option instanceof LinkedOptionImpl<?>) {
                this.widget.setDimension(this.widget.getDimension().expanded(-20, 0));
                this.applyAllButton = new TextScaledButtonWidget(((OptionListWidgetAccessor) this$0).getYaclScreen(), widget.getDimension().xLimit(), -50, 20, 20, 2f, Text.literal("⇛"), button -> {
                    ((LinkedOptionImpl<?>) option).syncLinkedOptions();
                    button.active = false;
                });
                this.applyAllButton.setTooltip(Tooltip.of(Text.of("Apply To All")));
                this.applyAllButton.active = !((LinkedOptionImpl<?>) option).linkedOptionsSynced();
            }
        }

        @Inject(method = "render", at = @At("TAIL"))
        private void onRender(DrawContext graphics, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta, CallbackInfo ci) {
            if (applyAllButton != null) {
                applyAllButton.setY(y);
                //not the greatest of ways to do it, but whatever
                applyAllButton.active = !((LinkedOptionImpl<?>) option).linkedOptionsSynced();
                applyAllButton.render(graphics, mouseX, mouseY, tickDelta);
            }

        }

        @Inject(method = "selectableChildren", at = @At("HEAD"), cancellable = true)
        private void onSelectableChildren(CallbackInfoReturnable<List<? extends Selectable>> cir) {
            if (option instanceof LinkedOptionImpl<?>) {
                cir.cancel();
                cir.setReturnValue(ImmutableList.of(widget, applyAllButton, resetButton));
            }
        }
        @Inject(method = "children", at = @At("HEAD"), cancellable = true)
        private void onChildren(CallbackInfoReturnable<List<? extends Selectable>> cir) {
            if (option instanceof LinkedOptionImpl<?>) {
                cir.cancel();
                cir.setReturnValue(ImmutableList.of(widget, applyAllButton, resetButton));
            }
        }
    }
}
