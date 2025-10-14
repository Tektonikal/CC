package tektonikal.crystalchams.config;

import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.gui.YACLScreen;
import net.minecraft.client.gui.screen.Screen;

public class SecondaryYACLScreen extends YACLScreen {
    private final ModelPartController control;

    public SecondaryYACLScreen(ModelPartController control, YetAnotherConfigLib config, Screen parent) {
        super(config, parent);
        this.control = control;
    }
}
