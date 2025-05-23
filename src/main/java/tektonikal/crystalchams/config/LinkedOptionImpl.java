package tektonikal.crystalchams.config;

import com.google.common.collect.ImmutableSet;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.ControllerBuilder;
import dev.isxander.yacl3.impl.ProvidesBindingForDeprecation;
import dev.isxander.yacl3.impl.utils.YACLConstants;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

//I DON'T CARE THAT THIS IS INTERNAL API !!!!!!!!!!!!!
public class LinkedOptionImpl<T> implements Option<T> {
    private final Text name;
    private OptionDescription description;
    private final Controller<T> controller;
    private boolean available;

    private final ImmutableSet<OptionFlag> flags;

    private final StateManager<T> stateManager;
    private final List<OptionEventListener<T>> listeners;
    private int currentListenerDepth;
    private final List<Option<T>> linkedOptions;

    public LinkedOptionImpl(
            @NotNull Text name,
            @NotNull Function<T, OptionDescription> descriptionFunction,
            @NotNull Function<Option<T>, Controller<T>> controlGetter,
            @NotNull StateManager<T> stateManager,
            boolean available,
            ImmutableSet<OptionFlag> flags,
            @NotNull Collection<OptionEventListener<T>> listeners, List<Option<T>> linkedOptions
    ) {
        this.name = name;
        this.available = available;
        this.flags = flags;
        this.listeners = new ArrayList<>(listeners);

        this.stateManager = stateManager;
        this.controller = controlGetter.apply(this);
        this.linkedOptions = linkedOptions;

        this.stateManager.addListener((oldValue, newValue) ->
                triggerListener(OptionEventListener.Event.STATE_CHANGE, false));
        addEventListener((opt, event) -> description = descriptionFunction.apply(opt.pendingValue()));
        triggerListener(OptionEventListener.Event.INITIAL, false);
    }

    @Override
    public @NotNull Text name() {
        return name;
    }

    @Override
    public @NotNull OptionDescription description() {
        return this.description;
    }

    @Override
    public @NotNull Text tooltip() {
        return description.text();
    }

    @Override
    public @NotNull Controller<T> controller() {
        return controller;
    }

    @Override
    public @NotNull StateManager<T> stateManager() {
        return stateManager;
    }

    @Override
    @Deprecated
    public @NotNull Binding<T> binding() {
        if (stateManager instanceof ProvidesBindingForDeprecation) {
            return ((ProvidesBindingForDeprecation<T>) stateManager).getBinding();
        }
        throw new UnsupportedOperationException("Binding is not available for this option - using a new state manager which does not directly expose the binding as it may not have one.");
    }

    @Override
    public boolean available() {
        return available;
    }


    public void addLinkedOption(Option<T> option) {
        if (option != this) {
            linkedOptions.add(option);
        }
    }
    @SafeVarargs
    public final void linkOptions(LinkedOptionImpl<T>... options) {
        options = ArrayUtils.addAll(options, this);
        for (LinkedOptionImpl<T> option : options) {
            option.addLinkedOptions(options);
        }
    }

    @SafeVarargs
    public final void addLinkedOptions(LinkedOptionImpl<T>... options) {
        for (Option<T> option : options) {
            addLinkedOption(option);
        }
    }

    public void syncLinkedOptions() {
        linkedOptions.forEach(tOption -> {
            tOption.requestSet(this.pendingValue());
        });
    }

    @Override
    public void setAvailable(boolean available) {
        boolean changed = this.available != available;

        this.available = available;

        if (changed) {
            if (!available) {
                this.stateManager.sync();
            }
            this.triggerListener(OptionEventListener.Event.AVAILABILITY_CHANGE, !available);
        }
    }

    @Override
    public @NotNull ImmutableSet<OptionFlag> flags() {
        return flags;
    }

    @Override
    public boolean changed() {
        return !this.stateManager.isSynced();
    }

    @Override
    public @NotNull T pendingValue() {
        return this.stateManager.get();
    }

    @Override
    public void requestSet(@NotNull T value) {
        Validate.notNull(value, "`value` cannot be null");

        this.stateManager.set(value);
    }

