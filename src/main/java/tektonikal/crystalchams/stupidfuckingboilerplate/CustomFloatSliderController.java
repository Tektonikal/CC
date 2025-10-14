package tektonikal.crystalchams.stupidfuckingboilerplate;

import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.controller.ValueFormatter;
import org.apache.commons.lang3.Validate;

import java.util.function.Function;
import net.minecraft.text.Text;


public class CustomFloatSliderController implements ICustomSliderController<Float> {
    /**
     * Formats floats to one decimal place
     */
    public static final Function<Float, Text> DEFAULT_FORMATTER = value -> Text.literal(String.format("%,.1f", value).replaceAll("[\u00a0\u202F]", " "));

    private final Option<Float> option;

    private final float min, max, interval;

    public final ValueFormatter<Float> valueFormatter;


    public CustomFloatSliderController(Option<Float> option, float min, float max, float interval) {
        this(option, min, max, interval, DEFAULT_FORMATTER);
    }

    public CustomFloatSliderController(Option<Float> option, float min, float max, float interval, Function<Float, Text> valueFormatter) {
        Validate.isTrue(max > min, "`max` cannot be smaller than `min`");
        Validate.isTrue(interval > 0, "`interval` must be more than 0");
        Validate.notNull(valueFormatter, "`valueFormatter` must not be null");

        this.option = option;
        this.min = min;
        this.max = max;
        this.interval = interval;
        this.valueFormatter = valueFormatter::apply;
    }

    public static CustomFloatSliderController createInternal(Option<Float> option, float min, float max, float interval, ValueFormatter<Float> formatter) {
        return new CustomFloatSliderController(option, min, max, interval, formatter::format);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Option<Float> option() {
        return option;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Text formatValue() {
        return valueFormatter.format(option().pendingValue());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double min() {
        return min;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double max() {
        return max;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double interval() {
        return interval;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPendingValue(double value) {
        option().requestSet((float) value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double pendingValue() {
        return option().pendingValue();
    }

}
