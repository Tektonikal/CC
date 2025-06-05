package tektonikal.crystalchams.config;

import com.google.common.collect.ImmutableList;
import dev.isxander.yacl3.api.ListOption;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.OptionGroup;
import dev.isxander.yacl3.impl.OptionGroupImpl;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.minecraft.text.Text;

public final class MutableOptionGroupImpl implements OptionGroup {
    private final @NotNull Text name;
    private final @NotNull OptionDescription description;
    public final ArrayList<Option<?>> options;
    private final boolean collapsed;
    private final boolean isRoot;

    public MutableOptionGroupImpl(@NotNull Text name, @NotNull OptionDescription description, ArrayList<Option<?>> options, boolean collapsed, boolean isRoot) {
        this.name = name;
        this.description = description;
        this.options = options;
        this.collapsed = collapsed;
        this.isRoot = isRoot;
    }

    @Override
    public @NotNull Text name() {
        return name;
    }

    @Override
    public OptionDescription description() {
        return description;
    }

    @Override
    public @NotNull Text tooltip() {
        return description.text();
    }

    @Override
    public @NotNull ImmutableList<Option<?>> options() {
        return ImmutableList.copyOf(options);
    }

    static Builder createBuilder() {
        return new MutableOptionGroupImpl.BuilderImpl();
    }

    @Override
    public boolean collapsed() {
        return collapsed;
    }

    @Override
    public boolean isRoot() {
        return isRoot;
    }

    @ApiStatus.Internal
    public static final class BuilderImpl implements Builder {
        private Text name = Text.empty();
        private OptionDescription description = OptionDescription.EMPTY;
        private final List<Option<?>> options = new ArrayList<>();
        private boolean collapsed = false;

        @Override
        public Builder name(@NotNull Text name) {
            Validate.notNull(name, "`name` must not be null");

            this.name = name;
            return this;
        }

        @Override
        public Builder description(@NotNull OptionDescription description) {
            Validate.notNull(description, "`description` must not be null");

            this.description = description;
            return this;
        }

        @Override
        public Builder option(@NotNull Option<?> option) {
            Validate.notNull(option, "`option` must not be null");

            if (option instanceof ListOption<?>)
                throw new UnsupportedOperationException("List options must not be added as an option but a group!");

            this.options.add(option);
            return this;
        }

        @Override
        public Builder options(@NotNull Collection<? extends Option<?>> options) {
            Validate.notEmpty(options, "`options` must not be empty");

            if (options.stream().anyMatch(ListOption.class::isInstance))
                throw new UnsupportedOperationException("List options must not be added as an option but a group!");

            this.options.addAll(options);
            return this;
        }

        @Override
        public Builder collapsed(boolean collapsible) {
            this.collapsed = collapsible;
            return this;
        }

        @Override
        public OptionGroup build() {
            Validate.notEmpty(options, "`options` must not be empty to build `OptionGroup`");

            return new MutableOptionGroupImpl(name, description, new ArrayList<>(options), collapsed, false);
        }
    }
}
