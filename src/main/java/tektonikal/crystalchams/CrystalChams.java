package tektonikal.crystalchams;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.StateManager;
import dev.isxander.yacl3.api.controller.ControllerBuilder;
import dev.isxander.yacl3.api.controller.EnumControllerBuilder;
import dev.isxander.yacl3.api.controller.ValueFormatter;
import dev.isxander.yacl3.gui.YACLScreen;
import dev.isxander.yacl3.gui.controllers.PopupControllerScreen;
import dev.isxander.yacl3.gui.controllers.slider.FloatSliderController;
import dev.isxander.yacl3.gui.controllers.slider.IntegerSliderController;
import dev.isxander.yacl3.impl.ListOptionEntryImpl;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.CoreShaderRegistrationCallback;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.navigation.NavigationAxis;
import net.minecraft.client.gui.screen.Screen;
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
import java.awt.List;
import java.lang.Math;
import java.util.*;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Function;

import static net.minecraft.client.render.RenderPhase.*;
import static net.minecraft.client.render.entity.EnderDragonEntityRenderer.CRYSTAL_BEAM_TEXTURE;

public class CrystalChams implements ModInitializer {
    public static final MinecraftClient mc = MinecraftClient.getInstance();
    public static final Function<Double, RenderLayer.MultiPhase> CUSTOM_DEBUG_LINE_STRIP = Util.memoize((lineWidth) -> RenderLayer.of("custom_debug_line_strip", VertexFormats.POSITION_COLOR_LIGHT, VertexFormat.DrawMode.DEBUG_LINE_STRIP, 1536, RenderLayer.MultiPhaseParameters.builder().program(POSITION_COLOR_LIGHTMAP_PROGRAM).lineWidth(new RenderPhase.LineWidth(OptionalDouble.of(lineWidth))).transparency(TRANSLUCENT_TRANSPARENCY).cull(DISABLE_CULLING).lightmap(ENABLE_LIGHTMAP).build(false)));
    public static ShaderProgram ENTITY_TRANSLUCENT_NOTEX;
    public static ShaderProgram END_PORTAL_TEX;
    public static ShaderProgram CUSTOM_IMAGE;
    public static final float PREVIEW_EASING_SPEED = 12.5F;
    public static final EnumMap<OptionGroups, ArrayList<EvilOption>> optionGroups = new  EnumMap<>(OptionGroups.class);
    public static final ValueFormatter<Integer> LIGHT_FORMATTER = value -> Text.of(value == -1 ? "Use World Light" : value + "");
    public static final ValueFormatter<Float> PERCENT_FORMATTER = value -> Text.of((int) (value * 100) + "%");
    public static final ValueFormatter<Float> MULTIPLIER_FORMATTER = val -> Text.of(String.format("%.2f", val) + "x");
    public static final ValueFormatter<Float> MULTIPLIER_FORMATTER_ONE_PLACE = val -> Text.of(String.format("%.1f", val) + "x");
    public static final ValueFormatter<Float> BLOCKS_FORMATTER = val -> Text.of(String.format("%.1f", val).replace(".0", "") + (Math.abs(val) == 1 ? " block" : " blocks"));
    public static final ValueFormatter<Float> BLOCKS_FORMATTER_TWO_PLACES = val -> Text.of(String.format("%.2f", val).replace(".0", "") + (Math.abs(val) == 1 ? " block" : " blocks"));
    public static final ValueFormatter<Float> SECONDS_FORMATTER = val -> Text.of(String.format("%.1f", val).replace(".0", "") + (Math.abs(val) == 1 ? " second" : " seconds"));
    public static final Function<Option<Float>, ControllerBuilder<Float>> PERCENT = floatOption -> CustomFloatSliderControllerBuilder.create(floatOption).range(0f, 1f).step(0.01f).formatValue(PERCENT_FORMATTER);
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
    public static EndCrystalEntity previewCrystalEntity = new EndCrystalEntity(mc.world, 0.5, 0, 0);
    public static float previewScaleSmoothed = ChamsConfig.CONFIG.instance().previewScale;
    public static float beamProgress;

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

