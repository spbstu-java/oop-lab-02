package domain;

import annotation.Repeat;

import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("unused")
public class SampleService {

    private final List<String> journal = new ArrayList<>();

  

    public String publicHello(String name) {
        return "Hello, " + name;
    }

    @Repeat(2)
    public int publicSum(int a, int b) {
        return a + b;
    }

    public List<String> getJournal() {
        return List.copyOf(journal);
    }



    @Repeat(2)
    protected String greet(String name) {
        return "Привет, " + name + "!";
    }

    @Repeat(3)
    protected int multiply(int a, int b) {
        return a * b;
    }

    protected double halve(double value) {
        return value / 2;
    }



    @Repeat(1)
    private String describePerson(Person person, boolean verbose) {
        return verbose ? person.name() + ", возраст " + person.age() : person.name();
    }

    @Repeat(4)
    private double average(double[] values) {
        if (values.length == 0) {
            return 0;
        }
        double sum = 0;
        for (double value : values) {
            sum += value;
        }
        return sum / values.length;
    }

    @Repeat(2)
    private void writeToJournal(String message, List<String> tags) {
        journal.add(message + " " + tags);
    }
}