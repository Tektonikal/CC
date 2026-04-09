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
    public float delay = 0;

    public float scale = 1;
    public Color color = Color.WHITE;
    public float alpha = 1;
    public int blockLightLevel = -1;
    public int skyLightLevel = -1;
    public RenderMode renderLayer = RenderMode.DEFAULT;
    public boolean culling = false;


    public boolean rainbow = false;
    public float rainbowSpeed = 2;
    public float rainbowDelay = 0;
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

    public ModelPartOptions() {
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ModelPartOptions that = (ModelPartOptions) o;
        return render == that.render && Float.compare(offset, that.offset) == 0 && Float.compare(rotationSpeed, that.rotationSpeed) == 0 && Float.compare(bounceHeight, that.bounceHeight) == 0 && Float.compare(bounceSpeed, that.bounceSpeed) == 0 && Float.compare(delay, that.delay) == 0 && Float.compare(scale, that.scale) == 0 && Float.compare(alpha, that.alpha) == 0 && blockLightLevel == that.blockLightLevel && skyLightLevel == that.skyLightLevel && culling == that.culling && rainbow == that.rainbow && Float.compare(rainbowSpeed, that.rainbowSpeed) == 0 && Float.compare(rainbowDelay, that.rainbowDelay) == 0 && Float.compare(rainbowSaturation, that.rainbowSaturation) == 0 && Float.compare(rainbowBrightness, that.rainbowBrightness) == 0 && animation == that.animation && animateVerticalOffset == that.animateVerticalOffset && Float.compare(startingVerticalOffset, that.startingVerticalOffset) == 0 && Float.compare(verticalOffsetAnimationDuration, that.verticalOffsetAnimationDuration) == 0 && Float.compare(verticalOffsetAnimationDelay, that.verticalOffsetAnimationDelay) == 0 && animateRotationSpeed == that.animateRotationSpeed && Float.compare(startingRotationSpeed, that.startingRotationSpeed) == 0 && Float.compare(rotationSpeedAnimationDuration, that.rotationSpeedAnimationDuration) == 0 && Float.compare(rotationSpeedAnimationDelay, that.rotationSpeedAnimationDelay) == 0 && animateBounceHeight == that.animateBounceHeight && Float.compare(startingBounceHeight, that.startingBounceHeight) == 0 && Float.compare(bounceHeightAnimationDuration, that.bounceHeightAnimationDuration) == 0 && Float.compare(bounceHeightAnimationDelay, that.bounceHeightAnimationDelay) == 0 && animateBounceSpeed == that.animateBounceSpeed && Float.compare(startingBounceSpeed, that.startingBounceSpeed) == 0 && Float.compare(bounceSpeedAnimationDuration, that.bounceSpeedAnimationDuration) == 0 && Float.compare(bounceSpeedAnimationDelay, that.bounceSpeedAnimationDelay) == 0 && animateTickDelay == that.animateTickDelay && Float.compare(startingTickDelay, that.startingTickDelay) == 0 && Float.compare(tickDelayAnimationDuration, that.tickDelayAnimationDuration) == 0 && Float.compare(tickDelayAnimationDelay, that.tickDelayAnimationDelay) == 0 && animateScale == that.animateScale && Float.compare(startingScale, that.startingScale) == 0 && Float.compare(scaleAnimationDuration, that.scaleAnimationDuration) == 0 && Float.compare(scaleAnimationDelay, that.scaleAnimationDelay) == 0 && animateColor == that.animateColor && Float.compare(colorAnimationDuration, that.colorAnimationDuration) == 0 && Float.compare(colorAnimationDelay, that.colorAnimationDelay) == 0 && animateAlpha == that.animateAlpha && Float.compare(startingAlpha, that.startingAlpha) == 0 && Float.compare(alphaAnimationDuration, that.alphaAnimationDuration) == 0 && Float.compare(alphaAnimationDelay, that.alphaAnimationDelay) == 0 && animateRainbowSpeed == that.animateRainbowSpeed && Float.compare(startingRainbowSpeed, that.startingRainbowSpeed) == 0 && Float.compare(rainbowSpeedAnimationDuration, that.rainbowSpeedAnimationDuration) == 0 && Float.compare(rainbowSpeedAnimationDelay, that.rainbowSpeedAnimationDelay) == 0 && animateRainbowDelay == that.animateRainbowDelay && Float.compare(startingRainbowDelay, that.startingRainbowDelay) == 0 && Float.compare(rainbowDelayAnimationDuration, that.rainbowDelayAnimationDuration) == 0 && Float.compare(rainbowDelayAnimationDelay, that.rainbowDelayAnimationDelay) == 0 && animateRainbowSaturation == that.animateRainbowSaturation && Float.compare(startingRainbowSaturation, that.startingRainbowSaturation) == 0 && Float.compare(rainbowSaturationAnimationDuration, that.rainbowSaturationAnimationDuration) == 0 && Float.compare(rainbowSaturationAnimationDelay, that.rainbowSaturationAnimationDelay) == 0 && animateRainbowBrightness == that.animateRainbowBrightness && Float.compare(startingRainbowBrightness, that.startingRainbowBrightness) == 0 && Float.compare(rainbowBrightnessAnimationDuration, that.rainbowBrightnessAnimationDuration) == 0 && Float.compare(rainbowBrightnessAnimationDelay, that.rainbowBrightnessAnimationDelay) == 0 && funnyOption == that.funnyOption && funnierOption == that.funnierOption && Objects.equals(color, that.color) && renderLayer == that.renderLayer && verticalOffsetEasing == that.verticalOffsetEasing && rotationSpeedEasing == that.rotationSpeedEasing && bounceHeightEasing == that.bounceHeightEasing && bounceSpeedEasing == that.bounceSpeedEasing && tickDelayEasing == that.tickDelayEasing && scaleEasing == that.scaleEasing && Objects.equals(startingColor, that.startingColor) && colorEasing == that.colorEasing && alphaEasing == that.alphaEasing && rainbowSpeedEasing == that.rainbowSpeedEasing && rainbowDelayEasing == that.rainbowDelayEasing && rainbowSaturationEasing == that.rainbowSaturationEasing && rainbowBrightnessEasing == that.rainbowBrightnessEasing;
    }

    @Override
    public int hashCode() {
        return Objects.hash(render, offset, rotationSpeed, bounceHeight, bounceSpeed, delay, scale, color, alpha, blockLightLevel, skyLightLevel, renderLayer, culling, rainbow, rainbowSpeed, rainbowDelay, rainbowSaturation, rainbowBrightness, animation, animateVerticalOffset, startingVerticalOffset, verticalOffsetAnimationDuration, verticalOffsetAnimationDelay, verticalOffsetEasing, animateRotationSpeed, startingRotationSpeed, rotationSpeedAnimationDuration, rotationSpeedAnimationDelay, rotationSpeedEasing, animateBounceHeight, startingBounceHeight, bounceHeightAnimationDuration, bounceHeightAnimationDelay, bounceHeightEasing, animateBounceSpeed, startingBounceSpeed, bounceSpeedAnimationDuration, bounceSpeedAnimationDelay, bounceSpeedEasing, animateTickDelay, startingTickDelay, tickDelayAnimationDuration, tickDelayAnimationDelay, tickDelayEasing, animateScale, startingScale, scaleAnimationDuration, scaleAnimationDelay, scaleEasing, animateColor, startingColor, colorAnimationDuration, colorAnimationDelay, colorEasing, animateAlpha, startingAlpha, alphaAnimationDuration, alphaAnimationDelay, alphaEasing, animateRainbowSpeed, startingRainbowSpeed, rainbowSpeedAnimationDuration, rainbowSpeedAnimationDelay, rainbowSpeedEasing, animateRainbowDelay, startingRainbowDelay, rainbowDelayAnimationDuration, rainbowDelayAnimationDelay, rainbowDelayEasing, animateRainbowSaturation, startingRainbowSaturation, rainbowSaturationAnimationDuration, rainbowSaturationAnimationDelay, rainbowSaturationEasing, animateRainbowBrightness, startingRainbowBrightness, rainbowBrightnessAnimationDuration, rainbowBrightnessAnimationDelay, rainbowBrightnessEasing, funnyOption, funnierOption);
    }
}
