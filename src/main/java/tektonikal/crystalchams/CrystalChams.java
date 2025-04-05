package tektonikal.crystalchams;

import dev.isxander.yacl3.api.Option;
import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Unique;
import tektonikal.crystalchams.annotation.Updatable;
import tektonikal.crystalchams.config.ChamsConfig;

import java.awt.*;
import java.util.Arrays;

public class CrystalChams implements ModInitializer {
    public static float getYOffset(int age, float tickDelta, float offset, float bounceSpeed, float bounceHeight) {
        float f = (float) age + tickDelta;
        float g = MathHelper.sin(f * bounceSpeed) / 2.0F + 0.5F;
        g = (g * g + g) * bounceHeight;
        return g - 1.4F + offset;
    }

    public static Color getRainbowCol(int delay, int speed) {
        return getRainbow(-((System.currentTimeMillis() + delay) % 10000L / 10000.0f) * speed);
    }

    //https://github.com/Splzh/ClearHitboxes/blob/main/src/main/java/splash/utils/ColorUtils.java !!
    private static Color getRainbow(double percent) {
        double offset = Math.PI * 2 / 3;
        double pos = percent * (Math.PI * 2);
        float red = (float) ((Math.sin(pos) * 127) + 128);
        float green = (float) ((Math.sin(pos + offset) * 127) + 128);
        float blue = (float) ((Math.sin(pos + offset * 2) * 127) + 128);
        return new Color((int) (red), (int) (green), (int) (blue), 255);
    }

    @Override
    public void onInitialize() {
        ChamsConfig.CONFIG.load();
        armSecuritySystem();
        unleashHell();
    }
    private static void armSecuritySystem() {
        //can't add listeners while options are created for my use-case, since not everything is fully initialized
        Arrays.stream(ChamsConfig.class.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Updatable.class))
                .forEach(field -> {
                    try {
                        ((Option<Boolean>) field.get(null)).addListener(ChamsConfig::update);
                    } catch (Exception x) {
                        throw new RuntimeException(x);
                    }
                });
    }
    @SuppressWarnings("rawtypes")
    public static void unleashHell() {
        Arrays.stream(ChamsConfig.class.getDeclaredFields())
                .filter(field -> field.getName().startsWith("o_"))
                .forEach(field -> {
                    try {
                        Object value = field.get(null);
                        ((Option) value).requestSet(((Option<?>) value).binding().getValue());
                    } catch (IllegalAccessException e) {
                        System.out.println("what the hell");
                    }
                });
    }
}
