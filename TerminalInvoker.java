import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Scanner;

public final class TerminalInvoker {
  private TerminalInvoker() {}

  public static void invoke(Object target, String methodName, Scanner input) throws ReflectiveOperationException {
    Method method = findMethod(target.getClass(), methodName);
    Object[] arguments = readArguments(method, input);
    method.setAccessible(true);
    method.invoke(target, arguments);
  }

  private static Method findMethod(Class<?> type, String name) throws NoSuchMethodException {
    return Arrays.stream(type.getDeclaredMethods())
        .filter(method -> method.getName().equals(name))
        .findFirst()
        .orElseThrow(() -> new NoSuchMethodException(name));
  }

  private static Object[] readArguments(Method method, Scanner input) {
    System.out.println("Method: " + method.getName());
    Class<?>[] types = method.getParameterTypes();
    Object[] arguments = new Object[types.length];
    for (int i = 0; i < types.length; i++) {
      printPrompt(types[i], i + 1);
      arguments[i] = ArgumentParser.parse(types[i], input.nextLine());
    }
    return arguments;
  }

  private static void printPrompt(Class<?> type, int position) {
    System.out.println("Parameter " + position + " (" + type.getTypeName() + "):");
    Class<?> elementType = type.isArray() ? type.getComponentType() : type;
    if (elementType.isEnum()) {
      Object[] options = elementType.getEnumConstants();
      for (int i = 0; i < options.length; i++) {
        System.out.println((i + 1) + ": " + options[i]);
      }
    }
    if (type.isArray()) {
      System.out.println("Enter elements separated by spaces (empty line for empty array):");
    }
  }
}
