package tektonikal.crystalchams;

import eu.midnightdust.lib.config.MidnightConfig;
import net.fabricmc.api.ModInitializer;
import tektonikal.crystalchams.config.ChamsConfig;

public class Crystalchams implements ModInitializer {
    @Override
    public void onInitialize() {
    MidnightConfig.init("crystalchams", ChamsConfig.class);
    }
}
