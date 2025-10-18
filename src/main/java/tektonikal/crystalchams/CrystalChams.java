package tektonikal.crystalchams;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.isxander.yacl3.api.Controller;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.StateManager;
import dev.isxander.yacl3.api.controller.ControllerBuilder;
import dev.isxander.yacl3.api.controller.EnumControllerBuilder;
import dev.isxander.yacl3.api.controller.ValueFormatter;
import dev.isxander.yacl3.gui.ValueFormatters;
import dev.isxander.yacl3.gui.YACLScreen;
import dev.isxander.yacl3.gui.controllers.PopupControllerScreen;
import dev.isxander.yacl3.gui.controllers.slider.FloatSliderController;
import dev.isxander.yacl3.gui.controllers.slider.IntegerSliderController;
import dev.isxander.yacl3.impl.ListOptionEntryImpl;
import dev.isxander.yacl3.impl.ListOptionImpl;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.CoreShaderRegistrationCallback;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.EndPortalBlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.*;
import org.lwjgl.opengl.GL11;
import tektonikal.crystalchams.annotation.Updatable;
import tektonikal.crystalchams.config.*;
import tektonikal.crystalchams.mixin.CategoryTabAccessor;
import tektonikal.crystalchams.mixin.PopupControllerScreenAccessor;
import tektonikal.crystalchams.stupidfuckingboilerplate.CustomFloatSliderController;
import tektonikal.crystalchams.stupidfuckingboilerplate.CustomFloatSliderControllerBuilder;
import tektonikal.crystalchams.stupidfuckingboilerplate.CustomTickBoxControllerBuilder;
import tektonikal.crystalchams.util.Easings;

import java.awt.*;
import java.lang.Math;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.OptionalDouble;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static net.minecraft.client.render.RenderPhase.*;
import static net.minecraft.client.render.entity.EnderDragonEntityRenderer.CRYSTAL_BEAM_TEXTURE;

