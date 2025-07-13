package tektonikal.crystalchams;

import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.gui.controllers.slider.FloatSliderController;
import dev.isxander.yacl3.gui.controllers.slider.IntegerSliderController;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.CoreShaderRegistrationCallback;
import net.irisshaders.iris.Iris;
import net.irisshaders.iris.api.v0.IrisApi;
import net.irisshaders.iris.layer.BufferSourceWrapper;
import net.irisshaders.iris.layer.EntityRenderStateShard;
import net.irisshaders.iris.layer.OuterWrappedRenderType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.EndPortalBlockEntityRenderer;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import tektonikal.crystalchams.annotation.Updatable;
import tektonikal.crystalchams.config.ChamsConfig;
import tektonikal.crystalchams.config.RenderMode;

import java.awt.*;
import java.util.Arrays;
import java.util.OptionalDouble;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Function;

import static net.minecraft.client.render.RenderPhase.*;

public class CrystalChams implements ModInitializer {
    public static final Function<Double, RenderLayer.MultiPhase> CUSTOM_DEBUG_LINE_STRIP = Util.memoize((lineWidth) -> RenderLayer.of("custom_debug_line_strip", VertexFormats.POSITION_COLOR_LIGHT, VertexFormat.DrawMode.DEBUG_LINE_STRIP, 1536, RenderLayer.MultiPhaseParameters.builder().program(POSITION_COLOR_LIGHTMAP_PROGRAM).lineWidth(new RenderPhase.LineWidth(OptionalDouble.of(lineWidth))).transparency(TRANSLUCENT_TRANSPARENCY).cull(DISABLE_CULLING).lightmap(ENABLE_LIGHTMAP).build(false)));
    public static ShaderProgram ENTITY_TRANSLUCENT_NOTEX;
    public static ShaderProgram END_PORTAL_TEX;
    public static ShaderProgram CUSTOM_IMAGE;
    public static final BiFunction<Identifier, Boolean, RenderLayer> CUSTOM_ENTITY_NOTEX = Util.memoize((texture, shouldCull) -> {
        RenderLayer.MultiPhaseParameters multiPhaseParameters = RenderLayer.MultiPhaseParameters.builder().program(new RenderPhase.ShaderProgram(() -> ENTITY_TRANSLUCENT_NOTEX)).texture(new RenderPhase.Texture(texture, false, false)).transparency(TRANSLUCENT_TRANSPARENCY).cull(shouldCull ? ENABLE_CULLING : DISABLE_CULLING).lightmap(ENABLE_LIGHTMAP).overlay(ENABLE_OVERLAY_COLOR).build(true);
        return RenderLayer.MultiPhase.of("custom_entity_translucent", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, VertexFormat.DrawMode.QUADS, 1536, true, true, multiPhaseParameters);
    });
    public static final BiFunction<Identifier, Boolean, RenderLayer> CUSTOM_END_GATEWAY = Util.memoize((texture, shouldCull) -> RenderLayer.of("custom_end_gateway", VertexFormats.POSITION_TEXTURE_COLOR, VertexFormat.DrawMode.QUADS, 1536, false, true, RenderLayer.MultiPhaseParameters.builder().program(new RenderPhase.ShaderProgram(() -> END_PORTAL_TEX)).texture(Textures.create().add(EndPortalBlockEntityRenderer.SKY_TEXTURE, false, false).add(EndPortalBlockEntityRenderer.PORTAL_TEXTURE, false, false).add(texture, false, false).build()).transparency(TRANSLUCENT_TRANSPARENCY).cull(shouldCull ? ENABLE_CULLING : DISABLE_CULLING).build(true)));
    public static final BiFunction<Identifier, Boolean, RenderLayer> CUSTOM_IMAGE_FUNC = Util.memoize((texture, shouldCull) -> RenderLayer.of("custom_end_gateway", VertexFormats.POSITION_TEXTURE, VertexFormat.DrawMode.QUADS, 1536, false, true, RenderLayer.MultiPhaseParameters.builder().program(new RenderPhase.ShaderProgram(() -> CUSTOM_IMAGE)).texture(Textures.create().add(Identifier.of("crystalchams:custom/image.png"), false, false).add(texture, false, false).build()).transparency(TRANSLUCENT_TRANSPARENCY).cull(shouldCull ? ENABLE_CULLING : DISABLE_CULLING).build(true)));
    public static final BiFunction<Identifier, Boolean, RenderLayer> CUSTOM_NORMAL = Util.memoize((texture, shouldCull) -> {
        RenderLayer.MultiPhaseParameters multiPhaseParameters = RenderLayer.MultiPhaseParameters.builder().program(ENTITY_TRANSLUCENT_PROGRAM).texture(new Texture(texture, false, false)).transparency(TRANSLUCENT_TRANSPARENCY).cull(shouldCull ? ENABLE_CULLING : DISABLE_CULLING).lightmap(ENABLE_LIGHTMAP).overlay(ENABLE_OVERLAY_COLOR).build(true);
        return RenderLayer.of("custom_entity_translucent", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, VertexFormat.DrawMode.QUADS, 1536, true, true, multiPhaseParameters);
    });
    public static EndCrystalEntity entity = new EndCrystalEntity(MinecraftClient.getInstance().world, 0.5, 0, 0.5);

    public static float getYOffset(float age, float offset, float bounceSpeed, float bounceHeight, float tickDelay) {
        //?????
        float g = (MathHelper.sin((age + tickDelay) % ((float) (Math.PI * 2F) / (0.2F * bounceSpeed)) * (0.2F * bounceSpeed)) / 2.0F) + 0.5F;
        g = (g * g + g) * (0.4F * bounceHeight);
        return g - 1.4F + offset;
    }

