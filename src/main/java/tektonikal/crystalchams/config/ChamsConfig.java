package tektonikal.crystalchams.config;

import com.google.gson.JsonSyntaxException;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.EnumControllerBuilder;
import dev.isxander.yacl3.api.controller.FloatSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import dev.isxander.yacl3.gui.YACLScreen;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.slf4j.LoggerFactory;
import tektonikal.crystalchams.CrystalChams;
import tektonikal.crystalchams.OptionGroups;
import tektonikal.crystalchams.annotation.Updatable;
import tektonikal.crystalchams.stupidfuckingboilerplate.ModelPartControllerBuilder;
import tektonikal.crystalchams.util.AnimatedGIFDecoder;
import tektonikal.crystalchams.util.Easings;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

import static tektonikal.crystalchams.CrystalChams.filePath;

public class ChamsConfig {
    public static final ConfigClassHandler<ChamsConfig> CONFIG = ConfigClassHandler.createBuilder(ChamsConfig.class)
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve("crystalchams.json"))
                    .build())
            .build();
    /*
    It's not a bad thing that the code relies on proper variable names,
    it's a good thing that the code enforces good naming conventions.
     */
    //@formatter:off
    //General:
    @SerialEntry public boolean modEnabled = true;
    @SerialEntry public boolean showAnimations = true;
    @SerialEntry public float previewScale = 1;
        @SerialEntry public boolean randomizeAge = true;
        @SerialEntry public boolean renderHitbox = false;
        //Shadow
        @SerialEntry public float shadowRadius = 0.5F;
        @SerialEntry public float shadowAlpha = 0.5F;
        //Base
        @SerialEntry public CrystalChams.BaseRenderMode baseRenderMode = CrystalChams.BaseRenderMode.DEFAULT;
        @SerialEntry public float baseOffset = 0F;
        @SerialEntry public float baseScale = 1F;
        @SerialEntry public int baseRotation = 0;
        @SerialEntry public Color baseColor = Color.decode("#FFFFFF");
        @SerialEntry public float baseAlpha = 1.0F;
        @SerialEntry public int baseBlockLightLevel = -1;
        @SerialEntry public int baseSkyLightLevel = -1;

        @SerialEntry public RenderMode baseRenderLayer = RenderMode.DEFAULT;
            @SerialEntry public boolean baseCulling = false;

            //Base Rainbow
            @SerialEntry public boolean baseRainbow = false;
            @SerialEntry public float baseRainbowSpeed = 2;
            @SerialEntry public float baseRainbowDelay = 0;
            @SerialEntry public float baseRainbowSaturation = 1;
            @SerialEntry public float baseRainbowBrightness = 1;
        //Config
            //Empty
        //Core
        @SerialEntry  public boolean renderCore = true;
            //Core Movement
            @SerialEntry public float coreOffset = 0f;
            @SerialEntry public float coreRotationSpeed = 1F;
            @SerialEntry public float coreBounceHeight = 1f;
            @SerialEntry public float coreBounceSpeed = 1f;
            @SerialEntry public float coreDelay = 0;
            //Core Rendering
            @SerialEntry public float coreScale = 1F;
            @SerialEntry public Color coreColor = Color.decode("#ffffff");
            @SerialEntry public float coreAlpha = 1;
            @SerialEntry public int coreBlockLightLevel = -1;
            @SerialEntry public int coreSkyLightLevel = -1;
            @SerialEntry public RenderMode coreRenderLayer = RenderMode.DEFAULT;
                @SerialEntry public boolean coreCulling = false;

            //Core Rainbow
            @SerialEntry  public boolean coreRainbow = false;
                @SerialEntry public float coreRainbowSpeed = 2;
                @SerialEntry public float coreRainbowDelay = 0;
                @SerialEntry public float coreRainbowSaturation = 1;
                @SerialEntry public float coreRainbowBrightness = 1;
            //Animation
            @SerialEntry public boolean coreAlphaAnimation = false;
                @SerialEntry public float coreStartAlpha = 1F;
                @SerialEntry public float coreAlphaAnimDuration = 1F;
                @SerialEntry public float coreAlphaDelay = 0F;
                @SerialEntry public Easings coreAlphaEasing = Easings.OFF;

            @SerialEntry public boolean coreScaleAnimation = false;
                @SerialEntry public float coreStartScale = 0F;
                @SerialEntry public float coreScaleAnimDuration = 1F;
                @SerialEntry public float coreScaleDelay = 0F;
                @SerialEntry public Easings coreScaleEasing = Easings.OFF;
        //Frame 1
        @SerialEntry public boolean renderFrames = true;
            @SerialEntry public List<ModelPartOptions> frameList = List.of(new ModelPartOptions(), new ModelPartOptions());
        //Beam
        @SerialEntry public boolean renderBeam = true;
            //Rendering
            @SerialEntry public Color beam1Color = Color.decode("#ffffff");
            @SerialEntry public float beam1Alpha = 1;
            @SerialEntry public float beam1Radius = 0.75F;
            @SerialEntry public int beam1BlockLightLevel = -1;
            @SerialEntry public int beam1SkyLightLevel = -1;
            @SerialEntry public Color beam2Color = Color.decode("#000000");
            @SerialEntry public float beam2Alpha = 1;
            @SerialEntry public float beam2Radius = 0.15F;
            @SerialEntry public int beam2BlockLightLevel = -1;
            @SerialEntry public int beam2SkyLightLevel = -1;
            //Rainbow 1
            @SerialEntry public boolean beam1Rainbow = false;
                @SerialEntry public float beam1RainbowSpeed = 2;
                @SerialEntry public float beam1RainbowDelay = 0;
                @SerialEntry public float beam1RainbowSaturation = 1;
                @SerialEntry public float beam1RainbowBrightness = 1;
            //Rainbow 2
            @SerialEntry public boolean beam2Rainbow = false;
                @SerialEntry public float beam2RainbowSpeed = 2;
                @SerialEntry public float beam2RainbowDelay = 0;
                @SerialEntry public float beam2RainbowSaturation = 1;
                @SerialEntry public float beam2RainbowBrightness = 1;
            //Extras
            @SerialEntry public float beamScrollSpeed = 1;
            @SerialEntry public int beamSides = 8;
            @SerialEntry public RenderMode beamRenderLayer = RenderMode.DEFAULT;
                @SerialEntry public boolean beamCulling = false;

    //@formatter:on
    @Updatable
    public static EvilOption<Boolean> o_modEnabled = CrystalChams.createBooleanOption(Text.translatable("config.option.modEnabled"),
            StateManager.createSimple(true, () -> CONFIG.instance().modEnabled, newVal -> CONFIG.instance().modEnabled = newVal), null);
