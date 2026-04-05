package tektonikal.crystalchams;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.*;
import dev.isxander.yacl3.api.utils.OptionUtils;
import dev.isxander.yacl3.config.v2.impl.serializer.GsonConfigSerializer;
import dev.isxander.yacl3.gui.YACLScreen;
import dev.isxander.yacl3.gui.controllers.PopupControllerScreen;
import dev.isxander.yacl3.gui.controllers.slider.FloatSliderController;
import dev.isxander.yacl3.gui.controllers.slider.IntegerSliderController;
import dev.isxander.yacl3.impl.ListOptionEntryImpl;
import dev.isxander.yacl3.impl.controller.ColorControllerBuilderImpl;
import dev.isxander.yacl3.impl.controller.FloatSliderControllerBuilderImpl;
import dev.isxander.yacl3.impl.controller.TickBoxControllerBuilderImpl;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.hud.debug.DebugHudEntries;
import net.minecraft.client.gui.navigation.NavigationAxis;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.EndPortalBlockEntityRenderer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.debug.EntityHitboxDebugRenderer;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.debug.gizmo.GizmoDrawing;
import net.minecraft.world.debug.gizmo.TextGizmo;
import org.joml.*;
import org.lwjgl.opengl.GL11;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tektonikal.crystalchams.annotation.Updatable;
import tektonikal.crystalchams.config.*;
import tektonikal.crystalchams.mixin.EntityHitboxDebugRendererMixin;
import tektonikal.crystalchams.mixin.RenderTickCounterAccessor;
import tektonikal.crystalchams.stupidfuckingboilerplate.*;
import tektonikal.crystalchams.util.Easings;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.Math;
import java.util.*;
import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Function;

import static net.minecraft.client.gl.RenderPipelines.ENTITY_SNIPPET;
import static net.minecraft.client.render.entity.EnderDragonEntityRenderer.CRYSTAL_BEAM_TEXTURE;

