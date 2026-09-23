import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class Main
{
    private static final int MAX_DEPTH = 10;

    public static void main(String[] args)
    {
        try
        {
            Car car = new Car();
            invokeAnnotatedMethods(car);
        } catch (RuntimeException e)
        {
            System.err.println("Ошибка выполнения: " + e.getMessage());
            e.printStackTrace();
        }
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
        return createArgument(type, 0);
    }

    private static Object createArgument(Class<?> type, int depth)
    {
        if (depth > MAX_DEPTH)
            throw new IllegalArgumentException(
                    "Слишком глубокая цепочка создания объектов: " + type.getName());

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

        if (type.isInterface() || Modifier.isAbstract(type.getModifiers()))
            throw new IllegalArgumentException("Для типа " + type.getName() + " требуется конкретная реализация");

        Constructor<?>[] constructors = type.getDeclaredConstructors();
        if (constructors.length == 0)
            throw new IllegalArgumentException("Нет конструкторов у типа " + type.getName());

        Constructor<?> shortest = constructors[0];
        for (Constructor<?> constructor : constructors)
        {
            if (constructor.getParameterCount() < shortest.getParameterCount())
                shortest = constructor;
        }

        Class<?>[] parameterTypes = shortest.getParameterTypes();
        Object[] arguments = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++)
            arguments[i] = createArgument(parameterTypes[i], depth + 1);

        try
        {
            shortest.setAccessible(true);
            return shortest.newInstance(arguments);
        } catch (ReflectiveOperationException e)
        {
            throw new IllegalArgumentException("Невозможно создать аргумент типа " + type.getName(), e);
        }
    }
}
