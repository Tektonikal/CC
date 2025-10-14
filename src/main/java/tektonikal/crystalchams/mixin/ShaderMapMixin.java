package tektonikal.crystalchams.mixin;

import net.irisshaders.iris.pipeline.programs.ShaderMap;
import net.minecraft.client.gl.ShaderProgram;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ShaderMap.class)
public class ShaderMapMixin {
    @Shadow @Final private ShaderProgram[] shaders;
//    @Inject(method = "<init>", at = @At("TAIL"), remap = false)
//    private void ough(Function<ShaderKey, ShaderProgram> factory, CallbackInfo ci){
//        ArrayUtils.addAll(shaders, CrystalChams.END_PORTAL_TEX);
//    }
}
