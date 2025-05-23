package tektonikal.crystalchams;

import dev.isxander.yacl3.api.Option;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.CoreShaderRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import tektonikal.crystalchams.annotation.Updatable;
import tektonikal.crystalchams.config.ChamsConfig;

import java.awt.*;
import java.util.Arrays;
import java.util.OptionalDouble;
import java.util.function.BiFunction;
import java.util.function.Function;

import static net.minecraft.client.render.RenderPhase.*;

public class CrystalChams implements ModInitializer {
    public static final Function<Double, RenderLayer.MultiPhase> CUSTOM_DEBUG_LINE_STRIP = Util.memoize((lineWidth) -> RenderLayer.of("custom_debug_line_strip", VertexFormats.POSITION_COLOR_LIGHT, VertexFormat.DrawMode.DEBUG_LINE_STRIP, 1536, RenderLayer.MultiPhaseParameters.builder().program(POSITION_COLOR_LIGHTMAP_PROGRAM).lineWidth(new RenderPhase.LineWidth(OptionalDouble.of(lineWidth))).transparency(TRANSLUCENT_TRANSPARENCY).cull(DISABLE_CULLING).lightmap(ENABLE_LIGHTMAP).build(false)));
    public static ShaderProgram ENTITY_TRANSLUCENT_NOTEX;
    public static final BiFunction<Identifier, Boolean, RenderLayer> CUSTOM_ENTITY_TRANSLUCENT = Util.memoize((texture, affectsOutline) -> {
        RenderLayer.MultiPhaseParameters multiPhaseParameters = RenderLayer.MultiPhaseParameters.builder().program(new RenderPhase.ShaderProgram(() -> ENTITY_TRANSLUCENT_NOTEX)).texture(new RenderPhase.Texture(texture, false, false)).transparency(TRANSLUCENT_TRANSPARENCY).cull(DISABLE_CULLING).lightmap(ENABLE_LIGHTMAP).overlay(ENABLE_OVERLAY_COLOR).build(affectsOutline);
        return RenderLayer.MultiPhase.of("entity_translucent", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, VertexFormat.DrawMode.QUADS, 1536, true, true, multiPhaseParameters);
    });
    public static EndCrystalEntity entity = new EndCrystalEntity(MinecraftClient.getInstance().world, 0.5, 0, 0.5);

    public static RenderLayer getDebugLineStrip(double lineWidth) {
        return CUSTOM_DEBUG_LINE_STRIP.apply(lineWidth);
    }
    //TODO: write evil kenny screenspace shader
    public static float getYOffset(float age, float offset, float bounceSpeed, float bounceHeight, float tickDelay) {
        //?????
        float g = (MathHelper.sin((age + tickDelay) % ((float) (Math.PI * 2F) / (0.2F * bounceSpeed)) * (0.2F * bounceSpeed)) / 2.0F) + 0.5F;
        g = (g * g + g) * (0.4F * bounceHeight);
        return g - 1.4F + offset;
    }

    public static int getRainbow(int delay, float speed) {
        double rainbowState = Math.ceil((System.currentTimeMillis() + delay)) * speed / 20;
        rainbowState %= 360;
        return Color.getHSBColor((float) (rainbowState / 360.0f), 1F, 1F).getRGB();
    }


