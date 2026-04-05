package tektonikal.crystalchams.config;

import dev.isxander.yacl3.api.NameableEnum;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import tektonikal.crystalchams.CrystalChams;

import java.util.function.BiFunction;
import java.util.function.Function;

public enum RenderMode implements NameableEnum {
    DEFAULT(true, (identifier, aBoolean) ->  RenderLayers.entityTranslucent(identifier));
//    GATEWAY(true, CrystalChams.CUSTOM_END_GATEWAY),
//    WIREFRAME(false, CrystalChams.CUSTOM_DEBUG_LINE_STRIP),
//    NOTEX(true, CrystalChams.CUSTOM_ENTITY_NOTEX),
//    IMAGE(true, CrystalChams.CUSTOM_IMAGE_FUNC);

    final boolean canCull;
    Function<Double, RenderLayer> function;
    BiFunction<Identifier, Boolean, RenderLayer> biFunction;


    RenderMode(boolean canCull, BiFunction<Identifier, Boolean, RenderLayer> function) {
        this.canCull = canCull;
        this.biFunction = function;
    }
    RenderMode(boolean canCull, Function<Double, RenderLayer> function) {
        this.canCull = canCull;
        this.function = function;
    }
    public boolean canCull() {
        return this.canCull;
    }

    public BiFunction<Identifier, Boolean, RenderLayer> getBiFunction() {
        return biFunction;
    }
    public Function<Double, RenderLayer> getFunction() {
        return function;
    }

    @Override
    public Text getDisplayName() {
        return switch (this) {
            case DEFAULT -> Text.translatable("config.renderMode.default");
//            case GATEWAY -> Text.translatable("config.renderMode.gateway");
//            case WIREFRAME -> Text.translatable("config.renderMode.wireframe");
//            case NOTEX -> Text.translatable("config.renderMode.noTexture");
//            case IMAGE -> Text.translatable("config.renderMode.image");
        };
    }
}
