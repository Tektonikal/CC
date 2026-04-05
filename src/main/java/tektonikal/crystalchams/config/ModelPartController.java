package tektonikal.crystalchams.config;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.FloatSliderControllerBuilder;
import dev.isxander.yacl3.api.utils.Dimension;
import dev.isxander.yacl3.gui.AbstractWidget;
import dev.isxander.yacl3.gui.YACLScreen;
import dev.isxander.yacl3.gui.controllers.ControllerWidget;
import dev.isxander.yacl3.gui.utils.GuiUtils;
import dev.isxander.yacl3.impl.ListOptionEntryImpl;
import dev.isxander.yacl3.impl.controller.ColorControllerBuilderImpl;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import tektonikal.crystalchams.CrystalChams;
import tektonikal.crystalchams.OptionGroups;
import tektonikal.crystalchams.util.Easings;

import java.awt.*;

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
    public EvilOption<Float> o_delay;
    public EvilOption<Float> o_scale;
    public EvilOption<Color> o_color;
    public EvilOption<Float> o_alpha;
    public EvilOption<Integer> o_blockLightLevel;
    public EvilOption<Integer> o_skyLightLevel;
    public EvilOption<RenderMode> o_renderLayer;
    public EvilOption<Boolean> o_culling;
    public EvilOption<Boolean> o_funnyOption;
    public EvilOption<Boolean> o_funnierOption;
    public EvilOption<Boolean> o_rainbow;
    public EvilOption<Float> o_rainbowSpeed;
    public EvilOption<Float> o_rainbowDelay;
    public EvilOption<Float> o_rainbowSaturation;
    public EvilOption<Float> o_rainbowBrightness;

    public EvilOption<Boolean> o_animation;

    public EvilOption<Boolean> o_animateVerticalOffset;
    public EvilOption<Float> o_startingVerticalOffset;
    public EvilOption<Float> o_verticalOffsetAnimationDuration;
    public EvilOption<Float> o_verticalOffsetAnimationDelay;
    public EvilOption<Easings> o_verticalOffsetEasing;

    @SuppressWarnings("deprecation")
    public ModelPartController(Option<ModelPartOptions> option) {
        this.option = option;
        o_render = CrystalChams.createBooleanOption(Text.translatable("config.option.renderFrames"),
                StateManager.createSimple(true, () -> option.binding().getValue().render, newVal -> option.binding().getValue().render = newVal), OptionGroups.RENDER);
        o_offset = EvilOption.<Float>createBuilder().name(Text.translatable("config.option.verticalOffset")).controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(-2.5f, 2.5f).step(0.1f).formatValue(CrystalChams.BLOCKS_FORMATTER)).stateManager(StateManager.createSimple(0F, () -> option.binding().getValue().offset, newVal -> option.binding().getValue().offset = newVal)).group(OptionGroups.VERTICAL_OFFSET).build();
        o_rotationSpeed = EvilOption.<Float>createBuilder().name(Text.translatable("config.option.rotationSpeed")).controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(-15f, 15f).step(0.1f).formatValue(val -> Text.translatable(String.format("%.1f", val) + "x"))).stateManager(StateManager.createSimple(1F, () -> option.binding().getValue().rotationSpeed, newVal -> option.binding().getValue().rotationSpeed = newVal)).group(OptionGroups.ROTATION_SPEED).build();
        o_bounceHeight = EvilOption.<Float>createBuilder().name(Text.translatable("config.option.bounceHeight")).controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 2.5f).step(0.05f).formatValue(val -> Text.translatable(String.format("%.2f", val) + "x"))).stateManager(StateManager.createSimple(1F, () -> option.binding().getValue().bounceHeight, newVal -> option.binding().getValue().bounceHeight = newVal)).group(OptionGroups.BOUNCE_HEIGHT).build();
        o_bounceSpeed = EvilOption.<Float>createBuilder().name(Text.translatable("config.option.bounceSpeed")).controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 2.5f).step(0.05f).formatValue(val -> Text.translatable(String.format("%.2f", val) + "x"))).stateManager(StateManager.createSimple(1F, () -> option.binding().getValue().bounceSpeed, newVal -> option.binding().getValue().bounceSpeed = newVal)).group(OptionGroups.BOUNCE_SPEED).build();
        o_delay = EvilOption.<Float>createBuilder().name(Text.translatable("config.option.delay")).controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(-2.5f, 2.5f).step(0.1f).formatValue(CrystalChams.SECONDS_FORMATTER)).stateManager(StateManager.createSimple(0F, () -> option.binding().getValue().delay, newVal -> option.binding().getValue().delay = newVal)).group(OptionGroups.DELAY).build();
        o_scale = EvilOption.<Float>createBuilder().name(Text.translatable("config.option.scale")).controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 2f).step(0.05f).formatValue(val -> Text.translatable(String.format("%.2f", val) + "x"))).stateManager(StateManager.createSimple(1F, () -> option.binding().getValue().scale, newVal -> option.binding().getValue().scale = newVal)).group(OptionGroups.SCALE).build();
        o_color = EvilOption.<Color>createBuilder().name(Text.translatable("config.group.color")).controller(ColorControllerBuilderImpl::new).stateManager(StateManager.createSimple(new Color(255, 255, 255), () -> option.binding().getValue().color, newVal -> option.binding().getValue().color = newVal)).group(OptionGroups.COLOR).build();
        o_alpha = EvilOption.<Float>createBuilder().name(Text.translatable("config.option.opacity")).controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 1f).step(0.01f).formatValue(val -> Text.translatable(String.format("%.0f", val * 100) + "%"))).stateManager(StateManager.createSimple(1F, () -> option.binding().getValue().alpha, newVal -> option.binding().getValue().alpha = newVal)).group(OptionGroups.OPACITY).build();
        o_blockLightLevel = CrystalChams.createBlockLightLevelOption(StateManager.createSimple(-1, () -> option.binding().getValue().blockLightLevel, newVal -> option.binding().getValue().blockLightLevel = newVal));
        o_skyLightLevel = CrystalChams.createSkyLightLevelOption(StateManager.createSimple(-1, () -> option.binding().getValue().skyLightLevel, newVal -> option.binding().getValue().skyLightLevel = newVal));

        o_renderLayer = CrystalChams.createRenderModeOption(Text.translatable("config.option.renderMode"),
                StateManager.createSimple(RenderMode.DEFAULT, () -> option.binding().getValue().renderLayer, newVal -> option.binding().getValue().renderLayer = newVal),
                OptionGroups.RENDER_MODE);
        o_culling = CrystalChams.createBooleanOption(Text.translatable(CrystalChams.SEPARATOR).append(Text.translatable("config.option.culling")),  StateManager.createSimple(false, () -> option.binding().getValue().culling, newVal -> option.binding().getValue().culling = newVal), OptionGroups.CULLED);
        o_funnyOption = CrystalChams.createBooleanOption(Text.translatable("Funny Option"),  StateManager.createSimple(false, () -> option.binding().getValue().funnyOption, newVal -> option.binding().getValue().funnyOption = newVal), OptionGroups.THE_FUNNY_OPTION);
        o_funnierOption = CrystalChams.createBooleanOption(Text.translatable("Funnier Option"),  StateManager.createSimple(false, () -> option.binding().getValue().funnierOption, newVal -> option.binding().getValue().funnierOption = newVal), OptionGroups.THE_FUNNIER_OPTION);
        o_rainbow = CrystalChams.createBooleanOption(Text.translatable("config.option.rainbow"),  StateManager.createSimple(false, () -> option.binding().getValue().rainbow, newVal -> option.binding().getValue().rainbow = newVal), OptionGroups.RAINBOW);
        o_rainbowSpeed = EvilOption.<Float>createBuilder().name(Text.translatable(CrystalChams.SEPARATOR).append(Text.translatable("config.option.rainbowSpeed"))).controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 10f).step(0.1f).formatValue(value -> Text.translatable(String.format("%.1f", value) + "x"))).stateManager(StateManager.createSimple(2F, () -> option.binding().getValue().rainbowSpeed, newVal -> option.binding().getValue().rainbowSpeed = newVal)).group(OptionGroups.RAINBOW_SPEED).build();
        o_rainbowDelay = EvilOption.<Float>createBuilder().name(Text.translatable(CrystalChams.SEPARATOR).append(Text.translatable("config.option.rainbowDelay"))).controller(integerOption -> FloatSliderControllerBuilder.create(integerOption).step(0.1F).range(-2.5F, 2.5F).formatValue(CrystalChams.SECONDS_FORMATTER)).stateManager(StateManager.createSimple(0F, () -> option.binding().getValue().rainbowDelay, newVal -> option.binding().getValue().rainbowDelay = newVal)).group(OptionGroups.RAINBOW_DELAY).build();
        o_rainbowSaturation = CrystalChams.createFloatOptionPercent(Text.translatable(CrystalChams.SEPARATOR).append(Text.translatable("config.option.rainbowSaturation")), StateManager.createSimple(1f, () -> option.binding().getValue().rainbowSaturation, newVal -> option.binding().getValue().rainbowSaturation = newVal), OptionGroups.RAINBOW_SATURATION);
        o_rainbowBrightness = CrystalChams.createFloatOptionPercent(Text.translatable(CrystalChams.SEPARATOR).append(Text.translatable("config.option.rainbowBrightness")), StateManager.createSimple(1F, () -> option.binding().getValue().rainbowBrightness, newVal -> option.binding().getValue().rainbowBrightness = newVal), OptionGroups.RAINBOW_BRIGHTNESS);
        o_animation = CrystalChams.createBooleanOption(Text.translatable("Animations"),
                StateManager.createSimple(false, () -> option.binding().getValue().animation, newVal -> option.binding().getValue().animation = newVal), OptionGroups.ANIMATION);

        o_animateVerticalOffset = CrystalChams.createBooleanOption(Text.translatable("Animate Vertical Offset"),
                StateManager.createSimple(false, () -> option.binding().getValue().animateVerticalOffset, newVal -> option.binding().getValue().animateVerticalOffset = newVal), OptionGroups.ANIMATE_VERTICAL_OFFSET);
        o_startingVerticalOffset =  EvilOption.<Float>createBuilder().name(Text.translatable("Vertical Offset")).controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(-2.5f, 2.5f).step(0.1f).formatValue(CrystalChams.BLOCKS_FORMATTER)).stateManager(StateManager.createSimple(0F, () -> option.binding().getValue().offset, newVal -> option.binding().getValue().offset = newVal)).group(OptionGroups.VERTICAL_OFFSET).build();


        //too lazy to set up annotations again, whatever man
        o_render.addListener(this::update);
        o_rainbow.addListener(this::update);
        this.update(o_render, o_render.stateManager().get());
        this.update(o_rainbow, o_rainbow.stateManager().get());
    }

    public void update(Option<Boolean> booleanOption, Boolean aBoolean) {
        if(booleanOption.equals(o_render)){
            o_offset.setAvailable(aBoolean);
            o_scale.setAvailable(aBoolean);
            o_rotationSpeed.setAvailable(aBoolean);
            o_bounceHeight.setAvailable(aBoolean);
            o_bounceSpeed.setAvailable(aBoolean);
            o_delay.setAvailable(aBoolean);
            o_color.setAvailable(aBoolean);
            o_alpha.setAvailable(aBoolean);
            o_rainbow.setAvailable(aBoolean);
            o_blockLightLevel.setAvailable(aBoolean);
            o_skyLightLevel.setAvailable(aBoolean);
            o_renderLayer.setAvailable(aBoolean);
            o_culling.setAvailable(aBoolean);
            o_animation.setAvailable(aBoolean);

            o_funnyOption.setAvailable(aBoolean);
            o_funnierOption.setAvailable(aBoolean);
        }
        else if(booleanOption.equals(o_rainbow)){
            boolean b = o_rainbow.available() && aBoolean;
            o_rainbowSpeed.setAvailable(b);
            o_rainbowDelay.setAvailable(b);
            o_rainbowSaturation.setAvailable(b);
            o_rainbowBrightness.setAvailable(b);
        }
    }
    @Override
    public Option<ModelPartOptions> option() {
        return option;
    }

    @Override
    public Text formatValue() {
        return Text.translatable("Edit");
    }

    @Override
    public AbstractWidget provideWidget(YACLScreen screen, Dimension<Integer> widgetDimension) {
        subScreen = YetAnotherConfigLib.createBuilder()
                .title(Text.translatable("config.title"))
                .category(ConfigCategory.createBuilder()
                        //TODO include index?
                        .name(Text.translatable("Edit Frame"))
                        .option(o_render)
                        .group(OptionGroup.createBuilder()
                                .name(Text.translatable("config.group.transform"))
                                .option(o_offset)
                                .option(o_scale)
                                .build())
                        .group(OptionGroup.createBuilder()
                                .name(Text.translatable("config.group.movement"))
                                .option(o_rotationSpeed)
                                .option(o_bounceHeight)
                                .option(o_bounceSpeed)
                                .option(o_delay).build())
                        .group(OptionGroup.createBuilder()
                                .name(Text.translatable("config.group.color"))
                                .option(o_color)
                                .option(o_alpha)
                                .option(o_rainbow)
                                .option(o_rainbowSpeed)
                                .option(o_rainbowDelay)
                                .option(o_rainbowSaturation)
                                .option(o_rainbowBrightness).build())
                        .group(OptionGroup.createBuilder()
                                .name(Text.translatable("config.group.rendering"))
                                .option(o_blockLightLevel)
                                .option(o_skyLightLevel)
                                .option(o_renderLayer)
                                .option(o_culling)
                                .option(o_funnyOption)
//                                .option(o_funnierOption)
                                .build())
//                        .group(OptionGroup.createBuilder()
//                                .name(Text.translatable("Animation"))
//
//                                .build())
                        .build())
                .save(ChamsConfig.CONFIG::save)
                .build();
        return new ModelPartOptionElement(this, screen, widgetDimension);
    }

    public static class ModelPartOptionElement extends ControllerWidget<ModelPartController> {
        public ModelPartOptionElement(ModelPartController control, YACLScreen screen, Dimension<Integer> dim) {
            super(control, screen, dim);
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
            Text name = Text.translatable("Frame " + (index + 1));
            Text shortenedName = Text.literal(GuiUtils.shortenString(name.getString(), textRenderer, getDimension().width() - getControlWidth() - getXPadding() - 7, "...")).setStyle(name.getStyle());

            drawButtonRect(graphics, getDimension().x(), getDimension().y(), getDimension().xLimit(), getDimension().yLimit(), hovered || focused, isAvailable());
            graphics.drawText(textRenderer, shortenedName, getDimension().x() + getXPadding(), getTextY(), getValueColor(), true);

            drawValueText(graphics, mouseX, mouseY, delta);

            control.hovered = isHovered();
        }


        @Override
        public boolean mouseClicked(Click mouseButtonEvent, boolean doubleClick) {
            if (!isMouseOver(mouseButtonEvent.x(), mouseButtonEvent.y()) || !isAvailable()) return false;
            playDownSound();
            client.setScreen(new SecondaryYACLScreen(control.subScreen, screen));
            return true;
        }
    }
}