public class CrystalChams implements ModInitializer {
    public static final MinecraftClient mc = MinecraftClient.getInstance();
    //    public static final Function<Double, RenderLayer.MultiPhase> CUSTOM_DEBUG_LINE_STRIP = Util.memoize((lineWidth) -> RenderLayer.of("custom_debug_line_strip", VertexFormats.POSITION_COLOR_LIGHT, VertexFormat.DrawMode.DEBUG_LINE_STRIP, 1536, RenderLayer.MultiPhaseParameters.builder().program(POSITION_COLOR_LIGHTMAP_PROGRAM).lineWidth(new LineWidth(OptionalDouble.of(lineWidth))).transparency(TRANSLUCENT_TRANSPARENCY).cull(DISABLE_CULLING).lightmap(ENABLE_LIGHTMAP).build(false)));
    public static final String SEPARATOR = " - ";
    public static ShaderProgram ENTITY_TRANSLUCENT_NOTEX;
    public static ShaderProgram END_PORTAL_TEX;
    public static int imageIndex = 0;
    public static ShaderProgram CUSTOM_IMAGE;
    public static final float PREVIEW_EASING_SPEED = 12.5F;
    public static final ValueFormatter<Integer> LIGHT_FORMATTER = value -> Text.of(value == -1 ? "Use World Light" : value + "");
    public static final ValueFormatter<Float> PERCENT_FORMATTER = value -> Text.of((int) (value * 100) + "%");
    public static final ValueFormatter<Float> MULTIPLIER_FORMATTER = val -> Text.of(String.format("%.2f", val) + "x");
    public static final ValueFormatter<Float> MULTIPLIER_FORMATTER_ONE_PLACE = val -> Text.of(String.format("%.1f", val) + "x");
    public static final ValueFormatter<Float> BLOCKS_FORMATTER = val -> Text.of(String.format("%.1f", val).replace(".0", "") + (Math.abs(val) == 1 ? " block" : " blocks"));
    public static final ValueFormatter<Float> BLOCKS_FORMATTER_TWO_PLACES = val -> Text.of(String.format("%.2f", val).replace(".0", "") + (Math.abs(val) == 1 ? " block" : " blocks"));
    public static final ValueFormatter<Float> SECONDS_FORMATTER = val -> Text.of(String.format("%.1f", val).replace(".0", "") + (Math.abs(val) == 1 ? " second" : " seconds"));
    public static final Function<Option<Float>, ControllerBuilder<Float>> PERCENT = floatOption -> FloatSliderControllerBuilder.create(floatOption).range(0f, 1f).step(0.01f).formatValue(PERCENT_FORMATTER);
    public static final Function<Option<Integer>, ControllerBuilder<Integer>> LIGHT = intOption -> IntegerSliderControllerBuilder.create(intOption).range(-1, 255).step(1).formatValue(LIGHT_FORMATTER);
    //    public static final BiFunction<Identifier, Boolean, RenderLayer> CUSTOM_ENTITY_NOTEX = Util.memoize((texture, shouldCull) -> {
//        RenderLayer.MultiPhaseParameters multiPhaseParameters = RenderLayer.MultiPhaseParameters.builder().program(new RenderPhase.ShaderProgram(() -> ENTITY_TRANSLUCENT_NOTEX)).texture(new Texture(texture, false, false)).transparency(TRANSLUCENT_TRANSPARENCY).cull(shouldCull ? ENABLE_CULLING : DISABLE_CULLING).lightmap(ENABLE_LIGHTMAP).overlay(ENABLE_OVERLAY_COLOR).build(true);
//        return RenderLayer.MultiPhase.of("custom_entity_translucent", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, VertexFormat.DrawMode.QUADS, 1536, true, true, multiPhaseParameters);
//    });
//    public static final BiFunction<Identifier, Boolean, RenderLayer> CUSTOM_END_GATEWAY = Util.memoize((texture, shouldCull) -> RenderLayer.of("custom_end_gateway", VertexFormats.POSITION_TEXTURE_COLOR, VertexFormat.DrawMode.QUADS, 1536, false, true, RenderLayer.MultiPhaseParameters.builder().program(new RenderPhase.ShaderProgram(() -> END_PORTAL_TEX)).texture(Textures.create().add(EndPortalBlockEntityRenderer.SKY_TEXTURE, false, false).add(EndPortalBlockEntityRenderer.PORTAL_TEXTURE, false, false).add(texture, false, false).build()).transparency(TRANSLUCENT_TRANSPARENCY).cull(shouldCull ? ENABLE_CULLING : DISABLE_CULLING).build(true)));
//    public static final BiFunction<Identifier, Boolean, RenderLayer> CUSTOM_IMAGE_FUNC = Util.memoize((texture, shouldCull) -> RenderLayer.of("custom_end_gateway", VertexFormats.POSITION_TEXTURE_COLOR_LIGHT, VertexFormat.DrawMode.QUADS, 1536, false, true, RenderLayer.MultiPhaseParameters.builder().program(new RenderPhase.ShaderProgram(() -> CUSTOM_IMAGE)).texture(Textures.create().add(Identifier.of("crystalchams:custom/image.png"), false, false).add(texture, false, false).build()).transparency(TRANSLUCENT_TRANSPARENCY).cull(shouldCull ? ENABLE_CULLING : DISABLE_CULLING).build(true)));
//    public static final BiFunction<Identifier, Boolean, RenderLayer> CUSTOM_NORMAL = Util.memoize((texture, shouldCull) -> {
//        RenderLayer.MultiPhaseParameters multiPhaseParameters = RenderLayer.MultiPhaseParameters.builder().program(ENTITY_TRANSLUCENT_PROGRAM).texture(new Texture(texture, false, false)).transparency(TRANSLUCENT_TRANSPARENCY).cull(shouldCull ? ENABLE_CULLING : DISABLE_CULLING).lightmap(ENABLE_LIGHTMAP).overlay(ENABLE_OVERLAY_COLOR).build(true);
//        return RenderLayer.of("custom_entity_translucent", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, VertexFormat.DrawMode.QUADS, 1536, true, true, multiPhaseParameters);
//    });
    public static int hoveredIndex;
    public static EndCrystalEntity previewCrystalEntity = new EndCrystalEntity(mc.world, 0.5, 0, 0);
    public static float previewScaleSmoothed = ChamsConfig.CONFIG.instance().previewScale;
    public static float beamProgress;
    public static Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
            .serializeNulls()
            .registerTypeHierarchyAdapter(Color.class, new GsonConfigSerializer.ColorTypeAdapter())
            .setPrettyPrinting()
            .create();
    public static final String CC_MOD_ID = "crystalchams";
    public static final Logger LOGGER = LoggerFactory.getLogger(CC_MOD_ID);
    public static List<BufferedImage> frames = List.of();
    public static String filePath = "";
    public EntityHitboxDebugRenderer yeah;
    public static final RenderPipeline ENTITY_TRANSLUCENT_CULL = RenderPipelines.register(
            RenderPipeline.builder(ENTITY_SNIPPET)
                    .withLocation("pipeline/entity_translucent")
                    .withShaderDefine("ALPHA_CUTOUT", 0.1F)
                    .withShaderDefine("PER_FACE_LIGHTING")
                    .withSampler("Sampler1")
                    .withBlend(BlendFunction.TRANSLUCENT)
                    .withCull(true)
                    .build()
    );
    private static final BiFunction<Identifier, Boolean, RenderLayer> ENTITY_TRANSLUCENT_CULLED = Util.memoize(
            (texture, affectsOutline) -> {
                RenderSetup renderSetup = RenderSetup.builder(ENTITY_TRANSLUCENT_CULL)
                        .texture("Sampler0", texture)
                        .useLightmap()
                        .useOverlay()
                        .crumbling()
                        .translucent()
                        .outlineMode(affectsOutline ? RenderSetup.OutlineMode.AFFECTS_OUTLINE : RenderSetup.OutlineMode.NONE)
                        .build();
                return RenderLayer.of("entity_translucent", renderSetup);
            }
    );

