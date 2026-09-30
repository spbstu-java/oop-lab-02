package com.example.lab02.service;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class ArgumentFactory
{
    private static final int DEFAULT_NUMBER = 5;
    private static final double DEFAULT_DECIMAL = 2.5;
    private static final String DEFAULT_TEXT = "test";
    private static final char DEFAULT_CHAR = 'a';
    private static final boolean DEFAULT_FLAG = true;
    private static final int EMPTY_SIZE = 0;

    private final Map<Class<?>, Supplier<Object>> suppliers = new HashMap<>();

    private final Deque<Class<?>> typesInProgress = new ArrayDeque<>();

    public ArgumentFactory()
    {
        registerSimpleTypes();
        registerCollections();
    }

    public Object[] createArgs(Class<?>[] types)
    {
        return Arrays.stream(types).map(this::createValue).toArray();
    }

    public Object createValue(Class<?> type)
    {
        Supplier<Object> supplier = suppliers.get(type);
        if (supplier != null)
        {
            return supplier.get();
        }
        if (type.isArray())
        {
            return Array.newInstance(type.getComponentType(), EMPTY_SIZE);
        }
        if (type.isEnum() && type.getEnumConstants().length > 0)
        {
            return type.getEnumConstants()[0];
        }
        return createByConstructor(type);
    }

    private void registerSimpleTypes()
    {
        register(DEFAULT_TEXT, String.class);
        register(DEFAULT_FLAG, boolean.class, Boolean.class);
        register(DEFAULT_CHAR, char.class, Character.class);
        register((byte) DEFAULT_NUMBER, byte.class, Byte.class);
        register((short) DEFAULT_NUMBER, short.class, Short.class);
        register(DEFAULT_NUMBER, int.class, Integer.class);
        register((long) DEFAULT_NUMBER, long.class, Long.class);
        register((float) DEFAULT_DECIMAL, float.class, Float.class);
        register(DEFAULT_DECIMAL, double.class, Double.class);
    }

    private void registerCollections()
    {
        suppliers.put(List.class, ArrayList::new);
        suppliers.put(Collection.class, ArrayList::new);
        suppliers.put(Set.class, HashSet::new);
        suppliers.put(Map.class, HashMap::new);
    }

    private void register(Object value, Class<?>... types)
    {
        for (Class<?> type : types)
        {
            suppliers.put(type, () -> value);
        }
    }

    private Object createByConstructor(Class<?> type)
    {
        if (typesInProgress.contains(type))
        {
            throw new IllegalArgumentException(
                    "Циклическая зависимость конструкторов для типа " + type.getName());
        }
        Constructor<?> constructor = findSimplestConstructor(type);
        typesInProgress.push(type);
        try
        {
            constructor.setAccessible(true);
            Object[] args = createArgs(constructor.getParameterTypes());
            return constructor.newInstance(args);
        }
        catch (ReflectiveOperationException e)
        {
            throw new IllegalArgumentException(
                    "Нельзя создать аргумент типа " + type.getName(), e);
        }
        finally
        {
            typesInProgress.pop();
        }
    }

    private Constructor<?> findSimplestConstructor(Class<?> type)
    {
        Constructor<?>[] constructors = type.getDeclaredConstructors();
        if (constructors.length == 0)
        {
            throw new IllegalArgumentException("У типа " + type.getName() + " нет конструктора");
        }
        Constructor<?> simplest = constructors[0];
        for (Constructor<?> candidate : constructors)
        {
            if (candidate.getParameterCount() < simplest.getParameterCount())
            {
                simplest = candidate;
            }
        }
        return simplest;
    }
}