//    public static EvilOption<Boolean> o_showAnimations = CrystalChams.createBooleanOption("config.option.showAnimations",
//            StateManager.createSimple(true, () -> CONFIG.instance().showAnimations, newVal -> CONFIG.instance().showAnimations = newVal), null);
    public static EvilOption<Boolean> o_renderHitbox = CrystalChams.createBooleanOption(Text.translatable("config.option.showHitboxes"),
            StateManager.createSimple(false, () -> CONFIG.instance().renderHitbox, newVal -> CONFIG.instance().renderHitbox = newVal), null);
    public static EvilOption<Float> o_previewScale = EvilOption.<Float>createBuilder()
            .name(Text.translatable("config.option.previewScale"))
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 2f).step(0.01f).formatValue(CrystalChams.PERCENT_FORMATTER))
            .stateManager(StateManager.createSimple(1F, () -> CONFIG.instance().previewScale, newVal -> CONFIG.instance().previewScale = newVal))
            .build();
    public static EvilOption<Boolean> o_randomizeAge = CrystalChams.createBooleanOption(Text.translatable("config.option.randomizeAge"),
            StateManager.createSimple(true, () -> CONFIG.instance().randomizeAge, newVal -> CONFIG.instance().randomizeAge = newVal), null);
    public static EvilOption<Float> o_shadowRadius = EvilOption.<Float>createBuilder()
            .name(Text.translatable("config.option.radius"))
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 2f).step(0.1f).formatValue(CrystalChams.BLOCKS_FORMATTER))
            .stateManager(StateManager.createSimple(0.5F, () -> CONFIG.instance().shadowRadius, newVal -> CONFIG.instance().shadowRadius = newVal))
            .build();
    public static EvilOption<Float> o_shadowAlpha = CrystalChams.createFloatOptionPercent(Text.translatable("config.option.opacity"), StateManager.createSimple(0.5F, () -> CONFIG.instance().shadowAlpha, newVal -> CONFIG.instance().shadowAlpha = newVal), null);
    public static EvilOption<Float> o_baseScale = EvilOption.<Float>createBuilder()
            .name(Text.translatable("config.option.scale"))
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 2f).step(0.05f).formatValue(CrystalChams.MULTIPLIER_FORMATTER))
            .stateManager(StateManager.createSimple(1F, () -> CONFIG.instance().baseScale, newVal -> CONFIG.instance().baseScale = newVal))
            .group(OptionGroups.SCALE)
            .build();
    public static EvilOption<Integer> o_baseRotation = EvilOption.<Integer>createBuilder()
            .name(Text.translatable("config.option.baseRotation"))
            .controller(integerOption -> IntegerSliderControllerBuilder.create(integerOption).step(1).range(0, 360).formatValue(value -> Text.translatable(value + "°")))
            .stateManager(StateManager.createSimple(0, () -> CONFIG.instance().baseRotation, newVal -> CONFIG.instance().baseRotation = newVal))
            .build();
    public static EvilOption<RenderMode> o_baseRenderLayer = CrystalChams.createRenderModeOption(Text.translatable("config.option.renderMode"),
            StateManager.createSimple(RenderMode.DEFAULT, () -> CONFIG.instance().baseRenderLayer, newVal -> CONFIG.instance().baseRenderLayer = newVal),
            OptionGroups.RENDER_MODE);
    public static EvilOption<Boolean> o_baseCulling = CrystalChams.createBooleanOption(Text.translatable("config.option.culling"),
            StateManager.createSimple(false, () -> CONFIG.instance().baseCulling, newVal -> CONFIG.instance().baseCulling = newVal),
            OptionGroups.CULLED);
    public static EvilOption<Float> o_baseOffset = EvilOption.<Float>createBuilder()
            .name(Text.translatable("config.option.verticalOffset"))
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(-2.5F, 2.5F).step(0.1F).formatValue(CrystalChams.BLOCKS_FORMATTER))
            .stateManager(StateManager.createSimple(0F, () -> CONFIG.instance().baseOffset, newVal -> CONFIG.instance().baseOffset = newVal))
            .group(OptionGroups.VERTICAL_OFFSET)
            .build();
    public static EvilOption<Color> o_baseColor = CrystalChams.createColorOption(Text.translatable("config.group.color"), StateManager.createSimple(Color.WHITE, () -> CONFIG.instance().baseColor, newVal -> CONFIG.instance().baseColor = newVal), OptionGroups.COLOR);
    public static EvilOption<Float> o_baseAlpha = CrystalChams.createFloatOptionPercent(Text.translatable("config.option.opacity"), StateManager.createSimple(1f, () -> CONFIG.instance().baseAlpha, newVal -> CONFIG.instance().baseAlpha = newVal), OptionGroups.OPACITY);
    public static EvilOption<Integer> o_baseBlockLightLevel = CrystalChams.createBlockLightLevelOption(StateManager.createSimple(-1, () -> CONFIG.instance().baseBlockLightLevel, newVal -> CONFIG.instance().baseBlockLightLevel = newVal));
    public static EvilOption<Integer> o_baseSkyLightLevel = CrystalChams.createSkyLightLevelOption(StateManager.createSimple(-1, () -> CONFIG.instance().baseSkyLightLevel, newVal -> CONFIG.instance().baseSkyLightLevel = newVal));

    @Updatable
    public static EvilOption<Boolean> o_baseRainbow = CrystalChams.createBooleanOption(Text.translatable("config.option.rainbow"),
            StateManager.createSimple(false, () -> CONFIG.instance().baseRainbow, newVal -> CONFIG.instance().baseRainbow = newVal),
            OptionGroups.RAINBOW);
    public static EvilOption<Float> o_baseRainbowSpeed = EvilOption.<Float>createBuilder()
            .name(Text.translatable(CrystalChams.SEPARATOR).append(Text.translatable("config.option.rainbowSpeed")))
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 10f).step(0.1f).formatValue(CrystalChams.MULTIPLIER_FORMATTER_ONE_PLACE))
//            .controller(integerOption -> CustomIntegerSliderController.create(integerOption).step(1).range(1, 10).formatValue(value -> Text.translatable(value + "x")))
            .stateManager(StateManager.createSimple(2F, () -> CONFIG.instance().baseRainbowSpeed, newVal -> CONFIG.instance().baseRainbowSpeed = newVal))
            .group(OptionGroups.RAINBOW_SPEED)
            .build();
    public static EvilOption<Float> o_baseRainbowDelay = EvilOption.<Float>createBuilder()
            .name(Text.translatable(CrystalChams.SEPARATOR).append(Text.translatable("config.option.rainbowDelay")))
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(-2.5f, 2.5f).step(0.1f).formatValue(CrystalChams.SECONDS_FORMATTER))
            .stateManager(StateManager.createSimple(0F, () -> CONFIG.instance().baseRainbowDelay, newVal -> CONFIG.instance().baseRainbowDelay = newVal))
            .available(CONFIG.instance().baseRainbow && CONFIG.instance().baseRenderMode != CrystalChams.BaseRenderMode.NEVER && CONFIG.instance().modEnabled)
            .group(OptionGroups.RAINBOW_DELAY)
            .build();
    public static EvilOption<Float> o_baseRainbowSaturation = CrystalChams.createFloatOptionPercent(Text.translatable(CrystalChams.SEPARATOR).append(Text.translatable("config.option.rainbowSaturation")), StateManager.createSimple(1f, () -> CONFIG.instance().baseRainbowSaturation, newVal -> CONFIG.instance().baseRainbowSaturation = newVal), OptionGroups.RAINBOW_SATURATION);
    public static EvilOption<Float> o_baseRainbowBrightness = CrystalChams.createFloatOptionPercent(Text.translatable(CrystalChams.SEPARATOR).append(Text.translatable("config.option.rainbowBrightness")), StateManager.createSimple(1F, () -> CONFIG.instance().baseRainbowBrightness, newVal -> CONFIG.instance().baseRainbowBrightness = newVal), OptionGroups.RAINBOW_BRIGHTNESS);
    //keep this one below all other base options
    public static EvilOption<CrystalChams.BaseRenderMode> o_baseRenderMode = EvilOption.<CrystalChams.BaseRenderMode>createBuilder()
            .name(Text.translatable("config.option.show"))
            .description(OptionDescription.createBuilder().text(Text.translatable("config.option.show.description")).build())
            .controller(renderModeOption -> EnumControllerBuilder.create(renderModeOption).enumClass(CrystalChams.BaseRenderMode.class))
            .listener(ChamsConfig::specialUpdate)
            .stateManager(StateManager.createSimple(CrystalChams.BaseRenderMode.DEFAULT, () -> CONFIG.instance().baseRenderMode, newVal -> CONFIG.instance().baseRenderMode = newVal))
            .group(OptionGroups.RENDER)
            .build();
    @Updatable
    public static EvilOption<Boolean> o_renderCore = CrystalChams.createBooleanOption(Text.translatable("config.option.renderCore"),
            StateManager.createSimple(true, () -> CONFIG.instance().renderCore, newVal -> CONFIG.instance().renderCore = newVal),
            OptionGroups.RENDER);
    public static EvilOption<Float> o_coreOffset = EvilOption.<Float>createBuilder()
            .name(Text.translatable("config.option.verticalOffset"))
            .group(OptionGroups.VERTICAL_OFFSET)
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(-2.5f, 2.5f).step(0.1f).formatValue(CrystalChams.BLOCKS_FORMATTER))
            .stateManager(StateManager.createSimple(0F, () -> CONFIG.instance().coreOffset, newVal -> CONFIG.instance().coreOffset = newVal))
            .build();
    public static EvilOption<Float> o_coreRotationSpeed = EvilOption.<Float>createBuilder()
            .group(OptionGroups.ROTATION_SPEED)
            .name(Text.translatable("config.option.rotationSpeed"))
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(-15f, 15f).step(0.1f).formatValue(val -> Text.translatable(String.format("%.1f", val) + "x")))
            .stateManager(StateManager.createSimple(1F, () -> CONFIG.instance().coreRotationSpeed, newVal -> CONFIG.instance().coreRotationSpeed = newVal))
            .build();
    public static EvilOption<Float> o_coreBounceHeight = EvilOption.<Float>createBuilder()
            .group(OptionGroups.BOUNCE_HEIGHT)
            .name(Text.translatable("config.option.bounceHeight"))
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 2.5f).step(0.05f).formatValue(val -> Text.translatable(String.format("%.2f", val) + "x")))
            .stateManager(StateManager.createSimple(1F, () -> CONFIG.instance().coreBounceHeight, newVal -> CONFIG.instance().coreBounceHeight = newVal))
            .build();
    public static EvilOption<Float> o_coreBounceSpeed = EvilOption.<Float>createBuilder()
            .group(OptionGroups.BOUNCE_SPEED)
            .name(Text.translatable("config.option.bounceSpeed"))
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 2.5f).step(0.05f).formatValue(val -> Text.translatable(String.format("%.2f", val) + "x")))
            .stateManager(StateManager.createSimple(1F, () -> CONFIG.instance().coreBounceSpeed, newVal -> CONFIG.instance().coreBounceSpeed = newVal))
            .build();
    public static EvilOption<Float> o_coreDelay = EvilOption.<Float>createBuilder()
            .group(OptionGroups.DELAY)
            .name(Text.translatable("config.option.delay"))
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(-2.5f, 2.5f).step(0.1f).formatValue(CrystalChams.SECONDS_FORMATTER))
            .stateManager(StateManager.createSimple(0F, () -> CONFIG.instance().coreDelay, newVal -> CONFIG.instance().coreDelay = newVal))
            .build();
    public static EvilOption<Float> o_coreScale = EvilOption.<Float>createBuilder()
            .name(Text.translatable("config.option.scale"))
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 2.5f).step(0.05f).formatValue(val -> Text.translatable(String.format("%.2f", val) + "x")))
            .stateManager(StateManager.createSimple(1F, () -> CONFIG.instance().coreScale, newVal -> CONFIG.instance().coreScale = newVal))
            .group(OptionGroups.SCALE)
            .build();
    public static EvilOption<Color> o_coreColor = CrystalChams.createColorOption(Text.translatable("config.group.color"), StateManager.createSimple(Color.WHITE, () -> CONFIG.instance().coreColor, newVal -> CONFIG.instance().coreColor = newVal), OptionGroups.COLOR);
    public static EvilOption<Float> o_coreAlpha = CrystalChams.createFloatOptionPercent(Text.translatable("config.option.opacity"), StateManager.createSimple(1f, () -> CONFIG.instance().coreAlpha, newVal -> CONFIG.instance().coreAlpha = newVal), OptionGroups.OPACITY);
    public static EvilOption<Integer> o_coreBlockLightLevel = CrystalChams.createBlockLightLevelOption(StateManager.createSimple(-1, () -> CONFIG.instance().coreBlockLightLevel, newVal -> CONFIG.instance().coreBlockLightLevel = newVal));
    public static EvilOption<Integer> o_coreSkyLightLevel = CrystalChams.createSkyLightLevelOption(StateManager.createSimple(-1, () -> CONFIG.instance().coreSkyLightLevel, newVal -> CONFIG.instance().coreSkyLightLevel = newVal));
    public static EvilOption<RenderMode> o_coreRenderLayer = CrystalChams.createRenderModeOption(Text.translatable("config.option.renderMode"),
            StateManager.createSimple(RenderMode.DEFAULT, () -> CONFIG.instance().coreRenderLayer, newVal -> CONFIG.instance().coreRenderLayer = newVal),
            OptionGroups.RENDER_MODE);
    public static EvilOption<Boolean> o_coreCulling = CrystalChams.createBooleanOption(Text.translatable(CrystalChams.SEPARATOR).append(Text.translatable("config.option.culling")),
            StateManager.createSimple(false, () -> CONFIG.instance().coreCulling, newVal -> CONFIG.instance().coreCulling = newVal),
            OptionGroups.CULLED);
    @Updatable
    public static EvilOption<Boolean> o_coreRainbow = CrystalChams.createBooleanOption(Text.translatable("config.option.rainbow"),
            StateManager.createSimple(false, () -> CONFIG.instance().coreRainbow, newVal -> CONFIG.instance().coreRainbow = newVal),
            OptionGroups.RAINBOW);
    public static EvilOption<Float> o_coreRainbowSpeed = EvilOption.<Float>createBuilder()
            .name(Text.translatable(CrystalChams.SEPARATOR).append(Text.translatable("config.option.rainbowSpeed")))
            .group(OptionGroups.RAINBOW_SPEED)
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 10f).step(0.1f).formatValue(CrystalChams.MULTIPLIER_FORMATTER_ONE_PLACE))
//            .controller(integerOption -> CustomIntegerSliderController.create(integerOption).step(1).range(1, 10).formatValue(value -> Text.translatable(value + "x")))
            .stateManager(StateManager.createSimple(2F, () -> CONFIG.instance().coreRainbowSpeed, newVal -> CONFIG.instance().coreRainbowSpeed = newVal))
            .build();
    public static EvilOption<Float> o_coreRainbowDelay = EvilOption.<Float>createBuilder()
            .name(Text.translatable(CrystalChams.SEPARATOR).append(Text.translatable("config.option.rainbowDelay")))
            .group(OptionGroups.RAINBOW_DELAY)
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(-2.5f, 2.5f).step(0.1f).formatValue(val -> Text.translatable(String.format("%.1f", val).replace(".0", "") + (Math.abs(val) == 1 ? " second" : " seconds"))))
            .stateManager(StateManager.createSimple(0F, () -> CONFIG.instance().coreRainbowDelay, newVal -> CONFIG.instance().coreRainbowDelay = newVal))
            .build();
    public static EvilOption<Float> o_coreRainbowSaturation = CrystalChams.createFloatOptionPercent(Text.translatable(CrystalChams.SEPARATOR).append(Text.translatable("config.option.rainbowSaturation")), StateManager.createSimple(1F, () -> CONFIG.instance().coreRainbowSaturation, newVal -> CONFIG.instance().coreRainbowSaturation = newVal), OptionGroups.RAINBOW_SATURATION);
    public static EvilOption<Float> o_coreRainbowBrightness = CrystalChams.createFloatOptionPercent(Text.translatable(CrystalChams.SEPARATOR).append(Text.translatable("config.option.rainbowBrightness")), StateManager.createSimple(1F, () -> CONFIG.instance().coreRainbowBrightness, newVal -> CONFIG.instance().coreRainbowBrightness = newVal), OptionGroups.RAINBOW_BRIGHTNESS);
    @Updatable
    public static EvilOption<Boolean> o_coreAlphaAnimation = CrystalChams.createBooleanOption(Text.translatable("config.option.animateOpacity"),
            StateManager.createSimple(false, () -> CONFIG.instance().coreAlphaAnimation, newVal -> CONFIG.instance().coreAlphaAnimation = newVal),
            OptionGroups.ANIMATE_ALPHA);
    public static EvilOption<Float> o_coreStartAlpha = CrystalChams.createFloatOptionPercent(Text.translatable(CrystalChams.SEPARATOR).append(Text.translatable("config.option.startingOpacity")), StateManager.createSimple(0F, () -> CONFIG.instance().coreStartAlpha, newVal -> CONFIG.instance().coreStartAlpha = newVal), OptionGroups.STARTING_ALPHA);
    public static EvilOption<Float> o_coreAlphaDelay = EvilOption.<Float>createBuilder()
            .name(Text.translatable(CrystalChams.SEPARATOR).append(Text.translatable("config.option.delay")))
            .group(OptionGroups.ALPHA_ANIMATION_DELAY)
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 2f).step(0.1f).formatValue(val -> Text.translatable(String.format("%.1f", val) + "s")))
            .stateManager(StateManager.createSimple(0F, () -> CONFIG.instance().coreAlphaDelay, newVal -> CONFIG.instance().coreAlphaDelay = newVal))
            .build();
    public static EvilOption<Float> o_coreAlphaAnimDuration = EvilOption.<Float>createBuilder()
            .name(Text.translatable(CrystalChams.SEPARATOR).append(Text.translatable("config.option.duration")))
            .group(OptionGroups.ALPHA_ANIMATION_DURATION)
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 2f).step(0.1f).formatValue(val -> Text.translatable(String.format("%.1f", val) + "s")))
            .stateManager(StateManager.createSimple(1F, () -> CONFIG.instance().coreAlphaAnimDuration, newVal -> CONFIG.instance().coreAlphaAnimDuration = newVal))
            .build();
    public static EvilOption<Easings> o_coreAlphaEasing = CrystalChams.createEasingOption(StateManager.createSimple(Easings.OFF, () -> CONFIG.instance().coreAlphaEasing, newVal -> CONFIG.instance().coreAlphaEasing = newVal), OptionGroups.ALPHA_EASING);
    @Updatable
    public static EvilOption<Boolean> o_coreScaleAnimation = CrystalChams.createBooleanOption(Text.translatable("config.option.animateScale"), StateManager.createSimple(false, () -> CONFIG.instance().coreScaleAnimation, newVal -> CONFIG.instance().coreScaleAnimation = newVal), OptionGroups.ANIMATE_SCALE);
    public static EvilOption<Float> o_coreStartScale = EvilOption.<Float>createBuilder()
            .name(Text.translatable(CrystalChams.SEPARATOR).append(Text.translatable("config.option.startingScale")))
            .group(OptionGroups.STARTING_SCALE)
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 2f).step(0.05f).formatValue(val -> Text.translatable(String.format("%.2f", val) + "x")))
            .stateManager(StateManager.createSimple(0F, () -> CONFIG.instance().coreStartScale, newVal -> CONFIG.instance().coreStartScale = newVal))
            .build();
    public static EvilOption<Float> o_coreScaleDelay = EvilOption.<Float>createBuilder()
            .name(Text.translatable(CrystalChams.SEPARATOR).append(Text.translatable("config.option.delay")))
            .group(OptionGroups.SCALE_ANIMATION_DELAY)
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 2f).step(0.1f).formatValue(val -> Text.translatable(String.format("%.1f", val) + "s")))
            .stateManager(StateManager.createSimple(0F, () -> CONFIG.instance().coreScaleDelay, newVal -> CONFIG.instance().coreScaleDelay = newVal))
            .build();
    public static EvilOption<Float> o_coreScaleAnimDuration = EvilOption.<Float>createBuilder()
            .name(Text.translatable(CrystalChams.SEPARATOR).append(Text.translatable("config.option.duration")))
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 2f).step(0.1f).formatValue(val -> Text.translatable(String.format("%.1f", val) + "s")))
            .stateManager(StateManager.createSimple(1F, () -> CONFIG.instance().coreScaleAnimDuration, newVal -> CONFIG.instance().coreScaleAnimDuration = newVal))
            .group(OptionGroups.SCALE_ANIMATION_DURATION)
            .build();
    public static EvilOption<Easings> o_coreScaleEasing = CrystalChams.createEasingOption(StateManager.createSimple(Easings.OFF, () -> CONFIG.instance().coreScaleEasing, newVal -> CONFIG.instance().coreScaleEasing = newVal), OptionGroups.SCALE_EASING);
    //TODO: look into drop-down widget
    @Updatable
    public static EvilOption<Boolean> o_renderFrames = CrystalChams.createBooleanOption(Text.translatable("config.option.renderFrames"),
            StateManager.createSimple(true, () -> CONFIG.instance().renderFrames, newVal -> CONFIG.instance().renderFrames = newVal), OptionGroups.RENDER);
    @Updatable
    public static EvilOption<Boolean> o_renderBeam = CrystalChams.createBooleanOption(Text.translatable("config.option.renderBeam"),
            StateManager.createSimple(true, () -> CONFIG.instance().renderBeam, newVal -> CONFIG.instance().renderBeam = newVal),
            OptionGroups.RENDER);
    public static EvilOption<Color> o_beam1Color = CrystalChams.createColorOption(Text.translatable("config.group.color"), StateManager.createSimple(Color.WHITE, () -> CONFIG.instance().beam1Color, newVal -> CONFIG.instance().beam1Color = newVal), OptionGroups.COLOR);
    public static EvilOption<Float> o_beam1Alpha = CrystalChams.createFloatOptionPercent(Text.translatable("config.option.opacity"), StateManager.createSimple(1F, () -> CONFIG.instance().beam1Alpha, newVal -> CONFIG.instance().beam1Alpha = newVal), OptionGroups.OPACITY);
    public static EvilOption<Integer> o_beam1BlockLightLevel = CrystalChams.createBlockLightLevelOption(StateManager.createSimple(-1, () -> CONFIG.instance().beam1BlockLightLevel, newVal -> CONFIG.instance().beam1BlockLightLevel = newVal));
    public static EvilOption<Integer> o_beam1SkyLightLevel = CrystalChams.createSkyLightLevelOption(StateManager.createSimple(-1, () -> CONFIG.instance().beam1SkyLightLevel, newVal -> CONFIG.instance().beam1SkyLightLevel = newVal));
    @Updatable
    public static EvilOption<Boolean> o_beam1Rainbow = CrystalChams.createBooleanOption(Text.translatable("config.option.opacity"),
            StateManager.createSimple(false, () -> CONFIG.instance().beam1Rainbow, newVal -> CONFIG.instance().beam1Rainbow = newVal),
            OptionGroups.RAINBOW);
    public static EvilOption<Float> o_beam1RainbowSpeed = EvilOption.<Float>createBuilder()
            .name(Text.translatable(CrystalChams.SEPARATOR).append(Text.translatable("config.option.rainbowSpeed")))
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 10f).step(0.1f).formatValue(CrystalChams.MULTIPLIER_FORMATTER_ONE_PLACE))
            .stateManager(StateManager.createSimple(2F, () -> CONFIG.instance().beam1RainbowSpeed, newVal -> CONFIG.instance().beam1RainbowSpeed = newVal))
            .group(OptionGroups.RAINBOW_SPEED)
            .build();
    public static EvilOption<Float> o_beam1RainbowDelay = EvilOption.<Float>createBuilder()
            .name(Text.translatable(CrystalChams.SEPARATOR).append(Text.translatable("config.option.rainbowDelay")))