    public static float getYOffset(float age, float offset, float bounceSpeed, float bounceHeight, float tickDelay) {
        //?????
        float g = (MathHelper.sin((age + (tickDelay * 20)) % ((float) (Math.PI * 2F) / (0.2F * bounceSpeed)) * (0.2F * bounceSpeed)) / 2.0F) + 0.5F;
        g = (g * g + g) * (0.4F * bounceHeight);
        return g - 1.4F + offset;
    }

    public static int getRainbow(float delay, float speed, float saturation, float brightness) {
        double rainbowState = Math.ceil((System.currentTimeMillis() + (int) (delay * 1000))) * speed / 20;
        rainbowState %= 360;
        return Color.getHSBColor((float) (rainbowState / 360.0f), saturation, brightness).getRGB();
    }

    public static RenderLayer getLayer(RenderMode layer, boolean cull, Identifier texture) {
        if(cull){
            return ENTITY_TRANSLUCENT_CULLED.apply(texture, true);
        }
        else{
            return RenderLayers.entityTranslucent(texture);
        }
//        if (layer.getBiFunction() != null) {
//            return layer.getBiFunction().apply(texture, cull);
//        } else {
//            return layer.getFunction().apply(1.0);
//        }
    }

//    public static void fillFloat(DrawContext context, float x1, float y1, float x2, float y2, int color) {
//        Matrix4f matrix4f = context.getMatrices().peek().getPositionMatrix();
//        VertexConsumer vertexConsumer = context.getVertexConsumers().getBuffer(RenderLayer.getGui());
//        vertexConsumer.vertex(matrix4f, x1, y1, 0).color(color);
//        vertexConsumer.vertex(matrix4f, x1, y2, 0).color(color);
//        vertexConsumer.vertex(matrix4f, x2, y2, 0).color(color);
//        vertexConsumer.vertex(matrix4f, x2, y1, 0).color(color);
//        context.draw();
//    }

    //mouse following rotation
    public static float crystalRotX;
    public static float crystalRotY;
    public static float crystalDraggedRotX;
    public static float crystalDraggedRotY;
    public static float crystalTargetDraggedRotX;
    public static float crystalTargetDraggedRotY;

    public static EvilOption<Boolean> createBooleanOption(Text name, StateManager<Boolean> stateManager, OptionGroups group) {
        return EvilOption.<Boolean>createBuilder().name(name).stateManager(stateManager).description(OptionDescription.of()).controller(TickBoxControllerBuilderImpl::new).group(group).build();
    }
    

//    public static EvilOption<Float> createFloatOptionSeconds(String name, StateManager<Float> stateManager, OptionGroups group) {
//        return EvilOption.<Float>createBuilder().name(Text.translatable(name)).stateManager(stateManager).description(OptionDescription.of()).controller(floatOption -> new FloatSliderControllerBuilderImpl(floatOption).range(-2.5f, 2.5f).step(0.1f).formatValue(SECONDS_FORMATTER)).group(group).build();
//    }

    public static EvilOption<Float> createFloatOptionPercent(Text name, StateManager<Float> stateManager, OptionGroups group) {
        return EvilOption.<Float>createBuilder().name(name).stateManager(stateManager).description(OptionDescription.of()).controller(PERCENT).group(group).build();
    }

    public static EvilOption<Easings> createEasingOption(StateManager<Easings> stateManager, OptionGroups group) {
        return EvilOption.<Easings>createBuilder().name(Text.translatable(SEPARATOR).append(Text.translatable("config.option.easing"))).stateManager(stateManager).description(OptionDescription.of()).controller(easingsOption -> EnumControllerBuilder.create(easingsOption).enumClass(Easings.class)).group(group).build();
    }

    public static EvilOption<RenderMode> createRenderModeOption(Text name, StateManager<RenderMode> stateManager, OptionGroups group) {
        return EvilOption.<RenderMode>createBuilder().name(name).stateManager(stateManager).description(OptionDescription.of()).controller(easingsOption -> EnumControllerBuilder.create(easingsOption).enumClass(RenderMode.class)).group(group).build();
    }

    public static EvilOption<Color> createColorOption(Text name, StateManager<Color> stateManager, OptionGroups group) {
        return EvilOption.<Color>createBuilder().name(name).stateManager(stateManager).description(OptionDescription.of()).controller(ColorControllerBuilderImpl::new).group(group).build();
    }

    public static EvilOption<Integer> createSkyLightLevelOption(StateManager<Integer> stateManager) {
        return EvilOption.<Integer>createBuilder()
                .name(Text.translatable("config.option.skyLightLevel"))
                .description(OptionDescription.createBuilder().text(Text.of("yeahhhh")).build())
                .controller(integerOption -> IntegerSliderControllerBuilder.create(integerOption).step(1).range(-1, 15).formatValue(CrystalChams.LIGHT_FORMATTER))
                .stateManager(stateManager)
                .group(OptionGroups.SKY_LIGHT)
                .build();
    }

    public static EvilOption<Integer> createBlockLightLevelOption(StateManager<Integer> stateManager) {
        return EvilOption.<Integer>createBuilder()
                .name(Text.translatable("config.option.blockLightLevel"))
                .description(OptionDescription.createBuilder().text(Text.of("yeahhhh")).build())
                .controller(integerOption -> IntegerSliderControllerBuilder.create(integerOption).step(1).range(-1, 15).formatValue(CrystalChams.LIGHT_FORMATTER))
                .stateManager(stateManager)
                .group(OptionGroups.BLOCK_LIGHT)
                .build();
    }