    @Override
    public void onInitialize() {
        CoreShaderRegistrationCallback.EVENT.register(context -> context.register(id("crystalchams_entity_translucent_notex"), VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, shaderProgram -> ENTITY_TRANSLUCENT_NOTEX = shaderProgram));
        ChamsConfig.CONFIG.load();
        armSecuritySystem();
        unleashHell();
        entity.setBeamTarget(new BlockPos(0, 1, 0));
        ChamsConfig.o_baseScale.linkOptions(ChamsConfig.o_coreScale, ChamsConfig.o_frame1Scale, ChamsConfig.o_frame2Scale);
        ChamsConfig.o_baseColor.linkOptions(ChamsConfig.o_coreColor, ChamsConfig.o_frame1Color, ChamsConfig.o_frame2Color, ChamsConfig.o_beam1Color, ChamsConfig.o_beam2Color);
        ChamsConfig.o_baseAlpha.linkOptions(ChamsConfig.o_coreAlpha, ChamsConfig.o_frame1Alpha, ChamsConfig.o_frame2Alpha, ChamsConfig.o_beam1Alpha, ChamsConfig.o_beam2Alpha);
        ChamsConfig.o_baseLightLevel.linkOptions(ChamsConfig.o_coreLightLevel, ChamsConfig.o_frame1LightLevel, ChamsConfig.o_frame2LightLevel, ChamsConfig.o_beam1LightLevel, ChamsConfig.o_beam2LightLevel);
        ChamsConfig.o_baseRenderLayer.linkOptions(ChamsConfig.o_coreRenderLayer, ChamsConfig.o_frame1RenderLayer, ChamsConfig.o_frame2RenderLayer, ChamsConfig.o_beamRenderLayer);
        ChamsConfig.o_baseRainbow.linkOptions(ChamsConfig.o_coreRainbow, ChamsConfig.o_frame1Rainbow, ChamsConfig.o_frame2Rainbow, ChamsConfig.o_beam1Rainbow, ChamsConfig.o_beam2Rainbow);
        ChamsConfig.o_baseRainbowSpeed.linkOptions(ChamsConfig.o_coreRainbowSpeed, ChamsConfig.o_frame1RainbowSpeed, ChamsConfig.o_frame2RainbowSpeed, ChamsConfig.o_beam1RainbowSpeed, ChamsConfig.o_beam2RainbowSpeed);
        ChamsConfig.o_baseRainbowDelay.linkOptions(ChamsConfig.o_coreRainbowDelay, ChamsConfig.o_frame1RainbowDelay, ChamsConfig.o_frame2RainbowDelay, ChamsConfig.o_beam1RainbowDelay, ChamsConfig.o_beam2RainbowDelay);
        //don't link vertical offset of crystal with base because why the fuck would you do that you fucking idiot fuck you
        ChamsConfig.o_coreOffset.linkOptions(ChamsConfig.o_frame1Offset, ChamsConfig.o_frame2Offset);
        ChamsConfig.o_coreRotationSpeed.linkOptions(ChamsConfig.o_frame1RotationSpeed, ChamsConfig.o_frame2RotationSpeed);
        ChamsConfig.o_coreBounceHeight.linkOptions(ChamsConfig.o_frame1BounceHeight, ChamsConfig.o_frame2BounceHeight);
        ChamsConfig.o_beam1Radius.linkOptions(ChamsConfig.o_beam2Radius);
        ChamsConfig.o_coreBounceSpeed.linkOptions(ChamsConfig.o_frame1BounceSpeed, ChamsConfig.o_frame2BounceSpeed);
        ChamsConfig.o_coreTickDelay.linkOptions(ChamsConfig.o_frame1TickDelay, ChamsConfig.o_frame2TickDelay);
    }
    public static Identifier id(String path) {
        return Identifier.of("crystalchams", path);
    }

    private static void armSecuritySystem() {
        //can't add listeners while options are created for my use-case, since not everything is fully initialized
        Arrays.stream(ChamsConfig.class.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Updatable.class))
                .forEach(field -> {
                    try {
                        ((Option<Boolean>) field.get(null)).addListener(ChamsConfig::update);
                    } catch (Exception x) {
                        throw new RuntimeException(x);
                    }
                });
    }

    @SuppressWarnings("rawtypes")
    public static void unleashHell() {
        Arrays.stream(ChamsConfig.class.getDeclaredFields())
                .filter(field -> field.getName().startsWith("o_"))
                .forEach(field -> {
                    try {
                        Object value = field.get(null);
                        ((Option) value).requestSet(((Option<?>) value).binding().getValue());
                    } catch (IllegalAccessException e) {
                        System.out.println("what the hell");
                    }
                });
    }
}