//            .controller(integerOption -> CustomIntegerSliderController.create(integerOption).step(1).range(-500, 500).formatValue(integer -> Text.translatable(integer + "ms")))
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(-2.5f, 2.5f).step(0.1f).formatValue(CrystalChams.SECONDS_FORMATTER))
            .stateManager(StateManager.createSimple(0F, () -> CONFIG.instance().beam1RainbowDelay, newVal -> CONFIG.instance().beam1RainbowDelay = newVal))
            .group(OptionGroups.DELAY)
            .build();
    public static EvilOption<Float> o_beam1RainbowSaturation = CrystalChams.createFloatOptionPercent(Text.translatable(CrystalChams.SEPARATOR).append(Text.translatable("config.option.rainbowSaturation")), StateManager.createSimple(1F, () -> CONFIG.instance().beam1RainbowSaturation, newVal -> CONFIG.instance().beam1RainbowSaturation = newVal), OptionGroups.RAINBOW_SATURATION);
    public static EvilOption<Float> o_beam1RainbowBrightness = CrystalChams.createFloatOptionPercent(Text.translatable(CrystalChams.SEPARATOR).append(Text.translatable("config.option.rainbowBrightness")), StateManager.createSimple(1F, () -> CONFIG.instance().beam1RainbowBrightness, newVal -> CONFIG.instance().beam1RainbowBrightness = newVal), OptionGroups.RAINBOW_BRIGHTNESS);
    public static EvilOption<Color> o_beam2Color = CrystalChams.createColorOption(Text.translatable("config.group.color"), StateManager.createSimple(new Color(0, 0, 0), () -> CONFIG.instance().beam2Color, newVal -> CONFIG.instance().beam2Color = newVal), OptionGroups.COLOR);
    public static EvilOption<Float> o_beam2Alpha = CrystalChams.createFloatOptionPercent(Text.translatable("config.option.opacity"), StateManager.createSimple(1F, () -> CONFIG.instance().beam2Alpha, newVal -> CONFIG.instance().beam2Alpha = newVal), OptionGroups.OPACITY);
    public static EvilOption<Integer> o_beam2BlockLightLevel = CrystalChams.createBlockLightLevelOption(StateManager.createSimple(-1, () -> CONFIG.instance().beam2BlockLightLevel, newVal -> CONFIG.instance().beam2BlockLightLevel = newVal));
    public static EvilOption<Integer> o_beam2SkyLightLevel = CrystalChams.createSkyLightLevelOption(StateManager.createSimple(-1, () -> CONFIG.instance().beam2SkyLightLevel, newVal -> CONFIG.instance().beam2SkyLightLevel = newVal));

    @Updatable
    public static EvilOption<Boolean> o_beam2Rainbow = CrystalChams.createBooleanOption(Text.translatable("config.option.rainbow"),
            StateManager.createSimple(false, () -> CONFIG.instance().beam2Rainbow, newVal -> CONFIG.instance().beam2Rainbow = newVal),
            OptionGroups.RAINBOW);
    public static EvilOption<Float> o_beam2RainbowSpeed = EvilOption.<Float>createBuilder()
            .name(Text.translatable(CrystalChams.SEPARATOR).append(Text.translatable("config.option.rainbowSpeed")))
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 10f).step(0.1f).formatValue(CrystalChams.MULTIPLIER_FORMATTER_ONE_PLACE))
            .stateManager(StateManager.createSimple(2F, () -> CONFIG.instance().beam2RainbowSpeed, newVal -> CONFIG.instance().beam2RainbowSpeed = newVal))
            .group(OptionGroups.RAINBOW_SPEED)
            .build();
    public static EvilOption<Float> o_beam2RainbowDelay = EvilOption.<Float>createBuilder()
            .name(Text.translatable(CrystalChams.SEPARATOR).append(Text.translatable("config.option.rainbowDelay")))
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(-2.5f, 2.5f).step(0.1f).formatValue(val -> Text.translatable(String.format("%.1f", val).replace(".0", "") + (Math.abs(val) == 1 ? " second" : " seconds"))))
            .stateManager(StateManager.createSimple(0F, () -> CONFIG.instance().beam2RainbowDelay, newVal -> CONFIG.instance().beam2RainbowDelay = newVal))
            .group(OptionGroups.RAINBOW_DELAY)
            .build();
    public static EvilOption<Float> o_beam2RainbowSaturation = CrystalChams.createFloatOptionPercent(Text.translatable(CrystalChams.SEPARATOR).append(Text.translatable("config.option.rainbowDelay")), StateManager.createSimple(1F, () -> CONFIG.instance().beam2RainbowSaturation, newVal -> CONFIG.instance().beam2RainbowSaturation = newVal), OptionGroups.RAINBOW_SATURATION);
    public static EvilOption<Float> o_beam2RainbowBrightness = CrystalChams.createFloatOptionPercent(Text.translatable(CrystalChams.SEPARATOR).append(Text.translatable("config.option.rainbowDelay")), StateManager.createSimple(1F, () -> CONFIG.instance().beam2RainbowBrightness, newVal -> CONFIG.instance().beam2RainbowBrightness = newVal), OptionGroups.RAINBOW_BRIGHTNESS);
    public static EvilOption<Float> o_beam1Radius = EvilOption.<Float>createBuilder()
            .name(Text.translatable("config.option.radius"))
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(-1f, 1f).step(0.05f).formatValue(CrystalChams.BLOCKS_FORMATTER_TWO_PLACES))
            .stateManager(StateManager.createSimple(0.75F, () -> CONFIG.instance().beam1Radius, newVal -> CONFIG.instance().beam1Radius = newVal))
            .group(OptionGroups.BEAM_RADIUS)
            .build();
    public static EvilOption<Float> o_beam2Radius = EvilOption.<Float>createBuilder()
            .name(Text.translatable("config.option.radius"))
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(-1f, 1f).step(0.05f).formatValue(CrystalChams.BLOCKS_FORMATTER_TWO_PLACES))
            .stateManager(StateManager.createSimple(0.15F, () -> CONFIG.instance().beam2Radius, newVal -> CONFIG.instance().beam2Radius = newVal))
            .group(OptionGroups.BEAM_RADIUS)
            .build();
    public static EvilOption<Integer> o_beamSides = EvilOption.<Integer>createBuilder()
            .name(Text.translatable("config.option.sides"))
            .controller(integerOption -> IntegerSliderControllerBuilder.create(integerOption).step(1).range(2, 64))
            .stateManager(StateManager.createSimple(8, () -> CONFIG.instance().beamSides, newVal -> CONFIG.instance().beamSides = newVal))
            .build();
    public static EvilOption<Float> o_beamScrollSpeed = EvilOption.<Float>createBuilder()
            .name(Text.translatable("config.option.scrollSpeed"))
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(-2f, 2f).step(0.05f).formatValue(CrystalChams.MULTIPLIER_FORMATTER))
            .stateManager(StateManager.createSimple(1F, () -> CONFIG.instance().beamScrollSpeed, newVal -> CONFIG.instance().beamScrollSpeed = newVal))
