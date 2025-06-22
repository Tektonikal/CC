package tektonikal.crystalchams.config;

import dev.isxander.yacl3.api.Controller;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import dev.isxander.yacl3.gui.controllers.TickBoxController;
import dev.isxander.yacl3.impl.controller.AbstractControllerBuilderImpl;

public class CustomTickBoxControllerBuilder extends AbstractControllerBuilderImpl<Boolean> implements TickBoxControllerBuilder {
    public CustomTickBoxControllerBuilder(Option<Boolean> option) {
        super(option);
    }

    //FACTS DONT CARE ABOUT YOUR @APISTATUS.INTERNAL !!!!!!
    @Override
    public Controller<Boolean> build() {
        return new CustomTickBoxController(option);
    }
}
