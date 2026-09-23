package com.example.lab02.model;

import com.example.lab02.annotation.Repeat;
import java.util.List;
import java.util.logging.Logger;

public class Sample
{
    private static final Logger LOGGER = Logger.getLogger(Sample.class.getName());

    @Repeat(2)
    public void hello(String name)
    {
        LOGGER.info("Публичный метод hello, имя: " + name);
    }

    public int add(int first, int second)
    {
        return first + second;
    }

    @Repeat(3)
    protected void printText(String text)
    {
        LOGGER.info("Защищенный метод, текст: " + text);
    }

    @Repeat(2)
    protected double multiply(int number, double factor)
    {
        return number * factor;
    }

    protected void skipProtected(String text)
    {
        LOGGER.info("Это сообщение не должно появиться: " + text);
    }

    @Repeat(4)
    private void printNumbers(int first, int second, int third)
    {
        LOGGER.info("Приватный метод, числа: " + first + ", " + second + ", " + third);
    }

    @Repeat(1)
    private void printList(String title, boolean flag, List<String> items)
    {
        LOGGER.info("Приватный метод, список: " + title + ", флаг=" + flag
                + ", размер=" + items.size());
    }

    private void skipPrivate(long value)
    {
        LOGGER.info("Это сообщение не должно появиться: " + value);
    }
}
