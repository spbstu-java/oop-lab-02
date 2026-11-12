package lab2;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        forceConsoleUtf8();

        AnnotatedClass instance = new AnnotatedClass();

        System.out.println("=== Демонстрация: автоматический вызов аннотированных"
                + " protected/private методов ===\n");

        try {
            AnnotatedMethodInvoker.invokeAnnotatedProtectedAndPrivate(AnnotatedClass.class, instance);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Ошибка рефлексионного вызова", e);
        }

        System.out.println("=== Вызов одного из public методов (для контроля, что класс жив) ===");
        System.out.println(instance.greet("Мир", 12));
    }

    /**
     * Принудительно переводит {@code System.out} на UTF-8, чтобы кириллица
     * корректно отображалась независимо от кодовой страницы консоли Windows.
     */
    private static void forceConsoleUtf8() {
        try {
            java.io.Console console = System.console();
            String charset = (console != null)
                    ? console.charset().name()
                    : StandardCharsets.UTF_8.name();

            PrintStream utf8 = new PrintStream(
                    new FileOutputStream(FileDescriptor.out), true, charset);
            System.setOut(utf8);
        } catch (UnsupportedEncodingException ignored) {
            // Недостижимая ветка: все кодовые страницы консоли поддерживаются.
        }
    }

}