public class CrystalChams implements ModInitializer {
    public static final Function<Double, RenderLayer.MultiPhase> CUSTOM_DEBUG_LINE_STRIP = Util.memoize((lineWidth) -> RenderLayer.of("custom_debug_line_strip", VertexFormats.POSITION_COLOR_LIGHT, VertexFormat.DrawMode.DEBUG_LINE_STRIP, 1536, RenderLayer.MultiPhaseParameters.builder().program(POSITION_COLOR_LIGHTMAP_PROGRAM).lineWidth(new RenderPhase.LineWidth(OptionalDouble.of(lineWidth))).transparency(TRANSLUCENT_TRANSPARENCY).cull(DISABLE_CULLING).lightmap(ENABLE_LIGHTMAP).build(false)));
    public static ShaderProgram ENTITY_TRANSLUCENT_NOTEX;
    public static ShaderProgram END_PORTAL_TEX;
    public static ShaderProgram CUSTOM_IMAGE;
    public static final float PREVIEW_EASING_SPEED = 12.5F;
    public static final ValueFormatter<Float> PERCENT_FORMATTER = value -> Text.of(String.format("%.1f", value) + "x");
    public static final ValueFormatter<Float> BLOCKS_FORMATTER = val -> Text.of(String.format("%.1f", val).replace(".0", "") + (Math.abs(val) == 1 ? " block" : " blocks"));
    public static final ValueFormatter<Float> SECONDS_FORMATTER = val -> Text.of(String.format("%.1f", val).replace(".0", "") + (Math.abs(val) == 1 ? " second" : " seconds"));
    public static final Function<Option<Float>, ControllerBuilder<Float>> PERCENT = floatOption -> CustomFloatSliderControllerBuilder.create(floatOption).range(0f, 10f).step(0.1f).formatValue(PERCENT_FORMATTER);
    public static final BiFunction<Identifier, Boolean, RenderLayer> CUSTOM_ENTITY_NOTEX = Util.memoize((texture, shouldCull) -> {
        RenderLayer.MultiPhaseParameters multiPhaseParameters = RenderLayer.MultiPhaseParameters.builder().program(new RenderPhase.ShaderProgram(() -> ENTITY_TRANSLUCENT_NOTEX)).texture(new RenderPhase.Texture(texture, false, false)).transparency(TRANSLUCENT_TRANSPARENCY).cull(shouldCull ? ENABLE_CULLING : DISABLE_CULLING).lightmap(ENABLE_LIGHTMAP).overlay(ENABLE_OVERLAY_COLOR).build(true);
        return RenderLayer.MultiPhase.of("custom_entity_translucent", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, VertexFormat.DrawMode.QUADS, 1536, true, true, multiPhaseParameters);
    });
    public static final BiFunction<Identifier, Boolean, RenderLayer> CUSTOM_END_GATEWAY = Util.memoize((texture, shouldCull) -> RenderLayer.of("custom_end_gateway", VertexFormats.POSITION_TEXTURE_COLOR, VertexFormat.DrawMode.QUADS, 1536, false, true, RenderLayer.MultiPhaseParameters.builder().program(new RenderPhase.ShaderProgram(() -> END_PORTAL_TEX)).texture(Textures.create().add(EndPortalBlockEntityRenderer.SKY_TEXTURE, false, false).add(EndPortalBlockEntityRenderer.PORTAL_TEXTURE, false, false).add(texture, false, false).build()).transparency(TRANSLUCENT_TRANSPARENCY).cull(shouldCull ? ENABLE_CULLING : DISABLE_CULLING).build(true)));
    public static final BiFunction<Identifier, Boolean, RenderLayer> CUSTOM_IMAGE_FUNC = Util.memoize((texture, shouldCull) -> RenderLayer.of("custom_end_gateway", VertexFormats.POSITION_TEXTURE_COLOR_LIGHT, VertexFormat.DrawMode.QUADS, 1536, false, true, RenderLayer.MultiPhaseParameters.builder().program(new RenderPhase.ShaderProgram(() -> CUSTOM_IMAGE)).texture(Textures.create().add(Identifier.of("crystalchams:custom/image.png"), false, false).add(texture, false, false).build()).transparency(TRANSLUCENT_TRANSPARENCY).cull(shouldCull ? ENABLE_CULLING : DISABLE_CULLING).build(true)));
    public static final BiFunction<Identifier, Boolean, RenderLayer> CUSTOM_NORMAL = Util.memoize((texture, shouldCull) -> {
        RenderLayer.MultiPhaseParameters multiPhaseParameters = RenderLayer.MultiPhaseParameters.builder().program(ENTITY_TRANSLUCENT_PROGRAM).texture(new Texture(texture, false, false)).transparency(TRANSLUCENT_TRANSPARENCY).cull(shouldCull ? ENABLE_CULLING : DISABLE_CULLING).lightmap(ENABLE_LIGHTMAP).overlay(ENABLE_OVERLAY_COLOR).build(true);
        return RenderLayer.of("custom_entity_translucent", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, VertexFormat.DrawMode.QUADS, 1536, true, true, multiPhaseParameters);
    });
    public static int hoveredIndex;
    public static EndCrystalEntity previewCrystalEntity = new EndCrystalEntity(MinecraftClient.getInstance().world, 0.5, 0, 0);
    public static float previewScaleSmoothed = ChamsConfig.CONFIG.instance().previewScale;
    public static float beamProgress;

    public static float getYOffset(float age, float offset, float bounceSpeed, float bounceHeight, float tickDelay) {
        //?????
        float g = (MathHelper.sin((age + tickDelay) % ((float) (Math.PI * 2F) / (0.2F * bounceSpeed)) * (0.2F * bounceSpeed)) / 2.0F) + 0.5F;
        g = (g * g + g) * (0.4F * bounceHeight);
        return g - 1.4F + offset;
    }

