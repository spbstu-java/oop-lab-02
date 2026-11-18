import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Comparator;

public class AnnotatedMethodCaller {

    public void invokeAnnotated(Object target) {
        Method[] methods = target.getClass().getDeclaredMethods();
        Arrays.sort(methods, Comparator.comparing(Method::getName)
                .thenComparingInt(Method::getParameterCount));

        for (Method method : methods) {
            if (method.isSynthetic()) {
                continue;
            }
            Repeat repeat = method.getAnnotation(Repeat.class);
            if (repeat == null) {
                continue;
            }
            if (!isHidden(method)) {
                System.out.println("Пропуск " + describe(method) + ": вызываются только protected и private");
                System.out.println();
                continue;
            }
            invokeRepeatedly(target, method, repeat.times());
        }
    }

    private boolean isHidden(Method method) {
        int modifiers = method.getModifiers();
        return Modifier.isProtected(modifiers) || Modifier.isPrivate(modifiers);
    }

    private void invokeRepeatedly(Object target, Method method, int times) {
        if (times < 0) {
            throw new IllegalArgumentException(
                    "Число повторов для " + method.getName() + " отрицательное: " + times);
        }
        method.setAccessible(true);
        System.out.println(describe(method) + ", повторов: " + times);
        for (int attempt = 1; attempt <= times; attempt++) {
            Object[] args = arguments(method);
            Object result = call(target, method, args);
            System.out.println("  " + attempt + ". аргументы " + Arrays.toString(args));
            System.out.println("     результат: " + result);
        }
        System.out.println();
    }

    private Object call(Object target, Method method, Object[] args) {
        try {
            return method.invoke(target, args);
        } catch (InvocationTargetException exception) {
            Throwable cause = exception.getCause();
            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            }
            throw new IllegalStateException("Метод " + method.getName() + " завершился с ошибкой", cause);
        } catch (IllegalAccessException exception) {
            throw new IllegalStateException("Нет доступа к методу " + method.getName(), exception);
        }
    }

    private Object[] arguments(Method method) {
        Class<?>[] types = method.getParameterTypes();
        Object[] args = new Object[types.length];
        for (int i = 0; i < types.length; i++) {
            args[i] = sample(types[i], i);
        }
        return args;
    }

    private Object sample(Class<?> type, int index) {
        if (type == boolean.class || type == Boolean.class) {
            return true;
        }
        if (type == byte.class || type == Byte.class) {
            return (byte) (1 + index);
        }
        if (type == short.class || type == Short.class) {
            return (short) (2 + index);
        }
        if (type == int.class || type == Integer.class) {
            return 8 + index * 6;
        }
        if (type == long.class || type == Long.class) {
            return 8L + index;
        }
        if (type == float.class || type == Float.class) {
            return 1.5f + index;
        }
        if (type == double.class || type == Double.class) {
            return 2.5 + index;
        }
        if (type == char.class || type == Character.class) {
            return (char) ('а' + index);
        }
        if (type == String.class) {
            return "образец-" + (index + 1);
        }
        if (type.isEnum()) {
            Object[] constants = type.getEnumConstants();
            if (constants.length == 0) {
                throw new IllegalArgumentException("В перечислении " + type.getSimpleName() + " нет констант");
            }
            return constants[index % constants.length];
        }
        if (type.isArray()) {
            Object array = Array.newInstance(type.getComponentType(), 1);
            Array.set(array, 0, sample(type.getComponentType(), index));
            return array;
        }
        return newInstance(type);
    }

    private Object newInstance(Class<?> type) {
        try {
            Constructor<?> constructor = type.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (ReflectiveOperationException exception) {
            throw new IllegalArgumentException(
                    "Не удалось создать аргумент типа " + type.getSimpleName()
                            + ": нужен конструктор без параметров",
                    exception);
        }
    }

    private String describe(Method method) {
        StringBuilder text = new StringBuilder();
        int modifiers = method.getModifiers();
        if (Modifier.isPublic(modifiers)) {
            text.append("public");
        } else if (Modifier.isProtected(modifiers)) {
            text.append("protected");
        } else if (Modifier.isPrivate(modifiers)) {
            text.append("private");
        } else {
            text.append("package");
        }
        text.append(' ');
        text.append(method.getDeclaringClass().getSimpleName());
        text.append('.');
        text.append(method.getName());
        text.append('(');
        Class<?>[] types = method.getParameterTypes();
        for (int i = 0; i < types.length; i++) {
            if (i > 0) {
                text.append(", ");
            }
            text.append(types[i].getSimpleName());
        }
        text.append(')');
        return text.toString();
    }
}