    public static VertexConsumer getLayer(VertexConsumerProvider vcp, RenderMode layer, boolean cull, Identifier texture) {
        if (layer.getBiFunction() != null) {
            return vcp.getBuffer(layer.getBiFunction().apply(texture, cull));
        } else {
            return vcp.getBuffer(layer.getFunction().apply(1.0));
        }
    }

    public static void fillFloat(DrawContext context, float x1, float y1, float x2, float y2, int color) {
        Matrix4f matrix4f = context.getMatrices().peek().getPositionMatrix();
        VertexConsumer vertexConsumer = context.getVertexConsumers().getBuffer(RenderLayer.getGui());
        vertexConsumer.vertex(matrix4f, x1, y1, 0).color(color);
        vertexConsumer.vertex(matrix4f, x1, y2, 0).color(color);
        vertexConsumer.vertex(matrix4f, x2, y2, 0).color(color);
        vertexConsumer.vertex(matrix4f, x2, y1, 0).color(color);
        context.draw();
    }

    public static float crystalRotX;
    public static float crystalRotY;

    public static boolean isThisMyScreen(Screen screen) {
        if (screen instanceof YACLScreen && (((YACLScreen) screen).config.title().equals(Text.of("Custom End Crystals")))) {
            return true;
        }
        return screen instanceof PopupControllerScreen && ((PopupControllerScreenAccessor) (screen)).getBackgroundYaclScreen().config.title().equals(Text.of("Custom End Crystals"));
    }

    public static boolean isThisMyScreenExcludingPopups(Screen screen) {
        return screen instanceof YACLScreen && (((YACLScreen) screen).config.title().equals(Text.of("Custom End Crystals")));
    }

    public static boolean isThisMyScreen() {
        return isThisMyScreen(CrystalChams.mc.currentScreen);
    }

    public static EvilOption<Boolean> createBooleanOption(String name, String description, StateManager<Boolean> stateManager, OptionGroups group) {
        return EvilOption.<Boolean>createBuilder().name(Text.of(name)).stateManager(stateManager).description(OptionDescription.of(Text.of(description))).controller(CustomTickBoxControllerBuilder::new).group(group).build();
    }

    public static EvilOption<Float> createFloatOptionSeconds(String name, String description, StateManager<Float> stateManager, OptionGroups group) {
        return EvilOption.<Float>createBuilder().name(Text.of(name)).description(OptionDescription.of(Text.of(description))).stateManager(stateManager).controller(floatOption -> CustomFloatSliderControllerBuilder.create(floatOption).range(-2.5f, 2.5f).step(0.1f).formatValue(SECONDS_FORMATTER)).group(group).build();
    }

    public static EvilOption<Float> createFloatOptionPercent(String name, String description, StateManager<Float> stateManager, OptionGroups group) {
        return EvilOption.<Float>createBuilder().name(Text.of(name)).description(OptionDescription.of(Text.of(description))).stateManager(stateManager).controller(PERCENT).group(group).build();
    }

    public static EvilOption<Easings> createEasingOption(String name, String description, StateManager<Easings> stateManager, OptionGroups group) {
        return EvilOption.<Easings>createBuilder().name(Text.of(name)).description(OptionDescription.of(Text.of(description))).stateManager(stateManager).controller(easingsOption -> EnumControllerBuilder.create(easingsOption).enumClass(Easings.class)).group(group).build();
    }

