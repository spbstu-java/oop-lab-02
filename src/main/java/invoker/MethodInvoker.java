package invoker;

import annotation.Repeat;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public final class MethodInvoker {

    private final ArgumentFactory argumentFactory;

    public MethodInvoker() {
        this(new ArgumentFactory());
    }

    public MethodInvoker(ArgumentFactory argumentFactory) {
        this.argumentFactory = Objects.requireNonNull(argumentFactory, "Фабрика аргументов не может быть null");
    }

    public List<InvocationResult> invokeAnnotated(Object target) {
        Objects.requireNonNull(target, "Целевой объект не может быть null");

        List<InvocationResult> results = new ArrayList<>();
        for (Method method : findAnnotatedMethods(target.getClass())) {
            int times = method.getAnnotation(Repeat.class).value();
            if (times < 0) {
                throw new MethodInvocationException(
                        "Отрицательное число вызовов (" + times + ") у метода " + method.getName());
            }
            method.setAccessible(true);
            for (int call = 1; call <= times; call++) {
                Object[] arguments = argumentFactory.createArguments(method);
                Object returnValue = invoke(target, method, arguments);
                results.add(new InvocationResult(method, call, arguments, returnValue));
            }
        }
        return results;
    }

    private static List<Method> findAnnotatedMethods(Class<?> type) {
        return Arrays.stream(type.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(Repeat.class))
                .filter(method -> Modifier.isProtected(method.getModifiers())
                        || Modifier.isPrivate(method.getModifiers()))
                .sorted(Comparator.comparing(Method::getName).thenComparing(Method::toGenericString))
                .toList();
    }

    private static Object invoke(Object target, Method method, Object[] arguments) {
        try {
            return method.invoke(target, arguments);
        } catch (IllegalAccessException e) {
            throw new MethodInvocationException("Нет доступа к методу " + method.getName(), e);
        } catch (InvocationTargetException e) {
            throw new MethodInvocationException(
                    "Метод " + method.getName() + " выбросил исключение: " + e.getCause(), e.getCause());
        }
    }
}