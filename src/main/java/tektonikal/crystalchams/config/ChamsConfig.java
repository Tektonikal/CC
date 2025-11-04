package tektonikal.crystalchams.config;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.EnumControllerBuilder;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import dev.isxander.yacl3.config.v2.impl.serializer.GsonConfigSerializer;
import dev.isxander.yacl3.impl.controller.ColorControllerBuilderImpl;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import tektonikal.crystalchams.CrystalChams;
import tektonikal.crystalchams.OptionGroups;
import tektonikal.crystalchams.annotation.Updatable;
import tektonikal.crystalchams.stupidfuckingboilerplate.*;
import tektonikal.crystalchams.util.Easings;

import java.awt.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ChamsConfig {
    public static final ConfigClassHandler<ChamsConfig> CONFIG = ConfigClassHandler.createBuilder(ChamsConfig.class)
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve("crystalchams.json"))
                    .build())
            .build();
    public static Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
            .serializeNulls()
            .registerTypeHierarchyAdapter(Color.class, new GsonConfigSerializer.ColorTypeAdapter())
            .setPrettyPrinting()
            .create();
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
        @SerialEntry public BaseRenderMode baseRenderMode = BaseRenderMode.DEFAULT;
        @SerialEntry public float baseOffset = 0F;
        @SerialEntry public float baseScale = 1F;
        @SerialEntry public int baseRotation = 0;
        @SerialEntry public Color baseColor = Color.decode("#FFFFFF");
        @SerialEntry public float baseAlpha = 1.0F;
        @SerialEntry public int baseLightLevel = -1;
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
            @SerialEntry public int coreLightLevel = -1;
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
                @SerialEntry public float coreStartAlpha = 0F;
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
            @SerialEntry public int beam1LightLevel = -1;
            @SerialEntry public Color beam2Color = Color.decode("#000000");
            @SerialEntry public float beam2Alpha = 1;
            @SerialEntry public float beam2Radius = 0.15F;
            @SerialEntry public int beam2LightLevel = -1;
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
            @SerialEntry public RenderMode beamRenderMode = RenderMode.DEFAULT;
                @SerialEntry public boolean beamCulling = false;

    //@formatter:on
    @Updatable
    public static Option<Boolean> o_modEnabled = CrystalChams.createBooleanOption("Mod Enabled",
            "I wonder what this one does!",
            StateManager.createSimple(true, () -> CONFIG.instance().modEnabled, newVal -> CONFIG.instance().modEnabled = newVal));
    public static Option<Boolean> o_showAnimations = CrystalChams.createBooleanOption("UI Animations",
            "Toggle some neat little animations for the UI.",
            StateManager.createSimple(true, () -> CONFIG.instance().showAnimations, newVal -> CONFIG.instance().showAnimations = newVal));
    public static Option<Boolean> o_renderHitbox = CrystalChams.createBooleanOption("Render Hitbox",
            "Renders the crystal's hitbox, regardless of the game's setting. Compatible with hitbox mods.",
            StateManager.createSimple(false, () -> CONFIG.instance().renderHitbox, newVal -> CONFIG.instance().renderHitbox = newVal));
    public static Option<Float> o_previewScale = Option.<Float>createBuilder()
            .name(Text.of("Preview Scale"))
            .controller(floatOption -> CustomFloatSliderControllerBuilder.create(floatOption).range(0f, 2f).step(0.01f).formatValue(CrystalChams.PERCENT_FORMATTER))
            .stateManager(StateManager.createSimple(1F, () -> CONFIG.instance().previewScale, newVal -> CONFIG.instance().previewScale = newVal))
            .build();
    public static Option<Boolean> o_randomizeAge = Option.<Boolean>createBuilder()
            .name(Text.of("Randomize Age"))
            //TODO: add proper description
            .controller(CustomTickBoxControllerBuilder::new)
            .stateManager(StateManager.createSimple(true, () -> CONFIG.instance().randomizeAge, newVal -> CONFIG.instance().randomizeAge = newVal))
            .build();
    public static Option<Float> o_shadowRadius = Option.<Float>createBuilder()
            .name(Text.of("Shadow Radius"))
            .controller(floatOption -> CustomFloatSliderControllerBuilder.create(floatOption).range(0f, 2f).step(0.1f).formatValue(CrystalChams.BLOCKS_FORMATTER))
            .stateManager(StateManager.createSimple(0.5F, () -> CONFIG.instance().shadowRadius, newVal -> CONFIG.instance().shadowRadius = newVal))
            .build();
    public static Option<Float> o_shadowAlpha = CrystalChams.createFloatOptionPercent("Shadow Opacity", "", StateManager.createSimple(0.5F, () -> CONFIG.instance().shadowAlpha, newVal -> CONFIG.instance().shadowAlpha = newVal));
    public static EvilOption<Float> o_baseScale = EvilOption.<Float>createBuilder()
            .name(Text.of("Scale"))
            .controller(floatOption -> CustomFloatSliderControllerBuilder.create(floatOption).range(0f, 2f).step(0.05f).formatValue(CrystalChams.MULTIPLIER_FORMATTER))
            .stateManager(StateManager.createSimple(1F, () -> CONFIG.instance().baseScale, newVal -> CONFIG.instance().baseScale = newVal))
            .group(OptionGroups.SCALE)
            .build();
    public static EvilOption<Integer> o_baseRotation = EvilOption.<Integer>createBuilder()
            .name(Text.of("Rotation"))
            .controller(integerOption -> CustomIntegerSliderControllerBuilder.create(integerOption).step(1).range(0, 360).formatValue(value -> Text.of(value + "°")))
            .stateManager(StateManager.createSimple(0, () -> CONFIG.instance().baseRotation, newVal -> CONFIG.instance().baseRotation = newVal))
            //i don't know why i have to do this now, one day it just broke and i had no idea why
            .available(CONFIG.instance().baseRainbow && CONFIG.instance().baseRenderMode != BaseRenderMode.NEVER && CONFIG.instance().modEnabled)
            .build();
    public static EvilOption<RenderMode> o_baseRenderLayer = EvilOption.<RenderMode>createBuilder()
            .name(Text.of("Render Mode"))
            .description(OptionDescription.createBuilder().text(Text.of("Culled: Doesn't render back sides of objects.\n\nWireframe: Draws outlines of objects. Does not support light levels. Line width also cannot be changed due to game limitations.\n\nGateway: Uses the game's end gateway shader. Does not support color, opacity, or light levels.")).build())
            .controller(renderModeOption -> EnumControllerBuilder.create(renderModeOption).enumClass(RenderMode.class))
            .stateManager(StateManager.createSimple(RenderMode.DEFAULT, () -> CONFIG.instance().baseRenderLayer, newVal -> CONFIG.instance().baseRenderLayer = newVal))
            .build();
    public static EvilOption<Boolean> o_baseCulling = EvilOption.<Boolean>createBuilder()
            .name(Text.of("Culled"))
            .controller(CustomTickBoxControllerBuilder::new)
            .stateManager(StateManager.createSimple(false, () -> CONFIG.instance().baseCulling, newVal -> CONFIG.instance().baseCulling = newVal))
            .build();
    public static Option<Float> o_baseOffset = Option.<Float>createBuilder()
            .name(Text.of("Vertical Offset"))
            .controller(floatOption -> CustomFloatSliderControllerBuilder.create(floatOption).range(-2F, 2F).step(0.1F).formatValue(CrystalChams.BLOCKS_FORMATTER))
            .stateManager(StateManager.createSimple(0F, () -> CONFIG.instance().baseOffset, newVal -> CONFIG.instance().baseOffset = newVal))
            .build();
    public static EvilOption<Color> o_baseColor = EvilOption.<Color>createBuilder()
            .name(Text.of("Color"))
            .controller(ColorControllerBuilderImpl::new)
            .stateManager(StateManager.createSimple(new Color(255, 255, 255), () -> CONFIG.instance().baseColor, newVal -> CONFIG.instance().baseColor = newVal))
            .build();
    public static EvilOption<Float> o_baseAlpha = CrystalChams.createFloatOptionPercent("Opacity", "", StateManager.createSimple(1f, () -> CONFIG.instance().baseAlpha, newVal -> CONFIG.instance().baseAlpha = newVal));
    public static EvilOption<Integer> o_baseLightLevel = EvilOption.<Integer>createBuilder()
            .name(Text.of("Light Level"))
            .description(OptionDescription.createBuilder().text(Text.of("How brightly lit the object is. -1 uses the world's lighting, while 0-255 is mapped respectively.")).build())
            .controller(integerOption -> CustomIntegerSliderControllerBuilder.create(integerOption).step(1).range(-1, 255).formatValue(CrystalChams.LIGHT_FORMATTER))
            .stateManager(StateManager.createSimple(-1, () -> CONFIG.instance().baseLightLevel, newVal -> CONFIG.instance().baseLightLevel = newVal))
            .build();
    @Updatable
    public static EvilOption<Boolean> o_baseRainbow = EvilOption.<Boolean>createBuilder()
            .name(Text.of("Rainbow Enabled"))
            .controller(CustomTickBoxControllerBuilder::new)
            .stateManager(StateManager.createSimple(false, () -> CONFIG.instance().baseRainbow, newVal -> CONFIG.instance().baseRainbow = newVal))
            .build();
    public static EvilOption<Float> o_baseRainbowSpeed = EvilOption.<Float>createBuilder()
            .name(Text.of("Speed"))
            .controller(floatOption -> CustomFloatSliderControllerBuilder.create(floatOption).range(0f, 10f).step(0.1f).formatValue(CrystalChams.MULTIPLIER_FORMATTER_ONE_PLACE))
