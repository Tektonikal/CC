package tektonikal.crystalchams.config;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder;
import dev.isxander.yacl3.api.utils.Dimension;
import dev.isxander.yacl3.gui.AbstractWidget;
import dev.isxander.yacl3.gui.YACLScreen;
import dev.isxander.yacl3.gui.controllers.ControllerWidget;
import dev.isxander.yacl3.gui.utils.GuiUtils;
import dev.isxander.yacl3.impl.ListOptionEntryImpl;
import dev.isxander.yacl3.impl.controller.ColorControllerBuilderImpl;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import tektonikal.crystalchams.CrystalChams;
import tektonikal.crystalchams.OptionGroups;
import tektonikal.crystalchams.stupidfuckingboilerplate.CustomFloatSliderControllerBuilder;
import tektonikal.crystalchams.stupidfuckingboilerplate.CustomIntegerSliderControllerBuilder;

import java.awt.*;
import java.util.Arrays;

public class ModelPartController implements Controller<ModelPartOptions> {
    public float alphaMultiplier = 1;
    public boolean hovered;
    private final Option<ModelPartOptions> option;
    public YetAnotherConfigLib subScreen;
    public EvilOption<Boolean> o_render;
    public EvilOption<Float> o_offset;
    public EvilOption<Float> o_rotationSpeed;
    public EvilOption<Float> o_bounceHeight;
    public EvilOption<Float> o_bounceSpeed;
    public EvilOption<Float> o_tickDelay;
    public EvilOption<Float> o_scale;
    public EvilOption<Color> o_color;
    public EvilOption<Float> o_alpha;
    public EvilOption<Integer> o_lightLevel;
    public EvilOption<RenderMode> o_renderLayer;
    public EvilOption<Boolean> o_culling;
    public EvilOption<Boolean> o_funnyOption;
    public EvilOption<Boolean> o_funnierOption;
    public EvilOption<Boolean> o_rainbow;
    public EvilOption<Float> o_rainbowSpeed;
    public EvilOption<Integer> o_rainbowDelay;
    public EvilOption<Float> o_rainbowSaturation;
    public EvilOption<Float> o_rainbowBrightness;

    public EvilOption<Boolean> o_animation;

    public EvilOption<Boolean> o_animateVerticalOffset;
//    public EvilOption<Float> o_startingVerticalOffset;
//    public EvilOption<Float> verticalOffsetAnimationDuration;
//    public EvilOption<Float> verticalOffsetAnimationDelay;
//    public EvilOption<Easings> verticalOffsetEasing;


