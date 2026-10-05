package org.laba;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class Main {

  public static void main(String[] args) {

    Hero hero = new Hero("Arthas", 10);

    Class<?> heroClass = hero.getClass();

    Method[] methods = heroClass.getDeclaredMethods();

    for (Method method : methods) {

      // Проверяем наличие аннотации Repeat
      Repeat annotation = method.getAnnotation(Repeat.class);

      if (annotation == null) {
        continue;
      }

      int modifiers = method.getModifiers();

      if (Modifier.isPublic(modifiers)) {
        continue;
      }

      // Получаем количество повторений из аннотации
      int count = annotation.value();

      // Получаем типы параметров метода
      Class<?>[] parameterTypes = method.getParameterTypes();

      // Создаем массив аргументов нужного размера
      Object[] parameters = new Object[parameterTypes.length];

      for (int i = 0; i < parameterTypes.length; i++) {
        parameters[i] = createArgument(parameterTypes[i]);
      }

      // Разрешаем вызов private/protected метода
      method.setAccessible(true);

      System.out.println("\nВызов метода: " + method.getName() +
                         ", количество повторений: " + count);

      for (int i = 0; i < count; i++) {
        try {
          method.invoke(hero, parameters);
        } catch (Exception e) {
          System.out.println("Ошибка при вызове " + method.getName());
          e.printStackTrace();
        }
      }
    }
  }

  private static Object createArgument(Class<?> type) {

    if (type == String.class) {
      return "Test";
    }

    if (type == int.class || type == Integer.class) {
      return 10;
    }

    if (type == double.class || type == Double.class) {
      return 1.5;
    }

    if (type == long.class || type == Long.class) {
      return 100L;
    }

    if (type == boolean.class || type == Boolean.class) {
      return true;
    }

    if (type == float.class || type == Float.class) {
      return 2.5f;
    }

    if (type == short.class || type == Short.class) {
      return (short)5;
    }

    if (type == byte.class || type == Byte.class) {
      return (byte)1;
    }

    if (type == char.class || type == Character.class) {
      return 'A';
    }

    // Для пользовательских классов
    try {
      Constructor<?> constructor = type.getDeclaredConstructors()[0];

      constructor.setAccessible(true);

      Class<?>[] parameterTypes = constructor.getParameterTypes();

      Object[] parameters = new Object[parameterTypes.length];

      for (int i = 0; i < parameterTypes.length; i++) {
        parameters[i] = createArgument(parameterTypes[i]);
      }

      return constructor.newInstance(parameters);

    } catch (Exception e) {
      throw new IllegalArgumentException(
          "Не удалось создать аргумент типа " + type.getName(), e);
    }
  }
}
