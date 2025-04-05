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
        //Shadow
        @SerialEntry public float shadowRadius = 0.5F;
        @SerialEntry public float shadowAlpha = 1;
        //Miscellaneous
        @SerialEntry public int lightLevel = -1;
        @SerialEntry public RenderMode renderLayer = RenderMode.DEFAULT;
        //Config
            //Empty
        //Core
        @SerialEntry  public boolean renderCore = true;
            //Core Movement
            @SerialEntry public float coreOffset = 0f;
            @SerialEntry public float coreRotationSpeed = 1F;
            @SerialEntry public float coreBounceHeight = 0.4f;
            @SerialEntry public float coreBounceSpeed = 0.2f;
            //Core Rendering
            @SerialEntry public float coreScale = 1.53125f;
            @SerialEntry public Color coreColor = Color.decode("#ffffff");
            @SerialEntry public float coreAlpha = 1;
            //Core Rainbow
            @SerialEntry  public boolean coreRainbow = false;
                @SerialEntry public int coreRainbowSpeed = 2;
                @SerialEntry public int coreRainbowDelay = 0;
        //Frame 1
        @SerialEntry  public boolean renderFrame1 = true;
            //Frame 1 Movement
            @SerialEntry public float frame1Offset = 0f;
            @SerialEntry public float frame1RotationSpeed = 1F;
            @SerialEntry public float frame1BounceHeight = 0.4f;
            @SerialEntry public float frame1BounceSpeed = 0.2f;
            //Frame 1 Rendering
            @SerialEntry public float frame1Scale = 2F;
            @SerialEntry public Color frame1Color = Color.decode("#ffffff");
            @SerialEntry public float frame1Alpha = 1f;
            //Frame 1 Rainbow
            @SerialEntry  public boolean frame1Rainbow = false;
                @SerialEntry public int frame1RainbowSpeed = 2;
                @SerialEntry public int frame1RainbowDelay = 0;
       //Frame 2
        @SerialEntry  public boolean renderFrame2 = true;
            //Frame 2 Movement
            @SerialEntry public float frame2Offset = 0f;
            @SerialEntry public float frame2RotationSpeed = 1F;
            @SerialEntry public float frame2BounceHeight = 0.4f;
            @SerialEntry public float frame2BounceSpeed = 0.2f;
            //Frame 2 Rendering
            @SerialEntry public float frame2Scale = 1.75F;
            @SerialEntry public Color frame2Color = Color.decode("#ffffff");
            @SerialEntry public float frame2Alpha = 1;
            //Frame 2 Rainbow
            @SerialEntry  public boolean frame2Rainbow = false;
                @SerialEntry public int frame2RainbowSpeed = 2;
                @SerialEntry public int frame2RainbowDelay = 0;
        //Beam
        @SerialEntry public boolean renderBeam = true;
            //Rendering
            @SerialEntry public Color beam1Color = Color.decode("#ffffff");
            @SerialEntry public float beam1Alpha = 1;
            @SerialEntry public float beam1Radius = 0.75F;
            @SerialEntry public Color beam2Color = Color.decode("#000000");
            @SerialEntry public float beam2Alpha = 1;
            @SerialEntry public float beam2Radius = 0.2F;
            //Rainbow 1
            @SerialEntry public boolean beam1Rainbow = false;
                @SerialEntry public int beam1RainbowSpeed = 2;
                @SerialEntry public int beam1RainbowDelay = 0;
            //Rainbow 2
            @SerialEntry public boolean beam2Rainbow = false;
                @SerialEntry public int beam2RainbowSpeed = 2;
                @SerialEntry public int beam2RainbowDelay = 0;
    //@formatter:on
    @Updatable
    public static Option<Boolean> o_modEnabled = Option.<Boolean>createBuilder()
            .name(Text.of("Mod Enabled"))
            .controller(TickBoxControllerBuilderImpl::new)
            .binding(true, () -> CONFIG.instance().modEnabled, newVal -> CONFIG.instance().modEnabled = newVal)
            .instant(true)
            .build();
    public static Option<Float> o_shadowRadius = Option.<Float>createBuilder()
            .name(Text.of("Shadow Radius"))
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0.1f, 2f).step(0.1f).formatValue(val -> Text.of(String.format("%.1f", val))))
            .binding(0.5F, () -> CONFIG.instance().shadowRadius, newVal -> CONFIG.instance().shadowRadius = newVal)
            .instant(true)
            .build();
    public static Option<Float> o_shadowAlpha = Option.<Float>createBuilder()
            .name(Text.of("Shadow Opacity"))
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 1f).step(0.01f).formatValue(val -> Text.of(String.format("%.2f", val) + "%")))
            .binding(1F, () -> CONFIG.instance().shadowAlpha, newVal -> CONFIG.instance().shadowAlpha = newVal)
            .instant(true)
            .build();
    public static Option<RenderMode> o_renderLayer = Option.<RenderMode>createBuilder()
            .name(Text.of("Render Mode"))
            .controller(renderModeOption -> EnumControllerBuilder.create(renderModeOption).enumClass(RenderMode.class))
            .binding(RenderMode.DEFAULT, () -> CONFIG.instance().renderLayer, newVal -> CONFIG.instance().renderLayer = newVal)
            .instant(true)
            .build();
    public static Option<Integer> o_lightLevel = Option.<Integer>createBuilder()
            .name(Text.of("Light Level"))
            .controller(integerOption -> IntegerSliderControllerBuilder.create(integerOption).step(1).range(-1, 255))
            .binding(-1, () -> CONFIG.instance().lightLevel, newVal -> CONFIG.instance().lightLevel = newVal)
            .instant(true)
            .build();
    @Updatable
    public static Option<Boolean> o_renderCore = Option.<Boolean>createBuilder()
            .name(Text.of("Render Core"))
            .binding(true, () -> CONFIG.instance().renderCore, newVal -> CONFIG.instance().renderCore = newVal)
            .controller(TickBoxControllerBuilderImpl::new)
            .instant(true)
            .build();
    public static Option<Float> o_coreOffset = Option.<Float>createBuilder()
            .name(Text.of("Vertical Offset"))
            .binding(0F, () -> CONFIG.instance().coreOffset, newVal -> CONFIG.instance().coreOffset = newVal)
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(-2.5f, 2.5f).step(0.1f).formatValue(val -> Text.of(String.format("%.1f", val))))
            .instant(true)
            .build();
    public static Option<Float> o_coreRotationSpeed = Option.<Float>createBuilder()
            .name(Text.of("Rotation Speed"))
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 25f).step(0.1f).formatValue(val -> Text.of(String.format("%.1f", val))))
            .binding(1F, () -> CONFIG.instance().coreRotationSpeed, newVal -> CONFIG.instance().coreRotationSpeed = newVal)
            .instant(true)
            .build();
    public static Option<Float> o_coreBounceHeight = Option.<Float>createBuilder()
            .name(Text.of("Bounce Height"))
            .binding(0.4F, () -> CONFIG.instance().coreBounceHeight, newVal -> CONFIG.instance().coreBounceHeight = newVal)
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 2f).step(0.05f).formatValue(val -> Text.of(String.format("%.2f", val))))
            .instant(true)
            .build();
    public static Option<Float> o_coreBounceSpeed = Option.<Float>createBuilder()
            .name(Text.of("Bounce Speed"))
            .binding(0.2F, () -> CONFIG.instance().coreBounceSpeed, newVal -> CONFIG.instance().coreBounceSpeed = newVal)
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 2f).step(0.05f).formatValue(val -> Text.of(String.format("%.2f", val))))
            .instant(true)
            .build();
    public static Option<Float> o_coreScale = Option.<Float>createBuilder()
            .name(Text.of("Scale"))
            .binding(1.53125F, () -> CONFIG.instance().coreScale, newVal -> CONFIG.instance().coreScale = newVal)
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 10f).step(0.01f).formatValue(val -> Text.of(String.format("%.2f", val))))
            .instant(true)
            .build();
    public static Option<Color> o_coreColor = Option.<Color>createBuilder()
            .name(Text.of("Color"))
            .controller(ColorControllerBuilderImpl::new)
            .binding(new Color(255, 255, 255), () -> CONFIG.instance().coreColor, newVal -> CONFIG.instance().coreColor = newVal)
            .instant(true)
            .build();
    public static Option<Float> o_coreAlpha = Option.<Float>createBuilder()
            .name(Text.of("Opacity"))
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 1f).step(0.01f).formatValue(val -> Text.of(String.format("%.0f", val * 100) + "%")))
            .binding(1F, () -> CONFIG.instance().coreAlpha, newVal -> CONFIG.instance().coreAlpha = newVal)
            .instant(true)
            .build();
    @Updatable
    public static Option<Boolean> o_coreRainbow = Option.<Boolean>createBuilder()
            .name(Text.of("Rainbow"))
            .binding(false, () -> CONFIG.instance().coreRainbow, newVal -> CONFIG.instance().coreRainbow = newVal)
            .controller(TickBoxControllerBuilderImpl::new)
            .instant(true)
            .build();
    public static Option<Integer> o_coreRainbowSpeed = Option.<Integer>createBuilder()
            .name(Text.of("Rainbow Speed"))
            .controller(integerOption -> IntegerSliderControllerBuilder.create(integerOption).step(1).range(1, 10))
            .binding(2, () -> CONFIG.instance().coreRainbowSpeed, newVal -> CONFIG.instance().coreRainbowSpeed = newVal)
            .instant(true)
            .build();
    public static Option<Integer> o_coreRainbowDelay = Option.<Integer>createBuilder()
            .name(Text.of("Rainbow Delay"))
            .controller(integerOption -> IntegerSliderControllerBuilder.create(integerOption).step(1).range(-500, 500))
            .binding(0, () -> CONFIG.instance().coreRainbowDelay, newVal -> CONFIG.instance().coreRainbowDelay = newVal)
            .instant(true)
            .build();
    @Updatable
    public static Option<Boolean> o_renderFrame1 = Option.<Boolean>createBuilder()
            .name(Text.of("Render Frame 1"))
            .binding(true, () -> CONFIG.instance().renderFrame1, newVal -> CONFIG.instance().renderFrame1 = newVal)
            .controller(TickBoxControllerBuilderImpl::new)
            .instant(true)
            .build();
    public static Option<Float> o_frame1Offset = Option.<Float>createBuilder()
            .name(Text.of("Vertical Offset"))
            .binding(0F, () -> CONFIG.instance().frame1Offset, newVal -> CONFIG.instance().frame1Offset = newVal)
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(-2.5f, 2.5f).step(0.1f).formatValue(val -> Text.of(String.format("%.1f", val))))
            .instant(true)
            .build();
    public static Option<Float> o_frame1RotationSpeed = Option.<Float>createBuilder()
            .name(Text.of("Rotation Speed"))
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 25f).step(0.1f).formatValue(val -> Text.of(String.format("%.1f", val))))
            .binding(1F, () -> CONFIG.instance().frame1RotationSpeed, newVal -> CONFIG.instance().frame1RotationSpeed = newVal)
            .instant(true)
            .build();
    public static Option<Float> o_frame1BounceHeight = Option.<Float>createBuilder()
            .name(Text.of("Bounce Height"))
            .binding(0.4F, () -> CONFIG.instance().frame1BounceHeight, newVal -> CONFIG.instance().frame1BounceHeight = newVal)
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 2f).step(0.05f).formatValue(val -> Text.of(String.format("%.2f", val))))
            .instant(true)
            .build();
    public static Option<Float> o_frame1BounceSpeed = Option.<Float>createBuilder()
            .name(Text.of("Bounce Speed"))
            .binding(0.2F, () -> CONFIG.instance().frame1BounceSpeed, newVal -> CONFIG.instance().frame1BounceSpeed = newVal)
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 2f).step(0.05f).formatValue(val -> Text.of(String.format("%.2f", val))))
            .instant(true)
            .build();
    public static Option<Float> o_frame1Scale = Option.<Float>createBuilder()
            .name(Text.of("Scale"))
            .binding(2F, () -> CONFIG.instance().frame1Scale, newVal -> CONFIG.instance().frame1Scale = newVal)
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 10f).step(0.01f).formatValue(val -> Text.of(String.format("%.2f", val))))
            .instant(true)
            .build();
    public static Option<Color> o_frame1Color = Option.<Color>createBuilder()
            .name(Text.of("Color"))
            .controller(ColorControllerBuilderImpl::new)
            .binding(new Color(255, 255, 255), () -> CONFIG.instance().frame1Color, newVal -> CONFIG.instance().frame1Color = newVal)
            .instant(true)
            .build();
    public static Option<Float> o_frame1Alpha = Option.<Float>createBuilder()
            .name(Text.of("Opacity"))
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 1f).step(0.01f).formatValue(val -> Text.of(String.format("%.0f", val * 100) + "%")))
            .binding(1F, () -> CONFIG.instance().frame1Alpha, newVal -> CONFIG.instance().frame1Alpha = newVal)
            .instant(true)
            .build();
    @Updatable
    public static Option<Boolean> o_frame1Rainbow = Option.<Boolean>createBuilder()
            .name(Text.of("Rainbow"))
            .binding(false, () -> CONFIG.instance().frame1Rainbow, newVal -> CONFIG.instance().frame1Rainbow = newVal)
            .controller(TickBoxControllerBuilderImpl::new)
            .instant(true)
            .build();
    public static Option<Integer> o_frame1RainbowSpeed = Option.<Integer>createBuilder()
            .name(Text.of("Rainbow Speed"))
            .controller(integerOption -> IntegerSliderControllerBuilder.create(integerOption).step(1).range(1, 10))
            .binding(2, () -> CONFIG.instance().frame1RainbowSpeed, newVal -> CONFIG.instance().frame1RainbowSpeed = newVal)
            .instant(true)
            .build();
    public static Option<Integer> o_frame1RainbowDelay = Option.<Integer>createBuilder()
            .name(Text.of("Rainbow Delay"))
            .controller(integerOption -> IntegerSliderControllerBuilder.create(integerOption).step(1).range(-500, 500))
            .binding(0, () -> CONFIG.instance().frame1RainbowDelay, newVal -> CONFIG.instance().frame1RainbowDelay = newVal)
            .instant(true)
            .build();
    @Updatable
    public static Option<Boolean> o_renderFrame2 = Option.<Boolean>createBuilder()
            .name(Text.of("Render Frame 2"))
            .binding(true, () -> CONFIG.instance().renderFrame2, newVal -> CONFIG.instance().renderFrame2 = newVal)
            .controller(TickBoxControllerBuilderImpl::new)
            .instant(true)
            .build();
    public static Option<Float> o_frame2Offset = Option.<Float>createBuilder()
            .name(Text.of("Vertical Offset"))
            .binding(0F, () -> CONFIG.instance().frame2Offset, newVal -> CONFIG.instance().frame2Offset = newVal)
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(-2.5f, 2.5f).step(0.1f).formatValue(val -> Text.of(String.format("%.1f", val))))
            .instant(true)
            .build();
    public static Option<Float> o_frame2RotationSpeed = Option.<Float>createBuilder()
            .name(Text.of("Rotation Speed"))
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 25f).step(0.1f).formatValue(val -> Text.of(String.format("%.1f", val))))
            .binding(1F, () -> CONFIG.instance().frame2RotationSpeed, newVal -> CONFIG.instance().frame2RotationSpeed = newVal)
            .instant(true)
            .build();
    public static Option<Float> o_frame2BounceHeight = Option.<Float>createBuilder()
            .name(Text.of("Bounce Height"))
            .binding(0.4F, () -> CONFIG.instance().frame2BounceHeight, newVal -> CONFIG.instance().frame2BounceHeight = newVal)
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 2f).step(0.05f).formatValue(val -> Text.of(String.format("%.2f", val))))
            .instant(true)
            .build();
    public static Option<Float> o_frame2BounceSpeed = Option.<Float>createBuilder()
            .name(Text.of("Bounce Speed"))
            .binding(0.2F, () -> CONFIG.instance().frame2BounceSpeed, newVal -> CONFIG.instance().frame2BounceSpeed = newVal)
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 2f).step(0.05f).formatValue(val -> Text.of(String.format("%.2f", val))))
            .instant(true)
            .build();
    public static Option<Float> o_frame2Scale = Option.<Float>createBuilder()
            .name(Text.of("Scale"))
            .binding(1.75F, () -> CONFIG.instance().frame2Scale, newVal -> CONFIG.instance().frame2Scale = newVal)
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 10f).step(0.01f).formatValue(val -> Text.of(String.format("%.2f", val))))
            .instant(true)
            .build();
    public static Option<Color> o_frame2Color = Option.<Color>createBuilder()
            .name(Text.of("Color"))
            .controller(ColorControllerBuilderImpl::new)
            .binding(new Color(255, 255, 255), () -> CONFIG.instance().frame2Color, newVal -> CONFIG.instance().frame2Color = newVal)
            .instant(true)
            .build();
    public static Option<Float> o_frame2Alpha = Option.<Float>createBuilder()
            .name(Text.of("Opacity"))
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 1f).step(0.01f).formatValue(val -> Text.of(String.format("%.0f", val * 100) + "%")))
            .binding(1F, () -> CONFIG.instance().frame2Alpha, newVal -> CONFIG.instance().frame2Alpha = newVal)
            .instant(true)
            .build();
    @Updatable
    public static Option<Boolean> o_frame2Rainbow = Option.<Boolean>createBuilder()
            .name(Text.of("Rainbow"))
            .binding(false, () -> CONFIG.instance().frame2Rainbow, newVal -> CONFIG.instance().frame2Rainbow = newVal)
            .controller(TickBoxControllerBuilderImpl::new)
            .instant(true)
            .build();
    public static Option<Integer> o_frame2RainbowSpeed = Option.<Integer>createBuilder()
            .name(Text.of("Rainbow Speed"))
            .controller(integerOption -> IntegerSliderControllerBuilder.create(integerOption).step(1).range(1, 10))
            .binding(2, () -> CONFIG.instance().frame2RainbowSpeed, newVal -> CONFIG.instance().frame2RainbowSpeed = newVal)
            .instant(true)
            .build();
    public static Option<Integer> o_frame2RainbowDelay = Option.<Integer>createBuilder()
            .name(Text.of("Rainbow Delay"))
            .controller(integerOption -> IntegerSliderControllerBuilder.create(integerOption).step(1).range(-500, 500))
            .binding(0, () -> CONFIG.instance().frame2RainbowDelay, newVal -> CONFIG.instance().frame2RainbowDelay = newVal)
            .instant(true)
            .build();
    @Updatable
    public static Option<Boolean> o_renderBeam = Option.<Boolean>createBuilder()
            .name(Text.of("Render Beam"))
            .binding(true, () -> CONFIG.instance().renderBeam, newVal -> CONFIG.instance().renderBeam = newVal)
            .controller(TickBoxControllerBuilderImpl::new)
            .instant(true)
            .build();
    public static Option<Color> o_beam1Color = Option.<Color>createBuilder()
            .name(Text.of("Color"))
            .controller(ColorControllerBuilderImpl::new)
            .binding(new Color(255, 255, 255), () -> CONFIG.instance().beam1Color, newVal -> CONFIG.instance().beam1Color = newVal)
            .instant(true)
            .build();
    public static Option<Float> o_beam1Alpha = Option.<Float>createBuilder()
            .name(Text.of("Opacity"))
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 1f).step(0.01f).formatValue(val -> Text.of(String.format("%.0f", val * 100) + "%")))
            .binding(1F, () -> CONFIG.instance().beam1Alpha, newVal -> CONFIG.instance().beam1Alpha = newVal)
            .instant(true)
            .build();
    @Updatable
    public static Option<Boolean> o_beam1Rainbow = Option.<Boolean>createBuilder()
            .name(Text.of("Rainbow"))
            .binding(false, () -> CONFIG.instance().beam1Rainbow, newVal -> CONFIG.instance().beam1Rainbow = newVal)
            .controller(TickBoxControllerBuilderImpl::new)
            .instant(true)
            .build();
    public static Option<Integer> o_beam1RainbowSpeed = Option.<Integer>createBuilder()
            .name(Text.of("Rainbow Speed"))
            .controller(integerOption -> IntegerSliderControllerBuilder.create(integerOption).step(1).range(1, 10))
            .binding(2, () -> CONFIG.instance().beam1RainbowSpeed, newVal -> CONFIG.instance().beam1RainbowSpeed = newVal)
            .instant(true)
            .build();
    public static Option<Integer> o_beam1RainbowDelay = Option.<Integer>createBuilder()
            .name(Text.of("Rainbow Delay"))
            .controller(integerOption -> IntegerSliderControllerBuilder.create(integerOption).step(1).range(-500, 500))
            .binding(0, () -> CONFIG.instance().beam1RainbowDelay, newVal -> CONFIG.instance().beam1RainbowDelay = newVal)
            .instant(true)
            .build();
    public static Option<Color> o_beam2Color = Option.<Color>createBuilder()
            .name(Text.of("Color"))
            .controller(ColorControllerBuilderImpl::new)
            .binding(new Color(0,0,0), () -> CONFIG.instance().beam2Color, newVal -> CONFIG.instance().beam2Color = newVal)
            .instant(true)
            .build();
    public static Option<Float> o_beam2Alpha = Option.<Float>createBuilder()
            .name(Text.of("Opacity"))
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 1f).step(0.01f).formatValue(val -> Text.of(String.format("%.0f", val * 100) + "%")))
            .binding(1F, () -> CONFIG.instance().beam2Alpha, newVal -> CONFIG.instance().beam2Alpha = newVal)
            .instant(true)
            .build();
    @Updatable
    public static Option<Boolean> o_beam2Rainbow = Option.<Boolean>createBuilder()
            .name(Text.of("Rainbow"))
            .binding(false, () -> CONFIG.instance().beam2Rainbow, newVal -> CONFIG.instance().beam2Rainbow = newVal)
            .controller(TickBoxControllerBuilderImpl::new)
            .instant(true)
            .build();
    public static Option<Integer> o_beam2RainbowSpeed = Option.<Integer>createBuilder()
            .name(Text.of("Rainbow Speed"))
            .controller(integerOption -> IntegerSliderControllerBuilder.create(integerOption).step(1).range(1, 10))
            .binding(2, () -> CONFIG.instance().beam2RainbowSpeed, newVal -> CONFIG.instance().beam2RainbowSpeed = newVal)
            .instant(true)
            .build();
    public static Option<Integer> o_beam2RainbowDelay = Option.<Integer>createBuilder()
            .name(Text.of("Rainbow Delay"))
            .controller(integerOption -> IntegerSliderControllerBuilder.create(integerOption).step(1).range(-500, 500))
            .binding(0, () -> CONFIG.instance().beam2RainbowDelay, newVal -> CONFIG.instance().beam2RainbowDelay = newVal)
            .instant(true)
            .build();
    public static Option<Float> o_beam1Radius = Option.<Float>createBuilder()
            .name(Text.of("Radius"))
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 1f).step(0.01f).formatValue(val -> Text.of(String.format("%.2f", val))))
            .binding(0.75F, () -> CONFIG.instance().beam1Radius, newVal -> CONFIG.instance().beam1Radius = newVal)
            .instant(true)
            .build();
    public static Option<Float> o_beam2Radius = Option.<Float>createBuilder()
            .name(Text.of("Radius"))
            .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 1f).step(0.01f).formatValue(val -> Text.of(String.format("%.2f", val))))
            .binding(0.2F, () -> CONFIG.instance().beam2Radius, newVal -> CONFIG.instance().beam2Radius = newVal)
            .instant(true)
            .build();

    public static Screen getConfigScreen(Screen parent) {
        return YetAnotherConfigLib.create(CONFIG, ((defaults, config, builder) -> builder
                        .title(Text.of("Custom End Crystals"))
                        .category(ConfigCategory.createBuilder()
                                .name(Text.of("General"))
                                .group(OptionGroup.createBuilder()
                                        .name(Text.of("General"))
                                        .option(o_modEnabled)
                                        .build())
                                .group(OptionGroup.createBuilder()
                                        .name(Text.of("Shadow"))
                                        .option(o_shadowRadius)
                                        .option(o_shadowAlpha)
                                        .build())
                                .group(OptionGroup.createBuilder()
                                        .name(Text.of("Config"))
                                        .option(ButtonOption.createBuilder()
                                                .name(Text.of("Copy Current Config"))
                                                .description(OptionDescription.of(Text.of("Copies the current configuration as text to your clipboard. Go share your configs with your buddies! (Make sure to save the config first)")))
                                                .action((yaclScreen, buttonOption) -> MinecraftClient.getInstance().keyboard.setClipboard(gson.toJson(CONFIG.instance())))
                                                .build())
                                        .option(ButtonOption.createBuilder()
                                                .name(Text.literal("Load Config From Clipboard").formatted(Formatting.DARK_RED, Formatting.BOLD))
                                                .description(OptionDescription.of(Text.of("Loads a configuration from your clipboard if it's valid. WARNING: LOADING A VALID CONFIGURATION WILL OVERWRITE YOUR CURRENT ONE.")))
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
                                        .build())
                                .group(OptionGroup.createBuilder()
                                        .name(Text.of("Miscellaneous"))
                                        .option(o_renderLayer)
                                        .option(o_lightLevel)
                                        .build())
                                .build())
                        .category(ConfigCategory.createBuilder()
                                .name(Text.of("Core"))
                                .option(o_renderCore)
                                .group(OptionGroup.createBuilder()
                                        .name(Text.of("Movement"))
                                        .option(o_coreOffset)
                                        .option(o_coreRotationSpeed)
                                        .option(o_coreBounceHeight)
                                        .option(o_coreBounceSpeed)
                                        .build())
                                .group(OptionGroup.createBuilder()
                                        .name(Text.of("Rendering"))
                                        .option(o_coreScale)
                                        .option(o_coreColor)
                                        .option(o_coreAlpha)
                                        .build())
                                .group(OptionGroup.createBuilder()
                                        .name(Text.of("Rainbow"))
                                        .option(o_coreRainbow)
                                        .option(o_coreRainbowSpeed)
                                        .option(o_coreRainbowDelay)
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
                                        .build())
                                .group(OptionGroup.createBuilder()
                                        .name(Text.of("Rendering"))
                                        .option(o_frame1Scale)
                                        .option(o_frame1Color)
                                        .option(o_frame1Alpha)
                                        .build())
                                .group(OptionGroup.createBuilder()
                                        .name(Text.of("Rainbow"))
                                        .option(o_frame1Rainbow)
                                        .option(o_frame1RainbowSpeed)
                                        .option(o_frame1RainbowDelay)
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
                                        .build())
                                .group(OptionGroup.createBuilder()
                                        .name(Text.of("Rendering"))
                                        .option(o_frame2Scale)
                                        .option(o_frame2Color)
                                        .option(o_frame2Alpha)
                                        .build())
                                .group(OptionGroup.createBuilder()
                                        .name(Text.of("Rainbow"))
                                        .option(o_frame2Rainbow)
                                        .option(o_frame2RainbowSpeed)
                                        .option(o_frame2RainbowDelay)
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
                                        .option(o_beam1Rainbow)
                                        .option(o_beam1RainbowSpeed)
                                        .option(o_beam1RainbowDelay)
                                        .build())
                                .group(OptionGroup.createBuilder()
                                        .name(Text.of("End"))
                                        .option(o_beam2Color)
                                        .option(o_beam2Alpha)
                                        .option(o_beam2Radius)
                                        .option(o_beam2Rainbow)
                                        .option(o_beam2RainbowSpeed)
                                        .option(o_beam2RainbowDelay)
                                        .build())
                                .build())
                ))
                .generateScreen(parent);
    }

    public static void update(Option<Boolean> booleanOption, Boolean aBoolean) {
        if (booleanOption.equals(o_modEnabled)) {
            o_shadowAlpha.setAvailable(aBoolean);
            o_shadowRadius.setAvailable(aBoolean);
            o_renderLayer.setAvailable(aBoolean);
            o_lightLevel.setAvailable(aBoolean);

            o_renderBeam.setAvailable(aBoolean);
            o_renderFrame1.setAvailable(aBoolean);
            o_renderFrame2.setAvailable(aBoolean);
            o_renderBeam.setAvailable(aBoolean);
        } else if (booleanOption.equals(o_renderCore)) {
            o_coreOffset.setAvailable(o_renderCore.available() && aBoolean);
            o_coreRotationSpeed.setAvailable(o_renderCore.available() && aBoolean);
            o_coreBounceHeight.setAvailable(o_renderCore.available() && aBoolean);
            o_coreBounceSpeed.setAvailable(o_renderCore.available() && aBoolean);
            o_coreScale.setAvailable(o_renderCore.available() && aBoolean);
            o_coreColor.setAvailable(o_renderCore.available() && aBoolean);
            o_coreAlpha.setAvailable(o_renderCore.available() && aBoolean);
            o_coreRainbow.setAvailable(o_renderCore.available() && aBoolean);
        } else if (booleanOption.equals(o_coreRainbow)) {
            o_coreRainbowSpeed.setAvailable(o_coreRainbow.available() && aBoolean);
            o_coreRainbowDelay.setAvailable(o_coreRainbow.available() && aBoolean);
        } else if (booleanOption.equals(o_renderFrame1)) {
            o_frame1Offset.setAvailable(o_renderFrame1.available() && aBoolean);
            o_frame1RotationSpeed.setAvailable(o_renderFrame1.available() && aBoolean);
            o_frame1BounceHeight.setAvailable(o_renderFrame1.available() && aBoolean);
            o_frame1BounceSpeed.setAvailable(o_renderFrame1.available() && aBoolean);
            o_frame1Scale.setAvailable(o_renderFrame1.available() && aBoolean);
            o_frame1Color.setAvailable(o_renderFrame1.available() && aBoolean);
            o_frame1Alpha.setAvailable(o_renderFrame1.available() && aBoolean);
            o_frame1Rainbow.setAvailable(o_renderFrame1.available() && aBoolean);
        } else if (booleanOption.equals(o_frame1Rainbow)) {
            o_frame1RainbowSpeed.setAvailable(o_frame1Rainbow.available() && aBoolean);
            o_frame1RainbowDelay.setAvailable(o_frame1Rainbow.available() && aBoolean);
        } else if (booleanOption.equals(o_renderFrame2)) {
            o_frame2Offset.setAvailable(o_renderFrame2.available() && aBoolean);
            o_frame2RotationSpeed.setAvailable(o_renderFrame2.available() && aBoolean);
            o_frame2BounceHeight.setAvailable(o_renderFrame2.available() && aBoolean);
            o_frame2BounceSpeed.setAvailable(o_renderFrame2.available() && aBoolean);
            o_frame2Scale.setAvailable(o_renderFrame2.available() && aBoolean);
            o_frame2Color.setAvailable(o_renderFrame2.available() && aBoolean);
            o_frame2Alpha.setAvailable(o_renderFrame2.available() && aBoolean);
            o_frame2Rainbow.setAvailable(o_renderFrame2.available() && aBoolean);
        } else if (booleanOption.equals(o_frame2Rainbow)) {
            o_frame2RainbowSpeed.setAvailable(o_frame2Rainbow.available() && aBoolean);
            o_frame2RainbowDelay.setAvailable(o_frame2Rainbow.available() && aBoolean);
        } else if (booleanOption.equals(o_renderBeam)) {
            o_beam1Color.setAvailable(o_renderBeam.available() && aBoolean);
            o_beam1Alpha.setAvailable(o_renderBeam.available() && aBoolean);
            o_beam1Rainbow.setAvailable(o_renderBeam.available() && aBoolean);
            o_beam1Radius.setAvailable(o_renderBeam.available() && aBoolean);
            o_beam2Color.setAvailable(o_renderBeam.available() && aBoolean);
            o_beam2Alpha.setAvailable(o_renderBeam.available() && aBoolean);
            o_beam2Rainbow.setAvailable(o_renderBeam.available() && aBoolean);
            o_beam2Radius.setAvailable(o_renderBeam.available() && aBoolean);
        } else if (booleanOption.equals(o_beam1Rainbow)) {
            o_beam1RainbowDelay.setAvailable(o_beam1Rainbow.available() && aBoolean);
            o_beam1RainbowSpeed.setAvailable(o_beam1Rainbow.available() && aBoolean);
        } else if (booleanOption.equals(o_beam2Rainbow)) {
            o_beam2RainbowDelay.setAvailable(o_beam2Rainbow.available() && aBoolean);
            o_beam2RainbowSpeed.setAvailable(o_beam2Rainbow.available() && aBoolean);
        }
    }

    public enum RenderMode implements NameableEnum {
        DEFAULT,
        GATEWAY,
        WIREFRAME,
        CULLED;

        @Override
        public Text getDisplayName() {
            return switch (this) {
                case DEFAULT -> Text.of("Default");
                case GATEWAY -> Text.of("Gateway");
                case WIREFRAME -> Text.of("Wireframe");
                case CULLED -> Text.of("Culled");
            };
        }
    }
}
