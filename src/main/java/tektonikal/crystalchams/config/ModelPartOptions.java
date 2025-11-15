package tektonikal.crystalchams.config;

import tektonikal.crystalchams.util.Easings;

import java.awt.*;
import java.util.Objects;

public class ModelPartOptions {

    public boolean render = true;
    public float offset = 0;
    public float rotationSpeed = 1;
    public float bounceHeight = 1;
    public float bounceSpeed = 1;
    public float tickDelay = 0;

    public float scale = 1;
    public Color color = Color.WHITE;
    public float alpha = 1;
    public int lightLevel = -1;
    public RenderMode renderLayer = RenderMode.DEFAULT;
    public boolean culling = false;


    public boolean rainbow = false;
    public float rainbowSpeed = 2;
    public int rainbowDelay = 0;
    public float rainbowSaturation = 1;
    public float rainbowBrightness = 1;

    public boolean animation = false;

    public boolean animateVerticalOffset = false;
    public float startingVerticalOffset = 0;
    public float verticalOffsetAnimationDuration = 1.0F;
    public float verticalOffsetAnimationDelay = 0;
    public Easings verticalOffsetEasing = Easings.OFF;

    public boolean animateRotationSpeed = false;
    public float startingRotationSpeed = 1;
    public float rotationSpeedAnimationDuration = 1.0F;
    public float rotationSpeedAnimationDelay = 0;
    public Easings rotationSpeedEasing = Easings.OFF;

    public boolean animateBounceHeight = false;
    public float startingBounceHeight = 1;
    public float bounceHeightAnimationDuration = 1.0F;
    public float bounceHeightAnimationDelay = 0;
    public Easings bounceHeightEasing = Easings.OFF;

    public boolean animateBounceSpeed = false;
    public float startingBounceSpeed = 1;
    public float bounceSpeedAnimationDuration = 1.0F;
    public float bounceSpeedAnimationDelay = 0;
    public Easings bounceSpeedEasing = Easings.OFF;

    public boolean animateTickDelay = false;
    public float startingTickDelay = 0;
    public float tickDelayAnimationDuration = 1.0F;
    public float tickDelayAnimationDelay = 0;
    public Easings tickDelayEasing = Easings.OFF;

    public boolean animateScale = false;
    public float startingScale = 1;
    public float scaleAnimationDuration = 1.0F;
    public float scaleAnimationDelay = 0;
    public Easings scaleEasing = Easings.OFF;

    //TODO: make sure to add edge case cause of -1
    public boolean animateLightLevel = false;
    public int startingLightLevel = -1;
    public float lightLevelAnimationDuration = 1.0F;
    public float lightLevelAnimationDelay = 0;
    public Easings lightLevelEasing = Easings.OFF;

    public boolean animateColor = false;
    public Color startingColor = Color.WHITE;
    public float colorAnimationDuration = 1.0F;
    public float colorAnimationDelay = 0;
    public Easings colorEasing = Easings.OFF;

    public boolean animateAlpha = false;

    public float startingAlpha = 1;
    public float alphaAnimationDuration = 1.0F;
    public float alphaAnimationDelay = 0;
    public Easings alphaEasing = Easings.OFF;

    public boolean animateRainbowSpeed = false;
    public float startingRainbowSpeed = 2;
    public float rainbowSpeedAnimationDuration = 1.0F;
    public float rainbowSpeedAnimationDelay = 0;
    public Easings rainbowSpeedEasing = Easings.OFF;

    public boolean animateRainbowDelay = false;
    public float startingRainbowDelay = 0;
    public float rainbowDelayAnimationDuration = 1.0F;
    public float rainbowDelayAnimationDelay = 0;
    public Easings rainbowDelayEasing = Easings.OFF;

    public boolean animateRainbowSaturation = false;
    public float startingRainbowSaturation = 1;
    public float rainbowSaturationAnimationDuration = 1.0F;
    public float rainbowSaturationAnimationDelay = 0;
    public Easings rainbowSaturationEasing = Easings.OFF;

    public boolean animateRainbowBrightness = false;
    public float startingRainbowBrightness = 1;
    public float rainbowBrightnessAnimationDuration = 1.0F;
    public float rainbowBrightnessAnimationDelay = 0;
    public Easings rainbowBrightnessEasing = Easings.OFF;