    @Override
    public void onInitialize() {
        yeah = new EntityHitboxDebugRenderer(mc);
        ChamsConfig.CONFIG.load();
//        CoreShaderRegistrationCallback.EVENT.register(context -> context.register(id("crystalchams_entity_translucent_notex"), VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, shaderProgram -> ENTITY_TRANSLUCENT_NOTEX = shaderProgram));
//        CoreShaderRegistrationCallback.EVENT.register(context -> context.register(id("crystalchams_end_gateway_tex"), VertexFormats.POSITION_TEXTURE_COLOR, shaderProgram -> END_PORTAL_TEX = shaderProgram));
//        CoreShaderRegistrationCallback.EVENT.register(context -> context.register(id("crystalchams_image"), VertexFormats.POSITION_TEXTURE, shaderProgram -> CUSTOM_IMAGE = shaderProgram));
        armSecuritySystem();
        WorldRenderEvents.BEFORE_DEBUG_RENDER.register(context -> {
            if (ChamsConfig.o_renderHitbox.pendingValue() && !mc.debugHudEntryList.isEntryVisible(DebugHudEntries.ENTITY_HITBOXES)) {
                if (mc.world != null) {
                    for (Entity entity : mc.world.getEntities()) {
                        if (!entity.isInvisible()
                                && entity instanceof EndCrystalEntity
                                //TODO: i guess bro
//                                && mc.worldRenderer.getCapturedFrustum().isVisible(entity.getBoundingBox())
                                && (entity != mc.getCameraEntity() || mc.options.getPerspective() != Perspective.FIRST_PERSON)) {
                            ((EntityHitboxDebugRendererMixin) yeah).yeah(entity, mc.getRenderTickCounter().getDynamicDeltaTicks(), false);
                        }
                    }
                }
            }
        });
        //yeah.
        unleashHell();
        ChamsConfig.update(ChamsConfig.o_baseRainbow, ChamsConfig.o_baseRainbow.stateManager().get());
        ChamsConfig.update(ChamsConfig.o_coreRainbow, ChamsConfig.o_coreRainbow.stateManager().get());
        ChamsConfig.update(ChamsConfig.o_beam1Rainbow, ChamsConfig.o_beam1Rainbow.stateManager().get());
        ChamsConfig.update(ChamsConfig.o_beam2Rainbow, ChamsConfig.o_beam2Rainbow.stateManager().get());
    }

//    public static void drawPreviewCrystal(DrawContext drawContext, ScreenRect rightPaneDim, YACLScreen yaclScreen) {
////        RenderSystem.enableBlend();
////        DiffuseLighting.method_34742();
//        float centerX = rightPaneDim.getCenter(NavigationAxis.HORIZONTAL);
//        float centerY = rightPaneDim.getCenter(NavigationAxis.VERTICAL);
//        //TODO: fuck around with these numbers a bit more later
//        double scaledMouseX = mc.mouse.getX() * mc.getWindow().getScaledWidth() / mc.getWindow().getWidth();
//        double scaledMouseY = mc.mouse.getY() * mc.getWindow().getScaledHeight() / mc.getWindow().getHeight();
//        CrystalChams.crystalRotX = (float) CrystalChams.ease(CrystalChams.crystalRotX, Math.atan((centerX - scaledMouseX) / 40F), 15F);
//        CrystalChams.crystalRotY = (float) CrystalChams.ease(CrystalChams.crystalRotY, Math.atan((centerY - scaledMouseY) / 40F), 15F);
//        CrystalChams.previewScaleSmoothed = (float) CrystalChams.ease(CrystalChams.previewScaleSmoothed, ChamsConfig.o_previewScale.pendingValue(), 15F);
//        if (Float.isNaN(CrystalChams.crystalRotX) || Float.isNaN(CrystalChams.crystalRotY)) {
//            CrystalChams.crystalRotX = 0;
//            CrystalChams.crystalRotY = 0;
//        }
//        if (Math.abs(crystalDraggedRotX) >= 360 && Math.abs(crystalTargetDraggedRotX) >= 360) {
//            crystalDraggedRotX %= 360;
//            crystalTargetDraggedRotX %= 360;
//        }
//        if (Math.abs(crystalDraggedRotY) >= 360 && Math.abs(crystalTargetDraggedRotY) >= 360) {
//            crystalDraggedRotY %= 360;
//            crystalTargetDraggedRotY %= 360;
//        }
//        if (yaclScreen.tabManager.getCurrentTab().getTitle().equals(Text.translatable("config.category.beam"))) {
//            crystalTargetDraggedRotX = 0;
//            crystalTargetDraggedRotY = 0;
//        }
//        CrystalChams.beamProgress = (float) CrystalChams.ease(CrystalChams.beamProgress, yaclScreen.tabManager.getCurrentTab().getTitle().equals(Text.translatable("config.category.beam")) ? 1 : 0, 5);
//        drawContext.getMatrices().pushMatrix();
//        drawContext.getMatrices().translate(centerX, centerY, 500.0);
//        float scaleFac = Math.min(CrystalChams.mc.getWindow().getScaledWidth(), CrystalChams.mc.getWindow().getScaledHeight()) / 7.5F * previewScaleSmoothed;
//        drawContext.getMatrices().scale(scaleFac, scaleFac, (scaleFac));
//        CrystalChams.crystalDraggedRotY = (float) ease(crystalDraggedRotY, crystalTargetDraggedRotY, 15);
//        CrystalChams.crystalDraggedRotX = (float) ease(crystalDraggedRotX, crystalTargetDraggedRotX, 15);
//        Vector3f yeah = new Vector3f(CrystalChams.crystalRotY * 20.0F * (float) (Math.PI / 180.0), -CrystalChams.crystalRotX * 20.0F * (float) (Math.PI / 180.0), (float) Math.PI);
//        yeah.x += CrystalChams.crystalDraggedRotY * (float) (Math.PI / 180.0);
//        yeah.y += CrystalChams.crystalDraggedRotX * (float) (Math.PI / 180.0);
//        drawContext.getMatrices().multiply(new Quaternionf().rotationXYZ(yeah.x, yeah.y, yeah.z));
//        CrystalChams.mc.getEntityRenderDispatcher().getRenderer(CrystalChams.previewCrystalEntity).render(CrystalChams.previewCrystalEntity, 0, ((RenderTickCounterAccessor) CrystalChams.mc.getRenderTickCounter()).getTickDelta(), drawContext.getMatrices(), CrystalChams.mc.getBufferBuilders().getEntityVertexConsumers(), 255);
//        drawContext.getMatrices().popMatrix();
//        if (ChamsConfig.o_renderBeam.pendingValue() && beamProgress > 0.1) {
//            drawContext.getMatrices().pushMatrix();
//            drawContext.getMatrices().scale(scaleFac, scaleFac, -scaleFac);
////            drawContext.getMatrices().multiply(new Quaternionf().rotationXYZ(yeah.x, yeah.y, yeah.z));
//            double scaleFactorX = (double) mc.getWindow().getWidth() / mc.getWindow().getScaledWidth();
//            double scaleFactorY = (double) mc.getWindow().getHeight() / mc.getWindow().getScaledHeight();
//            Vec3d vec = CrystalChams.screenSpaceToWorldSpace(MathHelper.lerp(CrystalChams.beamProgress, centerX * scaleFactorX, mc.mouse.getX()), MathHelper.lerp(CrystalChams.beamProgress, centerY * scaleFactorY, mc.mouse.getY()), 0, drawContext.getMatrices().peek().getPositionMatrix()).multiply(1 / CrystalChams.mc.getWindow().getScaleFactor());
//            drawContext.getMatrices().translate(vec.x, vec.y, vec.z);
//            CrystalChams.renderCustomBeam((float) (-vec.x + ((rightPaneDim.getCenter(NavigationAxis.HORIZONTAL) / scaleFac))), (float) -vec.y + (rightPaneDim.getCenter(NavigationAxis.VERTICAL) / scaleFac) - CrystalChams.getYOffset(CrystalChams.previewCrystalEntity.endCrystalAge + ((RenderTickCounterAccessor) CrystalChams.mc.getRenderTickCounter()).getTickDelta(), ChamsConfig.o_coreOffset.pendingValue(), ChamsConfig.o_coreBounceSpeed.pendingValue(), ChamsConfig.o_coreBounceHeight.pendingValue(), ChamsConfig.o_coreDelay.pendingValue()) - 2, 2.5F, ((RenderTickCounterAccessor) CrystalChams.mc.getRenderTickCounter()).getTickDelta(), CrystalChams.previewCrystalEntity.endCrystalAge, drawContext.getMatrices(), CrystalChams.mc.getBufferBuilders().getEntityVertexConsumers(), LightmapTextureManager.MAX_LIGHT_COORDINATE, 1);
//            drawContext.getMatrices().popMatrix();
//        }

