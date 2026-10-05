package lab2.invoker;

import lab2.annotation.Repeat;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;

public class AnnotationInvoker {
    private static final Map<Class<?>, Object> DEFAULTS = Map.ofEntries(
            Map.entry(int.class, 3),
            Map.entry(Integer.class, 3),
            Map.entry(long.class, 10L),
            Map.entry(Long.class, 10L),
            Map.entry(double.class, 1.5),
            Map.entry(Double.class, 1.5),
            Map.entry(float.class, 2.5f),
            Map.entry(Float.class, 2.5f),
            Map.entry(boolean.class, true),
            Map.entry(Boolean.class, true),
            Map.entry(char.class, 'a'),
            Map.entry(Character.class, 'a'),
            Map.entry(byte.class, (byte) 1),
            Map.entry(Byte.class, (byte) 1),
            Map.entry(short.class, (short) 2),
            Map.entry(Short.class, (short) 2),
            Map.entry(String.class, "sector a")
    );

    public void invokeAnnotated(Object target) throws ReflectiveOperationException {
        Method[] methods = target.getClass().getDeclaredMethods();
        Arrays.sort(methods, Comparator.comparing(Method::getName));

        for (Method method : methods) {
            Repeat repeat = method.getAnnotation(Repeat.class);
            int modifiers = method.getModifiers();
            if (repeat == null || !(Modifier.isProtected(modifiers) || Modifier.isPrivate(modifiers))) {
                continue;
            }

            method.setAccessible(true);
            Object[] args = new Object[method.getParameterCount()];
            Class<?>[] types = method.getParameterTypes();
            for (int i = 0; i < args.length; i++) {
                args[i] = valueFor(types[i]);
            }

            System.out.println("invoke " + method.getName() + " x" + repeat.value());
            for (int i = 0; i < repeat.value(); i++) {
                method.invoke(target, args);
            }
        }
    }

    private Object valueFor(Class<?> type) throws ReflectiveOperationException {
        Object value = DEFAULTS.get(type);
        if (value != null) {
            return value;
        }
        if (type.isArray()) {
            return java.lang.reflect.Array.newInstance(type.getComponentType(), 0);
        }
        if (type.isEnum() && type.getEnumConstants().length > 0) {
            return type.getEnumConstants()[0];
        }

        Constructor<?>[] constructors = type.getDeclaredConstructors();
        if (constructors.length == 0) {
            throw new NoSuchMethodException("no constructor found for " + type.getName());
        }

        Constructor<?> ctor = Arrays.stream(constructors)
                .min(Comparator.comparingInt(Constructor::getParameterCount))
                .orElseThrow();
        ctor.setAccessible(true);
        Class<?>[] paramTypes = ctor.getParameterTypes();
        Object[] args = new Object[paramTypes.length];
        for (int i = 0; i < args.length; i++) {
            args[i] = valueFor(paramTypes[i]);
        }
        return ctor.newInstance(args);
    }
}
