package tektonikal.crystalchams.config;

import eu.midnightdust.lib.config.MidnightConfig;

public class ChamsConfig extends MidnightConfig {
    @Comment(centered = true)
    public static String general = "";
    @Entry(name = "Enabled")
    public static boolean isActive = true;
    @Entry(name = "Render mode")
    public static RenderMode mode = RenderMode.DEFAULT;
    @Entry(name = "Scale", min = 0.1f, max = 2.5f,isSlider = true)
    public static float scale = 2.0f;
    @Entry(name = "Bounce height", min = 0f, max = 1f,isSlider = true)
    public static float bounce = 0.4f;
    @Entry(name = "Height Offset", min = -1f, max = 1f,isSlider = true)
    public static float offset = 0f;
    @Entry(name = "Rotation Speed", min = 0f, max = 25f,isSlider = true)
    public static float rotSpeed = 1f;
    @Entry(name = "Bounce speed", min = 0f, max = 1,isSlider = true)
    public static float BounceSpeed = 0.2f;
    @Entry(name = "Light level", min = -1, max = 255, isSlider = true)
    public static int lLevel = -1;
    @Comment(centered = true)
    public static String core = "";
    @Entry(name = "Render core")
    public static boolean renderCore = true;
    @Entry(name = "Color", isColor = true)
    public static String col = "#ffffff";
    @Entry(name = "Core Alpha", min = 0f, max = 1f,isSlider = true)
    public static float alpha = 1;
    @Comment(centered = true)
    public static String f1 = "";
    @Entry(name = "Render frame 1")
    public static boolean renderFrame1 = true;
    @Entry(name = "Frame 1 Color", isColor = true)
    public static String frameCol = "#ffffff";
    @Entry(name = "Frame 1 Alpha", min = 0f, max = 1f,isSlider = true)
    public static float frame1Alpha = 1f;
    @Comment(centered = true)
    public static String f2 = "";
    @Entry(name = "Render Frame 2")
    public static boolean renderFrame2 = true;
    @Entry(name = "Frame 2 Color", isColor = true)
    public static String frameCol2 = "#ffffff";
    @Entry(name = "Frame 2 Alpha", min = 0f, max = 1f, isSlider = true)
    public static float frame2Alpha = 1;
    @Comment(centered = true)
    public static String shadowC = "";
    @Entry(name = "Shadow size", isSlider = true, min = 0, max = 1.5F)
    public static float shadow = 0.5F;
    @Entry(name = "Shadow opacity", isSlider = true, min =0, max = 1)
    public static float shadowOpacity = 1;

    public enum RenderMode {
        DEFAULT,
        PORTAL,
        GATEWAY,
        WIREFRAME,
        CULLED,
    }
}