    /// /        drawContext.fill(rightPaneDim.getLeft(), rightPaneDim.getTop(), rightPaneDim.getRight(), rightPaneDim.getBottom(), 0xFF00FF00);
    /// /        drawContext.draw();
//        RenderSystem.disableBlend();
//        DiffuseLighting.enableGuiDepthLighting();
//    }
    public static Identifier id(String path) {
        return Identifier.of("crystalchams", path);
    }

    private static void armSecuritySystem() {
        //can't add listeners while options are created for my use-case, since not everything is fully initialized
        Arrays.stream(ChamsConfig.class.getDeclaredFields()).filter(field -> field.isAnnotationPresent(Updatable.class)).forEach(field -> {
            try {
                ((Option<Boolean>) field.get(null)).addListener(ChamsConfig::update);
            } catch (Exception x) {
                LOGGER.error("Error adding listeners: {}", field.getName());
            }
        });
    }

    public static void unleashHell() {
        try {
            Arrays.stream(ChamsConfig.class.getDeclaredFields()).filter(field -> field.getName().startsWith("o_") && !field.getName().equals("CONFIG")).forEach(field -> {
                try {
                    ((Option) field.get(null)).requestSet(ChamsConfig.class.getField(field.getName().replace("o_", "")).get(ChamsConfig.CONFIG.instance()));
                } catch (IllegalAccessException | NoSuchFieldException e) {
                    CrystalChams.LOGGER.error("Error while loading config. what the fuck man");
                }
            });
        } catch (SecurityException e) {
            throw new RuntimeException(e);
        }
    }

