package tektonikal.crystalchams.config;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.EnumControllerBuilder;
import dev.isxander.yacl3.api.controller.FloatSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import dev.isxander.yacl3.config.v2.impl.serializer.GsonConfigSerializer;
import dev.isxander.yacl3.impl.controller.ColorControllerBuilderImpl;
import dev.isxander.yacl3.impl.controller.TickBoxControllerBuilderImpl;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import tektonikal.crystalchams.CrystalChams;
import tektonikal.crystalchams.annotation.Updatable;
import tektonikal.crystalchams.util.Easings;

import java.awt.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

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
    @SerialEntry  public boolean modEnabled = true;
    @SerialEntry  public boolean showAnimations = true;
        @SerialEntry public boolean randomizeAge = true;
        //Shadow
        @SerialEntry public float shadowRadius = 0.5F;
        @SerialEntry public float shadowAlpha = 0.5F;
        //Base
        @SerialEntry public BaseRenderMode showBaseMode = BaseRenderMode.DEFAULT;
        @SerialEntry public float baseOffset = -1F;
        @SerialEntry public float baseScale = 1F;
        @SerialEntry public int baseRotation = 0;
        @SerialEntry public Color baseColor = Color.decode("#FFFFFF");
        @SerialEntry public float baseAlpha = 1.0F;
        @SerialEntry public int baseLightLevel = -1;
        @SerialEntry public RenderMode baseRenderMode = RenderMode.DEFAULT;
            @SerialEntry public boolean baseCulling = false;

            //Base Rainbow
            @SerialEntry public boolean baseRainbow = false;
            @SerialEntry public int baseRainbowSpeed = 2;
            @SerialEntry public int baseRainbowDelay = 0;
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
            @SerialEntry public float coreTickDelay = 0;
            //Core Rendering
            @SerialEntry public float coreScale = 1F;
            @SerialEntry public Color coreColor = Color.decode("#ffffff");
            @SerialEntry public float coreAlpha = 1;
            @SerialEntry public int coreLightLevel = -1;
            @SerialEntry public RenderMode coreRenderLayer = RenderMode.DEFAULT;
                @SerialEntry public boolean coreCulling = false;

            //Core Rainbow
            @SerialEntry  public boolean coreRainbow = false;
                @SerialEntry public int coreRainbowSpeed = 2;
                @SerialEntry public int coreRainbowDelay = 0;
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
        @SerialEntry  public boolean renderFrame1 = true;
            //Frame 1 Movement
            @SerialEntry public float frame1Offset = 0f;
            @SerialEntry public float frame1RotationSpeed = 1F;
            @SerialEntry public float frame1BounceHeight = 1f;
            @SerialEntry public float frame1BounceSpeed = 1f;
            @SerialEntry public float frame1TickDelay = 0;
            //Frame 1 Rendering
            @SerialEntry public float frame1Scale = 1F;
            @SerialEntry public Color frame1Color = Color.decode("#ffffff");
            @SerialEntry public float frame1Alpha = 1f;
            @SerialEntry public int frame1LightLevel = -1;
            @SerialEntry public RenderMode frame1RenderLayer = RenderMode.DEFAULT;
                @SerialEntry public boolean frame1Culling = false;
            //Frame 1 Rainbow
            @SerialEntry  public boolean frame1Rainbow = false;
                @SerialEntry public int frame1RainbowSpeed = 2;
                @SerialEntry public int frame1RainbowDelay = 0;
                @SerialEntry public float frame1RainbowSaturation = 1;
                @SerialEntry public float frame1RainbowBrightness = 1;
       //Frame 2
        @SerialEntry  public boolean renderFrame2 = true;
            //Frame 2 Movement
            @SerialEntry public float frame2Offset = 0f;
            @SerialEntry public float frame2RotationSpeed = 1F;
            @SerialEntry public float frame2BounceHeight = 1f;
            @SerialEntry public float frame2BounceSpeed = 1f;
            @SerialEntry public float frame2TickDelay = 0;
            //Frame 2 Rendering
            @SerialEntry public float frame2Scale = 1F;
            @SerialEntry public Color frame2Color = Color.decode("#ffffff");
            @SerialEntry public float frame2Alpha = 1;
            @SerialEntry public int frame2LightLevel = -1;
            @SerialEntry public RenderMode frame2RenderLayer = RenderMode.DEFAULT;
                @SerialEntry public boolean frame2Culling = false;

            //Frame 2 Rainbow
            @SerialEntry  public boolean frame2Rainbow = false;
                @SerialEntry public int frame2RainbowSpeed = 2;
                @SerialEntry public int frame2RainbowDelay = 0;
                @SerialEntry public float frame2RainbowSaturation = 1;
                @SerialEntry public float frame2RainbowBrightness = 1;
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
                @SerialEntry public int beam1RainbowSpeed = 2;
                @SerialEntry public int beam1RainbowDelay = 0;
                @SerialEntry public float beam1RainbowSaturation = 1;
                @SerialEntry public float beam1RainbowBrightness = 1;
            //Rainbow 2
            @SerialEntry public boolean beam2Rainbow = false;
                @SerialEntry public int beam2RainbowSpeed = 2;
                @SerialEntry public int beam2RainbowDelay = 0;
                @SerialEntry public float beam2RainbowSaturation = 1;
                @SerialEntry public float beam2RainbowBrightness = 1;
            //Extras
            @SerialEntry public float beamScrollSpeed = 1;
            @SerialEntry public int beamSides = 8;
            @SerialEntry public RenderMode beamRenderMode = RenderMode.DEFAULT;
                @SerialEntry public boolean beamCulling = false;

    //@formatter:on
    @Updatable
    public static Option<Boolean> o_modEnabled = Option.<Boolean>createBuilder()
            .name(Text.of("Mod Enabled"))
            .controller(CustomTickBoxControllerBuilder::new)
            .stateManager(StateManager.createInstant(true, () -> CONFIG.instance().modEnabled, newVal -> CONFIG.instance().modEnabled = newVal))
            .build();
    public static Option<Boolean> o_showAnimations = Option.<Boolean>createBuilder()
            .name(Text.of("UI Animations"))
            .controller(CustomTickBoxControllerBuilder::new)
            .stateManager(StateManager.createInstant(true, () -> CONFIG.instance().showAnimations, newVal -> CONFIG.instance().showAnimations = newVal))
            .build();
    public static Option<Boolean> o_randomizeAge = Option.<Boolean>createBuilder()
            .name(Text.of("Randomize Age"))
            //TODO: add proper description
            .controller(CustomTickBoxControllerBuilder::new)
            .stateManager(StateManager.createInstant(true, () -> CONFIG.instance().randomizeAge, newVal -> CONFIG.instance().randomizeAge = newVal))
            .build();
    public static Option<Float> o_shadowRadius = Option.<Float>createBuilder()
            .name(Text.of("Shadow Radius"))
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 2f).step(0.1f).formatValue(val -> Text.of(String.format("%.1f", val).replace(".0", "") + (val == 1 ? " block" : " blocks"))))
            .stateManager(StateManager.createInstant(0.5F, () -> CONFIG.instance().shadowRadius, newVal -> CONFIG.instance().shadowRadius = newVal))
            .listener((floatOption, aFloat) -> floatOption.setAvailable(o_modEnabled.pendingValue()))
            .build();
    public static Option<Float> o_shadowAlpha = Option.<Float>createBuilder()
            .name(Text.of("Shadow Opacity"))
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 1f).step(0.01f).formatValue(val -> Text.of(String.format("%d", (int) (val * 100)) + "%")))
            .stateManager(StateManager.createInstant(0.5F, () -> CONFIG.instance().shadowAlpha, newVal -> CONFIG.instance().shadowAlpha = newVal))
            .build();
    public static LinkedOptionImpl<Float> o_baseScale = LinkedOptionImpl.<Float>createBuilder()
            .name(Text.of("Scale"))
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 5f).step(0.05f).formatValue(val -> Text.of(String.format("%.2f", val) + "x")))
            .stateManager(StateManager.createInstant(1F, () -> CONFIG.instance().baseScale, newVal -> CONFIG.instance().baseScale = newVal))
            .build();
    public static LinkedOptionImpl<Integer> o_baseRotation = LinkedOptionImpl.<Integer>createBuilder()
            .name(Text.of("Rotation"))
            .controller(integerOption -> IntegerSliderControllerBuilder.create(integerOption).step(1).range(0, 360).formatValue(value -> Text.of(value + "°")))
            .stateManager(StateManager.createInstant(0, () -> CONFIG.instance().baseRotation, newVal -> CONFIG.instance().baseRotation = newVal))
            //i don't know why i have to do this now, one day it just broke and i had no idea why
            .available(CONFIG.instance().baseRainbow && CONFIG.instance().showBaseMode != BaseRenderMode.NEVER && CONFIG.instance().modEnabled)
            .build();
    public static LinkedOptionImpl<RenderMode> o_baseRenderLayer = LinkedOptionImpl.<RenderMode>createBuilder()
            .name(Text.of("Render Mode"))
            .description(OptionDescription.createBuilder().text(Text.of("Culled: Doesn't render back sides of objects.\n\nWireframe: Draws outlines of objects. Does not support light levels. Line width also cannot be changed due to game limitations.\n\nGateway: Uses the game's end gateway shader. Does not support color, opacity, or light levels.")).build())
            .controller(renderModeOption -> EnumControllerBuilder.create(renderModeOption).enumClass(RenderMode.class))
            .stateManager(StateManager.createInstant(RenderMode.DEFAULT, () -> CONFIG.instance().baseRenderMode, newVal -> CONFIG.instance().baseRenderMode = newVal))
            .build();
    public static Option<Boolean> o_baseCulling = Option.<Boolean>createBuilder()
            .name(Text.of("Culled"))
            .controller(CustomTickBoxControllerBuilder::new)
            .stateManager(StateManager.createInstant(false, () -> CONFIG.instance().baseCulling, newVal -> CONFIG.instance().baseCulling = newVal))
            .build();
    public static Option<Float> o_baseOffset = Option.<Float>createBuilder()
            .name(Text.of("Vertical Offset"))
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(-2F, 2F).step(0.1F).formatValue(val -> Text.of(String.format("%.1f", val).replace(".0", "") + (Math.abs(val) == 1 ? " block" : " blocks"))))
            .stateManager(StateManager.createInstant(-1F, () -> CONFIG.instance().baseOffset, newVal -> CONFIG.instance().baseOffset = newVal))
            .build();
    public static LinkedOptionImpl<Color> o_baseColor = LinkedOptionImpl.<Color>createBuilder()
            .name(Text.of("Color"))
            .controller(ColorControllerBuilderImpl::new)
            .stateManager(StateManager.createInstant(new Color(255, 255, 255), () -> CONFIG.instance().baseColor, newVal -> CONFIG.instance().baseColor = newVal))
            .build();
    public static LinkedOptionImpl<Float> o_baseAlpha = LinkedOptionImpl.<Float>createBuilder()
            .name(Text.of("Opacity"))
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 1f).step(0.01f).formatValue(val -> Text.of(String.format("%.0f", val * 100) + "%")))
            .stateManager(StateManager.createInstant(1F, () -> CONFIG.instance().baseAlpha, newVal -> CONFIG.instance().baseAlpha = newVal))
            .build();
    public static LinkedOptionImpl<Integer> o_baseLightLevel = LinkedOptionImpl.<Integer>createBuilder()
            .name(Text.of("Light Level"))
            .description(OptionDescription.createBuilder().text(Text.of("How brightly lit the object is. -1 uses the world's lighting, while 0-255 is mapped respectively.")).build())
            .controller(integerOption -> IntegerSliderControllerBuilder.create(integerOption).step(1).range(-1, 255).formatValue(value -> Text.of(value == -1 ? "Use World Light" : value + "")))
            .stateManager(StateManager.createInstant(-1, () -> CONFIG.instance().baseLightLevel, newVal -> CONFIG.instance().baseLightLevel = newVal))
            .build();
    @Updatable
    public static LinkedOptionImpl<Boolean> o_baseRainbow = LinkedOptionImpl.<Boolean>createBuilder()
            .name(Text.of("Rainbow Enabled"))
            .controller(CustomTickBoxControllerBuilder::new)
            .stateManager(StateManager.createInstant(false, () -> CONFIG.instance().baseRainbow, newVal -> CONFIG.instance().baseRainbow = newVal))
            .build();
    public static LinkedOptionImpl<Integer> o_baseRainbowSpeed = LinkedOptionImpl.<Integer>createBuilder()
            .name(Text.of("Speed"))
            .controller(integerOption -> IntegerSliderControllerBuilder.create(integerOption).step(1).range(1, 10).formatValue(value -> Text.of(value + "x")))
            .stateManager(StateManager.createInstant(2, () -> CONFIG.instance().baseRainbowSpeed, newVal -> CONFIG.instance().baseRainbowSpeed = newVal))
            //i don't know why i have to do this now, one day it just broke and i had no idea why
            .available(CONFIG.instance().baseRainbow && CONFIG.instance().showBaseMode != BaseRenderMode.NEVER && CONFIG.instance().modEnabled)
            .build();
    public static LinkedOptionImpl<Integer> o_baseRainbowDelay = LinkedOptionImpl.<Integer>createBuilder()
            .name(Text.of("Delay"))
            .controller(integerOption -> IntegerSliderControllerBuilder.create(integerOption).step(1).range(-500, 500).formatValue(integer -> Text.of(integer + "ms")))
            .stateManager(StateManager.createInstant(0, () -> CONFIG.instance().baseRainbowDelay, newVal -> CONFIG.instance().baseRainbowDelay = newVal))
            .available(CONFIG.instance().baseRainbow && CONFIG.instance().showBaseMode != BaseRenderMode.NEVER && CONFIG.instance().modEnabled)
            .build();
    public static LinkedOptionImpl<Float> o_baseRainbowSaturation = LinkedOptionImpl.<Float>createBuilder()
            .name(Text.of("Saturation"))
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 1f).step(0.01f).formatValue(val -> Text.of(String.format("%.0f", val * 100) + "%")))
            .stateManager(StateManager.createInstant(1F, () -> CONFIG.instance().baseRainbowSaturation, newVal -> CONFIG.instance().baseRainbowSaturation = newVal))
            .available(CONFIG.instance().baseRainbow && CONFIG.instance().showBaseMode != BaseRenderMode.NEVER && CONFIG.instance().modEnabled)
            .build();
    public static LinkedOptionImpl<Float> o_baseRainbowBrightness = LinkedOptionImpl.<Float>createBuilder()
            .name(Text.of("Brightness"))
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 1f).step(0.01f).formatValue(val -> Text.of(String.format("%.0f", val * 100) + "%")))
            .stateManager(StateManager.createInstant(1F, () -> CONFIG.instance().baseRainbowBrightness, newVal -> CONFIG.instance().baseRainbowBrightness = newVal))
            .available(CONFIG.instance().baseRainbow && CONFIG.instance().showBaseMode != BaseRenderMode.NEVER && CONFIG.instance().modEnabled)
            .build();
    //keep this one below all other base options
    public static Option<BaseRenderMode> o_baseRenderMode = Option.<BaseRenderMode>createBuilder()
            .name(Text.of("Show"))
            .description(OptionDescription.createBuilder().text(Text.of("When to show the base.\n\nAlways: Always shows the base.\n\nDefault: Only show the base when the crystal has the base enabled.\n\nNever: Never shows the base.")).build())
            .controller(renderModeOption -> EnumControllerBuilder.create(renderModeOption).enumClass(BaseRenderMode.class))
            .listener(ChamsConfig::specialUpdate)
            .stateManager(StateManager.createInstant(BaseRenderMode.DEFAULT, () -> CONFIG.instance().showBaseMode, newVal -> CONFIG.instance().showBaseMode = newVal))
            .build();
    @Updatable
    public static Option<Boolean> o_renderCore = Option.<Boolean>createBuilder()
            .name(Text.of("Render Core"))
            .controller(CustomTickBoxControllerBuilder::new)
            .stateManager(StateManager.createInstant(true, () -> CONFIG.instance().renderCore, newVal -> CONFIG.instance().renderCore = newVal))
            .build();
    public static LinkedOptionImpl<Float> o_coreOffset = LinkedOptionImpl.<Float>createBuilder()
            .name(Text.of("Vertical Offset"))
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(-2.5f, 2.5f).step(0.1f).formatValue(val -> Text.of(String.format("%.1f", val).replace(".0", "") + (Math.abs(val) == 1 ? " block" : " blocks"))))
            .stateManager(StateManager.createInstant(0F, () -> CONFIG.instance().coreOffset, newVal -> CONFIG.instance().coreOffset = newVal))
            .build();
    public static LinkedOptionImpl<Float> o_coreRotationSpeed = LinkedOptionImpl.<Float>createBuilder()
            .name(Text.of("Rotation Speed"))
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(-25f, 25f).step(0.1f).formatValue(val -> Text.of(String.format("%.1f", val) + "x")))
            .stateManager(StateManager.createInstant(1F, () -> CONFIG.instance().coreRotationSpeed, newVal -> CONFIG.instance().coreRotationSpeed = newVal))
            .build();
    public static LinkedOptionImpl<Float> o_coreBounceHeight = LinkedOptionImpl.<Float>createBuilder()
            .name(Text.of("Bounce Height"))
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 2f).step(0.05f).formatValue(val -> Text.of(String.format("%.2f", val) + "x")))
            .stateManager(StateManager.createInstant(1F, () -> CONFIG.instance().coreBounceHeight, newVal -> CONFIG.instance().coreBounceHeight = newVal))
            .build();
    public static LinkedOptionImpl<Float> o_coreBounceSpeed = LinkedOptionImpl.<Float>createBuilder()
            .name(Text.of("Bounce Speed"))
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 2f).step(0.05f).formatValue(val -> Text.of(String.format("%.2f", val) + "x")))
            .stateManager(StateManager.createInstant(1F, () -> CONFIG.instance().coreBounceSpeed, newVal -> CONFIG.instance().coreBounceSpeed = newVal))
            .build();
    public static LinkedOptionImpl<Float> o_coreTickDelay = LinkedOptionImpl.<Float>createBuilder()
            .name(Text.of("Tick Delay"))
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(-20f, 20f).step(0.1f).formatValue(val -> Text.of(String.format("%.1f", val) + " ticks")))
            .stateManager(StateManager.createInstant(0F, () -> CONFIG.instance().coreTickDelay, newVal -> CONFIG.instance().coreTickDelay = newVal))
            .build();
    public static LinkedOptionImpl<Float> o_coreScale = LinkedOptionImpl.<Float>createBuilder()
            .name(Text.of("Scale"))
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 5f).step(0.05f).formatValue(val -> Text.of(String.format("%.2f", val) + "x")))
            .stateManager(StateManager.createInstant(1F, () -> CONFIG.instance().coreScale, newVal -> CONFIG.instance().coreScale = newVal))
            .build();
    public static LinkedOptionImpl<Color> o_coreColor = LinkedOptionImpl.<Color>createBuilder()
            .name(Text.of("Color"))
            .controller(ColorControllerBuilderImpl::new)
            .stateManager(StateManager.createInstant(new Color(255, 255, 255), () -> CONFIG.instance().coreColor, newVal -> CONFIG.instance().coreColor = newVal))
            .build();
    public static LinkedOptionImpl<Float> o_coreAlpha = LinkedOptionImpl.<Float>createBuilder()
            .name(Text.of("Opacity"))
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 1f).step(0.01f).formatValue(val -> Text.of(String.format("%.0f", val * 100) + "%")))
            .stateManager(StateManager.createInstant(1F, () -> CONFIG.instance().coreAlpha, newVal -> CONFIG.instance().coreAlpha = newVal))
            .build();
    public static LinkedOptionImpl<Integer> o_coreLightLevel = LinkedOptionImpl.<Integer>createBuilder()
            .name(Text.of("Light Level"))
            .description(OptionDescription.createBuilder().text(Text.of("How brightly lit the object is. -1 uses the world's lighting, while 0-255 is mapped respectively.")).build())
            .controller(integerOption -> IntegerSliderControllerBuilder.create(integerOption).step(1).range(-1, 255).formatValue(value -> Text.of(value == -1 ? "Use World Light" : value + "")))
            .stateManager(StateManager.createInstant(-1, () -> CONFIG.instance().coreLightLevel, newVal -> CONFIG.instance().coreLightLevel = newVal))
            .build();
    public static LinkedOptionImpl<RenderMode> o_coreRenderLayer = LinkedOptionImpl.<RenderMode>createBuilder()
            .name(Text.of("Render Mode"))
            .description(OptionDescription.createBuilder().text(Text.of("Culled: Doesn't render back sides of objects.\n\nWireframe: Draws outlines of objects. Does not support light levels. Line width also cannot be changed due to game limitations.\n\nGateway: Uses the game's end gateway shader. Does not support color, opacity, or light levels.")).build())
            .controller(renderModeOption -> EnumControllerBuilder.create(renderModeOption).enumClass(RenderMode.class))
            .stateManager(StateManager.createInstant(RenderMode.DEFAULT, () -> CONFIG.instance().coreRenderLayer, newVal -> CONFIG.instance().coreRenderLayer = newVal))
            .build();
    public static Option<Boolean> o_coreCulling = Option.<Boolean>createBuilder()
            .name(Text.of("Culled"))
            .controller(CustomTickBoxControllerBuilder::new)
            .stateManager(StateManager.createInstant(false, () -> CONFIG.instance().coreCulling, newVal -> CONFIG.instance().coreCulling = newVal))
            .build();
    @Updatable
    public static LinkedOptionImpl<Boolean> o_coreRainbow = LinkedOptionImpl.<Boolean>createBuilder()
            .name(Text.of("Rainbow Enabled"))
            .controller(CustomTickBoxControllerBuilder::new)
            .stateManager(StateManager.createInstant(false, () -> CONFIG.instance().coreRainbow, newVal -> CONFIG.instance().coreRainbow = newVal))
            .build();
    public static LinkedOptionImpl<Integer> o_coreRainbowSpeed = LinkedOptionImpl.<Integer>createBuilder()
            .name(Text.of("Speed"))
            .controller(integerOption -> IntegerSliderControllerBuilder.create(integerOption).step(1).range(1, 10).formatValue(value -> Text.of(value + "x")))
            .stateManager(StateManager.createInstant(2, () -> CONFIG.instance().coreRainbowSpeed, newVal -> CONFIG.instance().coreRainbowSpeed = newVal))
            .available(CONFIG.instance().coreRainbow && CONFIG.instance().renderCore && CONFIG.instance().modEnabled)
            .build();
    public static LinkedOptionImpl<Integer> o_coreRainbowDelay = LinkedOptionImpl.<Integer>createBuilder()
            .name(Text.of("Delay"))
            .controller(integerOption -> IntegerSliderControllerBuilder.create(integerOption).step(1).range(-500, 500).formatValue(integer -> Text.of(integer + "ms")))
            .stateManager(StateManager.createInstant(0, () -> CONFIG.instance().coreRainbowDelay, newVal -> CONFIG.instance().coreRainbowDelay = newVal))
            .available(CONFIG.instance().coreRainbow && CONFIG.instance().renderCore && CONFIG.instance().modEnabled)
            .build();
    public static LinkedOptionImpl<Float> o_coreRainbowSaturation = LinkedOptionImpl.<Float>createBuilder()
            .name(Text.of("Saturation"))
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 1f).step(0.01f).formatValue(val -> Text.of(String.format("%.0f", val * 100) + "%")))
            .stateManager(StateManager.createInstant(1F, () -> CONFIG.instance().coreRainbowSaturation, newVal -> CONFIG.instance().coreRainbowSaturation = newVal))
            .available(CONFIG.instance().coreRainbow && CONFIG.instance().renderCore && CONFIG.instance().modEnabled)
            .build();
    public static LinkedOptionImpl<Float> o_coreRainbowBrightness = LinkedOptionImpl.<Float>createBuilder()
            .name(Text.of("Brightness"))
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 1f).step(0.01f).formatValue(val -> Text.of(String.format("%.0f", val * 100) + "%")))
            .stateManager(StateManager.createInstant(1F, () -> CONFIG.instance().coreRainbowBrightness, newVal -> CONFIG.instance().coreRainbowBrightness = newVal))
            .available(CONFIG.instance().coreRainbow && CONFIG.instance().renderCore && CONFIG.instance().modEnabled)
            .build();
    @Updatable
    public static Option<Boolean> o_coreAlphaAnimation = Option.<Boolean>createBuilder()
            .name(Text.of("Animation Enabled"))
            .controller(CustomTickBoxControllerBuilder::new)
            .stateManager(StateManager.createInstant(false, () -> CONFIG.instance().coreAlphaAnimation, newVal -> CONFIG.instance().coreAlphaAnimation = newVal))
            .build();
    public static Option<Float> o_coreStartOpacity = Option.<Float>createBuilder()
            .name(Text.of("Starting Opacity"))
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 1f).step(0.01f).formatValue(val -> Text.of(String.format("%.0f", val * 100) + "%")))
            .stateManager(StateManager.createInstant(0F, () -> CONFIG.instance().coreStartAlpha, newVal -> CONFIG.instance().coreStartAlpha = newVal))
            .build();
    public static Option<Float> o_coreAlphaDelay = Option.<Float>createBuilder()
            .name(Text.of("Delay"))
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 2f).step(0.1f).formatValue(val -> Text.of(String.format("%.1f", val) + "s")))
            .stateManager(StateManager.createInstant(0F, () -> CONFIG.instance().coreAlphaDelay, newVal -> CONFIG.instance().coreAlphaDelay = newVal))
            .build();
    public static Option<Float> o_coreAlphaAnimDuration = Option.<Float>createBuilder()
            .name(Text.of("Duration"))
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 2f).step(0.1f).formatValue(val -> Text.of(String.format("%.1f", val) + "s")))
            .stateManager(StateManager.createInstant(1F, () -> CONFIG.instance().coreAlphaAnimDuration, newVal -> CONFIG.instance().coreAlphaAnimDuration = newVal))
            .build();
    public static Option<Easings> o_coreAlphaEasing = Option.<Easings>createBuilder()
            .name(Text.of("Easing"))
            .controller(easingsOption -> EnumControllerBuilder.create(easingsOption).enumClass(Easings.class))
            .stateManager(StateManager.createInstant(Easings.OFF, () -> CONFIG.instance().coreAlphaEasing, newVal -> CONFIG.instance().coreAlphaEasing = newVal))
            .build();
    @Updatable
    public static Option<Boolean> o_coreScaleAnimation = Option.<Boolean>createBuilder()
            .name(Text.of("Animation Enabled"))
            .controller(CustomTickBoxControllerBuilder::new)
            .stateManager(StateManager.createInstant(false, () -> CONFIG.instance().coreScaleAnimation, newVal -> CONFIG.instance().coreScaleAnimation = newVal))
            .build();
    public static Option<Float> o_coreStartScale = Option.<Float>createBuilder()
            .name(Text.of("Starting Scale"))
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 5f).step(0.05f).formatValue(val -> Text.of(String.format("%.2f", val) + "x")))
            .stateManager(StateManager.createInstant(0F, () -> CONFIG.instance().coreStartScale, newVal -> CONFIG.instance().coreStartScale = newVal))
            .build();
    public static Option<Float> o_coreScaleDelay = Option.<Float>createBuilder()
            .name(Text.of("Delay"))
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 2f).step(0.1f).formatValue(val -> Text.of(String.format("%.1f", val) + "s")))
            .stateManager(StateManager.createInstant(0F, () -> CONFIG.instance().coreScaleDelay, newVal -> CONFIG.instance().coreScaleDelay = newVal))
            .build();
    public static Option<Float> o_coreScaleAnimDuration = Option.<Float>createBuilder()
            .name(Text.of("Duration"))
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 2f).step(0.1f).formatValue(val -> Text.of(String.format("%.1f", val) + "s")))
            .stateManager(StateManager.createInstant(1F, () -> CONFIG.instance().coreScaleAnimDuration, newVal -> CONFIG.instance().coreScaleAnimDuration = newVal))
            .build();
    public static Option<Easings> o_coreScaleEasing = Option.<Easings>createBuilder()
            .name(Text.of("Easing"))
            .controller(easingsOption -> EnumControllerBuilder.create(easingsOption).enumClass(Easings.class))
            .stateManager(StateManager.createInstant(Easings.OFF, () -> CONFIG.instance().coreScaleEasing, newVal -> CONFIG.instance().coreScaleEasing = newVal))
            .build();
    //TODO: look into drop-down widget
    @Updatable
    public static Option<Boolean> o_renderFrame1 = Option.<Boolean>createBuilder()
            .name(Text.of("Render Frame 1"))
            .controller(CustomTickBoxControllerBuilder::new)
            .stateManager(StateManager.createInstant(true, () -> CONFIG.instance().renderFrame1, newVal -> CONFIG.instance().renderFrame1 = newVal))
            .build();
    public static LinkedOptionImpl<Float> o_frame1Offset = LinkedOptionImpl.<Float>createBuilder()
            .name(Text.of("Vertical Offset"))
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(-2.5f, 2.5f).step(0.1f).formatValue(val -> Text.of(String.format("%.1f", val).replace(".0", "") + (Math.abs(val) == 1 ? " block" : " blocks"))))
            .stateManager(StateManager.createInstant(0F, () -> CONFIG.instance().frame1Offset, newVal -> CONFIG.instance().frame1Offset = newVal))
            .build();
    public static LinkedOptionImpl<Float> o_frame1RotationSpeed = LinkedOptionImpl.<Float>createBuilder()
            .name(Text.of("Rotation Speed"))
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(-15f, 15f).step(0.1f).formatValue(val -> Text.of(String.format("%.1f", val) + "x")))
            .stateManager(StateManager.createInstant(1F, () -> CONFIG.instance().frame1RotationSpeed, newVal -> CONFIG.instance().frame1RotationSpeed = newVal))
            .build();
    public static LinkedOptionImpl<Float> o_frame1BounceHeight = LinkedOptionImpl.<Float>createBuilder()
            .name(Text.of("Bounce Height"))
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 2f).step(0.05f).formatValue(val -> Text.of(String.format("%.2f", val) + "x")))
            .stateManager(StateManager.createInstant(1F, () -> CONFIG.instance().frame1BounceHeight, newVal -> CONFIG.instance().frame1BounceHeight = newVal))
            .build();
    public static LinkedOptionImpl<Float> o_frame1BounceSpeed = LinkedOptionImpl.<Float>createBuilder()
            .name(Text.of("Bounce Speed"))
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 2f).step(0.05f).formatValue(val -> Text.of(String.format("%.2f", val) + "x")))
            .stateManager(StateManager.createInstant(1F, () -> CONFIG.instance().frame1BounceSpeed, newVal -> CONFIG.instance().frame1BounceSpeed = newVal))
            .build();
    public static LinkedOptionImpl<Float> o_frame1TickDelay = LinkedOptionImpl.<Float>createBuilder()
            .name(Text.of("Tick Delay"))
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(-20f, 20f).step(0.1f).formatValue(val -> Text.of(String.format("%.1f", val) + " ticks")))
            .stateManager(StateManager.createInstant(0F, () -> CONFIG.instance().frame1TickDelay, newVal -> CONFIG.instance().frame1TickDelay = newVal))
            .build();
    public static LinkedOptionImpl<Float> o_frame1Scale = LinkedOptionImpl.<Float>createBuilder()
            .name(Text.of("Scale"))
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 5f).step(0.05f).formatValue(val -> Text.of(String.format("%.2f", val) + "x")))
            .stateManager(StateManager.createInstant(1F, () -> CONFIG.instance().frame1Scale, newVal -> CONFIG.instance().frame1Scale = newVal))
            .build();
    public static LinkedOptionImpl<Color> o_frame1Color = LinkedOptionImpl.<Color>createBuilder()
            .name(Text.of("Color"))
            .controller(ColorControllerBuilderImpl::new)
            .stateManager(StateManager.createInstant(new Color(255, 255, 255), () -> CONFIG.instance().frame1Color, newVal -> CONFIG.instance().frame1Color = newVal))
            .build();
    public static LinkedOptionImpl<Float> o_frame1Alpha = LinkedOptionImpl.<Float>createBuilder()
            .name(Text.of("Opacity"))
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 1f).step(0.01f).formatValue(val -> Text.of(String.format("%.0f", val * 100) + "%")))
            .stateManager(StateManager.createInstant(1F, () -> CONFIG.instance().frame1Alpha, newVal -> CONFIG.instance().frame1Alpha = newVal))
            .build();
    public static LinkedOptionImpl<Integer> o_frame1LightLevel = LinkedOptionImpl.<Integer>createBuilder()
            .name(Text.of("Light Level"))
            .description(OptionDescription.createBuilder().text(Text.of("How brightly lit the object is. -1 uses the world's lighting, while 0-255 is mapped respectively.")).build())
            .controller(integerOption -> IntegerSliderControllerBuilder.create(integerOption).step(1).range(-1, 255).formatValue(value -> Text.of(value == -1 ? "Use World Light" : value + "")))
            .stateManager(StateManager.createInstant(-1, () -> CONFIG.instance().frame1LightLevel, newVal -> CONFIG.instance().frame1LightLevel = newVal))
            .build();
    public static LinkedOptionImpl<RenderMode> o_frame1RenderLayer = LinkedOptionImpl.<RenderMode>createBuilder()
            .name(Text.of("Render Mode"))
            .description(OptionDescription.createBuilder().text(Text.of("Culled: Doesn't render back sides of objects.\n\nWireframe: Draws outlines of objects. Does not support light levels. Line width also cannot be changed due to game limitations.\n\nGateway: Uses the game's end gateway shader. Does not support color, opacity, or light levels.")).build())
            .controller(renderModeOption -> EnumControllerBuilder.create(renderModeOption).enumClass(RenderMode.class))
            .stateManager(StateManager.createInstant(RenderMode.DEFAULT, () -> CONFIG.instance().frame1RenderLayer, newVal -> CONFIG.instance().frame1RenderLayer = newVal))
            .build();
    public static Option<Boolean> o_frame1Culling = Option.<Boolean>createBuilder()
            .name(Text.of("Culled"))
            .controller(CustomTickBoxControllerBuilder::new)
            .stateManager(StateManager.createInstant(false, () -> CONFIG.instance().frame1Culling, newVal -> CONFIG.instance().frame1Culling = newVal))
            .build();
    @Updatable
    public static LinkedOptionImpl<Boolean> o_frame1Rainbow = LinkedOptionImpl.<Boolean>createBuilder()
            .name(Text.of("Rainbow"))
            .controller(CustomTickBoxControllerBuilder::new)
            .stateManager(StateManager.createInstant(false, () -> CONFIG.instance().frame1Rainbow, newVal -> CONFIG.instance().frame1Rainbow = newVal))
            .build();
    public static LinkedOptionImpl<Integer> o_frame1RainbowSpeed = LinkedOptionImpl.<Integer>createBuilder()
            .name(Text.of("Rainbow Speed"))
            .controller(integerOption -> IntegerSliderControllerBuilder.create(integerOption).step(1).range(1, 10).formatValue(value -> Text.of(value + "x")))
            .stateManager(StateManager.createInstant(2, () -> CONFIG.instance().frame1RainbowSpeed, newVal -> CONFIG.instance().frame1RainbowSpeed = newVal))
            .available(CONFIG.instance().frame1Rainbow && CONFIG.instance().renderFrame1 && CONFIG.instance().modEnabled)
            .build();
    public static LinkedOptionImpl<Integer> o_frame1RainbowDelay = LinkedOptionImpl.<Integer>createBuilder()
            .name(Text.of("Rainbow Delay"))
            .controller(integerOption -> IntegerSliderControllerBuilder.create(integerOption).step(1).range(-500, 500).formatValue(integer -> Text.of(integer + "ms")))
            .stateManager(StateManager.createInstant(0, () -> CONFIG.instance().frame1RainbowDelay, newVal -> CONFIG.instance().frame1RainbowDelay = newVal))
            .available(CONFIG.instance().frame1Rainbow && CONFIG.instance().renderFrame1 && CONFIG.instance().modEnabled)
            .build();
    public static LinkedOptionImpl<Float> o_frame1RainbowSaturation = LinkedOptionImpl.<Float>createBuilder()
            .name(Text.of("Saturation"))
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 1f).step(0.01f).formatValue(val -> Text.of(String.format("%.0f", val * 100) + "%")))
            .stateManager(StateManager.createInstant(1F, () -> CONFIG.instance().frame1RainbowSaturation, newVal -> CONFIG.instance().frame1RainbowSaturation = newVal))
            .available(CONFIG.instance().frame1Rainbow && CONFIG.instance().renderFrame1 && CONFIG.instance().modEnabled)
            .build();
    public static LinkedOptionImpl<Float> o_frame1RainbowBrightness = LinkedOptionImpl.<Float>createBuilder()
            .name(Text.of("Brightness"))
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 1f).step(0.01f).formatValue(val -> Text.of(String.format("%.0f", val * 100) + "%")))
            .stateManager(StateManager.createInstant(1F, () -> CONFIG.instance().frame1RainbowBrightness, newVal -> CONFIG.instance().frame1RainbowBrightness = newVal))
            .available(CONFIG.instance().frame1Rainbow && CONFIG.instance().renderFrame1 && CONFIG.instance().modEnabled)
            .build();
    @Updatable
    public static Option<Boolean> o_renderFrame2 = Option.<Boolean>createBuilder()
            .name(Text.of("Render Frame 2"))
            .controller(CustomTickBoxControllerBuilder::new)
            .stateManager(StateManager.createInstant(true, () -> CONFIG.instance().renderFrame2, newVal -> CONFIG.instance().renderFrame2 = newVal))
            .build();
    public static LinkedOptionImpl<Float> o_frame2Offset = LinkedOptionImpl.<Float>createBuilder()
            .name(Text.of("Vertical Offset"))
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(-2.5f, 2.5f).step(0.1f).formatValue(val -> Text.of(String.format("%.1f", val).replace(".0", "") + (Math.abs(val) == 1 ? " block" : " blocks"))))
            .stateManager(StateManager.createInstant(0F, () -> CONFIG.instance().frame2Offset, newVal -> CONFIG.instance().frame2Offset = newVal))
            .build();
    public static LinkedOptionImpl<Float> o_frame2RotationSpeed = LinkedOptionImpl.<Float>createBuilder()
            .name(Text.of("Rotation Speed"))
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(-25f, 25f).step(0.1f).formatValue(val -> Text.of(String.format("%.1f", val) + "x")))
            .stateManager(StateManager.createInstant(1F, () -> CONFIG.instance().frame2RotationSpeed, newVal -> CONFIG.instance().frame2RotationSpeed = newVal))
            .build();
    public static LinkedOptionImpl<Float> o_frame2BounceHeight = LinkedOptionImpl.<Float>createBuilder()
            .name(Text.of("Bounce Height"))
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 2f).step(0.05f).formatValue(val -> Text.of(String.format("%.2f", val) + "x")))
            .stateManager(StateManager.createInstant(1F, () -> CONFIG.instance().frame2BounceHeight, newVal -> CONFIG.instance().frame2BounceHeight = newVal))
            .build();
    public static LinkedOptionImpl<Float> o_frame2BounceSpeed = LinkedOptionImpl.<Float>createBuilder()
            .name(Text.of("Bounce Speed"))
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 2f).step(0.05f).formatValue(val -> Text.of(String.format("%.2f", val) + "x")))
            .stateManager(StateManager.createInstant(1F, () -> CONFIG.instance().frame2BounceSpeed, newVal -> CONFIG.instance().frame2BounceSpeed = newVal))
            .build();
    public static LinkedOptionImpl<Float> o_frame2TickDelay = LinkedOptionImpl.<Float>createBuilder()
            .name(Text.of("Tick Delay"))
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(-20f, 20f).step(0.1f).formatValue(val -> Text.of(String.format("%.1f", val) + " ticks")))
            .stateManager(StateManager.createInstant(0F, () -> CONFIG.instance().frame2TickDelay, newVal -> CONFIG.instance().frame2TickDelay = newVal))
            .build();
    public static LinkedOptionImpl<Float> o_frame2Scale = LinkedOptionImpl.<Float>createBuilder()
            .name(Text.of("Scale"))
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 5f).step(0.05f).formatValue(val -> Text.of(String.format("%.2f", val) + "x")))
            .stateManager(StateManager.createInstant(1F, () -> CONFIG.instance().frame2Scale, newVal -> CONFIG.instance().frame2Scale = newVal))
            .build();
    public static LinkedOptionImpl<Color> o_frame2Color = LinkedOptionImpl.<Color>createBuilder()
            .name(Text.of("Color"))
            .controller(ColorControllerBuilderImpl::new)
            .stateManager(StateManager.createInstant(new Color(255, 255, 255), () -> CONFIG.instance().frame2Color, newVal -> CONFIG.instance().frame2Color = newVal))
            .build();
    public static LinkedOptionImpl<Float> o_frame2Alpha = LinkedOptionImpl.<Float>createBuilder()
            .name(Text.of("Opacity"))
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 1f).step(0.01f).formatValue(val -> Text.of(String.format("%.0f", val * 100) + "%")))
            .stateManager(StateManager.createInstant(1F, () -> CONFIG.instance().frame2Alpha, newVal -> CONFIG.instance().frame2Alpha = newVal))
            .build();
    public static LinkedOptionImpl<Integer> o_frame2LightLevel = LinkedOptionImpl.<Integer>createBuilder()
            .name(Text.of("Light Level"))
            .description(OptionDescription.createBuilder().text(Text.of("How brightly lit the object is. -1 uses the world's lighting, while 0-255 is mapped respectively.")).build())
            .controller(integerOption -> IntegerSliderControllerBuilder.create(integerOption).step(1).range(-1, 255).formatValue(value -> Text.of(value == -1 ? "Use World Light" : value + "")))
            .stateManager(StateManager.createInstant(-1, () -> CONFIG.instance().frame2LightLevel, newVal -> CONFIG.instance().frame2LightLevel = newVal))
            .build();
    public static LinkedOptionImpl<RenderMode> o_frame2RenderLayer = LinkedOptionImpl.<RenderMode>createBuilder()
            .name(Text.of("Render Mode"))
            .description(OptionDescription.createBuilder().text(Text.of("Culled: Doesn't render back sides of objects.\n\nWireframe: Draws outlines of objects. Does not support light levels. Line width also cannot be changed due to game limitations.\n\nGateway: Uses the game's end gateway shader. Does not support color, opacity, or light levels.")).build())
            .controller(renderModeOption -> EnumControllerBuilder.create(renderModeOption).enumClass(RenderMode.class))
            .stateManager(StateManager.createInstant(RenderMode.DEFAULT, () -> CONFIG.instance().frame2RenderLayer, newVal -> CONFIG.instance().frame2RenderLayer = newVal))
            .build();
    public static Option<Boolean> o_frame2Culling = Option.<Boolean>createBuilder()
            .name(Text.of("Culled"))
            .controller(CustomTickBoxControllerBuilder::new)
            .stateManager(StateManager.createInstant(false, () -> CONFIG.instance().frame2Culling, newVal -> CONFIG.instance().frame2Culling = newVal))
            .build();
    @Updatable
    public static LinkedOptionImpl<Boolean> o_frame2Rainbow = LinkedOptionImpl.<Boolean>createBuilder()
            .name(Text.of("Rainbow"))
            .controller(CustomTickBoxControllerBuilder::new)
            .stateManager(StateManager.createInstant(false, () -> CONFIG.instance().frame2Rainbow, newVal -> CONFIG.instance().frame2Rainbow = newVal))
            .build();
    public static LinkedOptionImpl<Integer> o_frame2RainbowSpeed = LinkedOptionImpl.<Integer>createBuilder()
            .name(Text.of("Rainbow Speed"))
            .controller(integerOption -> IntegerSliderControllerBuilder.create(integerOption).step(1).range(1, 10).formatValue(value -> Text.of(value + "x")))
            .stateManager(StateManager.createInstant(2, () -> CONFIG.instance().frame2RainbowSpeed, newVal -> CONFIG.instance().frame2RainbowSpeed = newVal))
            .available(CONFIG.instance().frame2Rainbow && CONFIG.instance().renderFrame1 && CONFIG.instance().modEnabled)
            .build();
    public static LinkedOptionImpl<Integer> o_frame2RainbowDelay = LinkedOptionImpl.<Integer>createBuilder()
            .name(Text.of("Rainbow Delay"))
            .controller(integerOption -> IntegerSliderControllerBuilder.create(integerOption).step(1).range(-500, 500).formatValue(integer -> Text.of(integer + "ms")))
            .stateManager(StateManager.createInstant(0, () -> CONFIG.instance().frame2RainbowDelay, newVal -> CONFIG.instance().frame2RainbowDelay = newVal))
            .available(CONFIG.instance().frame2Rainbow && CONFIG.instance().renderFrame1 && CONFIG.instance().modEnabled)
            .build();
    public static LinkedOptionImpl<Float> o_frame2RainbowSaturation = LinkedOptionImpl.<Float>createBuilder()
            .name(Text.of("Saturation"))
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 1f).step(0.01f).formatValue(val -> Text.of(String.format("%.0f", val * 100) + "%")))
            .stateManager(StateManager.createInstant(1F, () -> CONFIG.instance().frame2RainbowSaturation, newVal -> CONFIG.instance().frame2RainbowSaturation = newVal))
            .available(CONFIG.instance().frame2Rainbow && CONFIG.instance().renderFrame2 && CONFIG.instance().modEnabled)
            .build();
    public static LinkedOptionImpl<Float> o_frame2RainbowBrightness = LinkedOptionImpl.<Float>createBuilder()
            .name(Text.of("Brightness"))
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 1f).step(0.01f).formatValue(val -> Text.of(String.format("%.0f", val * 100) + "%")))
            .stateManager(StateManager.createInstant(1F, () -> CONFIG.instance().frame2RainbowBrightness, newVal -> CONFIG.instance().frame2RainbowBrightness = newVal))
            .available(CONFIG.instance().frame2Rainbow && CONFIG.instance().renderFrame2 && CONFIG.instance().modEnabled)
            .build();
    @Updatable
    public static Option<Boolean> o_renderBeam = Option.<Boolean>createBuilder()
            .name(Text.of("Render Beam"))
            .controller(CustomTickBoxControllerBuilder::new)
            .stateManager(StateManager.createInstant(true, () -> CONFIG.instance().renderBeam, newVal -> CONFIG.instance().renderBeam = newVal))
            .build();
    public static LinkedOptionImpl<Color> o_beam1Color = LinkedOptionImpl.<Color>createBuilder()
            .name(Text.of("Color"))
            .controller(ColorControllerBuilderImpl::new)
            .stateManager(StateManager.createInstant(new Color(255, 255, 255), () -> CONFIG.instance().beam1Color, newVal -> CONFIG.instance().beam1Color = newVal))
            .build();
    public static LinkedOptionImpl<Float> o_beam1Alpha = LinkedOptionImpl.<Float>createBuilder()
            .name(Text.of("Opacity"))
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 1f).step(0.01f).formatValue(val -> Text.of(String.format("%.0f", val * 100) + "%")))
            .stateManager(StateManager.createInstant(1F, () -> CONFIG.instance().beam1Alpha, newVal -> CONFIG.instance().beam1Alpha = newVal))
            .build();
    public static LinkedOptionImpl<Integer> o_beam1LightLevel = LinkedOptionImpl.<Integer>createBuilder()
            .name(Text.of("Light Level"))
            .description(OptionDescription.createBuilder().text(Text.of("How brightly lit the object is. -1 uses the world's lighting, while 0-255 is mapped respectively.")).build())
            .controller(integerOption -> IntegerSliderControllerBuilder.create(integerOption).step(1).range(-1, 255).formatValue(value -> Text.of(value == -1 ? "Use World Light" : value + "")))
            .stateManager(StateManager.createInstant(-1, () -> CONFIG.instance().beam1LightLevel, newVal -> CONFIG.instance().beam1LightLevel = newVal))
            .build();
    @Updatable
    public static LinkedOptionImpl<Boolean> o_beam1Rainbow = LinkedOptionImpl.<Boolean>createBuilder()
            .name(Text.of("Rainbow"))
            .controller(CustomTickBoxControllerBuilder::new)
            .stateManager(StateManager.createInstant(false, () -> CONFIG.instance().beam1Rainbow, newVal -> CONFIG.instance().beam1Rainbow = newVal))
            .build();
    public static LinkedOptionImpl<Integer> o_beam1RainbowSpeed = LinkedOptionImpl.<Integer>createBuilder()
            .name(Text.of("Rainbow Speed"))
            .controller(integerOption -> IntegerSliderControllerBuilder.create(integerOption).step(1).range(1, 10).formatValue(value -> Text.of(value + "x")))
            .stateManager(StateManager.createInstant(2, () -> CONFIG.instance().beam1RainbowSpeed, newVal -> CONFIG.instance().beam1RainbowSpeed = newVal))
            .available(CONFIG.instance().beam1Rainbow && CONFIG.instance().renderBeam && CONFIG.instance().modEnabled)
            .build();
    public static LinkedOptionImpl<Integer> o_beam1RainbowDelay = LinkedOptionImpl.<Integer>createBuilder()
            .name(Text.of("Rainbow Delay"))
            .controller(integerOption -> IntegerSliderControllerBuilder.create(integerOption).step(1).range(-500, 500).formatValue(integer -> Text.of(integer + "ms")))
            .stateManager(StateManager.createInstant(0, () -> CONFIG.instance().beam1RainbowDelay, newVal -> CONFIG.instance().beam1RainbowDelay = newVal))
            .available(CONFIG.instance().beam1Rainbow && CONFIG.instance().renderBeam && CONFIG.instance().modEnabled)
            .build();
    public static LinkedOptionImpl<Float> o_beam1RainbowSaturation = LinkedOptionImpl.<Float>createBuilder()
            .name(Text.of("Saturation"))
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 1f).step(0.01f).formatValue(val -> Text.of(String.format("%.0f", val * 100) + "%")))
            .stateManager(StateManager.createInstant(1F, () -> CONFIG.instance().beam1RainbowSaturation, newVal -> CONFIG.instance().beam1RainbowSaturation = newVal))
            .available(CONFIG.instance().beam1Rainbow && CONFIG.instance().renderBeam && CONFIG.instance().modEnabled)
            .build();
    public static LinkedOptionImpl<Float> o_beam1RainbowBrightness = LinkedOptionImpl.<Float>createBuilder()
            .name(Text.of("Brightness"))
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 1f).step(0.01f).formatValue(val -> Text.of(String.format("%.0f", val * 100) + "%")))
            .stateManager(StateManager.createInstant(1F, () -> CONFIG.instance().beam1RainbowBrightness, newVal -> CONFIG.instance().beam1RainbowBrightness = newVal))
            .available(CONFIG.instance().beam1Rainbow && CONFIG.instance().renderBeam && CONFIG.instance().modEnabled)
            .build();
    public static LinkedOptionImpl<Color> o_beam2Color = LinkedOptionImpl.<Color>createBuilder()
            .name(Text.of("Color"))
            .controller(ColorControllerBuilderImpl::new)
            .stateManager(StateManager.createInstant(new Color(0, 0, 0), () -> CONFIG.instance().beam2Color, newVal -> CONFIG.instance().beam2Color = newVal))
            .build();
    public static LinkedOptionImpl<Float> o_beam2Alpha = LinkedOptionImpl.<Float>createBuilder()
            .name(Text.of("Opacity"))
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 1f).step(0.01f).formatValue(val -> Text.of(String.format("%.0f", val * 100) + "%")))
            .stateManager(StateManager.createInstant(1F, () -> CONFIG.instance().beam2Alpha, newVal -> CONFIG.instance().beam2Alpha = newVal))
            .build();
    public static LinkedOptionImpl<Integer> o_beam2LightLevel = LinkedOptionImpl.<Integer>createBuilder()
            .name(Text.of("Light Level"))
            .description(OptionDescription.createBuilder().text(Text.of("How brightly lit the object is. -1 uses the world's lighting, while 0-255 is mapped respectively.")).build())
            .controller(integerOption -> IntegerSliderControllerBuilder.create(integerOption).step(1).range(-1, 255).formatValue(value -> Text.of(value == -1 ? "Use World Light" : value + "")))
            .stateManager(StateManager.createInstant(-1, () -> CONFIG.instance().beam2LightLevel, newVal -> CONFIG.instance().beam2LightLevel = newVal))
            .build();
    @Updatable
    public static LinkedOptionImpl<Boolean> o_beam2Rainbow = LinkedOptionImpl.<Boolean>createBuilder()
            .name(Text.of("Rainbow"))
            .controller(CustomTickBoxControllerBuilder::new)
            .stateManager(StateManager.createInstant(false, () -> CONFIG.instance().beam2Rainbow, newVal -> CONFIG.instance().beam2Rainbow = newVal))
            .build();
    public static LinkedOptionImpl<Integer> o_beam2RainbowSpeed = LinkedOptionImpl.<Integer>createBuilder()
            .name(Text.of("Rainbow Speed"))
            .controller(integerOption -> IntegerSliderControllerBuilder.create(integerOption).step(1).range(1, 10).formatValue(value -> Text.of(value + "x")))
            .stateManager(StateManager.createInstant(2, () -> CONFIG.instance().beam2RainbowSpeed, newVal -> CONFIG.instance().beam2RainbowSpeed = newVal))
            .available(CONFIG.instance().beam2Rainbow && CONFIG.instance().renderBeam && CONFIG.instance().modEnabled)
            .build();
    public static LinkedOptionImpl<Integer> o_beam2RainbowDelay = LinkedOptionImpl.<Integer>createBuilder()
            .name(Text.of("Rainbow Delay"))
            .controller(integerOption -> IntegerSliderControllerBuilder.create(integerOption).step(1).range(-500, 500).formatValue(integer -> Text.of(integer + "ms")))
            .stateManager(StateManager.createInstant(0, () -> CONFIG.instance().beam2RainbowDelay, newVal -> CONFIG.instance().beam2RainbowDelay = newVal))
            .available(CONFIG.instance().beam2Rainbow && CONFIG.instance().renderBeam && CONFIG.instance().modEnabled)
            .build();
    public static LinkedOptionImpl<Float> o_beam2RainbowSaturation = LinkedOptionImpl.<Float>createBuilder()
            .name(Text.of("Saturation"))
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 1f).step(0.01f).formatValue(val -> Text.of(String.format("%.0f", val * 100) + "%")))
            .stateManager(StateManager.createInstant(1F, () -> CONFIG.instance().beam2RainbowSaturation, newVal -> CONFIG.instance().beam2RainbowSaturation = newVal))
            .available(CONFIG.instance().beam2Rainbow && CONFIG.instance().renderBeam && CONFIG.instance().modEnabled)
            .build();
    public static LinkedOptionImpl<Float> o_beam2RainbowBrightness = LinkedOptionImpl.<Float>createBuilder()
            .name(Text.of("Brightness"))
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 1f).step(0.01f).formatValue(val -> Text.of(String.format("%.0f", val * 100) + "%")))
            .stateManager(StateManager.createInstant(1F, () -> CONFIG.instance().beam2RainbowBrightness, newVal -> CONFIG.instance().beam2RainbowBrightness = newVal))
            .available(CONFIG.instance().beam2Rainbow && CONFIG.instance().renderBeam && CONFIG.instance().modEnabled)
            .build();
    public static LinkedOptionImpl<Float> o_beam1Radius = LinkedOptionImpl.<Float>createBuilder()
            .name(Text.of("Radius"))
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(-1f, 1f).step(0.05f).formatValue(val -> Text.of(String.format("%.2f", val).replace(".0", "") + (val == 1 ? " block" : " blocks"))))
            .stateManager(StateManager.createInstant(0.75F, () -> CONFIG.instance().beam1Radius, newVal -> CONFIG.instance().beam1Radius = newVal))
            .build();
    public static LinkedOptionImpl<Float> o_beam2Radius = LinkedOptionImpl.<Float>createBuilder()
            .name(Text.of("Radius"))
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(-1f, 1f).step(0.05f).formatValue(val -> Text.of(String.format("%.2f", val).replace(".0", "") + (val == 1 ? " block" : " blocks"))))
            .stateManager(StateManager.createInstant(0.15F, () -> CONFIG.instance().beam2Radius, newVal -> CONFIG.instance().beam2Radius = newVal))
            .build();
    public static Option<Integer> o_beamSides = Option.<Integer>createBuilder()
            .name(Text.of("Sides"))
            .controller(integerOption -> IntegerSliderControllerBuilder.create(integerOption).step(1).range(2, 64))
            .stateManager(StateManager.createInstant(8, () -> CONFIG.instance().beamSides, newVal -> CONFIG.instance().beamSides = newVal))
            .build();
    public static Option<Float> o_beamScrollSpeed = Option.<Float>createBuilder()
            .name(Text.of("Scroll Speed"))
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(-2f, 2f).step(0.05f).formatValue(val -> Text.of(String.format("%.2f", val))))
            .stateManager(StateManager.createInstant(1F, () -> CONFIG.instance().beamScrollSpeed, newVal -> CONFIG.instance().beamScrollSpeed = newVal))
            .build();
    public static LinkedOptionImpl<RenderMode> o_beamRenderLayer = LinkedOptionImpl.<RenderMode>createBuilder()
            .name(Text.of("Render Mode"))
            .description(OptionDescription.createBuilder().text(Text.of("Culled: Doesn't render back sides of objects.\n\nWireframe: Draws outlines of objects. Does not support light levels. Line width also cannot be changed due to game limitations.\n\nGateway: Uses the game's end gateway shader. Does not support color, opacity, or light levels.")).build())
            .controller(renderModeOption -> EnumControllerBuilder.create(renderModeOption).enumClass(RenderMode.class))
            .stateManager(StateManager.createInstant(RenderMode.DEFAULT, () -> CONFIG.instance().beamRenderMode, newVal -> CONFIG.instance().beamRenderMode = newVal))
            .build();
    public static Option<Boolean> o_beamCulling = Option.<Boolean>createBuilder()
            .name(Text.of("Culled"))
            .controller(CustomTickBoxControllerBuilder::new)
            .stateManager(StateManager.createInstant(false, () -> CONFIG.instance().beamCulling, newVal -> CONFIG.instance().beamCulling = newVal))
            .build();


    public static void update(Option<Boolean> booleanOption, Boolean aBoolean) {
        if (booleanOption.equals(o_modEnabled)) {
            o_shadowAlpha.setAvailable(aBoolean);
            o_shadowRadius.setAvailable(aBoolean);
            o_randomizeAge.setAvailable(aBoolean);
            //TODO: UI animations toggle (maybe)

            o_baseRenderMode.setAvailable(aBoolean);
            o_renderCore.setAvailable(aBoolean);
            o_renderFrame1.setAvailable(aBoolean);
            o_renderFrame2.setAvailable(aBoolean);
            o_renderBeam.setAvailable(aBoolean);
        } else if (booleanOption.equals(o_renderCore)) {
            boolean available = o_renderCore.available() && aBoolean;
            o_coreOffset.setAvailable(available);
            o_coreRotationSpeed.setAvailable(available);
            o_coreBounceHeight.setAvailable(available);
            o_coreBounceSpeed.setAvailable(available);
            o_coreTickDelay.setAvailable(available);
            o_coreScale.setAvailable(available);
            o_coreColor.setAvailable(available);
            o_coreAlpha.setAvailable(available);
            o_coreLightLevel.setAvailable(available);
            o_coreRenderLayer.setAvailable(available);
            o_coreRainbow.setAvailable(available);
            o_coreCulling.setAvailable(available);
        } else if (booleanOption.equals(o_coreRainbow)) {
            boolean available = o_coreRainbow.available() && aBoolean;
            o_coreRainbowSpeed.setAvailable(available);
            o_coreRainbowDelay.setAvailable(available);
            o_coreRainbowSaturation.setAvailable(available);
            o_coreRainbowBrightness.setAvailable(available);
            o_coreColor.setAvailable(!available && o_renderCore.available());
        } else if (booleanOption.equals(o_renderFrame1)) {
            boolean available = o_renderFrame1.available() && aBoolean;
            o_frame1Offset.setAvailable(available);
            o_frame1RotationSpeed.setAvailable(available);
            o_frame1BounceHeight.setAvailable(available);
            o_frame1BounceSpeed.setAvailable(available);
            o_frame1TickDelay.setAvailable(available);
            o_frame1Scale.setAvailable(available);
            o_frame1Color.setAvailable(available);
            o_frame1Alpha.setAvailable(available);
            o_frame1LightLevel.setAvailable(available);
            o_frame1RenderLayer.setAvailable(available);
            o_frame1Rainbow.setAvailable(available);
        } else if (booleanOption.equals(o_frame1Rainbow)) {
            boolean available = o_frame1Rainbow.available() && aBoolean;
            o_frame1Color.setAvailable(!o_frame1Rainbow.available() && o_renderFrame1.available() && o_modEnabled.available());
            o_frame1RainbowSpeed.setAvailable(available);
            o_frame1RainbowDelay.setAvailable(available);
            o_frame1RainbowSaturation.setAvailable(available);
            o_frame1RainbowBrightness.setAvailable(available);
        } else if (booleanOption.equals(o_renderFrame2)) {
            boolean available = o_renderFrame2.available() && aBoolean;
            o_frame2Offset.setAvailable(available);
            o_frame2RotationSpeed.setAvailable(available);
            o_frame2BounceHeight.setAvailable(available);
            o_frame2BounceSpeed.setAvailable(available);
            o_frame2TickDelay.setAvailable(available);
            o_frame2Scale.setAvailable(available);
            o_frame2Color.setAvailable(available);
            o_frame2Alpha.setAvailable(available);
            o_frame2LightLevel.setAvailable(available);
            o_frame2RenderLayer.setAvailable(available);
            o_frame2Rainbow.setAvailable(available);
            o_frame2Culling.setAvailable(available);
        } else if (booleanOption.equals(o_frame2Rainbow)) {
            boolean available = o_frame2Rainbow.available() && aBoolean;
            o_frame2RainbowSpeed.setAvailable(available);
            o_frame2RainbowDelay.setAvailable(available);
            o_frame2RainbowSaturation.setAvailable(available);
            o_frame2RainbowBrightness.setAvailable(available);
            o_frame2Color.setAvailable(!(available));
        } else if (booleanOption.equals(o_renderBeam)) {
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
            boolean available = o_baseRainbow.available() && aBoolean;
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
                                .option(o_randomizeAge)
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
                                                .action((yaclScreen, buttonOption) -> MinecraftClient.getInstance().keyboard.setClipboard(gson.toJson(CONFIG.instance())))
                                                .build())
                                        .option(ButtonOption.createBuilder()
                                                .name(Text.literal("Load Config From Clipboard").formatted(Formatting.DARK_RED, Formatting.BOLD))
                                                .description(OptionDescription.createBuilder().text(Text.of("Loads a configuration from your clipboard if it's valid. WARNING: LOADING A VALID CONFIGURATION WILL OVERWRITE YOUR CURRENT ONE.")).build())
                                                .action((yaclScreen, buttonOption) -> {
                                                    //this sucks but it works!!
                                                    try {
                                                        gson.fromJson(MinecraftClient.getInstance().keyboard.getClipboard(), ChamsConfig.class);
                                                    } catch (JsonSyntaxException e) {
                                                        System.out.println("invalid config!!!");
                                                        return;
                                                    }
                                                    try {
                                                        Path path = FabricLoader.getInstance().getConfigDir().resolve("crystalchams.json");
                                                        Files.delete(path);
                                                        Files.createFile(path);
                                                        Files.writeString(path, MinecraftClient.getInstance().keyboard.getClipboard(), StandardCharsets.UTF_8);
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
                                                .build())
                                        .build())
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
                                .group(MutableOptionGroupImpl.createBuilder()
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
                                .build()
                        )
                        .category(ConfigCategory.createBuilder()
                                .name(Text.of("Core"))
                                .option(o_renderCore)
                                .group(OptionGroup.createBuilder()
                                        .name(Text.of("Movement"))
                                        .option(o_coreOffset)
                                        .option(o_coreRotationSpeed)
                                        .option(o_coreBounceHeight)
                                        .option(o_coreBounceSpeed)
                                        .option(o_coreTickDelay)
                                        .build())
                                .group(MutableOptionGroupImpl.createBuilder()
                                        .name(Text.of("Rendering"))
                                        .option(o_coreScale)
                                        .option(o_coreLightLevel)
                                        .option(o_coreRenderLayer)
                                        .build())
//                                .group(OptionGroup.createBuilder()
//                                        .name(Text.of("Rainbow"))
//                                        .option(o_coreRainbowSpeed)
//                                        .option(o_coreRainbowDelay)
//                                        .option(o_coreRainbowSaturation)
//                                        .option(o_coreRainbowBrightness)
//                                        .build())
                                .group(MutableOptionGroupImpl.createBuilder()
                                        .name(Text.of("Color"))
                                        .option(o_coreColor)
                                        .option(o_coreAlpha)
                                        .option(o_coreRainbow)
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
                                        .option(ButtonOption.createBuilder()
                                                .name(Text.of("Reset Preview"))
                                                .description(OptionDescription.createBuilder().text(Text.of("Reset the crystal age to preview the animation settings again.")).build())
                                                .action((yaclScreen, buttonOption) -> CrystalChams.entity.age = 0)
                                                .build())
                                        .build())
                                .build())
                        .category(ConfigCategory.createBuilder()
                                .name(Text.of("Frame 1"))
                                .option(o_renderFrame1)
                                .group(OptionGroup.createBuilder()
                                        .name(Text.of("Movement"))
                                        .option(o_frame1Offset)
                                        .option(o_frame1RotationSpeed)
                                        .option(o_frame1BounceHeight)
                                        .option(o_frame1BounceSpeed)
                                        .option(o_frame1TickDelay)
                                        .build())
                                .group(MutableOptionGroupImpl.createBuilder()
                                        .name(Text.of("Rendering"))
                                        .option(o_frame1Scale)
                                        .option(o_frame1Color)
                                        .option(o_frame1Alpha)
                                        .option(o_frame1LightLevel)
                                        .option(o_frame1RenderLayer)
                                        .build())
                                .group(OptionGroup.createBuilder()
                                        .name(Text.of("Rainbow"))
                                        .option(o_frame1Rainbow)
                                        .option(o_frame1RainbowSpeed)
                                        .option(o_frame1RainbowDelay)
                                        .option(o_frame1RainbowSaturation)
                                        .option(o_frame1RainbowBrightness)
                                        .build())
                                .build())
                        .category(ConfigCategory.createBuilder()
                                .name(Text.of("Frame 2"))
                                .option(o_renderFrame2)
                                .group(OptionGroup.createBuilder()
                                        .name(Text.of("Movement"))
                                        .option(o_frame2Offset)
                                        .option(o_frame2RotationSpeed)
                                        .option(o_frame2BounceHeight)
                                        .option(o_frame2BounceSpeed)
                                        .option(o_frame2TickDelay)
                                        .build())
                                .group(MutableOptionGroupImpl.createBuilder()
                                        .name(Text.of("Rendering"))
                                        .option(o_frame2Scale)
                                        .option(o_frame2Color)
                                        .option(o_frame2Alpha)
                                        .option(o_frame2LightLevel)
                                        .option(o_frame2RenderLayer)
                                        .build())
                                .group(OptionGroup.createBuilder()
                                        .name(Text.of("Rainbow"))
                                        .option(o_frame2Rainbow)
                                        .option(o_frame2RainbowSpeed)
                                        .option(o_frame2RainbowDelay)
                                        .option(o_frame2RainbowSaturation)
                                        .option(o_frame2RainbowBrightness)
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