    @Override
    public boolean applyValue() {
        if (changed()) {
            this.stateManager.apply();
            return true;
        }
        return false;
    }

    @Override
    public void forgetPendingValue() {
        this.stateManager.sync();
    }

    @Override
    public void requestSetDefault() {
        this.stateManager.resetToDefault(StateManager.ResetAction.BY_OPTION);
    }

    @Override
    public boolean isPendingValueDefault() {
        return this.stateManager.isDefault();
    }

    @Override
    public void addEventListener(OptionEventListener<T> listener) {
        this.listeners.add(listener);
    }

    @Override
    @Deprecated
    public void addListener(BiConsumer<Option<T>, T> changedListener) {
        addEventListener((opt, event) -> changedListener.accept(opt, opt.pendingValue()));
    }

    public void triggerListener(OptionEventListener.Event event, boolean allowDepth) {
        if (allowDepth || currentListenerDepth == 0) {
            Validate.isTrue(
                    currentListenerDepth <= 10,
                    "Listener depth exceeded 10! Possible cyclic listener pattern: a listener triggered an event that triggered the initial event etc etc."
            );

            currentListenerDepth++;

            for (OptionEventListener<T> listener : listeners) {
                listener.onEvent(this, event);
            }

            currentListenerDepth--;
        }
    }

    static <T> LinkedOptionBuilderImpl<T> createBuilder() {
        return new LinkedOptionImpl.LinkedOptionBuilderImpl<>();
    }

    public boolean linkedOptionsSynced() {
        for (Option<T> option : linkedOptions) {
            if (!option.stateManager().get().equals(this.stateManager.get())) {
                return false;
            }
        }
        return true;
    }


    public static class LinkedOptionBuilderImpl<T> implements Builder<T> {
        private Text name = Text.literal("Name not specified!").formatted(Formatting.RED);

        private Function<T, OptionDescription> descriptionFunction = pending -> OptionDescription.EMPTY;

        private Function<Option<T>, Controller<T>> controlGetter;

        private List<Option<T>> linkedOptions = new ArrayList<>();

        private boolean available = true;

        private final Set<OptionFlag> flags = new HashSet<>();

        private final List<OptionEventListener<T>> listeners = new ArrayList<>();

        private @Nullable Binding<T> binding;
        private boolean instantDeprecated = false;

        private @Nullable StateManager<T> stateManager;

        @Override
        public LinkedOptionBuilderImpl<T> name(@NotNull Text name) {
            Validate.notNull(name, "`name` cannot be null");

            this.name = name;
            return this;
        }

        @Override
        public LinkedOptionBuilderImpl<T> description(@NotNull OptionDescription description) {
            return description(opt -> description);
        }

        @Override
        public LinkedOptionBuilderImpl<T> description(@NotNull Function<T, OptionDescription> descriptionFunction) {
            this.descriptionFunction = descriptionFunction;
            return this;
        }

        public LinkedOptionBuilderImpl<T> linkedOptions(@NotNull List<Option<T>> linkedOptions) {
            this.linkedOptions = linkedOptions;
            return this;
        }

        @Override
        public LinkedOptionBuilderImpl<T> controller(@NotNull Function<Option<T>, ControllerBuilder<T>> controllerBuilder) {
            Validate.notNull(controllerBuilder, "`controllerBuilder` cannot be null");

            return customController(opt -> controllerBuilder.apply(opt).build());
        }

        @Override
        public LinkedOptionBuilderImpl<T> customController(@NotNull Function<Option<T>, Controller<T>> control) {
            Validate.notNull(control, "`control` cannot be null");

            this.controlGetter = control;
            return this;
        }

        @Override
        public LinkedOptionBuilderImpl<T> stateManager(@NotNull StateManager<T> stateManager) {
            Validate.notNull(stateManager, "`stateManager` cannot be null");

            Validate.isTrue(binding == null, "Cannot set state manager when binding is set");
            Validate.isTrue(!instantDeprecated, "Cannot set state manager when instant is set");

            this.stateManager = stateManager;
            return this;
        }