//            .controller(integerOption -> CustomIntegerSliderController.create(integerOption).step(1).range(1, 10).formatValue(value -> Text.of(value + "x")))
            .stateManager(StateManager.createSimple(2F, () -> CONFIG.instance().baseRainbowSpeed, newVal -> CONFIG.instance().baseRainbowSpeed = newVal))
            //i don't know why i have to do this now, one day it just broke and i had no idea why
            .available(CONFIG.instance().baseRainbow && CONFIG.instance().baseRenderMode != BaseRenderMode.NEVER && CONFIG.instance().modEnabled)
            .build();
    public static EvilOption<Float> o_baseRainbowDelay = EvilOption.<Float>createBuilder()
            .name(Text.of("Delay"))
            .controller(floatOption -> CustomFloatSliderControllerBuilder.create(floatOption).range(-2.5f, 2.5f).step(0.1f).formatValue(CrystalChams.SECONDS_FORMATTER))
//            .controller(integerOption -> CustomIntegerSliderController.create(integerOption).step(1).range(-500, 500).formatValue(integer -> Text.of(integer + "ms")))
            .stateManager(StateManager.createSimple(0F, () -> CONFIG.instance().baseRainbowDelay, newVal -> CONFIG.instance().baseRainbowDelay = newVal))
            .available(CONFIG.instance().baseRainbow && CONFIG.instance().baseRenderMode != BaseRenderMode.NEVER && CONFIG.instance().modEnabled)
            .build();
    public static EvilOption<Float> o_baseRainbowSaturation = CrystalChams.createFloatOptionPercent("Saturation", "", StateManager.createSimple(1f, () -> CONFIG.instance().baseRainbowSaturation, newVal -> CONFIG.instance().baseRainbowSaturation = newVal));
    public static EvilOption<Float> o_baseRainbowBrightness = CrystalChams.createFloatOptionPercent("Brightness", "", StateManager.createSimple(1F, () -> CONFIG.instance().baseRainbowBrightness, newVal -> CONFIG.instance().baseRainbowBrightness = newVal));
    //keep this one below all other base options
    public static Option<BaseRenderMode> o_baseRenderMode = Option.<BaseRenderMode>createBuilder()
            .name(Text.of("Show"))
            .description(OptionDescription.createBuilder().text(Text.of("When to show the base.\n\nAlways: Always shows the base.\n\nDefault: Only show the base when the crystal has the base enabled.\n\nNever: Never shows the base.")).build())
            .controller(renderModeOption -> EnumControllerBuilder.create(renderModeOption).enumClass(BaseRenderMode.class))
            .listener(ChamsConfig::specialUpdate)
            .stateManager(StateManager.createSimple(BaseRenderMode.DEFAULT, () -> CONFIG.instance().baseRenderMode, newVal -> CONFIG.instance().baseRenderMode = newVal))
            .build();
    @Updatable
    public static EvilOption<Boolean> o_renderCore = EvilOption.<Boolean>createBuilder()
            .name(Text.of("Render Core"))
            .controller(CustomTickBoxControllerBuilder::new)
            .stateManager(StateManager.createSimple(true, () -> CONFIG.instance().renderCore, newVal -> CONFIG.instance().renderCore = newVal))
            .build();
    public static EvilOption<Float> o_coreOffset = EvilOption.<Float>createBuilder()
            .name(Text.of("Vertical Offset"))
            .controller(floatOption -> CustomFloatSliderControllerBuilder.create(floatOption).range(-2.5f, 2.5f).step(0.1f).formatValue(CrystalChams.BLOCKS_FORMATTER))
            .stateManager(StateManager.createSimple(0F, () -> CONFIG.instance().coreOffset, newVal -> CONFIG.instance().coreOffset = newVal))
            .build();
    public static EvilOption<Float> o_coreRotationSpeed = EvilOption.<Float>createBuilder()
            .name(Text.of("Rotation Speed"))
            .controller(floatOption -> CustomFloatSliderControllerBuilder.create(floatOption).range(-15f, 15f).step(0.1f).formatValue(val -> Text.of(String.format("%.1f", val) + "x")))
            .stateManager(StateManager.createSimple(1F, () -> CONFIG.instance().coreRotationSpeed, newVal -> CONFIG.instance().coreRotationSpeed = newVal))
            .build();
    public static EvilOption<Float> o_coreBounceHeight = EvilOption.<Float>createBuilder()
            .name(Text.of("Bounce Height"))
            .controller(floatOption -> CustomFloatSliderControllerBuilder.create(floatOption).range(0f, 2.5f).step(0.05f).formatValue(val -> Text.of(String.format("%.2f", val) + "x")))
            .stateManager(StateManager.createSimple(1F, () -> CONFIG.instance().coreBounceHeight, newVal -> CONFIG.instance().coreBounceHeight = newVal))
            .build();
    public static EvilOption<Float> o_coreBounceSpeed = EvilOption.<Float>createBuilder()
            .name(Text.of("Bounce Speed"))
            .controller(floatOption -> CustomFloatSliderControllerBuilder.create(floatOption).range(0f, 2.5f).step(0.05f).formatValue(val -> Text.of(String.format("%.2f", val) + "x")))
            .stateManager(StateManager.createSimple(1F, () -> CONFIG.instance().coreBounceSpeed, newVal -> CONFIG.instance().coreBounceSpeed = newVal))
            .build();
    public static EvilOption<Float> o_coreDelay = EvilOption.<Float>createBuilder()
            .name(Text.of("Delay"))
            .controller(floatOption -> CustomFloatSliderControllerBuilder.create(floatOption).range(-2.5f, 2.5f).step(0.1f).formatValue(CrystalChams.SECONDS_FORMATTER))
            .stateManager(StateManager.createSimple(0F, () -> CONFIG.instance().coreDelay, newVal -> CONFIG.instance().coreDelay = newVal))
            .build();
    public static EvilOption<Float> o_coreScale = EvilOption.<Float>createBuilder()
            .name(Text.of("Scale"))
            .controller(floatOption -> CustomFloatSliderControllerBuilder.create(floatOption).range(0f, 2.5f).step(0.05f).formatValue(val -> Text.of(String.format("%.2f", val) + "x")))
            .stateManager(StateManager.createSimple(1F, () -> CONFIG.instance().coreScale, newVal -> CONFIG.instance().coreScale = newVal))
            .group(OptionGroups.SCALE)
            .build();
    public static EvilOption<Color> o_coreColor = EvilOption.<Color>createBuilder()
            .name(Text.of("Color"))
            .controller(ColorControllerBuilderImpl::new)
            .stateManager(StateManager.createSimple(new Color(255, 255, 255), () -> CONFIG.instance().coreColor, newVal -> CONFIG.instance().coreColor = newVal))
            .build();
    public static EvilOption<Float> o_coreAlpha = CrystalChams.createFloatOptionPercent("Opacity", "", StateManager.createSimple(1f, () -> CONFIG.instance().coreAlpha, newVal -> CONFIG.instance().coreAlpha = newVal));
    public static EvilOption<Integer> o_coreLightLevel = EvilOption.<Integer>createBuilder()
            .name(Text.of("Light Level"))
            .description(OptionDescription.createBuilder().text(Text.of("How brightly lit the object is. -1 uses the world's lighting, while 0-255 is mapped respectively.")).build())
            .controller(integerOption -> CustomIntegerSliderControllerBuilder.create(integerOption).step(1).range(-1, 255).formatValue(value -> Text.of(value == -1 ? "Use World Light" : value + "")))
            .stateManager(StateManager.createSimple(-1, () -> CONFIG.instance().coreLightLevel, newVal -> CONFIG.instance().coreLightLevel = newVal))
            .build();
    public static EvilOption<RenderMode> o_coreRenderLayer = EvilOption.<RenderMode>createBuilder()
            .name(Text.of("Render Mode"))
            .description(OptionDescription.createBuilder().text(Text.of("Culled: Doesn't render back sides of objects.\n\nWireframe: Draws outlines of objects. Does not support light levels. Line width also cannot be changed due to game limitations.\n\nGateway: Uses the game's end gateway shader. Does not support color, opacity, or light levels.")).build())
            .controller(renderModeOption -> EnumControllerBuilder.create(renderModeOption).enumClass(RenderMode.class))
            .stateManager(StateManager.createSimple(RenderMode.DEFAULT, () -> CONFIG.instance().coreRenderLayer, newVal -> CONFIG.instance().coreRenderLayer = newVal))
            .build();
    public static EvilOption<Boolean> o_coreCulling = EvilOption.<Boolean>createBuilder()
            .name(Text.of("Culled"))
            .controller(CustomTickBoxControllerBuilder::new)
            .stateManager(StateManager.createSimple(false, () -> CONFIG.instance().coreCulling, newVal -> CONFIG.instance().coreCulling = newVal))
            .build();
    @Updatable
    public static EvilOption<Boolean> o_coreRainbow = EvilOption.<Boolean>createBuilder()
            .name(Text.of("Rainbow Enabled"))
            .controller(CustomTickBoxControllerBuilder::new)
            .stateManager(StateManager.createSimple(false, () -> CONFIG.instance().coreRainbow, newVal -> CONFIG.instance().coreRainbow = newVal))
            .build();
    public static EvilOption<Float> o_coreRainbowSpeed = EvilOption.<Float>createBuilder()
            .name(Text.of("├Speed"))
            .controller(floatOption -> CustomFloatSliderControllerBuilder.create(floatOption).range(0f, 10f).step(0.1f).formatValue(CrystalChams.MULTIPLIER_FORMATTER_ONE_PLACE))
