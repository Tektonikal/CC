package tektonikal.crystalchams.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tektonikal.crystalchams.config.ChamsConfig;
import tektonikal.crystalchams.interfaces.EndCrystalEntityMixinInterface;

@Mixin(EndCrystalEntity.class)
public abstract class EndCrystalEntityMixin extends Entity implements EndCrystalEntityMixinInterface {
//    @Shadow
//    public int endCrystalAge;
//    @Unique
//    public float rotation = 0;

//    @Unique
//    public float CC$getFrame1BounceSpeed() {
//        return frame1BounceSpeed;
//    }
//
//    @Unique
//    public void CC$setFrame1BounceSpeed(float frame1BounceSpeed) {
//        this.frame1BounceSpeed = frame1BounceSpeed;
//    }
//
//    @Unique
//    public float frame1BounceSpeed = ChamsConfig.CONFIG.instance().frame1BounceSpeed;

    @Shadow public int endCrystalAge;

    public EndCrystalEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "<init>(Lnet/minecraft/entity/EntityType;Lnet/minecraft/world/World;)V", at = @At(value = "TAIL"))
    private void CC$ough(EntityType<? extends EndCrystalEntity> entityType, World world, CallbackInfo ci){
        if(!ChamsConfig.CONFIG.instance().randomizeAge){
            this.endCrystalAge = 0;
        }
    }

//    @Inject(method = "tick", at = @At("TAIL"))
//    public void tick(CallbackInfo info) {
//        rotation = (endCrystalAge + ((RenderTickCounter.Dynamic) MinecraftClient.getInstance().getRenderTickCounter()).tickDelta) * 3;
//    }
//
//    @Unique
//    public float CC$getRotation() {
//        return rotation;
//    }
//
//    @Unique
//    public void CC$setRotation(float rotation) {
//        this.rotation = rotation;
//    }
}
