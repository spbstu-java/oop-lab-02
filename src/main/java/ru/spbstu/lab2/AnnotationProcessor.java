package main.java.ru.spbstu.lab2;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/** Универсальный обработчик аннотированных protected/private методов */
public class AnnotationProcessor {
    public void process(Object object) {
        if (object == null) {
            throw new IllegalArgumentException("Обрабатываемый объект не должен быть null.");
        }

        // Получаем все методы, объявленные в классе объекта
        Method[] methods = object.getClass().getDeclaredMethods();

        for (Method method : methods) {
            Repeat repeat = method.getAnnotation(Repeat.class);
            if (repeat == null) {
                continue;
            }

            int modifiers = method.getModifiers();
            if (!Modifier.isProtected(modifiers) && !Modifier.isPrivate(modifiers)) {
                continue;
            }

            if (repeat.value() < 0) {
                throw new IllegalArgumentException("Число повторений не может быть отрицательным.");
            }

            // Открываем доступ к защищённому или приватному методу
            method.setAccessible(true);

            // Число и типы параметров определяются во время выполнения
            Object[] arguments = createArguments(method);

            System.out.println("\nМетод " + method.getName()
                    + " будет вызван " + repeat.value() + " раз(а).");

            for (int i = 0; i < repeat.value(); i++) {
                try {
                    method.invoke(object, arguments);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException("Ошибка вызова метода " + method.getName(), e);
                }
            }
        }
    }

    private Object[] createArguments(Method method) {
        Class<?>[] types = method.getParameterTypes();
        Object[] arguments = new Object[types.length];

        for (int i = 0; i < types.length; i++) {
            arguments[i] = createDefaultValue(types[i]);
        }
        return arguments;
    }

    /** Создаёт ненулевые значения для поддерживаемых типов параметров */
    private Object createDefaultValue(Class<?> type) {
        if (type == int.class || type == Integer.class) return 10;
        if (type == double.class || type == Double.class) return 4.5;
        if (type == long.class || type == Long.class) return 100L;
        if (type == float.class || type == Float.class) return 2.5f;
        if (type == boolean.class || type == Boolean.class) return true;
        if (type == char.class || type == Character.class) return 'A';
        if (type == byte.class || type == Byte.class) return (byte) 1;
        if (type == short.class || type == Short.class) return (short) 2;
        if (type == String.class) return "Тестовая строка";

        throw new IllegalArgumentException("Не настроено создание значения типа: " + type.getName());
    }
}
