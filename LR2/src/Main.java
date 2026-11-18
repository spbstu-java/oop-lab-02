public class Main {
    public static void main(String[] args) {
        Knitter knitter = new Knitter("Мастерская «Петля»");
        new AnnotatedMethodCaller().invokeAnnotated(knitter);
    }
}