//            .group()
            .build();
    public static EvilOption<RenderMode> o_beamRenderLayer = CrystalChams.createRenderModeOption(Text.translatable("config.option.renderMode"),
            StateManager.createSimple(RenderMode.DEFAULT, () -> CONFIG.instance().beamRenderLayer, newVal -> CONFIG.instance().beamRenderLayer = newVal),
            OptionGroups.RENDER_MODE);
    public static EvilOption<Boolean> o_beamCulling = CrystalChams.createBooleanOption(Text.translatable(CrystalChams.SEPARATOR).append(Text.translatable("config.option.culling")),

            StateManager.createSimple(false, () -> CONFIG.instance().beamCulling, newVal -> CONFIG.instance().beamCulling = newVal),
            OptionGroups.CULLED);
    public static ListOption<ModelPartOptions> o_frameList = ListOption.<ModelPartOptions>createBuilder()
            .name(Text.translatable("config.option.frameList"))
            .controller(ModelPartControllerBuilder::new)
            .binding(List.of(new ModelPartOptions(), new ModelPartOptions()), () -> CONFIG.instance().frameList, newVal -> CONFIG.instance().frameList = newVal)
            .initial(ModelPartOptions::new)
            .build();


    public static void update(Option<Boolean> booleanOption, Boolean aBoolean) {
        if (booleanOption.equals(o_modEnabled)) {
            o_shadowAlpha.setAvailable(aBoolean);
            o_shadowRadius.setAvailable(aBoolean);
            o_randomizeAge.setAvailable(aBoolean);

            o_baseRenderMode.setAvailable(aBoolean);
            o_renderCore.setAvailable(aBoolean);
            o_renderFrames.setAvailable(aBoolean);
            o_renderBeam.setAvailable(aBoolean);
        } else if (booleanOption.equals(o_renderCore)) {
            boolean available = o_renderCore.available() && aBoolean;
            o_coreOffset.setAvailable(available);
            o_coreRotationSpeed.setAvailable(available);
            o_coreBounceHeight.setAvailable(available);
            o_coreBounceSpeed.setAvailable(available);
            o_coreDelay.setAvailable(available);
            o_coreScale.setAvailable(available);
            o_coreColor.setAvailable(available);
            o_coreAlpha.setAvailable(available);
            o_coreBlockLightLevel.setAvailable(available);
            o_coreSkyLightLevel.setAvailable(available);
            o_coreRenderLayer.setAvailable(available);
            o_coreRainbow.setAvailable(available);
            o_coreCulling.setAvailable(available);
            o_coreColor.setAvailable(available && !o_coreRainbow.stateManager().get());
        } else if (booleanOption.equals(o_coreRainbow)) {
            boolean available = o_coreRainbow.available() && aBoolean;
            o_coreRainbowSpeed.setAvailable(available);
            o_coreRainbowDelay.setAvailable(available);
            o_coreRainbowSaturation.setAvailable(available);
            o_coreRainbowBrightness.setAvailable(available);
        } else if (booleanOption.equals(o_renderFrames)) {
            o_frameList.setAvailable(o_renderFrames.available() && aBoolean);
        } else if (booleanOption.equals(o_renderBeam)) {
            boolean available = o_renderBeam.available() && aBoolean;
            o_beam1Color.setAvailable(available);
            o_beam1Alpha.setAvailable(available);
            o_beam1Rainbow.setAvailable(available);
            o_beam1Radius.setAvailable(available);
            o_beam1BlockLightLevel.setAvailable(available);
            o_beam1SkyLightLevel.setAvailable(available);
            o_beam2Color.setAvailable(available);
            o_beam2Alpha.setAvailable(available);
            o_beam2Rainbow.setAvailable(available);
            o_beam2Radius.setAvailable(available);
            o_beam2BlockLightLevel.setAvailable(available);
            o_beam2SkyLightLevel.setAvailable(available);
            o_beamSides.setAvailable(available);
            o_beamScrollSpeed.setAvailable(available);
            o_beamRenderLayer.setAvailable(available);
            o_beamCulling.setAvailable(available);
        } else if (booleanOption.equals(o_beam1Rainbow)) {
            boolean available = o_beam1Rainbow.available() && aBoolean;
            o_beam1RainbowDelay.setAvailable(available);
            o_beam1RainbowSpeed.setAvailable(available);
            o_beam1RainbowBrightness.setAvailable(available);
            o_beam1RainbowSaturation.setAvailable(available);
        } else if (booleanOption.equals(o_beam2Rainbow)) {
            boolean available = o_beam2Rainbow.available() && aBoolean;
            o_beam2RainbowDelay.setAvailable(available);
            o_beam2RainbowSpeed.setAvailable(available);
            o_beam2RainbowBrightness.setAvailable(available);
            o_beam2RainbowSaturation.setAvailable(available);
        } else if (booleanOption.equals(o_baseRainbow)) {
            boolean available = o_baseRainbow.stateManager().get() && aBoolean;
            o_baseRainbowSpeed.setAvailable(available);
            o_baseRainbowDelay.setAvailable(available);
            o_baseRainbowSaturation.setAvailable(available);
            o_baseRainbowBrightness.setAvailable(available);
            o_baseColor.setAvailable(available);
        }
        specialUpdate(o_baseRenderMode, o_baseRenderMode.pendingValue());
    }

    private static void specialUpdate(Option<CrystalChams.BaseRenderMode> baseRenderModeOption, Object baseRenderMode) {
        boolean available = baseRenderModeOption.available() && (baseRenderMode != CrystalChams.BaseRenderMode.NEVER);
        o_baseOffset.setAvailable(available);
        o_baseScale.setAvailable(available);
        o_baseColor.setAvailable(available);
        o_baseAlpha.setAvailable(available);
        o_baseBlockLightLevel.setAvailable(available);
        o_baseSkyLightLevel.setAvailable(available);
        o_baseRenderLayer.setAvailable(available);
        o_baseRainbow.setAvailable(available);
        o_baseRotation.setAvailable(available);
        o_baseCulling.setAvailable(available);
    }

    public static Screen getConfigScreen(Screen parent) {
        return YetAnotherConfigLib.create(CONFIG, (defaults, config, builder) -> builder
                        .title(Text.translatable("config.title"))
                        .category(ConfigCategory.createBuilder()
                                .name(Text.translatable("config.category.general"))
                                .option(o_modEnabled)
                                .group(OptionGroup.createBuilder()
                                        .name(Text.translatable("config.group.shadow"))
                                        .option(o_shadowRadius)
                                        .option(o_shadowAlpha)
                                        .build())
                                .group(OptionGroup.createBuilder()
                                        .name(Text.translatable("config.group.config"))
                                        .option(ButtonOption.createBuilder()
                                                .name(Text.translatable("config.option.copyConfig"))
                                                .description(OptionDescription.createBuilder().text(Text.translatable("config.option.copyConfig.description")).build())
                                                .action((yaclScreen, buttonOption) -> CrystalChams.mc.keyboard.setClipboard(CrystalChams.gson.toJson(CONFIG.instance())))
                                                .text(Text.translatable("config.option.copyConfig.text"))
                                                .build())
                                        .option(ButtonOption.createBuilder()
                                                .name(Text.translatable("config.option.loadConfig"))
                                                .description(OptionDescription.createBuilder().text(Text.translatable("config.option.loadConfig.description")).build())
                                                .text(Text.translatable("config.option.loadConfig.text"))
                                                //TODO
                                                .action((yaclScreen, buttonOption) -> {
                                                    //compared to the previous version, this is the second-worst way to load a config file. BUT it works.
                                                    try {
                                                        ChamsConfig c = CrystalChams.gson.fromJson(CrystalChams.mc.keyboard.getClipboard(), ChamsConfig.class);
                                                        Arrays.stream(ChamsConfig.class.getDeclaredFields()).filter(field -> field.getName().startsWith("o_") && !field.getName().equals("CONFIG")).forEach(field -> {
                                                            try {
//                                                                System.out.println(field.getName() + ":" + ChamsConfig.class.getField(field.getName().replace("o_", "")).get(c));
                                                                ((Option) field.get(null)).requestSet(ChamsConfig.class.getField(field.getName().replace("o_", "")).get(c));
                                                            } catch (IllegalAccessException | NoSuchFieldException e) {
                                                                CrystalChams.LOGGER.error("Error while loading config. what the fuck man");
                                                            }
                                                        });
//                                                        CrystalChams.unleashHell();
                                                    } catch (JsonSyntaxException e) {
                                                        CrystalChams.LOGGER.info("Invalid config file!");
                                                    }
                                                })
                                                .build())
                                        .option(ButtonOption.createBuilder()
                                                .name(Text.translatable("config.option.randomize"))
                                                .description(OptionDescription.of(Text.translatable("config.option.randomize.description")))
                                                .action((yaclScreen, buttonOption) -> CrystalChams.randomizeOptions())
                                                .text(Text.translatable("config.option.randomize.text"))
                                                .build())
                                        .build())
                                .group(OptionGroup.createBuilder()
                                        .name(Text.translatable("config.group.misc"))
                                        .option(o_randomizeAge)
                                        .option(o_renderHitbox)
//                                        .option(o_showAnimations)
                                        .option(o_previewScale)
                                        .option(LabelOption.create(Text.translatable("config.label.previewHint")))
                                        .build()
                                )
                                .build())
                        .category(ConfigCategory.createBuilder()
                                .name(Text.translatable("config.category.core"))
                                .option(o_renderCore)
                                .group(OptionGroup.createBuilder()
                                        .name(Text.translatable("config.group.transform"))
                                        .option(o_coreOffset)
                                        .option(o_coreScale)
                                        .build())
                                .group(OptionGroup.createBuilder()
                                        .name(Text.translatable("config.group.movement"))
                                        .option(o_coreRotationSpeed)
                                        .option(o_coreBounceHeight)
                                        .option(o_coreBounceSpeed)
                                        .option(o_coreDelay)
                                        .build())
                                .group(OptionGroup.createBuilder()
                                        .name(Text.translatable("config.group.color"))
                                        .option(o_coreColor)
                                        .option(o_coreAlpha)
                                        .option(o_coreRainbow)
                                        .option(o_coreRainbowSpeed)
                                        .option(o_coreRainbowDelay)
                                        .option(o_coreRainbowSaturation)
                                        .option(o_coreRainbowBrightness)
                                        .build())
                                .group(OptionGroup.createBuilder()
                                        .name(Text.translatable("config.group.rendering"))
                                        .option(o_coreBlockLightLevel)
                                        .option(o_coreSkyLightLevel)
                                        .option(o_coreRenderLayer)
                                        .option(o_coreCulling)
                                        .build())
                                .group(OptionGroup.createBuilder()
                                        .name(Text.translatable("config.group.animation"))
                                        .option(o_coreAlphaAnimation)
                                        .option(o_coreStartAlpha)
                                        .option(o_coreAlphaAnimDuration)
                                        .option(o_coreAlphaDelay)
                                        .option(o_coreAlphaEasing)
                                        .option(o_coreScaleAnimation)
                                        .option(o_coreStartScale)
                                        .option(o_coreScaleAnimDuration)
                                        .option(o_coreScaleDelay)
                                        .option(o_coreScaleEasing)
                                        .build())
                                .build())
                        .category(ConfigCategory.createBuilder()
                                .name(Text.translatable("config.category.frames"))
                                .option(o_renderFrames)
                                .group(o_frameList)
                                .build())
                        .category(ConfigCategory.createBuilder()
                                .name(Text.translatable("config.category.base"))
                                .option(o_baseRenderMode)
                                .group(OptionGroup.createBuilder()
                                        .name(Text.translatable("config.group.transform"))
                                        .option(o_baseOffset)
                                        .option(o_baseScale)
                                        .option(o_baseRotation)
                                        //TODO: base rotation speed?
                                        .build()
                                )
                                .group(OptionGroup.createBuilder()
                                        .name(Text.translatable("config.group.color"))
                                        .option(o_baseColor)
                                        .option(o_baseAlpha)
                                        .option(o_baseRainbow)
                                        .option(o_baseRainbowSpeed)
                                        .option(o_baseRainbowDelay)
                                        .option(o_baseRainbowSaturation)
                                        .option(o_baseRainbowBrightness)
                                        .build())
                                .group(OptionGroup.createBuilder()
                                        .name(Text.translatable("config.group.rendering"))
                                        .option(o_baseBlockLightLevel)
                                        .option(o_baseSkyLightLevel)
                                        .option(o_baseRenderLayer)
                                        .option(o_baseCulling)
                                        .build())
                                .build())
                        .category(ConfigCategory.createBuilder()
                                .name(Text.translatable("config.category.beam"))
                                .option(o_renderBeam)
                                .group(OptionGroup.createBuilder()
                                        .name(Text.translatable("config.group.start"))
                                        .option(o_beam1Radius)
                                        .option(o_beam1Color)
                                        .option(o_beam1Alpha)
                                        .option(o_beam1Rainbow)
                                        .option(o_beam1RainbowSpeed)
                                        .option(o_beam1RainbowDelay)
                                        .option(o_beam1RainbowSaturation)
                                        .option(o_beam1RainbowBrightness)
                                        .option(o_beam1BlockLightLevel)
                                        .option(o_beam1SkyLightLevel)
                                        .build())
                                .group(OptionGroup.createBuilder()
                                        .name(Text.translatable("config.group.end"))
                                        .option(o_beam2Radius)
                                        .option(o_beam2Color)
                                        .option(o_beam2Alpha)
                                        .option(o_beam2Rainbow)
                                        .option(o_beam2RainbowSpeed)
                                        .option(o_beam2RainbowDelay)
                                        .option(o_beam2RainbowSaturation)
                                        .option(o_beam2RainbowBrightness)
                                        .option(o_beam2BlockLightLevel)
                                        .option(o_beam2SkyLightLevel)
                                        .build())
                                .group(OptionGroup.createBuilder()
                                        .name(Text.translatable("config.group.extras"))
                                        .option(o_beamSides)
                                        .option(o_beamScrollSpeed)
                                        .option(o_beamRenderLayer)
                                        .option(o_beamCulling)
                                        .build())
                                .build()))
                .generateScreen(parent);
    }
}