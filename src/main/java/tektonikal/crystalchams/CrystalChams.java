package tektonikal.crystalchams;

import net.fabricmc.api.ModInitializer;
import tektonikal.crystalchams.config.ChamsConfig;

public class CrystalChams implements ModInitializer {
    @Override
    public void onInitialize() {
        ChamsConfig.CONFIG.load();
    }
}
