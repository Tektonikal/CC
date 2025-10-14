package tektonikal.crystalchams.stupidfuckingboilerplate;

import dev.isxander.yacl3.api.Controller;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.impl.controller.AbstractControllerBuilderImpl;
import tektonikal.crystalchams.config.ModelPartController;
import tektonikal.crystalchams.config.ModelPartOptions;

public class ModelPartControllerBuilder extends AbstractControllerBuilderImpl<ModelPartOptions>{
    public ModelPartControllerBuilder(Option<ModelPartOptions> option) {
        super(option);
    }

    //FACTS DONT CARE ABOUT YOUR @APISTATUS.INTERNAL !!!!!!
    @Override
    public Controller<ModelPartOptions> build() {
        return new ModelPartController(option);
    }
}