    /*
    public boolean animate<NAME> = false;
    public <TYPE> starting<NAME> = <TYPEVAL>;
    public float <NAME>AnimationDuration = 1.0F;
    public float <NAME>AnimationDelay = 0;
    public Easings <NAME>Easing = Easings.OFF;
     */

    public boolean funnyOption = false;
    public boolean funnierOption = false;

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof ModelPartOptions that)) return false;

        return render == that.render && Float.compare(offset, that.offset) == 0 && Float.compare(rotationSpeed, that.rotationSpeed) == 0 && Float.compare(bounceHeight, that.bounceHeight) == 0 && Float.compare(bounceSpeed, that.bounceSpeed) == 0 && Float.compare(tickDelay, that.tickDelay) == 0 && Float.compare(scale, that.scale) == 0 && Float.compare(alpha, that.alpha) == 0 && lightLevel == that.lightLevel && culling == that.culling && rainbow == that.rainbow && Float.compare(rainbowSpeed, that.rainbowSpeed) == 0 && rainbowDelay == that.rainbowDelay && Float.compare(rainbowSaturation, that.rainbowSaturation) == 0 && Float.compare(rainbowBrightness, that.rainbowBrightness) == 0 && animation == that.animation && animateVerticalOffset == that.animateVerticalOffset && Float.compare(startingVerticalOffset, that.startingVerticalOffset) == 0 && Float.compare(verticalOffsetAnimationDuration, that.verticalOffsetAnimationDuration) == 0 && Float.compare(verticalOffsetAnimationDelay, that.verticalOffsetAnimationDelay) == 0 && animateRotationSpeed == that.animateRotationSpeed && Float.compare(startingRotationSpeed, that.startingRotationSpeed) == 0 && Float.compare(rotationSpeedAnimationDuration, that.rotationSpeedAnimationDuration) == 0 && Float.compare(rotationSpeedAnimationDelay, that.rotationSpeedAnimationDelay) == 0 && animateBounceHeight == that.animateBounceHeight && Float.compare(startingBounceHeight, that.startingBounceHeight) == 0 && Float.compare(bounceHeightAnimationDuration, that.bounceHeightAnimationDuration) == 0 && Float.compare(bounceHeightAnimationDelay, that.bounceHeightAnimationDelay) == 0 && animateBounceSpeed == that.animateBounceSpeed && Float.compare(startingBounceSpeed, that.startingBounceSpeed) == 0 && Float.compare(bounceSpeedAnimationDuration, that.bounceSpeedAnimationDuration) == 0 && Float.compare(bounceSpeedAnimationDelay, that.bounceSpeedAnimationDelay) == 0 && animateTickDelay == that.animateTickDelay && Float.compare(startingTickDelay, that.startingTickDelay) == 0 && Float.compare(tickDelayAnimationDuration, that.tickDelayAnimationDuration) == 0 && Float.compare(tickDelayAnimationDelay, that.tickDelayAnimationDelay) == 0 && animateScale == that.animateScale && Float.compare(startingScale, that.startingScale) == 0 && Float.compare(scaleAnimationDuration, that.scaleAnimationDuration) == 0 && Float.compare(scaleAnimationDelay, that.scaleAnimationDelay) == 0 && animateLightLevel == that.animateLightLevel && startingLightLevel == that.startingLightLevel && Float.compare(lightLevelAnimationDuration, that.lightLevelAnimationDuration) == 0 && Float.compare(lightLevelAnimationDelay, that.lightLevelAnimationDelay) == 0 && animateColor == that.animateColor && Float.compare(colorAnimationDuration, that.colorAnimationDuration) == 0 && Float.compare(colorAnimationDelay, that.colorAnimationDelay) == 0 && animateAlpha == that.animateAlpha && Float.compare(startingAlpha, that.startingAlpha) == 0 && Float.compare(alphaAnimationDuration, that.alphaAnimationDuration) == 0 && Float.compare(alphaAnimationDelay, that.alphaAnimationDelay) == 0 && animateRainbowSpeed == that.animateRainbowSpeed && Float.compare(startingRainbowSpeed, that.startingRainbowSpeed) == 0 && Float.compare(rainbowSpeedAnimationDuration, that.rainbowSpeedAnimationDuration) == 0 && Float.compare(rainbowSpeedAnimationDelay, that.rainbowSpeedAnimationDelay) == 0 && animateRainbowDelay == that.animateRainbowDelay && Float.compare(startingRainbowDelay, that.startingRainbowDelay) == 0 && Float.compare(rainbowDelayAnimationDuration, that.rainbowDelayAnimationDuration) == 0 && Float.compare(rainbowDelayAnimationDelay, that.rainbowDelayAnimationDelay) == 0 && animateRainbowSaturation == that.animateRainbowSaturation && Float.compare(startingRainbowSaturation, that.startingRainbowSaturation) == 0 && Float.compare(rainbowSaturationAnimationDuration, that.rainbowSaturationAnimationDuration) == 0 && Float.compare(rainbowSaturationAnimationDelay, that.rainbowSaturationAnimationDelay) == 0 && animateRainbowBrightness == that.animateRainbowBrightness && Float.compare(startingRainbowBrightness, that.startingRainbowBrightness) == 0 && Float.compare(rainbowBrightnessAnimationDuration, that.rainbowBrightnessAnimationDuration) == 0 && Float.compare(rainbowBrightnessAnimationDelay, that.rainbowBrightnessAnimationDelay) == 0 && funnyOption == that.funnyOption && funnierOption == that.funnierOption && color.equals(that.color) && renderLayer == that.renderLayer && verticalOffsetEasing == that.verticalOffsetEasing && rotationSpeedEasing == that.rotationSpeedEasing && bounceHeightEasing == that.bounceHeightEasing && bounceSpeedEasing == that.bounceSpeedEasing && tickDelayEasing == that.tickDelayEasing && scaleEasing == that.scaleEasing && lightLevelEasing == that.lightLevelEasing && startingColor.equals(that.startingColor) && colorEasing == that.colorEasing && alphaEasing == that.alphaEasing && rainbowSpeedEasing == that.rainbowSpeedEasing && rainbowDelayEasing == that.rainbowDelayEasing && rainbowSaturationEasing == that.rainbowSaturationEasing && rainbowBrightnessEasing == that.rainbowBrightnessEasing;
    }

    @Override
    public int hashCode() {
        int result = Boolean.hashCode(render);
        result = 31 * result + Float.hashCode(offset);
        result = 31 * result + Float.hashCode(rotationSpeed);
        result = 31 * result + Float.hashCode(bounceHeight);
        result = 31 * result + Float.hashCode(bounceSpeed);
        result = 31 * result + Float.hashCode(tickDelay);
        result = 31 * result + Float.hashCode(scale);
        result = 31 * result + color.hashCode();
        result = 31 * result + Float.hashCode(alpha);
        result = 31 * result + lightLevel;
        result = 31 * result + renderLayer.hashCode();
        result = 31 * result + Boolean.hashCode(culling);
        result = 31 * result + Boolean.hashCode(rainbow);
        result = 31 * result + Float.hashCode(rainbowSpeed);
        result = 31 * result + rainbowDelay;
        result = 31 * result + Float.hashCode(rainbowSaturation);
        result = 31 * result + Float.hashCode(rainbowBrightness);
        result = 31 * result + Boolean.hashCode(animation);
        result = 31 * result + Boolean.hashCode(animateVerticalOffset);
        result = 31 * result + Float.hashCode(startingVerticalOffset);
        result = 31 * result + Float.hashCode(verticalOffsetAnimationDuration);
        result = 31 * result + Float.hashCode(verticalOffsetAnimationDelay);
        result = 31 * result + verticalOffsetEasing.hashCode();
        result = 31 * result + Boolean.hashCode(animateRotationSpeed);
        result = 31 * result + Float.hashCode(startingRotationSpeed);
        result = 31 * result + Float.hashCode(rotationSpeedAnimationDuration);
        result = 31 * result + Float.hashCode(rotationSpeedAnimationDelay);
        result = 31 * result + rotationSpeedEasing.hashCode();
        result = 31 * result + Boolean.hashCode(animateBounceHeight);
        result = 31 * result + Float.hashCode(startingBounceHeight);
        result = 31 * result + Float.hashCode(bounceHeightAnimationDuration);
        result = 31 * result + Float.hashCode(bounceHeightAnimationDelay);
        result = 31 * result + bounceHeightEasing.hashCode();
        result = 31 * result + Boolean.hashCode(animateBounceSpeed);
        result = 31 * result + Float.hashCode(startingBounceSpeed);
        result = 31 * result + Float.hashCode(bounceSpeedAnimationDuration);
        result = 31 * result + Float.hashCode(bounceSpeedAnimationDelay);
        result = 31 * result + bounceSpeedEasing.hashCode();
        result = 31 * result + Boolean.hashCode(animateTickDelay);
        result = 31 * result + Float.hashCode(startingTickDelay);
        result = 31 * result + Float.hashCode(tickDelayAnimationDuration);
        result = 31 * result + Float.hashCode(tickDelayAnimationDelay);
        result = 31 * result + tickDelayEasing.hashCode();
        result = 31 * result + Boolean.hashCode(animateScale);
        result = 31 * result + Float.hashCode(startingScale);
        result = 31 * result + Float.hashCode(scaleAnimationDuration);
        result = 31 * result + Float.hashCode(scaleAnimationDelay);
        result = 31 * result + scaleEasing.hashCode();
        result = 31 * result + Boolean.hashCode(animateLightLevel);
        result = 31 * result + startingLightLevel;
        result = 31 * result + Float.hashCode(lightLevelAnimationDuration);
        result = 31 * result + Float.hashCode(lightLevelAnimationDelay);
        result = 31 * result + lightLevelEasing.hashCode();
        result = 31 * result + Boolean.hashCode(animateColor);
        result = 31 * result + startingColor.hashCode();
        result = 31 * result + Float.hashCode(colorAnimationDuration);
        result = 31 * result + Float.hashCode(colorAnimationDelay);
        result = 31 * result + colorEasing.hashCode();
        result = 31 * result + Boolean.hashCode(animateAlpha);
        result = 31 * result + Float.hashCode(startingAlpha);
        result = 31 * result + Float.hashCode(alphaAnimationDuration);
        result = 31 * result + Float.hashCode(alphaAnimationDelay);
        result = 31 * result + alphaEasing.hashCode();
        result = 31 * result + Boolean.hashCode(animateRainbowSpeed);
        result = 31 * result + Float.hashCode(startingRainbowSpeed);
        result = 31 * result + Float.hashCode(rainbowSpeedAnimationDuration);
        result = 31 * result + Float.hashCode(rainbowSpeedAnimationDelay);
        result = 31 * result + rainbowSpeedEasing.hashCode();
        result = 31 * result + Boolean.hashCode(animateRainbowDelay);
        result = 31 * result + Float.hashCode(startingRainbowDelay);
        result = 31 * result + Float.hashCode(rainbowDelayAnimationDuration);
        result = 31 * result + Float.hashCode(rainbowDelayAnimationDelay);
        result = 31 * result + rainbowDelayEasing.hashCode();
        result = 31 * result + Boolean.hashCode(animateRainbowSaturation);
        result = 31 * result + Float.hashCode(startingRainbowSaturation);
        result = 31 * result + Float.hashCode(rainbowSaturationAnimationDuration);
        result = 31 * result + Float.hashCode(rainbowSaturationAnimationDelay);
        result = 31 * result + rainbowSaturationEasing.hashCode();
        result = 31 * result + Boolean.hashCode(animateRainbowBrightness);
        result = 31 * result + Float.hashCode(startingRainbowBrightness);
        result = 31 * result + Float.hashCode(rainbowBrightnessAnimationDuration);
        result = 31 * result + Float.hashCode(rainbowBrightnessAnimationDelay);
        result = 31 * result + rainbowBrightnessEasing.hashCode();
        result = 31 * result + Boolean.hashCode(funnyOption);
        result = 31 * result + Boolean.hashCode(funnierOption);
        return result;
    }

    public ModelPartOptions() {
    }
}
