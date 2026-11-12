package main.java.ru.spbstu.lab2;

public class Main {
    public static void main(String[] args) {
        Worker worker = new Worker();

        System.out.println("Лабораторная работа № 2 - Аннотации");
        System.out.println("\nПубличные методы");
        worker.showMessage("Демонстрация публичного метода");
        worker.calculateSum(5, 7);

        System.out.println("\nАннотированные protected/private методы");
        AnnotationProcessor processor = new AnnotationProcessor();
        processor.process(worker);

        System.out.println("\nПрограмма завершена.");
    }
}