    public ModelPartController(Option<ModelPartOptions> option) {
        this.option = option;
        o_render = CrystalChams.createBooleanOption("Render Frame", "", StateManager.createSimple(true, () -> option.binding().getValue().render, newVal -> option.binding().getValue().render = newVal));
        o_offset = EvilOption.<Float>createBuilder().name(Text.of("Vertical Offset")).controller(floatOption -> CustomFloatSliderControllerBuilder.create(floatOption).range(-2.5f, 2.5f).step(0.1f).formatValue(val -> Text.of(String.format("%.1f", val).replace(".0", "") + (Math.abs(val) == 1 ? " block" : " blocks")))).stateManager(StateManager.createSimple(0F, () -> option.binding().getValue().offset, newVal -> option.binding().getValue().offset = newVal)).build();
        o_rotationSpeed = EvilOption.<Float>createBuilder().name(Text.of("Rotation Speed")).controller(floatOption -> CustomFloatSliderControllerBuilder.create(floatOption).range(-15f, 15f).step(0.1f).formatValue(val -> Text.of(String.format("%.1f", val) + "x"))).stateManager(StateManager.createSimple(1F, () -> option.binding().getValue().rotationSpeed, newVal -> option.binding().getValue().rotationSpeed = newVal)).build();
        o_bounceHeight = EvilOption.<Float>createBuilder().name(Text.of("Bounce Height")).controller(floatOption -> CustomFloatSliderControllerBuilder.create(floatOption).range(0f, 2.5f).step(0.05f).formatValue(val -> Text.of(String.format("%.2f", val) + "x"))).stateManager(StateManager.createSimple(1F, () -> option.binding().getValue().bounceHeight, newVal -> option.binding().getValue().bounceHeight = newVal)).build();
        o_bounceSpeed = EvilOption.<Float>createBuilder().name(Text.of("Bounce Speed")).controller(floatOption -> CustomFloatSliderControllerBuilder.create(floatOption).range(0f, 2.5f).step(0.05f).formatValue(val -> Text.of(String.format("%.2f", val) + "x"))).stateManager(StateManager.createSimple(1F, () -> option.binding().getValue().bounceSpeed, newVal -> option.binding().getValue().bounceSpeed = newVal)).build();
        o_tickDelay = EvilOption.<Float>createBuilder().name(Text.of("Tick Delay")).controller(floatOption -> CustomFloatSliderControllerBuilder.create(floatOption).range(-20f, 20f).step(0.1f).formatValue(val -> Text.of(String.format("%.1f", val) + " ticks"))).stateManager(StateManager.createSimple(0F, () -> option.binding().getValue().tickDelay, newVal -> option.binding().getValue().tickDelay = newVal)).build();
        o_scale = EvilOption.<Float>createBuilder().name(Text.of("Scale")).controller(floatOption -> CustomFloatSliderControllerBuilder.create(floatOption).range(0f, 2f).step(0.05f).formatValue(val -> Text.of(String.format("%.2f", val) + "x"))).stateManager(StateManager.createSimple(1F, () -> option.binding().getValue().scale, newVal -> option.binding().getValue().scale = newVal)).build();
        o_color = EvilOption.<Color>createBuilder().name(Text.of("Color")).controller(ColorControllerBuilderImpl::new).stateManager(StateManager.createSimple(new Color(255, 255, 255), () -> option.binding().getValue().color, newVal -> option.binding().getValue().color = newVal)).build();
        o_alpha = EvilOption.<Float>createBuilder().name(Text.of("Opacity")).controller(floatOption -> CustomFloatSliderControllerBuilder.create(floatOption).range(0f, 1f).step(0.01f).formatValue(val -> Text.of(String.format("%.0f", val * 100) + "%"))).stateManager(StateManager.createSimple(1F, () -> option.binding().getValue().alpha, newVal -> option.binding().getValue().alpha = newVal)).build();
        o_lightLevel = EvilOption.<Integer>createBuilder().name(Text.of("Light Level")).description(OptionDescription.createBuilder().text(Text.of("How brightly lit the object is. -1 uses the world's lighting, while 0-255 is mapped respectively.")).build()).stateManager(StateManager.createSimple(-1, () -> option.binding().getValue().lightLevel, newVal -> option.binding().getValue().lightLevel = newVal)).controller(integerOption -> IntegerSliderControllerBuilder.create(integerOption).step(1).range(-1, 255).formatValue(value -> Text.of(value == -1 ? "Use World Light" : value + ""))).build();
        o_renderLayer = CrystalChams.createRenderModeOption("Render Mode", "",
                StateManager.createSimple(RenderMode.DEFAULT, () -> option.binding().getValue().renderLayer, newVal -> option.binding().getValue().renderLayer = newVal),
                OptionGroups.RENDER_MODE);
        o_culling = CrystalChams.createBooleanOption("Culled", "", StateManager.createSimple(false, () -> option.binding().getValue().culling, newVal -> option.binding().getValue().culling = newVal));
        o_funnyOption = CrystalChams.createBooleanOption("Funny Option", "", StateManager.createSimple(false, () -> option.binding().getValue().funnyOption, newVal -> option.binding().getValue().funnyOption = newVal));
        o_funnierOption = CrystalChams.createBooleanOption("Funnier Option", "", StateManager.createSimple(false, () -> option.binding().getValue().funnierOption, newVal -> option.binding().getValue().funnierOption = newVal));
        o_rainbow = CrystalChams.createBooleanOption("Rainbow", "", StateManager.createSimple(false, () -> option.binding().getValue().rainbow, newVal -> option.binding().getValue().rainbow = newVal));
        o_rainbowSpeed = EvilOption.<Float>createBuilder().name(Text.of("Rainbow Speed")).controller(floatOption -> CustomFloatSliderControllerBuilder.create(floatOption).range(0f, 10f).step(0.1f).formatValue(value -> Text.of(String.format("%.1f", value) + "x"))).stateManager(StateManager.createSimple(2F, () -> option.binding().getValue().rainbowSpeed, newVal -> option.binding().getValue().rainbowSpeed = newVal)).build();
        o_rainbowDelay = EvilOption.<Integer>createBuilder().name(Text.of("Rainbow Delay")).controller(integerOption -> CustomIntegerSliderControllerBuilder.create(integerOption).step(1).range(-500, 500).formatValue(integer -> Text.of(integer + "ms"))).stateManager(StateManager.createSimple(0, () -> option.binding().getValue().rainbowDelay, newVal -> option.binding().getValue().rainbowDelay = newVal)).build();
        o_rainbowSaturation = EvilOption.<Float>createBuilder().name(Text.of("Saturation")).controller(floatOption -> CustomFloatSliderControllerBuilder.create(floatOption).range(0f, 1f).step(0.01f).formatValue(val -> Text.of(String.format("%.0f", val * 100) + "%"))).stateManager(StateManager.createSimple(1F, () -> option.binding().getValue().rainbowSaturation, newVal -> option.binding().getValue().rainbowSaturation = newVal)).build();
        o_rainbowBrightness = EvilOption.<Float>createBuilder().name(Text.of("Brightness")).controller(floatOption -> CustomFloatSliderControllerBuilder.create(floatOption).range(0f, 1f).step(0.01f).formatValue(val -> Text.of(String.format("%.0f", val * 100) + "%"))).stateManager(StateManager.createSimple(1F, () -> option.binding().getValue().rainbowBrightness, newVal -> option.binding().getValue().rainbowBrightness = newVal)).build();
        o_animation = CrystalChams.createBooleanOption("Animations", "", StateManager.createSimple(false, () -> option.binding().getValue().animation, newVal -> option.binding().getValue().animation = newVal));

        o_animateVerticalOffset = CrystalChams.createBooleanOption("Animate Vertical Offset", "", StateManager.createSimple(false, () -> option.binding().getValue().animateVerticalOffset, newVal -> option.binding().getValue().animateVerticalOffset = newVal));

    }

