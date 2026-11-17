import java.util.Arrays;

public class RepeatableService {
  public enum Status {
    READY, DONE
  }

  private Status status = Status.READY;
  private int completedCalls = 0;

  private void logState(String message) {
    System.out.println(message + " | status = " + this.status + ", completedCalls = " + this.completedCalls);
  }

  @Repeat(3)
  public void get(String text) {
    RepeatExecutor.run(() -> {
      completedCalls++;
      logState("📄 get: " + text);
    });
  }

  @Repeat
  public void add(int first, int second) {
    RepeatExecutor.run(() -> {
      completedCalls++;
      logState("✍️ add: " + (first + second));
    });
  }

  public void runPrivateWork() {
    calculatePrivate(1.0, 1, 'A');
    printPrivate(new String[] { "ABC", "DFG" }, new int[] { 20, 30, 52, 57 });
  }

  @Repeat
  protected void printProtected(String text, int number) {
    RepeatExecutor.run(() -> {
      completedCalls++;
      logState("🔒📄 printProtected: " + text + ", " + number);
    });
  }

  @Repeat(3)
  protected void inspectProtected(String message, Status requestedStatus, boolean enabled) {
    RepeatExecutor.run(() -> {
      completedCalls++;
      status = status == Status.READY ? Status.DONE : Status.READY;
      logState("🔒🔎 inspectProtected: " + message + ", requestedStatus=" + requestedStatus
          + ", enabled=" + enabled);
    });
  }

  @Repeat(4)
  private void calculatePrivate(double number, Integer count, char symbol) {
    RepeatExecutor.run(() -> {
      completedCalls++;
      logState("🏡🧮 calculatePrivate: " + number + ", " + count + ", " + symbol);
    });
  }

  @Repeat
  private void printPrivate(String[] texts, int[] numbers) {
    RepeatExecutor.run(() -> {
      logState("🏡📄 printPrivate: " + Arrays.toString(texts) + ", " + Arrays.toString(numbers));
    });
  }
}
