import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Comparator;

public class Main {
    public static void main(String[] args) {
        invokeRepeatedly(new Workshop());
    }

    private static void invokeRepeatedly(Object target) {
        ArgumentFactory factory = new ArgumentFactory();
        Method[] methods = target.getClass().getDeclaredMethods();
        Arrays.sort(methods, Comparator.comparing(Method::getName));

        for (Method method : methods) {
            Repeat repeat = method.getAnnotation(Repeat.class);
            int modifiers = method.getModifiers();
            if (repeat == null || !(Modifier.isProtected(modifiers) || Modifier.isPrivate(modifiers))) {
                continue;
            }

            try {
                if (!method.trySetAccessible()) {
                    throw new IllegalAccessException("Нет доступа к методу");
                }
                for (int call = 0; call < repeat.value(); call++) {
                    Object[] arguments = factory.createArguments(method.getParameterTypes());
                    method.invoke(target, arguments);
                }
            } catch (InvocationTargetException e) {
                System.err.printf("%s: метод завершился с ошибкой: %s%n", method.getName(), e.getCause());
            } catch (ReflectiveOperationException e) {
                System.err.printf("%s: %s%n", method.getName(), e.getMessage());
            }
        }
    }
}