    @Override
    public Option<ModelPartOptions> option() {
        return option;
    }

    @Override
    public Text formatValue() {
        return Text.of("Edit");
    }

    @Override
    public AbstractWidget provideWidget(YACLScreen screen, Dimension<Integer> widgetDimension) {
        //            System.out.println(ChamsConfig.o_frameList.isPendingValueDefault());
        //            ((ListOptionImplAccessor) ChamsConfig.o_frameList).triggerListener(OptionEventListener.Event.STATE_CHANGE, false);
        subScreen = YetAnotherConfigLib.createBuilder().title(Text.of("Custom End Crystals")).category(ConfigCategory.createBuilder().name(Text.of("Edit Frame")).option(o_render).group(OptionGroup.createBuilder().name(Text.of("Movement")).option(o_offset).option(o_rotationSpeed).option(o_bounceHeight).option(o_bounceSpeed).option(o_tickDelay).build()).group(OptionGroup.createBuilder().name(Text.of("Rendering")).option(o_scale).option(o_color).option(o_alpha).option(o_lightLevel).option(o_renderLayer).option(o_culling).option(o_funnyOption).option(o_funnierOption).build()).group(OptionGroup.createBuilder().name(Text.of("Rainbow")).option(o_rainbow).option(o_rainbowSpeed).option(o_rainbowDelay).option(o_rainbowSaturation).option(o_rainbowBrightness).build()).build()).save(ChamsConfig.CONFIG::save).build();
        return new ModelPartOptionElement(this, screen, widgetDimension);
    }

    public static class ModelPartOptionElement extends ControllerWidget<ModelPartController> {
        public ModelPartOptionElement(ModelPartController control, YACLScreen screen, Dimension<Integer> dim) {
            super(control, screen, dim);
            //TODO: this really fuckin sucks, it keeps old option elements in it, but it doesn't crash!
            Arrays.stream(ModelPartController.class.getDeclaredFields()).filter(field -> field.getName().startsWith("o_")).forEach(field -> {
                try {
                    Object value = field.get(control);
                    if (value instanceof EvilOption && ((EvilOption<?>) value).group() != null) {
                        CrystalChams.optionGroups.get(((EvilOption<?>) value).group()).add((EvilOption) value);
                    }
                } catch (IllegalAccessException e) {
                    System.out.println("what the hell");
                }
            });
        }

        @Override
        protected int getHoveredControlWidth() {
            return 0;
        }

        @Override
        public void render(DrawContext graphics, int mouseX, int mouseY, float delta) {
            hovered = isMouseOver(mouseX, mouseY);
            int index = -1;
            for (ListOptionEntry<ModelPartOptions> modelPartOptionsListOptionEntry : ChamsConfig.o_frameList.options()) {
                ModelPartController controller = (ModelPartController) ((ListOptionEntryImpl.EntryController) (modelPartOptionsListOptionEntry.controller())).controller();
                if (controller.equals(this.control)) {
                    index = ChamsConfig.o_frameList.indexOf(modelPartOptionsListOptionEntry);
                }
            }
            Text name = Text.of("Frame " + (index + 1));
            Text shortenedName = Text.literal(GuiUtils.shortenString(name.getString(), textRenderer, getDimension().width() - getControlWidth() - getXPadding() - 7, "...")).setStyle(name.getStyle());

            drawButtonRect(graphics, getDimension().x(), getDimension().y(), getDimension().xLimit(), getDimension().yLimit(), hovered || focused, isAvailable());
            graphics.drawText(textRenderer, shortenedName, getDimension().x() + getXPadding(), getTextY(), getValueColor(), true);

            drawValueText(graphics, mouseX, mouseY, delta);

            control.hovered = isHovered();
            if (control.hovered || CrystalChams.hoveredIndex == -1) {
                this.control.alphaMultiplier = (float) CrystalChams.ease(control.alphaMultiplier, 1, 5);
            } else {
                this.control.alphaMultiplier = (float) CrystalChams.ease(control.alphaMultiplier, 0.25, 5);
            }
        }


        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (!isMouseOver(mouseX, mouseY) || !isAvailable()) return false;
            playDownSound();
            client.setScreen(new SecondaryYACLScreen(control, control.subScreen, screen));
            return true;
        }
    }
}
