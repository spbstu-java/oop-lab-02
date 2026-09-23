import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class Main {
    public static void main(String[] args)
    {
        Car car = new Car();
        try
        {
            invokeAnnotatedMethods(car);
        } catch (Exception e)
        {
            System.err.println("Ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void invokeAnnotatedMethods(Object object) throws Exception
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
            {
                args[i] = createArgument(paramTypes[i]);
            }
            for (int i = 0; i < count; i++)
            {
                method.invoke(object, args);
            }
        }
    }

    private static Object createArgument(Class<?> type) throws Exception
    {
        if (type == String.class) return "АИ-95";
        if (type == int.class || type == Integer.class) return 200;
        if (type == double.class || type == Double.class) return 50.0;
        if (type == boolean.class || type == Boolean.class) return true;
        if (type == char.class || type == Character.class) return 'A';
        if (type == long.class || type == Long.class) return 0L;
        if (type == float.class || type == Float.class) return 0.0f;
        try
        {
            return type.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException e)
        {
            throw new IllegalArgumentException(
                    "Невозможно создать аргумент типа '" + type.getSimpleName() + "'. " +
                            "Добавьте в этот класс публичный конструктор без параметров " +
                            "или добавьте явную обработку этого типа в метод createArgument()."
            );
        }
    }
}