package lab2;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация с целочисленным параметром — указывает, сколько раз должен быть
 * вызван аннотированный метод рефлексионным вызывателем.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Repeat {
    /** Количество повторных вызовов аннотированного метода. */
    int value() default 1;
}