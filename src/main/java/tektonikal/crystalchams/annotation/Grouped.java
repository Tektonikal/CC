package tektonikal.crystalchams.annotation;

import tektonikal.crystalchams.OptionGroups;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Grouped {
    OptionGroups group();
}
