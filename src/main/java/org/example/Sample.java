package org.example;

import java.util.ArrayList;

public class Sample {
    @Repeat(5)
    public void greet(String name) {
        System.out.println("public greet: name='" + name + "'");
    }

    public int sum(int a, int b) {
        return a + b;
    }

    @Repeat(2)
    protected void log(String message, int level) {
        System.out.println("protected log: message='" + message + "', level=" + level);
    }

    @Repeat(3)
    protected void toggle(boolean flag) {
        System.out.println("protected toggle: flag=" + flag);
    }

    protected double scale(double value, double factor) {
        return value * factor;
    }

    @Repeat(1)
    private void store(long id, double[] values, ArrayList<String> tags) {
        System.out.println("private store: id=" + id + ", values=" + values.length + ", tags=" + tags);
    }

    @Repeat(2)
    private void report(StringBuilder text, int code) {
        System.out.println("private report: text='" + text + "', code=" + code);
    }

    private String join(String first, String second) {
        return first + second;
    }
}
