package tektonikal.crystalchams.config;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import dev.isxander.yacl3.impl.controller.TickBoxControllerBuilderImpl;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class ChamsConfig {
    public static final ConfigClassHandler<ChamsConfig> CONFIG = ConfigClassHandler.createBuilder(ChamsConfig.class)
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve("crystalchams.json"))
                    .build())
            .build();
    @SerialEntry
    public boolean modEnabled = true;
    @SerialEntry
    public RenderMode mode = RenderMode.DEFAULT;
    @SerialEntry
    public float bounce = 0.4f;
    @SerialEntry
    public float BounceSpeed = 0.2f;
    @SerialEntry
    public int lLevel = -1;
    @SerialEntry
    public RenderMode renderMode = RenderMode.DEFAULT;
    @SerialEntry
    public boolean renderCore = true;
    @SerialEntry
    public float coreScale = 1.53125f;
    @SerialEntry
    public float coreOffset = 0f;
    @SerialEntry
    public float coreRotSpeed = 1f;
    @SerialEntry
    public String col = "#ffffff";
    @SerialEntry
    public float alpha = 1;
    @SerialEntry
    public boolean renderFrame1 = true;
    @SerialEntry
    public float frame1Scale = 2F;
    @SerialEntry
    public float frame1Offset = 0f;
    @SerialEntry
    public String frameCol = "#ffffff";
    @SerialEntry
    public float frame1Alpha = 1f;
    @SerialEntry
    public boolean renderFrame2 = true;
    @SerialEntry
    public float frame2Scale = 1.75F;
    @SerialEntry
    public float frame2Offset = 0f;
    @SerialEntry
    public String frameCol2 = "#ffffff";
    @SerialEntry
    public float frame2Alpha = 1;
    @SerialEntry
    public float shadowSize = 0.5F;
    @SerialEntry
    public float shadowOpacity = 1;

    public enum RenderMode implements NameableEnum {
        DEFAULT,
        GATEWAY,
        WIREFRAME,
        CULLED;

        @Override
        public Text getDisplayName() {
            return switch (name()){
                default -> Text.of("blegh");
            };
        }
    }
    public static Screen getConfigScreen(Screen parent) {
        return YetAnotherConfigLib.createBuilder()
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
                        .build())
                .category(ConfigCategory.createBuilder()
                        .name(Text.of("Core"))
                        .build())
                .category(ConfigCategory.createBuilder()
                        .name(Text.of("Frame 1"))
                        .build())
                .category(ConfigCategory.createBuilder()
                        .name(Text.of("Frame 2"))
                        .build())
                .build()
                .generateScreen(parent);
    }
}