//            .controller(integerOption -> CustomIntegerSliderController.create(integerOption).step(1).range(1, 10).formatValue(value -> Text.of(value + "x")))
            .stateManager(StateManager.createSimple(2F, () -> CONFIG.instance().coreRainbowSpeed, newVal -> CONFIG.instance().coreRainbowSpeed = newVal))
            .build();
    public static EvilOption<Float> o_coreRainbowDelay = EvilOption.<Float>createBuilder()
            .name(Text.of("├Delay"))
            .controller(floatOption -> CustomFloatSliderControllerBuilder.create(floatOption).range(-2.5f, 2.5f).step(0.1f).formatValue(val -> Text.of(String.format("%.1f", val).replace(".0", "") + (Math.abs(val) == 1 ? " second" : " seconds"))))
            .stateManager(StateManager.createSimple(0F, () -> CONFIG.instance().coreRainbowDelay, newVal -> CONFIG.instance().coreRainbowDelay = newVal))
            .build();
    public static EvilOption<Float> o_coreRainbowSaturation = CrystalChams.createFloatOptionPercent("├Saturation", "", StateManager.createSimple(1F, () -> CONFIG.instance().coreRainbowSaturation, newVal -> CONFIG.instance().coreRainbowSaturation = newVal));
    public static EvilOption<Float> o_coreRainbowBrightness = CrystalChams.createFloatOptionPercent("└ ▙ ┕ ┖ ┗ Brightness", "", StateManager.createSimple(1F, () -> CONFIG.instance().coreRainbowBrightness, newVal -> CONFIG.instance().coreRainbowBrightness = newVal));
    @Updatable
    public static Option<Boolean> o_coreAlphaAnimation = Option.<Boolean>createBuilder()
            .name(Text.of("Animation Enabled"))
            .controller(CustomTickBoxControllerBuilder::new)
            .stateManager(StateManager.createSimple(false, () -> CONFIG.instance().coreAlphaAnimation, newVal -> CONFIG.instance().coreAlphaAnimation = newVal))
            .build();
    public static Option<Float> o_coreStartOpacity = CrystalChams.createFloatOptionPercent("Starting Opacity", "", StateManager.createSimple(1F, () -> CONFIG.instance().coreStartAlpha, newVal -> CONFIG.instance().coreStartAlpha = newVal));
    public static Option<Float> o_coreAlphaDelay = Option.<Float>createBuilder()
            .name(Text.of("Delay"))
            .controller(floatOption -> CustomFloatSliderControllerBuilder.create(floatOption).range(0f, 2f).step(0.1f).formatValue(val -> Text.of(String.format("%.1f", val) + "s")))
            .stateManager(StateManager.createSimple(0F, () -> CONFIG.instance().coreAlphaDelay, newVal -> CONFIG.instance().coreAlphaDelay = newVal))
            .build();
    public static Option<Float> o_coreAlphaAnimDuration = Option.<Float>createBuilder()
            .name(Text.of("Duration"))
            .controller(floatOption -> CustomFloatSliderControllerBuilder.create(floatOption).range(0f, 2f).step(0.1f).formatValue(val -> Text.of(String.format("%.1f", val) + "s")))
            .stateManager(StateManager.createSimple(1F, () -> CONFIG.instance().coreAlphaAnimDuration, newVal -> CONFIG.instance().coreAlphaAnimDuration = newVal))
            .build();
    public static Option<Easings> o_coreAlphaEasing = Option.<Easings>createBuilder()
            .name(Text.of("Easing"))
            .controller(easingsOption -> EnumControllerBuilder.create(easingsOption).enumClass(Easings.class))
            .stateManager(StateManager.createSimple(Easings.OFF, () -> CONFIG.instance().coreAlphaEasing, newVal -> CONFIG.instance().coreAlphaEasing = newVal))
            .build();
    @Updatable
    public static Option<Boolean> o_coreScaleAnimation = Option.<Boolean>createBuilder()
            .name(Text.of("Animation Enabled"))
            .controller(CustomTickBoxControllerBuilder::new)
            .stateManager(StateManager.createSimple(false, () -> CONFIG.instance().coreScaleAnimation, newVal -> CONFIG.instance().coreScaleAnimation = newVal))
            .build();
    public static Option<Float> o_coreStartScale = Option.<Float>createBuilder()
            .name(Text.of("Starting Scale"))
            .controller(floatOption -> CustomFloatSliderControllerBuilder.create(floatOption).range(0f, 2f).step(0.05f).formatValue(val -> Text.of(String.format("%.2f", val) + "x")))
            .stateManager(StateManager.createSimple(0F, () -> CONFIG.instance().coreStartScale, newVal -> CONFIG.instance().coreStartScale = newVal))
            .build();
    public static Option<Float> o_coreScaleDelay = Option.<Float>createBuilder()
            .name(Text.of("Delay"))
            .controller(floatOption -> CustomFloatSliderControllerBuilder.create(floatOption).range(0f, 2f).step(0.1f).formatValue(val -> Text.of(String.format("%.1f", val) + "s")))
            .stateManager(StateManager.createSimple(0F, () -> CONFIG.instance().coreScaleDelay, newVal -> CONFIG.instance().coreScaleDelay = newVal))
            .build();
    public static Option<Float> o_coreScaleAnimDuration = Option.<Float>createBuilder()
            .name(Text.of("Duration"))
            .controller(floatOption -> CustomFloatSliderControllerBuilder.create(floatOption).range(0f, 2f).step(0.1f).formatValue(val -> Text.of(String.format("%.1f", val) + "s")))
            .stateManager(StateManager.createSimple(1F, () -> CONFIG.instance().coreScaleAnimDuration, newVal -> CONFIG.instance().coreScaleAnimDuration = newVal))
            .build();
    public static Option<Easings> o_coreScaleEasing = Option.<Easings>createBuilder()
            .name(Text.of("Easing"))
            .controller(easingsOption -> EnumControllerBuilder.create(easingsOption).enumClass(Easings.class))
            .stateManager(StateManager.createSimple(Easings.OFF, () -> CONFIG.instance().coreScaleEasing, newVal -> CONFIG.instance().coreScaleEasing = newVal))
            .build();
    //TODO: look into drop-down widget
    @Updatable
    public static EvilOption<Boolean> o_renderFrames = EvilOption.<Boolean>createBuilder()
            .name(Text.of("Render Frames"))
            .controller(CustomTickBoxControllerBuilder::new)
            .stateManager(StateManager.createSimple(true, () -> CONFIG.instance().renderFrames, newVal -> CONFIG.instance().renderFrames = newVal))
            .build();
    @Updatable
    public static EvilOption<Boolean> o_renderBeam = EvilOption.<Boolean>createBuilder()
            .name(Text.of("Render Beam"))
            .controller(CustomTickBoxControllerBuilder::new)
            .stateManager(StateManager.createSimple(true, () -> CONFIG.instance().renderBeam, newVal -> CONFIG.instance().renderBeam = newVal))
            .build();
    public static EvilOption<Color> o_beam1Color = EvilOption.<Color>createBuilder()
            .name(Text.of("Color"))
            .controller(ColorControllerBuilderImpl::new)
            .stateManager(StateManager.createSimple(new Color(255, 255, 255), () -> CONFIG.instance().beam1Color, newVal -> CONFIG.instance().beam1Color = newVal))
            .build();
    public static EvilOption<Float> o_beam1Alpha = CrystalChams.createFloatOptionPercent("Opacity", "", StateManager.createSimple(1F, () -> CONFIG.instance().beam1Alpha, newVal -> CONFIG.instance().beam1Alpha = newVal));
    public static EvilOption<Integer> o_beam1LightLevel = EvilOption.<Integer>createBuilder()
            .name(Text.of("Light Level"))
            .description(OptionDescription.createBuilder().text(Text.of("How brightly lit the object is. -1 uses the world's lighting, while 0-255 is mapped respectively.")).build())
            .controller(integerOption -> CustomIntegerSliderControllerBuilder.create(integerOption).step(1).range(-1, 255).formatValue(value -> Text.of(value == -1 ? "Use World Light" : value + "")))
            .stateManager(StateManager.createSimple(-1, () -> CONFIG.instance().beam1LightLevel, newVal -> CONFIG.instance().beam1LightLevel = newVal))
            .build();
    @Updatable
    public static EvilOption<Boolean> o_beam1Rainbow = EvilOption.<Boolean>createBuilder()
            .name(Text.of("Rainbow"))
            .controller(CustomTickBoxControllerBuilder::new)
            .stateManager(StateManager.createSimple(false, () -> CONFIG.instance().beam1Rainbow, newVal -> CONFIG.instance().beam1Rainbow = newVal))
            .build();
    public static EvilOption<Float> o_beam1RainbowSpeed = EvilOption.<Float>createBuilder()
            .name(Text.of("Rainbow Speed"))
            .controller(floatOption -> CustomFloatSliderControllerBuilder.create(floatOption).range(0f, 10f).step(0.1f).formatValue(CrystalChams.MULTIPLIER_FORMATTER_ONE_PLACE))
            .stateManager(StateManager.createSimple(2F, () -> CONFIG.instance().beam1RainbowSpeed, newVal -> CONFIG.instance().beam1RainbowSpeed = newVal))
            .available(CONFIG.instance().beam1Rainbow && CONFIG.instance().renderBeam && CONFIG.instance().modEnabled)
            .build();
    public static EvilOption<Float> o_beam1RainbowDelay = EvilOption.<Float>createBuilder()
            .name(Text.of("Rainbow Delay"))