    public static void randomizeOptions() {
        Arrays.stream(ChamsConfig.class.getDeclaredFields()).filter(field -> field.getName().startsWith("o_")).forEach(field -> {
            if (field.getName().equals("o_frameList")) {
                //TODO: i still think it would be really funny to have it generate a random number of frames
                ChamsConfig.o_frameList.options().forEach(listOptionEntry -> {
                    ModelPartController controller = (ModelPartController) ((ListOptionEntryImpl.EntryController) (listOptionEntry.controller())).controller();
                    Arrays.stream(ModelPartController.class.getDeclaredFields()).filter(field1 -> field1.getName().startsWith("o_")).forEach(field1 -> {
                        try {
                            randomizeOption(field1.get(controller));
                        } catch (IllegalAccessException | NullPointerException ignored) {
                            LOGGER.error("Error during randomization of options");
                        }
                    });
                });
                return;
            }
            try {
                randomizeOption(field.get(null));
            } catch (IllegalAccessException e) {
                LOGGER.error("This shouldn't happen. this sucks");
            }
        });
    }

    private static void randomizeOption(Object value) {
        try {
            if (value.equals(ChamsConfig.o_modEnabled)) {
                ChamsConfig.o_modEnabled.requestSet(true);
                return;
            }
            if (value.equals(ChamsConfig.o_baseRenderMode)) {
                //TODO: expand the list of options. i don't want to randomize the preview scale anymore
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
                case "tektonikal.crystalchams.config.CustomTickBoxController":
                    ((Option<Boolean>) value).requestSet(Math.random() > 0.5);
                    break;
                case "dev.isxander.yacl3.gui.controllers.ColorController":
                    ((Option<Color>) value).requestSet(new Color((int) (Math.random() * 0x1000000)));
                    break;
                case "dev.isxander.yacl3.gui.controllers.slider.FloatSliderController":
                    ((Option<Float>) value).requestSet(safeRandom((float) ((FloatSliderController) ((Option<Float>) value).controller()).min(), (float) ((FloatSliderController) ((Option<Float>) value).controller()).max()));
                    break;
//                case "tektonikal.crystalchams.stupidfuckingboilerplate.CustomFloatSliderController":
//                    ((Option<Float>) value).requestSet(safeRandom((float) ((CustomFloatSliderController) ((Option<Float>) value).controller()).min(), (float) ((CustomFloatSliderController) ((Option<Float>) value).controller()).max()));
//                    break;
                case "dev.isxander.yacl3.gui.controllers.slider.IntegerSliderController":
                    ((Option<Integer>) value).requestSet(safeRandom((int) ((IntegerSliderController) ((Option<Integer>) value).controller()).min(), (int) ((IntegerSliderController) ((Option<Integer>) value).controller()).max()));
                    break;
//                case "tektonikal.crystalchams.stupidfuckingboilerplate.CustomIntegerSliderController":
//                    ((Option<Integer>) value).requestSet(safeRandom((int) ((CustomIntegerSliderController) ((Option<Integer>) value).controller()).min(), (int) ((CustomIntegerSliderController) ((Option<Integer>) value).controller()).max()));
                case "dev.isxander.yacl3.gui.controllers.cycling.EnumController":
                    //TODO: multiple enum typesv
//                    ((Option<RenderMode>) value).requestSet(RenderMode.values()[safeRandom(0, RenderMode.values().length - 1)]);
                    break;
                default:
                    LOGGER.warn("Skipping unrandomizable option: {}", ((Option<?>) value).controller().option().name());
            }
        } catch (UnsupportedOperationException e) {
            LOGGER.error("Error during randomization of option (SINGULAR)");
        }
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
        return (start + (end - start) * (1 - Math.exp(-((double) mc.getRenderTime() / 1000000000) * speed)));
    }

//    public static Vec3d screenSpaceToWorldSpace(double x, double y, double d, Matrix4f matrix4f) {
//        int displayHeight = CrystalChams.mc.getWindow().getScaledHeight();
//        int displayWidth = CrystalChams.mc.getWindow().getScaledWidth();
//        Vector3f target = new Vector3f();
//
//        Matrix4f matrixProj = new Matrix4f(RenderSystem.getProjectionMatrix());
//        Matrix4f matrixModel = new Matrix4f(RenderSystem.getModelViewMatrix());
//        Matrix4f lastWorldSpaceMatrix = new Matrix4f(matrix4f);
//        int[] lastViewport = new int[4];
//        GL11.glGetIntegerv(GL11.GL_VIEWPORT, lastViewport);
//
//        matrixProj.mul(matrixModel).mul(lastWorldSpaceMatrix).unproject((float) x / displayWidth * lastViewport[2], (float) (displayHeight - y) / displayHeight * lastViewport[3], (float) d, lastViewport, target);
//
//        return new Vec3d(target.x, target.y, target.z);
//    }