    public static int getRainbow(float delay, float speed, float saturation, float brightness) {
        double rainbowState = Math.ceil((System.currentTimeMillis() + (int) (delay * 1000))) * speed / 20;
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

    public static boolean isThisMyScreen(Screen screen) {
        if (screen instanceof YACLScreen && (((YACLScreen) screen).config.title().equals(Text.of("Custom End Crystals")))) {
            return true;
        }
        return screen instanceof PopupControllerScreen && ((PopupControllerScreenAccessor) (screen)).getBackgroundYaclScreen().config.title().equals(Text.of("Custom End Crystals"));
    }

    public static boolean isThisMyScreen() {
        return isThisMyScreen(MinecraftClient.getInstance().currentScreen);
    }

    public static EvilOption<Boolean> createBooleanOption(String name, String description, StateManager<Boolean> stateManager) {
        return EvilOption.<Boolean>createBuilder().name(Text.of(name)).stateManager(stateManager).description(OptionDescription.of(Text.of(description))).controller(CustomTickBoxControllerBuilder::new).build();
    }

    public static EvilOption<Float> createFloatOptionSeconds(String name, String description, StateManager<Float> stateManager) {
        return EvilOption.<Float>createBuilder().name(Text.of(name)).description(OptionDescription.of(Text.of(description))).stateManager(stateManager).controller(floatOption -> CustomFloatSliderControllerBuilder.create(floatOption).range(-2.5f, 2.5f).step(0.1f).formatValue(SECONDS_FORMATTER)).build();
    }

    public static EvilOption<Float> createFloatOptionPercent(String name, String description, StateManager<Float> stateManager) {
        return EvilOption.<Float>createBuilder().name(Text.of(name)).description(OptionDescription.of(Text.of(description))).stateManager(stateManager).controller(PERCENT).build();
    }

    public static EvilOption<Easings> createEasingOption(String name, String description, StateManager<Easings> stateManager) {
        return EvilOption.<Easings>createBuilder().name(Text.of(name)).description(OptionDescription.of(Text.of(description))).stateManager(stateManager).controller(easingsOption -> EnumControllerBuilder.create(easingsOption).enumClass(Easings.class)).build();
    }

    @Override
    public void onInitialize() {
        ChamsConfig.CONFIG.load();
        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (isThisMyScreen(screen)) {
                ScreenEvents.afterRender(screen).register((screen1, drawContext, mouseX, mouseY, tickDelta) -> {
                    YACLScreen yaclScreen;
                    ScreenRect rightPaneDim;
                    if (screen instanceof PopupControllerScreen) {
                        rightPaneDim = ((CategoryTabAccessor) ((PopupControllerScreenAccessor) screen).getBackgroundYaclScreen().tabManager.getCurrentTab()).rightPaneDim();
                        yaclScreen = ((PopupControllerScreenAccessor) screen).getBackgroundYaclScreen();
                    } else {
                        rightPaneDim = ((CategoryTabAccessor) ((YACLScreen) screen).tabManager.getCurrentTab()).rightPaneDim();
                        yaclScreen = (YACLScreen) screen;
                    }
                    RenderSystem.enableBlend();
                    DiffuseLighting.method_34742();
                    //TODO: fuck around with these numbers a bit more later
                    CrystalChams.crystalRotX = (float) CrystalChams.ease(CrystalChams.crystalRotX, Math.atan((((rightPaneDim.getLeft() + rightPaneDim.getRight()) / 2F) - (MinecraftClient.getInstance().mouse.getX() * MinecraftClient.getInstance().getWindow().getScaledWidth() / MinecraftClient.getInstance().getWindow().getWidth())) / 40F), 15F);
                    CrystalChams.crystalRotY = (float) CrystalChams.ease(CrystalChams.crystalRotY, Math.atan((((rightPaneDim.getTop() + rightPaneDim.getBottom()) / 2F) - (MinecraftClient.getInstance().mouse.getY() * MinecraftClient.getInstance().getWindow().getScaledHeight() / MinecraftClient.getInstance().getWindow().getHeight())) / 40F), 15F);
                    CrystalChams.previewScaleSmoothed = (float) CrystalChams.ease(CrystalChams.previewScaleSmoothed, ChamsConfig.o_previewScale.pendingValue(), 10F);
                    if (Float.isNaN(CrystalChams.crystalRotX)) {
                        CrystalChams.crystalRotX = 0;
                    }
                    if (Float.isNaN(CrystalChams.crystalRotY)) {
                        CrystalChams.crystalRotY = 0;
                    }
                    if ((yaclScreen).tabManager.getCurrentTab().getTitle().equals(Text.of("Beam"))) {
                        CrystalChams.beamProgress = (float) CrystalChams.ease(CrystalChams.beamProgress, 1, 5);
                    } else {
                        CrystalChams.beamProgress = (float) CrystalChams.ease(CrystalChams.beamProgress, 0, 5);
                    }
                    drawContext.getMatrices().push();

                    drawContext.getMatrices().translate((rightPaneDim.getLeft() + rightPaneDim.getRight()) / 2F, (rightPaneDim.getTop() + rightPaneDim.getBottom()) / 2F, 500.0);
                    float scaleFac = Math.min(MinecraftClient.getInstance().getWindow().getScaledWidth(), MinecraftClient.getInstance().getWindow().getScaledHeight()) / 7.5F * previewScaleSmoothed;
                    drawContext.getMatrices().scale(scaleFac, scaleFac, -(scaleFac));
                    drawContext.getMatrices().multiply(RotationAxis.POSITIVE_Z.rotation((float) Math.PI));
                    drawContext.getMatrices().multiply(RotationAxis.POSITIVE_X.rotation(CrystalChams.crystalRotY * 25.0F * (float) (Math.PI / 180.0)));
                    drawContext.getMatrices().multiply(RotationAxis.NEGATIVE_Y.rotation(CrystalChams.crystalRotX * 35.0F * (float) (Math.PI / 180.0)));
                    MinecraftClient.getInstance().getEntityRenderDispatcher().getRenderer(CrystalChams.previewCrystalEntity).render(CrystalChams.previewCrystalEntity, 0, ((RenderTickCounter.Dynamic) MinecraftClient.getInstance().getRenderTickCounter()).tickDelta, drawContext.getMatrices(), MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers(), 255);
                    drawContext.getMatrices().pop();
                    if (ChamsConfig.o_renderBeam.pendingValue()) {
                        drawContext.getMatrices().push();
                        drawContext.getMatrices().scale(scaleFac, scaleFac, -scaleFac);
                        Vec3d vec = CrystalChams.screenSpaceToWorldSpace(MinecraftClient.getInstance().mouse.getX(), MinecraftClient.getInstance().mouse.getY(), 0, drawContext.getMatrices().peek().getPositionMatrix()).multiply(1 / MinecraftClient.getInstance().getWindow().getScaleFactor());
//                        drawContext.fill((int) vec.x, (int) vec.y, (int) (vec.x + 1), (int) (vec.y + 1), 0x80FFFFFF);
                        drawContext.getMatrices().translate(vec.x, vec.y, vec.z);
                        //i wanted this to extend out from the crystal towards the cursor, but maybe i'll do it later
                        CrystalChams.renderBeamSpecial((float) (-vec.x + ((rightPaneDim.getLeft() + rightPaneDim.getRight()) / 2F / scaleFac)), (float) -vec.y + ((rightPaneDim.getTop() + rightPaneDim.getBottom()) / 2F / scaleFac) - CrystalChams.getYOffset(CrystalChams.previewCrystalEntity.endCrystalAge + ((RenderTickCounter.Dynamic) MinecraftClient.getInstance().getRenderTickCounter()).tickDelta, ChamsConfig.o_coreOffset.pendingValue(), ChamsConfig.o_coreBounceSpeed.pendingValue(), ChamsConfig.o_coreBounceHeight.pendingValue(), ChamsConfig.o_coreTickDelay.pendingValue()) - 2, 2.5F, ((RenderTickCounter.Dynamic) MinecraftClient.getInstance().getRenderTickCounter()).tickDelta, CrystalChams.previewCrystalEntity.endCrystalAge, drawContext.getMatrices(), MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers(), 255);
                        drawContext.getMatrices().pop();
                    }
                    drawContext.draw();
                    RenderSystem.disableBlend();
                    DiffuseLighting.enableGuiDepthLighting();
                });
            }
        });
        CoreShaderRegistrationCallback.EVENT.register(context -> context.register(id("crystalchams_entity_translucent_notex"), VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, shaderProgram -> ENTITY_TRANSLUCENT_NOTEX = shaderProgram));
        CoreShaderRegistrationCallback.EVENT.register(context -> context.register(id("crystalchams_end_gateway_tex"), VertexFormats.POSITION_TEXTURE_COLOR, shaderProgram -> END_PORTAL_TEX = shaderProgram));
        CoreShaderRegistrationCallback.EVENT.register(context -> context.register(id("crystalchams_image"), VertexFormats.POSITION_TEXTURE, shaderProgram -> CUSTOM_IMAGE = shaderProgram));

