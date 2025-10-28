package tektonikal.crystalchams.stupidfuckingboilerplate;

import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.controller.SliderControllerBuilder;

public interface CustomIntegerSliderControllerBuilder extends SliderControllerBuilder<Integer, CustomIntegerSliderControllerBuilder> {
    static CustomIntegerSliderControllerBuilder create(Option<Integer> option) {
        return new CustomIntegerSliderControllerBuilderImpl(option);
    }
}
