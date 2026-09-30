package main;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Comparator;

// Инвокер: вызывает все аннотированные non-public методы
public class Invoker {
    public static void invokeAnnotated(Object target) {
        for (Method m : target.getClass().getDeclaredMethods()) {
            Repeat rep = m.getAnnotation(Repeat.class);
            int mod = m.getModifiers();
            if (rep == null || !(Modifier.isProtected(mod) || Modifier.isPrivate(mod))) continue;

            m.setAccessible(true);
            for (int i = 1; i <= rep.value(); i++) {
                try {
                    Object[] args = Arrays.stream(m.getParameterTypes()).map(Invoker::arg).toArray();
                    System.out.printf("%s #%d -> %s%n", m.getName(), i, m.invoke(target, args));
                } catch (Exception e) {
                    System.out.printf("%s: ошибка %s%n", m.getName(), e);
                    break;
                }
            }
        }
    }

    // значение-заглушка под любой тип параметра, без null
    private static Object arg(Class<?> t) {
        return switch (Arguments.of(t)) {
            case BOOLEAN -> true;
            case BYTE    -> (byte) 1;
            case SHORT   -> (short) 1;
            case INT     -> 3;
            case LONG    -> 1L;
            case FLOAT   -> 1.5f;
            case DOUBLE  -> 2.5;
            case CHAR    -> '*';
            case STRING  -> "text";
            case ARRAY   -> Array.newInstance(t.getComponentType(), 0); // и для varargs
            case ENUM    -> t.getEnumConstants()[0];
            case OBJECT  -> newObject(t);
        };
    }

    // испр по ревью - можно брать конструктор с наименьшим числом параметров, аргументы аналогично методам.
    private static Object newObject(Class<?> t) {
        Constructor<?> c = Arrays.stream(t.getDeclaredConstructors())
                .min(Comparator.comparingInt(Constructor::getParameterCount))
                .orElseThrow(() -> new IllegalArgumentException("no constructors: " + t.getName()));
        try {
            c.setAccessible(true);
            return c.newInstance(Arrays.stream(c.getParameterTypes()).map(Invoker::arg).toArray());
        } catch (ReflectiveOperationException e) {
            throw new IllegalArgumentException("impossible to create " + t.getName(), e);
        }
    }
}