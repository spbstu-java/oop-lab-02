import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public final class Main {
    public static void main(String[] args) {
        invokeAnnotatedMethods(new DemoClass());
    }

    public static void invokeAnnotatedMethods(Object obj) {
        var methods = obj.getClass().getDeclaredMethods();

        for (Method method : methods) {
            // Проверка существования такой аннотации
            if (!method.isAnnotationPresent(RunCount.class)) continue;
            // Пропускаем публичные методы
            if (Modifier.isPublic(method.getModifiers())) continue;

            var params = new Object[method.getParameterCount()];
            for (var j = 0; j < params.length; j++) {
                params[j] = instantiateObject(method.getParameterTypes()[j]);
            }

            for (Object param : params) {
                if (param == null) {
                    System.out.println("Null object for parameter in method: " + method.getName());
                    continue;
                }
            }

            try {
                method.setAccessible(true);

                for (var i = 0; i < method.getAnnotation(RunCount.class).value(); i++) {
                    method.invoke(obj, params);
                    System.out.println("Invoke: " + method.getName() + " run: " + (i + 1));
                }
            } catch (Exception e) {
                System.out.println("Error method: " + method.getName() + " message: " + e.getMessage());
            }
        }
    }
    private static Object instantiateObject(Class<?> type) {
        try {
            if (type.isPrimitive()) return getDefaultValue(type);

            final var constructor = type.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (Exception e) {
            System.out.println("Failed to instantiate parameter of type: " + type.getName());
            return null;
        }
    }

    private static Object getDefaultValue(Class<?> type) {
        if (type.isArray()) return Array.newInstance(type.getComponentType(), 0);

        return switch (type.getName()) {
            case "int" -> 0;
            case "boolean" -> false;
            case "double" -> 0.0;
            case "long" -> 0L;
            case "float" -> 0.0f;
            case "short" -> (short) 0;
            case "byte" -> (byte) 0;
            case "char" -> '\u0000';
            case "java.lang.String" -> "str";
            default -> null;
        };
    }
}