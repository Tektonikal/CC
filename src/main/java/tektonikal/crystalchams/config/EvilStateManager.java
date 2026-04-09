package tektonikal.crystalchams.config;

import dev.isxander.yacl3.api.Binding;
import dev.isxander.yacl3.api.StateManager;
import dev.isxander.yacl3.impl.ProvidesBindingForDeprecation;

public class EvilStateManager<T> implements StateManager<T>, ProvidesBindingForDeprecation<T> {
    private final Binding<T> binding;
    private T pendingValue;
    private StateListener<T> stateListener;

    public EvilStateManager(Binding<T> binding) {
        this.binding = binding;
        this.pendingValue = binding.getValue();
        this.stateListener = StateListener.noop();
    }

    @Override
    public void set(T value) {
        boolean changed = !this.pendingValue.equals(value);

        this.pendingValue = value;

        if (changed) stateListener.onStateChange(this.pendingValue, value);
    }

    @Override
    public T get() {
        return pendingValue;
    }

    @Override
    public void apply() {
        binding.setValue(pendingValue);
    }

    @Override
    public void resetToDefault(ResetAction action) {
        this.set(binding.defaultValue());
    }

    @Override
    public void sync() {
        this.set(binding.getValue());
    }

    @Override
    public boolean isSynced() {
        return binding.getValue().equals(pendingValue);
    }

    @Override
    public boolean isDefault() {
        return binding.defaultValue().equals(pendingValue);
    }

    @Override
    public void addListener(StateListener<T> stateListener) {
        this.stateListener = this.stateListener.andThen(stateListener);
    }

    @Override
    public Binding<T> getBinding() {
        return binding;
    }
}
