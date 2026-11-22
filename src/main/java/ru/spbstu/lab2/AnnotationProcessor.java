
package main.java.ru.spbstu.lab2;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.Objects;
import java.util.function.Function;

public class AnnotationProcessor {

    public void process(
            Object object,
            Function<Parameter, Object> argumentResolver
    ) {
        if (object == null) {
            throw new IllegalArgumentException(
                    "Обрабатываемый объект не должен быть null."
            );
        }

        Objects.requireNonNull(
                argumentResolver,
                "Обработчик аргументов не должен быть null."
        );

        Method[] methods = object.getClass().getDeclaredMethods();

        for (Method method : methods) {

            Repeat repeat = method.getAnnotation(Repeat.class);

            if (repeat == null) {
                continue;
            }

            int modifiers = method.getModifiers();

            if (!Modifier.isProtected(modifiers)
                    && !Modifier.isPrivate(modifiers)) {
                continue;
            }

            if (repeat.value() < 0) {
                throw new IllegalArgumentException(
                        "Число повторений не может быть отрицательным."
                );
            }

            method.setAccessible(true);

            Parameter[] parameters = method.getParameters();

            Object[] arguments = new Object[parameters.length];

            for (int i = 0; i < parameters.length; i++) {

                Parameter parameter = parameters[i];

                Object argument = argumentResolver.apply(parameter);

                if (argument == null) {
                    throw new IllegalArgumentException(
                            "Аргумент не должен быть null: "
                                    + parameter.getType().getName()
                    );
                }

                Class<?> parameterType = parameter.getType();

                if (!isCompatible(parameterType, argument.getClass())) {
                    throw new IllegalArgumentException(
                            "Неверный тип аргумента для параметра: "
                                    + parameterType.getName()
                    );
                }

                arguments[i] = argument;
            }

            System.out.println(
                    "\nМетод " + method.getName()
                            + " будет вызван "
                            + repeat.value() + " раз(а)."
            );

            for (int i = 0; i < repeat.value(); i++) {
                try {
                    method.invoke(object, arguments);

                } catch (IllegalAccessException
                         | InvocationTargetException e) {

                    throw new RuntimeException(
                            "Ошибка вызова метода " + method.getName(),
                            e
                    );
                }
            }
        }
    }

    private boolean isCompatible(
            Class<?> parameterType,
            Class<?> argumentType
    ) {
        if (parameterType.isAssignableFrom(argumentType)) {
            return true;
        }

        if (parameterType == int.class) return argumentType == Integer.class;
        if (parameterType == double.class) return argumentType == Double.class;
        if (parameterType == long.class) return argumentType == Long.class;
        if (parameterType == float.class) return argumentType == Float.class;
        if (parameterType == boolean.class) return argumentType == Boolean.class;
        if (parameterType == char.class) return argumentType == Character.class;
        if (parameterType == byte.class) return argumentType == Byte.class;
        if (parameterType == short.class) return argumentType == Short.class;

        return false;
    }
}
