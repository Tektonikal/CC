package tektonikal.crystalchams.config;

import tektonikal.crystalchams.CrystalChams;

import java.util.function.Supplier;

public class ValueAnimator {
    private float value;
    private final Supplier<Float> supplier;

    public ValueAnimator(Supplier<Float> supplier) {
        this.supplier = supplier;
        value = supplier.get();
    }

    public void update() {
        value = (float) CrystalChams.ease(value, supplier.get(), CrystalChams.PREVIEW_EASING_SPEED);
    }
    public Number getValue(){
        return value;
    }
}
