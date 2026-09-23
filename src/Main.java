import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class Main {
    public static void main(String[] args)
    {
        Car car = new Car();
        invokeAnnotatedMethods(car);
    }

    public static void invokeAnnotatedMethods(Object object)
    {
        for (Method method : object.getClass().getDeclaredMethods())
        {
            Repeat repeat = method.getAnnotation(Repeat.class);
            if (repeat == null)
                continue;
            int mod = method.getModifiers();
            if (!Modifier.isProtected(mod) && !Modifier.isPrivate(mod))
                continue;
            int count = repeat.value();
            if (count <= 0)
                continue;
            method.setAccessible(true);
            Class<?>[] paramTypes = method.getParameterTypes();
            Object[] args = new Object[paramTypes.length];
            for (int i = 0; i < paramTypes.length; i++)
                args[i] = createArgument(paramTypes[i]);
            for (int i = 0; i < count; i++)
            {
                try
                {
                    method.invoke(object, args);
                } catch (ReflectiveOperationException e)
                {
                    throw new RuntimeException("Не удалось вызвать метод " + method.getName(), e);
                }
            }
        }
    }

    private static Object createArgument(Class<?> type)
    {
        if (type == String.class)
            return "АИ-95";
        if (type == boolean.class || type == Boolean.class)
            return true;
        if (type == byte.class || type == Byte.class)
            return (byte) 1;
        if (type == short.class || type == Short.class)
            return (short) 2;
        if (type == int.class || type == Integer.class)
            return 200;
        if (type == long.class || type == Long.class)
            return 0L;
        if (type == float.class || type == Float.class)
            return 0.0f;
        if (type == double.class || type == Double.class)
            return 50.0;
        if (type == char.class || type == Character.class)
            return 'A';
        try
        {
            Constructor<?> constructor = type.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (ReflectiveOperationException e)
        {
            throw new IllegalArgumentException(
                    "Невозможно создать аргумент типа '" + type.getSimpleName() + "'. " +
                            "Добавьте в этот класс конструктор без параметров или добавьте явную обработку этого типа."
            );
        }
    }
}