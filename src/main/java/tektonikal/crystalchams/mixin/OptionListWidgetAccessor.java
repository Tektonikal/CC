package tektonikal.crystalchams.mixin;


import com.google.common.collect.ImmutableList;
import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.ListOptionEntry;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionGroup;
import dev.isxander.yacl3.gui.*;
import dev.isxander.yacl3.impl.ListOptionEntryImpl;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tektonikal.crystalchams.CrystalChams;
import tektonikal.crystalchams.OptionGroups;
import tektonikal.crystalchams.config.ChamsConfig;
import tektonikal.crystalchams.config.EvilOption;
import tektonikal.crystalchams.config.ModelPartController;
import tektonikal.crystalchams.config.ModelPartOptions;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

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
            if (option instanceof EvilOption<?> && ((EvilOption<?>) option).group() != null) {
                this.widget.setDimension(this.widget.getDimension().expanded(-20, 0));
                this.applyAllButton = new TextScaledButtonWidget(((OptionListWidgetAccessor) this$0).getYaclScreen(), widget.getDimension().xLimit(), -50, 20, 20, 2f, Text.literal("⇛"), button -> {
                    syncLinkedOptions(((EvilOption<?>) option).group());
                    button.active = false;
                });
                this.applyAllButton.active = optionsSynced() && option.available();
            }
        }

        //Sigh.
        @Unique
        private boolean optionsSynced() {
            //TODO
            List<EvilOption> list = getLinkedOptions(((EvilOption<?>) option).group()).stream().filter(evilOption -> !evilOption.equals(option)).toList();
            for (EvilOption evilOption : list) {
                if (!evilOption.stateManager().get().equals(option.stateManager().get())) {
                    return false;
                }
            }
            return true;
        }

        @Unique
        private void syncLinkedOptions(OptionGroups group) {
            getLinkedOptions(group).stream().filter(evilOption -> !evilOption.equals(option)).forEach(option -> {
                if (group == OptionGroups.RENDER) {
                    if (this.option.equals(ChamsConfig.o_baseRenderMode)) {
                        option.requestSet(this.option.stateManager().get() != CrystalChams.BaseRenderMode.NEVER);
                        return;
                    }
                    if (option.equals(ChamsConfig.o_baseRenderMode)) {
                        option.requestSet((boolean) this.option.stateManager().get() ? CrystalChams.BaseRenderMode.DEFAULT : CrystalChams.BaseRenderMode.NEVER);
                        return;
                    }
                    option.requestSet(this.option.stateManager().get());
                } else {
                    option.requestSet(this.option.stateManager().get());
                }

            });
        }

        @Unique
        private static List<EvilOption> getLinkedOptions(OptionGroups group) {
            //THIS IS EVEN WORSE KILLING MYSELF
            List<EvilOption> options = new java.util.ArrayList<>(List.of());
            Arrays.stream(ChamsConfig.class.getDeclaredFields()).filter(field -> field.getName().startsWith("o_") && !field.getName().equals("o_frameList")).forEach(input -> {
                try {
                    options.add((EvilOption) input.get(null));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            });
            for (ListOptionEntry<ModelPartOptions> entry : ChamsConfig.o_frameList.options()) {
                ModelPartController controller = (ModelPartController) ((ListOptionEntryImpl.EntryController) (entry.controller())).controller();
                Arrays.stream(controller.getClass().getDeclaredFields()).filter(field -> field.getName().startsWith("o_") && !field.getName().equals("o_frameList")).forEach(input -> {
                    try {
                        options.add((EvilOption) input.get(controller));
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
            return options.stream().filter(evilOption -> evilOption.group() == group).toList();
        }

        @Inject(method = "render", at = @At("TAIL"))
        private void onRender(DrawContext graphics, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta, CallbackInfo ci) {
            if (applyAllButton != null) {
                applyAllButton.setY(y);
                //not the greatest of ways to do it, but whatever
                applyAllButton.active = !optionsSynced() && option.available();
                applyAllButton.setTooltip(applyAllButton.active ? Tooltip.of(Text.of("Apply To All")) : null);
                applyAllButton.render(graphics, mouseX, mouseY, tickDelta);
            }

        }

        @Inject(method = "selectableChildren", at = @At("HEAD"), cancellable = true)
        private void onSelectableChildren(CallbackInfoReturnable<List<? extends Selectable>> cir) {
            if (option instanceof EvilOption<?> && ((EvilOption<?>) option).group() != null) {
                cir.cancel();
                cir.setReturnValue(ImmutableList.of(widget, applyAllButton, resetButton));
            }
        }

        @Inject(method = "children", at = @At("HEAD"), cancellable = true)
        private void onChildren(CallbackInfoReturnable<List<? extends Selectable>> cir) {
            if (option instanceof EvilOption<?> && ((EvilOption<?>) option).group() != null) {
                cir.cancel();
                cir.setReturnValue(ImmutableList.of(widget, applyAllButton, resetButton));
            }
        }
    }
}
