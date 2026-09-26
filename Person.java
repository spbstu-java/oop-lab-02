public class Person {
    private final String name;

    public Person() {
        this.name = "Петя";
    }

    public Person(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Человек с именем " + name;
    }
}
