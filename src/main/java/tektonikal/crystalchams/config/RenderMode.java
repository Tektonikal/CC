package tektonikal.crystalchams.config;

import dev.isxander.yacl3.api.NameableEnum;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.util.function.BiFunction;
import java.util.function.Function;

public enum RenderMode implements NameableEnum {
    DEFAULT(true, (identifier, aBoolean) -> RenderTypes.entityTranslucent(identifier));
//    GATEWAY(true, CrystalChams.CUSTOM_END_GATEWAY),
//    WIREFRAME(false, CrystalChams.CUSTOM_DEBUG_LINE_STRIP),
//    NOTEX(true, CrystalChams.CUSTOM_ENTITY_NOTEX),
//    IMAGE(true, CrystalChams.CUSTOM_IMAGE_FUNC);

    final boolean canCull;
    Function<Double, RenderType> function;
    BiFunction<Identifier, Boolean, RenderType> biFunction;


    RenderMode(boolean canCull, BiFunction<Identifier, Boolean, RenderType> function) {
        this.canCull = canCull;
        this.biFunction = function;
    }

    RenderMode(boolean canCull, Function<Double, RenderType> function) {
        this.canCull = canCull;
        this.function = function;
    }

    public boolean canCull() {
        return this.canCull;
    }

    public BiFunction<Identifier, Boolean, RenderType> getBiFunction() {
        return biFunction;
    }

    public Function<Double, RenderType> getFunction() {
        return function;
    }

    @Override
    public Component getDisplayName() {
        return switch (this) {
            case DEFAULT -> Component.translatable("config.renderMode.default");
//            case GATEWAY -> Text.translatable("config.renderMode.gateway");
//            case WIREFRAME -> Text.translatable("config.renderMode.wireframe");
//            case NOTEX -> Text.translatable("config.renderMode.noTexture");
//            case IMAGE -> Text.translatable("config.renderMode.image");
        };
    }
}
