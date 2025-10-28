package tektonikal.crystalchams.stupidfuckingboilerplate;

import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.controller.ValueFormatter;
import net.minecraft.text.Text;
import org.apache.commons.lang3.Validate;

import java.util.function.Function;

/**
 * {@link ISliderController} for integers.
 */
public class CustomIntegerSliderController implements ICustomSliderController<Integer> {
    public static final Function<Integer, Text> DEFAULT_FORMATTER = value -> Text.literal(String.format("%,d", value).replaceAll("[\u00a0\u202F]", " "));

    private final Option<Integer> option;

    private final int min, max, interval;

    public final ValueFormatter<Integer> valueFormatter;

    /**
     * Constructs a {@link ISliderController} for integers
     * using the default value formatter {@link IntegerSliderController#DEFAULT_FORMATTER}.
     *
     * @param option bound option
     * @param min minimum slider value
     * @param max maximum slider value
     * @param interval step size (or increments) for the slider
     */
    public CustomIntegerSliderController(Option<Integer> option, int min, int max, int interval) {
        this(option, min, max, interval, DEFAULT_FORMATTER);
    }

    /**
     * Constructs a {@link ISliderController} for integers.
     *
     * @param option bound option
     * @param min minimum slider value
     * @param max maximum slider value
     * @param interval step size (or increments) for the slider
     * @param valueFormatter format the value into any {@link Text}
     */
    public CustomIntegerSliderController(Option<Integer> option, int min, int max, int interval, Function<Integer, Text> valueFormatter) {
        Validate.isTrue(max > min, "`max` cannot be smaller than `min`");
        Validate.isTrue(interval > 0, "`interval` must be more than 0");
        Validate.notNull(valueFormatter, "`valueFormatter` must not be null");

        this.option = option;
        this.min = min;
        this.max = max;
        this.interval = interval;
        this.valueFormatter = valueFormatter::apply;
    }

    public static CustomIntegerSliderController createInternal(Option<Integer> option, int min, int max, int interval, ValueFormatter<Integer> formatter) {
        return new CustomIntegerSliderController(option, min, max, interval, formatter::format);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Option<Integer> option() {
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
        option().requestSet((int) value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double pendingValue() {
        return option().pendingValue();
    }

}
