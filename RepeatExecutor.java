public final class RepeatExecutor {
  private static final StackWalker WALKER = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);

  public static void run(Runnable body) {
    StackWalker.StackFrame caller = WALKER.walk(frames -> frames.skip(1).findFirst().orElseThrow());

    try {
      Repeat repeat = caller.getDeclaringClass()
          .getDeclaredMethod(caller.getMethodName(), caller.getMethodType().parameterArray())
          .getAnnotation(Repeat.class);

      int count = repeat == null ? 1 : repeat.value();

      if (count < 0) {
        throw new IllegalArgumentException("Negative repeat: " + count);
      }

      for (int i = 0; i < count; i++) {
        body.run();
      }
    } catch (NoSuchMethodException e) {
      throw new IllegalStateException("Lost method to call", e);
    }
  }
}
