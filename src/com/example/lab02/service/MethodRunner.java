package com.example.lab02.service;

import com.example.lab02.annotation.Repeat;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Comparator;
import java.util.logging.Logger;

public class MethodRunner
{
    private static final Logger LOGGER = Logger.getLogger(MethodRunner.class.getName());

    private final ArgumentFactory factory;

    public MethodRunner(ArgumentFactory factory)
    {
        this.factory = factory;
    }

    public void runAll(Object target)
    {
        Arrays.stream(target.getClass().getDeclaredMethods())
                .filter(this::isNeeded)
                .sorted(Comparator.comparing(Method::getName))
                .forEach(method -> runMethod(target, method));
    }

    private boolean isNeeded(Method method)
    {
        int modifiers = method.getModifiers();
        boolean hiddenMethod = Modifier.isProtected(modifiers) || Modifier.isPrivate(modifiers);
        return hiddenMethod && method.isAnnotationPresent(Repeat.class);
    }

    private void runMethod(Object target, Method method)
    {
        int times = method.getAnnotation(Repeat.class).value();
        method.setAccessible(true);
        LOGGER.info("Вызываем метод " + method.getName() + ", количество вызовов: " + times);
        for (int i = 1; i <= times; i++)
        {
            callOnce(target, method);
        }
    }

    private void callOnce(Object target, Method method)
    {
        try
        {
            Object[] args = factory.createArgs(method.getParameterTypes());
            Object result = method.invoke(target, args);
            if (method.getReturnType() != void.class)
            {
                LOGGER.info("Метод " + method.getName() + " вернул: " + result);
            }
        }
        catch (InvocationTargetException e)
        {
            LOGGER.warning("Метод " + method.getName() + " выбросил исключение: " + e.getCause());
        }
        catch (IllegalAccessException | IllegalArgumentException e)
        {
            LOGGER.warning("Метод " + method.getName() + " пропущен: " + e.getMessage());
        }
    }
}
