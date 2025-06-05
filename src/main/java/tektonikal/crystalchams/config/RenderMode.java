package tektonikal.crystalchams.config;

import dev.isxander.yacl3.api.NameableEnum;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import tektonikal.crystalchams.CrystalChams;

import java.util.function.BiFunction;
import java.util.function.Function;

public enum RenderMode implements NameableEnum {
    DEFAULT(true, CrystalChams.CUSTOM_NORMAL),
    GATEWAY(true, CrystalChams.CUSTOM_END_GATEWAY),
    WIREFRAME(false, CrystalChams.CUSTOM_DEBUG_LINE_STRIP),
    NOTEX(true, CrystalChams.CUSTOM_ENTITY_NOTEX),
    IMAGE(true, CrystalChams.CUSTOM_IMAGE_FUNC);

    final boolean canCull;
    Function<Double, RenderLayer.MultiPhase> function;
    BiFunction<Identifier, Boolean, RenderLayer> biFunction;


    RenderMode(boolean canCull, BiFunction<Identifier, Boolean, RenderLayer> function) {
        this.canCull = canCull;
        this.biFunction = function;
    }
    RenderMode(boolean canCull, Function<Double, RenderLayer.MultiPhase> function) {
        this.canCull = canCull;
        this.function = function;
    }
    public boolean canCull() {
        return this.canCull;
    }

    public BiFunction<Identifier, Boolean, RenderLayer> getBiFunction() {
        return biFunction;
    }
    public Function<Double, RenderLayer.MultiPhase> getFunction() {
        return function;
    }

    @Override
    public Text getDisplayName() {
        return switch (this) {
            case DEFAULT -> Text.of("Default");
            case GATEWAY -> Text.of("Gateway");
            case WIREFRAME -> Text.of("Wireframe");
            case NOTEX -> Text.of("No Texture Color");
            case IMAGE -> Text.of("Image");
        };
    }
}
