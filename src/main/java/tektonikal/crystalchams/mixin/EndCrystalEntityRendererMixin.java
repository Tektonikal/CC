package tektonikal.crystalchams.mixin;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EndCrystalEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import tektonikal.crystalchams.config.ChamsConfig;

import java.awt.*;

@Mixin(EndCrystalEntityRenderer.class)
public abstract class EndCrystalEntityRendererMixin extends EntityRenderer {
    @Shadow
    @Final
    private ModelPart core;

    @Shadow
    @Final
    private ModelPart frame;

    @Mutable
    @Shadow @Final private static RenderLayer END_CRYSTAL;

    @Shadow @Final private static Identifier TEXTURE;

    protected EndCrystalEntityRendererMixin(EntityRendererFactory.Context ctx) {
        super(ctx);
    }


    @Inject(method = "render(Lnet/minecraft/entity/decoration/EndCrystalEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("HEAD"))
    private void render(EndCrystalEntity endCrystalEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        if(ChamsConfig.isActive){
        this.shadowRadius = ChamsConfig.shadow;
        this.shadowOpacity = ChamsConfig.shadowOpacity;
        switch (ChamsConfig.mode){
            case PORTAL -> END_CRYSTAL = RenderLayer.getEndPortal();
            //for some reason the lineWidth value does absolutely nothing when I try to change it
            case WIREFRAME -> END_CRYSTAL = RenderLayer.getDebugLineStrip(10);
            case GATEWAY -> END_CRYSTAL = RenderLayer.getEndGateway();
            case CULLED -> END_CRYSTAL = RenderLayer.getItemEntityTranslucentCull(TEXTURE);
            default -> END_CRYSTAL = RenderLayer.getEntityTranslucent(TEXTURE);
        }
        }
        else{
            END_CRYSTAL = RenderLayer.getEntityTranslucent(TEXTURE);
            this.shadowRadius = 0.5F;
            this.shadowOpacity = 1;
        }
    }

    @ModifyArgs(method = "render(Lnet/minecraft/entity/decoration/EndCrystalEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;scale(FFF)V", ordinal = 0))
    private void modifyScale(Args args) {
        if (ChamsConfig.isActive){
        args.set(0, ChamsConfig.scale);
        args.set(1, ChamsConfig.scale);
        args.set(2, ChamsConfig.scale);
        }
    }

    @Redirect(method = "render(Lnet/minecraft/entity/decoration/EndCrystalEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/EndCrystalEntityRenderer;getYOffset(Lnet/minecraft/entity/decoration/EndCrystalEntity;F)F"))
    private float offset(EndCrystalEntity crystal, float tickDelta) {
        if (ChamsConfig.isActive){
            float f = crystal.endCrystalAge + tickDelta;
            float g = MathHelper.sin(f * ChamsConfig.BounceSpeed) / 2.0F + 0.5F;
            g = (g * g + g) * ChamsConfig.bounce;
            return g - 1.4F + ChamsConfig.offset;
        }
        return EndCrystalEntityRenderer.getYOffset(crystal, tickDelta);
    }


    @Redirect(method = {"render(Lnet/minecraft/entity/decoration/EndCrystalEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelPart;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;II)V", ordinal = 3))
    private void modifyCore(ModelPart instance, MatrixStack matrices, VertexConsumer vertices, int light, int overlay) {
        if(ChamsConfig.isActive){
            if(ChamsConfig.renderCore){
            try{
            Color color = Color.decode(ChamsConfig.col);
            this.core.render(matrices, vertices, ChamsConfig.lLevel == -1 ? light : ChamsConfig.lLevel, overlay, color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, ChamsConfig.alpha);
            }
            catch (NumberFormatException e){
                this.core.render(matrices, vertices, light, overlay, 1, 0,0,1);
            }
            }
        }
        else{
            this.core.render(matrices, vertices, light, overlay);
        }
    }

    @Redirect(method = "render(Lnet/minecraft/entity/decoration/EndCrystalEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelPart;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;II)V", ordinal = 1))
    private void modifyFrame1(ModelPart instance, MatrixStack matrices, VertexConsumer vertices, int light, int overlay) {
        if (ChamsConfig.isActive) {
        if (ChamsConfig.renderFrame1) {
            try{
            Color color = Color.decode(ChamsConfig.frameCol);
            this.frame.render(matrices, vertices, ChamsConfig.lLevel == -1 ? light : ChamsConfig.lLevel, overlay, color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, ChamsConfig.frame1Alpha);
            }
            catch (NumberFormatException e){
                this.frame.render(matrices, vertices, light, overlay, 1, 0,0,1);
            }
        }
        }
        else{
            this.frame.render(matrices, vertices, light, overlay);
        }
    }
    @ModifyArgs(method = "render(Lnet/minecraft/entity/decoration/EndCrystalEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/RotationAxis;rotationDegrees(F)Lorg/joml/Quaternionf;"))
//    @ModifyArgs(method={"render(Lnet/minecraft/entity/decoration/EndCrystalEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"}, at=@At(value="INVOKE", target="Lnet/minecraft/util/math/RotationAxis;rotationDegrees(F)Lorg/joml/Quaternionf;"))
//        @ModifyArgs(method = "render(Lnet/minecraft/entity/decoration/EndCrystalEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Vec3f;getDegreesQuaternion(F)Lnet/minecraft/util/math/Quaternion;"))
    private void modifySpeed(Args args) {
        if (!ChamsConfig.isActive) {
            return;
        }
        args.set(0, (Float) args.get(0) * ChamsConfig.rotSpeed);
    }

    @Redirect(method = {"render(Lnet/minecraft/entity/decoration/EndCrystalEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelPart;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;II)V", ordinal = 2))
    private void modifyFrame2(ModelPart instance, MatrixStack matrices, VertexConsumer vertices, int light, int overlay) {
        if (ChamsConfig.isActive) {
        if (ChamsConfig.renderFrame2) {
            try{
            Color color = Color.decode(ChamsConfig.frameCol2);
                this.frame.render(matrices, vertices, ChamsConfig.lLevel == -1 ? light : ChamsConfig.lLevel, overlay, color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, ChamsConfig.frame2Alpha);
            }
            catch (NumberFormatException e){
                this.frame.render(matrices, vertices, light, overlay, 1, 0,0,1);
            }
        }
        }
        else{
            this.frame.render(matrices, vertices, light, overlay);
        }
    }
}
