package tektonikal.crystalchams.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tektonikal.crystalchams.config.ChamsConfig;
import tektonikal.crystalchams.interfaces.EndCrystalEntityMixinInterface;

@Mixin(EndCrystal.class)
public abstract class EndCrystalMixin extends Entity implements EndCrystalEntityMixinInterface {
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

    @Shadow public int time;

    public EndCrystalMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Inject(method = "<init>(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/Level;)V", at = @At(value = "TAIL"))
    private void CC$ough(EntityType<? extends EndCrystal> entityType, Level world, CallbackInfo ci){
        if(!ChamsConfig.o_randomizeAge.pendingValue()){
            this.time = 0;
        }
    }

//    @Inject(method = "tick", at = @At("TAIL"))
//    public void tick(CallbackInfo info) {
//        rotation = (endCrystalAge + ((RenderTickCounterAccessor) CrystalChams.mc.getRenderTickCounter()).getTickDelta()) * 3;
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
