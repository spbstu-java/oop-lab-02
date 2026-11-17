import java.lang.reflect.Array;

public final class ArgumentParser {
  private ArgumentParser() {}

  public static Object parse(Class<?> type, String text) {
    if (type == String.class) return text;
    String value = text.trim();
    if (type.isArray()) return parseArray(type.getComponentType(), value);
    if (type.isEnum()) return parseEnum(type, value);
    if (type == int.class || type == Integer.class) return Integer.valueOf(value);
    if (type == long.class || type == Long.class) return Long.valueOf(value);
    if (type == short.class || type == Short.class) return Short.valueOf(value);
    if (type == byte.class || type == Byte.class) return Byte.valueOf(value);
    if (type == double.class || type == Double.class) {
      double number = Double.parseDouble(value);
      if (!Double.isFinite(number)) throw new IllegalArgumentException("Expected finite number");
      return number;
    }
    if (type == float.class || type == Float.class) {
      float number = Float.parseFloat(value);
      if (!Float.isFinite(number)) throw new IllegalArgumentException("Expected finite number");
      return number;
    }
    if (type == boolean.class || type == Boolean.class) {
      if (!value.equalsIgnoreCase("true") && !value.equalsIgnoreCase("false")) {
        throw new IllegalArgumentException("Expected true or false");
      }
      return Boolean.valueOf(value);
    }
    if (type == char.class || type == Character.class) {
      if (text.length() != 1) throw new IllegalArgumentException("Expected one character");
      return text.charAt(0);
    }
    throw new IllegalArgumentException("Unsupported parameter type: " + type.getName());
  }

  private static Object parseArray(Class<?> elementType, String value) {
    String[] elements = value.isEmpty() ? new String[0] : value.split("\\s+");
    Object array = Array.newInstance(elementType, elements.length);
    for (int i = 0; i < elements.length; i++) {
      Array.set(array, i, parse(elementType, elements[i]));
    }
    return array;
  }

  private static Object parseEnum(Class<?> type, String value) {
    Object[] options = type.getEnumConstants();
    int index = Integer.parseInt(value) - 1;
    if (index < 0 || index >= options.length) throw new IllegalArgumentException("Invalid enum option");
    return options[index];
  }
}
