package tektonikal.crystalchams.stupidfuckingboilerplate;

import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.controller.SliderControllerBuilder;

public interface CustomFloatSliderControllerBuilder extends SliderControllerBuilder<Float, CustomFloatSliderControllerBuilder> {
    static CustomFloatSliderControllerBuilderImpl create(Option<Float> option) {
        return new CustomFloatSliderControllerBuilderImpl(option);
    }
}