//            .controller(integerOption -> CustomIntegerSliderController.create(integerOption).step(1).range(-500, 500).formatValue(integer -> Text.of(integer + "ms")))
            .controller(floatOption -> CustomFloatSliderControllerBuilder.create(floatOption).range(-2.5f, 2.5f).step(0.1f).formatValue(val -> Text.of(String.format("%.1f", val).replace(".0", "") + (Math.abs(val) == 1 ? " second" : " seconds"))))
            .stateManager(StateManager.createSimple(0F, () -> CONFIG.instance().beam1RainbowDelay, newVal -> CONFIG.instance().beam1RainbowDelay = newVal))
            .available(CONFIG.instance().beam1Rainbow && CONFIG.instance().renderBeam && CONFIG.instance().modEnabled)
            .build();
    public static EvilOption<Float> o_beam1RainbowSaturation = CrystalChams.createFloatOptionPercent("Saturation", "", StateManager.createSimple(1F, () -> CONFIG.instance().beam1RainbowSaturation, newVal -> CONFIG.instance().beam1RainbowSaturation = newVal));
    public static EvilOption<Float> o_beam1RainbowBrightness = CrystalChams.createFloatOptionPercent("Brightness", "", StateManager.createSimple(1F, () -> CONFIG.instance().beam1RainbowBrightness, newVal -> CONFIG.instance().beam1RainbowBrightness = newVal));
    public static EvilOption<Color> o_beam2Color = EvilOption.<Color>createBuilder()
            .name(Text.of("Color"))
            .controller(ColorControllerBuilderImpl::new)
            .stateManager(StateManager.createSimple(new Color(0, 0, 0), () -> CONFIG.instance().beam2Color, newVal -> CONFIG.instance().beam2Color = newVal))
            .build();
    public static EvilOption<Float> o_beam2Alpha = CrystalChams.createFloatOptionPercent("Opacity", "", StateManager.createSimple(1F, () -> CONFIG.instance().beam2Alpha, newVal -> CONFIG.instance().beam2Alpha = newVal));
    public static EvilOption<Integer> o_beam2LightLevel = EvilOption.<Integer>createBuilder()
            .name(Text.of("Light Level"))
            .description(OptionDescription.createBuilder().text(Text.of("How brightly lit the object is. -1 uses the world's lighting, while 0-255 is mapped respectively.")).build())
            .controller(integerOption -> CustomIntegerSliderControllerBuilder.create(integerOption).step(1).range(-1, 255).formatValue(value -> Text.of(value == -1 ? "Use World Light" : value + "")))
            .stateManager(StateManager.createSimple(-1, () -> CONFIG.instance().beam2LightLevel, newVal -> CONFIG.instance().beam2LightLevel = newVal))
            .build();
    @Updatable
    public static EvilOption<Boolean> o_beam2Rainbow = EvilOption.<Boolean>createBuilder()
            .name(Text.of("Rainbow"))
            .controller(CustomTickBoxControllerBuilder::new)
            .stateManager(StateManager.createSimple(false, () -> CONFIG.instance().beam2Rainbow, newVal -> CONFIG.instance().beam2Rainbow = newVal))
            .build();
    public static EvilOption<Float> o_beam2RainbowSpeed = EvilOption.<Float>createBuilder()
            .name(Text.of("Rainbow Speed"))
            .controller(floatOption -> CustomFloatSliderControllerBuilder.create(floatOption).range(0f, 10f).step(0.1f).formatValue(CrystalChams.MULTIPLIER_FORMATTER_ONE_PLACE))
            .stateManager(StateManager.createSimple(2F, () -> CONFIG.instance().beam2RainbowSpeed, newVal -> CONFIG.instance().beam2RainbowSpeed = newVal))
            .available(CONFIG.instance().beam2Rainbow && CONFIG.instance().renderBeam && CONFIG.instance().modEnabled)
            .build();
    public static EvilOption<Float> o_beam2RainbowDelay = EvilOption.<Float>createBuilder()
            .name(Text.of("Rainbow Delay"))
            .controller(floatOption -> CustomFloatSliderControllerBuilder.create(floatOption).range(-2.5f, 2.5f).step(0.1f).formatValue(val -> Text.of(String.format("%.1f", val).replace(".0", "") + (Math.abs(val) == 1 ? " second" : " seconds"))))
            .stateManager(StateManager.createSimple(0F, () -> CONFIG.instance().beam2RainbowDelay, newVal -> CONFIG.instance().beam2RainbowDelay = newVal))
            .available(CONFIG.instance().beam2Rainbow && CONFIG.instance().renderBeam && CONFIG.instance().modEnabled)
            .build();
    public static EvilOption<Float> o_beam2RainbowSaturation = CrystalChams.createFloatOptionPercent("Saturation", "", StateManager.createSimple(1F, () -> CONFIG.instance().beam2RainbowSaturation, newVal -> CONFIG.instance().beam2RainbowSaturation = newVal));
    public static EvilOption<Float> o_beam2RainbowBrightness = CrystalChams.createFloatOptionPercent("Brightness", "", StateManager.createSimple(1F, () -> CONFIG.instance().beam2RainbowBrightness, newVal -> CONFIG.instance().beam2RainbowBrightness = newVal));
    public static EvilOption<Float> o_beam1Radius = EvilOption.<Float>createBuilder()
            .name(Text.of("Radius"))
            .controller(floatOption -> CustomFloatSliderControllerBuilder.create(floatOption).range(-1f, 1f).step(0.05f).formatValue(CrystalChams.BLOCKS_FORMATTER_TWO_PLACES))
            .stateManager(StateManager.createSimple(0.75F, () -> CONFIG.instance().beam1Radius, newVal -> CONFIG.instance().beam1Radius = newVal))
            .build();
    public static EvilOption<Float> o_beam2Radius = EvilOption.<Float>createBuilder()
            .name(Text.of("Radius"))
            .controller(floatOption -> CustomFloatSliderControllerBuilder.create(floatOption).range(-1f, 1f).step(0.05f).formatValue(CrystalChams.BLOCKS_FORMATTER_TWO_PLACES))
            .stateManager(StateManager.createSimple(0.15F, () -> CONFIG.instance().beam2Radius, newVal -> CONFIG.instance().beam2Radius = newVal))
            .build();
    public static Option<Integer> o_beamSides = Option.<Integer>createBuilder()
            .name(Text.of("Sides"))
            .controller(integerOption -> CustomIntegerSliderControllerBuilder.create(integerOption).step(1).range(2, 64))
            .stateManager(StateManager.createSimple(8, () -> CONFIG.instance().beamSides, newVal -> CONFIG.instance().beamSides = newVal))
            .build();
    public static Option<Float> o_beamScrollSpeed = Option.<Float>createBuilder()
            .name(Text.of("Scroll Speed"))
            .controller(floatOption -> CustomFloatSliderControllerBuilder.create(floatOption).range(-2f, 2f).step(0.05f).formatValue(CrystalChams.MULTIPLIER_FORMATTER))
            .stateManager(StateManager.createSimple(1F, () -> CONFIG.instance().beamScrollSpeed, newVal -> CONFIG.instance().beamScrollSpeed = newVal))
            .build();
    public static EvilOption<RenderMode> o_beamRenderLayer = EvilOption.<RenderMode>createBuilder()
            .name(Text.of("Render Mode"))
            .description(OptionDescription.createBuilder().text(Text.of("Culled: Doesn't render back sides of objects.\n\nWireframe: Draws outlines of objects. Does not support light levels. Line width also cannot be changed due to game limitations.\n\nGateway: Uses the game's end gateway shader. Does not support color, opacity, or light levels.")).build())
            .controller(renderModeOption -> EnumControllerBuilder.create(renderModeOption).enumClass(RenderMode.class))
            .stateManager(StateManager.createSimple(RenderMode.DEFAULT, () -> CONFIG.instance().beamRenderMode, newVal -> CONFIG.instance().beamRenderMode = newVal))
            .build();
    public static EvilOption<Boolean> o_beamCulling = EvilOption.<Boolean>createBuilder()
            .name(Text.of("Culled"))
            .controller(CustomTickBoxControllerBuilder::new)
            .stateManager(StateManager.createSimple(false, () -> CONFIG.instance().beamCulling, newVal -> CONFIG.instance().beamCulling = newVal))
            .build();
    public static ListOption<ModelPartOptions> o_frameList = ListOption.<ModelPartOptions>createBuilder()
            .name(Text.of("Frame List"))
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
            o_coreLightLevel.setAvailable(available);
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
        }
        else if (booleanOption.equals(o_renderFrames)){
            o_frameList.setAvailable(o_renderFrames.available() && aBoolean);
        }
        else if (booleanOption.equals(o_renderBeam)) {
            boolean available = o_renderBeam.available() && aBoolean;
            o_beam1Color.setAvailable(available);
            o_beam1Alpha.setAvailable(available);
            o_beam1Rainbow.setAvailable(available);
            o_beam1Radius.setAvailable(available);
            o_beam1LightLevel.setAvailable(available);
            o_beam2Color.setAvailable(available);
            o_beam2Alpha.setAvailable(available);
            o_beam2Rainbow.setAvailable(available);
            o_beam2Radius.setAvailable(available);
            o_beam2LightLevel.setAvailable(available);
            o_beamSides.setAvailable(available);
            o_beamScrollSpeed.setAvailable(available);
            o_beamRenderLayer.setAvailable(available);
        } else if (booleanOption.equals(o_beam1Rainbow)) {
            boolean available = o_beam1Rainbow.available() && aBoolean;
            o_beam1RainbowDelay.setAvailable(available);
            o_beam1RainbowSpeed.setAvailable(available);
            o_beam1RainbowBrightness.setAvailable(available);
            o_beam1RainbowSaturation.setAvailable(available);
            o_beam1Color.setAvailable(available);
        } else if (booleanOption.equals(o_beam2Rainbow)) {
            boolean available = o_beam2Rainbow.available() && aBoolean;
            o_beam2RainbowDelay.setAvailable(available);
            o_beam2RainbowSpeed.setAvailable(available);
            o_beam2RainbowBrightness.setAvailable(available);
            o_beam2RainbowSaturation.setAvailable(available);
            o_beam2Color.setAvailable(available);
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

    private static void specialUpdate(Option<BaseRenderMode> baseRenderModeOption, BaseRenderMode baseRenderMode) {
        boolean available = baseRenderModeOption.available() && baseRenderMode != BaseRenderMode.NEVER;
        o_baseOffset.setAvailable(available);
        o_baseScale.setAvailable(available);
        o_baseColor.setAvailable(available);
        o_baseAlpha.setAvailable(available);
        o_baseLightLevel.setAvailable(available);
        o_baseRenderLayer.setAvailable(available);
        o_baseRainbow.setAvailable(available);
        o_baseRotation.setAvailable(available);
        o_baseCulling.setAvailable(available);
    }

    public enum BaseRenderMode implements NameableEnum {
        ALWAYS,
        DEFAULT,
        NEVER;


        @Override
        public Text getDisplayName() {
            return switch (this) {
                case ALWAYS -> Text.of("Always");
                case DEFAULT -> Text.of("Default");
                case NEVER -> Text.of("Never");
            };
        }
    }

    public static Screen getConfigScreen(Screen parent) {
        return YetAnotherConfigLib.create(CONFIG, ((defaults, config, builder) -> builder
                        .title(Text.of("Custom End Crystals"))
                        .category(ConfigCategory.createBuilder()
                                .name(Text.of("General"))
                                .option(o_modEnabled)
                                .group(OptionGroup.createBuilder()
                                        .name(Text.of("Shadow"))
                                        .option(o_shadowRadius)
                                        .option(o_shadowAlpha)
                                        .build())
                                .group(OptionGroup.createBuilder()
                                        .name(Text.of("Config"))
                                        .option(ButtonOption.createBuilder()
                                                .name(Text.of("Copy Current Config"))
                                                .description(OptionDescription.createBuilder().text(Text.of("Copies the current configuration as text to your clipboard. Go share your configs with your buddies! (Make sure to save the config first)")).build())
                                                .action((yaclScreen, buttonOption) -> CrystalChams.mc.keyboard.setClipboard(gson.toJson(CONFIG.instance())))
                                                .text(Text.of("Copy"))
                                                .build())
                                        .option(ButtonOption.createBuilder()
                                                .name(Text.literal("Load Config From Clipboard").formatted(Formatting.UNDERLINE, Formatting.ITALIC, Formatting.RED))
                                                .description(OptionDescription.createBuilder().text(Text.of("Loads a configuration from your clipboard if it's valid. WARNING: LOADING A VALID CONFIGURATION WILL OVERWRITE YOUR CURRENT ONE.")).build())
                                                .text(Text.of("Load"))
                                                .action((yaclScreen, buttonOption) -> {
                                                    //this sucks but it works!!
                                                    try {
                                                        gson.fromJson(CrystalChams.mc.keyboard.getClipboard(), ChamsConfig.class);
                                                    } catch (JsonSyntaxException e) {
                                                        System.out.println("invalid config!!!");
                                                        return;
                                                    }
                                                    try {
                                                        Path path = FabricLoader.getInstance().getConfigDir().resolve("crystalchams.json");
                                                        Files.delete(path);
                                                        Files.createFile(path);
                                                        Files.writeString(path, CrystalChams.mc.keyboard.getClipboard(), StandardCharsets.UTF_8);
                                                        ChamsConfig.CONFIG.load();
                                                        CrystalChams.unleashHell();
                                                    } catch (IOException e) {
                                                        throw new RuntimeException(e);
                                                    }
                                                })
                                                .build())
                                        .option(ButtonOption.createBuilder()
                                                .name(Text.of("Randomize All Values"))
                                                .description(OptionDescription.of(Text.of("Can't come up with a good configuration? Randomize every option and see what happens!")))
                                                .action((yaclScreen, buttonOption) -> CrystalChams.randomizeOptions())
                                                .text(Text.of("Randomize"))
                                                .build())
                                        .build())
                                .group(OptionGroup.createBuilder()
                                        .name(Text.of("Miscellaneous"))
                                        .option(o_randomizeAge)
                                        .option(o_renderHitbox)
                                        .option(o_showAnimations)
                                        .option(o_previewScale)
                                        .build()
                                )
                                .build())
                        .category(ConfigCategory.createBuilder()
                                .name(Text.of("Core"))
                                .option(o_renderCore)
                                .group(OptionGroup.createBuilder()
                                        .name(Text.of("Transform"))
                                        .option(o_coreOffset)
                                        .option(o_coreScale)
                                        .build())
                                .group(OptionGroup.createBuilder()
                                        .name(Text.of("Movement"))
                                        .option(o_coreRotationSpeed)
                                        .option(o_coreBounceHeight)
                                        .option(o_coreBounceSpeed)
                                        .option(o_coreDelay)
                                        .build())
                                .group(OptionGroup.createBuilder()
                                        .name(Text.of("Color"))
                                        .option(o_coreColor)
                                        .option(o_coreAlpha)
                                        .option(o_coreRainbow)
                                        .option(o_coreRainbowSpeed)
                                        .option(o_coreRainbowDelay)
                                        .option(o_coreRainbowSaturation)
                                        .option(o_coreRainbowBrightness)
                                        .build())
                                .group(OptionGroup.createBuilder()
                                        .name(Text.of("Rendering"))
                                        .option(o_coreLightLevel)
                                        .option(o_coreRenderLayer)
                                        .option(o_coreCulling)
                                        .build())
                                .group(OptionGroup.createBuilder()
                                        .name(Text.of("Animation"))
                                        .option(o_coreAlphaAnimation)
                                        .option(o_coreStartOpacity)
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
                                .name(Text.of("Frames"))
                                .option(o_renderFrames)
                                .option(o_frameList)
                                .build())
                        .category(ConfigCategory.createBuilder()
                                .name(Text.of("Base"))
                                .option(o_baseRenderMode)
                                .group(OptionGroup.createBuilder()
                                        .name(Text.of("Come up with a name for this one later"))
                                        .option(o_baseOffset)
                                        .option(o_baseRotation)
                                        .build()
                                )
                                .group(OptionGroup.createBuilder()
                                        .name(Text.of("Rendering"))
                                        .option(o_baseScale)
                                        .option(o_baseColor)
                                        .option(o_baseAlpha)
                                        .option(o_baseLightLevel)
                                        .option(o_baseRenderLayer)
                                        .build())
                                .group(OptionGroup.createBuilder()
                                        .name(Text.of("Rainbow"))
                                        .option(o_baseRainbow)
                                        .option(o_baseRainbowSpeed)
                                        .option(o_baseRainbowDelay)
                                        .option(o_baseRainbowSaturation)
                                        .option(o_baseRainbowBrightness)
                                        .build())
                                .build())
                        .category(ConfigCategory.createBuilder()
                                .name(Text.of("Beam"))
                                .option(o_renderBeam)
                                .group(OptionGroup.createBuilder()
                                        .name(Text.of("Start"))
                                        .option(o_beam1Color)
                                        .option(o_beam1Alpha)
                                        .option(o_beam1Radius)
                                        .option(o_beam1LightLevel)
                                        .option(o_beam1Rainbow)
                                        .option(o_beam1RainbowSpeed)
                                        .option(o_beam1RainbowDelay)
                                        .option(o_beam1RainbowSaturation)
                                        .option(o_beam1RainbowBrightness)
                                        .build())
                                .group(OptionGroup.createBuilder()
                                        .name(Text.of("End"))
                                        .option(o_beam2Color)
                                        .option(o_beam2Alpha)
                                        .option(o_beam2Radius)
                                        .option(o_beam2LightLevel)
                                        .option(o_beam2Rainbow)
                                        .option(o_beam2RainbowSpeed)
                                        .option(o_beam2RainbowDelay)
                                        .option(o_beam2RainbowSaturation)
                                        .option(o_beam2RainbowBrightness)
                                        .build())
                                .group(OptionGroup.createBuilder()
                                        .name(Text.of("Extras"))
                                        .option(o_beamSides)
                                        .option(o_beamScrollSpeed)
                                        .option(o_beamRenderLayer)
                                        .build())
                                .build())
                ))
                .generateScreen(parent);
    }
}