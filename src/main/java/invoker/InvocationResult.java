package invoker;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

public record InvocationResult(Method method, int callNumber, Object[] arguments, Object returnValue) {

    public String describe() {
        String visibility = Modifier.isPrivate(method.getModifiers()) ? "private" : "protected";
        String result = method.getReturnType() == void.class ? "void" : String.valueOf(returnValue);
        return String.format("%s %s, вызов %d, аргументы %s -> %s",
                visibility, method.getName(), callNumber, Arrays.deepToString(arguments), result);
    }
}