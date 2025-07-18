package tektonikal.crystalchams.mixin;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EndCrystalEntityRenderer;
import net.minecraft.client.render.entity.EnderDragonEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tektonikal.crystalchams.CrystalChams;
import tektonikal.crystalchams.config.ChamsConfig;

import java.awt.*;

import static tektonikal.crystalchams.CrystalChams.getLayer;

@Mixin(EnderDragonEntityRenderer.class)
public abstract class EnderDragonEntityRendererMixin extends EntityRenderer<EnderDragonEntity> {


    @Shadow @Final private static Identifier TEXTURE;

    @Shadow @Final public static Identifier CRYSTAL_BEAM_TEXTURE;

    protected EnderDragonEntityRendererMixin(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Redirect(method = "render(Lnet/minecraft/entity/boss/dragon/EnderDragonEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/EndCrystalEntityRenderer;getYOffset(Lnet/minecraft/entity/decoration/EndCrystalEntity;F)F"))
    private float CC$BeamOffsetRedirect(EndCrystalEntity crystal, float tickDelta) {
        if (ChamsConfig.CONFIG.instance().modEnabled) {
            return CrystalChams.getYOffset(crystal.endCrystalAge + tickDelta, ChamsConfig.CONFIG.instance().coreOffset, ChamsConfig.CONFIG.instance().coreBounceSpeed, ChamsConfig.CONFIG.instance().coreBounceHeight, ChamsConfig.CONFIG.instance().coreTickDelay);
        } else {
            return EndCrystalEntityRenderer.getYOffset(crystal, tickDelta);
        }
    }

    @Inject(method = "renderCrystalBeam", at = @At(value = "HEAD"), cancellable = true)
    private static void CC$BeamRenderInject(float dx, float dy, float dz, float tickDelta, int age, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (!ChamsConfig.CONFIG.instance().modEnabled) {
            return;
        }
        if (!ChamsConfig.CONFIG.instance().renderBeam) {
            ci.cancel();
            return;
        }
        float f = MathHelper.sqrt(dx * dx + dz * dz);
        float v = dx * dx + dy * dy + dz * dz;
        float g = MathHelper.sqrt(v);
        matrices.push();
        matrices.translate(0.0F, 2.0F, 0.0F);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotation((float) (-Math.atan2((double) dz, (double) dx)) - (float) (Math.PI / 2)));
        matrices.multiply(RotationAxis.POSITIVE_X.rotation((float) (-Math.atan2((double) f, (double) dy)) - (float) (Math.PI / 2)));
        VertexConsumer vertexConsumer = getLayer(vertexConsumers, ChamsConfig.CONFIG.instance().beamRenderMode, true, CRYSTAL_BEAM_TEXTURE);
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
            int startCol = ChamsConfig.CONFIG.instance().beam1Rainbow ? ColorHelper.Argb.getArgb((int) (ChamsConfig.CONFIG.instance().beam1Alpha * 255F), rainbowCol1.getRed(), rainbowCol1.getGreen(), rainbowCol1.getBlue()) : ColorHelper.Argb.getArgb((int) (ChamsConfig.CONFIG.instance().beam1Alpha * 255F), ChamsConfig.CONFIG.instance().beam1Color.getRed(), ChamsConfig.CONFIG.instance().beam1Color.getGreen(), ChamsConfig.CONFIG.instance().beam1Color.getBlue());
            int endCol = ChamsConfig.CONFIG.instance().beam2Rainbow ? ColorHelper.Argb.getArgb((int) (ChamsConfig.CONFIG.instance().beam2Alpha * 255F), rainbowCol2.getRed(), rainbowCol2.getGreen(), rainbowCol2.getBlue()) : ColorHelper.Argb.getArgb((int) (ChamsConfig.CONFIG.instance().beam2Alpha * 255F), ChamsConfig.CONFIG.instance().beam2Color.getRed(), ChamsConfig.CONFIG.instance().beam2Color.getGreen(), ChamsConfig.CONFIG.instance().beam2Color.getBlue());
            vertexConsumer.vertex(entry, k * ChamsConfig.CONFIG.instance().beam2Radius, l * ChamsConfig.CONFIG.instance().beam2Radius, 0.0F).color(endCol).texture(m, h).overlay(OverlayTexture.DEFAULT_UV).light(ChamsConfig.CONFIG.instance().beam2LightLevel != -1 ? ChamsConfig.CONFIG.instance().beam2LightLevel : light).normal(entry, 0.0F, -1.0F, 0.0F);
            vertexConsumer.vertex(entry, k * ChamsConfig.CONFIG.instance().beam1Radius, l * ChamsConfig.CONFIG.instance().beam1Radius, g).color(startCol).texture(m, i).overlay(OverlayTexture.DEFAULT_UV).light(ChamsConfig.CONFIG.instance().beam1LightLevel != -1 ? ChamsConfig.CONFIG.instance().beam1LightLevel : light).normal(entry, 0.0F, -1.0F, 0.0F);
            vertexConsumer.vertex(entry, o * ChamsConfig.CONFIG.instance().beam1Radius, p * ChamsConfig.CONFIG.instance().beam1Radius, g).color(startCol).texture(q, i).overlay(OverlayTexture.DEFAULT_UV).light(ChamsConfig.CONFIG.instance().beam1LightLevel != -1 ? ChamsConfig.CONFIG.instance().beam1LightLevel : light).normal(entry, 0.0F, -1.0F, 0.0F);
            vertexConsumer.vertex(entry, o * ChamsConfig.CONFIG.instance().beam2Radius, p * ChamsConfig.CONFIG.instance().beam2Radius, 0.0F).color(endCol).texture(q, h).overlay(OverlayTexture.DEFAULT_UV).light(ChamsConfig.CONFIG.instance().beam2LightLevel != -1 ? ChamsConfig.CONFIG.instance().beam2LightLevel : light).normal(entry, 0.0F, -1.0F, 0.0F);
            k = o;
            l = p;
            m = q;
        }

        matrices.pop();
        ci.cancel();
    }
}