        armSecuritySystem();
        unleashHell();
        ChamsConfig.o_baseScale.linkOptions(ChamsConfig.o_coreScale);
        ChamsConfig.o_baseColor.linkOptions(ChamsConfig.o_coreColor, ChamsConfig.o_beam1Color, ChamsConfig.o_beam2Color);
        ChamsConfig.o_baseAlpha.linkOptions(ChamsConfig.o_coreAlpha, ChamsConfig.o_beam1Alpha, ChamsConfig.o_beam2Alpha);
        ChamsConfig.o_baseLightLevel.linkOptions(ChamsConfig.o_coreLightLevel, ChamsConfig.o_beam1LightLevel, ChamsConfig.o_beam2LightLevel);
        ChamsConfig.o_baseRenderLayer.linkOptions(ChamsConfig.o_coreRenderLayer, ChamsConfig.o_beamRenderLayer);
        ChamsConfig.o_baseRainbow.linkOptions(ChamsConfig.o_coreRainbow, ChamsConfig.o_beam1Rainbow, ChamsConfig.o_beam2Rainbow);
        ChamsConfig.o_baseRainbowSpeed.linkOptions(ChamsConfig.o_coreRainbowSpeed, ChamsConfig.o_beam1RainbowSpeed, ChamsConfig.o_beam2RainbowSpeed);
        ChamsConfig.o_baseRainbowDelay.linkOptions(ChamsConfig.o_coreRainbowDelay, ChamsConfig.o_beam1RainbowDelay, ChamsConfig.o_beam2RainbowDelay);
        ChamsConfig.o_baseRainbowBrightness.linkOptions(ChamsConfig.o_coreRainbowBrightness, ChamsConfig.o_beam1RainbowBrightness, ChamsConfig.o_beam2RainbowBrightness);
        ChamsConfig.o_baseRainbowSaturation.linkOptions(ChamsConfig.o_coreRainbowSaturation, ChamsConfig.o_beam1RainbowSaturation, ChamsConfig.o_beam2RainbowSaturation);
        //don't link vertical offset of crystal with base because why the fuck would you do that you fucking idiot fuck you
        ChamsConfig.o_beam1Radius.linkOptions(ChamsConfig.o_beam2Radius);
        ChamsConfig.o_baseCulling.linkOptions(ChamsConfig.o_coreCulling, ChamsConfig.o_beamCulling);
        //uhhh i can't link the base with the other options, future me will deal with this
        ChamsConfig.o_renderBeam.linkOptions(ChamsConfig.o_renderCore);
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
            if(field.getName().equals("o_frameList")){
                ChamsConfig.o_frameList.options().forEach(listOptionEntry -> {
                    ModelPartController controller = (ModelPartController) ((ListOptionEntryImpl.EntryController) (listOptionEntry.controller())).controller();
                    Arrays.stream(ModelPartController.class.getDeclaredFields()).filter(field1 -> field1.getName().startsWith("o_")).forEach(field1 -> {
                        try {
                            randomizeOption(field1.get(controller));
                        } catch (IllegalAccessException e) {
                            System.out.println("QHAR???");
                        } catch (NullPointerException e) {
                            System.out.println("you dumbass. you forgot about " + field1.getName());
                        }
                    });
                });
                return;
            }
            try {
                randomizeOption(field.get(null));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
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
                    case "tektonikal.crystalchams.config.CustomTickBoxController":
                        ((Option<Boolean>) value).requestSet(Math.random() > 0.5);
                        break;
                    case "dev.isxander.yacl3.gui.controllers.ColorController":
                        ((Option<Color>) value).requestSet(new Color((int) (Math.random() * 0x1000000)));
                        break;
                    case "dev.isxander.yacl3.gui.controllers.slider.FloatSliderController":
                        ((Option<Float>) value).requestSet(safeRandom((float) ((FloatSliderController) ((Option<Float>) value).controller()).min(), (float) ((FloatSliderController) ((Option<Float>) value).controller()).max()));
                        break;
                    case "tektonikal.crystalchams.stupidfuckingboilerplate.CustomFloatSliderController":
                        ((Option<Float>) value).requestSet(safeRandom((float) ((CustomFloatSliderController) ((Option<Float>) value).controller()).min(), (float) ((CustomFloatSliderController) ((Option<Float>) value).controller()).max()));
                        break;
                    case "dev.isxander.yacl3.gui.controllers.slider.IntegerSliderController":
                        ((Option<Integer>) value).requestSet(safeRandom((int) ((IntegerSliderController) ((Option<Integer>) value).controller()).min(), (int) ((IntegerSliderController) ((Option<Integer>) value).controller()).max()));
                        break;
                    case "dev.isxander.yacl3.gui.controllers.cycling.EnumController":
                        ((Option<RenderMode>) value).requestSet(RenderMode.values()[safeRandom(0, RenderMode.values().length - 1)]);
                        break;
                    default:
                        System.out.println(((Option<?>) value).controller().getClass().getCanonicalName());
                }
        } catch (UnsupportedOperationException e) {
            System.out.println("SOMETHING HAS GONE TERRIBLY WRONG!!!");
            e.printStackTrace();
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
        return (start + (end - start) * (1 - Math.exp(-(1.0F / MinecraftClient.getInstance().getCurrentFps()) * speed)));
    }

    public static Vec3d screenSpaceToWorldSpace(double x, double y, double d, Matrix4f matrix4f) {
        int displayHeight = MinecraftClient.getInstance().getWindow().getScaledHeight();
        int displayWidth = MinecraftClient.getInstance().getWindow().getScaledWidth();
        Vector3f target = new Vector3f();

        Matrix4f matrixProj = new Matrix4f(RenderSystem.getProjectionMatrix());
        Matrix4f matrixModel = new Matrix4f(RenderSystem.getModelViewMatrix());
        Matrix4f lastWorldSpaceMatrix = new Matrix4f(matrix4f);
        int[] lastViewport = new int[4];
        GL11.glGetIntegerv(GL11.GL_VIEWPORT, lastViewport);

        matrixProj.mul(matrixModel).mul(lastWorldSpaceMatrix).unproject((float) x / displayWidth * lastViewport[2], (float) (displayHeight - y) / displayHeight * lastViewport[3], (float) d, lastViewport, target);

        return new Vec3d(target.x, target.y, target.z);
    }

    //TODO: this is just for the preview. i don't remember why. make it use real-time options
    public static void renderBeamSpecial(float dx, float dy, float dz, float tickDelta, int age, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        float f = MathHelper.sqrt(dx * dx + dz * dz);
        float v = dx * dx + dy * dy + dz * dz;
        float g = MathHelper.sqrt(v);
        matrices.push();
        matrices.multiply(RotationAxis.POSITIVE_Y.rotation((float) (-Math.atan2(dz, dx)) - (float) (Math.PI / 2)));
        matrices.multiply(RotationAxis.POSITIVE_X.rotation((float) (-Math.atan2(f, dy)) - (float) (Math.PI / 2)));
        VertexConsumer vertexConsumer = getLayer(vertexConsumers, ChamsConfig.CONFIG.instance().beamRenderMode, false, CRYSTAL_BEAM_TEXTURE);
        float h = -(tickDelta + age) * (0.01F * ChamsConfig.CONFIG.instance().beamScrollSpeed);
        float i = MathHelper.sqrt(v) / 32.0F - (tickDelta + age) * (0.01F * ChamsConfig.CONFIG.instance().beamScrollSpeed);
        float k = 0.0F;
        float l = 1F;
        float m = 0.0F;
        MatrixStack.Entry entry = matrices.peek();

        for (int n = 1; n <= ChamsConfig.CONFIG.instance().beamSides; n++) {
            float o = MathHelper.sin((float) n * (float) (Math.PI * 2) / ChamsConfig.CONFIG.instance().beamSides);
            float p = MathHelper.cos((float) n * (float) (Math.PI * 2) / ChamsConfig.CONFIG.instance().beamSides);
            float q = (float) n / ChamsConfig.CONFIG.instance().beamSides;
            Color rainbowCol1 = new Color(CrystalChams.getRainbow(ChamsConfig.CONFIG.instance().beam1RainbowDelay, ChamsConfig.CONFIG.instance().beam1RainbowSpeed, ChamsConfig.CONFIG.instance().beam1RainbowSaturation, ChamsConfig.CONFIG.instance().beam1RainbowBrightness));
            Color rainbowCol2 = new Color(CrystalChams.getRainbow(ChamsConfig.CONFIG.instance().beam2RainbowDelay, ChamsConfig.CONFIG.instance().beam2RainbowSpeed, ChamsConfig.CONFIG.instance().beam2RainbowSaturation, ChamsConfig.CONFIG.instance().beam2RainbowBrightness));
            int startCol = ChamsConfig.CONFIG.instance().beam1Rainbow ? ColorHelper.Argb.getArgb((int) (ChamsConfig.CONFIG.instance().beam1Alpha * CrystalChams.beamProgress * 255F), rainbowCol1.getRed(), rainbowCol1.getGreen(), rainbowCol1.getBlue()) : ColorHelper.Argb.getArgb((int) (ChamsConfig.CONFIG.instance().beam1Alpha * CrystalChams.beamProgress * 255F), ChamsConfig.CONFIG.instance().beam1Color.getRed(), ChamsConfig.CONFIG.instance().beam1Color.getGreen(), ChamsConfig.CONFIG.instance().beam1Color.getBlue());
            int endCol = ChamsConfig.CONFIG.instance().beam2Rainbow ? ColorHelper.Argb.getArgb((int) (ChamsConfig.CONFIG.instance().beam2Alpha * CrystalChams.beamProgress * 255F), rainbowCol2.getRed(), rainbowCol2.getGreen(), rainbowCol2.getBlue()) : ColorHelper.Argb.getArgb((int) (ChamsConfig.CONFIG.instance().beam2Alpha * CrystalChams.beamProgress * 255F), ChamsConfig.CONFIG.instance().beam2Color.getRed(), ChamsConfig.CONFIG.instance().beam2Color.getGreen(), ChamsConfig.CONFIG.instance().beam2Color.getBlue());
            vertexConsumer.vertex(entry, k * ChamsConfig.CONFIG.instance().beam2Radius, l * ChamsConfig.CONFIG.instance().beam2Radius, 0.0F).color(endCol).texture(m, h).overlay(OverlayTexture.DEFAULT_UV).light(ChamsConfig.CONFIG.instance().beam2LightLevel != -1 ? ChamsConfig.CONFIG.instance().beam2LightLevel : light).normal(entry, 0.0F, -1.0F, 0.0F);
            vertexConsumer.vertex(entry, k * ChamsConfig.CONFIG.instance().beam1Radius, l * ChamsConfig.CONFIG.instance().beam1Radius, g).color(startCol).texture(m, i).overlay(OverlayTexture.DEFAULT_UV).light(ChamsConfig.CONFIG.instance().beam1LightLevel != -1 ? ChamsConfig.CONFIG.instance().beam1LightLevel : light).normal(entry, 0.0F, -1.0F, 0.0F);
            vertexConsumer.vertex(entry, o * ChamsConfig.CONFIG.instance().beam1Radius, p * ChamsConfig.CONFIG.instance().beam1Radius, g).color(startCol).texture(q, i).overlay(OverlayTexture.DEFAULT_UV).light(ChamsConfig.CONFIG.instance().beam1LightLevel != -1 ? ChamsConfig.CONFIG.instance().beam1LightLevel : light).normal(entry, 0.0F, -1.0F, 0.0F);
            vertexConsumer.vertex(entry, o * ChamsConfig.CONFIG.instance().beam2Radius, p * ChamsConfig.CONFIG.instance().beam2Radius, 0.0F).color(endCol).texture(q, h).overlay(OverlayTexture.DEFAULT_UV).light(ChamsConfig.CONFIG.instance().beam2LightLevel != -1 ? ChamsConfig.CONFIG.instance().beam2LightLevel : light).normal(entry, 0.0F, -1.0F, 0.0F);
            k = o;
            l = p;
            m = q;
        }
        matrices.pop();
    }
}