    public static int getRainbow(int delay, float speed, float saturation, float brightness) {
        double rainbowState = Math.ceil((System.currentTimeMillis() + delay)) * speed / 20;
        rainbowState %= 360;
        return Color.getHSBColor((float) (rainbowState / 360.0f), saturation, brightness).getRGB();
    }

    public static VertexConsumer getLayer(VertexConsumerProvider vcp, RenderMode layer, boolean cull, Identifier texture) {
        if (layer.getBiFunction() != null) {
            return vcp.getBuffer(layer.getBiFunction().apply(texture, cull));
        } else {
            return vcp.getBuffer(layer.getFunction().apply(1.0));
        }
    }

    public static float crystalRotX;
    public static float crystalRotY;


    @Override
    public void onInitialize() {
        CoreShaderRegistrationCallback.EVENT.register(context -> context.register(id("crystalchams_entity_translucent_notex"), VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, shaderProgram -> ENTITY_TRANSLUCENT_NOTEX = shaderProgram));
        CoreShaderRegistrationCallback.EVENT.register(context -> context.register(id("crystalchams_end_gateway_tex"), VertexFormats.POSITION_TEXTURE_COLOR, shaderProgram -> END_PORTAL_TEX = shaderProgram));
        CoreShaderRegistrationCallback.EVENT.register(context -> context.register(id("crystalchams_image"), VertexFormats.POSITION_TEXTURE, shaderProgram -> CUSTOM_IMAGE = shaderProgram));

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
        Arrays.stream(ChamsConfig.class.getDeclaredFields()).filter(field -> field.isAnnotationPresent(Updatable.class)).forEach(field -> {
            try {
                ((Option<Boolean>) field.get(null)).addListener(ChamsConfig::update);
            } catch (Exception x) {
                throw new RuntimeException(x);
            }
        });
    }

    @SuppressWarnings("rawtypes")
    public static void unleashHell() {
        Arrays.stream(ChamsConfig.class.getDeclaredFields()).filter(field -> field.getName().startsWith("o_")).forEach(field -> {
            try {
                Object value = field.get(null);
                ((Option) value).requestSet(((Option<?>) value).binding().getValue());
            } catch (IllegalAccessException e) {
                System.out.println("what the hell");
            }
        });
    }

    public static void randomizeOptions() {
        Arrays.stream(ChamsConfig.class.getDeclaredFields()).filter(field -> field.getName().startsWith("o_")).forEach(field -> {
            try {
                Object value = field.get(null);
                if (value.equals(ChamsConfig.o_modEnabled)) {
                    ChamsConfig.o_modEnabled.requestSet(true);
                    return;
                }
                if (value.equals(ChamsConfig.o_baseRenderMode)) {
                    //TODO
                    return;
                }
                if (value.equals(ChamsConfig.o_coreAlphaEasing)) {
                    return;
                }
                if (value.equals(ChamsConfig.o_coreScaleEasing)) {
                    return;
                }
                switch (((Option<?>) value).controller().getClass().getCanonicalName()) {
                    case "dev.isxander.yacl3.gui.controllers.TickBoxController":
                        ((Option<Boolean>) value).requestSet(Math.random() > 0.5);
                        break;
                    case "dev.isxander.yacl3.gui.controllers.ColorController":
                        ((Option<Color>) value).requestSet(new Color((int) (Math.random() * 0x1000000)));
                        break;
                    case "dev.isxander.yacl3.gui.controllers.slider.FloatSliderController":
                        ((Option<Float>) value).requestSet(safeRandom((float) ((FloatSliderController) ((Option<Float>) value).controller()).min(), (float) ((FloatSliderController) ((Option<Float>) value).controller()).max()));
                        break;
                    case "dev.isxander.yacl3.gui.controllers.slider.IntegerSliderController":
                        ((Option<Integer>) value).requestSet(safeRandom((int) ((IntegerSliderController) ((Option<Integer>) value).controller()).min(), (int) ((IntegerSliderController) ((Option<Integer>) value).controller()).max()));
                        break;
                    case "dev.isxander.yacl3.gui.controllers.cycling.EnumController":
                        ((Option<RenderMode>) value).requestSet(RenderMode.values()[safeRandom(0, RenderMode.values().length - 1)]);
                        break;
                    default:
                        System.out.println("huhhhhh????");
                }
            } catch (IllegalAccessException e) {
                System.out.println("Something has gone slightly less terribly, but still wrong!!!");
            } catch (UnsupportedOperationException e) {
                System.out.println("SOMETHING HAS GONE TERRIBLY WRONG!!!");
            }
        });
    }

    public static Random rand = new Random();

    public static int safeRandom(int min, int max) {
        //I don't trust this thing
        return min == max ? min : rand.nextInt(Math.max(min, max) - Math.min(min, max) + 1) + Math.min(min, max);
    }

    //this "works" but isn't fully correct according to my testing.
    //if only it was easy to generate random floats within a set range, and it allowed negative numbers...
    //TODO: remake this (maybe just do random int + rand float?)
    public static float safeRandom(float min, float max) {
        if (min == max) {
            return min;
        } else {
            float midpoint = Math.max(min, max) / 2 + Math.min(min, max) / 2;
            float half_range = Math.max(min, max) / 2 - Math.min(min, max) / 2;
            int plus_minus = rand.nextBoolean() ? 1 : -1;
            return midpoint + plus_minus * rand.nextFloat() * half_range;
        }
    }
    public static double ease(double start, double end, float speed) {
        return (start + (end - start) * (1 - Math.exp(-(1.0F / MinecraftClient.getInstance().getCurrentFps()) * speed)));
    }
}
