import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.function.Supplier;

public class Main {

    private static final Map<Class<?>, Supplier<?>> VALUES =
            Map.ofEntries(
                    Map.entry(String.class, () -> "АИ-95"),
                    Map.entry(int.class, () -> 200),
                    Map.entry(double.class, () -> 10.5),
                    Map.entry(boolean.class, () -> true)
            );

    public static void main(String[] args) throws ReflectiveOperationException
    {
        Car car = new Car();
        invokeAnnotatedMethods(car);
    }

    public static void invokeAnnotatedMethods(Object object) throws ReflectiveOperationException
    {
        Class<?> type = object.getClass();
        for (Method method : type.getDeclaredMethods())
        {
            Repeat repeat = method.getAnnotation(Repeat.class);
            if (repeat == null)
            {
                continue;
            }
            int modifiers = method.getModifiers();
            if (!Modifier.isProtected(modifiers) && !Modifier.isPrivate(modifiers))
            {
                continue;
            }
            int repetitions = repeat.value();
            if (repetitions < 0)
            {
                throw new IllegalArgumentException("Отрицательное число повторений у метода: " + method.getName());
            }
            if (repetitions == 0)
            {
                continue;
            }
            method.setAccessible(true);
            Class<?>[] parameterTypes = method.getParameterTypes();
            Object[] arguments = new Object[parameterTypes.length];
            for (int i = 0; i < parameterTypes.length; i++)
            {
                arguments[i] = createArgument(parameterTypes[i]);
            }
            for (int i = 0; i < repetitions; i++)
            {
                method.invoke(object, arguments);
            }
        }
    }

    private static Object createArgument(Class<?> type) throws ReflectiveOperationException
    {
        Supplier<?> supplier = VALUES.get(type);
        if (supplier != null)
        {
            return supplier.get();
        }
        return type.getConstructor().newInstance();
    }
}
