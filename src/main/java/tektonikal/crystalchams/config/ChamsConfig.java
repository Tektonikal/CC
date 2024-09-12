package tektonikal.crystalchams.config;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.EnumControllerBuilder;
import dev.isxander.yacl3.api.controller.FloatSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import dev.isxander.yacl3.impl.controller.ColorControllerBuilderImpl;
import dev.isxander.yacl3.impl.controller.TickBoxControllerBuilderImpl;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.awt.*;

public class ChamsConfig {
    public static final ConfigClassHandler<ChamsConfig> CONFIG = ConfigClassHandler.createBuilder(ChamsConfig.class)
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve("crystalchams.json"))
                    .build())
            .build();
    @SerialEntry
    public boolean modEnabled = true;
    @SerialEntry
    public float shadowRadius = 0.5F;
    @SerialEntry
    public float shadowAlpha = 1;
    @SerialEntry
    public int lightLevel = -1;
    @SerialEntry
    public RenderMode renderLayer = RenderMode.DEFAULT;

    @SerialEntry
    public boolean renderCore = true;
    @SerialEntry
    public float coreScale = 1.53125f;
    @SerialEntry
    public float coreOffset = 0f;
    @SerialEntry
    public float coreRotationSpeed = 1F;
    @SerialEntry
    public float coreBounceHeight = 0.4f;
    @SerialEntry
    public float coreBounceSpeed = 0.2f;
    @SerialEntry
    public Color coreColor = Color.decode("#ffffff");
    @SerialEntry
    public float coreAlpha = 1;
    @SerialEntry
    public boolean coreChroma = false;
    @SerialEntry
    public int coreChromaDelay = 0;
    @SerialEntry
    public int coreChromaSpeed = 5;

    //TODO: rainbow/chroma

    @SerialEntry
    public boolean renderFrame1 = true;
    @SerialEntry
    public float frame1Scale = 1.75F;
    @SerialEntry
    public float frame1Offset = 0f;
    @SerialEntry
    public float frame1RotationSpeed = 1F;
    @SerialEntry
    public float frame1BounceHeight = 0.4f;
    @SerialEntry
    public float frame1BounceSpeed = 0.2f;
    @SerialEntry
    public Color frame1Color = Color.decode("#ffffff");
    @SerialEntry
    public float frame1Alpha = 1f;
    @SerialEntry
    public boolean frame1Chroma = false;
    @SerialEntry
    public int frame1ChromaDelay = 0;
    @SerialEntry
    public int frame1ChromaSpeed = 5;


    @SerialEntry
    public boolean renderFrame2 = true;
    @SerialEntry
    public float frame2Scale = 2F;
    @SerialEntry
    public float frame2Offset = 0f;
    @SerialEntry
    public float frame2RotationSpeed = 1F;
    @SerialEntry
    public float frame2BounceHeight = 0.4f;
    @SerialEntry
    public float frame2BounceSpeed = 0.2f;
    @SerialEntry
    public Color frame2Color = Color.decode("#ffffff");
    @SerialEntry
    public float frame2Alpha = 1;
    @SerialEntry
    public boolean frame2Chroma = false;
    @SerialEntry
    public int frame2ChromaDelay = 0;
    @SerialEntry
    public int frame2ChromaSpeed = 5;

    public enum RenderMode implements NameableEnum {
        DEFAULT,
        GATEWAY,
        WIREFRAME,
        CULLED;

        @Override
        public Text getDisplayName() {
            return switch (name()) {
                case "DEFAULT" -> Text.of("Default");
                case "GATEWAY" -> Text.of("Gateway");
                case "WIREFRAME" -> Text.of("Wireframe");
                case "CULLED" -> Text.of("Culled");
                default -> Text.of("blegh .");
            };
        }
    }

    public static Screen getConfigScreen(Screen parent) {
        return YetAnotherConfigLib.create(CONFIG, ((defaults, config, builder) -> builder
                        .title(Text.of("Custom End Crystals"))
                        .category(ConfigCategory.createBuilder()
                                .name(Text.of("General"))
                                .group(OptionGroup.createBuilder()
                                        .name(Text.of("General"))
                                        .option(Option.<Boolean>createBuilder()
                                                .name(Text.of("Mod Enabled"))
                                                .controller(TickBoxControllerBuilderImpl::new)
                                                .binding(true, () -> CONFIG.instance().modEnabled, newVal -> CONFIG.instance().modEnabled = newVal)
                                                .build())
                                        .build())
                                .group(OptionGroup.createBuilder()
                                        .name(Text.of("Shadow"))
                                        .option(Option.<Float>createBuilder()
                                                .name(Text.of("Shadow Radius"))
                                                .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0.1f, 2f).step(0.1f).formatValue(val -> Text.of(String.format("%.1f", val))))
                                                .binding(0.5F, () -> CONFIG.instance().shadowRadius, newVal -> CONFIG.instance().shadowRadius = newVal)
                                                .build())
                                        .option(Option.<Float>createBuilder()
                                                .name(Text.of("Shadow Opacity"))
                                                .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 1f).step(0.01f).formatValue(val -> Text.of(String.format("%.2f", val) + "%")))
                                                .binding(1F, () -> CONFIG.instance().shadowAlpha, newVal -> CONFIG.instance().shadowAlpha = newVal)
                                                .build())
                                        .build())
                                .group(OptionGroup.createBuilder()
                                        .name(Text.of("Miscellaneous"))
                                        .option(Option.<RenderMode>createBuilder()
                                                .name(Text.of("Render Mode"))
                                                .controller(renderModeOption -> EnumControllerBuilder.create(renderModeOption).enumClass(RenderMode.class))
                                                .binding(RenderMode.DEFAULT, () -> CONFIG.instance().renderLayer, newVal -> CONFIG.instance().renderLayer = newVal)
                                                .build())
                                        .option(Option.<Integer>createBuilder()
                                                .name(Text.of("Light Level"))
                                                .controller(integerOption -> IntegerSliderControllerBuilder.create(integerOption).step(1).range(-1, 255))
                                                .binding(-1, () -> CONFIG.instance().lightLevel, newVal -> CONFIG.instance().lightLevel = newVal)
                                                .build())
                                        .build())
                                .build())
                        .category(ConfigCategory.createBuilder()
                                .name(Text.of("Core"))
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.of("Render Core"))
                                        .binding(true, () -> CONFIG.instance().renderCore, newVal -> CONFIG.instance().renderCore = newVal)
                                        .controller(TickBoxControllerBuilderImpl::new)
                                        .build())
                                .option(Option.<Float>createBuilder()
                                        .name(Text.of("Scale"))
                                        .binding(1.53125F, () -> CONFIG.instance().coreScale, newVal -> CONFIG.instance().coreScale = newVal)
                                        .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 10f).step(0.01f).formatValue(val -> Text.of(String.format("%.2f", val))))
                                        .build())
                                .option(Option.<Float>createBuilder()
                                        .name(Text.of("Vertical Offset"))
                                        .binding(0F, () -> CONFIG.instance().coreOffset, newVal -> CONFIG.instance().coreOffset = newVal)
                                        .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(-2.5f, 2.5f).step(0.1f).formatValue(val -> Text.of(String.format("%.1f", val))))
                                        .build())
                                .option(Option.<Float>createBuilder()
                                        .name(Text.of("Rotation Speed"))
                                        .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 25f).step(0.1f).formatValue(val -> Text.of(String.format("%.1f", val))))
                                        .binding(1F, () -> CONFIG.instance().coreRotationSpeed, newVal -> CONFIG.instance().coreRotationSpeed = newVal)
                                        .build())
                                .option(Option.<Float>createBuilder()
                                        .name(Text.of("Bounce Height"))
                                        .binding(0.4F, () -> CONFIG.instance().coreBounceHeight, newVal -> CONFIG.instance().coreBounceHeight = newVal)
                                        .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 2f).step(0.05f).formatValue(val -> Text.of(String.format("%.2f", val))))
                                        .build())
                                .option(Option.<Float>createBuilder()
                                        .name(Text.of("Bounce Speed"))
                                        .binding(0.2F, () -> CONFIG.instance().coreBounceSpeed, newVal -> CONFIG.instance().coreBounceSpeed = newVal)
                                        .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 2f).step(0.05f).formatValue(val -> Text.of(String.format("%.2f", val))))
                                        .build())
                                .option(Option.<Color>createBuilder()
                                        .name(Text.of("Color"))
                                        .controller(ColorControllerBuilderImpl::new)
                                        .binding(new Color(255, 255, 255), () -> CONFIG.instance().coreColor, newVal -> CONFIG.instance().coreColor = newVal)
                                        .build())
                                .option(Option.<Float>createBuilder()
                                        .name(Text.of("Opacity"))
                                        .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 1f).step(0.01f).formatValue(val -> Text.of(String.format("%.0f", val * 100) + "%")))
                                        .binding(1F, () -> CONFIG.instance().coreAlpha, newVal -> CONFIG.instance().coreAlpha = newVal)
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.of("Chroma"))
                                        .binding(false, () -> CONFIG.instance().coreChroma, newVal -> CONFIG.instance().coreChroma = newVal)
                                        .controller(TickBoxControllerBuilderImpl::new)
                                        .build())
                                .option(Option.<Integer>createBuilder()
                                        .name(Text.of("Chroma Delay"))
                                        .controller(integerOption -> IntegerSliderControllerBuilder.create(integerOption).step(1).range(-500, 500))
                                        .binding(0, () -> CONFIG.instance().coreChromaDelay, newVal -> CONFIG.instance().coreChromaDelay = newVal)
                                        .build())
                                .option(Option.<Integer>createBuilder()
                                        .name(Text.of("Chroma Speed"))
                                        .controller(integerOption -> IntegerSliderControllerBuilder.create(integerOption).step(1).range(1,10))
                                        .binding(2, () -> CONFIG.instance().coreChromaSpeed, newVal -> CONFIG.instance().coreChromaSpeed = newVal)
                                        .build())
                                .build())
                        .category(ConfigCategory.createBuilder()
                                .name(Text.of("Frame 1"))
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.of("Render Frame 1"))
                                        .binding(true, () -> CONFIG.instance().renderFrame1, newVal -> CONFIG.instance().renderFrame1 = newVal)
                                        .controller(TickBoxControllerBuilderImpl::new)
                                        .build())
                                .option(Option.<Float>createBuilder()
                                        .name(Text.of("Scale"))
                                        .binding(2F, () -> CONFIG.instance().frame1Scale, newVal -> CONFIG.instance().frame1Scale = newVal)
                                        .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 10f).step(0.01f).formatValue(val -> Text.of(String.format("%.2f", val))))
                                        .build())
                                .option(Option.<Float>createBuilder()
                                        .name(Text.of("Vertical Offset"))
                                        .binding(0F, () -> CONFIG.instance().frame1Offset, newVal -> CONFIG.instance().frame1Offset = newVal)
                                        .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(-2.5f, 2.5f).step(0.1f).formatValue(val -> Text.of(String.format("%.1f", val))))
                                        .build())
                                .option(Option.<Float>createBuilder()
                                        .name(Text.of("Rotation Speed"))
                                        .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 25f).step(0.1f).formatValue(val -> Text.of(String.format("%.1f", val))))
                                        .binding(1F, () -> CONFIG.instance().frame1RotationSpeed, newVal -> CONFIG.instance().frame1RotationSpeed = newVal)
                                        .build())
                                .option(Option.<Float>createBuilder()
                                        .name(Text.of("Bounce Height"))
                                        .binding(0.4F, () -> CONFIG.instance().frame1BounceHeight, newVal -> CONFIG.instance().frame1BounceHeight = newVal)
                                        .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 2f).step(0.05f).formatValue(val -> Text.of(String.format("%.2f", val))))
                                        .build())
                                .option(Option.<Float>createBuilder()
                                        .name(Text.of("Bounce Speed"))
                                        .binding(0.2F, () -> CONFIG.instance().frame1BounceSpeed, newVal -> CONFIG.instance().frame1BounceSpeed = newVal)
                                        .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 2f).step(0.05f).formatValue(val -> Text.of(String.format("%.2f", val))))
                                        .build())
                                .option(Option.<Color>createBuilder()
                                        .name(Text.of("Color"))
                                        .controller(ColorControllerBuilderImpl::new)
                                        .binding(new Color(255, 255, 255), () -> CONFIG.instance().frame1Color, newVal -> CONFIG.instance().frame1Color = newVal)
                                        .build())
                                .option(Option.<Float>createBuilder()
                                        .name(Text.of("Opacity"))
                                        .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 1f).step(0.01f).formatValue(val -> Text.of(String.format("%.0f", val * 100) + "%")))
                                        .binding(1F, () -> CONFIG.instance().frame1Alpha, newVal -> CONFIG.instance().frame1Alpha = newVal)
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.of("Chroma"))
                                        .binding(false, () -> CONFIG.instance().frame1Chroma, newVal -> CONFIG.instance().frame1Chroma = newVal)
                                        .controller(TickBoxControllerBuilderImpl::new)
                                        .build())
                                .option(Option.<Integer>createBuilder()
                                        .name(Text.of("Chroma Delay"))
                                        .controller(integerOption -> IntegerSliderControllerBuilder.create(integerOption).step(1).range(-500, 500))
                                        .binding(0, () -> CONFIG.instance().frame1ChromaDelay, newVal -> CONFIG.instance().frame1ChromaDelay = newVal)
                                        .build())
                                .option(Option.<Integer>createBuilder()
                                        .name(Text.of("Chroma Speed"))
                                        .controller(integerOption -> IntegerSliderControllerBuilder.create(integerOption).step(1).range(1,10))
                                        .binding(2, () -> CONFIG.instance().frame1ChromaSpeed, newVal -> CONFIG.instance().frame1ChromaSpeed = newVal)
                                        .build())
                                .build())
                        .category(ConfigCategory.createBuilder()
                                .name(Text.of("Frame 2"))
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.of("Render Frame 2"))
                                        .binding(true, () -> CONFIG.instance().renderCore, newVal -> CONFIG.instance().renderCore = newVal)
                                        .controller(TickBoxControllerBuilderImpl::new)
                                        .build())
                                .option(Option.<Float>createBuilder()
                                        .name(Text.of("Scale"))
                                        .binding(1.75F, () -> CONFIG.instance().frame2Scale, newVal -> CONFIG.instance().frame2Scale = newVal)
                                        .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 10f).step(0.01f).formatValue(val -> Text.of(String.format("%.2f", val))))
                                        .build())
                                .option(Option.<Float>createBuilder()
                                        .name(Text.of("Vertical Offset"))
                                        .binding(0F, () -> CONFIG.instance().frame2Offset, newVal -> CONFIG.instance().frame2Offset = newVal)
                                        .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(-2.5f, 2.5f).step(0.1f).formatValue(val -> Text.of(String.format("%.1f", val))))
                                        .build())
                                .option(Option.<Float>createBuilder()
                                        .name(Text.of("Rotation Speed"))
                                        .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 25f).step(0.1f).formatValue(val -> Text.of(String.format("%.1f", val))))
                                        .binding(1F, () -> CONFIG.instance().frame2RotationSpeed, newVal -> CONFIG.instance().frame2RotationSpeed = newVal)
                                        .build())
                                .option(Option.<Float>createBuilder()
                                        .name(Text.of("Bounce Height"))
                                        .binding(0.4F, () -> CONFIG.instance().frame2BounceHeight, newVal -> CONFIG.instance().frame2BounceHeight = newVal)
                                        .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 2f).step(0.05f).formatValue(val -> Text.of(String.format("%.2f", val))))
                                        .build())
                                .option(Option.<Float>createBuilder()
                                        .name(Text.of("Bounce Speed"))
                                        .binding(0.2F, () -> CONFIG.instance().frame2BounceSpeed, newVal -> CONFIG.instance().frame2BounceSpeed = newVal)
                                        .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 2f).step(0.05f).formatValue(val -> Text.of(String.format("%.2f", val))))
                                        .build())
                                .option(Option.<Color>createBuilder()
                                        .name(Text.of("Color"))
                                        .controller(ColorControllerBuilderImpl::new)
                                        .binding(new Color(255, 255, 255), () -> CONFIG.instance().frame2Color, newVal -> CONFIG.instance().frame2Color = newVal)
                                        .build())
                                .option(Option.<Float>createBuilder()
                                        .name(Text.of("Opacity"))
                                        .controller(floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 1f).step(0.01f).formatValue(val -> Text.of(String.format("%.0f", val * 100) + "%")))
                                        .binding(1F, () -> CONFIG.instance().frame2Alpha, newVal -> CONFIG.instance().frame2Alpha = newVal)
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.of("Chroma"))
                                        .binding(false, () -> CONFIG.instance().frame2Chroma, newVal -> CONFIG.instance().frame2Chroma = newVal)
                                        .controller(TickBoxControllerBuilderImpl::new)
                                        .build())
                                .option(Option.<Integer>createBuilder()
                                        .name(Text.of("Chroma Delay"))
                                        .controller(integerOption -> IntegerSliderControllerBuilder.create(integerOption).step(1).range(-500, 500))
                                        .binding(0, () -> CONFIG.instance().frame2ChromaDelay, newVal -> CONFIG.instance().frame2ChromaDelay = newVal)
                                        .build())
                                .option(Option.<Integer>createBuilder()
                                        .name(Text.of("Chroma Speed"))
                                        .controller(integerOption -> IntegerSliderControllerBuilder.create(integerOption).step(1).range(1,10))
                                        .binding(2, () -> CONFIG.instance().frame2ChromaSpeed, newVal -> CONFIG.instance().frame2ChromaSpeed = newVal)
                                        .build())
                                .build())))
                .generateScreen(parent);
    }
}