    public static void renderCustomBeam(float dx, float dy, float dz, float tickProgress, MatrixStack matrices, OrderedRenderCommandQueue queue, int light, int mode) {
        /*
        //TODO
        0 - non-preview
        1 - preview
        2 - funnier option
         */
//        if (mode == 2) {
//            renderVanillaCrystalBeam(dx, dy, dz, tickDelta, age, matrices, vertexConsumers, light);
//            return;
//        }
        float f = MathHelper.sqrt(dx * dx + dz * dz);
        float v = dx * dx + dy * dy + dz * dz;
        float g = MathHelper.sqrt(v);
        matrices.push();
        if (mode == 0) {
            matrices.translate(0.0F, 2.0F, 0.0F);
        }
        matrices.multiply(RotationAxis.POSITIVE_Y.rotation((float) (-Math.atan2(dz, dx)) - (float) (Math.PI / 2)));
        matrices.multiply(RotationAxis.POSITIVE_X.rotation((float) (-Math.atan2(f, dy)) - (float) (Math.PI / 2)));
//        VertexConsumer vertexConsumer = getLayer(vertexConsumers, ChamsConfig.o_beamRenderLayer.pendingValue(), ChamsConfig.o_beamCulling.pendingValue(), CRYSTAL_BEAM_TEXTURE);
        float h = -(tickProgress) * (0.01F * ChamsConfig.o_beamScrollSpeed.pendingValue());
        float i = MathHelper.sqrt(v) / 32.0F - (tickProgress) * (0.01F * ChamsConfig.o_beamScrollSpeed.pendingValue());
        queue.submitCustom(
                matrices,
                //TODO: has visual inconsistency with the shader
                getLayer(RenderMode.DEFAULT, ChamsConfig.o_beamCulling.pendingValue(), CRYSTAL_BEAM_TEXTURE),
                (matricesEntry, vertexConsumer) -> {
        float k = 0.0F;
        float l = 1F;
        float m = 0.0F;
                    for (int n = 1; n <= ChamsConfig.o_beamSides.pendingValue(); n++) {
                        float o = MathHelper.sin((float) n * (float) (Math.PI * 2) / ChamsConfig.o_beamSides.pendingValue());
                        float p = MathHelper.cos((float) n * (float) (Math.PI * 2) / ChamsConfig.o_beamSides.pendingValue());
                        float q = (float) n / ChamsConfig.o_beamSides.pendingValue();
                        Color rainbowCol1 = new Color(CrystalChams.getRainbow(ChamsConfig.o_beam1RainbowDelay.pendingValue(), ChamsConfig.o_beam1RainbowSpeed.pendingValue(), ChamsConfig.o_beam1RainbowSaturation.pendingValue(), ChamsConfig.o_beam1RainbowBrightness.pendingValue()));
                        Color rainbowCol2 = new Color(CrystalChams.getRainbow(ChamsConfig.o_beam2RainbowDelay.pendingValue(), ChamsConfig.o_beam2RainbowSpeed.pendingValue(), ChamsConfig.o_beam2RainbowSaturation.pendingValue(), ChamsConfig.o_beam2RainbowBrightness.pendingValue()));
                        int startCol = ChamsConfig.o_beam1Rainbow.pendingValue() ? ColorHelper.getArgb((int) (ChamsConfig.o_beam1Alpha.pendingValue() * (mode == 1 ? CrystalChams.beamProgress : 1) * 255F), rainbowCol1.getRed(), rainbowCol1.getGreen(), rainbowCol1.getBlue()) : ColorHelper.getArgb((int) (ChamsConfig.o_beam1Alpha.pendingValue() * (mode == 1 ? CrystalChams.beamProgress : 1) * 255F), ChamsConfig.o_beam1Color.pendingValue().getRed(), ChamsConfig.o_beam1Color.pendingValue().getGreen(), ChamsConfig.o_beam1Color.pendingValue().getBlue());
                        int endCol = ChamsConfig.o_beam2Rainbow.pendingValue() ? ColorHelper.getArgb((int) (ChamsConfig.o_beam2Alpha.pendingValue() * (mode == 1 ? CrystalChams.beamProgress : 1) * 255F), rainbowCol2.getRed(), rainbowCol2.getGreen(), rainbowCol2.getBlue()) : ColorHelper.getArgb((int) (ChamsConfig.o_beam2Alpha.pendingValue() * (mode == 1 ? CrystalChams.beamProgress : 1) * 255F), ChamsConfig.o_beam2Color.pendingValue().getRed(), ChamsConfig.o_beam2Color.pendingValue().getGreen(), ChamsConfig.o_beam2Color.pendingValue().getBlue());
                        vertexConsumer.vertex(matricesEntry, k * ChamsConfig.o_beam2Radius.pendingValue(), l * ChamsConfig.o_beam2Radius.pendingValue(), 0.0F).color(endCol).texture(m, h).overlay(OverlayTexture.DEFAULT_UV).light(packLight(ChamsConfig.o_beam2BlockLightLevel.pendingValue(), ChamsConfig.o_beam2SkyLightLevel.pendingValue(), light)).normal(matricesEntry, 0.0F, -1.0F, 0.0F);
                        vertexConsumer.vertex(matricesEntry, k * ChamsConfig.o_beam1Radius.pendingValue(), l * ChamsConfig.o_beam1Radius.pendingValue(), g).color(startCol).texture(m, i).overlay(OverlayTexture.DEFAULT_UV).light(packLight(ChamsConfig.o_beam1BlockLightLevel.pendingValue(), ChamsConfig.o_beam1SkyLightLevel.pendingValue(), light)).normal(matricesEntry, 0.0F, -1.0F, 0.0F);
                        vertexConsumer.vertex(matricesEntry, o * ChamsConfig.o_beam1Radius.pendingValue(), p * ChamsConfig.o_beam1Radius.pendingValue(), g).color(startCol).texture(q, i).overlay(OverlayTexture.DEFAULT_UV).light(packLight(ChamsConfig.o_beam1BlockLightLevel.pendingValue(), ChamsConfig.o_beam1SkyLightLevel.pendingValue(), light)).normal(matricesEntry, 0.0F, -1.0F, 0.0F);
                        vertexConsumer.vertex(matricesEntry, o * ChamsConfig.o_beam2Radius.pendingValue(), p * ChamsConfig.o_beam2Radius.pendingValue(), 0.0F).color(endCol).texture(q, h).overlay(OverlayTexture.DEFAULT_UV).light(packLight(ChamsConfig.o_beam2BlockLightLevel.pendingValue(), ChamsConfig.o_beam2SkyLightLevel.pendingValue(), light)).normal(matricesEntry, 0.0F, -1.0F, 0.0F);
                        k = o;
                        l = p;
                        m = q;
                    }
                }
        );
        matrices.pop();
    }