        @Override
        public LinkedOptionBuilderImpl<T> binding(@NotNull Binding<T> binding) {
            Validate.notNull(binding, "`binding` cannot be null");
            Validate.isTrue(stateManager == null, "Cannot set binding when state manager is set");

            this.binding = binding;
            return this;
        }

        @Override
        public LinkedOptionBuilderImpl<T> binding(@NotNull T def, @NotNull Supplier<@NotNull T> getter, @NotNull Consumer<@NotNull T> setter) {
            Validate.notNull(def, "`def` must not be null");
            Validate.notNull(getter, "`getter` must not be null");
            Validate.notNull(setter, "`setter` must not be null");

            return binding(Binding.generic(def, getter, setter));
        }

        @Override
        public LinkedOptionBuilderImpl<T> available(boolean available) {
            this.available = available;
            return this;
        }

        @Override
        public LinkedOptionBuilderImpl<T> flag(@NotNull OptionFlag... flag) {
            Validate.notNull(flag, "`flag` must not be null");

            this.flags.addAll(Arrays.asList(flag));
            return this;
        }

        @Override
        public LinkedOptionBuilderImpl<T> flags(@NotNull Collection<? extends OptionFlag> flags) {
            Validate.notNull(flags, "`flags` must not be null");

            this.flags.addAll(flags);
            return this;
        }

        @Override
        @Deprecated
        public LinkedOptionBuilderImpl<T> instant(boolean instant) {
            Validate.isTrue(stateManager == null, "Cannot set instant when state manager is set");
            YACLConstants.LOGGER.error("Option.Builder#instant is deprecated behaviour. Please use a custom state manager instead: `.state(StateManager.createInstant(Binding))`");

            this.instantDeprecated = instant;
            return this;
        }

        @Override
        public LinkedOptionBuilderImpl<T> addListener(@NotNull OptionEventListener<T> listener) {
            Validate.notNull(listener, "`listener` must not be null");

            this.listeners.add(listener);
            return this;
        }

        @Override
        public LinkedOptionBuilderImpl<T> addListeners(@NotNull Collection<@NotNull OptionEventListener<T>> optionEventListeners) {
            Validate.notNull(optionEventListeners, "`optionEventListeners` must not be null");

            this.listeners.addAll(optionEventListeners);
            return this;
        }

        @Override
        public LinkedOptionBuilderImpl<T> listener(@NotNull BiConsumer<Option<T>, T> listener) {
            Validate.notNull(listener, "`listener` must not be null");

            return this.addListener((opt, event) -> listener.accept(opt, opt.pendingValue()));
        }

        @Override
        public LinkedOptionBuilderImpl<T> listeners(@NotNull Collection<BiConsumer<Option<T>, T>> listeners) {
            Validate.notNull(listeners, "`listeners` must not be null");

            this.addListeners(listeners.stream()
                    .map(listener ->
                            (OptionEventListener<T>) (opt, event) ->
                                    listener.accept(opt, opt.pendingValue())
                    ).toList()
            );
            return this;
        }

        @Override
        public LinkedOptionImpl<T> build() {
            Validate.notNull(controlGetter, "`control` must not be null when building `Option`");

            if (instantDeprecated) {
                if (binding == null) {
                    throw new IllegalStateException("Cannot build option with instant when binding is not set");
                }
                Validate.isTrue(flags.isEmpty(), "instant application does not support option flags");

                this.stateManager = StateManager.createInstant(binding);
            } else if (binding != null) {
                stateManager = StateManager.createSimple(binding);
            }
            Validate.notNull(stateManager, "State manager must be set, either by using .binding() to create a simple manager or .state() to create an advanced one");

            Validate.isTrue(!stateManager.isAlwaysSynced() || flags.isEmpty(), "Always synced state managers do not support option flags.");

            return new LinkedOptionImpl<>(name, descriptionFunction, controlGetter, stateManager, available, ImmutableSet.copyOf(flags), listeners, linkedOptions);
        }
    }
}
