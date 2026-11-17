import java.util.Scanner;

public class Lab2 {
  public static void main(String[] args) throws ReflectiveOperationException {
    try (Scanner input = new Scanner(System.in)) {
      RepeatableService service = new RepeatableService();
      TerminalInvoker.invoke(service, "get", input);
      TerminalInvoker.invoke(service, "add", input);
      TerminalInvoker.invoke(service, "printProtected", input);
      TerminalInvoker.invoke(service, "inspectProtected", input);
      TerminalInvoker.invoke(service, "calculatePrivate", input);
      TerminalInvoker.invoke(service, "printPrivate", input);
    }
  }
}