    public static int packLight(int block, int sky, int light) {
        if (block == -1) {
            block = LightmapTextureManager.getBlockLightCoordinates(light);
        }
        if (sky == -1) {
            sky = LightmapTextureManager.getSkyLightCoordinates(light);
        }
        return block << 4 | sky << 20;
    }

    private static void renderVanillaCrystalBeam(float dx, float dy, float dz, float tickDelta, int age, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        float f = MathHelper.sqrt(dx * dx + dz * dz);
        float value = dx * dx + dy * dy + dz * dz;
        float g = MathHelper.sqrt(value);
        matrices.push();
        matrices.translate(0.0F, 2.0F, 0.0F);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotation((float) (-Math.atan2(dz, dx)) - (float) (Math.PI / 2)));
        matrices.multiply(RotationAxis.POSITIVE_X.rotation((float) (-Math.atan2(f, dy)) - (float) (Math.PI / 2)));
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayers.entitySmoothCutout(CRYSTAL_BEAM_TEXTURE));
        float h = 0.0F - (age + tickDelta) * 0.01F;
        float i = MathHelper.sqrt(value) / 32.0F - (age + tickDelta) * 0.01F;
        int j = 8;
        float k = 0.0F;
        float l = 0.75F;
        float m = 0.0F;
        MatrixStack.Entry entry = matrices.peek();

        for (int n = 1; n <= 8; n++) {
            float o = MathHelper.sin(n * (float) (Math.PI * 2) / 8.0F) * 0.75F;
            float p = MathHelper.cos(n * (float) (Math.PI * 2) / 8.0F) * 0.75F;
            float q = n / 8.0F;
            vertexConsumer.vertex(entry, k * 0.2F, l * 0.2F, 0.0F)
                    .color(Colors.BLACK)
                    .texture(m, h)
                    .overlay(OverlayTexture.DEFAULT_UV)
                    .light(light)
                    .normal(entry, 0.0F, -1.0F, 0.0F);
            vertexConsumer.vertex(entry, k, l, g).color(Colors.WHITE).texture(m, i).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(entry, 0.0F, -1.0F, 0.0F);
            vertexConsumer.vertex(entry, o, p, g).color(Colors.WHITE).texture(q, i).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(entry, 0.0F, -1.0F, 0.0F);
            vertexConsumer.vertex(entry, o * 0.2F, p * 0.2F, 0.0F)
                    .color(Colors.BLACK)
                    .texture(q, h)
                    .overlay(OverlayTexture.DEFAULT_UV)
                    .light(light)
                    .normal(entry, 0.0F, -1.0F, 0.0F);
            k = o;
            l = p;
            m = q;
        }

        matrices.pop();
    }

    public enum BaseRenderMode implements NameableEnum {
        ALWAYS,
        DEFAULT,
        NEVER;


        @Override
        public Text getDisplayName() {
            return switch (this) {
                case ALWAYS -> Text.translatable("config.baseMode.always");
                case DEFAULT -> Text.translatable("config.baseMode.default");
                case NEVER -> Text.translatable("config.baseMode.never");
            };
        }
    }
}
