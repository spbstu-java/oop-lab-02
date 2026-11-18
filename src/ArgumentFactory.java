import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

final class ArgumentFactory {
    Object[] createArguments(Class<?>[] parameterTypes) throws ReflectiveOperationException {
        return createArguments(parameterTypes, new HashSet<>());
    }

    private Object[] createArguments(Class<?>[] parameterTypes, Set<Class<?>> constructing)
            throws ReflectiveOperationException {
        Object[] arguments = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            arguments[i] = createValue(parameterTypes[i], constructing);
        }
        return arguments;
    }

    private Object createValue(Class<?> type, Set<Class<?>> constructing)
            throws ReflectiveOperationException {
        if (type.isPrimitive()) {
            // Массив даёт стандартное значение любого примитива с нужной обёрткой.
            return Array.get(Array.newInstance(type, 1), 0);
        }
        if (type.isArray()) {
            return Array.newInstance(type.getComponentType(), 0);
        }
        if (type.isEnum()) {
            Object[] constants = type.getEnumConstants();
            return constants.length == 0 ? null : constants[0];
        }
        if (type.isInterface() || Modifier.isAbstract(type.getModifiers())) {
            return null;
        }

        if (!constructing.add(type)) {
            throw new ReflectiveOperationException("Цикл в параметрах конструктора: " + type.getName());
        }
        try {
            return construct(type, constructing);
        } finally {
            constructing.remove(type);
        }
    }

    private Object construct(Class<?> type, Set<Class<?>> constructing)
            throws ReflectiveOperationException {
        Constructor<?>[] constructors = type.getDeclaredConstructors();
        Arrays.sort(constructors, Comparator.comparingInt(Constructor::getParameterCount));

        ReflectiveOperationException failure =
                new ReflectiveOperationException("Не удалось создать объект " + type.getName());
        for (Constructor<?> constructor : constructors) {
            if (!constructor.trySetAccessible()) {
                continue;
            }
            try {
                Object[] arguments = createArguments(constructor.getParameterTypes(), constructing);
                return constructor.newInstance(arguments);
            } catch (ReflectiveOperationException e) {
                failure.addSuppressed(e);
            }
        }
        throw failure;
    }
}
