package invoker;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Создаёт значения аргументов по типам параметров метода.
 * Никогда не возвращает null: если значение создать нельзя, выбрасывает исключение.
 */
public final class ArgumentFactory {

    private static final int MAX_DEPTH = 4;
    private static final int ARRAY_LENGTH = 2;

    private final Map<Class<?>, Supplier<Object>> defaults = new HashMap<>();

    public ArgumentFactory() {
        register(int.class, Integer.class, 1);
        register(long.class, Long.class, 1L);
        register(short.class, Short.class, (short) 1);
        register(byte.class, Byte.class, (byte) 1);
        register(double.class, Double.class, 1.0);
        register(float.class, Float.class, 1.0f);
        register(boolean.class, Boolean.class, true);
        register(char.class, Character.class, 'a');

        defaults.put(String.class, () -> "test");
        defaults.put(CharSequence.class, () -> "test");
        defaults.put(Object.class, Object::new);
        defaults.put(List.class, ArrayList::new);
        defaults.put(Collection.class, ArrayList::new);
        defaults.put(Iterable.class, ArrayList::new);
        defaults.put(Set.class, HashSet::new);
        defaults.put(Map.class, HashMap::new);
    }

    public Object[] createArguments(Method method) {
        Class<?>[] types = method.getParameterTypes();
        Object[] arguments = new Object[types.length];
        for (int i = 0; i < types.length; i++) {
            arguments[i] = create(types[i], 0);
        }
        return arguments;
    }

    private void register(Class<?> primitive, Class<?> wrapper, Object value) {
        defaults.put(primitive, () -> value);
        defaults.put(wrapper, () -> value);
    }

    private Object create(Class<?> type, int depth) {
        Supplier<Object> supplier = defaults.get(type);
        if (supplier != null) {
            return supplier.get();
        }
        if (type.isArray()) {
            return createArray(type.getComponentType(), depth);
        }
        if (type.isEnum()) {
            return firstEnumConstant(type);
        }
        if (depth >= MAX_DEPTH) {
            throw cannotCreate(type, "слишком глубокая вложенность");
        }
        return createWithConstructor(type, depth);
    }

    private Object createArray(Class<?> componentType, int depth) {
        Object array = Array.newInstance(componentType, ARRAY_LENGTH);
        for (int i = 0; i < ARRAY_LENGTH; i++) {
            Array.set(array, i, create(componentType, depth + 1));
        }
        return array;
    }

    private Object firstEnumConstant(Class<?> type) {
        Object[] constants = type.getEnumConstants();
        if (constants.length == 0) {
            throw cannotCreate(type, "в перечислении нет констант");
        }
        return constants[0];
    }

    private Object createWithConstructor(Class<?> type, int depth) {
        if (type.isInterface() || Modifier.isAbstract(type.getModifiers())) {
            throw cannotCreate(type, "интерфейс или абстрактный класс");
        }
        Constructor<?>[] constructors = type.getDeclaredConstructors();
        Arrays.sort(constructors, (a, b) -> Integer.compare(a.getParameterCount(), b.getParameterCount()));

        for (Constructor<?> constructor : constructors) {
            try {
                Class<?>[] parameterTypes = constructor.getParameterTypes();
                Object[] arguments = new Object[parameterTypes.length];
                for (int i = 0; i < parameterTypes.length; i++) {
                    arguments[i] = create(parameterTypes[i], depth + 1);
                }
                constructor.setAccessible(true);
                return constructor.newInstance(arguments);
            } catch (ReflectiveOperationException | RuntimeException e) {

            }
        }
        throw cannotCreate(type, "не найден подходящий конструктор");
    }

    private static MethodInvocationException cannotCreate(Class<?> type, String reason) {
        return new MethodInvocationException(
                "Не удалось создать аргумент типа " + type.getName() + ": " + reason);
    }
}