    @Override
    public void onInitialize() {
        ChamsConfig.CONFIG.load();
        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (isThisMyScreen(screen)) {
                //reset age when opening preview
                if (isThisMyScreenExcludingPopups(screen)) {
                    previewCrystalEntity.age = 0;
                }
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
                    float centerX = rightPaneDim.getCenter(NavigationAxis.HORIZONTAL);
                    float centerY = rightPaneDim.getCenter(NavigationAxis.VERTICAL);
                    //TODO: fuck around with these numbers a bit more later
                    double scaledMouseX = mc.mouse.getX() * mc.getWindow().getScaledWidth() / mc.getWindow().getWidth();
                    double scaledMouseY = mc.mouse.getY() * mc.getWindow().getScaledHeight() / mc.getWindow().getHeight();
                    CrystalChams.crystalRotX = (float) CrystalChams.ease(CrystalChams.crystalRotX, Math.atan((centerX - scaledMouseX) / 40F), 15F);
                    CrystalChams.crystalRotY = (float) CrystalChams.ease(CrystalChams.crystalRotY, Math.atan((centerY - scaledMouseY) / 40F), 15F);
                    CrystalChams.previewScaleSmoothed = (float) CrystalChams.ease(CrystalChams.previewScaleSmoothed, ChamsConfig.o_previewScale.pendingValue(), 10F);
                    if (Float.isNaN(CrystalChams.crystalRotX) || Float.isNaN(CrystalChams.crystalRotY)) {
                        CrystalChams.crystalRotX = 0;
                        CrystalChams.crystalRotY = 0;
                    }
                    CrystalChams.beamProgress = (float) CrystalChams.ease(CrystalChams.beamProgress, yaclScreen.tabManager.getCurrentTab().getTitle().equals(Text.of("Beam")) ? 1 : 0, 5);
                    drawContext.getMatrices().push();
                    drawContext.getMatrices().translate(centerX, centerY, 500.0);
                    float scaleFac = Math.min(CrystalChams.mc.getWindow().getScaledWidth(), CrystalChams.mc.getWindow().getScaledHeight()) / 7.5F * previewScaleSmoothed;
                    drawContext.getMatrices().scale(scaleFac, scaleFac, -(scaleFac));
                    drawContext.getMatrices().multiply(RotationAxis.POSITIVE_Z.rotation((float) Math.PI));
                    //TODO these numbers too
                    drawContext.getMatrices().multiply(RotationAxis.POSITIVE_X.rotation(CrystalChams.crystalRotY * 25.0F * (float) (Math.PI / 180.0)));
                    drawContext.getMatrices().multiply(RotationAxis.NEGATIVE_Y.rotation(CrystalChams.crystalRotX * 35.0F * (float) (Math.PI / 180.0)));
                    CrystalChams.mc.getEntityRenderDispatcher().getRenderer(CrystalChams.previewCrystalEntity).render(CrystalChams.previewCrystalEntity, 0, ((RenderTickCounter.Dynamic) CrystalChams.mc.getRenderTickCounter()).tickDelta, drawContext.getMatrices(), CrystalChams.mc.getBufferBuilders().getEntityVertexConsumers(), 255);
                    drawContext.getMatrices().pop();
                    if (ChamsConfig.o_renderBeam.pendingValue()) {
                        drawContext.getMatrices().push();
                        drawContext.getMatrices().scale(scaleFac, scaleFac, -scaleFac);
                        double scaleFactorX = (double) mc.getWindow().getWidth() / mc.getWindow().getScaledWidth();
                        double scaleFactorY = (double) mc.getWindow().getHeight() / mc.getWindow().getScaledHeight();
                        Vec3d vec = CrystalChams.screenSpaceToWorldSpace(MathHelper.lerp(CrystalChams.beamProgress, centerX * scaleFactorX, mc.mouse.getX()), MathHelper.lerp(CrystalChams.beamProgress, centerY * scaleFactorY, mc.mouse.getY()), 0, drawContext.getMatrices().peek().getPositionMatrix()).multiply(1 / CrystalChams.mc.getWindow().getScaleFactor());
                        drawContext.getMatrices().translate(vec.x, vec.y, vec.z);
                        CrystalChams.renderCustomBeam((float) (-vec.x + ((rightPaneDim.getCenter(NavigationAxis.HORIZONTAL) / scaleFac))), (float) -vec.y + (rightPaneDim.getCenter(NavigationAxis.VERTICAL) / scaleFac) - CrystalChams.getYOffset(CrystalChams.previewCrystalEntity.endCrystalAge + ((RenderTickCounter.Dynamic) CrystalChams.mc.getRenderTickCounter()).tickDelta, ChamsConfig.o_coreOffset.pendingValue(), ChamsConfig.o_coreBounceSpeed.pendingValue(), ChamsConfig.o_coreBounceHeight.pendingValue(), ChamsConfig.o_coreDelay.pendingValue()) - 2, 2.5F, ((RenderTickCounter.Dynamic) CrystalChams.mc.getRenderTickCounter()).tickDelta, CrystalChams.previewCrystalEntity.endCrystalAge, drawContext.getMatrices(), CrystalChams.mc.getBufferBuilders().getEntityVertexConsumers(), 255, true);
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
        for(OptionGroups g : OptionGroups.values()) {
            optionGroups.put(g, new ArrayList<>());
        }
        armSecuritySystem();
        unleashHell();
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
                System.out.println("what the fuck");
                throw new RuntimeException(x);
            }
        });
    }

    @SuppressWarnings("rawtypes")
    public static void unleashHell() {
        Arrays.stream(ChamsConfig.class.getDeclaredFields()).filter(field -> field.getName().startsWith("o_")).forEach(field -> {
            try {
                Object value = field.get(null);
                if (value instanceof EvilOption) {
                    optionGroups.get(((EvilOption<?>) value).group()).add((EvilOption) value);
                }
                ((Option) value).requestSet(((Option<?>) value).binding().getValue());
            } catch (IllegalAccessException e) {
                System.out.println("what the hell");
            }
        });
    }

    public static void randomizeOptions() {
        Arrays.stream(ChamsConfig.class.getDeclaredFields()).filter(field -> field.getName().startsWith("o_")).forEach(field -> {
            if (field.getName().equals("o_frameList")) {
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
        return (start + (end - start) * (1 - Math.exp(-(1.0F / CrystalChams.mc.getCurrentFps()) * speed)));
    }

    public static Vec3d screenSpaceToWorldSpace(double x, double y, double d, Matrix4f matrix4f) {
        int displayHeight = CrystalChams.mc.getWindow().getScaledHeight();
        int displayWidth = CrystalChams.mc.getWindow().getScaledWidth();
        Vector3f target = new Vector3f();

        Matrix4f matrixProj = new Matrix4f(RenderSystem.getProjectionMatrix());
        Matrix4f matrixModel = new Matrix4f(RenderSystem.getModelViewMatrix());
        Matrix4f lastWorldSpaceMatrix = new Matrix4f(matrix4f);
        int[] lastViewport = new int[4];
        GL11.glGetIntegerv(GL11.GL_VIEWPORT, lastViewport);

        matrixProj.mul(matrixModel).mul(lastWorldSpaceMatrix).unproject((float) x / displayWidth * lastViewport[2], (float) (displayHeight - y) / displayHeight * lastViewport[3], (float) d, lastViewport, target);

        return new Vec3d(target.x, target.y, target.z);
    }

    public static void renderCustomBeam(float dx, float dy, float dz, float tickDelta, int age, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, boolean preview) {
        float f = MathHelper.sqrt(dx * dx + dz * dz);
        float v = dx * dx + dy * dy + dz * dz;
        float g = MathHelper.sqrt(v);
        matrices.push();
        if (!preview) {
            matrices.translate(0.0F, 2.0F, 0.0F);
        }
        matrices.multiply(RotationAxis.POSITIVE_Y.rotation((float) (-Math.atan2(dz, dx)) - (float) (Math.PI / 2)));
        matrices.multiply(RotationAxis.POSITIVE_X.rotation((float) (-Math.atan2(f, dy)) - (float) (Math.PI / 2)));
        VertexConsumer vertexConsumer = getLayer(vertexConsumers, ChamsConfig.o_beamRenderLayer.pendingValue(), ChamsConfig.o_beamCulling.pendingValue(), CRYSTAL_BEAM_TEXTURE);
        float h = -(tickDelta + age) * (0.01F * ChamsConfig.o_beamScrollSpeed.pendingValue());
        float i = MathHelper.sqrt(v) / 32.0F - (tickDelta + age) * (0.01F * ChamsConfig.o_beamScrollSpeed.pendingValue());
        float k = 0.0F;
        float l = 1F;
        float m = 0.0F;
        MatrixStack.Entry entry = matrices.peek();

        for (int n = 1; n <= ChamsConfig.o_beamSides.pendingValue(); n++) {
            float o = MathHelper.sin((float) n * (float) (Math.PI * 2) / ChamsConfig.o_beamSides.pendingValue());
            float p = MathHelper.cos((float) n * (float) (Math.PI * 2) / ChamsConfig.o_beamSides.pendingValue());
            float q = (float) n / ChamsConfig.o_beamSides.pendingValue();
            Color rainbowCol1 = new Color(CrystalChams.getRainbow(ChamsConfig.o_beam1RainbowDelay.pendingValue(), ChamsConfig.o_beam1RainbowSpeed.pendingValue(), ChamsConfig.o_beam1RainbowSaturation.pendingValue(), ChamsConfig.o_beam1RainbowBrightness.pendingValue()));
            Color rainbowCol2 = new Color(CrystalChams.getRainbow(ChamsConfig.o_beam2RainbowDelay.pendingValue(), ChamsConfig.o_beam2RainbowSpeed.pendingValue(), ChamsConfig.o_beam2RainbowSaturation.pendingValue(), ChamsConfig.o_beam2RainbowBrightness.pendingValue()));
            int startCol = ChamsConfig.o_beam1Rainbow.pendingValue() ? ColorHelper.Argb.getArgb((int) (ChamsConfig.o_beam1Alpha.pendingValue() * (preview ? CrystalChams.beamProgress : 1) * 255F), rainbowCol1.getRed(), rainbowCol1.getGreen(), rainbowCol1.getBlue()) : ColorHelper.Argb.getArgb((int) (ChamsConfig.o_beam1Alpha.pendingValue() * (preview ? CrystalChams.beamProgress : 1) * 255F), ChamsConfig.o_beam1Color.pendingValue().getRed(), ChamsConfig.o_beam1Color.pendingValue().getGreen(), ChamsConfig.o_beam1Color.pendingValue().getBlue());
            int endCol = ChamsConfig.o_beam2Rainbow.pendingValue() ? ColorHelper.Argb.getArgb((int) (ChamsConfig.o_beam2Alpha.pendingValue() * (preview ? CrystalChams.beamProgress : 1) * 255F), rainbowCol2.getRed(), rainbowCol2.getGreen(), rainbowCol2.getBlue()) : ColorHelper.Argb.getArgb((int) (ChamsConfig.o_beam2Alpha.pendingValue() * (preview ? CrystalChams.beamProgress : 1) * 255F), ChamsConfig.o_beam2Color.pendingValue().getRed(), ChamsConfig.o_beam2Color.pendingValue().getGreen(), ChamsConfig.o_beam2Color.pendingValue().getBlue());
            vertexConsumer.vertex(entry, k * ChamsConfig.o_beam2Radius.pendingValue(), l * ChamsConfig.o_beam2Radius.pendingValue(), 0.0F).color(endCol).texture(m, h).overlay(OverlayTexture.DEFAULT_UV).light(ChamsConfig.o_beam2LightLevel.pendingValue() != -1 ? ChamsConfig.o_beam2LightLevel.pendingValue() : light).normal(entry, 0.0F, -1.0F, 0.0F);
            vertexConsumer.vertex(entry, k * ChamsConfig.o_beam1Radius.pendingValue(), l * ChamsConfig.o_beam1Radius.pendingValue(), g).color(startCol).texture(m, i).overlay(OverlayTexture.DEFAULT_UV).light(ChamsConfig.o_beam1LightLevel.pendingValue() != -1 ? ChamsConfig.o_beam1LightLevel.pendingValue() : light).normal(entry, 0.0F, -1.0F, 0.0F);
            vertexConsumer.vertex(entry, o * ChamsConfig.o_beam1Radius.pendingValue(), p * ChamsConfig.o_beam1Radius.pendingValue(), g).color(startCol).texture(q, i).overlay(OverlayTexture.DEFAULT_UV).light(ChamsConfig.o_beam1LightLevel.pendingValue() != -1 ? ChamsConfig.o_beam1LightLevel.pendingValue() : light).normal(entry, 0.0F, -1.0F, 0.0F);
            vertexConsumer.vertex(entry, o * ChamsConfig.o_beam2Radius.pendingValue(), p * ChamsConfig.o_beam2Radius.pendingValue(), 0.0F).color(endCol).texture(q, h).overlay(OverlayTexture.DEFAULT_UV).light(ChamsConfig.o_beam2LightLevel.pendingValue() != -1 ? ChamsConfig.o_beam2LightLevel.pendingValue() : light).normal(entry, 0.0F, -1.0F, 0.0F);
            k = o;
            l = p;
            m = q;
        }
        matrices.pop();
    }
}
