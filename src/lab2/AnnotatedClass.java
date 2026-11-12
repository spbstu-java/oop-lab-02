package lab2;

/**
 * Класс с public, protected и private методами (по 3 каждого вида), все методы
 * имеют параметры. Защищённые и приватные методы помечены аннотацией
 * {@code @Repeat}, чтобы их можно было вызывать рефлексией из другого класса
 * столько раз, сколько указывает параметр аннотации.
 */
public class AnnotatedClass {

    // ---------- public методы (не автоматически вызываются) ----------

    public String greet(String name, int hour) {
        return "Привет, " + name + "! Сейчас " + hour + " ч.";
    }

    public int sum(int a, int b) {
        return a + b;
    }

    public void describe(double value) {
        System.out.println("  [public] describe(" + value + ")");
    }

    // ---------- protected методы (аннотированы) ----------

    @Repeat(2)
    protected void log(String message, long id) {
        System.out.println("  [protected] log(message=\"" + message + "\", id=" + id + ")");
    }

    @Repeat(3)
    protected int transform(int base, String suffix, boolean flag) {
        System.out.println("  [protected] transform(base=" + base + ", suffix=\"" + suffix + "\", flag=" + flag + ")");
        return base;
    }

    @Repeat(2)
    protected void applyWrap(Integer factor, double ratio) {
        System.out.println("  [protected] applyWrap(factor=" + factor + ", ratio=" + ratio + ")");
    }

    // ---------- private методы (аннотированы) ----------

    @Repeat(4)
    private int calc(int a, int b) {
        System.out.println("  [private] calc(a=" + a + ", b=" + b + ") -> " + (a - b));
        return a - b;
    }

    @Repeat(2)
    private String secret(String token) {
        System.out.println("  [private] secret(token=\"" + token + "\")");
        return token;
    }

    @Repeat(3)
    private void process(Object payload, long offset, char marker) {
        System.out.println("  [private] process(payload=" + payload + ", offset=" + offset + ", marker='" + marker + "')");
    }
}