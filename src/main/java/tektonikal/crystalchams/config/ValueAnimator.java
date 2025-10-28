package tektonikal.crystalchams.config;

import tektonikal.crystalchams.CrystalChams;

import java.util.function.Supplier;

public class ValueAnimator {
    private Number value;
    private final Supplier<Number> supplier;

    public ValueAnimator(Supplier<Number> supplier) {
        this.supplier = supplier;
        value = supplier.get();
    }

    public void update() {
        value = CrystalChams.ease((Double) value, (Double) supplier.get(), CrystalChams.PREVIEW_EASING_SPEED);
    }
    public Number getValue(){
        return value;
    }
}
