package tektonikal.crystalchams.stupidfuckingboilerplate;

import dev.isxander.yacl3.api.Controller;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.controller.ValueFormatter;
import dev.isxander.yacl3.gui.controllers.slider.IntegerSliderController;
import dev.isxander.yacl3.impl.controller.AbstractControllerBuilderImpl;

public class CustomIntegerSliderControllerBuilderImpl extends AbstractControllerBuilderImpl<Integer> implements CustomIntegerSliderControllerBuilder {
    private int min, max;
    private int step;
    private ValueFormatter<Integer> formatter = IntegerSliderController.DEFAULT_FORMATTER::apply;

    public CustomIntegerSliderControllerBuilderImpl(Option<Integer> option) {
        super(option);
    }

    @Override
    public CustomIntegerSliderControllerBuilder range(Integer min, Integer max) {
        this.min = min;
        this.max = max;
        return this;
    }

    @Override
    public CustomIntegerSliderControllerBuilder step(Integer step) {
        this.step = step;
        return this;
    }

    @Override
    public CustomIntegerSliderControllerBuilder formatValue(ValueFormatter<Integer> formatter) {
        this.formatter = formatter;
        return this;
    }

    @Override
    public Controller<Integer> build() {
        return CustomIntegerSliderController.createInternal(option, min, max, step, formatter);
    }
}
