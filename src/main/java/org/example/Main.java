package org.example;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class Main {
    public static void main(String[] args) throws ReflectiveOperationException {
        Sample sample = new Sample();
        for (Method method : Sample.class.getDeclaredMethods()) {
            int modifiers = method.getModifiers();
            if (!method.isAnnotationPresent(Repeat.class)
                    || !(Modifier.isProtected(modifiers) || Modifier.isPrivate(modifiers))) {
                continue;
            }
            method.setAccessible(true);
            Object[] arguments = createArguments(method.getParameterTypes());
            for (int i = 0; i < method.getAnnotation(Repeat.class).value(); i++) {
                method.invoke(sample, arguments);
            }
        }
    }

    private static Object[] createArguments(Class<?>[] types) throws ReflectiveOperationException {
        Object[] arguments = new Object[types.length];
        for (int i = 0; i < types.length; i++) {
            arguments[i] = createValue(types[i]);
        }
        return arguments;
    }

    private static Object createValue(Class<?> type) throws ReflectiveOperationException {
        if (type.isPrimitive()) {
            return Array.get(Array.newInstance(type, 1), 0);
        }
        if (type.isArray()) {
            return Array.newInstance(type.getComponentType(), 0);
        }
        return type.getDeclaredConstructor().newInstance();
    }
}
