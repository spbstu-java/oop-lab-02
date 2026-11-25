import java.lang.reflect.Array;
import java.lang.reflect.Method;

public class Lab2 {
  private static RepeatableService service = new RepeatableService();

  public static void main(String[] args) throws ReflectiveOperationException {
    // RepeatableService service = new RepeatableService();

    invoke(service, "get");
    invoke(service, "add");
    invoke(service, "printProtected");
    invoke(service, "inspectProtected");
    invoke(service, "calculatePrivate");
    invoke(service, "printPrivate");
  }

  private static void invoke(Object target, String name) throws ReflectiveOperationException {
    boolean found = false;

    for (Method method : target.getClass().getDeclaredMethods()) {
      if (!method.getName().equals(name) || !method.isAnnotationPresent(Repeat.class)) {
        continue;
      }

      found = true;

      Class<?>[] types = method.getParameterTypes();
      Object[] arguments = new Object[types.length];

      for (int i = 0; i < types.length; i++) {
        arguments[i] = createArgument(types[i]);
      }

      method.setAccessible(true);
      method.invoke(target, arguments);
    }

    if (!found) {
      throw new NoSuchMethodException(name);
    }
  }

  private static Object createArgument(Class<?> type) throws ReflectiveOperationException {
    if (type == String.class)
      return "sample";
    if (type == boolean.class || type == Boolean.class)
      return false;
    if (type == char.class || type == Character.class)
      return 'A';
    if (type == int.class || type == Integer.class)
      return 0;
    if (type == double.class || type == Double.class)
      return 0.0;
    if (type.isArray())
      return Array.newInstance(type.getComponentType(), 0);
    if (type.isEnum()) {
      Object[] constants = type.getEnumConstants();

      return constants[0];
    }

    var constructor = type.getDeclaredConstructor();

    constructor.setAccessible(true);

    return constructor.newInstance();
  }
}
