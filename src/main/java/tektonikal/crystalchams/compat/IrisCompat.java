package tektonikal.crystalchams.compat;

import net.fabricmc.loader.api.FabricLoader;
import net.irisshaders.iris.api.v0.IrisApi;

public class IrisCompat {
    public static boolean isUsingShaderPack(){
        //TODO: remove?
        return FabricLoader.getInstance().isModLoaded("iris") && IrisApi.getInstance().isShaderPackInUse();
    }
}
