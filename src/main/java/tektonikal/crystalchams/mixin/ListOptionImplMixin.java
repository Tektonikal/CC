package tektonikal.crystalchams.mixin;

import dev.isxander.yacl3.impl.ListOptionImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tektonikal.crystalchams.CrystalChams;

@Mixin(ListOptionImpl.class)
public class ListOptionImplMixin {
    @Inject(method = "isPendingValueDefault", at = @At("HEAD"), remap = false, cancellable = true)
    private static void ough(CallbackInfoReturnable<Boolean> cir){
//        if(CrystalChams.isThisMyScreen()){
//            cir.setReturnValue(false);
//        }
    }
}
